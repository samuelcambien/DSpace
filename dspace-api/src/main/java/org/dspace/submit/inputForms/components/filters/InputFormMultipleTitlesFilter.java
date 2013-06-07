package org.dspace.submit.inputForms.components.filters;

import org.dspace.app.util.SubmissionInfo;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 30/05/13
 * Time: 10:58
 */
public class InputFormMultipleTitlesFilter extends InputFormFieldFilter{

    @Override
    public boolean isFieldActive(SubmissionInfo submissionInfo) {
        return submissionInfo.getSubmissionItem().hasMultipleTitles();
    }
}
