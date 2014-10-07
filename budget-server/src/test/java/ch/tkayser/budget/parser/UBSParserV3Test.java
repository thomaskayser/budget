package ch.tkayser.budget.parser;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.parser.impl.Constants;
import ch.tkayser.budget.parser.impl.UBSParserV3;

public class UBSParserV3Test extends ParserTestBase {

    private static final String UBS_TESTFILE = "/parser/ubs_test_v3.csv";

    @Test
    public void testParser() throws Exception {

        // create parser and parse test file
        UBSParserV3 parser = new UBSParserV3();
        List<TransactionDTO> parsedTransaction = parser.parserTransaction(getClass().getResource(UBS_TESTFILE)
                .openStream());

        // check the tx
        Assert.assertNotNull(parsedTransaction);
        Assert.assertEquals(3, parsedTransaction.size());

        // tx 1
        TransactionDTO tx = parsedTransaction.get(0);
        Assert.assertNull(tx.getAccount());
        Assert.assertNull(tx.getReciever());
        Assert.assertNull(tx.getSender());
        Assert.assertEquals(parseDate("29.04.2011"), tx.getValuta());
        Assert.assertEquals(new BigDecimal("2011"), tx.getAmount());
        Assert.assertEquals("DAUERAUFTRAG", tx.getBookingText());

        // tx 2
        tx = parsedTransaction.get(1);
        Assert.assertNull(tx.getAccount());
        Assert.assertEquals(parseDate("02.05.2011"), tx.getValuta());
        Assert.assertEquals(new BigDecimal("523.05"), tx.getAmount());
        Assert.assertNull(tx.getReciever());
        Assert.assertNull(tx.getSender());
        Assert.assertEquals("LASTSCHRIFT" + Constants.BOOKINGTEXT_LINE_SEPARATOR + "KPT1W WIDERSPRUCH AN UBS"
                + Constants.BOOKINGTEXT_LINE_SEPARATOR + "INNERT 30 TAGEN", tx.getBookingText());

        // tx 3
        tx = parsedTransaction.get(2);
        Assert.assertNull(tx.getAccount());
        Assert.assertEquals(parseDate("02.05.2011"), tx.getValuta());
        Assert.assertEquals(new BigDecimal("100"), tx.getAmount());
        Assert.assertNull(tx.getReciever());
        Assert.assertNull(tx.getSender());
        Assert.assertEquals("LASTSCHRIFT" + Constants.BOOKINGTEXT_LINE_SEPARATOR + "WOV1W WIDERSPRUCH AN UBS"
                + Constants.BOOKINGTEXT_LINE_SEPARATOR + "INNERT 30 TAGEN", tx.getBookingText());

    }

}
