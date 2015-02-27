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

import java.util.ArrayList;
import java.util.List;

import ch.tkayser.budget.parser.impl.*;
import ch.tkayser.budget.parser.impl.mt940.BankCoopParser;



public class ParserFactory {

    
    // the registered parsers
    private static List<TransactionParser> m_parsers;
    static {
	m_parsers = new ArrayList<TransactionParser>();
        m_parsers.add(new BankCoopParser());
        m_parsers.add(new VisecaCCParser());
        m_parsers.add(new UBSParserV4());
        m_parsers.add(new UBSParserV3());
        m_parsers.add(new UBSParserV2());
	m_parsers.add(new UBSParserV1());
    }
    
    
    /**
     * get a list with all available parsers
     * 
     * @return
     */
    public static List<TransactionParser> getAvailableParsers() {
	return m_parsers;	
    }

}
