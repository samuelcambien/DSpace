package org.dspace.submit.inputForms.components.filters;

import org.dspace.app.util.SubmissionInfo;
import org.springframework.beans.factory.annotation.Required;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 30/05/13
 * Time: 10:49
 */
public class InputFormWorkflowFieldFilter extends InputFormFieldFilter {

    private boolean inWorkflow;


    public boolean isInWorkflow() {
        return inWorkflow;
    }

    @Required
    public void setInWorkflow(boolean inWorkflow) {
        this.inWorkflow = inWorkflow;
    }


    @Override
    public boolean isFieldActive(SubmissionInfo submissionInfo) {
        if(submissionInfo.isInWorkflow() && inWorkflow)
        {
            return true;
        }else
        if(!submissionInfo.isInWorkflow() && !inWorkflow)
        {
            return true;
        }
        return false;
    }
}
