/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.xmlui.aspect.submission.submit;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.lang.ArrayUtils;
import org.dspace.app.xmlui.aspect.submission.submit.inputForms.components.InputFormField;
import org.dspace.app.xmlui.aspect.submission.submit.inputForms.components.InputFormXmluiMap;
import org.dspace.app.xmlui.utils.UIException;
import org.dspace.app.xmlui.aspect.submission.AbstractSubmissionStep;
import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Body;
import org.dspace.app.xmlui.wing.element.Division;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.PageMeta;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Collection;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.content.authority.MetadataAuthorityManager;
import org.dspace.content.authority.Choices;

import org.dspace.kernel.ServiceManager;
import org.dspace.submit.inputForms.components.InputForm;
import org.dspace.submit.inputForms.components.InputFormMap;
import org.dspace.submit.inputForms.components.InputFormPage;
import org.dspace.utils.DSpace;
import org.xml.sax.SAXException;

/**
 * This is a step of the item submission processes. The describe step queries
 * the user for various metadata items about the item. For the most part all the
 * questions queried are completely configurable via the input-sets.xml file.
 * This system allows for multiple pages to be defined so this step is different
 * from all other stages in that it may represent multiple stages within the
 * submission processes.
 *
 * @author Scott Phillips
 * @author Tim Donohue (updated for Configurable Submission)
 */
public class DescribeStep extends AbstractSubmissionStep
{
        /** Language Strings **/
    protected static final Message T_head =
        message("xmlui.Submission.submit.DescribeStep.head");
    protected static final Message T_unknown_field=
        message("xmlui.Submission.submit.DescribeStep.unknown_field");
    protected static final Message T_required_field=
        message("xmlui.Submission.submit.DescribeStep.required_field");

    /** hash of all submission forms details */
    private static InputFormMap inputFormsMap;
    private static InputFormXmluiMap<String, InputFormField> inputFormXmluiMap;


        /**
     * A shared resource of the inputs reader. The 'inputs' are the
     * questions we ask the user to describe an item during the
     * submission process. The reader is a utility class to read
     * that configuration file.
     */

        /**
         * Establish our required parameters, abstractStep will enforce these.
         */
        public DescribeStep() throws ServletException
        {
            this.requireSubmission = true;
            this.requireStep = true;

            // Ensure that the InputsReader is initialized.
            ServiceManager serviceManager = new DSpace().getServiceManager();
            inputFormsMap = serviceManager.getServiceByName(InputFormMap.class.getName(), InputFormMap.class);
            inputFormXmluiMap = serviceManager.getServiceByName("inputFormXmluiMap", InputFormXmluiMap.class);
        }
        
        public void addPageMeta(PageMeta pageMeta) throws SAXException, WingException,
        UIException, SQLException, IOException, AuthorizeException
        {
            super.addPageMeta(pageMeta);
            int collectionID = submission.getCollection().getID();
            pageMeta.addMetadata("choice", "collection").addContent(String.valueOf(collectionID));

            String jumpTo = submissionInfo.getJumpToField();
            if (jumpTo != null)
            {
                pageMeta.addMetadata("page", "jumpTo").addContent(jumpTo);
            }
        }

        public void addBody(Body body) throws SAXException, WingException,
        UIException, SQLException, IOException, AuthorizeException
        {
            // Obtain the inputs (i.e. metadata fields we are going to display)
            Item item = submission.getItem();
            Collection collection = submission.getCollection();
            String actionURL = contextPath + "/handle/"+collection.getHandle() + "/submit/" + knot.getId() + ".continue";

            InputForm inputForm = inputFormsMap.getInputForm(collection);
            InputFormPage inputFormPage = inputForm.getPages().get(getPage() - 1);

            //Retrieve the metadata fields, the filtering of the shown ones is already taking care of
            java.util.List<org.dspace.submit.inputForms.components.InputFormField> formFields = inputFormPage.getMetadataFields(submissionInfo);


            Division div = body.addInteractiveDivision("submit-describe",actionURL,Division.METHOD_POST,"primary submission");
            div.setHead(T_submission_head);
            addSubmissionProgressList(div);

            List form = div.addList("submit-describe",List.TYPE_FORM);
            form.setHead(T_head);



            // Iterate over all inputs and add it to the form.
            for(org.dspace.submit.inputForms.components.InputFormField formField : formFields)
            {
                //Retrieve the rendering class
                InputFormField renderClass = inputFormXmluiMap.get(formField);
                if(renderClass != null){
                    boolean fieldInError = isFieldInError(InputFormField.getFieldName(formField.getMetadataField()));
                    renderClass.renderField(form, formField, submission, formField.getMetadataValues(submissionInfo.getSubmissionItem().getItem()), fieldInError);

                } else {
                    //TODO: log error !
                }
            }

                // add standard control/paging buttons
            addControlButtons(form);
        }

    /**
     * Each submission step must define its own information to be reviewed
     * during the final Review/Verify Step in the submission process.
     * <P>
     * The information to review should be tacked onto the passed in
     * List object.
     * <P>
     * NOTE: To remain consistent across all Steps, you should first
     * add a sub-List object (with this step's name as the heading),
     * by using a call to reviewList.addList().   This sublist is
     * the list you return from this method!
     *
     * @param reviewList
     *      The List to which all reviewable information should be added
     * @return
     *      The new sub-List object created by this step, which contains
     *      all the reviewable information.  If this step has nothing to
     *      review, then return null!
     */
    public List addReviewSection(List reviewList) throws SAXException,
        WingException, UIException, SQLException, IOException,
        AuthorizeException
    {
        //Create a new list section for this step (and set its heading)
        List describeSection = reviewList.addList("submit-review-" + this.stepAndPage, List.TYPE_FORM);
        describeSection.setHead(T_head);
        
        //Review the values assigned to all inputs
        //on this page of the Describe step.

        MetadataAuthorityManager mam = MetadataAuthorityManager.getManager();
        InputForm inputForm = inputFormsMap.getInputForm(submissionInfo.getSubmissionItem().getCollection());
        InputFormPage inputFormPage = inputForm.getPages().get(getPage() - 1);

        //Retrieve the metadata fields, the filtering of the shown ones is already taking care of
        java.util.List<org.dspace.submit.inputForms.components.InputFormField> formFields = inputFormPage.getMetadataFields(submissionInfo);



        for (org.dspace.submit.inputForms.components.InputFormField inputFormField : formFields)
        {
            DCValue[] metadataValues = inputFormField.getMetadataValues(submission.getItem());

            if (ArrayUtils.isNotEmpty(metadataValues))
            {
                for (DCValue metadataValue : metadataValues)
                {
                    String displayValue = inputFormField.getDisplayedValue(metadataValue);

                    // Only display this field if we have a value to display
                    if (displayValue!=null && displayValue.length()>0)
                    {

                        describeSection.addLabel(inputFormField.getLabelMessageKey());
                        if (mam.isAuthorityControlled(metadataValue.schema, metadataValue.element, metadataValue.qualifier))
                        {
                            String confidence = (metadataValue.authority != null && metadataValue.authority.length() > 0) ?
                                Choices.getConfidenceText(metadataValue.confidence).toLowerCase() :
                                "blank";
                            org.dspace.app.xmlui.wing.element.Item authItem =
                                describeSection.addItem("submit-review-field-with-authority", "ds-authority-confidence cf-"+confidence);
                            authItem.addContent(displayValue);
                        }
                        else
                        {
                            describeSection.addItem(displayValue);
                        }
                    }
                } // For each DCValue
            } // If values exist
        } // For each input
        
        // return this new "describe" section
        return describeSection;
    }

    /**
     * Check if the given fieldname is listed as being in error.
     *
     * @param fieldName
     * @return
     */
    private boolean isFieldInError(String fieldName)
    {
        return (this.errorFields.contains(fieldName));
    }

        /**
         * There is a problem with the way hints are handled. The class that we use to
         * read the input-forms.xml configuration will append and prepend HTML to hints.
         * This causes all sorts of confusion when inserting into the DRI page, so this
         * method will strip that extra HTML and just leave the cleaned comments.
         *
         *
         * However this method will not remove naughty or sexual innuendoes from the
         * field's hints.
         *
         *
         * @param dirtyHints HTML-ized hints
         * @return Hints without HTML.
         */
        private static final String HINT_HTML_PREFIX = "<tr><td colspan=\"4\" class=\"submitFormHelp\">";
        private static final String HINT_HTML_POSTFIX = "</td></tr>";
        private String cleanHints(String dirtyHints)
        {
                String clean = (dirtyHints!=null ? dirtyHints : "");

                if (clean.startsWith(HINT_HTML_PREFIX))
                {
                        clean = clean.substring(HINT_HTML_PREFIX.length());
                }

                if (clean.endsWith(HINT_HTML_POSTFIX))
                {
                        clean = clean.substring(0,clean.length() - HINT_HTML_POSTFIX.length());
                }

                return clean;
        }
}
