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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.tkayser.budget.dto.TimeGroup;
import ch.tkayser.budget.util.BudgetUtil;

/**
 * Sheet with Balances for all Accounts with totals
 * 
 * @author isc-kat
 * 
 */
public class BalanceSheet implements Serializable {

    private static final long serialVersionUID = 219034530255129147L;

    // rows with account balances
    private Map<Account, AccountBalances> m_accountBalances;

    // group totals
    private List<BigDecimal> m_groupTotals;

    // the grand total
    private BigDecimal m_grandTotal;

    // the total avg
    private BigDecimal m_grandAvg;

    // the group keys
    private List<Integer> m_groupKeys;

    // a logger
    private Logger m_log = LoggerFactory.getLogger(BalanceSheet.class);

    // the criteria the sheet is grouped by
    private final TimeGroup m_groupedBy;

    public BalanceSheet(List<Account> accounts, Date from, Date to, TimeGroup groupedBy) {

        if (groupedBy == null) {
            throw new RuntimeException("groupedBy is required");
        }

        // store grouped by
        m_groupedBy = groupedBy;

        // get the keys of the groups for this TimeGroup and interval
        m_groupKeys = TimeGroup.getGroupKeys(from, to, groupedBy);

        // add one dummy key for ungrouped sheets
        if (m_groupedBy.equals(TimeGroup.NONE)) {
            m_groupKeys = new ArrayList<Integer>();
            m_groupKeys.add(new Integer(-1));
        }

        // get the number of groups for the interval and group
        int nrOfGroups = m_groupKeys.size();

        // init the balances for all accounts
        m_accountBalances = new HashMap<Account, AccountBalances>();
        for (Account account : accounts) {
            // init the accountbalances
            AccountBalances acBalances = new AccountBalances();
            acBalances.setAccount(account);

            // create the grouped values
            List<BigDecimal> groupedValues = new ArrayList<BigDecimal>();
            for (int i = 0; i < nrOfGroups; i++) {
                groupedValues.add(new BigDecimal("0"));
            }
            acBalances.setTimeGroupBalances(groupedValues);

            // add the row
            m_accountBalances.put(account, acBalances);
        }

    }

    /**
     * add the balance of an account for a timegroup to the matrix
     * 
     * @param balance
     */
    public void addAccountBalance(AccountBalanceForTimeGroup balance) {
        // get the accountbalances for the account
        AccountBalances acBalances = m_accountBalances.get(balance.getAccount());
        // get the index for the group key
        int keyIndex = -1;
        if (!m_groupedBy.equals(TimeGroup.NONE)) {
            keyIndex = m_groupKeys.indexOf(balance.getTimeKey());
        } else {
            // ungrouped. allways in first group
            keyIndex = 0;
        }
        // add the balance to any existing entries
        BigDecimal existingBalance = acBalances.getTimeGroupBalances().get(keyIndex);
        existingBalance = existingBalance.add(balance.getBalance());
        acBalances.getTimeGroupBalances().set(keyIndex, existingBalance);
    }

    /**
     * add all balances
     * 
     * @param balances
     */
    public void addAccountBalances(List<AccountBalanceForTimeGroup> balances) {
        for (AccountBalanceForTimeGroup balance : balances) {
            addAccountBalance(balance);
        }
    }

    /**
     * Add the balance of all children to an account
     * 
     * @param account
     */
    private void addSumOfChildrenBalances(Account account) {
        addSumOfChildrenBalances(account, 0);
    }

    /**
     * add the sum of the children to the sum of an account (RECURSIVE)
     * 
     * @param account
     */
    private void addSumOfChildrenBalances(Account account, int level) {

        m_log.debug("{} Summing up children of {}", getIdent(level), account.getName());

        // sum up amount of all children (recursive)
        for (Account subAccount : account.getChildren()) {

            m_log.debug("{} - child {}", getIdent(level), subAccount.getName());

            if (subAccount.getChildren().size() > 0) {
                // has Children: calculate recursive
                addSumOfChildrenBalances(subAccount, level + 1);
            }
            // add the balances of the sub account to this one
            m_log.debug("{} = Add sum of child {}", getIdent(level), subAccount.getName());

            m_accountBalances.get(account).addTimeGroupBalances(m_accountBalances.get(subAccount));
        }
    }

    /**
     * calculates the matrix columns:
     * 
     * * sums up accounts with hierarchies * calculates the sum and avg for each account row * calculate the total row
     */
    public void calculateMatrixColumns() {

        // Calculate the groupTotals, grand total and grand avg. This must be
        // done before summing up

        // init grandTotal, grandAvg und group Totals
        m_grandTotal = new BigDecimal("0");
        m_grandAvg = new BigDecimal("0");
        m_groupTotals = new ArrayList<BigDecimal>();
        for (int i = 0; i < m_groupKeys.size(); i++) {
            m_groupTotals.add(new BigDecimal("0"));
        }

        // calculate group Totals
        for (AccountBalances accountBalance : m_accountBalances.values()) {
            // add all timeGroupTotals
            for (int i = 0; i < accountBalance.getTimeGroupBalances().size(); i++) {
                BigDecimal grpTotal = m_groupTotals.get(i);
                grpTotal = grpTotal.add(accountBalance.getTimeGroupBalances().get(i));
                grpTotal = BudgetUtil.setScale(grpTotal);
                m_groupTotals.set(i, grpTotal);
            }
        }

        // calculate grandTotal and grandAvg
        for (BigDecimal amount : m_groupTotals) {
            m_grandTotal = m_grandTotal.add(amount);

        }
        m_grandTotal = BudgetUtil.setScale(m_grandTotal);
        BigDecimal groups = BudgetUtil.setScale(new BigDecimal(m_groupKeys.size()));
        m_grandAvg = BudgetUtil.divide(m_grandTotal, groups);

        // calculate the groupSums up the account hierarchie (recursive,
        // starting from parent accounts)
        for (Account account : m_accountBalances.keySet()) {
            if (account.getParent() == null) {
                addSumOfChildrenBalances(account);
            }
        }

        // calculate the total and avg values for the accounts
        for (AccountBalances accountBalance : m_accountBalances.values()) {

            // calculate the balance
            BigDecimal balance = new BigDecimal("0");
            for (BigDecimal amount : accountBalance.getTimeGroupBalances()) {
                balance = balance.add(amount);
            }
            balance = BudgetUtil.setScale(balance);
            accountBalance.setBalance(balance);

            // calculate the avg
            accountBalance.setAvgBalance(BudgetUtil.divide(balance, groups));

        }

    }

    /**
     * @return the accountRows
     */
    public List<AccountBalances> getAccountBalances() {
        return new ArrayList<AccountBalances>(m_accountBalances.values());
    }

    /**
     * get the account balances for an account
     * 
     * @param account
     */
    public AccountBalances getAccountBalances(Account account) {
        return m_accountBalances.get(account);
    }

    /**
     * @return the avgTotal
     */
    public BigDecimal getGrandAvg() {
        return m_grandAvg;
    }

    /**
     * @return the grandTotal
     */
    public BigDecimal getGrandTotal() {
        return m_grandTotal;
    }

    public TimeGroup getGroupedBy() {
        return this.m_groupedBy;
    }

    public List<Integer> getGroupKeys() {
        return this.m_groupKeys;
    }

    /**
     * @return the groupTotals
     */
    public List<BigDecimal> getGroupTotals() {
        return m_groupTotals;
    }

    private String getIdent(int ident) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < ident; i++) {
            out.append("  ");
        }
        return out.toString();
    }

}
