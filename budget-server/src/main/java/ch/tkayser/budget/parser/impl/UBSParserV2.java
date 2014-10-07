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

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.exception.BudgetException;
import ch.tkayser.budget.parser.TransactionParser;

import com.csvreader.CsvReader;

/**
 * @author isc-kat
 * 
 */
public class UBSParserV2 implements TransactionParser {

    // name of the parser
    private static String    PARSER_NAME        = "UBS_V2";

    // column constants
    private static final int COL_VALUTA         = 11;
    private static final int COL_AUFTRAGGEBER   = 12;
    private static final int COL_EMPFAENGER     = 13;

    private static final int COL_BESCHREIBUNG_1 = 14;
    private static final int COL_BESCHREIBUNG_2 = 15;
    private static final int COL_BESCHREIBUNG_3 = 16;
    private static final int COL_BELASTUNG      = 18;
    private static final int COL_GUTSCHRIFT     = 19;

    // number of columns
    private static final int COL_COUNT          = 21;

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.parser.TransactionParser#getParserName()
     */
    public String getParserName() {
        return PARSER_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.tkayser.budget.parser.TransactionParser#parserTransaction(java.io.
     * InputStream)
     */
    public List<TransactionDTO> parserTransaction(InputStream input) throws BudgetException {

        try {

            // Init result
            List<TransactionDTO> parsedTx = new ArrayList<TransactionDTO>();

            // read the file
            CsvReader reader = new CsvReader(input, Constants.COLUMM_DELIMITER,
                    Charset.forName(Constants.DEFAULT_CHARSET));
            reader.readHeaders();

            while (reader.readRecord()) {
                if (reader.getColumnCount() == COL_COUNT && !reader.get(COL_VALUTA).equals("")) {
                    TransactionDTO tx = new TransactionDTO();

                    // valuta
                    tx.setValuta(ParserHelper.parseDate(reader.get(COL_VALUTA)));


                    // sender
                    tx.setSender(reader.get(COL_AUFTRAGGEBER));
                    if (tx.getSender().equals("")) {
                        tx.setSender(null);
                    }

                    // reciever
                    tx.setReciever(reader.get(COL_EMPFAENGER));
                    if (tx.getReciever().equals("")) {
                        tx.setReciever(null);
                    }
                    
                    // amount (gutschrift oder belastung)
                    BigDecimal amount = ParserHelper.parseAmount(reader.get(COL_BELASTUNG));
                    if (amount == null) {
                        // gutschrift (als negativer betrag da es ein negativer aufwand ist)
                        amount = ParserHelper.parseAmount(reader.get(COL_GUTSCHRIFT));
                        amount = amount.multiply(new BigDecimal("-1"));
                    }
                    tx.setAmount(amount);
                    

                    // booking text from all text fields
                    tx.setBookingText(ParserHelper.createBookingText(reader.get(COL_BESCHREIBUNG_1),
                            reader.get(COL_BESCHREIBUNG_2), reader.get(COL_BESCHREIBUNG_3)));

                    parsedTx.add(tx);
                }
            }

            return parsedTx;

        } catch (Throwable e) {
            // warp exception
            throw new BudgetException("error.parsingubsfile", e);
        }
    }
}
