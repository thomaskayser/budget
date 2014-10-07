/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-04-14 16:26:43 +0200 (Do, 14 Apr 2011) $
 *   $Author: tom $
 *   $Revision: 2100 $ 
 */
package ch.tkayser.budget.swing.balance;

import java.util.Date;

import ch.tkayser.budget.dto.TimeGroup;
import ch.tkayser.budget.swing.MainPresenter;
import ch.tkayser.budget.swing.presenter.AbstractBudgetPresenter;

/**
 * Presenter fuer Transactions
 */
public class BalancePresenter extends AbstractBudgetPresenter {

    // Models
    // TreeTableModel
    private BalanceTreeTableModel model = new BalanceTreeTableModel();

    // @formatter:on

    /**
     * 
     * @param mainPresenter
     */
    public BalancePresenter(MainPresenter mainPresenter) {
        super(mainPresenter);
    }

    /**
     * gibt das Model fuer die Saldi zurueck
     * 
     * @return
     */
    public BalanceTreeTableModel getBalanceModel() {
        return model;
    }

    /**
     * find Transactions and refresh the transactionTableModel
     * 
     * @param from
     * @param to
     * @param account
     */
    public void refreshBalances(Date from, Date to, TimeGroup timeGroup) {
        model.setSheet(getService().getAccountBalances(from, to, timeGroup));
    }

}
