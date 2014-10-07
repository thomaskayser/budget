/*
 * Software is written by:
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2011
 * 
 */
package ch.tkayser.budget.swing.widgets;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

import ch.tkayser.budget.swing.Constants;
import ch.tkayser.budget.swing.validation.BudgetValidator;


/**
 * Decorator for validations.
 * 
 * The decorator is bound to a JComponent and a BudgetValidator.
 * 
 * The name of the attribute bound to the JComponent is stored in the ClientProperties of the JComponent. The key
 * of the property is UIFa
 * 
 *
 * 
 * 
 * It listenes for changes on the validity of an attriubt and
 * displays coresponding decorations.
 * 
 * @author tom
 *
 */
public class ValidationDecoration extends AbstractComponentDecorator {
    
    // the resouceMap
    private ResourceMap resourceMap;

    // the component we are attached to
    private final JComponent component;

    // the attribute name of the deocarted component
    private String attributeName;

    // the validator
    private final BudgetValidator<?> validator;
    
    // is the decorated Jcomponent valid
    private boolean isValid;

    // the error image
    private ImageIcon errorImage;


    /**
     * @param c
     */
    public ValidationDecoration(JComponent component,  BudgetValidator<?> validator) {
        super(component);
        this.component = component;
        this.validator = validator;

        // validation ok 
        isValid = true;
        
        // get image for errors
        resourceMap = Application.getInstance().getContext().getResourceMap(getClass());
        errorImage = resourceMap.getImageIcon(Constants.ICON_ERROR);
        
        // attribute name holen
        attributeName = (String)component.getClientProperty(UIFactory.CLIENT_PROPERTY_ATTRIBUT_NAME);
        
        // listen to changes on the attribute
        validator.addPropertyChangeListener(new PropertyChangeListener() {           
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(attributeName)) {
                   setValidationState();
                }
            }
        });
        
    }
    

    /**
     * set the validation state and repaint the JComponent
     *
     */
    private void setValidationState() {
        if (validator.getViolations(attributeName).size() > 0) {
            isValid = false;            
        } else {
            isValid = true;            
        }
        // repaint in the Swing thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                component.repaint();
            }            
        });
    }


    /* (non-Javadoc)
     * @see ch.tkayser.budget.swing.widgets.AbstractComponentDecorator#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics g) {
        if (!isValid) {
            Rectangle decorationBounds = getDecorationBounds();
            g.drawImage(errorImage.getImage(),0,decorationBounds.height-errorImage.getIconWidth()-5, component);            
        }        
    }

}
