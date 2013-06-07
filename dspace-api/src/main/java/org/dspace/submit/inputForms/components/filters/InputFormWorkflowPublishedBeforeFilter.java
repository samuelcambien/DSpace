package org.dspace.submit.inputForms.components.filters;

import org.dspace.app.util.SubmissionInfo;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 30/05/13
 * Time: 11:51
 */
public class InputFormWorkflowPublishedBeforeFilter extends InputFormFieldFilter{
    @Override
    public boolean isFieldActive(SubmissionInfo submissionInfo) {
        return submissionInfo.getSubmissionItem().isPublishedBefore();
    }
}
