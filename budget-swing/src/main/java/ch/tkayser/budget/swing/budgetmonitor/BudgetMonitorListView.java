/*
 * Software is written by:
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2011
 * 
 */
package ch.tkayser.budget.swing.budgetmonitor;

import static ch.tkayser.budget.swing.widgets.UIFactory.*;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;

import org.jdesktop.application.Action;

import ch.tkayser.budget.dto.BudgetDTO;
import ch.tkayser.budget.swing.widgets.AbstractBudgetPanel;
import ch.tkayser.budget.swing.widgets.UIFactory;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.list.SelectionInList;

/**
 * @author tom
 * 
 */
@SuppressWarnings("serial")
public class BudgetMonitorListView extends AbstractBudgetPanel {

    // action Names
    public static final String AC_NEW_MONITOR = "newMonitorAction";

    public static final String AC_DELETE_MONITOR = "deleteMonitorAction";

    // the presenter
    private final BudgetMonitorPresenter presenter;

    // UI Elements
    private JList monitorList;

    // Models
    private SelectionInList<BudgetDTO> monitorListModel;

    private PresentationModel<BudgetDTO> monitorDetailModel;

    public BudgetMonitorListView(BudgetMonitorPresenter presenter) {
        super();
        this.presenter = presenter;
        createViewContent();
    }

    /**
     * 
     */
    private void createViewContent() {
        // actions hinzufuegen
        addToToolbar(BudgetMonitorListView.AC_NEW_MONITOR, BudgetMonitorListView.AC_DELETE_MONITOR);

        // initialize the models
        initModels();

        // Build split pane. left with JList for monitors, right with details
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setName("monitor_split");
        splitPane.setDividerLocation(150);
        splitPane.setContinuousLayout(true);
        splitPane.setLeftComponent(getMonitorList());
        splitPane.setRightComponent(getMonitorDetails());

        // set it as content
        setViewContent(splitPane);

    }

    /**
     * Init the models
     * 
     */
    private void initModels() {
        // list Model for the monitor
        monitorListModel = presenter.getMonitorListModel();
        

        // details model for the selected monitor
        monitorDetailModel = presenter.getCalculationPresentationModel(monitorListModel);

    }

    /**
     * erstellt das Panelmit den Monitor Details
     * 
     * @return
     */
    private Component getMonitorDetails() {
        // labels erstellen
        JLabel lblMonthly = createLabel("lbl.monthly");
        JLabel lblDaily = createLabel("lbl.daily");
        JLabel lblUptoDay = createLabel("lbl.uptoday");
        JLabel lblRemaining = createLabel("lbl.remaining");
        JLabel lblDate = createLabel("lbl.date");
        JFormattedTextField txtMonthly = createBigDecimalDisplayField(monitorDetailModel.getModel(BudgetDTO.PROP_AMOUNT_PER_MONTH), 80);
        JFormattedTextField txtDaily = createBigDecimalDisplayField(monitorDetailModel.getModel(BudgetDTO.PROP_AMOUNT_PER_DAY));
        JFormattedTextField txtUpToDay = createBigDecimalDisplayField(monitorDetailModel.getModel(BudgetDTO.PROP_AMOUNT_UPTODAY));
        JFormattedTextField txtRemaining = createBigDecimalDisplayField(monitorDetailModel.getModel(BudgetDTO.PROP_AMOUNT_REMAINING));
        JSpinner spnMonitorDate = UIFactory.createDateSpinner(presenter.getMonitorDateSpinnerModel(monitorDetailModel
                .getModel(BudgetDTO.PROP_MONITOR_DATE)));

        // create the panel
        JPanel pnlMon = new JPanel();
        GroupLayout layout = new GroupLayout(pnlMon);
        pnlMon.setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup().addComponent(lblMonthly).addComponent(lblDate).addComponent(lblDaily)
                .addComponent(lblUptoDay).addComponent(lblRemaining));
        hGroup.addGroup(layout.createParallelGroup().addComponent(txtMonthly).addComponent(txtDaily).addComponent(txtUpToDay)
                .addComponent(txtRemaining).addComponent(spnMonitorDate));
        layout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(lblMonthly).addComponent(txtMonthly));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(lblDaily).addComponent(txtDaily));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(lblUptoDay).addComponent(txtUpToDay));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(lblRemaining).addComponent(txtRemaining));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(lblDate).addComponent(spnMonitorDate));
        layout.setVerticalGroup(vGroup);
       
        return pnlMon;
        


    }

    /**
     * Erstellt das Panel mit der Liste mit den Monitoren
     * 
     * @return
     */
    private Component getMonitorList() {

        // JList with Monitor
        monitorList = BasicComponentFactory.createList(monitorListModel, new BudgetMonitorCellListRenderer());
        JScrollPane pane = new JScrollPane(monitorList);
        pane.setPreferredSize(new Dimension(200, 150));
        
        // add listener
        monitorList.addMouseListener(new MouseAdapter() {

            /**
             * Ueberschreibt Methode von Superklasse {@see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    presenter.edit((BudgetDTO)monitorList.getSelectedValue());
                }                
            }
        });
        return pane;

    }

    @Action
    public void deleteMonitorAction(ActionEvent e) {
        presenter.delete((BudgetDTO)monitorList.getSelectedValue());
    }

    @Action
    public void newMonitorAction(ActionEvent e) {
        presenter.create();
    }

}
