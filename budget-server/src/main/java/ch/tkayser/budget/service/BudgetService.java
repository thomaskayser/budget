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
package ch.tkayser.budget.service;

import java.util.Date;
import java.util.List;

import ch.tkayser.budget.dto.AccountDTO;
import ch.tkayser.budget.dto.BalanceSheetDTO;
import ch.tkayser.budget.dto.BudgetDTO;
import ch.tkayser.budget.dto.TimeGroup;
import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.dto.TransferTransactionDTO;

public interface BudgetService {

    /**
     * create transactions from a list of unbooked transactions. trys to find the account from previously booked tx
     * 
     * @param unbookedTx
     * @return @
     */
    public List<TransactionDTO> bookTransactions(List<TransactionDTO> unbookedTx);

    /**
     * book a transfer transaction
     * 
     * @param transferTx @
     */
    public void bookTransfer(TransferTransactionDTO transferTx);

    /**
     * delete an account
     * 
     * @param account
     */
    public void deleteAccount(AccountDTO account);

    /**
     * delete a budget
     * 
     * @param budget @
     */
    public void deleteBudget(BudgetDTO budget);

    /**
     * delete a transaction
     * 
     * @param tx @
     */
    public void deleteTransaction(TransactionDTO tx);

    /**
     * 
     * @param from
     * @param to
     * @return @
     */
    public List<TransactionDTO> findTransactions(Date from, Date to);

    /**
     * 
     * @param from
     * @param to
     * @param account
     * @return @
     */
    public List<TransactionDTO> findTransactions(Date from, Date to, Long accountId, boolean withChildAccounts);

    /**
     * find tx with text infos
     * 
     * @param bookingText search for this pattern. (optional)
     * @param reciever search for this pattern in the reciever. (optional)
     * @param sender search for this pattern in the sender. (optional)
     * @return @
     */
    public List<TransactionDTO> findTransactions(String reciever, String sender, String bookingText);

    /**
     * get the account saldos for a date range grouped by a timeGroup
     * 
     * @param from
     * @param to
     * @param groupedBy
     * @return @
     */
    public BalanceSheetDTO getAccountBalances(Date from, Date to, TimeGroup groupedBy);

    /**
     * get all accounts
     * 
     * @return
     */
    public List<AccountDTO> getAllAccounts();

    /**
     * get all budget monitors
     * 
     * @return @
     */
    public List<BudgetDTO> getBudgets();

    /**
     * is the service alive?
     * 
     * @return
     */
    public Boolean isAlive();

    /**
     * save an account
     * 
     * @param account
     */
    public AccountDTO saveAccount(AccountDTO account);

    /**
     * save a budget
     * 
     * @param budget
     * @return the saved budget @
     */
    public BudgetDTO saveBudget(BudgetDTO budget);

    /**
     * save a transaction
     * 
     * @param tx @
     */
    public TransactionDTO saveTransaction(TransactionDTO tx);

}
