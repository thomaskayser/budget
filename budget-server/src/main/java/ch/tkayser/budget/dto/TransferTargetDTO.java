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

import javax.validation.constraints.NotNull;

import ch.tkayser.budget.base.dto.BaseDTO;

public class TransferTargetDTO extends BaseDTO {

	private static final long serialVersionUID = -3157847769543800577L;

    //@formatter:off
    public static final String PROP_TOACCOUNT      = "toAccount";
    public static final String PROP_AMOUNT         = "amount";
    public static final String PROP_BOOKINGTEXT    = "bookingText";
    //@formatter:on
	
	// target account
    @NotNull
	private AccountDTO toAccount;
	// target amount
    @NotNull
	private BigDecimal amount;
	// target text
	private String bookingText;


	public AccountDTO getToAccount() {
		return toAccount;
	}

	public void setToAccount(AccountDTO toAccount) {
	    this.toAccount = toAccount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
	    this.amount = amount;
	}

	public String getBookingText() {
		return bookingText;
	}

	public void setBookingText(String bookingText) {
	    this.bookingText = bookingText;
	}

}
