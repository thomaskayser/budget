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
package ch.tkayser.budget.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import ch.tkayser.budget.base.dao.BudgetNamedQueries;
import ch.tkayser.budget.base.dao.impl.BaseDAOImpl;
import ch.tkayser.budget.dao.BudgetDAO;
import ch.tkayser.budget.domain.Account;
import ch.tkayser.budget.domain.AccountBalanceForTimeGroup;
import ch.tkayser.budget.domain.Transaction;
import ch.tkayser.budget.dto.TimeGroup;

@Stateless
@Local
@Remote
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BudgetDAOImpl extends BaseDAOImpl implements BudgetDAO {

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.dao.BudgetDAO#findAllTransactions(java.util.Date,
     * java.util.Date)
     */
    @SuppressWarnings("unchecked")
    public List<Transaction> findTransactions(Date from, Date to) {
        Query q = m_em.createNamedQuery(BudgetNamedQueries.TRANSACTION_FIND_BY_DATERANGE);
        q.setParameter(Transaction.QP_DATEFROM, from);
        q.setParameter(Transaction.QP_DATETO, to);
        return q.getResultList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.dao.BudgetDAO#findAllTransactions(java.util.Date,
     * java.util.Date, ch.tkayser.budget.dto.AccountDTO)
     */
    @SuppressWarnings("unchecked")
    public List<Transaction> findTransactions(Date from, Date to, List<Long> accountIds) {
        Query q = m_em.createNamedQuery(BudgetNamedQueries.TRANSACTION_FIND_BY_ACCOUNTS_AND_DATERANGE);
        q.setParameter(Transaction.QP_DATEFROM, from);
        q.setParameter(Transaction.QP_DATETO, to);
        q.setParameter(Transaction.QP_ACCOUNTS_IDS, accountIds);
        return q.getResultList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.dao.BudgetDAO#findTransactions(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<Transaction> findTransactions(String reciever, String sender, String bookingText) {

        // create the query string
        StringBuilder query = new StringBuilder();
        query.append("Select t from " + Transaction.class.getSimpleName() + " t WHERE 1 = 1");

        // reciever
        if (reciever != null) {
            query.append(" AND upper(m_reciever) like :reciever ");
        }
        if (sender != null) {
            query.append(" AND upper(m_sender) like :sender ");
        }
        if (bookingText != null) {
            query.append(" AND upper(m_bookingText) like :bookingtext ");
        }

        // create the query
        Query q = m_em.createQuery(query.toString());

        // set the parameters
        if (reciever != null) {
            q.setParameter("reciever", getSearchText(reciever));
        }
        if (sender != null) {
            q.setParameter("sender", getSearchText(sender));
        }
        if (bookingText != null) {
            q.setParameter("bookingtext", getSearchText(bookingText));
        }

        return q.getResultList();

    }

    /**
     * create a searchtext with wildcard
     * 
     * @param text
     * @return
     */
    private String getSearchText(String text) {
        // create the search text
        StringBuilder searchText = new StringBuilder();
        searchText.append("%");
        searchText.append(text.toUpperCase());
        searchText.append("%");
        return searchText.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.dao.BudgetDAO#calculateBalances(java.util.Date,
     * java.util.Date, ch.tkayser.budget.service.TimeGroup)
     */
    @SuppressWarnings("unchecked")
    public List<AccountBalanceForTimeGroup> countBalances(Date from, Date to, TimeGroup group) {
        if (group == null) {
            throw new RuntimeException("group must be set");
        }

        String groupFunc = "";
        switch (group) {
        case MONTH:
            groupFunc = "month(m_valuta)";
            break;
        case QUARTER:
            groupFunc = "(month(m_valuta)-1)/3 + 1";
            break;
        case YEAR:
            groupFunc = "year(m_valuta)";
            break;
        case NONE:
            break;
        }

        // create query for sum per account. Group by parameter if there is one
        StringBuilder q = new StringBuilder();
        q.append("SELECT t.m_account.id ");
        if (!groupFunc.equals("")) {
            q.append(", " + groupFunc);
        }
        q.append(", SUM(m_amount)");
        q.append(" from " + Transaction.class.getSimpleName() + " t ");
        q.append(" where m_valuta between :from and :to");
        q.append(" GROUP BY t.m_account.id ");
        if (!groupFunc.equals("") ) {
            q.append(", " + groupFunc + " ORDER BY " + groupFunc);
        }

        // execute query
        Query query = m_em.createQuery(q.toString());
        query.setParameter("from", from);
        query.setParameter("to", to);
        List<Object[]> resultList = query.getResultList();

        // creaet the results
        List<AccountBalanceForTimeGroup> balances = new ArrayList<AccountBalanceForTimeGroup>();
        for (Object[] o : resultList) {
            AccountBalanceForTimeGroup balance;
            // read the accont
            Account account = findEntityById(Account.class, (Long) o[0]);
            // create balance with or without timeGroup
            if (o.length == 3) {
                balance = new AccountBalanceForTimeGroup(account, (Integer) o[1], (BigDecimal) o[2]);
            } else {
                balance = new AccountBalanceForTimeGroup(account, (BigDecimal) o[1]);
            }
            balances.add(balance);
        }

        // return the found balances
        return balances;

    }

}
