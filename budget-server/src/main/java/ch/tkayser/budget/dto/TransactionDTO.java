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
package ch.tkayser.budget.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import ch.tkayser.budget.base.dto.BaseDTO;

/**
 * @author isc-kat
 * 
 */
public class TransactionDTO extends BaseDTO {

    //@formatter:off
    public static final String PROP_VALUTA      = "valuta";
    public static final String PROP_ACCOUNT     = "account";
    public static final String PROP_AMOUNT      = "amount";
    public static final String PROP_BOOKINGTEXT = "bookingText";
    public static final String PROP_RECIEVER    = "reciever";
    public static final String PROP_SENDER      = "sender";
    //@formatter:on
    private static final long serialVersionUID = -5960956029026096545L;

    // the id
    private Long id;

    // valuata date
    @NotNull
    private Date valuta;

    // the account
    @NotNull
    private AccountDTO account;

    // amount
    @NotNull
    private BigDecimal amount;

    // the booking info
    private String bookingText;

    private String reciever;

    private String sender;

    // the version
    private Integer version;


    /**
     * @return the account
     */
    public AccountDTO getAccount() {
        return account;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @return the bookingText
     */
    public String getBookingText() {
        return bookingText;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the reciever
     */
    public String getReciever() {
        return reciever;
    }

    /**
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * @return the valuata
     */
    public Date getValuta() {
        return valuta;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(AccountDTO account) {
        AccountDTO oldValue = this.account;
        this.account = account;
        firePropertyChange(PROP_ACCOUNT, oldValue, this.account);
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        BigDecimal oldValue = this.amount;
        this.amount = amount;
        firePropertyChange(PROP_AMOUNT, oldValue, this.amount);
    }

    /**
     * @param bookingText the bookingText to set
     */
    public void setBookingText(String bookingText) {
        this.bookingText = bookingText;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @param reciever the reciever to set
     */
    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * @param valuta the valuata to set
     */
    public void setValuta(Date valuta) {
        Date oldValue = this.valuta;
        this.valuta = valuta;
        firePropertyChange(PROP_VALUTA, oldValue, this.valuta);
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

}
