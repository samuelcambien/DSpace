/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.submit.step;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.dspace.app.util.SubmissionInfo;
import org.dspace.app.util.Util;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.*;
import org.dspace.core.Context;
import org.dspace.submit.AbstractProcessingStep;
import org.dspace.submit.inputForms.components.*;
import org.dspace.submit.inputForms.components.MetadataField;
import org.dspace.utils.DSpace;

/**
 * Describe step for DSpace submission process. Handles the gathering of
 * descriptive information (i.e. metadata) for an item being submitted into
 * DSpace.
 * <P>
 * This class performs all the behind-the-scenes processing that
 * this particular step requires.  This class's methods are utilized
 * by both the JSP-UI and the Manakin XML-UI
 * <P>
 *
 * @see org.dspace.app.util.SubmissionConfig
 * @see org.dspace.app.util.SubmissionStepConfig
 * @see org.dspace.submit.AbstractProcessingStep
 *
 * @author Tim Donohue
 * @version $Revision$
 */
public class DescribeStep extends AbstractProcessingStep
{
    /** log4j logger */
    private static Logger log = Logger.getLogger(DescribeStep.class);

    /** hash of all submission forms details */
    private static InputFormMap inputFormsMap;

    /***************************************************************************
     * STATUS / ERROR FLAGS (returned by doProcessing() if an error occurs or
     * additional user interaction may be required)
     *
     * (Do NOT use status of 0, since it corresponds to STATUS_COMPLETE flag
     * defined in the JSPStepManager class)
     **************************************************************************/
    // user requested an extra input field to be displayed
    public static final int STATUS_MORE_INPUT_REQUESTED = 1;

    // there were required fields that were not filled out
    public static final int STATUS_MISSING_REQUIRED_FIELDS = 2;
    

    /** Constructor */
    public DescribeStep() throws ServletException
    {
        //load the Input forms map
        inputFormsMap = new DSpace().getServiceManager().getServiceByName(InputFormMap.class.getName(), InputFormMap.class);

    }

   

    /**
     * Do any processing of the information input by the user, and/or perform
     * step processing (if no user interaction required)
     * <P>
     * It is this method's job to save any data to the underlying database, as
     * necessary, and return error messages (if any) which can then be processed
     * by the appropriate user interface (JSP-UI or XML-UI)
     * <P>
     * NOTE: If this step is a non-interactive step (i.e. requires no UI), then
     * it should perform *all* of its processing in this method!
     *
     * @param context
     *            current DSpace context
     * @param request
     *            current servlet request object
     * @param response
     *            current servlet response object
     * @param subInfo
     *            submission info object
     * @return Status or error flag which will be processed by
     *         doPostProcessing() below! (if STATUS_COMPLETE or 0 is returned,
     *         no errors occurred!)
     */
    public int doProcessing(Context context, HttpServletRequest request,
            HttpServletResponse response, SubmissionInfo subInfo)
            throws ServletException, IOException, SQLException,
            AuthorizeException
    {
        // check what submit button was pressed in User Interface
        String buttonPressed = Util.getSubmitButton(request, NEXT_BUTTON);

        // get the item and current page
        Item item = subInfo.getSubmissionItem().getItem();
        int currentPage = getCurrentPage(request);

        // lookup applicable inputs
        Collection c = subInfo.getSubmissionItem().getCollection();
        InputForm inputForm = inputFormsMap.getInputForm(c);
        InputFormPage inputFormPage = inputForm.getPages().get(currentPage - 1);

        //Retrieve the metadata fields, the filtering of the shown ones is already taking care of
        List<InputFormField> formFields = inputFormPage.getMetadataFields(subInfo);

        // Step 1:
        // clear out all item metadata defined on this page
        for (InputFormField formField : formFields) {
            formField.clearMetadataValues(item);
        }

        // Clear required-field errors first since missing authority
        // values can add them too.
        clearErrorFields(request);
        List<String> errorFields = getErrorFields(request);


        // Step 2:
        // now update the item metadata.
        boolean moreInput = false;
        for (int i = 0; i < formFields.size(); i++)
        {
            InputFormField formField = formFields.get(i);
            MetadataField metadataField = formField.getMetadataField();
            String fieldName = org.dspace.content.MetadataField.formKey(metadataField.getSchema(), metadataField.getElement(), metadataField.getQualifier());

            formField.readInput(item, request, errorFields);

            // determine if more input fields were requested
            if (!moreInput
                    && buttonPressed.equals("submit_" + fieldName + "_add"))
            {
                subInfo.setMoreBoxesFor(fieldName);
                subInfo.setJumpToField(fieldName);
                moreInput = true;
            }
            // was XMLUI's "remove" button pushed?
            else if (buttonPressed.equals("submit_" + fieldName + "_delete"))
            {
                subInfo.setJumpToField(fieldName);
            }
        }

        // Step 3:
        // Check to see if any fields are missing
        // Only check for required fields if user clicked the "next", the "previous" or the "progress bar" button
        if (buttonPressed.equals(NEXT_BUTTON)
                || buttonPressed.startsWith(PROGRESS_BAR_PREFIX)
                || buttonPressed.equals(PREVIOUS_BUTTON)
                || buttonPressed.equals(CANCEL_BUTTON))
        {
            for (InputFormField formField : formFields) {
                MetadataField metadataField = formField.getMetadataField();
                if (formField.isMetadataInError(item)) {
                    String metadataFieldKey = org.dspace.content.MetadataField.formKey(metadataField.getSchema(), metadataField.getElement(), metadataField.getQualifier());
                    // since this field is missing add to list of error fields
                    errorFields.add(metadataFieldKey);
                }
            }
        }

        // Step 4: Store the error fields in our request
       setErrorFields(request, errorFields);

        // Step 5:
        // Save changes to database
        subInfo.getSubmissionItem().update();

        // commit changes
        context.commit();

        // check for request for more input fields, first
        if (moreInput)
        {
            return STATUS_MORE_INPUT_REQUESTED;
        }
        // if one or more fields errored out, return
        else if (getErrorFields(request) != null && getErrorFields(request).size() > 0)
        {
            return STATUS_MISSING_REQUIRED_FIELDS;
        }

        // completed without errors
        return STATUS_COMPLETE;
    }

    

    /**
     * Retrieves the number of pages that this "step" extends over. This method
     * is used to build the progress bar.
     * <P>
     * This method may just return 1 for most steps (since most steps consist of
     * a single page). But, it should return a number greater than 1 for any
     * "step" which spans across a number of HTML pages. For example, the
     * configurable "Describe" step (configured using input-forms.xml) overrides
     * this method to return the number of pages that are defined by its
     * configuration file.
     * <P>
     * Steps which are non-interactive (i.e. they do not display an interface to
     * the user) should return a value of 1, so that they are only processed
     * once!
     *
     * @param request
     *            The HTTP Request
     * @param subInfo
     *            The current submission information object
     *
     * @return the number of pages in this step
     */
    public int getNumberOfPages(HttpServletRequest request,
            SubmissionInfo subInfo) throws ServletException
    {
        // get number of input pages (i.e. "Describe" pages)
        try
        {
            InputForm inputForm = inputFormsMap.getInputForm(subInfo.getSubmissionItem().getCollection());
            return inputForm.getPages().size();
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
