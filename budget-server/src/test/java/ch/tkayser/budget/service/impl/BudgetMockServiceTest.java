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
package ch.tkayser.budget.service.impl;

import java.util.ArrayList;
import org.junit.Test;
import ch.tkayser.budget.dto.AccountDTO;
import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.exception.BudgetException;
import ch.tkayser.budget.util.BudgetMockService;

/**
 * @author isc-kat
 * 
 */
public class BudgetMockServiceTest {

    @Test
    public void testAll() throws BudgetException {

	// create mock service
	BudgetMockService service = new BudgetMockService();

	// save methods
	AccountDTO ac = new AccountDTO();
	ac.setName("Test");
	service.saveAccount(ac);
	TransactionDTO tx = new TransactionDTO();
	tx.setAccount(ac);
	service.saveTransaction(tx);

	// find methods
	service.findTransactions(null, null);
	service.findTransactions(null, null, ac.getId(), true);
	service.findTransactions("", null, null);
	service.getAllAccounts();

	// book tx
	service.bookTransactions(new ArrayList<TransactionDTO>());

	// balance methods
	service.getAccountBalances(null, null, null);

	// delete methods
	service.deleteAccount(ac);
	service.deleteTransaction(tx);

    }
}
