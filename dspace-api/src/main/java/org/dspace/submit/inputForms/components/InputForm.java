package org.dspace.submit.inputForms.components;

import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 29/05/13
 * Time: 11:56
 */
public class InputForm {

    protected List<InputFormPage> pages;

    public List<InputFormPage> getPages() {
        return pages;
    }

    @Required
    public void setPages(List<InputFormPage> pages) {
        this.pages = pages;
    }
}
