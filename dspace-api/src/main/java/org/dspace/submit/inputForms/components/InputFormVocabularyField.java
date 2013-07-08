package org.dspace.submit.inputForms.components;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 13/06/13
 * Time: 09:29
 */
public abstract class InputFormVocabularyField extends InputFormField {

    private String vocabulary;
    private boolean isClosedVocabulary;

    public String getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(String vocabulary) {
        this.vocabulary = vocabulary;
    }

    public boolean isClosedVocabulary() {
        return isClosedVocabulary;
    }

    public void setClosedVocabulary(boolean closedVocabulary) {
        isClosedVocabulary = closedVocabulary;
    }
}
