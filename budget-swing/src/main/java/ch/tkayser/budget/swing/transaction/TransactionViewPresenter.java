/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-05-01 13:53:59 +0200 (So, 01 Mai 2011) $
 *   $Author: tom $
 *   $Revision: 2139 $ 
 */
package ch.tkayser.budget.swing.transaction;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.jdesktop.application.Application;

import ch.tkayser.budget.dto.AccountDTO;
import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.parser.TransactionParser;
import ch.tkayser.budget.swing.Constants;
import ch.tkayser.budget.swing.MainApplication;
import ch.tkayser.budget.swing.MainPresenter;
import ch.tkayser.budget.swing.common.AbstractBudgetTask;
import ch.tkayser.budget.swing.common.ExceptionDialog;
import ch.tkayser.budget.swing.presenter.AbstractBudgetEditPresenter;
import ch.tkayser.budget.swing.validation.BudgetValidator;
import ch.tkayser.budget.swing.widgets.AbstractBudgetDialog;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.list.SelectionInList;

/**
 * Presenter fuer Transactions
 */
public class TransactionViewPresenter extends AbstractBudgetEditPresenter<TransactionDTO> {

    /**
     * Task zum loeschen einer Buchung
     */
    private class DeleteTransactionTask extends AbstractBudgetTask<Void, Void> {

        // the account to save
        private TransactionDTO tx;

        public DeleteTransactionTask(TransactionDTO tx) {
            this.tx = tx;
        }

        protected Void doInBackground() throws Exception {

            if (txSource == TXSource.DB) {
                // delete the tx in the db
                getService().deleteTransaction(tx);
            }

            // remove the tx from the list
            removeFromTableModel(tx);

            return null;
        }

    }

    /**
     * Task zum speichern einer Buchung
     */
    private class SaveTransaction extends AbstractBudgetTask<Void, Void> {

        // the budget to save
        private TransactionDTO tx;

        public SaveTransaction(TransactionDTO tx) {
            this.tx = tx;
        }

        protected Void doInBackground() throws Exception {
            if (txSource == TXSource.DB) {
                // save the tx in the db
                tx = getService().saveTransaction(tx);
            }

            // refresh the transaction in the TableModel
            replaceInTableModel(tx);

            return null;
        }

    }

    /**
     * Task zum speichern der importierten Buchungen
     */
    private class SaveImportedTransactions extends AbstractBudgetTask<Void, Void> {

        // the budget to save
        private List<TransactionDTO> tx;

        public SaveImportedTransactions(List<TransactionDTO> tx) {
            this.tx = tx;
        }

        protected Void doInBackground() throws Exception {
            for (TransactionDTO transaction : tx) {
                getService().saveTransaction(transaction);
            }

            // clear the model
            tableModel.clear();

            return null;
        }

    }

    // @formatter:off

    /**
     * Enumeration for the Source of the transactions
     */
    private enum TXSource {
        DB, IMPORT;
    }

    // The Source of the displayed Tx
    private TXSource                          txSource      = TXSource.DB;

    // the number of the edited Tx in the tableModel
    private static final int                  NO_ROW_EDITED = -1;
    private int                               editedTxRow   = NO_ROW_EDITED;

    // Models
    // TableModel
    private TransactionTableModel             tableModel    = new TransactionTableModel(); ;

    // the edit model, validator and dialog
    private PresentationModel<TransactionDTO> editModel;
    private BudgetValidator<TransactionDTO>   validator;
    private TransactionEditView               editDialog;

    // the import view
    private TransactionImportView             importDialog;

    // @formatter:on

    /**
     * 
     * @param mainPresenter
     */
    public TransactionViewPresenter(MainPresenter mainPresenter) {
        super(mainPresenter);
    }

    /**
     * Transaction im TableModel erstezen
     * 
     * @param tx
     */
    public void replaceInTableModel(TransactionDTO tx) {
        // Transaction ersezten: wenn keine editiert wurde, hinzufeugen
        if (editedTxRow == NO_ROW_EDITED) {
            tableModel.addTransaction(tx);
        } else {
            tableModel.replaceTransaction(editedTxRow, tx);
        }
    }

    /**
     * Transaction aus dem TableModel entfernen
     * 
     * @param tx
     * 
     * @param tx
     */
    public void removeFromTableModel(TransactionDTO tx) {
        tableModel.removeTransaction(tableModel.getRowInModel(tx));
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see
     * ch.tkayser.budget.swing.presenter.Presenter#delete(ch.tkayser.budget.base
     * .dto.BaseDTO)}
     * 
     * @param bean
     */
    @Override
    public void delete(TransactionDTO bean) {
        executeTask(new DeleteTransactionTask(bean));
    }

    /**
     * refresh the transactionTableModel
     * 
     * @param from
     * @param to
     * @param account
     */
    public void refreshTransactions(Date from, Date to, AccountDTO account, boolean withChildren) {
        List<TransactionDTO> tx = null;
        if (account == null) {
            tx = getService().findTransactions(from, to);
        } else {
            tx = getService().findTransactions(from, to, account.getId(), withChildren);
        }
        tableModel.setTransactions(tx);
        // tx Source wechseln
        txSource = TXSource.DB;

    }

    /**
     * liefert eine SelectionInList mit Konti zurueck
     * 
     * @return
     */
    public SelectionInList<AccountDTO> getAccountSelection(boolean mandatory) {
        return getMainPresenter().getAccountPresenter().getSelectionInList(mandatory);
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see
     * ch.tkayser.budget.swing.presenter.AbstractBudgetPresenter#getEditDialog()
     * * }
     * 
     * @return
     */
    @Override
    public AbstractBudgetDialog<TransactionDTO> getEditDialog() {
        if (editDialog == null) {
            editDialog = new TransactionEditView(this);
        }
        return editDialog;
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see
     * ch.tkayser.budget.swing.presenter.Presenter#getEditModel()}
     * 
     * @return
     */
    @Override
    public PresentationModel<TransactionDTO> getEditModel() {
        if (editModel == null) {
            editModel = new PresentationModel<TransactionDTO>(new TransactionDTO());
        }
        return editModel;
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see
     * ch.tkayser.budget.swing.presenter.Presenter#getEditValidator()}
     * 
     * @return
     */
    @Override
    public BudgetValidator<TransactionDTO> getEditValidator() {
        if (validator == null) {
            validator = new BudgetValidator<TransactionDTO>(getEditModel());
        }
        return validator;
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see
     * ch.tkayser.budget.swing.presenter.AbstractBudgetPresenter#
     * getNewBeanInstance()}
     * 
     * @return
     */
    @Override
    protected TransactionDTO getNewBeanInstance() {
        return new TransactionDTO();
    }

    /**
     * gibt das Tabellen Model zurueck
     * 
     * @return
     */
    public TransactionTableModel getTableModel() {
        return this.tableModel;
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see
     * ch.tkayser.budget.swing.presenter.Presenter#save(ch.tkayser.budget.base.
     * dto.BaseDTO)}
     * 
     * @param bean
     */
    @Override
    public void save(TransactionDTO bean) {
        executeTask(new SaveTransaction(bean));
    }

    /**
     * TransactionImport Dialog anzeigen
     * 
     */
    public void importTransactions() {
        if (importDialog == null) {
            importDialog = new TransactionImportView(this);
        }
        importDialog.setVisible(true);
    }

    /**
     * import Transactions with a parser
     * 
     * @param filePath
     * @param parser
     */
    public void importTransactions(String filePath, TransactionParser parser) {
        try {
            List<TransactionDTO> transactions = getService().bookTransactions(
                    parser.parserTransaction(new FileInputStream(new File(filePath))));
            // tx anzeigen und source aendern
            tableModel.setTransactions(transactions);
            txSource = TXSource.IMPORT;
        } catch (Exception e) {
            ExceptionDialog.showDialg(Constants.ERROR_IMPORT_FILE, e);
        }

    }

    /**
     * Ueberschreibt Methode von Superklasse {@see
     * ch.tkayser.budget.swing.presenter.AbstractBudgetPresenter#edit(ch.tkayser
     * .budget.base.dto.BaseDTO)}
     * 
     * @param bean
     */
    @Override
    public void edit(TransactionDTO bean) {
        // store the number
        editedTxRow = tableModel.getRowInModel(bean);
        super.edit(bean);
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see
     * ch.tkayser.budget.swing.presenter.AbstractBudgetPresenter#create()}
     * 
     */
    @Override
    public void create() {
        editedTxRow = NO_ROW_EDITED;
        super.create();
    }

    /**
     * Speichert alle importierten Tx.
     * 
     */
    public void saveAllImportedTransactions() {
        // alle tx validieren
        PresentationModel<TransactionDTO> model = new PresentationModel<TransactionDTO>(new TransactionDTO());
        BudgetValidator<TransactionDTO> validator = new BudgetValidator<TransactionDTO>(model);

        // init set with violations
        Set<ConstraintViolation<?>> violations = new HashSet<ConstraintViolation<?>>();
        for (TransactionDTO tx : tableModel.getTransactions()) {
            model.setBean(tx);
            violations.addAll(validator.validateBean());
        }

        // ok?
        if (violations.size() == 0) {
            executeTask(new SaveImportedTransactions(tableModel.getTransactions()));
        } else {
            ConstraintViolationException exception = new ConstraintViolationException(violations);
            ExceptionDialog.showDialg(translateKey(Constants.ERROR_SAVING), exception);
        }
    }

    /**
     * i18n
     * 
     * @param key
     * @return
     */
    protected String translateKey(String key) {
        return Application.getInstance(MainApplication.class).getContext().getResourceMap().getString(key);
    }
}
