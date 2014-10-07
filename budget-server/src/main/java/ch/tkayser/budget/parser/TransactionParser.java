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
package ch.tkayser.budget.parser;

import java.io.InputStream;
import java.util.List;

import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.exception.BudgetException;

public interface TransactionParser {

    /**
     * parse transactions from a input stream
     * 
     * @param f
     * @return
     */
    public List<TransactionDTO> parserTransaction(InputStream input) throws BudgetException;

    /**
     * get the name of the parser
     * 
     * @return
     */
    public String getParserName();

}
