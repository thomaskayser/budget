/*
 * Software is written by:
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2011
 * 
 */
package ch.tkayser.budget.swing.balance;

import static ch.tkayser.budget.swing.widgets.UIFactory.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.application.Action;
import org.jdesktop.swingx.JXTreeTable;

import ch.tkayser.budget.dto.TimeGroup;
import ch.tkayser.budget.swing.Constants;
import ch.tkayser.budget.swing.widgets.AbstractBudgetPanel;
import ch.tkayser.budget.swing.widgets.daterange.DateRangePanel;

/**
 * @author tom
 * 
 */
@SuppressWarnings("serial")
public class BalanceView extends AbstractBudgetPanel {

    //@formatter:off
    // Actions
    private static final String AC_REFRESH               = "refreshAction";
    
    // the presenter
    private final BalancePresenter         presenter;

    // UI elements
    // tabelle mit transactions
    private JXTreeTable                    balanceTable;      
    private BalanceTreeTableModel          model;

    // panel mit datumsbereich
    private DateRangePanel                 dateRangePanel;

    // Cb mit Gruppierung
    private JComboBox cbGroup;



    //@formatter:on

    public BalanceView(BalancePresenter presenter) {
        super();
        this.presenter = presenter;
        createViewContent();
    }

    /**
     * Inhalt der View erstellen
     * 
     */
    private void createViewContent() {

        // Such panel
        JPanel pnlSearch = createSearchPanel();

        // Panel mit Balances erstellen
        JPanel pnlTx = createBalancePanel();

        // zusammenbauen
        JPanel pnlMain = new JPanel();
        GroupLayout layout = new GroupLayout(pnlMain);
        pnlMain.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(pnlSearch).addComponent(pnlTx));
        layout.setHorizontalGroup(layout.createParallelGroup().addComponent(pnlSearch, Alignment.LEADING).addComponent(pnlTx));

        // view content setzen
        setViewContent(pnlMain);

    }

    /**
     * Panel mit Tx bauen
     * 
     * @return
     */
    private JPanel createBalancePanel() {

        // Panel erstellen
        JPanel pnlTx = new JPanel();
        pnlTx.setLayout(new BorderLayout());

        // JTreeTabele und model
        model = presenter.getBalanceModel();
        balanceTable = createJTreeTable(model);

        // in ScrollPane wrappen
        JScrollPane txPane = new JScrollPane(balanceTable);

        // zu panel hinzufuegen
        pnlTx.add(txPane, BorderLayout.CENTER);

        return pnlTx;

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

        // Gruppieren
        JLabel lblGroupBy = createLabel(Constants.LBL_GROUPBY);
        cbGroup = createComboBox(new DefaultComboBoxModel(TimeGroup.values()), new TimeGroupLabelProvider());

        // refresh button
        JButton btnRefresh = new JButton(getAction(AC_REFRESH));

        GroupLayout searchLayout = new GroupLayout(pnlSearch);
        pnlSearch.setLayout(searchLayout);
        searchLayout.setAutoCreateGaps(true);
        searchLayout.setAutoCreateContainerGaps(true);
        searchLayout.setVerticalGroup(searchLayout.createParallelGroup(Alignment.CENTER).addComponent(dateRangePanel)
                .addComponent(lblGroupBy).addComponent(cbGroup, 24, 24, 24).addComponent(btnRefresh));
        searchLayout.setHorizontalGroup(searchLayout.createSequentialGroup().addComponent(dateRangePanel, 600, 600, 600)
                .addComponent(lblGroupBy).addComponent(cbGroup).addComponent(btnRefresh));

        return pnlSearch;
    }

    /**
     * Action to refresh the balances
     */
    @Action
    public void refreshAction(ActionEvent e) {
        // search balances
        presenter.refreshBalances(dateRangePanel.getDateFrom(), dateRangePanel.getDateTo(), (TimeGroup)cbGroup.getSelectedItem());
    }

}
