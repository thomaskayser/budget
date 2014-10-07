/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-04-14 16:26:43 +0200 (Do, 14 Apr 2011) $
 *   $Author: tom $
 *   $Revision: 2100 $ 
 */
package ch.tkayser.budget.swing.common;

import static ch.tkayser.budget.swing.widgets.UIFactory.*;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.GroupLayout.Alignment;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ResourceMap;

import ch.tkayser.budget.swing.Constants;
import ch.tkayser.budget.swing.MainApplication;

/**
 * Dialog zum Anzeigen von Exceptions
 */
public class ExceptionDialog  extends JDialog {

    private static final long serialVersionUID = 1L;
    
    private static final String AC_OK = "OKAction";
    
    // a resource map
    private ResourceMap resourceMap;

    // the Message
    private final String errorMessage;

    // the exception
    private final Throwable exception;

    // the action map
    private ApplicationActionMap actionMap;

    
    /** 
     *
     * @param msgKey
     * @param exception
     */
    public ExceptionDialog(String errorMessage, Throwable exception) {
        
        super(MainApplication.getMainApplication().getMainFrame());
        
        // get the resource Map
        resourceMap = Application.getInstance().getContext().getResourceMap(getClass());
        
        actionMap = Application.getInstance(MainApplication.class).getContext().getActionMap(this.getClass(), this);

        // store the message and exception
        this.errorMessage = errorMessage;        
        this.exception = exception;
        
        // set the title
        setTitle(resourceMap.getString(Constants.DIALOG_TITEL));
        
        // init the content
        initializeContent();
        
        // modaler dialog
        setModal(true);
        setResizable(true);
        pack();

        // center
        setLocationRelativeTo(MainApplication.getMainApplication().getMainFrame());

    }

    /** 
     * Inhalt erstellen
     *
     */
    private void initializeContent() {
 
        // lables , textarea und ok button erstellen
        JLabel lblError = createLabel(errorMessage, errorMessage);
        lblError.setIcon(resourceMap.getImageIcon(Constants.ICON_ERROR));
        JLabel lblErrorDtails = createLabel("lbl.errordetails");        
        JTextArea txtException = new JTextArea(getStackTrace(exception));
        JScrollPane spException = new JScrollPane(txtException);
        JButton btnOk = new JButton(actionMap.get(AC_OK));
        
        // layout
        Container pane = getContentPane();
        GroupLayout mainLayout = new GroupLayout(pane);
        pane.setLayout(mainLayout);
        mainLayout.setAutoCreateContainerGaps(true);
        mainLayout.setAutoCreateGaps(true);        
        // linksbuendig untereinander ausrichten
        mainLayout.setHorizontalGroup(mainLayout.createParallelGroup().addComponent(lblError).addComponent(lblErrorDtails).addComponent(spException, 600, 600, 1200).addComponent(btnOk, Alignment.TRAILING));
        mainLayout.setVerticalGroup(mainLayout.createSequentialGroup().addComponent(lblError).addComponent(lblErrorDtails).addComponent(spException, 200, 200, 500).addComponent(btnOk));
        
        resourceMap.injectComponents(this);        
        
    }

    /**
     * Fehler Dialog anzeigen
     *
     * @param msgKey
     * @param exception
     */
    public static void showDialg(String errorMessage, Throwable exception) {
        ExceptionDialog dlg = new ExceptionDialog(errorMessage, exception);
        dlg.setVisible(true);        
    }
    
    
    private String getStackTrace(Throwable exception) {
        // init the result
        StringBuilder out = new StringBuilder();

        // loop over the exception chain...
        Throwable cause = exception;
        while (cause != null) {

            // add exception
            if (cause != exception) {
            out.append("\n");
            out.append("CAUSED BY:");
            out.append("\n");       
            }
            out.append(cause.getClass().getName());
            out.append(": "+cause.getMessage());
            
            // Add stack
            out.append("\n");
            for (StackTraceElement element : cause.getStackTrace()) {
            out.append("  ");
            out.append(element.getClassName());
            out.append(".");
            out.append(element.getMethodName());
            out.append(" (at Line");
            out.append(element.getLineNumber());
            out.append(")");
            out.append("\n");
            }
            // next exception
            cause = cause.getCause();
        }

        
        // return the stack
        return out.toString();

    }
    
    @Action
    public void OKAction(ActionEvent e) {
        setVisible(false);
    }

    

}
