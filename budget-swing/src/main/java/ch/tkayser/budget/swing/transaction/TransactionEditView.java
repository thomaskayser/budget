/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-04-06 11:47:33 +0200 (Mi, 06 Apr 2011) $
 *   $Author: tom $
 *   $Revision: 2071 $ 
 */
package ch.tkayser.budget.swing.transaction;

import static ch.tkayser.budget.swing.widgets.UIFactory.*;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.swing.account.AccountListCellRenderer;
import ch.tkayser.budget.swing.widgets.AbstractBudgetDialog;

/**
 * Dialog zum bearbeiten eines Kontos
 */
public class TransactionEditView extends AbstractBudgetDialog<TransactionDTO> {

    private static final long serialVersionUID = 1L;

    //@formatter:off
    
    // the account presenter
    private final TransactionViewPresenter transactionPresenter;

    
    //@formatter:on

    public TransactionEditView(TransactionViewPresenter presenter) {
        super(presenter);
        this.transactionPresenter = presenter;
        // inhalt setzen
        setDialogContent(getContentPanel());
    }

    /**
     * Inhalts panel erstellen
     * 
     * @return
     */
    private JPanel getContentPanel() {

        // Labels
        JLabel lblValuta = createLabel("lbl.valuta");
        JLabel lblAccount = createLabel("lbl.account");
        JLabel lblAmount = createLabel("lbl.amount");
        JLabel lblBookingText = createLabel("lbl.bookingText");
        JLabel lblReciever = createLabel("lbl.reciever");
        JLabel lblSender = createLabel("lbl.sender");

        // Fields
        // Valuta
        JFormattedTextField txtValuta = createDateEditField(transactionPresenter.getEditModel().getModel(TransactionDTO.PROP_VALUTA),
                TransactionDTO.PROP_VALUTA, transactionPresenter.getEditValidator(), 80);

        // textAmount
        JFormattedTextField txtAmount = createBigDecimalEditField(
                transactionPresenter.getEditModel().getModel(TransactionDTO.PROP_AMOUNT), TransactionDTO.PROP_AMOUNT,
                transactionPresenter.getEditValidator());

        // Account
        JComboBox cbAccount = createComboBox(transactionPresenter.getEditModel(), TransactionDTO.PROP_ACCOUNT,
                transactionPresenter.getEditValidator(), transactionPresenter.getAccountSelection(true), new AccountListCellRenderer());

        // booking texts
        JTextField txtBookingText = createTextEditField(transactionPresenter.getEditModel().getModel(TransactionDTO.PROP_BOOKINGTEXT),
                TransactionDTO.PROP_BOOKINGTEXT, transactionPresenter.getEditValidator());

        // reciever
        JTextField txtReciever = createTextEditField(transactionPresenter.getEditModel().getModel(TransactionDTO.PROP_RECIEVER),
                TransactionDTO.PROP_RECIEVER, transactionPresenter.getEditValidator());

        // sender
        JTextField txtSender = createTextEditField(transactionPresenter.getEditModel().getModel(TransactionDTO.PROP_SENDER),
                TransactionDTO.PROP_SENDER, transactionPresenter.getEditValidator());

        // layout
        JPanel pnlFields = new JPanel();
        GroupLayout fieldsLayout = new GroupLayout(pnlFields);
        pnlFields.setLayout(fieldsLayout);
        fieldsLayout.setAutoCreateContainerGaps(true);
        fieldsLayout.setAutoCreateGaps(true);

        // horizontal: labels and text fields parallel
        GroupLayout.SequentialGroup hGroup = fieldsLayout.createSequentialGroup();
        hGroup.addGroup(fieldsLayout.createParallelGroup().addComponent(lblValuta).addComponent(lblAmount).addComponent(lblAccount)
                .addComponent(lblBookingText).addComponent(lblReciever).addComponent(lblSender));
        hGroup.addGroup(fieldsLayout.createParallelGroup().addComponent(txtValuta).addComponent(txtAmount).addComponent(cbAccount)
                .addComponent(txtBookingText).addComponent(txtReciever).addComponent(txtSender));
        fieldsLayout.setHorizontalGroup(hGroup);

        // vertical: lbl and textfield parallel
        GroupLayout.SequentialGroup vGroup = fieldsLayout.createSequentialGroup();
        vGroup.addGroup(fieldsLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblValuta).addComponent(txtValuta));
        vGroup.addGroup(fieldsLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblAmount).addComponent(txtAmount));
        vGroup.addGroup(fieldsLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblAccount).addComponent(cbAccount));
        vGroup.addGroup(fieldsLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblBookingText).addComponent(txtBookingText));
        vGroup.addGroup(fieldsLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblReciever).addComponent(txtReciever));
        vGroup.addGroup(fieldsLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblSender).addComponent(txtSender));
        fieldsLayout.setVerticalGroup(vGroup);

        return pnlFields;
    }

}
