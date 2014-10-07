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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.exception.BudgetException;
import ch.tkayser.budget.parser.TransactionParser;
import ch.tkayser.budget.parser.impl.mt940.Mt940Transaction.Mt940Tag;

/**
 * @author Tom
 * 
 */
public class BankCoopParser implements TransactionParser {

    private static final String PARSER_NAME      = "Bank Coop - MT940";

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.tkayser.budget.parser.TransactionParser#parserTransaction(java.io.
     * InputStream)
     */
    @Override
    public List<TransactionDTO> parserTransaction(InputStream input) throws BudgetException {
        try {

            // all parsed tx
            List<Mt940Transaction> parsedTransactions = new ArrayList<Mt940Transaction>();

            // current Mt940 Tx
            Mt940Transaction tx = null;

            // current tag
            Mt940Tag tag = null;

            // File zeilenweise lesen
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = reader.readLine();
            while (line != null) {
                // neue TX?
                if (Mt940Transaction.isTxStart(line)) {
                    tx = new Mt940Transaction();
                    parsedTransactions.add(tx);
                }
                // neuer tag
                if (Mt940Transaction.isTagStart(line)) {
                    if (tx != null) {
                        tag = new Mt940Tag(line);
                        tx.addTag(tag);                        
                    }
                }
                // zeile hinzufuegen
                if (tag != null) {
                    tag.addLine(line);
                    
                }
                // next line
                line = reader.readLine();
            }
           
            
            List<TransactionDTO> result = new ArrayList<TransactionDTO>();
            for (Mt940Transaction parsedTx: parsedTransactions) {
                result.add(parsedTx.toTransactionDTO());
            }
            return result;
            
        } catch (Throwable e) {
            // warp exception
            throw new BudgetException("Fehler beim Parsen des BankCoop Files:ß", e);
        }
    }

    @Override
    public String getParserName() {
        return PARSER_NAME;
    }

}
