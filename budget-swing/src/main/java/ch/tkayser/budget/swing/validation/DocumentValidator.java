/*
 * Software is written by:
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2011
 * 
 */
package ch.tkayser.budget.swing.validation;

import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ch.tkayser.budget.swing.formating.ValidationMessages;
import ch.tkayser.budget.swing.widgets.UIFactory;

/**
 * @author tom
 * 
 */
public class DocumentValidator implements DocumentListener {

    // the validator
    private BudgetValidator<?>     validator;

    // the textfied
    private JFormattedTextField textField;

    public DocumentValidator(BudgetValidator<?> validator, JFormattedTextField textField) {
        super();
        this.validator = validator;
        this.textField = textField;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.
     * DocumentEvent)
     */
    @Override
    public void changedUpdate(DocumentEvent evts) {
        validateValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.
     * DocumentEvent)
     */
    @Override
    public void insertUpdate(DocumentEvent evt) {
        validateValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.
     * DocumentEvent)
     */
    @Override
    public void removeUpdate(DocumentEvent evt) {
        validateValue();
    }

    /**
     * value des documents validieren
     */
    private void validateValue() {
        try {
            String text = textField.getText();
            if (text != null && text.length() > 0) {
                textField.getFormatter().stringToValue(textField.getText());
            }
            validator.clearInputViolations(getAttributeName());
        } catch (ParseException e) {
            // classe des textfields holen
            Class<?> fieldClass = null;
            if (textField.getValue() != null) {
                fieldClass = textField.getValue().getClass();
            }
            validator.setInputViolation(getAttributeName(), ValidationMessages.getMessage(fieldClass, getAttributeName()));
        }
    }

    /**
     * attribute name aus den ClientProperties des TextFieds lesen
     * 
     * @return
     */
    private String getAttributeName() {
       return (String) textField.getClientProperty(UIFactory.CLIENT_PROPERTY_ATTRIBUT_NAME);
        
    }

}
