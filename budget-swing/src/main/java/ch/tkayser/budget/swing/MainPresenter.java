/*
 * Software is written by:
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2011
 * 
 */
package ch.tkayser.budget.swing;

import ch.tkayser.budget.swing.account.AccountPresenter;
import ch.tkayser.budget.swing.balance.BalancePresenter;
import ch.tkayser.budget.swing.budgetmonitor.BudgetMonitorPresenter;
import ch.tkayser.budget.swing.transaction.TransactionViewPresenter;

/**
 * Main Presenter
 * 
 * @author tom
 * 
 */
public class MainPresenter {

    //@formatter:off
    // sub presenters
    private AccountPresenter             accountPresenter;
    private BudgetMonitorPresenter       monitorPresenter;
    private TransactionViewPresenter     transactionPresenter;
    private BalancePresenter             balancePresenter;
    //@formatter:on

    public MainPresenter() {
        super();
    }

    /**
     * liefert die AccountPresenter Instanz zurueck
     * 
     * @return
     */
    public AccountPresenter getAccountPresenter() {
        if (accountPresenter == null) {
            accountPresenter = new AccountPresenter(this);
        }
        return this.accountPresenter;
    }

    /**
     * liefert die BudgetMonitorPresenter instanz zurueck
     * 
     * @return
     */
    public BudgetMonitorPresenter getBudgetMonitorPresenter() {
        if (monitorPresenter == null) {
            monitorPresenter = new BudgetMonitorPresenter(this);
        }
        return this.monitorPresenter;
    }

    /**
     * liefert die BudgetMonitorPresenter instanz zurueck
     * 
     * @return
     */
    public TransactionViewPresenter getTransactionPresenter() {
        if (transactionPresenter == null) {
            transactionPresenter = new TransactionViewPresenter(this);
        }
        return this.transactionPresenter;
    }

    /**
     * liefert die BalancePresenter instanz zurueck
     * 
     * @return
     */
    public BalancePresenter getBalancePresenter() {
        if (balancePresenter == null) {
            balancePresenter = new BalancePresenter(this);
        }
        return this.balancePresenter;
    }
}
