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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import ch.tkayser.budget.dto.TimeGroup;
import ch.tkayser.budget.test.BudgetBaseTest;
import ch.tkayser.budget.test.DatenHelper;

/**
 * @author tom
 * 
 */
public class BalanceSheetTest extends BudgetBaseTest {

    @Test
    public void testCreateTheMatrix() {
	// create some accounts
	DatenHelper helper = new DatenHelper();
	List<Account> accounts = helper.getTestAccountsForBalanceSheet();

	// create a monthly
	Date from = DatenHelper.parseDate("01.01.2008");
	Date to = DatenHelper.parseDate("31.12.2008");
	TimeGroup groupBy = TimeGroup.MONTH;
	BalanceSheet matrix = new BalanceSheet(accounts, from, to, groupBy);

	// check the created rows for the accounts
	Assert.assertEquals(accounts.size(), matrix.getAccountBalances().size());
	for (AccountBalances row : matrix.getAccountBalances()) {
	    Assert.assertEquals(12, row.getTimeGroupBalances().size());
	    Assert.assertEquals(12, row.getTimeGroupBalances().size());
	    for (BigDecimal value : row.getTimeGroupBalances()) {
		Assert.assertEquals(new BigDecimal(0), value);
	    }
	}

	// create a yearly matrix
	from = DatenHelper.parseDate("01.01.2008");
	to = DatenHelper.parseDate("31.12.2009");
	groupBy = TimeGroup.YEAR;
	matrix = new BalanceSheet(accounts, from, to, groupBy);

	// check the created rows for the accounts
	Assert.assertEquals(accounts.size(), matrix.getAccountBalances().size());
	for (AccountBalances row : matrix.getAccountBalances()) {
	    Assert.assertEquals(2, row.getTimeGroupBalances().size());
	    for (BigDecimal value : row.getTimeGroupBalances()) {
		Assert.assertEquals(new BigDecimal(0), value);
	    }
	}

	// create a quarterly matrix
	from = DatenHelper.parseDate("01.01.2008");
	to = DatenHelper.parseDate("30.09.2008");
	groupBy = TimeGroup.QUARTER;
	matrix = new BalanceSheet(accounts, from, to, groupBy);

	// check the created rows for the accounts
	Assert.assertEquals(accounts.size(), matrix.getAccountBalances().size());
	for (AccountBalances row : matrix.getAccountBalances()) {
	    Assert.assertEquals(3, row.getTimeGroupBalances().size());
	    for (BigDecimal value : row.getTimeGroupBalances()) {
		Assert.assertEquals(new BigDecimal(0), value);
	    }
	}

	// create an ungrouped matrix
	from = DatenHelper.parseDate("01.01.2008");
	to = DatenHelper.parseDate("31.12.2008");
	groupBy = TimeGroup.NONE;
	matrix = new BalanceSheet(accounts, from, to, groupBy);

	// check the created rows for the accounts
	Assert.assertEquals(accounts.size(), matrix.getAccountBalances().size());
	for (AccountBalances row : matrix.getAccountBalances()) {
	    Assert.assertEquals(1, row.getTimeGroupBalances().size());
	    for (BigDecimal value : row.getTimeGroupBalances()) {
		Assert.assertEquals(new BigDecimal(0), value);
	    }
	}

    }

    @Test
    public void testGroupedCalculation() {

	// create some accounts
	DatenHelper helper = new DatenHelper();
	List<Account> accounts = helper.getTestAccountsForBalanceSheet();

	// create balances for testing
	List<AccountBalanceForTimeGroup> balances = new ArrayList<AccountBalanceForTimeGroup>();
	balances.add(createBalance(accounts.get(1), 1, "15.75"));
	balances.add(createBalance(accounts.get(1), 2, "25.35"));
	balances.add(createBalance(accounts.get(1), 3, "35.75"));
	balances.add(createBalance(accounts.get(1), 4, "100"));
	balances.add(createBalance(accounts.get(1), 6, "99.15"));
	balances.add(createBalance(accounts.get(2), 1, "175.55"));
	balances.add(createBalance(accounts.get(2), 2, "22.05"));
	balances.add(createBalance(accounts.get(2), 3, "125.2"));
	balances.add(createBalance(accounts.get(3), 1, "200"));
	balances.add(createBalance(accounts.get(3), 2, "200"));
	balances.add(createBalance(accounts.get(3), 3, "200"));
	balances.add(createBalance(accounts.get(3), 4, "200"));
	balances.add(createBalance(accounts.get(3), 5, "200"));
	balances.add(createBalance(accounts.get(3), 6, "200"));
	balances.add(createBalance(accounts.get(4), 2, "75.55"));
	balances.add(createBalance(accounts.get(4), 4, "88.15"));
	balances.add(createBalance(accounts.get(4), 5, "135.40"));

	// create a monthly matrix
	Date from = DatenHelper.parseDate("01.01.2008");
	Date to = DatenHelper.parseDate("30.06.2008");
	TimeGroup groupBy = TimeGroup.MONTH;
	BalanceSheet matrix = new BalanceSheet(accounts, from, to, groupBy);
	for (AccountBalanceForTimeGroup balance : balances) {
	    matrix.addAccountBalance(balance);
	}

	// calculate the values
	matrix.calculateMatrixColumns();

	// check
	Assert.assertEquals(accounts.size(), matrix.getAccountBalances().size());

	// first account
	AccountBalances acBalances = matrix.getAccountBalances(accounts.get(0));
	Assert.assertEquals(accounts.get(0), acBalances.getAccount());
	Assert.assertEquals(6, acBalances.getTimeGroupBalances().size());
	Assert.assertEquals((double) 391.3, acBalances.getTimeGroupBalances().get(0).doubleValue());
	Assert.assertEquals((double) 247.4, acBalances.getTimeGroupBalances().get(1).doubleValue());
	Assert
		.assertEquals((double) 360.95, acBalances.getTimeGroupBalances().get(2)
			.doubleValue());
	Assert.assertEquals((double) 300, acBalances.getTimeGroupBalances().get(3).doubleValue());
	Assert.assertEquals((double) 200, acBalances.getTimeGroupBalances().get(4).doubleValue());
	Assert
		.assertEquals((double) 299.15, acBalances.getTimeGroupBalances().get(5)
			.doubleValue());
	Assert.assertEquals((double) 1798.8, acBalances.getBalance().doubleValue());
	Assert.assertEquals((double) 299.8, acBalances.getAvgBalance().doubleValue());

	// second
	acBalances = matrix.getAccountBalances(accounts.get(1));
	Assert.assertEquals(accounts.get(1), acBalances.getAccount());
	Assert.assertEquals(6, acBalances.getTimeGroupBalances().size());
	Assert.assertEquals((double) 15.75, acBalances.getTimeGroupBalances().get(0).doubleValue());
	Assert.assertEquals((double) 25.35, acBalances.getTimeGroupBalances().get(1).doubleValue());
	Assert.assertEquals((double) 35.75, acBalances.getTimeGroupBalances().get(2).doubleValue());
	Assert.assertEquals((double) 100, acBalances.getTimeGroupBalances().get(3).doubleValue());
	Assert.assertEquals((double) 0, acBalances.getTimeGroupBalances().get(4).doubleValue());
	Assert.assertEquals((double) 99.15, acBalances.getTimeGroupBalances().get(5).doubleValue());
	Assert.assertEquals((double) 276, acBalances.getBalance().doubleValue());
	Assert.assertEquals((double) 46, acBalances.getAvgBalance().doubleValue());

	// third
	acBalances = matrix.getAccountBalances(accounts.get(2));
	Assert.assertEquals(accounts.get(2), acBalances.getAccount());
	Assert.assertEquals(6, acBalances.getTimeGroupBalances().size());
	Assert
		.assertEquals((double) 375.55, acBalances.getTimeGroupBalances().get(0)
			.doubleValue());
	Assert
		.assertEquals((double) 222.05, acBalances.getTimeGroupBalances().get(1)
			.doubleValue());
	Assert.assertEquals((double) 325.2, acBalances.getTimeGroupBalances().get(2).doubleValue());
	Assert.assertEquals((double) 200, acBalances.getTimeGroupBalances().get(3).doubleValue());
	Assert.assertEquals((double) 200, acBalances.getTimeGroupBalances().get(4).doubleValue());
	Assert.assertEquals((double) 200, acBalances.getTimeGroupBalances().get(5).doubleValue());
	Assert.assertEquals((double) 1522.8, acBalances.getBalance().doubleValue());
	Assert.assertEquals((double) 253.8, acBalances.getAvgBalance().doubleValue());

	// forth
	acBalances = matrix.getAccountBalances(accounts.get(3));
	Assert.assertEquals(accounts.get(3), acBalances.getAccount());
	Assert.assertEquals(6, acBalances.getTimeGroupBalances().size());
	Assert.assertEquals((double) 200, acBalances.getTimeGroupBalances().get(0).doubleValue());
	Assert.assertEquals((double) 200, acBalances.getTimeGroupBalances().get(1).doubleValue());
	Assert.assertEquals((double) 200, acBalances.getTimeGroupBalances().get(2).doubleValue());
	Assert.assertEquals((double) 200, acBalances.getTimeGroupBalances().get(3).doubleValue());
	Assert.assertEquals((double) 200, acBalances.getTimeGroupBalances().get(4).doubleValue());
	Assert.assertEquals((double) 200, acBalances.getTimeGroupBalances().get(5).doubleValue());
	Assert.assertEquals((double) 1200, acBalances.getBalance().doubleValue());
	Assert.assertEquals((double) 200, acBalances.getAvgBalance().doubleValue());

	// fith
	acBalances = matrix.getAccountBalances(accounts.get(4));
	Assert.assertEquals(accounts.get(4), acBalances.getAccount());
	Assert.assertEquals(6, acBalances.getTimeGroupBalances().size());
	Assert.assertEquals((double) 0, acBalances.getTimeGroupBalances().get(0).doubleValue());
	Assert.assertEquals((double) 75.55, acBalances.getTimeGroupBalances().get(1).doubleValue());
	Assert.assertEquals((double) 0, acBalances.getTimeGroupBalances().get(2).doubleValue());
	Assert.assertEquals((double) 88.15, acBalances.getTimeGroupBalances().get(3).doubleValue());
	Assert.assertEquals((double) 135.4, acBalances.getTimeGroupBalances().get(4).doubleValue());
	Assert.assertEquals((double) 0, acBalances.getTimeGroupBalances().get(5).doubleValue());
	Assert.assertEquals((double) 299.1, acBalances.getBalance().doubleValue());
	Assert.assertEquals((double) 49.85, acBalances.getAvgBalance().doubleValue());

	// groupTotals
	Assert.assertEquals(6, matrix.getGroupTotals().size());
	Assert.assertEquals((double) 391.3, matrix.getGroupTotals().get(0).doubleValue());
	Assert.assertEquals((double) 322.95, matrix.getGroupTotals().get(1).doubleValue());
	Assert.assertEquals((double) 360.95, matrix.getGroupTotals().get(2).doubleValue());
	Assert.assertEquals((double) 388.15, matrix.getGroupTotals().get(3).doubleValue());
	Assert.assertEquals((double) 335.4, matrix.getGroupTotals().get(4).doubleValue());
	Assert.assertEquals((double) 299.15, matrix.getGroupTotals().get(5).doubleValue());

	
	// grand Total and total avg
	Assert.assertEquals((double) 2097.9, matrix.getGrandTotal().doubleValue());
	Assert.assertEquals((double) 349.65, matrix.getGrandAvg().doubleValue());

    }

    @Test
    public void testUnGroupedCalculation() {

	// create some accounts
	DatenHelper helper = new DatenHelper();
	List<Account> accounts = helper.getTestAccountsForBalanceSheet();

	// create ungrouped balances for testing
	List<AccountBalanceForTimeGroup> balances = new ArrayList<AccountBalanceForTimeGroup>();
	balances.add(createBalance(accounts.get(1), "15.75"));
	balances.add(createBalance(accounts.get(2), "175.55"));
	balances.add(createBalance(accounts.get(3), "200"));
	balances.add(createBalance(accounts.get(4), "135.40"));

	
	// create an ungrouped matrix
	Date from = DatenHelper.parseDate("01.01.2008");
	Date to = DatenHelper.parseDate("30.06.2008");
	TimeGroup groupBy = TimeGroup.NONE;
	BalanceSheet matrix = new BalanceSheet(accounts, from, to, groupBy);
	for (AccountBalanceForTimeGroup balance : balances) {
	    matrix.addAccountBalance(balance);
	}

	// calculate the values
	matrix.calculateMatrixColumns();

	// check
	Assert.assertEquals(accounts.size(), matrix.getAccountBalances().size());

	// first account
	AccountBalances acBalances = matrix.getAccountBalances(accounts.get(0));
	Assert.assertEquals(accounts.get(0), acBalances.getAccount());
	Assert.assertEquals(1, acBalances.getTimeGroupBalances().size());
	Assert.assertEquals((double) 391.3, acBalances.getTimeGroupBalances().get(0).doubleValue());
	Assert.assertEquals((double) 391.3, acBalances.getBalance().doubleValue());
	Assert.assertEquals((double) 391.3, acBalances.getAvgBalance().doubleValue());

	// second
	acBalances = matrix.getAccountBalances(accounts.get(1));
	Assert.assertEquals(accounts.get(1), acBalances.getAccount());
	Assert.assertEquals(1, acBalances.getTimeGroupBalances().size());
	Assert.assertEquals((double) 15.75, acBalances.getTimeGroupBalances().get(0).doubleValue());
	Assert.assertEquals((double) 15.75, acBalances.getBalance().doubleValue());
	Assert.assertEquals((double) 15.75, acBalances.getAvgBalance().doubleValue());

	// third
	acBalances = matrix.getAccountBalances(accounts.get(2));
	Assert.assertEquals(accounts.get(2), acBalances.getAccount());
	Assert.assertEquals(1, acBalances.getTimeGroupBalances().size());
	Assert.assertEquals((double) 375.55, acBalances.getTimeGroupBalances().get(0).doubleValue());
	Assert.assertEquals((double) 375.55, acBalances.getBalance().doubleValue());
	Assert.assertEquals((double) 375.55, acBalances.getAvgBalance().doubleValue());

	// forth
	acBalances = matrix.getAccountBalances(accounts.get(3));
	Assert.assertEquals(accounts.get(3), acBalances.getAccount());
	Assert.assertEquals(1, acBalances.getTimeGroupBalances().size());
	Assert.assertEquals((double) 200, acBalances.getTimeGroupBalances().get(0).doubleValue());
	Assert.assertEquals((double) 200, acBalances.getBalance().doubleValue());
	Assert.assertEquals((double) 200, acBalances.getAvgBalance().doubleValue());

	// fith
	acBalances = matrix.getAccountBalances(accounts.get(4));
	Assert.assertEquals(accounts.get(4), acBalances.getAccount());
	Assert.assertEquals(1, acBalances.getTimeGroupBalances().size());
	Assert.assertEquals((double) 135.40, acBalances.getTimeGroupBalances().get(0).doubleValue());
	Assert.assertEquals((double) 135.40, acBalances.getBalance().doubleValue());
	Assert.assertEquals((double) 135.40, acBalances.getAvgBalance().doubleValue());

	// groupTotals
	Assert.assertEquals(1, matrix.getGroupTotals().size());
	Assert.assertEquals((double) 526.7, matrix.getGroupTotals().get(0).doubleValue());

	// grand Total and total avg
	Assert.assertEquals((double) 526.7, matrix.getGrandTotal().doubleValue());
	Assert.assertEquals((double) 526.7, matrix.getGrandAvg().doubleValue());

    }

	/**
     * create a balance Object
     * 
     * @param account
     * @param groupId
     * @param amount
     * @return
     */
    private AccountBalanceForTimeGroup createBalance(Account account, int groupId, String amount) {

	return new AccountBalanceForTimeGroup(account, new Integer(groupId), toBigDecimal(amount));
    }

    /**
     * create a balance Object
     * 
     * @param account
     * @param amount
     * @return
     */
    private AccountBalanceForTimeGroup createBalance(Account account, String amount) {

	return new AccountBalanceForTimeGroup(account, toBigDecimal(amount));
    }

}
