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
package ch.tkayser.budget.parser.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ParserHelper {

    // a logger
    private static Logger s_log = LoggerFactory.getLogger(ParserHelper.class);

    // dateformat
    private static SimpleDateFormat m_Datefmt = new SimpleDateFormat();

    // decimal format
    private static DecimalFormat m_amountFmt = new DecimalFormat(Constants.DEFAULT_AMOUNT_FORMAT,
	    new DecimalFormatSymbols(new Locale("de", "ch")));

    /**
     * create a booking text from strings
     * 
     * @param text
     * @return
     */
    public static String createBookingText(String... text) {
	StringBuilder out = new StringBuilder();
	for (int i = 0; i < text.length; i++) {
	    if (!text[i].equals("")) {
		// add separator
		if (out.length() > 0) {
		    out.append(Constants.BOOKINGTEXT_LINE_SEPARATOR);
		}
		// add text
		out.append(text[i]);
	    }
	}
	return out.toString();
    }

    /**
     * parse an amount in the default format
     * 
     * @param amount
     * @return
     */
    public static BigDecimal parseAmount(String amount) {
	return parseAmount(Constants.DEFAULT_AMOUNT_FORMAT, amount);
    }

    /**
     * parse an amount
     * 
     * @param format
     * @param amount
     * @return
     */
    public static BigDecimal parseAmount(String format, String amount) {
	try {
	    // check empty string
	    if (amount.equals("")) {
		return null;
	    }
	    m_amountFmt.applyPattern(format);
	    return new BigDecimal(m_amountFmt.parse(amount).toString());

	} catch (ParseException e) {
	    s_log.error("Error parsing the amount " + amount + " with the format " + format, e);
	    return null;
	}
    }

    /**
     * parse a date with the default dateformat
     * 
     * @param date
     * @return
     */
    public static Date parseDate(String date) {
	return parseDate(Constants.DEFAULT_DATE_FORMAT, date);
    }

    /**
     * parse a date
     * 
     * @param dateFormat
     * @param date
     * @return the parsed date or null if the date was not parseable
     */
    public static Date parseDate(String dateFormat, String date) {
	try {
	    m_Datefmt.applyPattern(dateFormat);
	    return m_Datefmt.parse(date);
	} catch (ParseException e) {
	    s_log.error("Error parsing the date " + date + " with the format " + dateFormat, e);
	    return null;
	}
    }

}
