/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-04-19 15:06:40 +0200 (Di, 19 Apr 2011) $
 *   $Author: tom $
 *   $Revision: 2114 $ 
 */
package ch.tkayser.budget.swing.account;

import static ch.tkayser.budget.swing.widgets.UIFactory.*;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.tkayser.budget.dto.AccountDTO;
import ch.tkayser.budget.swing.widgets.AbstractBudgetDialog;

import com.jgoodies.binding.list.SelectionInList;

/**
 * Dialog zum bearbeiten eines Kontos
 */
public class AccountEditView extends AbstractBudgetDialog<AccountDTO> {

    private static final long serialVersionUID = 1L;

    // the account presenter
    private final AccountPresenter accountPresenter;

    // the selectionInList fuer das ParentAccount
    private SelectionInList<AccountDTO> parentAccountSelection;

    public AccountEditView(AccountPresenter presenter) {
        super(presenter);
        this.accountPresenter = presenter;
        // inhalt setzen
        setDialogContent(getContentPanel());
    }

    /**
     * Inhalts panel erstellen TODO: Bitte durch Methoden Beschreibung ersetzen
     * 
     * @return
     */
    private JPanel getContentPanel() {

        // Labels
        JLabel lblName = createLabel("lbl.name");
        JLabel lblParent = createLabel("lbl.parent");

        // Fields
        JTextField txtName = createTextEditField(accountPresenter.getEditModel().getModel(AccountDTO.PROP_NAME), AccountDTO.PROP_NAME,
                accountPresenter.getEditValidator(), 120);
        parentAccountSelection = accountPresenter.getSelectionInList(false);
        JComboBox cbParent = createComboBox(accountPresenter.getEditModel(), AccountDTO.PROP_PARENT, accountPresenter.getEditValidator(),
                parentAccountSelection, new AccountListCellRenderer());

        JPanel pnlFields = new JPanel();
        GroupLayout fieldsLayout = new GroupLayout(pnlFields);
        pnlFields.setLayout(fieldsLayout);
        fieldsLayout.setAutoCreateContainerGaps(true);
        fieldsLayout.setAutoCreateGaps(true);

        // horizontal: labels and text fields parallel
        GroupLayout.SequentialGroup hGroup = fieldsLayout.createSequentialGroup();
        hGroup.addGroup(fieldsLayout.createParallelGroup().addComponent(lblName).addComponent(lblParent));
        hGroup.addGroup(fieldsLayout.createParallelGroup().addComponent(txtName).addComponent(cbParent));
        fieldsLayout.setHorizontalGroup(hGroup);

        // vertical: lbl and textfield parallel
        GroupLayout.SequentialGroup vGroup = fieldsLayout.createSequentialGroup();
        vGroup.addGroup(fieldsLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblName).addComponent(txtName));
        vGroup.addGroup(fieldsLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblParent).addComponent(cbParent));
        fieldsLayout.setVerticalGroup(vGroup);

        return pnlFields;
    }


}
