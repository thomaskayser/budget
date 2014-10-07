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
package ch.tkayser.budget.domain;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.Test;

import ch.tkayser.budget.test.BudgetBaseTest;

/**
 * @author tom
 * 
 */
public class AccountBalancesTest extends BudgetBaseTest {

    @Test
    public void addTimeGroupBalances() {
        
        AccountBalances b1 = new AccountBalances();
        b1.getTimeGroupBalances().add(new BigDecimal("155"));
        b1.getTimeGroupBalances().add(new BigDecimal("0"));
        b1.getTimeGroupBalances().add(new BigDecimal("25.55"));

        AccountBalances b2 = new AccountBalances();
        b2.getTimeGroupBalances().add(new BigDecimal("464.55"));
        b2.getTimeGroupBalances().add(new BigDecimal("456.53"));
        b2.getTimeGroupBalances().add(new BigDecimal("0"));

        b1.addTimeGroupBalances(b2);
        
        // check
        Assert.assertEquals(new BigDecimal("619.55"), b1.getTimeGroupBalances().get(0));
        Assert.assertEquals(new BigDecimal("456.53"), b1.getTimeGroupBalances().get(1));
        Assert.assertEquals(new BigDecimal("25.55"), b1.getTimeGroupBalances().get(2));
        
    }

}
