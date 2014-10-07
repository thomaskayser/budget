package ch.tkayser.budget.parser;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.parser.impl.Constants;
import ch.tkayser.budget.parser.impl.UBSParserV2;

public class UBSParserV2Test extends ParserTestBase {

    private static final String UBS_TESTFILE = "/parser/ubs_test_v2.csv";

    @Test
    public void testParser() throws Exception {

        // create parser and parse test file
        UBSParserV2 parser = new UBSParserV2();
        List<TransactionDTO> parsedTransaction = parser.parserTransaction(getClass().getResource(UBS_TESTFILE)
                .openStream());

        // check the tx
        Assert.assertNotNull(parsedTransaction);
        Assert.assertEquals(3, parsedTransaction.size());

        // tx 1
        TransactionDTO tx = parsedTransaction.get(0);
        Assert.assertNull(tx.getAccount());
        Assert.assertEquals(parseDate("01.04.2011"), tx.getValuta());
        Assert.assertEquals(new BigDecimal("-1500"), tx.getAmount());
        Assert.assertEquals("Tom", tx.getSender());
        Assert.assertNull(tx.getReciever());
        Assert.assertEquals("VERGUETUNG" + Constants.BOOKINGTEXT_LINE_SEPARATOR + "UEBERTRAG"
                + Constants.BOOKINGTEXT_LINE_SEPARATOR + "CH7200235235902167591, 9935091TI4807885", tx.getBookingText());

        // tx 2
        tx = parsedTransaction.get(1);
        Assert.assertNull(tx.getAccount());
        Assert.assertEquals(parseDate("02.04.2011"), tx.getValuta());
        Assert.assertEquals(new BigDecimal("38.9"), tx.getAmount());
        Assert.assertEquals("Gordana", tx.getReciever());
        Assert.assertNull(tx.getSender());
        Assert.assertEquals("E-BANKING-AUFTRAG" + Constants.BOOKINGTEXT_LINE_SEPARATOR + "V PLUS AG"
                + Constants.BOOKINGTEXT_LINE_SEPARATOR + "8620 WETZIKON", tx.getBookingText());


        // tx 3
        tx = parsedTransaction.get(2);
        Assert.assertNull(tx.getAccount());
        Assert.assertEquals(parseDate("03.04.2011"), tx.getValuta());
        Assert.assertEquals(new BigDecimal("450.99"), tx.getAmount());
        Assert.assertNull(tx.getReciever());
        Assert.assertNull(tx.getSender());
        Assert.assertEquals("Test" + Constants.BOOKINGTEXT_LINE_SEPARATOR + "Auftrag", tx.getBookingText());        

    }

}
