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

import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

import ch.tkayser.budget.swing.Constants;
import ch.tkayser.budget.swing.validation.BudgetValidator;

/**
 * Displays a Panel with a list of errors
 * 
 * @author tom
 * 
 */
public class ErrorListPanel extends JPanel {

    private static final long        serialVersionUID = 1L;

    // the labels to display the violations
    private List<JLabel>             errorLabels;

    // the error icon
    private Icon                     errorIcon;

    // the validator
    private final BudgetValidator<?> validator;

    // the resource Map
    private ResourceMap resourceMap;


    public ErrorListPanel(BudgetValidator<?> validator) {
        super();
        this.validator = validator;
        
        // get the resource Map
        resourceMap = Application.getInstance().getContext().getResourceMap(getClass());

        
        // init labels
        errorLabels = new ArrayList<JLabel>();
        
        // register auf Violations Property
        validator.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(BudgetValidator.PROPERTY_VIOLATIONS)) {
                    refreshLabels();
                }
            }

        });
        

        // groesse init
        setMinimumSize(new Dimension(200, 50));
        
        // weisser hintergrund
        setBackground(Color.WHITE);
        
    }
    
    

    /**
     * Load the error Icon lazy
     * 
     * @return
     */
    private Icon getErrorIcon() {
        if (errorIcon == null) {
            // get the resource Map
            errorIcon = resourceMap.getIcon(Constants.ICON_ERROR);

        }
        return errorIcon;
    }

    /**
     * gibt die anzahl anzuzeigenden Fehler zurzueck
     * @return
     */
    private int getNumberOfErrorsToDisplay() {
        return resourceMap.getInteger(Constants.PREF_NUMBER_OF_ERRORS).intValue();
    }

    /**
     * 
     */
    protected void refreshLabels() {
        // bestehende labels entfernen
        for (JLabel errorLabel : errorLabels) {
            remove(errorLabel);
        }
        
        // layout erstellen
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateContainerGaps(false);
        layout.setAutoCreateGaps(false);
        ParallelGroup horizontalGroup = layout.createParallelGroup();
        SequentialGroup verticalGroup = layout.createSequentialGroup();
        layout.setHorizontalGroup(horizontalGroup);
        layout.setVerticalGroup(verticalGroup);
        
        // neue labels erstellen (fuer die anzahl anzuzeigenden Fehler
        int error = 1;
        int errorsToDisplay = getNumberOfErrorsToDisplay();
        for (String violation : validator.getViolations()) {
            if (error < errorsToDisplay) {
                JLabel errorLbl = new JLabel(violation);
                errorLbl.setIcon(getErrorIcon());
                errorLabels.add(errorLbl);
                verticalGroup.addComponent(errorLbl);
                horizontalGroup.addComponent(errorLbl);                            
            }
            error++;
        }

        // repaint in the Swing thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ErrorListPanel.this.repaint();
                ErrorListPanel.this.getParent().repaint();
            }
        });

    }

}
