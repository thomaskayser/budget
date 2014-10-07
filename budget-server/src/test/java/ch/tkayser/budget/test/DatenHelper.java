package ch.tkayser.budget.test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.tkayser.budget.dao.BudgetDAO;
import ch.tkayser.budget.domain.Account;
import ch.tkayser.budget.domain.Transaction;

/**
 * @author isc-kat
 * 
 */
/**
 * @author tom
 *
 */
public class DatenHelper {

    // a logger
    private static Logger      s_log                  = LoggerFactory.getLogger(DatenHelper.class);

    // test accounts and transactions
    private List<Account>      m_testAccounts;
    private List<Transaction>  m_testTransactions;

    // a random
    private Random             m_random;

    // the baseDAO
    private BudgetDAO          m_baseDAO;

    // booking text constant
    public static final String BOOKING_TEXT_TEMPLATE  = "Buchung";
    public static final String SENDER_TEXT_TEMPLATE   = "From";
    public static final String RECIEVER_TEXT_TEMPLATE = "To";

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public DatenHelper() {
        this(null);
    }

    public DatenHelper(BudgetDAO baseDAO) {
        super();
        // store DAO
        m_baseDAO = baseDAO;
        // init random
        m_random = new Random();
        m_random.setSeed(new Date().getTime());
        // init arrays
        m_testTransactions = new ArrayList<Transaction>();
        m_testAccounts = new ArrayList<Account>();
    }

    /**
     * delete testdata we created
     */
    public void cleanUp() {

        // remove transactions (if there are any
        Iterator<Transaction> itTx = m_testTransactions.iterator();
        while (itTx.hasNext()) {
            Transaction tx = itTx.next();
            // read again, delete and flush
            tx = m_baseDAO.findEntityById(Transaction.class, tx.getId());
            m_baseDAO.deleteEntity(tx);
            itTx.remove();

        }

        // remove accounts (children first)
        for (Iterator<Account> itAc = m_testAccounts.iterator(); itAc.hasNext();) {
            try {
                Account ac = itAc.next();
                if (ac.getParent() != null) {
                    // read again, delete and flush
                    ac = m_baseDAO.findEntityById(Account.class, ac.getId());
                    m_baseDAO.deleteEntity(ac);
                    itAc.remove();
                }
            } catch (EntityExistsException e) {
                // ignore...
                s_log.error("Exception ", e);
            }
        }
        // now parents
        for (Iterator<Account> itAc = m_testAccounts.iterator(); itAc.hasNext();) {
            try {
                Account ac = itAc.next();
                // read again, delete and flush
                ac = m_baseDAO.findEntityById(Account.class, ac.getId());
                m_baseDAO.deleteEntity(ac);
                itAc.remove();
            } catch (EntityExistsException e) {
                // ignore...
                s_log.error("Exception ", e);
            }
        }
    }

    /**
     * get a random date
     * 
     * @param from
     * @param to
     * @return
     */
    public Date getRandomDate(Date from, Date to) {
        // get days between datest
        Calendar start = Calendar.getInstance();
        start.setTime(from);
        Calendar end = Calendar.getInstance();
        end.setTime(to);
        int days = end.get(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR);
        // add random number of days to start date
        start.add(Calendar.DATE, m_random.nextInt(days));
        // return the date
        return start.getTime();
    }

    /**
     * @return the testAccounts
     */
    public List<Account> getSavedTestAccounts() {
        return m_testAccounts;
    }

    /**
     * @return the testTransactions
     */
    public List<Transaction> getSavedTestTransactions() {
        return m_testTransactions;
    }

    /**
     * save some test accounts
     */
    public void saveTestAccounts() {

        // create some accounts
        // single account
        Account account = new Account();
        account.setName("Ferien");
        account = m_baseDAO.saveEntity(account);
        m_testAccounts.add(account);

        // account with sub accounts
        account = new Account();
        account.setName("Arzt");
        account = m_baseDAO.saveEntity(account);
        m_testAccounts.add(account);
        Account acMain = account;

        account = new Account();
        account.setName("Zahnarzt");
        account.setParent(acMain);
        account = m_baseDAO.saveEntity(account);
        m_testAccounts.add(account);

        account = new Account();
        account.setName("Hausarzt");
        account.setParent(acMain);
        account = m_baseDAO.saveEntity(account);
        m_testAccounts.add(account);

    }

    /**
     * create and save some tx
     * 
     * @param account
     * @param from
     * @param to
     * @param numberOfTX
     */
    public void saveTestTransaction(Account account, Date from, Date to, int numberOfTX) {

        for (int i = 0; i < numberOfTX; i++) {

            // create a tx
            Transaction tx = new Transaction();
            tx.setAccount(account);
            tx.setAmount(getRandomAmount());
            tx.setBookingText(DatenHelper.BOOKING_TEXT_TEMPLATE + " " + i);
            tx.setSender(DatenHelper.SENDER_TEXT_TEMPLATE + " " + i);
            tx.setReciever(DatenHelper.RECIEVER_TEXT_TEMPLATE + " " + i);
            tx.setValuta(getRandomDate(from, to));
            tx = m_baseDAO.saveEntity(tx);
            m_testTransactions.add(tx);
        }

    }

    public BigDecimal getRandomAmount() {
        // random integer
        Double rndAmount = (double) m_random.nextInt(100000);
        // add double
        rndAmount += m_random.nextDouble();
        BigDecimal amount = new BigDecimal(rndAmount);
        amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
        return amount;
    }

    /**
     * create test accounts:
     * <ul>
     * <li>0 Wohnung</li>
     * <li>1 > Heizkosten</li>
     * <li>2 > Miete</li>
     * <li>3 -> Nebenkosten</li>
     * <li>4 Diverses</li>
     * </ul>
     * 
     * @return
     */
    public List<Account> getTestAccountsForBalanceSheet() {

        List<Account> accounts = new ArrayList<Account>();
        Account accountWohnung = new Account();
        accountWohnung.setId(1L);
        accountWohnung.setName("Wohnung");
        accounts.add(accountWohnung);

        Account ac = new Account();
        ac.setId(2L);
        ac.setName("Heizkosten");
        ac.setParent(accountWohnung);
        accounts.add(ac);

        Account acMiete = new Account();
        acMiete.setId(3L);
        acMiete.setName("Miete");
        acMiete.setParent(accountWohnung);
        accounts.add(acMiete);

        ac = new Account();
        ac.setId(3L);
        ac.setName("Nebenkosten");
        ac.setParent(acMiete);
        accounts.add(ac);

        ac = new Account();
        ac.setId(4L);
        ac.setName("Diverses");
        accounts.add(ac);

        return accounts;
    }

}
