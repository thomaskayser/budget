/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-04-14 08:48:17 +0200 (Do, 14 Apr 2011) $
 *   $Author: tom $
 *   $Revision: 2099 $ 
 */
package ch.tkayser.budget.swing.transaction;

import static ch.tkayser.budget.swing.widgets.UIFactory.*;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

import ch.tkayser.budget.parser.ParserFactory;
import ch.tkayser.budget.parser.TransactionParser;
import ch.tkayser.budget.swing.Constants;
import ch.tkayser.budget.swing.MainApplication;

/**
 * Dialog zum bearbeiten eines Kontos
 */
public class TransactionImportView extends JDialog {

    private static final long serialVersionUID = 1L;

    // @formatter:off

    // the resource map
    protected ResourceMap                  resourceMap;

    // action Names
    public static final String             AC_IMPORT        = "importAction";
    public static final String             AC_CANCEL        = "cancelAction";
    public static final String             AC_CHOOSE_FILE   = "chooseFileAction";

    // the account presenter
    private final TransactionViewPresenter transactionPresenter;

    // UI Elements
    // FileName TextField
    private JTextField                     txtFileName;
    // combobBox with parser
    private JComboBox                      cbParser;


    // @formatter:on

    public TransactionImportView(TransactionViewPresenter presenter) {
        super();
        this.transactionPresenter = presenter;

        // get the resource Map
        resourceMap = Application.getInstance().getContext().getResourceMap(getClass());

        // Window Properties
        setModal(true);
        setResizable(true);

        // title
        setTitle(resourceMap.getString(Constants.DIALOG_TITEL));

        // dialog erstellen
        createDialog();

        // inject resources
        resourceMap.injectComponents(this);

        // set position
        pack();
        setLocationRelativeTo(MainApplication.getMainApplication().getMainFrame());

    }    
    

    /**
     * TODO: Bitte durch Methoden Beschreibung ersetzen
     * 
     */
    private void createDialog() {

        // create the content panel
        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(new BorderLayout());
        pnlContent.add(getContent());

        // Panel mit Buttons erstellen
        JPanel pnlButtons = createButtonPanel();

        // layout the content and button panel
        Container pane = getContentPane();
        GroupLayout mainLayout = new GroupLayout(pane);
        pane.setLayout(mainLayout);
        mainLayout.setAutoCreateContainerGaps(true);
        mainLayout.setAutoCreateGaps(true);
        // horizontal: paralell. Buttons rechts ausgerichtet
        mainLayout.setHorizontalGroup(mainLayout.createParallelGroup().addComponent(pnlContent)
                .addComponent(pnlButtons, Alignment.TRAILING));
        mainLayout.setVerticalGroup(mainLayout.createSequentialGroup().addComponent(pnlContent).addComponent(pnlButtons));

    }

    /**
     * Gibt den Inhalt zurueck
     * 
     * @return
     */
    private JPanel getContent() {

        // FileName und ChooseButton
        JLabel lblFileName = createLabel("lbl.fileName");
        txtFileName = createTextDisplayField();
        txtFileName.setName("test");
        JButton btnChooseFile = new JButton(getAction(AC_CHOOSE_FILE));

        // Parser
        JLabel lblParser = createLabel("lbl.parser");
        cbParser = createComboBox(
                new DefaultComboBoxModel(ParserFactory.getAvailableParsers().toArray(new TransactionParser[] {})),
                new TransactionParserListCellRenderer());

        // Layouten
        // lblFile und openButton in einem Panel
        JPanel pnlSelectedFile = new JPanel();
        GroupLayout layoutFile = new GroupLayout(pnlSelectedFile);
        pnlSelectedFile.setLayout(layoutFile);
        layoutFile.setAutoCreateGaps(true);
        layoutFile.setHorizontalGroup(layoutFile.createSequentialGroup().addComponent(txtFileName).addComponent(btnChooseFile));
        layoutFile.setVerticalGroup(layoutFile.createParallelGroup(Alignment.BASELINE).addComponent(txtFileName).addComponent(btnChooseFile));

        // Panel erstellen und layouten
        JPanel pnlImport = new JPanel();
        GroupLayout layout = new GroupLayout(pnlImport);
        pnlImport.setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        // horizontal: labels and text fields parallel
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup().addComponent(lblFileName).addComponent(lblParser));
        hGroup.addGroup(layout.createParallelGroup().addComponent(pnlSelectedFile).addComponent(cbParser));
        layout.setHorizontalGroup(hGroup);

        // vertical: lbl and textfield parallel
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE, false).addComponent(lblFileName).addComponent(pnlSelectedFile));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(lblParser).addComponent(cbParser));
        layout.setVerticalGroup(vGroup);
        
        return pnlImport;
        
        

    }

    /**
     * Panel mit Buttons erstellen
     * 
     * @return
     */
    private JPanel createButtonPanel() {

        // Import Button
        JButton btnSave = new JButton(getAction(AC_IMPORT));
        getRootPane().setDefaultButton(btnSave);
        // Cancel button
        JButton btnCancel = new JButton(getAction(AC_CANCEL));

        // Panel erstellen
        JPanel pnlButtons = new JPanel();
        GroupLayout btnLayout = new GroupLayout(pnlButtons);
        pnlButtons.setLayout(btnLayout);
        btnLayout.setAutoCreateContainerGaps(true);
        btnLayout.setAutoCreateGaps(true);
        btnLayout.setHorizontalGroup(btnLayout.createSequentialGroup().addComponent(btnSave).addComponent(btnCancel));
        btnLayout.setVerticalGroup(btnLayout.createParallelGroup().addComponent(btnSave).addComponent(btnCancel));

        return pnlButtons;

    }

    /**
     * get an action for the abstractBudgetDialog Class
     * 
     * @param actionName
     * @return
     */
    private Action getAction(String actionName) {
        return Application.getInstance(MainApplication.class).getContext().getActionMap(getClass(), this).get(actionName);
    }

    /**
     * Tx importieren
     * 
     * @param e
     */
    @org.jdesktop.application.Action
    public void importAction(ActionEvent e) {
        transactionPresenter.importTransactions(txtFileName.getText(), (TransactionParser)cbParser.getSelectedItem());
        setVisible(false);
    }

    /**
     * Action zum abbrechen des Bearbeitens. Ruft die abstrakte Methode doCancel auf.
     * 
     * @param e
     */
    @org.jdesktop.application.Action
    public void cancelAction(ActionEvent e) {
        setVisible(false);
    }

    /**
     * Action zum abbrechen des Bearbeitens. Ruft die abstrakte Methode doCancel auf.
     * 
     * @param e
     */
    @org.jdesktop.application.Action
    public void chooseFileAction(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(MainApplication.getMainApplication().getMainFrame());        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            txtFileName.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

}
