package ch.tkayser.budget.parser;

import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.parser.impl.Constants;
import ch.tkayser.budget.parser.impl.UBSParserV1;
import ch.tkayser.budget.parser.impl.VisecaCCParser;
import junit.framework.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class VisecaCCParserTest extends ParserTestBase {

    private static final String TESTFILE = "/parser/viseca.pdf";

    @Test
    public void testParser() throws Exception {

        // create parser and parse test file
        VisecaCCParser parser = new VisecaCCParser();
        List<TransactionDTO> parsedTransaction = parser.parserTransaction(getClass().getResource(TESTFILE)
                .openStream());

        assertTrue(parsedTransaction.size() ==16);
        TransactionDTO tx = parsedTransaction.get(0);
        assertEquals(parseDate("26.01.2015"),tx.getValuta());
        assertEquals( "Hotel Lagant, Brand EUR 2'089.90", tx.getBookingText());
        assertNull(tx.getAccount());
        assertNull(tx.getReciever());
        assertNull(tx.getSender());
        assertNull(tx.getSender());

        tx = parsedTransaction.get(1);
        assertEquals( parseDate("27.01.2015"),tx.getValuta());
        assertEquals(new BigDecimal("11.9"),tx.getAmount());
        assertEquals("NETFLIX.COM, 0800 561 554", tx.getBookingText());
        assertNull(tx.getAccount());
        assertNull(tx.getReciever());
        assertNull(tx.getSender());
        assertNull(tx.getSender());


    }

}
