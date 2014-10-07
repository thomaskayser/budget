package ch.tkayser.budget.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import ch.tkayser.budget.domain.Account;
import ch.tkayser.budget.domain.Transaction;
import ch.tkayser.budget.dto.AccountDTO;
import ch.tkayser.budget.dto.BalanceSheetDTO;
import ch.tkayser.budget.dto.BalanceSheetRowDTO;
import ch.tkayser.budget.dto.BudgetDTO;
import ch.tkayser.budget.dto.TimeGroup;
import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.dto.TransferTargetDTO;
import ch.tkayser.budget.dto.TransferTransactionDTO;
import ch.tkayser.budget.exception.BudgetException;
import ch.tkayser.budget.service.BudgetServiceConstants;
import ch.tkayser.budget.test.BudgetEJBBaseTest;
import ch.tkayser.budget.test.DatenHelper;
import ch.tkayser.budget.util.BudgetUtil;

/**
 * 
 */

/**
 * @author isc-kat
 * 
 */
public class BudgetServiceImplTest extends BudgetEJBBaseTest {

    @Test
    public void testAccountMethods() throws BudgetException {
        // create an account and save it
        AccountDTO parent = new AccountDTO();
        parent.setName("ParentAccount");
        AccountDTO saved = m_budgetService.saveAccount(parent);
        long id1 = saved.getId();

        // check
        Assert.assertNotNull(saved);
        Assert.assertNotNull(saved.getId());
        Assert.assertEquals(parent.getName(), saved.getName());

        // create a child account
        parent = saved;
        AccountDTO child = new AccountDTO();
        child.setName("ChildAccount");
        child.setParent(parent);
        child = m_budgetService.saveAccount(child);
        long id2 = child.getId();

        // check
        Assert.assertEquals(parent.getId(), child.getParent().getId());

        // another child account
        child = new AccountDTO();
        child.setName("ChildAccount 2");
        child.setParent(parent);
        child = m_budgetService.saveAccount(child);
        long id3 = child.getId();

        // check
        Assert.assertEquals(parent.getId(), child.getParent().getId());

        // get all acounts
        List<AccountDTO> allAccounts = m_budgetService.getAllAccounts();
        Assert.assertEquals(3, allAccounts.size());
        for (AccountDTO ac : allAccounts) {
            Assert.assertTrue(ac.getId().equals(id1) || ac.getId().equals(id2) || ac.getId().equals(id3));
        }

        // delete the child
        m_budgetService.deleteAccount(child);

        // get all accounts
        allAccounts = m_budgetService.getAllAccounts();
        Assert.assertEquals(2, allAccounts.size());

    }

    @Test
    public void testBudgetMethods() throws BudgetException {

        // create an budget and save it
        BudgetDTO budget1 = new BudgetDTO();
        budget1.setName("budget1");
        budget1.setAmountPerMonth(new BigDecimal(3500));
        BudgetDTO saved1 = m_budgetService.saveBudget(budget1);

        // check
        Assert.assertNotNull(saved1);
        Assert.assertNotNull(saved1.getId());
        Assert.assertEquals(budget1.getName(), saved1.getName());
        Assert.assertEquals(budget1.getAmountPerMonth(), saved1.getAmountPerMonth());

        // create a second budget and save it
        BudgetDTO budget2 = new BudgetDTO();
        budget2.setName("budget2");
        budget2.setAmountPerMonth(new BigDecimal(5500));
        BudgetDTO saved2 = m_budgetService.saveBudget(budget2);

        // check
        Assert.assertNotNull(saved2);
        Assert.assertNotNull(saved2.getId());
        Assert.assertEquals(budget2.getName(), saved2.getName());
        Assert.assertEquals(budget2.getAmountPerMonth(), saved2.getAmountPerMonth());

        // get all budget
        List<BudgetDTO> allBudgets = m_budgetService.getBudgets();
        Assert.assertEquals(2, allBudgets.size());

        // delete a budget
        m_budgetService.deleteBudget(saved1);

        // get all accounts
        allBudgets = m_budgetService.getBudgets();
        Assert.assertEquals(1, allBudgets.size());

    }

    @Test
    public void testTransactionMethods() throws BudgetException {
        // create some accounts
        AccountDTO account1 = new AccountDTO();
        account1.setName("Ac1");
        account1 = m_budgetService.saveAccount(account1);
        AccountDTO account2 = new AccountDTO();
        account2.setName("Ac2");
        account2 = m_budgetService.saveAccount(account2);

        // create some tx
        TransactionDTO tx = new TransactionDTO();
        tx.setAccount(account1);
        tx.setAmount(new BigDecimal(155.75));
        tx.setBookingText("Booking 1");
        tx.setValuta(DatenHelper.parseDate("15.01.2008"));
        TransactionDTO saved = m_budgetService.saveTransaction(tx);

        // check
        Assert.assertNotNull(saved);
        Assert.assertNotNull(saved.getId());
        Assert.assertEquals(saved.getBookingText(), tx.getBookingText());
        Assert.assertEquals(saved.getValuta(), tx.getValuta());
        Assert.assertEquals(saved.getAmount(), tx.getAmount());
        Assert.assertEquals(saved.getAccount().getId(), tx.getAccount().getId());

        // save some more
        tx = new TransactionDTO();
        tx.setAccount(account1);
        tx.setAmount(new BigDecimal(55.75));
        tx.setBookingText("Booking 2");
        tx.setSender("My mom");
        tx.setValuta(DatenHelper.parseDate("10.02.2008"));
        m_budgetService.saveTransaction(tx);
        tx = new TransactionDTO();
        tx.setAccount(account2);
        tx.setAmount(new BigDecimal(35.75));
        tx.setBookingText("Booking 3");
        tx.setSender("My mom");
        tx.setReciever("My hun");
        tx.setValuta(DatenHelper.parseDate("17.01.2008"));
        m_budgetService.saveTransaction(tx);
        tx = new TransactionDTO();
        tx.setAccount(account2);
        tx.setAmount(new BigDecimal(35.75));
        tx.setSender("My dad");
        tx.setBookingText("Booking 4");
        tx.setValuta(DatenHelper.parseDate("28.03.2008"));
        m_budgetService.saveTransaction(tx);

        // find by info
        List<TransactionDTO> allTx = m_budgetService.findTransactions(null, null, "3");
        Assert.assertEquals(1, allTx.size());
        Assert.assertTrue(allTx.get(0).getBookingText().contains("3"));
        allTx = m_budgetService.findTransactions("My HUN", null, null);
        Assert.assertEquals(1, allTx.size());
        Assert.assertEquals("Booking 3", allTx.get(0).getBookingText());
        allTx = m_budgetService.findTransactions(null, "MoM", null);
        Assert.assertEquals(2, allTx.size());
        Assert.assertTrue(allTx.get(0).getSender().toLowerCase().contains("mom"));
        Assert.assertTrue(allTx.get(1).getSender().toLowerCase().contains("mom"));

        // find by date range
        allTx = m_budgetService.findTransactions(DatenHelper.parseDate("01.01.2008"),
                DatenHelper.parseDate("31.01.2008"));
        Assert.assertEquals(2, allTx.size());
        allTx = m_budgetService.findTransactions(DatenHelper.parseDate("01.01.2008"),
                DatenHelper.parseDate("28.02.2008"));
        Assert.assertEquals(3, allTx.size());

        // find by date range and account
        allTx = m_budgetService.findTransactions(DatenHelper.parseDate("01.01.2008"),
                DatenHelper.parseDate("31.03.2008"), account2.getId(), false);
        Assert.assertEquals(2, allTx.size());
        Assert.assertEquals(account2.getId(), allTx.get(0).getAccount().getId());
        Assert.assertEquals(account2.getId(), allTx.get(1).getAccount().getId());

        allTx = m_budgetService.findTransactions(DatenHelper.parseDate("01.01.2008"),
                DatenHelper.parseDate("31.01.2008"), account1.getId(), false);
        Assert.assertEquals(1, allTx.size());
        Assert.assertEquals(account1.getId(), allTx.get(0).getAccount().getId());

        // delete january tx
        allTx = m_budgetService.findTransactions(DatenHelper.parseDate("01.01.2008"),
                DatenHelper.parseDate("31.01.2008"));
        for (TransactionDTO transaction : allTx) {
            m_budgetService.deleteTransaction(transaction);
        }

        // read again
        allTx = m_budgetService.findTransactions(DatenHelper.parseDate("01.01.2008"),
                DatenHelper.parseDate("31.01.2008"));
        Assert.assertEquals(0, allTx.size());

    }

    @Test
    public void bookTx() throws BudgetException {

        // create some accounts
        DatenHelper helper = new DatenHelper(m_budgetDAO);
        helper.saveTestAccounts();
        List<Account> accounts = helper.getSavedTestAccounts();

        // text constants for the test
        final String reciever1 = "SWICA";
        final String reciever2 = "Baettig und Bucher";
        final String sender1 = "SWICA";
        final String bookingText1 = "Bezug Bankomat" + BudgetServiceConstants.BOOKINGTEXT_LINE_SEPARATOR
                + "LUKB Luzern";
        final String bookingText2 = "Bezug Bankomat" + BudgetServiceConstants.BOOKINGTEXT_LINE_SEPARATOR
                + "BEKB Irgendwo";

        // save some tx

        // Case one: Same Recipient
        Transaction txReciever1 = new Transaction();
        txReciever1.setAccount(accounts.get(0));
        txReciever1.setAmount(new BigDecimal(15));
        txReciever1.setValuta(DatenHelper.parseDate("01.01.2008"));
        txReciever1.setBookingText("blabla");
        txReciever1.setReciever(reciever1);
        txReciever1 = m_budgetDAO.saveEntity(txReciever1);

        Transaction txReciever1b = new Transaction();
        txReciever1b.setAccount(accounts.get(1));
        txReciever1b.setAmount(new BigDecimal(15));
        txReciever1b.setValuta(DatenHelper.parseDate("24.08.2006"));
        txReciever1b.setBookingText("blabla");
        txReciever1b.setReciever(reciever1);
        txReciever1b = m_budgetDAO.saveEntity(txReciever1b);

        Transaction txReciever2 = new Transaction();
        txReciever2.setAccount(accounts.get(1));
        txReciever2.setAmount(new BigDecimal(15));
        txReciever2.setValuta(DatenHelper.parseDate("31.12.2009"));
        txReciever2.setBookingText("blabla");
        txReciever2.setReciever(reciever2);
        txReciever2 = m_budgetDAO.saveEntity(txReciever2);

        Transaction txReciever2b = new Transaction();
        txReciever2b.setValuta(DatenHelper.parseDate("15.08.2009"));
        txReciever2b.setAccount(accounts.get(2));
        txReciever2b.setAmount(new BigDecimal(15));
        txReciever2b.setBookingText("blabla");
        txReciever2b.setReciever(reciever2);
        txReciever2b = m_budgetDAO.saveEntity(txReciever2b);

        // Case two: Same Sender
        Transaction txSender1 = new Transaction();
        txSender1.setAccount(accounts.get(2));
        txSender1.setAmount(new BigDecimal(15));
        txSender1.setValuta(DatenHelper.parseDate("22.10.2008"));
        txSender1.setBookingText("blabla");
        txSender1.setSender(sender1);
        txSender1 = m_budgetDAO.saveEntity(txSender1);

        Transaction txSender1b = new Transaction();
        txSender1b.setAccount(accounts.get(3));
        txSender1b.setAmount(new BigDecimal(15));
        txSender1b.setValuta(DatenHelper.parseDate("21.10.2008"));
        txSender1b.setBookingText("blabla");
        txSender1b.setSender(sender1);
        txSender1b = m_budgetDAO.saveEntity(txSender1b);

        // Case three: Booking text: row equals or completely equals
        Transaction txBookingText = new Transaction();
        txBookingText.setAccount(accounts.get(2));
        txBookingText.setAmount(new BigDecimal(15));
        txBookingText.setValuta(DatenHelper.parseDate("23.06.2008"));
        txBookingText.setBookingText(bookingText1);
        txBookingText = m_budgetDAO.saveEntity(txBookingText);

        Transaction txBookingText2 = new Transaction();
        txBookingText2.setAccount(accounts.get(3));
        txBookingText2.setAmount(new BigDecimal(15));
        txBookingText2.setValuta(DatenHelper.parseDate("15.02.2008"));
        txBookingText2.setBookingText(bookingText1);
        txBookingText2 = m_budgetDAO.saveEntity(txBookingText2);

        // create tx to book
        List<TransactionDTO> toBook = new ArrayList<TransactionDTO>();

        // Case one: Same Recipient
        TransactionDTO txToBook = new TransactionDTO();
        txToBook.setReciever(reciever1);
        toBook.add(txToBook);

        txToBook = new TransactionDTO();
        txToBook.setReciever(reciever2);
        toBook.add(txToBook);

        // Case two: Same Sender
        txToBook = new TransactionDTO();
        txToBook.setSender(sender1);
        toBook.add(txToBook);

        // Case three: Booking text: row equals or completely equals
        txToBook = new TransactionDTO();
        txToBook.setBookingText(bookingText1);
        toBook.add(txToBook);

        txToBook = new TransactionDTO();
        txToBook.setBookingText(bookingText2);
        toBook.add(txToBook);

        // book them
        List<TransactionDTO> bookedTransactions = m_budgetService.bookTransactions(toBook);
        Assert.assertEquals(5, bookedTransactions.size());

        // check them
        // Case one: Same Recipient
        TransactionDTO bookedTx = bookedTransactions.get(0);
        Assert.assertEquals(reciever1, bookedTx.getReciever());
        Assert.assertEquals(txReciever1.getAccount().getId(), bookedTx.getAccount().getId());

        bookedTx = bookedTransactions.get(1);
        Assert.assertEquals(reciever2, bookedTx.getReciever());
        Assert.assertEquals(txReciever2.getAccount().getId(), bookedTx.getAccount().getId());

        // Case two: Same Sender
        bookedTx = bookedTransactions.get(2);
        Assert.assertEquals(sender1, bookedTx.getSender());
        Assert.assertEquals(txSender1.getAccount().getId(), bookedTx.getAccount().getId());

        // Case three: Same booking text part
        bookedTx = bookedTransactions.get(3);
        Assert.assertEquals(bookingText1, bookedTx.getBookingText());
        Assert.assertEquals(txBookingText.getAccount().getId(), bookedTx.getAccount().getId());

        bookedTx = bookedTransactions.get(4);
        Assert.assertEquals(bookingText2, bookedTx.getBookingText());
        Assert.assertEquals(txBookingText.getAccount().getId(), bookedTx.getAccount().getId());

    }

    @Test
    public void testFindByAccount() {
        // create some accounts
        AccountDTO parent1 = new AccountDTO();
        parent1.setName("P1");
        parent1 = m_budgetService.saveAccount(parent1);

        AccountDTO child1 = new AccountDTO();
        child1.setName("C1");
        child1.setParent(parent1);
        child1 = m_budgetService.saveAccount(child1);
        
        AccountDTO child2 = new AccountDTO();
        child2.setParent(parent1);
        child2.setName("C2");
        child2 = m_budgetService.saveAccount(child2);

        AccountDTO child21 = new AccountDTO();
        child21.setParent(child2);
        child21.setName("C21");
        child21 = m_budgetService.saveAccount(child21);
        
        

        saveTestTransaction(parent1, parseDate("01.01.2013"));
        saveTestTransaction(child1, parseDate("01.01.2013"));
        saveTestTransaction(child2, parseDate("01.01.2013"));
        saveTestTransaction(child21, parseDate("01.01.2013"));

        // find parent only
        assertTx(parent1, 1, false);
        assertTx(child1, 1, false);
        assertTx(child2, 1, false);
        assertTx(child21, 1, false);

        // find parent with children
        assertTx(parent1, 4, true);
        assertTx(child1, 1, true);
        assertTx(child2, 2, true);
        assertTx(child21, 1, true);

    }

    private void assertTx(AccountDTO account, int txCount, boolean withChildren) {
        Assert.assertTrue(m_budgetService.findTransactions(parseDate("01.01.2013"), parseDate("31.01.2013"), account.getId(),
                withChildren).size() == txCount);
    }

    /**
     * @param child21
     * @param parseDate
     * @param parseDate2
     * @param i
     */
    private void saveTestTransaction(AccountDTO accountDTO, Date date) {
        TransactionDTO tx = new TransactionDTO();
        tx.setValuta(date);
        tx.setAccount(accountDTO);
        tx.setAmount(new BigDecimal(0));
        m_budgetService.saveTransaction(tx);
    }

    @Test
    public void testBalances() throws BudgetException {

        // create some accounts and tx
        DatenHelper helper = new DatenHelper(m_budgetDAO);
        helper.saveTestAccounts();
        helper.saveTestTransaction(helper.getSavedTestAccounts().get(0), DatenHelper.parseDate("01.01.2009"),
                DatenHelper.parseDate("31.12.2009"), 50);
        helper.saveTestTransaction(helper.getSavedTestAccounts().get(1), DatenHelper.parseDate("01.01.2009"),
                DatenHelper.parseDate("31.12.2009"), 50);

        // create a monthly matrix
        Date from = DatenHelper.parseDate("01.01.2009");
        Date to = DatenHelper.parseDate("31.12.2009");
        TimeGroup groupBy = TimeGroup.MONTH;
        BalanceSheetDTO matrix = m_budgetService.getAccountBalances(from, to, groupBy);

        // groupby
        Assert.assertEquals(matrix.getGroupedBy(), TimeGroup.MONTH);

        // groupKeys
        Assert.assertTrue(matrix.getGroupKeys().size() > 0);

        // check if there are values
        for (BalanceSheetRowDTO row : matrix.getRows()) {
            Assert.assertNotNull(row);
            // check groups
            for (BigDecimal group : row.getGroupTotals()) {
                Assert.assertNotNull(group);
            }

            // check total
            Assert.assertNotNull(row.getRowAvg());
            Assert.assertNotNull(row.getRowTotal());

        }

    }

    @Test
    public void testBookTransfer() throws BudgetException {

        // create some accounts
        AccountDTO acFrom = new AccountDTO();
        acFrom.setName("from");
        acFrom = m_budgetService.saveAccount(acFrom);
        AccountDTO acTo1 = new AccountDTO();
        acTo1.setName("to1");
        acTo1 = m_budgetService.saveAccount(acTo1);
        AccountDTO acTo2 = new AccountDTO();
        acTo2.setName("to2");
        acTo2 = m_budgetService.saveAccount(acTo2);

        // book a transfer
        TransferTransactionDTO transferTx = new TransferTransactionDTO();
        String bookingText = "Umbuchung ab t1";
        Date valuta = DatenHelper.parseDate("15.02.2008");
        transferTx.setFromAccount(acFrom);
        transferTx.setValuta(valuta);
        transferTx.setBookingText(bookingText);

        // target account1
        String target1Text = "buchung auf to1";
        BigDecimal target1Amount = new BigDecimal("15.25");
        TransferTargetDTO target1 = new TransferTargetDTO();
        target1.setToAccount(acTo1);
        target1.setAmount(target1Amount);
        target1.setBookingText(target1Text);
        transferTx.addTarget(target1);

        // target account2
        String target2Text = "buchung auf to";
        BigDecimal target2Amount = new BigDecimal("5.15");
        TransferTargetDTO target2 = new TransferTargetDTO();
        target2.setToAccount(acTo2);
        target2.setAmount(target2Amount);
        target2.setBookingText(target2Text);
        transferTx.addTarget(target2);

        // book the tx
        m_budgetService.bookTransfer(transferTx);

        // read the three tx
        Assert.assertEquals(3, m_budgetService.findTransactions(valuta, valuta).size());

        TransactionDTO txFrom = m_budgetService.findTransactions(valuta, valuta, acFrom.getId(), false).get(0);
        Assert.assertEquals(acFrom, txFrom.getAccount());
        Assert.assertEquals(BudgetUtil.multiply(target1Amount.add(target2Amount), new BigDecimal("-1")),
                txFrom.getAmount());
        Assert.assertEquals(bookingText, txFrom.getBookingText());
        Assert.assertEquals(valuta, txFrom.getValuta());

        TransactionDTO txTo1 = m_budgetService.findTransactions(valuta, valuta, acTo1.getId(), false).get(0);
        Assert.assertEquals(acTo1, txTo1.getAccount());
        Assert.assertEquals(target1Amount, txTo1.getAmount());
        Assert.assertEquals(target1Text, txTo1.getBookingText());
        Assert.assertEquals(valuta, txTo1.getValuta());

        TransactionDTO txTo2 = m_budgetService.findTransactions(valuta, valuta, acTo2.getId(), false).get(0);
        Assert.assertEquals(acTo2, txTo2.getAccount());
        Assert.assertEquals(target2Amount, txTo2.getAmount());
        Assert.assertEquals(target2Text, txTo2.getBookingText());
        Assert.assertEquals(valuta, txTo2.getValuta());

    }
}
