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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import ch.tkayser.budget.base.dao.BudgetNamedQueries;

/**
 * @author isc-kat
 * 
 */
@Entity
@Table(name = "TRANSACTION")
@NamedQueries( {
	@NamedQuery(name = BudgetNamedQueries.TRANSACTION_FIND_BY_DATERANGE, query = "SELECT t from Transaction t WHERE t.m_valuta between :"
		+ Transaction.QP_DATEFROM + " AND :" + Transaction.QP_DATETO),
	@NamedQuery(name = BudgetNamedQueries.TRANSACTION_FIND_BY_ACCOUNTS_AND_DATERANGE, query = "SELECT t from Transaction t WHERE t.m_valuta between :"
		+ Transaction.QP_DATEFROM
		+ " AND :"
		+ Transaction.QP_DATETO
		+ " AND m_account.id IN (:"
		+ Transaction.QP_ACCOUNTS_IDS+")") })
public class Transaction implements Serializable {

    private static final long serialVersionUID = 2152853207637896591L;
    
    // constants for query parameter
    public static final String QP_DATEFROM = "dateFrom";
    public static final String QP_DATETO = "dateTo";
    public static final String QP_ACCOUNTS_IDS = "accountIds";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_SEQ")
    @SequenceGenerator(name = "GEN_SEQ", sequenceName = "TRANSACTION_SEQ", allocationSize = 10, initialValue = 1)
    @Column(name = "TRX_ID")
    private Long m_id;

    @Column(name = "TRX_VALUTA", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date m_valuta;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "TRX_ACT_ID", nullable=false)
    private Account m_account;

    @Column(name = "TRX_SENDER", length=1000)
    private String m_sender;

    @Column(name = "TRX_RECIEVER", length=1000)
    private String m_reciever;

    @Column(name = "TRX_BOOKINGTEXT", length=1000)
    private String m_bookingText;

    @Column(name = "TRX_AMOUNT", nullable=false)    
    private BigDecimal m_amount;

    @Version
    @Column(name = "TRX_VERSION")
    private Integer m_version;

    /**
     * @return the account
     */
    public Account getAccount() {
	return m_account;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
	return m_amount;
    }

    /**
     * @return the bookingText
     */
    public String getBookingText() {
	return m_bookingText;
    }

    /**
     * @return the id
     */
    public Long getId() {
	return m_id;
    }

    /**
     * @return the reciever
     */
    public String getReciever() {
	return m_reciever;
    }

    /**
     * @return the sender
     */
    public String getSender() {
	return m_sender;
    }

    /**
     * @return the valuata
     */
    public Date getValuta() {
	return m_valuta;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return m_version;
    }

    /**
     * @param account
     *            the account to set
     */
    public void setAccount(Account account) {
	m_account = account;
    }

    /**
     * @param amount
     *            the amount to set
     */
    public void setAmount(BigDecimal amount) {
	m_amount = amount;
    }

    /**
     * @param bookingText
     *            the bookingText to set
     */
    public void setBookingText(String bookingText) {
	m_bookingText = bookingText;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
	m_id = id;
    }

    /**
     * @param reciever
     *            the reciever to set
     */
    public void setReciever(String reciever) {
	m_reciever = reciever;
    }

    /**
     * @param sender
     *            the sender to set
     */
    public void setSender(String sender) {
	m_sender = sender;
    }

    /**
     * @param valuta
     *            the valuata to set
     */
    public void setValuta(Date valuta) {
	m_valuta = valuta;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.m_version = version;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	StringBuilder out = new StringBuilder();
	out.append(new SimpleDateFormat("dd.MM.yyyy").format(getValuta()));
	out.append(": ");
	if (getAccount() != null) {
		out.append("[" + getAccount().getName() + "] ");	    
	}
	out.append(getAmount());
	out.append(" - ");
	out.append("FROM: " + getSender());
	out.append(" - ");
	out.append("TO: " + getReciever());
	out.append(" - ");
	out.append(getBookingText());
	return out.toString();
    }

}
