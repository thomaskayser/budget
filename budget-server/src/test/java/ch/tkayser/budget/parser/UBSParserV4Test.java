package ch.tkayser.budget.parser;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.parser.impl.Constants;
import ch.tkayser.budget.parser.impl.UBSParserV4;

public class UBSParserV4Test extends ParserTestBase {

    private static final String UBS_TESTFILE = "/parser/ubs_test_v4.csv";

    @Test
    public void testParser() throws Exception {

        // create parser and parse test file
        UBSParserV4 parser = new UBSParserV4();
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
        Assert.assertEquals(parseDate("31.10.2011"), tx.getValuta());
        Assert.assertEquals(new BigDecimal("2011"), tx.getAmount());
        Assert.assertEquals("DAUERAUFTRAG"+Constants.BOOKINGTEXT_LINE_SEPARATOR+"SWISSCANTO"+ Constants.BOOKINGTEXT_LINE_SEPARATOR+"8001 ZUERICH", tx.getBookingText());

        // tx 2
        tx = parsedTransaction.get(1);
        Assert.assertNull(tx.getAccount());
        Assert.assertEquals(parseDate("01.11.2011"), tx.getValuta());
        Assert.assertEquals(new BigDecimal("523.05"), tx.getAmount());
        Assert.assertNull(tx.getReciever());
        Assert.assertNull(tx.getSender());
        Assert.assertEquals("LASTSCHRIFT" + Constants.BOOKINGTEXT_LINE_SEPARATOR + "KPT1W WIDERSPRUCH AN UBS"
                + Constants.BOOKINGTEXT_LINE_SEPARATOR + "INNERT 30 TAGEN, KRANKENKASSE KPT, 3000 BERN 22, PRAEMIENRECHNUNG, 11 2011", tx.getBookingText());

        // tx 3
        tx = parsedTransaction.get(2);
        Assert.assertNull(tx.getAccount());
        Assert.assertEquals(parseDate("19.10.2011"), tx.getValuta());
        Assert.assertEquals(new BigDecimal("-3100"), tx.getAmount());
        Assert.assertNull(tx.getReciever());
        Assert.assertNull(tx.getSender());
        Assert.assertEquals("VERGUETUNG" + Constants.BOOKINGTEXT_LINE_SEPARATOR + "ANEA GMBH"
                + Constants.BOOKINGTEXT_LINE_SEPARATOR + "BANNLISTRASSE 17, 4533 RIEDHOLZ, LONH", tx.getBookingText());

    }

}
