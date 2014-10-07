/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-03-22 15:02:48 +0100 (Di, 22 Mrz 2011) $
 *   $Author: tom $
 *   $Revision: 2036 $ 
 */
package ch.tkayser.budget.dto;

import java.math.BigDecimal;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import ch.tkayser.budget.test.BudgetBaseTest;
import ch.tkayser.budget.util.BudgetUtil;
import ch.tkayser.budget.util.ObjectCloner;

/**
 * TODO: Bitte durch Klassenbeschreibung ersetzen
 */
public class CloningTest extends BudgetBaseTest{ 
    
    @Test
    public void cloneAccountDTO() throws CloneNotSupportedException {
        AccountDTO ac = new AccountDTO();
        ac.setId(1L);
        ac.setName("parent");
        ac.setVersion(new Integer(1));
        
        AccountDTO acChild = new AccountDTO();
        acChild.setId(2L);
        acChild.setName("child");
        acChild.setVersion(new Integer(1));
        acChild.setParent(ac);
        
        AccountDTO clone = (AccountDTO)ObjectCloner.deepCopy(ac);
        Assert.assertEquals(clone.getName(), ac.getName());
        Assert.assertEquals(clone.getId(), ac.getId());
        Assert.assertEquals(clone.getVersion(), ac.getVersion());
        Assert.assertEquals(clone.getChildren().size(), ac.getChildren().size());
        Assert.assertEquals(clone.getChildren().get(0).getId(), ac.getChildren().get(0).getId());

        
        clone = (AccountDTO)ObjectCloner.deepCopy(acChild);
        Assert.assertEquals(clone.getName(), acChild.getName());
        Assert.assertEquals(clone.getChildren().size(), acChild.getChildren().size());       
        Assert.assertEquals(clone.getParent().getId(), acChild.getParent().getId());
        
    }
    
    @Test
    public void cloneBudgetDTO() throws CloneNotSupportedException {
        BudgetDTO budget = new BudgetDTO();
        budget.setId(1L);
        budget.setName("parent");
        budget.setVersion(new Integer(1));
        budget.setAmountPerMonth(BudgetUtil.setScale(new BigDecimal(3500)));
        budget.setMonitorDate(new Date());
                
        BudgetDTO clone = (BudgetDTO)ObjectCloner.deepCopy(budget);
        Assert.assertEquals(clone.getId(), budget.getId());
        Assert.assertEquals(clone.getVersion(), budget.getVersion());
        Assert.assertEquals(clone.getName(), budget.getName());
        Assert.assertEquals(clone.getAmountPerMonth(), budget.getAmountPerMonth());
        Assert.assertEquals(clone.getAmountPerDay(), budget.getAmountPerDay());
        Assert.assertEquals(clone.getAmountRemaining(), budget.getAmountRemaining());
        Assert.assertEquals(clone.getAmountUpToDay(), budget.getAmountUpToDay());
        Assert.assertEquals(clone.getMonitorDate(), budget.getMonitorDate());
    }

    @Test
    public void cloneTransaction() throws CloneNotSupportedException {
        AccountDTO ac = new AccountDTO();
        ac.setId(1L);
        ac.setName("parent");
        ac.setVersion(new Integer(1));
        
        TransactionDTO tx = new TransactionDTO();
        tx.setAccount(ac);
        tx.setAmount(BudgetUtil.setScale(new BigDecimal(35.35)));
        tx.setBookingText("text");
        tx.setReciever("rec");
        tx.setSender("sender");
        tx.setValuta(new Date());
        tx.setId(1L);
        tx.setVersion(new Integer(2));

        TransactionDTO clone = (TransactionDTO)ObjectCloner.deepCopy(tx);
        Assert.assertEquals(clone.getId(), tx.getId());
        Assert.assertEquals(clone.getValuta(), tx.getValuta());
        Assert.assertEquals(clone.getVersion(), tx.getVersion());
        Assert.assertEquals(clone.getAccount().getId(), tx.getAccount().getId());
        Assert.assertEquals(clone.getAmount(), tx.getAmount());
        Assert.assertEquals(clone.getBookingText(), tx.getBookingText());
        Assert.assertEquals(clone.getSender(), tx.getSender());
        Assert.assertEquals(clone.getReciever(), tx.getReciever());
    }
}
