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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import ch.tkayser.budget.base.dto.BaseDTO;

/**
 * DTO for a transfer transaction
 * 
 * @author isc-kat
 * 
 */
public class TransferTransactionDTO extends BaseDTO {

	private static final long serialVersionUID = 7198434239262455078L;

    //@formatter:off
    public static final String PROP_FROMACCOUNT    = "fromAccount";
    public static final String PROP_VALUTA         = "valuta";
    public static final String PROP_BOOKINGTEXT    = "bookingText";
    //@formatter:on
	
	// the source account
    @NotNull
	private AccountDTO fromAccount;
	
	// valuta for the whole transfer
    @NotNull    
	private Date valuta;
	
	// booking text for the source account
	private String bookingText;
	

	// the targets of the transfer
	private List<TransferTargetDTO> targets  = new ArrayList<TransferTargetDTO>();

	/**
	 * add a target
	 * @param target
	 */
	public void addTarget(TransferTargetDTO target) {
		targets.add(target);
	}

	/**
	 * @return the bookingText
	 */
	public String getBookingText() {
		return bookingText;
	}

	/**
	 * @return the fromAccount
	 */
	public AccountDTO getFromAccount() {
		return fromAccount;
	}

	/**
	 * get the target
	 * @return
	 */
	public List<TransferTargetDTO> getTargets() {
		return targets;
	}


	/**
	 * @return the valuta
	 */
	public Date getValuta() {
		return valuta;
	}

	/**
	 * @param bookingText
	 *            the bookingText to set
	 */
	public void setBookingText(String bookingText) {
	    this.bookingText = bookingText;
	}

	/**
	 * @param fromAccount
	 *            the fromAccount to set
	 */
	public void setFromAccount(AccountDTO fromAccount) {
	    this.fromAccount = fromAccount;
	}

	/**
	 * set the targets
	 * @param targets
	 */
	public void setTargets(List<TransferTargetDTO> targets) {
	    this.targets = targets;
	}

	/**
	 * @param valuta
	 *            the valuta to set
	 */
	public void setValuta(Date valuta) {
	    this.valuta = valuta;
	}

}
