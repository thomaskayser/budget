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
package ch.tkayser.budget.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ch.tkayser.budget.dto.AccountDTO;
import ch.tkayser.budget.dto.BalanceSheetDTO;
import ch.tkayser.budget.dto.BalanceSheetRowDTO;
import ch.tkayser.budget.dto.BudgetDTO;
import ch.tkayser.budget.dto.TimeGroup;
import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.dto.TransferTransactionDTO;
import ch.tkayser.budget.exception.BudgetException;
import ch.tkayser.budget.service.BudgetService;

public class BudgetMockService implements BudgetService {

    private static final String       DEFAULT_DATE_FORMAT = "dd.MM.yyyy";

    // ids
    private static int                transactionId       = 0;
    private static int                accountId           = 0;
    private static int                budgetId            = 0;

    // maps with transactions, accounts and budgets, the id is the key of the
    // map
    // @formatter:off
    private Map<Long, TransactionDTO> m_transactions;
    private Map<Long, AccountDTO>     m_accounts;
    private Map<Long, BudgetDTO>      m_budgets;

    // a random
    private Random                    m_random;

    // @formatter:on

    public BudgetMockService() {
        initTestData();
        m_random = new Random();
        m_random.setSeed(new Date().getTime());
    }

    public List<TransactionDTO> bookTransactions(List<TransactionDTO> unbookedTx) throws BudgetException {
        return unbookedTx;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.tkayser.budget.service.BudgetService#bookTransfer(ch.tkayser.budget
     * .dto.TransferTransactionDTO)
     */
    public void bookTransfer(TransferTransactionDTO transferTx) throws BudgetException {
        // do nothing
    }

    public void deleteAccount(AccountDTO account) throws BudgetException {
        account.setParent(null);
        m_accounts.remove(account.getId());
    }

    @Override
    public void deleteBudget(BudgetDTO budget) throws BudgetException {
        m_budgets.remove(budget.getId());
    }

    public void deleteTransaction(TransactionDTO tx) throws BudgetException {
        m_transactions.remove(tx.getId());
    }

    public List<TransactionDTO> findTransactions(Date from, Date to) throws BudgetException {
        return new ArrayList<TransactionDTO>(m_transactions.values());
    }

    public List<TransactionDTO> findTransactions(Date from, Date to, Long accountId, boolean withChildren)
            throws BudgetException {
        List<TransactionDTO> result = new ArrayList<TransactionDTO>();
        for (TransactionDTO tx : m_transactions.values()) {
            // tx belongs to account
            if (accountId.equals(tx.getAccount().getId())) {
                result.add(tx);
            }
            if (withChildren) {
                // blongs to child account?
                AccountDTO parent = m_accounts.get(accountId);
                for (AccountDTO child : parent.getChildren()) {
                    if (child.equals(tx.getAccount())) {
                        result.add(tx);
                    }
                }
            }
        }
        return result;
    }

    public List<TransactionDTO> findTransactions(String reciever, String sender, String bookingText)
            throws BudgetException {
        return new ArrayList<TransactionDTO>(m_transactions.values());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.tkayser.budget.service.BudgetService#getAccountBalances(java.util.
     * Date, java.util.Date, ch.tkayser.budget.service.TimeGroup)
     */
    public BalanceSheetDTO getAccountBalances(Date from, Date to, TimeGroup groupedBy) throws BudgetException {

        BalanceSheetDTO sheet = new BalanceSheetDTO();
        sheet.setGroupedBy(groupedBy);
        sheet.setGroupKeys(TimeGroup.getGroupKeys(from, to, groupedBy));

        // two roots with some children
        BalanceSheetRowDTO root = new BalanceSheetRowDTO();
        root.setTitle("Arzt");
        setRandomAmounts(root);
        root.setTotalRow(false);
        BalanceSheetRowDTO child = new BalanceSheetRowDTO();
        child.setTitle("Zahnarzt");
        child.setTotalRow(false);
        setRandomAmounts(child);
        root.addChildRow(child);
        child = new BalanceSheetRowDTO();
        child.setTitle("Hausazrt");
        child.setTotalRow(false);
        setRandomAmounts(child);
        root.addChildRow(child);
        sheet.getRows().add(root);
        root = new BalanceSheetRowDTO();
        root.setTitle("Diverses");
        setRandomAmounts(root);
        root.setTotalRow(false);
        sheet.getRows().add(root);

        // total row
        BalanceSheetRowDTO total = new BalanceSheetRowDTO();
        total.setTotalRow(true);
        total.setTitle("Totale");
        setRandomAmounts(total);
        sheet.getRows().add(total);

        return sheet;

    }

    public List<AccountDTO> getAllAccounts() throws BudgetException {
        return new ArrayList<AccountDTO>(m_accounts.values());
    }

    @Override
    public List<BudgetDTO> getBudgets() throws BudgetException {
        return new ArrayList<BudgetDTO>(m_budgets.values());
    }

    private BigDecimal getRandomAmount() {
        // random integer
        Double rndAmount = (double) m_random.nextInt(100000);
        // add double
        rndAmount += m_random.nextDouble();
        BigDecimal amount = new BigDecimal(rndAmount);
        amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
        return amount;
    }

    private List<BigDecimal> getRandomAmountList(int nr) {
        List<BigDecimal> amounts = new ArrayList<BigDecimal>();
        for (int i = 0; i < nr; i++) {
            amounts.add(getRandomAmount());
        }
        return amounts;
    }

    /**
     * create some testdata
     */
    private void initTestData() {
        // create some accounts
        m_accounts = new HashMap<Long, AccountDTO>();
        AccountDTO acGesundheit = new AccountDTO();
        acGesundheit.setId(1L);
        acGesundheit.setName("Gesundheit");
        m_accounts.put(acGesundheit.getId(), acGesundheit);
        AccountDTO acArzt = new AccountDTO();
        acArzt.setId(2L);
        acArzt.setName("Arzt");
        acArzt.setParent(acGesundheit);
        m_accounts.put(acArzt.getId(), acArzt);
        AccountDTO acKK = new AccountDTO();
        acKK.setId(3L);
        acKK.setName("Krankenkasse");
        acKK.setParent(acGesundheit);
        m_accounts.put(acKK.getId(), acKK);
        AccountDTO acMiete = new AccountDTO();
        acMiete.setId(4L);
        acMiete.setName("Miete");
        m_accounts.put(acMiete.getId(), acMiete);

        // some tx
        m_transactions = new HashMap<Long, TransactionDTO>();
        TransactionDTO tx = new TransactionDTO();
        tx.setAccount(acArzt);
        tx.setReciever("Pilatus Praxis");
        tx.setBookingText("Behandlung 15.2");
        tx.setAmount(BudgetUtil.setScale(new BigDecimal(175.55)));
        tx.setValuta(toDate("15.03.2009"));
        saveTransaction(tx);
        tx = new TransactionDTO();
        tx.setAccount(acKK);
        tx.setReciever("SWICA");
        tx.setBookingText("Februar");
        tx.setAmount(new BigDecimal(175));
        tx.setValuta(toDate("28.02.2009"));
        saveTransaction(tx);
        tx = new TransactionDTO();
        tx.setAccount(acKK);
        tx.setReciever("SWICA");
        tx.setBookingText("März");
        tx.setAmount(new BigDecimal(175));
        tx.setValuta(toDate("28.03.2009"));
        saveTransaction(tx);
        tx = new TransactionDTO();
        tx.setAccount(acKK);
        tx.setSender("SWICA");
        tx.setBookingText("Gutschrift Pilatus Praxis");
        tx.setAmount(new BigDecimal(-85));
        tx.setValuta(toDate("28.04.2009"));
        saveTransaction(tx);
        tx = new TransactionDTO();
        tx.setAccount(acMiete);
        tx.setReciever("Baettig und Bucher");
        tx.setBookingText("Miete Februar");
        tx.setAmount(new BigDecimal(1990));
        tx.setValuta(toDate("01.02.2009"));
        saveTransaction(tx);
        tx = new TransactionDTO();
        tx.setAccount(acMiete);
        tx.setReciever("Baettig und Bucher");
        tx.setBookingText("Miete Maerz");
        tx.setAmount(new BigDecimal(1990));
        tx.setValuta(toDate("01.03.2009"));
        saveTransaction(tx);

        // create two budgets
        m_budgets = new HashMap<Long, BudgetDTO>();
        BudgetDTO m = new BudgetDTO();
        m.setId(1L);
        m.setName("Budget 1");
        m.setAmountPerMonth(new BigDecimal("3500"));
        m_budgets.put(m.getId(), m);

        m = new BudgetDTO();
        m.setId(2L);
        m.setName("Budget 2");
        m.setAmountPerMonth(new BigDecimal("5500"));
        m_budgets.put(m.getId(), m);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.service.BudgetService#isAlive()
     */
    public Boolean isAlive() {
        return Boolean.TRUE;
    }

    public AccountDTO saveAccount(AccountDTO account) throws BudgetException {
        if (account.getId() == null) {
            account.setId(Long.valueOf(accountId++));
        }
        m_accounts.put(account.getId(), account);
        return account;
    }

    @Override
    public BudgetDTO saveBudget(BudgetDTO budget) throws BudgetException {
        if (budget.getId() == null) {
            budget.setId(Long.valueOf(budgetId++));
        }
        m_budgets.put(budget.getId(), budget);
        return budget;
    }

    public TransactionDTO saveTransaction(TransactionDTO tx) throws BudgetException {
        if (tx.getId() == null) {
            tx.setId(Long.valueOf(transactionId++));
        }
        m_transactions.put(tx.getId(), tx);
        return tx;
    }

    /**
     * set random amounts on a row
     * 
     * @param row
     */
    private void setRandomAmounts(BalanceSheetRowDTO row) {
        row.setGroupTotals(getRandomAmountList(5));
        row.setRowTotal(getRandomAmount());
        row.setRowAvg(getRandomAmount());
    }

    private Date toDate(String date) {
        try {
            return parseDate(DEFAULT_DATE_FORMAT, date);
        } catch (Exception e) {
            return null;
        }
    }

    private Date parseDate(String format, String date) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

}
