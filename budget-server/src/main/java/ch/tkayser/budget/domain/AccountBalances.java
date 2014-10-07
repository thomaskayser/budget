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
package ch.tkayser.budget.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ch.tkayser.budget.util.BudgetUtil;

public class AccountBalances implements Serializable  {

    private static final long serialVersionUID = -1125792423499195249L;

    // the account
    private Account m_account;

    // the list with the balances
    private List<BigDecimal> m_timeGroupBalances = new ArrayList<BigDecimal>();

    // the total balance
    private BigDecimal m_balance;

    // the avg balance
    private BigDecimal m_avgBalance;

    /**
     * @return the account
     */
    public Account getAccount() {
	return m_account;
    }

    /**
     * @return the avgBalance
     */
    public BigDecimal getAvgBalance() {
	return m_avgBalance;
    }

    /**
     * @return the balance
     */
    public BigDecimal getBalance() {
	return m_balance;
    }

    /**
     * @return the timeGroupbalances
     */
    public List<BigDecimal> getTimeGroupBalances() {
	return m_timeGroupBalances;
    }

    /**
     * @param account
     *            the account to set
     */
    public void setAccount(Account account) {
	this.m_account = account;
    }

    /**
     * @param avgBalance
     *            the avgBalance to set
     */
    public void setAvgBalance(BigDecimal avgBalance) {
	m_avgBalance = avgBalance;
    }

    /**
     * @param balance
     *            the balance to set
     */
    public void setBalance(BigDecimal balance) {
	m_balance = balance;
    }

    /**
     * @param timeGroupbalances
     *            the timeGroupbalances to set
     */
    public void setTimeGroupBalances(List<BigDecimal> timeGroupbalances) {
	m_timeGroupBalances = timeGroupbalances;
    }

    /**
     * add the timegroup balances of another account to this one
     * 
     * @param otherAccount
     */
    public void addTimeGroupBalances(AccountBalances otherAccount) {
	// equal timeGroups ?
	if (otherAccount.getTimeGroupBalances().size() != m_timeGroupBalances.size()) {
	    return;
	}
	// add timegroup balances of other account
	for (int i = 0; i < m_timeGroupBalances.size(); i++) {
	    // get the sum of this account
	    BigDecimal sum = m_timeGroupBalances.get(i);
	    // add the others sum
	    sum = sum.add(otherAccount.getTimeGroupBalances().get(i));
	    sum = BudgetUtil.setScale(sum);
	    // set the new sum on the parent
	    m_timeGroupBalances.set(i, sum);
	}
    }

}
