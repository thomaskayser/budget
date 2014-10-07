/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-04-16 20:13:02 +0200 (Sa, 16 Apr 2011) $
 *   $Author: tom $
 *   $Revision: 2102 $ 
 */
package ch.tkayser.budget.swing.widgets;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

import ch.tkayser.budget.base.dto.BaseDTO;
import ch.tkayser.budget.swing.Constants;
import ch.tkayser.budget.swing.MainApplication;
import ch.tkayser.budget.swing.presenter.EditPresenter;
import ch.tkayser.budget.swing.validation.BudgetValidator;

import com.jgoodies.binding.beans.PropertyConnector;

/**
 * Abstracte Superklasse fuer alle Editoren
 */
public abstract class AbstractBudgetDialog<T extends BaseDTO> extends JDialog {
    private static final long    serialVersionUID = 1L;

    // the resource map
    protected ResourceMap        resourceMap;

    // action Names
    // @formatter:off
    public static final String   AC_SAVE          = "saveAction";
    public static final String   AC_CANCEL        = "cancelAction";
    // @formatter:on

    // the presenter
    private final EditPresenter<T>   presenter;

    // the panel with the content
    private JPanel               pnlContent;

    /**
     * Dialog erstellen
     * 
     * @param model
     * 
     */
    public AbstractBudgetDialog(EditPresenter<T> presenter) {
        super();        
        setName(getClass().getSimpleName());
        this.presenter = presenter;

        // get the resource Map
        resourceMap = Application.getInstance().getContext().getResourceMap(getClass());

        // Window Properties
        setModal(true);
        setResizable(true);

        // title
        setTitle(resourceMap.getString(Constants.DIALOG_TITEL));

        // dialog erstellen
        createDialog();

    }

    /**
     * get an action for the abstractBudgetDialog Class
     * 
     * @param actionName
     * @return
     */
    private Action getAction(String actionName) {
        return Application.getInstance(MainApplication.class).getContext()
                .getActionMap(AbstractBudgetDialog.class, this).get(actionName);

    }

    /**
     * Create the content of the dialog
     * 
     */
    private void createDialog() {
        // panel mit Fehlern erstellen
        JPanel pnlErrors = new ErrorListPanel(presenter.getEditValidator());

        // create the content panel
        pnlContent = new JPanel();
        pnlContent.setLayout(new BorderLayout());


        // Panel mit Buttons erstellen
        JButton btnSave = new JButton(getAction(AC_SAVE));
        getRootPane().setDefaultButton(btnSave);
        JButton btnCancel = new JButton(getAction(AC_CANCEL));
        JPanel pnlButtons = new JPanel();
        GroupLayout btnLayout = new GroupLayout(pnlButtons);
        pnlButtons.setLayout(btnLayout);
        btnLayout.setAutoCreateContainerGaps(true);
        btnLayout.setAutoCreateGaps(true);
        btnLayout.setHorizontalGroup(btnLayout.createSequentialGroup().addComponent(btnSave).addComponent(btnCancel));
        btnLayout.setVerticalGroup(btnLayout.createParallelGroup().addComponent(btnSave).addComponent(btnCancel));

        // layout the content and button panel
        Container pane = getContentPane();
        GroupLayout mainLayout = new GroupLayout(pane);
        pane.setLayout(mainLayout);
        mainLayout.setAutoCreateContainerGaps(true);
        mainLayout.setAutoCreateGaps(true);
        // horizontal: paralell. Buttons rechts ausgerichtet
        mainLayout.setHorizontalGroup(mainLayout.createParallelGroup().addComponent(pnlErrors).addComponent(pnlContent)
                .addComponent(pnlButtons, Alignment.TRAILING));
        mainLayout.setVerticalGroup(mainLayout.createSequentialGroup().addComponent(pnlErrors).addComponent(pnlContent)
                .addComponent(pnlButtons));

        // validation status des validators mit dem enabled status des ok
        // buttons verknuepfen
        PropertyConnector connector = PropertyConnector.connect(presenter.getEditValidator(),
                BudgetValidator.PROPERTY_VALIDATION_OK, btnSave, "enabled");
        connector.updateProperty2();
    }

    /**
     * Inhalt setzen.
     * 
     * @param content
     */
    protected void setDialogContent(JComponent content) {
        pnlContent.add(content, BorderLayout.CENTER);
    }

    /**
     * Action zum speichern. Ruft die abstrakte Methode doSave auf.
     * 
     * @param e
     */
    @org.jdesktop.application.Action
    public void saveAction(ActionEvent e) {
        presenter.save(presenter.getEditModel().getBean());
        setVisible(false);
    }

    /**
     * Action zum abbrechen des Bearbeitens. Ruft die abstrakte Methode doCancel
     * auf.
     * 
     * @param e
     */
    @org.jdesktop.application.Action
    public void cancelAction(ActionEvent e) {
        setVisible(false);
    }

    /**
     * display the dialog
     */
    public final void displayDialog() {

        // force validation
        presenter.getEditValidator().validateBean();

        // inject resources
        resourceMap.injectComponents(this);
        
        // set position
        pack();
        setLocationRelativeTo(MainApplication.getMainApplication().getMainFrame());

        
        // display
        setVisible(true);

    }


}
