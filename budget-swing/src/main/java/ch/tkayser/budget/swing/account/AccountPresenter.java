/*
 * Software is written by:
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2011
 * 
 */
package ch.tkayser.budget.swing.account;

import java.util.ArrayList;
import java.util.List;

import ch.tkayser.budget.dto.AccountDTO;
import ch.tkayser.budget.exception.BudgetException;
import ch.tkayser.budget.swing.MainPresenter;
import ch.tkayser.budget.swing.common.AbstractBudgetTask;
import ch.tkayser.budget.swing.common.ManagedSelectionInList;
import ch.tkayser.budget.swing.presenter.AbstractBudgetEditPresenter;
import ch.tkayser.budget.swing.validation.BudgetValidator;
import ch.tkayser.budget.swing.widgets.AbstractBudgetDialog;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.list.SelectionInList;

/**
 * @author tom
 * 
 */
public class AccountPresenter extends AbstractBudgetEditPresenter<AccountDTO> {

    /**
     * Task zum loeschen eines Kontos
     */
    private class DeleteAccountTask extends AbstractBudgetTask<Void, Void> {

        // the account to save
        private final AccountDTO account;

        public DeleteAccountTask(AccountDTO account) {
            this.account = account;
        }

        protected Void doInBackground() throws Exception {
            // delete the account
            getService().deleteAccount(account);

            // refresh the account models
            refreshAccountModels();

            return null;
        }

    }

    /**
     * Task zum speichern eines Accounts
     */
    private class SaveAccountTask extends AbstractBudgetTask<Void, Void> {

        // the budget to save
        private final AccountDTO account;

        public SaveAccountTask(AccountDTO account) {
            this.account = account;
        }

        protected Void doInBackground() throws Exception {
            // save the bean and refresh the accounts
            getService().saveAccount(account);
            // refresh the account models
            refreshAccountModels();
            return null;
        }

    }

    // @formatter:off
    // list with all accounts
    private List<AccountDTO> allAccounts;

    // modells
    // Tree Model
    private AccountTreeModel treeModel;

    // selectionInLIst Models
    private List<ManagedSelectionInList<AccountDTO>> selectionModels = new ArrayList<ManagedSelectionInList<AccountDTO>>();

    // the edit model, validator and dialog
    private PresentationModel<AccountDTO>            editModel;
    private BudgetValidator<AccountDTO>              validator;
    private AccountEditView                          editDialog;

    // @formatter:on

    /**
     * 
     * @param mainPresenter
     * 
     * @param bean
     * @param service
     * @throws BudgetException
     */
    public AccountPresenter(MainPresenter mainPresenter) {
        super(mainPresenter);

        // liste mit konti initialisieren
        refreshAccountModels();

    }

    /**
     * gibt ein Tree Model der Accounts zurueck
     * 
     * @return
     */
    public AccountTreeModel getAccountTreeModel() {
        if (treeModel == null) {
            treeModel = new AccountTreeModel();
            treeModel.setAccounts(allAccounts);
        }

        return treeModel;
    }

    /**
     * gibt ein SelectionInList Model mit allen Konti zurueck.
     * 
     * Der ValueHolder muss vom Bezuger gesetzt werden. Der Presenter aktualisiert die List
     * 
     * @return
     */
    public SelectionInList<AccountDTO> getSelectionInList(boolean mandatory) {
        // Selection erstellen
        ManagedSelectionInList<AccountDTO> selection = new ManagedSelectionInList<AccountDTO>(mandatory, new SelectionInList<AccountDTO>());
        selection.fillSelection(allAccounts);
        selectionModels.add(selection);
        return selection.getSelectionInList();
    }

    /**
     * Alle AccountModelle refreshen
     * 
     */
    public void refreshAccountModels() {
        // konti neu laden
        allAccounts = getService().getAllAccounts();

        // modelle updaten
        if (treeModel != null) {
            treeModel.setAccounts(allAccounts);
        }
        for (ManagedSelectionInList<AccountDTO> selection : selectionModels) {
            selection.fillSelection(allAccounts);
        }

    }

    /**
     * Ueberschreibt Methode von Superklasse {@see ch.tkayser.budget.swing.presenter.AbstractBudgetEditPresenter#getNewBeanInstance()}
     * 
     * @return
     */
    @Override
    protected AccountDTO getNewBeanInstance() {
        return new AccountDTO();
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see ch.tkayser.budget.swing.presenter.Presenter#delete(ch.tkayser.budget.base.dto.BaseDTO)}
     * 
     * @param bean
     */
    @Override
    public void delete(AccountDTO bean) {
        executeTask(new DeleteAccountTask(bean));
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see ch.tkayser.budget.swing.presenter.Presenter#save(ch.tkayser.budget.base.dto.BaseDTO)}
     * 
     * @param bean
     */
    @Override
    public void save(AccountDTO bean) {
        executeTask(new SaveAccountTask(bean));
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see ch.tkayser.budget.swing.presenter.Presenter#getEditModel()}
     * 
     * @return
     */
    @Override
    public PresentationModel<AccountDTO> getEditModel() {
        if (editModel == null) {
            editModel = new PresentationModel<AccountDTO>(new AccountDTO());
        }
        return editModel;

    }

    /**
     * Ueberschreibt Methode von Superklasse {@see ch.tkayser.budget.swing.presenter.Presenter#getEditValidator()}
     * 
     * @return
     */
    @Override
    public BudgetValidator<AccountDTO> getEditValidator() {
        if (validator == null) {
            validator = new BudgetValidator<AccountDTO>(getEditModel());
        }
        return validator;
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see ch.tkayser.budget.swing.presenter.AbstractBudgetPresenter#getEditDialog()}
     * 
     * @return
     */
    @Override
    public AbstractBudgetDialog<AccountDTO> getEditDialog() {
        if (editDialog == null) {
            editDialog = new AccountEditView(this);
        }
        return editDialog;
    }

}
