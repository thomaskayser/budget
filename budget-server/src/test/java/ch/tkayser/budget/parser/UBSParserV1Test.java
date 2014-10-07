package ch.tkayser.budget.parser;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.parser.impl.Constants;
import ch.tkayser.budget.parser.impl.UBSParserV1;

public class UBSParserV1Test extends ParserTestBase {

    private static final String UBS_TESTFILE = "/parser/ubs_test_v1.csv";

    @Test
    public void testParser() throws Exception {

        // create parser and parse test file
        UBSParserV1 parser = new UBSParserV1();
        List<TransactionDTO> parsedTransaction = parser.parserTransaction(getClass().getResource(UBS_TESTFILE)
                .openStream());

        // check the tx
        Assert.assertNotNull(parsedTransaction);
        Assert.assertEquals(3, parsedTransaction.size());

        // tx 1
        TransactionDTO tx = parsedTransaction.get(0);
        Assert.assertNull(tx.getAccount());
        Assert.assertEquals(parseDate("02.09.2009"), tx.getValuta());
        Assert.assertEquals(new BigDecimal("116.3"), tx.getAmount());
        Assert.assertNull(tx.getSender());
        Assert.assertEquals("CABLECOM GMBH 8021 ZUERICH", tx.getReciever());
        Assert.assertEquals("LASTSCHRIFT" + Constants.BOOKINGTEXT_LINE_SEPARATOR + "CAB8W WIDERSPRUCH AN UBS"
                + Constants.BOOKINGTEXT_LINE_SEPARATOR + "INNERT 30 TAGEN", tx.getBookingText());

        // tx 2
        tx = parsedTransaction.get(1);
        Assert.assertNull(tx.getAccount());
        Assert.assertEquals(parseDate("30.07.2009"), tx.getValuta());
        Assert.assertEquals(new BigDecimal("-3"), tx.getAmount());
        Assert.assertEquals("ANNETTE DROEFKE IM HOF 8A 8590 ROMANSHORN", tx.getSender());
        Assert.assertNull(tx.getReciever());
        Assert.assertEquals("VERGUETUNG", tx.getBookingText());

        // tx 3
        tx = parsedTransaction.get(2);
        Assert.assertEquals(parseDate("25.07.2009"), tx.getValuta());
        Assert.assertEquals(new BigDecimal("4715.92"), tx.getAmount());
        Assert.assertNull(tx.getReciever());
        Assert.assertNull(tx.getSender());
        Assert.assertEquals("BEZUG BANCOMAT" + Constants.BOOKINGTEXT_LINE_SEPARATOR + "KARTE 35666020-0 1012"
                + Constants.BOOKINGTEXT_LINE_SEPARATOR + "CS LU_KAUFMANNW_2", tx.getBookingText());
        Assert.assertNull(tx.getAccount());

    }

}
