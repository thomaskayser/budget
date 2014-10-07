package ch.tkayser.budget.parser;

import org.junit.Test;

public class ParserFactoryTest extends ParserTestBase {
    
    
    @Test
    public void testFactory() {
	for (TransactionParser parser: ParserFactory.getAvailableParsers()) {
	    m_log.debug("Parser found: {}", parser.getParserName());
	    
	}
    }

}
