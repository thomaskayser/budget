 /*
 * Software is written by:
 *
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2009
 * 
 */
package ch.tkayser.budget.dao;

import java.util.Date;
import java.util.List;

import ch.tkayser.budget.base.dao.BaseDAO;
import ch.tkayser.budget.domain.Account;
import ch.tkayser.budget.domain.AccountBalanceForTimeGroup;
import ch.tkayser.budget.domain.Transaction;
import ch.tkayser.budget.dto.TimeGroup;

public interface BudgetDAO extends BaseDAO {

    /**
     * 
     * @param from
     * @param to
     * @return
     */
    public List<Transaction> findTransactions(Date from, Date to);

    /**
     * 
     * @param from
     * @param to
     * @param account
     * @return
     */
    public List<Transaction> findTransactions(Date from, Date to, List<Long> accountIds);

    /**
     * find tx with a booking text
     * 
     * @param bookingText
     * @return
     */
    public List<Transaction> findTransactions(String reciever, String sender, String bookingText);
    
    /**
     * calculate the balances for all accounts for a date range
     * @param from
     * @param to
     * @param group
     * @return the balances for the accounts and date ranges
     */
    public List<AccountBalanceForTimeGroup> countBalances(Date from, Date to, TimeGroup group);

}
