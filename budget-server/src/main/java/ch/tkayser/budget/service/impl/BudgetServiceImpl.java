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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.tkayser.budget.base.service.impl.BaseServiceImpl;
import ch.tkayser.budget.dao.BudgetDAO;
import ch.tkayser.budget.domain.Account;
import ch.tkayser.budget.domain.AccountBalanceForTimeGroup;
import ch.tkayser.budget.domain.AccountBalances;
import ch.tkayser.budget.domain.BalanceSheet;
import ch.tkayser.budget.domain.Budget;
import ch.tkayser.budget.domain.Transaction;
import ch.tkayser.budget.dto.AccountDTO;
import ch.tkayser.budget.dto.BalanceSheetDTO;
import ch.tkayser.budget.dto.BalanceSheetRowDTO;
import ch.tkayser.budget.dto.BudgetDTO;
import ch.tkayser.budget.dto.TimeGroup;
import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.dto.TransferTargetDTO;
import ch.tkayser.budget.dto.TransferTransactionDTO;
import ch.tkayser.budget.exception.BudgetExceptionInterceptor;
import ch.tkayser.budget.service.BudgetService;
import ch.tkayser.budget.service.BudgetServiceConstants;
import ch.tkayser.budget.util.BudgetUtil;

/**
 * @author isc-kat
 * 
 */
@Stateless
@Local
@Remote
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Interceptors(BudgetExceptionInterceptor.class)
@RolesAllowed("budget_user")
public class BudgetServiceImpl extends BaseServiceImpl implements BudgetService {

    @EJB
    private BudgetDAO m_budgetDAO;

    // a logger
    private Logger m_log = LoggerFactory.getLogger(BudgetServiceImpl.class);

    /**
     * add child rows to a parent row. recursive
     * 
     * @param account
     * @param sheet
     * @param parent
     */
    private void addChildRows(BalanceSheet sheet, Account account, BalanceSheetRowDTO parent) {
        // loop over children of an account
        for (Account childAccount : account.getChildren()) {
            // create row for the child and add to parent row
            BalanceSheetRowDTO childRow = toRowDTO(sheet.getAccountBalances(childAccount));
            parent.addChildRow(childRow);

            // check for children
            if (childAccount.getChildren().size() > 0) {
                // add children recursive
                addChildRows(sheet, childAccount, childRow);
            }

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.service.BudgetService#createTransactions(java.util. List)
     */
    public List<TransactionDTO> bookTransactions(List<TransactionDTO> unbookedTx) {

        // try to find accounts for the transactions
        for (TransactionDTO transaction : unbookedTx) {

            // get the interesting text fields
            String reciever = transaction.getReciever();
            String sender = transaction.getSender();
            String bookingText = transaction.getBookingText();

            // find for same reciever
            List<TransactionDTO> foundTx = null;
            if (reciever != null) {
                m_log.debug("Searching tx with reciever {}", reciever);
                foundTx = findTransactions(reciever, null, null);
                setAccountFromFirstTransaction(transaction, foundTx);
            }

            // not found? find for same sender
            if (transaction.getAccount() == null && sender != null) {
                m_log.debug("Searching tx with sender {}", sender);
                foundTx = findTransactions(null, sender, null);
                setAccountFromFirstTransaction(transaction, foundTx);

            }

            // not found? search with booking text
            if (transaction.getAccount() == null && bookingText != null) {

                // try with parts of booking text
                StringTokenizer tokens = new StringTokenizer(bookingText, BudgetServiceConstants.BOOKINGTEXT_LINE_SEPARATOR.trim());
                while (tokens.hasMoreElements() && transaction.getAccount() == null) {
                    String text = tokens.nextToken();
                    m_log.debug("Searching tx with text {}", text);
                    foundTx = findTransactions(null, null, text);
                    setAccountFromFirstTransaction(transaction, foundTx);

                }

            }

        }

        return unbookedTx;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.service.BudgetService#bookTransfer(ch.tkayser.budget .dto.TransferTransactionDTO)
     */
    public void bookTransfer(TransferTransactionDTO transferTx) {

        // create the target tx
        BigDecimal sum = new BigDecimal(0);
        for (TransferTargetDTO target : transferTx.getTargets()) {
            TransactionDTO txTo = new TransactionDTO();
            txTo.setAccount(target.getToAccount());
            txTo.setAmount(target.getAmount());
            txTo.setValuta(transferTx.getValuta());
            txTo.setBookingText(target.getBookingText());
            saveTransaction(txTo);
            // sum up the amounts
            sum = sum.add(target.getAmount());
        }

        // create the from tx
        TransactionDTO txFrom = new TransactionDTO();
        txFrom.setAccount(transferTx.getFromAccount());
        txFrom.setAmount(BudgetUtil.multiply(sum, new BigDecimal("-1")));
        txFrom.setValuta(transferTx.getValuta());
        txFrom.setBookingText(transferTx.getBookingText());
        saveTransaction(txFrom);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.service.BudgetService#deleteAccount(ch.tkayser.budget .dto.AccountDTO)
     */
    public void deleteAccount(AccountDTO account) {
        Account a = map(account, new Account());
        m_budgetDAO.deleteEntity(a);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.service.BudgetService#deleteBudget(ch.tkayser.budget.dto.BudgetDTO)
     */
    public void deleteBudget(BudgetDTO budget) {
        Budget b = fromBudgetDTO(budget);
        m_budgetDAO.deleteEntity(b);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.service.BudgetService#deleteTransaction(ch.tkayser. budget.dto.TransactionDTO)
     */
    public void deleteTransaction(TransactionDTO tx) {
        Transaction transaction = map(tx, new Transaction());
        m_budgetDAO.deleteEntity(transaction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.service.BudgetService#findAllTransaction(java.util. Date, java.util.Date)
     */
    public List<TransactionDTO> findTransactions(Date from, Date to) {
        return toTransactionDTO(m_budgetDAO.findTransactions(from, to));
    }


    public List<TransactionDTO> findTransactions(Date from, Date to, Long accountId, boolean withChildAccounts) {
        // liste mit allen Accounts aufbereiten
        List<Long> accountIds = new ArrayList<Long>();        
        accountIds.add(accountId);
        if (withChildAccounts) {
            // parent laden
            Account account = m_budgetDAO.findEntityById(Account.class, accountId);
            addAllChildAccountIds(account, accountIds);
        }
        return toTransactionDTO(m_budgetDAO.findTransactions(from, to,accountIds));
    }
    
    private void addAllChildAccountIds(Account parent, List<Long> accountIds) {
        if (parent != null && parent.getChildren() != null) {
            for (Account child: parent.getChildren()) {
                // id des Childs hinzufuegen
                accountIds.add(child.getId());
                // children des childs hinzufuegen
                addAllChildAccountIds(child, accountIds);                
            }            
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.service.BudgetService#findTransactions(java.lang.String , java.lang.String, java.lang.String)
     */
    public List<TransactionDTO> findTransactions(String reciever, String sender, String bookingText) {
        return toTransactionDTO(m_budgetDAO.findTransactions(reciever, sender, bookingText));
    }

    /**
     * transform accountDTO -> account
     * 
     * @param account
     * @return
     */
    protected Account fromAccountDTO(AccountDTO accountDTO) {
        return map(accountDTO, new Account());
    }

    /**
     * transform budgetDTO -> budget
     * 
     * @param budget
     * @return
     */
    protected Budget fromBudgetDTO(BudgetDTO budgetDTO) {
        return map(budgetDTO, new Budget());
    }

    /**
     * transform accountDTO -> account
     * 
     * @param account
     * @return
     */
    protected Transaction fromTransactionDTO(TransactionDTO tx) {
        return map(tx, new Transaction());
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.service.BudgetService#getAccountBalances(java.util. Date, java.util.Date,
     * ch.tkayser.budget.service.TimeGroup)
     */
    public BalanceSheetDTO getAccountBalances(Date from, Date to, TimeGroup groupedBy) {

        // get the account balances and set the accounts
        List<AccountBalanceForTimeGroup> countedBalances = m_budgetDAO.countBalances(from, to, groupedBy);

        // create the matrix
        BalanceSheet matrix = new BalanceSheet(m_budgetDAO.findAllentitties(Account.class, Account.NAME_ATTRIBUT), from, to, groupedBy);

        // add all balances
        matrix.addAccountBalances(countedBalances);

        // calculate the rest
        matrix.calculateMatrixColumns();

        // convert to dto
        return toBalanceSheetDTO(matrix);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.service.BudgetService#getAllAccounts()
     */
    public List<AccountDTO> getAllAccounts() {
        // read all
        List<AccountDTO> result = new ArrayList<AccountDTO>();
        for (Account ac : m_budgetDAO.findAllentitties(Account.class, Account.NAME_ATTRIBUT)) {

            // convert to dto
            result.add(map(ac, new AccountDTO()));
        }

        // return them
        return result;
    }

    @Override
    public List<BudgetDTO> getBudgets() {
        // read all
        List<BudgetDTO> result = new ArrayList<BudgetDTO>();
        for (Budget budget : m_budgetDAO.findAllentitties(Budget.class, Budget.NAME_ATTRIBUT)) {
            // convert to dto
            result.add(toBudgetDTO(budget));
        }
        // return them
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.service.BudgetService#isAlive()
     */
    public Boolean isAlive() {
        return Boolean.TRUE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.service.BudgetService#saveAccount(ch.tkayser.budget .dto.AccountDTO)
     */
    public AccountDTO saveAccount(AccountDTO account) {
        Account acToSave = map(account, new Account());
        acToSave = m_budgetDAO.saveEntity(acToSave);
        return map(acToSave, new AccountDTO());
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.service.BudgetService#saveBudget(ch.tkayser.budget.dto.BudgetDTO)
     */
    public BudgetDTO saveBudget(BudgetDTO budget) {
        Budget budgetToSave = fromBudgetDTO(budget);
        budgetToSave = m_budgetDAO.saveEntity(budgetToSave);
        return toBudgetDTO(budgetToSave);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.service.BudgetService#saveTransaction(ch.tkayser.budget .dto.TransactionDTO)
     */
    public TransactionDTO saveTransaction(TransactionDTO tx) {
        Transaction txToSave = map(tx, new Transaction());
        txToSave = m_budgetDAO.saveEntity(txToSave);
        return map(txToSave, new TransactionDTO());
    }

    private void setAccountFromFirstTransaction(TransactionDTO transaction, List<TransactionDTO> foundTx) {

        if (foundTx.size() > 0) {

            // sort the transactions by valuate (desc)
            Collections.sort(foundTx, new Comparator<TransactionDTO>() {
                public int compare(TransactionDTO o1, TransactionDTO o2) {
                    // null check
                    Date valuta1 = o1.getValuta();
                    Date valuta2 = o2.getValuta();
                    if (valuta1 == null || valuta2 == null) {
                        return 1;
                    }

                    // compare valutas and reverse order
                    int result = o1.getValuta().compareTo(o2.getValuta());
                    result *= -1;
                    return result;
                }
            });

            // get account from first one
            transaction.setAccount(foundTx.get(0).getAccount());
            m_log.debug("Account {} found in tx from {}", transaction.getAccount().getName(), foundTx.get(0).getValuta());

        }

    }

    /**
     * transform account -> accountDTO
     * 
     * @param account
     * @return
     */
    protected AccountDTO toAccountDTO(Account account) {
        return map(account, new AccountDTO());
    }

    /**
     * @param sheet
     * @return
     */
    protected BalanceSheetDTO toBalanceSheetDTO(BalanceSheet sheet) {

        // convert to dto manually
        BalanceSheetDTO sheetDTO = new BalanceSheetDTO();

        // process root balances
        for (AccountBalances balances : sheet.getAccountBalances()) {
            if (balances.getAccount().getParent() == null) {
                // create and add the row
                BalanceSheetRowDTO parentRow = toRowDTO(balances);
                sheetDTO.getRows().add(parentRow);
                // process children (recursive)
                addChildRows(sheet, balances.getAccount(), parentRow);
            }
        }

        // order by title
        sheetDTO.orderByTitle();

        // add total row
        BalanceSheetRowDTO totalRow = new BalanceSheetRowDTO();
        totalRow.setTotalRow(true);
        totalRow.setTitle(BudgetServiceConstants.TOTAL_ROW_TITLE);
        totalRow.setGroupTotals(sheet.getGroupTotals());
        totalRow.setRowTotal(sheet.getGrandTotal());
        totalRow.setRowAvg(sheet.getGrandAvg());
        sheetDTO.getRows().add(totalRow);

        // set timegroup
        sheetDTO.setGroupedBy(sheet.getGroupedBy());
        
        // group keys
        sheetDTO.setGroupKeys(new ArrayList<Integer>(sheet.getGroupKeys()));
        
        // return the sheet
        return sheetDTO;

    }

    /**
     * map budget -> budgetDTO
     * 
     * @param budget
     * @return
     */
    protected BudgetDTO toBudgetDTO(Budget budget) {
        return map(budget, new BudgetDTO());
    }

    /**
     * create a balanceSheetRow from an accountBalances
     * 
     * @param balances
     * @return
     */
    private BalanceSheetRowDTO toRowDTO(AccountBalances balances) {

        // create the row
        BalanceSheetRowDTO row = new BalanceSheetRowDTO();

        // set the accountname as title
        row.setTitle(balances.getAccount().getName());

        // add group totals, row total and row avg
        row.setGroupTotals(new ArrayList<BigDecimal>(balances.getTimeGroupBalances()));
        row.setRowTotal(balances.getBalance());
        row.setRowAvg(balances.getAvgBalance());

        return row;

    }

    /**
     * convert a list of Transaction to TransactionDTO
     * 
     * @param allTx
     * @return
     */
    private List<TransactionDTO> toTransactionDTO(List<Transaction> allTx) {
        List<TransactionDTO> result = new ArrayList<TransactionDTO>();
        for (Transaction tx : allTx) {
            result.add(map(tx, new TransactionDTO()));
        }
        return result;
    }

    /**
     * map an account to an accountDTO
     * 
     * @param account
     * @return
     */
    protected TransactionDTO toTransactionDTO(Transaction tx) {
        return map(tx, new TransactionDTO());
    }

}
