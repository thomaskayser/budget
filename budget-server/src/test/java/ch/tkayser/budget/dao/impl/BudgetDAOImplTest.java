/**
 * 
 */
package ch.tkayser.budget.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import ch.tkayser.budget.domain.Account;
import ch.tkayser.budget.domain.AccountBalanceForTimeGroup;
import ch.tkayser.budget.domain.Transaction;
import ch.tkayser.budget.dto.TimeGroup;
import ch.tkayser.budget.exception.BudgetException;
import ch.tkayser.budget.test.BudgetEJBBaseTest;
import ch.tkayser.budget.test.DatenHelper;

/**
 * @author isc-kat
 * 
 */
public class BudgetDAOImplTest extends BudgetEJBBaseTest {

    @Test
    public void testFindTransactionsByInfo() throws BudgetException {

        // write some data
        DatenHelper helper = new DatenHelper(m_budgetDAO);
        helper.saveTestAccounts();
        helper.saveTestTransaction(helper.getSavedTestAccounts().get(0), DatenHelper.parseDate("01.01.2008"), DatenHelper
                .parseDate("31.12.2008"), 20);

        // get tx by booking text
        // all tx
        List<Transaction> tx = m_budgetDAO.findTransactions(null, null, DatenHelper.BOOKING_TEXT_TEMPLATE);
        logTx(tx);
        Assert.assertEquals(helper.getSavedTestTransactions().size(), tx.size());

        // only with booking text number 18
        tx = m_budgetDAO.findTransactions(null, null, DatenHelper.BOOKING_TEXT_TEMPLATE.substring(2) + " 18");
        Assert.assertEquals(1, tx.size());
        Assert.assertTrue(tx.get(0).getBookingText().contains("18"));

        // only with sender text number 15
        tx = m_budgetDAO.findTransactions(null, DatenHelper.SENDER_TEXT_TEMPLATE.substring(2) + " 15", null);
        Assert.assertEquals(1, tx.size());
        Assert.assertTrue(tx.get(0).getSender().contains("15"));

        // only with reciever text number 13
        tx = m_budgetDAO.findTransactions(DatenHelper.RECIEVER_TEXT_TEMPLATE.substring(2) + " 13", null, null);
        Assert.assertEquals(1, tx.size());
        Assert.assertTrue(tx.get(0).getReciever().contains("13"));

        // combination
        tx = m_budgetDAO.findTransactions(DatenHelper.RECIEVER_TEXT_TEMPLATE.substring(2) + " 7",
                DatenHelper.SENDER_TEXT_TEMPLATE.substring(2) + " 7", DatenHelper.BOOKING_TEXT_TEMPLATE.substring(3)
                        + " 7");
        Assert.assertEquals(1, tx.size());
        Assert.assertTrue(tx.get(0).getReciever().contains("7"));
        Assert.assertTrue(tx.get(0).getSender().contains("7"));
        Assert.assertTrue(tx.get(0).getBookingText().contains("7"));

    }

    private void logTx(List<Transaction> tx) {
        for (Transaction t : tx) {
            m_log.debug("Tx read: {}", t);
        }
    }

    @Test
    public void testFindTransactionsByDate() {

        // write some data
        DatenHelper helper = new DatenHelper(m_budgetDAO);
        helper.saveTestAccounts();
        helper.saveTestTransaction(helper.getSavedTestAccounts().get(1), DatenHelper.parseDate("01.01.2008"), DatenHelper
                .parseDate("31.12.2008"), 20);

        // get tx for each month
        Calendar from = Calendar.getInstance();
        from.setTime(DatenHelper.parseDate("01.01.2008"));
        Calendar to = Calendar.getInstance();
        to.setTime(DatenHelper.parseDate("31.01.2008"));

        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 12; i++) {
            List<Transaction> tx = m_budgetDAO.findTransactions(from.getTime(), to.getTime());
            for (Transaction t : tx) {
                // check the month
                cal.setTime(t.getValuta());
                Assert.assertEquals(i, cal.get(Calendar.MONTH));
            }
            logTx(tx);
            from.add(Calendar.MONTH, 1);
            to.add(Calendar.MONTH, 1);
        }

    }

    @Test
    public void testFindTransactionsByDateAndAccount() {

        // write some data
        DatenHelper helper = new DatenHelper(m_budgetDAO);
        helper.saveTestAccounts();
        helper.saveTestTransaction(helper.getSavedTestAccounts().get(0), DatenHelper.parseDate("01.01.2008"), DatenHelper
                .parseDate("31.12.2008"), 20);
        helper.saveTestTransaction(helper.getSavedTestAccounts().get(1), DatenHelper.parseDate("01.01.2008"), DatenHelper
                .parseDate("31.12.2008"), 20);

        // check both accounts
        for (Account a : helper.getSavedTestAccounts().subList(0, 1)) {
            // get tx for each month
            Calendar from = Calendar.getInstance();
            from.setTime(DatenHelper.parseDate("01.01.2008"));
            Calendar to = Calendar.getInstance();
            to.setTime(DatenHelper.parseDate("31.01.2008"));

            Calendar cal = Calendar.getInstance();

            // get tx for each month
            for (int i = 0; i < 12; i++) {
                List<Long> accountIds = new ArrayList<Long>();
                accountIds.add(a.getId());
                List<Transaction> tx = m_budgetDAO.findTransactions(from.getTime(), to.getTime(), accountIds);
                for (Transaction t : tx) {
                    // check the month
                    cal.setTime(t.getValuta());
                    Assert.assertEquals(i, cal.get(Calendar.MONTH));
                    // check the acccount
                    Assert.assertEquals(helper.getSavedTestAccounts().get(0), a);
                }
                logTx(tx);
                from.add(Calendar.MONTH, 1);
                to.add(Calendar.MONTH, 1);
            }
        }


    }
    
    @Test
    public void testFindMultipleAccounts() {
        // write some data
        DatenHelper helper = new DatenHelper(m_budgetDAO);
        helper.saveTestAccounts();
        helper.saveTestTransaction(helper.getSavedTestAccounts().get(1), DatenHelper.parseDate("01.01.2008"), DatenHelper
                .parseDate("31.12.2008"), 1);
        helper.saveTestTransaction(helper.getSavedTestAccounts().get(2), DatenHelper.parseDate("01.01.2008"), DatenHelper
                .parseDate("31.12.2008"), 1);
        
        // alle accounts
        List<Long> accountIds = new ArrayList<Long>();
        accountIds.add(helper.getSavedTestAccounts().get(1).getId());
        accountIds.add(helper.getSavedTestAccounts().get(2).getId());
        List<Transaction> tx = m_budgetDAO.findTransactions(parseDate("01.01.2008"), parseDate("31.12.2008"), accountIds);
        Assert.assertEquals(tx.size(), 2);
        
        // nur ac2
        accountIds.clear();
        accountIds.add(helper.getSavedTestAccounts().get(2).getId());
        tx = m_budgetDAO.findTransactions(parseDate("01.01.2008"), parseDate("31.12.2008"), accountIds);
        Assert.assertEquals(tx.size(), 1);
        Assert.assertEquals(tx.get(0).getAccount(), helper.getSavedTestAccounts().get(2));


    }

    @Test
    public void testCalculateBalances() {

        // write some data
        DatenHelper helper = new DatenHelper(m_budgetDAO);
        helper.saveTestAccounts();
        helper.saveTestTransaction(helper.getSavedTestAccounts().get(0), DatenHelper.parseDate("01.01.2008"), DatenHelper
                .parseDate("31.12.2009"), 40);
        helper.saveTestTransaction(helper.getSavedTestAccounts().get(1), DatenHelper.parseDate("01.01.2008"), DatenHelper
                .parseDate("31.12.2009"), 40);

        // do some smoke tests. functional tests are on budgetService

        // calculate monthly totals
        List<AccountBalanceForTimeGroup> calculateBalances = m_budgetDAO.countBalances(DatenHelper.parseDate("01.01.2008"), DatenHelper
                .parseDate("31.12.2009"), TimeGroup.MONTH);
        checkBalances(calculateBalances);

        // calculate quarterly totals
        calculateBalances = m_budgetDAO.countBalances(DatenHelper.parseDate("01.01.2008"), DatenHelper
                .parseDate("31.12.2009"), TimeGroup.QUARTER);
        checkBalances(calculateBalances);

        // yearly
        calculateBalances = m_budgetDAO.countBalances(DatenHelper.parseDate("01.01.2008"), DatenHelper
                .parseDate("31.12.2009"), TimeGroup.YEAR);
        checkBalances(calculateBalances);

        // no grouping
        calculateBalances = m_budgetDAO.countBalances(DatenHelper.parseDate("01.01.2008"), DatenHelper
                .parseDate("31.12.2009"), TimeGroup.NONE);
        checkBalances(calculateBalances, false);

    }

    /**
     * check if every field is set
     * 
     * @param calculateBalances
     */
    private void checkBalances(List<AccountBalanceForTimeGroup> calculateBalances) {
        checkBalances(calculateBalances, false);
    }

    /**
     * check if every field is set
     * 
     * @param calculateBalances
     */
    private void checkBalances(List<AccountBalanceForTimeGroup> calculateBalances, boolean checkTimeGroup) {
        for (AccountBalanceForTimeGroup balance : calculateBalances) {
            Assert.assertNotNull(balance.getAccount());
            Assert.assertNotNull(balance.getBalance());
            if (checkTimeGroup) {
                Assert.assertNotNull(balance.getTimeKey());
            }
        }
    }

}
