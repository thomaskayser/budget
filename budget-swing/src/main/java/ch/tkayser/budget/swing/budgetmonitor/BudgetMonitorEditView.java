/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-04-16 20:13:02 +0200 (Sa, 16 Apr 2011) $
 *   $Author: tom $
 *   $Revision: 2102 $ 
 */
package ch.tkayser.budget.swing.budgetmonitor;

import static ch.tkayser.budget.swing.widgets.UIFactory.*;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.tkayser.budget.dto.BudgetDTO;
import ch.tkayser.budget.swing.presenter.EditPresenter;
import ch.tkayser.budget.swing.widgets.AbstractBudgetDialog;

/**
 * Dialog zum bearbeiten eines Monitors
 */
public class BudgetMonitorEditView extends AbstractBudgetDialog<BudgetDTO> {

    private static final long serialVersionUID = 1L;
    
    // the presenter
    private final EditPresenter<BudgetDTO> budgetPresenter;

    /**
     * 
     * 
     * @param parent
     * 
     */
    public BudgetMonitorEditView(EditPresenter<BudgetDTO> presenter) {
        super(presenter);
        this.budgetPresenter = presenter;
        // content setzen
        setDialogContent(getContentPanel());
    }

    /**
     * Inhalt erstellen
     *
     * @return
     */
    private JPanel getContentPanel() {
        // Labels
        JLabel lblName = createLabel("lbl.name");
        JLabel lblMonthly = createLabel("lbl.monthly");

        // TextFields
        JTextField txtName = createTextEditField(budgetPresenter.getEditModel().getModel(BudgetDTO.PROP_NAME), BudgetDTO.PROP_NAME, budgetPresenter.getEditValidator(), 120);
        JFormattedTextField txtMonthly = createBigDecimalEditField(budgetPresenter.getEditModel().getModel(BudgetDTO.PROP_AMOUNT_PER_MONTH),
                BudgetDTO.PROP_AMOUNT_PER_MONTH, budgetPresenter.getEditValidator());

        JPanel pnlFields = new JPanel();
        GroupLayout fieldsLayout = new GroupLayout(pnlFields);
        pnlFields.setLayout(fieldsLayout);
        fieldsLayout.setAutoCreateContainerGaps(true);
        fieldsLayout.setAutoCreateGaps(true);

        // horizontal: labels and text fields parallel
        GroupLayout.SequentialGroup hGroup = fieldsLayout.createSequentialGroup();
        hGroup.addGroup(fieldsLayout.createParallelGroup().addComponent(lblName).addComponent(lblMonthly));
        hGroup.addGroup(fieldsLayout.createParallelGroup().addComponent(txtName).addComponent(txtMonthly));
        fieldsLayout.setHorizontalGroup(hGroup);

        // vertical: lbl and textfield parallel
        GroupLayout.SequentialGroup vGroup = fieldsLayout.createSequentialGroup();
        vGroup.addGroup(fieldsLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblName).addComponent(txtName));
        vGroup.addGroup(fieldsLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblMonthly).addComponent(txtMonthly));
        fieldsLayout.setVerticalGroup(vGroup);

        return pnlFields;
    }

}
