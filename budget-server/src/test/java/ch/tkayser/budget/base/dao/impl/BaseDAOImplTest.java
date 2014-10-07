/**
 * 
 */
package ch.tkayser.budget.base.dao.impl;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import ch.tkayser.budget.domain.Account;
import ch.tkayser.budget.domain.Transaction;
import ch.tkayser.budget.test.BudgetEJBBaseTest;
import ch.tkayser.budget.test.DatenHelper;

/**
 * @author isc-kat
 * 
 */
public class BaseDAOImplTest extends BudgetEJBBaseTest {

    @Test
    public void testSaveEntity() {

	// write an account
	Account ac = new Account();
	ac.setName("Account 1");
	ac = m_budgetDAO.saveEntity(ac);

	// check
	Assert.assertNotNull(ac.getId());

	// read again
	Account fromDB = m_budgetDAO.findEntityById(Account.class, ac.getId());
	Assert.assertNotNull(fromDB);
	Assert.assertEquals(ac.getId(), fromDB.getId());
	Assert.assertEquals(ac.getName(), fromDB.getName());
	Assert.assertEquals(ac.getParent(), fromDB.getParent());

    }

    @Test
    public void testDeletEntity() {

	// check state before
	Assert.assertTrue(m_budgetDAO.findAllentitties(Account.class).size() == 0);

	// write an account
	Account ac = new Account();
	ac.setName("Account 1");
	ac = m_budgetDAO.saveEntity(ac);

	// check
	Assert.assertNotNull(ac.getId());

	// read and delete
	ac = m_budgetDAO.findEntityById(Account.class, ac.getId());
	m_budgetDAO.deleteEntity(ac);

	// read again
	Account fromDB = m_budgetDAO.findEntityById(Account.class, ac.getId());
	Assert.assertNull(fromDB);

    }

    @Test
    public void testFindAll() {

	// write some data
	DatenHelper helper = new DatenHelper(m_budgetDAO);
	helper.saveTestAccounts();
	helper.saveTestTransaction(helper.getSavedTestAccounts().get(0), DatenHelper
		.parseDate("01.01.2008"), DatenHelper.parseDate("31.12.2008"), 20);

	// get all accounts
	List<Account> allAccounts = m_budgetDAO.findAllentitties(Account.class);
	Assert.assertEquals(helper.getSavedTestAccounts().size(), allAccounts.size());
	for (Account ac : allAccounts) {
	    m_log.debug("Account found {}", ac);
	}
	
	// ordered by name
	allAccounts = m_budgetDAO.findAllentitties(Account.class, Account.NAME_ATTRIBUT);
	Assert.assertEquals(helper.getSavedTestAccounts().size(), allAccounts.size());
	for (Account ac : allAccounts) {
	    m_log.debug("Account found {}", ac);
	}
	

	// get all tx
	List<Transaction> tx = m_budgetDAO.findAllentitties(Transaction.class);
	Assert.assertEquals(helper.getSavedTestTransactions().size(), tx.size());
	for (Transaction t : tx) {
	    m_log.debug("Tx found: {}", t);
	}

    }

}
