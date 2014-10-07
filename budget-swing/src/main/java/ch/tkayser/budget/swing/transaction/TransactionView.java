/*
 * Software is written by:
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2011
 * 
 */
package ch.tkayser.budget.swing.transaction;

import static ch.tkayser.budget.swing.widgets.UIFactory.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jdesktop.application.Action;

import ch.tkayser.budget.dto.AccountDTO;
import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.swing.account.AccountListCellRenderer;
import ch.tkayser.budget.swing.widgets.AbstractBudgetPanel;
import ch.tkayser.budget.swing.widgets.daterange.DateRangePanel;

/**
 * @author tom
 * 
 */
@SuppressWarnings("serial")
public class TransactionView extends AbstractBudgetPanel {

    // @formatter:off
    // Actions
    private static final String            AC_REFRESH          = "refreshAction";
    public static final String             AC_NEW_TX           = "newTransactionAction";
    public static final String             AC_DELETE_TX        = "deleteTransactionAction";
    public static final String             AC_SAVE_IMPORTED_TX = "saveImportedTransactionAction";
    public static final String             AC_IMPORT_TX        = "importTransactionAction";

    // the presenter
    private final TransactionViewPresenter presenter;

    // UI elements
    // tabelle mit transactions
    private JTable                         txTable;
    private TransactionTableModel          model;

    // panel mit datumsbereich
    private DateRangePanel                 dateRangePanel;
    // konto ComboBox
    private JComboBox                      cbAccount;
    // Checkbox mit children
    private JCheckBox                      cbWithChildren;

    private JLabel                         lblTotalAmount;

    // @formatter:on

    public TransactionView(TransactionViewPresenter presenter) {
        super();
        this.presenter = presenter;
        createViewContent();
    }

    /**
     * Inhalt der View erstellen
     * 
     */
    private void createViewContent() {

        // toolbar aufbauen
        addToToolbar(AC_NEW_TX, AC_DELETE_TX, AC_IMPORT_TX, AC_SAVE_IMPORTED_TX);

        // Such panel
        JPanel pnlSearch = createSearchPanel();

        // Panel mit Transactions erstellen
        JPanel pnlTx = createTxPanel();

        // zusammenbauen
        JPanel pnlMain = new JPanel();
        GroupLayout layout = new GroupLayout(pnlMain);
        pnlMain.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(pnlSearch).addComponent(pnlTx));
        layout.setHorizontalGroup(layout.createParallelGroup().addComponent(pnlSearch, Alignment.LEADING)
                .addComponent(pnlTx));

        // view content setzen
        setViewContent(pnlMain);

    }

    /**
     * Panel mit Tx bauen
     * 
     * @return
     */
    private JPanel createTxPanel() {

        // Panel erstellen
        JPanel pnlTx = new JPanel();
        pnlTx.setLayout(new BorderLayout());

        // Tabelle und Model
        model = presenter.getTableModel();
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent arg0) {
                BigDecimal total = new BigDecimal(0);
                if (model.getTransactions() != null) {
                    for (TransactionDTO tx : model.getTransactions()) {
                        total = total.add(tx.getAmount());
                    }
                }
                DecimalFormat format = new DecimalFormat("###,###,##0.00");
                lblTotalAmount.setText(format.format(total.doubleValue()));
            }
        });
        txTable = createJTable(model);

        // listener zum anzeigen des editors
        txTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    presenter.edit(getSelectedTransacction());
                }
            }
        });

        // in ScrollPane wrappen
        JScrollPane txPane = new JScrollPane(txTable);

        // zu panel hinzufuegen
        pnlTx.add(txPane, BorderLayout.CENTER);

        return pnlTx;

    }

    /**
     * Gibt die in der Tabelle selektierte Tx zuruecke
     * 
     * @return
     */
    protected TransactionDTO getSelectedTransacction() {
        int row = txTable.convertRowIndexToModel(txTable.getSelectedRow());
        return model.getTransaction(row);
    }

    /**
     * @return
     */
    private JPanel createSearchPanel() {

        // Such Panel erstellen
        JPanel pnlSearch = new JPanel();

        // Datumsbereich
        dateRangePanel = new DateRangePanel();
        pnlSearch.add(dateRangePanel);

        // konto
        JLabel lblAccount = createLabel("lbl.account");
        cbAccount = createComboBox(presenter.getAccountSelection(false), new AccountListCellRenderer());
        
        // Checkbox mit kindern
        cbWithChildren = createCheckBox("cb.withChidren");

        // refresh button
        JButton btnRefresh = new JButton(getAction(AC_REFRESH));

        // Total Label
        JLabel lblTotal = createLabel("lbl.total");
        lblTotalAmount = createLabel("", "");

        GroupLayout searchLayout = new GroupLayout(pnlSearch);
        pnlSearch.setLayout(searchLayout);
        searchLayout.setAutoCreateGaps(true);
        searchLayout.setAutoCreateContainerGaps(true);
        searchLayout.setVerticalGroup(searchLayout.createParallelGroup(Alignment.CENTER).addComponent(dateRangePanel)
                .addComponent(lblAccount).addComponent(cbAccount).addComponent(cbWithChildren).addComponent(btnRefresh).addComponent(lblTotal)
                .addComponent(lblTotalAmount));
        searchLayout.setHorizontalGroup(searchLayout.createSequentialGroup()
                .addComponent(dateRangePanel, 600, 600, 600).addComponent(lblAccount)
                .addComponent(cbAccount, 150, 150, 200).addComponent(cbWithChildren).addComponent(btnRefresh).addComponent(lblTotal)
                .addComponent(lblTotalAmount));

        return pnlSearch;
    }

    /**
     * Action to refresh the tx
     */
    @Action
    public void refreshAction(ActionEvent e) {
        // search accounts
        presenter.refreshTransactions(dateRangePanel.getDateFrom(), dateRangePanel.getDateTo(),
                (AccountDTO) cbAccount.getSelectedItem(), cbWithChildren.isSelected());
    }

    /**
     * Action to import transactions
     */
    @Action
    public void importTransactionAction(ActionEvent e) {
        // import tx from file
        presenter.importTransactions();
    }

    /**
     * Action to add a transaction
     */
    @Action
    public void newTransactionAction(ActionEvent e) {
        // create new tx
        presenter.create();
    }

    /**
     * Action to delete a transaction
     */
    @Action
    public void deleteTransactionAction(ActionEvent e) {
        // delete the tx
        presenter.delete(getSelectedTransacction());
    }

    /**
     * Action to save all imported tx
     */
    @Action
    public void saveImportedTransactionAction(ActionEvent e) {
        presenter.saveAllImportedTransactions();
    }

}
