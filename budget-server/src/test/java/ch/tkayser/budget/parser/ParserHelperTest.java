package ch.tkayser.budget.parser;

import java.math.BigDecimal;
import java.text.ParseException;

import junit.framework.Assert;

import org.junit.Test;

import ch.tkayser.budget.parser.impl.Constants;
import ch.tkayser.budget.parser.impl.ParserHelper;

public class ParserHelperTest extends ParserTestBase {

    
    @Test
    public void testDateParsing() {

	Assert.assertEquals(parseDate("01.01.2008"), ParserHelper
		.parseDate("01.01.2008"));
	Assert.assertEquals(parseDate("01.01.2008"), ParserHelper.parseDate(
		"dd-MM-yyyy", "01-01-2008"));

	// invalid date
	Assert.assertNull(ParserHelper.parseDate("balba"));

    }

    @Test
    public void testAmountParsing() throws ParseException {
	Assert.assertEquals(new BigDecimal("166.30").doubleValue(), ParserHelper.parseAmount(
		"166.3").doubleValue());
	Assert.assertEquals(new BigDecimal("4166.30").doubleValue(), ParserHelper.parseAmount(
		"4'166.3").doubleValue());
	Assert.assertEquals(new BigDecimal("4166.30").doubleValue(), ParserHelper.parseAmount(
		"4'166.3").doubleValue());

	// invalid amount
	Assert.assertNull(ParserHelper.parseAmount("balba"));

    }

    @Test
    public void testCreaetBookingText() {
	Assert.assertEquals("abc" + Constants.BOOKINGTEXT_LINE_SEPARATOR + "efg", ParserHelper
		.createBookingText("abc", "efg"));
	Assert.assertEquals("efg" + Constants.BOOKINGTEXT_LINE_SEPARATOR + "deg", ParserHelper
		.createBookingText("", "efg", "deg"));
	Assert.assertEquals("abc" + Constants.BOOKINGTEXT_LINE_SEPARATOR + "deg", ParserHelper
		.createBookingText("abc", "", "deg"));
	Assert.assertEquals("abc" + Constants.BOOKINGTEXT_LINE_SEPARATOR + "deg"
		+ Constants.BOOKINGTEXT_LINE_SEPARATOR + "kkk", ParserHelper.createBookingText(
		"abc", "", "deg", "", "kkk"));

    }
}
