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

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountBalanceForTimeGroup implements Serializable  {

    private static final long serialVersionUID = -3809584111589434594L;

    // the accounid and account
    private Account    m_account;

    // time group
    private Integer    m_timeKey;

    // the balances
    private BigDecimal m_balance;

    public AccountBalanceForTimeGroup(Account account, Integer timeKey, BigDecimal balance) {
        m_account = account;
        m_timeKey = timeKey;
        m_balance = balance;
    }

    public AccountBalanceForTimeGroup(Account account, BigDecimal balance) {
        this(account, null, balance);
    }

    /**
     * @return the accountId
     */
    public Account getAccount() {
        return m_account;
    }

    /**
     * @return the timeGroup
     */
    public Integer getTimeKey() {
        return m_timeKey;
    }

    /**
     * @return the balance
     */
    public BigDecimal getBalance() {
        return m_balance;
    }

}
