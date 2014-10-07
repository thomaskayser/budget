/*
 * Software is written by:
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2012
 * 
 */
package ch.tkayser.budget.parser.impl.mt940;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.parser.impl.ParserHelper;

/**
 * @author Tom
 * 
 */
public class Mt940Transaction {

    // @formatter:off

    // parsed tags
    private static final String   TAG_START    = ":";
    private static final String   TAG_END      = ":";
    private static final String   TAG_TX_START = TAG_START + "61:";
    private static final String   TAG_DETAIL   = TAG_START + "86:";
    
    // known booking texts 
    private static final List<String> KNOWN_BOOKINGTEXTS;
    static
    {
    	KNOWN_BOOKINGTEXTS = new ArrayList<String>();
    	KNOWN_BOOKINGTEXTS.add("KONTO");
    	KNOWN_BOOKINGTEXTS.add("ZINSBELASTUNG");
    	KNOWN_BOOKINGTEXTS.add("GELDBEZUG");
    	KNOWN_BOOKINGTEXTS.add("GEBÜHREN");
    	KNOWN_BOOKINGTEXTS.add("DIENSTLEISTUNG");
    }

    // sign for debit/credit tx
    private static final Object   CREDIT = "C";

    // the parsed tags
    private Map<String, Mt940Tag> tags;
    
    // formats
    private static final String   VALUTA_FORMAT = "yyMMdd";
    private static final String   AMOUNT_FORMAT = "000000000000.00";

    // @formatter:on

    public void addTag(Mt940Tag tag) {
        if (tags == null) {
            tags = new HashMap<String, Mt940Tag>();
        }
        tags.put(tag.getTag(), tag);
    }

    public Map<String, Mt940Tag> getTags() {
        return tags;
    }

    public static boolean isTxStart(String line) {
        return line.startsWith(TAG_TX_START);
    }

    public static boolean isTagStart(String line) {
        return line.startsWith(TAG_START);
    }

    public TransactionDTO toTransactionDTO() throws ParseException {
        // tx parsen aus tags
        TransactionDTO tx = new TransactionDTO();
        Mt940Tag startTag = tags.get(TAG_TX_START);
        // Valuta
        String firstLine = startTag.getLines().get(0);
        String valuta = firstLine.substring(0, 6);
        tx.setValuta(ParserHelper.parseDate(VALUTA_FORMAT, valuta));

        // Betrag
        String amountTxt = firstLine.substring(11,26);
        amountTxt = amountTxt.replace(",", ".");
        BigDecimal amount = ParserHelper.parseAmount(AMOUNT_FORMAT, amountTxt);
        if (isGutschrift()) {
            amount = amount.multiply(new BigDecimal("-1"));       
        }
        tx.setAmount(amount);

        Mt940Tag detailTag = tags.get(TAG_DETAIL);       
        if (detailTag != null && detailTag.hasLines())  {
            if (isGutschrift()) {
            	// Der Detail Tag enthalet den Absender. Wir nehmen die 1. Zeile
            		tx.setSender(detailTag.getLines().get(0));        		
            	
            } else {
            	firstLine = detailTag.getLines().get(0);
            	if (isKnownBookingText(firstLine)) {
                	// bekannte buchungstexte, welche keinen Empfaenger enthalten
            		tx.setBookingText(firstLine);
            	} else {
            		// keine Bekannter text: empfaenger parsen
            		tx.setReciever(firstLine);
            	}
            }
        	
        }
        

        return tx;
    }
    
    private boolean isKnownBookingText(String line) {
    	for (String knownText: KNOWN_BOOKINGTEXTS) {
    		if (line.toUpperCase().startsWith(knownText)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    private boolean isGutschrift() {
        String firstLine = tags.get(TAG_TX_START).getLines().get(0);
        return firstLine.substring(10,11).equals(CREDIT);
    }

    /**
     * Tag in a M940 Record
     * 
     */
    public static class Mt940Tag {
        private String       tag;
        private List<String> lines;

        /**
         * @param tag
         */
        public Mt940Tag(String line) {
            this.tag = line.substring(0, line.indexOf(TAG_END, 2)+1);
        }

        /**
         * @return the tag
         */
        public String getTag() {
            return tag;
        }

        /**
         * @param tag
         *            the tag to set
         */
        public void setTag(String tag) {
            this.tag = tag;
        }

        /**
         * @return the rows
         */
        public List<String> getLines() {
            return lines;
        }

        /**
         * @param rows
         *            the rows to set
         */
        public void setLines(List<String> rows) {
            this.lines = rows;
        }

        public void addLine(String row) {
            if (lines == null) {
                lines = new ArrayList<String>();
            }
            // tag entfernen auf der 1. row
            if (row.startsWith(tag)) {
            	row = row.substring(tag.length());            	
            }
            lines.add(row);
        }
        
        public boolean hasLines() {
        	return lines != null && lines.size() > 0;
        }

    }
    


}
