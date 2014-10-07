package ch.tkayser.budget.dto;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.Test;

import ch.tkayser.budget.dto.BudgetDTO;
import ch.tkayser.budget.exception.BudgetException;
import ch.tkayser.budget.test.BudgetBaseTest;

/**
 * 
 */

/**
 * @author isc-kat
 * 
 */
public class BudgetDTOTest extends BudgetBaseTest {

	@Test
	public void testRecalculateBudget() throws BudgetException {
		
		BudgetDTO bu = new BudgetDTO();
		
		// calculate with null amountPerMonth;
		bu.setAmountPerMonth(null);
                Assert.assertEquals(toBigDecimal("0"), bu.getAmountPerDay());
                Assert.assertEquals(toBigDecimal("0"), bu.getAmountUpToDay());
                Assert.assertEquals(toBigDecimal("0"), bu.getAmountRemaining());

                // amount 6000
		bu.setAmountPerMonth(new BigDecimal("6000"));
		
		
		// calculate values for 25.11
		bu.setMonitorDate(parseDate("25.11.2009"));
		Assert.assertEquals(toBigDecimal("200"), bu.getAmountPerDay());
		Assert.assertEquals(toBigDecimal("5000"), bu.getAmountUpToDay());
		Assert.assertEquals(toBigDecimal("1000"), bu.getAmountRemaining());

		// calculate values for 30.11
		bu.setMonitorDate(parseDate("12.11.2009"));
		Assert.assertEquals(toBigDecimal("200"), bu.getAmountPerDay());
		Assert.assertEquals(toBigDecimal("2400"), bu.getAmountUpToDay());
		Assert.assertEquals(toBigDecimal("3600"), bu.getAmountRemaining());

		// calculate values for 1.11
		bu.setMonitorDate(parseDate("01.11.2009"));
		Assert.assertEquals(toBigDecimal("200"), bu.getAmountPerDay());
		Assert.assertEquals(toBigDecimal("200"), bu.getAmountUpToDay());
		Assert.assertEquals(toBigDecimal("5800"), bu.getAmountRemaining());

		// calculate values for 30.11
		bu.setMonitorDate(parseDate("30.11.2009"));
		Assert.assertEquals(toBigDecimal("200"), bu.getAmountPerDay());
		Assert.assertEquals(toBigDecimal("6000"), bu.getAmountUpToDay());
		Assert.assertEquals(toBigDecimal("0"), bu.getAmountRemaining());

	
	}
}
