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
package ch.tkayser.budget.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import ch.tkayser.budget.domain.Account;
import ch.tkayser.budget.domain.AccountBalanceForTimeGroup;
import ch.tkayser.budget.domain.BalanceSheet;
import ch.tkayser.budget.domain.Budget;
import ch.tkayser.budget.domain.Transaction;
import ch.tkayser.budget.dto.AccountDTO;
import ch.tkayser.budget.dto.BalanceSheetDTO;
import ch.tkayser.budget.dto.BalanceSheetRowDTO;
import ch.tkayser.budget.dto.BudgetDTO;
import ch.tkayser.budget.dto.TimeGroup;
import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.test.BudgetBaseTest;
import ch.tkayser.budget.test.DatenHelper;

public class BudgetServiceMappingTest extends BudgetBaseTest {

    /**
     * create a test account
     * 
     * @return the account
     */
    private Account createTestAccount() {
        Account parent = new Account();
        parent.setId(1L);
        parent.setName("Parent");
        parent.setVersion(1);
        Account child = new Account();
        child.setId(2L);
        child.setName("Child 1");
        child.setParent(parent);
        child.setVersion(1);
        child = new Account();
        child.setId(3L);
        child.setName("Child 2");
        child.setParent(parent);
        child.setVersion(1);

        return parent;

    }

    @Test
    public void testAccountMappings() {

        // create a test account with some child accounts
        Account parent = createTestAccount();

        // map to dto
        BudgetServiceImpl service = new BudgetServiceImpl();
        AccountDTO parentDTO = service.toAccountDTO(parent);

        // check
        Assert.assertEquals(parentDTO.getName(), parent.getName());
        Assert.assertEquals(parentDTO.getId(), parent.getId());
        Assert.assertEquals(parentDTO.getVersion(), parent.getVersion());
        Assert.assertEquals(2, parentDTO.getChildren().size());
        Assert.assertEquals(parentDTO.getChildren().get(0).getId(), parent.getChildren().get(0).getId());
        Assert.assertEquals(parentDTO.getChildren().get(0).getName(), parent.getChildren().get(0).getName());
        Assert.assertEquals(parentDTO.getChildren().get(0).getParent().getId(), parent.getChildren().get(0).getParent()
                .getId());
        Assert.assertEquals(parentDTO.getChildren().get(1).getId(), parent.getChildren().get(1).getId());
        Assert.assertEquals(parentDTO.getChildren().get(1).getName(), parent.getChildren().get(1).getName());
        Assert.assertEquals(parentDTO.getChildren().get(1).getParent().getId(), parent.getChildren().get(1).getParent()
                .getId());

        // map dto to domain object
        Account mappedBackParent = service.fromAccountDTO(parentDTO);

        // check
        Assert.assertNotSame(mappedBackParent, parent);
        Assert.assertEquals(mappedBackParent.getName(), parent.getName());
        Assert.assertEquals(mappedBackParent.getId(), parent.getId());
        Assert.assertEquals(mappedBackParent.getVersion(), parent.getVersion());
        Assert.assertEquals(mappedBackParent.getChildren().size(), parent.getChildren().size());
        Assert.assertEquals(mappedBackParent.getChildren().get(0).getId(), parent.getChildren().get(0).getId());
        Assert.assertEquals(mappedBackParent.getChildren().get(0).getName(), parent.getChildren().get(0).getName());
        Assert.assertEquals(mappedBackParent.getChildren().get(0).getParent().getId(), parent.getChildren().get(0)
                .getParent().getId());
        Assert.assertEquals(mappedBackParent.getChildren().get(1).getId(), parent.getChildren().get(1).getId());
        Assert.assertEquals(mappedBackParent.getChildren().get(1).getName(), parent.getChildren().get(1).getName());
        Assert.assertEquals(mappedBackParent.getChildren().get(1).getParent().getId(), parent.getChildren().get(1)
                .getParent().getId());

        // move an account to another parent
        Account ac1 = parent.getChildren().get(0);
        Account ac2 = parent.getChildren().get(1);
        ac2.setParent(ac1);

        parentDTO = service.toAccountDTO(parent);

        // check
        Assert.assertEquals(1, parentDTO.getChildren().size());
        Assert.assertEquals(ac1.getId(), parentDTO.getChildren().get(0).getId());
        Assert.assertEquals(parent.getChildren().get(0).getId(), parentDTO.getChildren().get(0).getId());
        Assert.assertEquals(1, parentDTO.getChildren().get(0).getChildren().size());
        Assert.assertEquals(ac2.getId(), parentDTO.getChildren().get(0).getChildren().get(0).getId());
        Assert.assertEquals(parent.getChildren().get(0).getChildren().get(0).getId(), parentDTO.getChildren().get(0)
                .getChildren().get(0).getId());

        // map back
        mappedBackParent = service.fromAccountDTO(parentDTO);
        Assert.assertEquals(1, parentDTO.getChildren().size());
        Assert.assertEquals(ac1.getId(), mappedBackParent.getChildren().get(0).getId());
        Assert.assertEquals(parent.getChildren().get(0).getId(), mappedBackParent.getChildren().get(0).getId());
        Assert.assertEquals(1, mappedBackParent.getChildren().get(0).getChildren().size());
        Assert.assertEquals(ac2.getId(), mappedBackParent.getChildren().get(0).getChildren().get(0).getId());
        Assert.assertEquals(parent.getChildren().get(0).getChildren().get(0).getId(), mappedBackParent.getChildren()
                .get(0).getChildren().get(0).getId());

        // move account back to parent
        AccountDTO acDTO1 = parentDTO.getChildren().get(0);
        AccountDTO acDTO2 = acDTO1.getChildren().get(0);
        acDTO2.setParent(parentDTO);

        // map back
        mappedBackParent = service.fromAccountDTO(parentDTO);
        Assert.assertEquals(2, parentDTO.getChildren().size());
        Assert.assertEquals(ac1.getId(), mappedBackParent.getChildren().get(0).getId());
        Assert.assertEquals(ac2.getId(), mappedBackParent.getChildren().get(1).getId());
        Assert.assertEquals(0, mappedBackParent.getChildren().get(0).getChildren().size());

    }

    @Test
    public void testTransactionMappings() {

        // create a test transaction
        Account ac1 = createTestAccount();
        Transaction t1 = new Transaction();
        t1.setAccount(ac1);
        t1.setAmount(new BigDecimal(155.75));
        t1.setBookingText("Test Booking");
        t1.setReciever("for them");
        t1.setSender("from dad");
        t1.setValuta(new Date());
        t1.setVersion(1);

        // map to a dto
        BudgetServiceImpl service = new BudgetServiceImpl();
        TransactionDTO txDTO = service.toTransactionDTO(t1);

        // check
        Assert.assertEquals(txDTO.getBookingText(), t1.getBookingText());
        Assert.assertEquals(txDTO.getReciever(), t1.getReciever());
        Assert.assertEquals(txDTO.getSender(), t1.getSender());
        Assert.assertEquals(txDTO.getAmount(), t1.getAmount());
        Assert.assertEquals(txDTO.getValuta(), t1.getValuta());
        Assert.assertEquals(txDTO.getAccount().getId(), t1.getAccount().getId());
        Assert.assertEquals(txDTO.getVersion(), t1.getVersion());

        // map back to domain object
        Transaction mappedBackTx = service.fromTransactionDTO(txDTO);

        // check
        Assert.assertNotSame(mappedBackTx, t1);
        Assert.assertEquals(mappedBackTx.getBookingText(), t1.getBookingText());
        Assert.assertEquals(mappedBackTx.getAmount(), t1.getAmount());
        Assert.assertEquals(mappedBackTx.getValuta(), t1.getValuta());
        Assert.assertEquals(mappedBackTx.getAccount().getId(), t1.getAccount().getId());
        Assert.assertEquals(mappedBackTx.getVersion(), t1.getVersion());

    }

    @Test
    public void testBudgetMappings() {

        // create a budget
        Budget budget = new Budget();
        budget.setName("budget");
        budget.setAmountPerMonth(new BigDecimal("3500"));
        budget.setVersion(1);

        // map to a dto
        BudgetServiceImpl service = new BudgetServiceImpl();
        BudgetDTO budgetDTO = service.toBudgetDTO(budget);

        // check
        Assert.assertEquals(budget.getName(), budgetDTO.getName());
        Assert.assertEquals(budget.getAmountPerMonth(), budgetDTO.getAmountPerMonth());
        Assert.assertEquals(budget.getVersion(), budgetDTO.getVersion());

        // map back to domain object
        Budget mappedBackBudget = service.fromBudgetDTO(budgetDTO);

        // check
        Assert.assertNotSame(mappedBackBudget, budget);
        Assert.assertEquals(budgetDTO.getName(), mappedBackBudget.getName());
        Assert.assertEquals(budgetDTO.getAmountPerMonth(), mappedBackBudget.getAmountPerMonth());

    }

    @Test
    public void testBalanceSheetMappings() {

        // convert a balancesheet to the dto
        // create test accounts
        DatenHelper helper = new DatenHelper();
        List<Account> accounts = helper.getTestAccountsForBalanceSheet();

        // create the balanceSheet
        Date from = parseDate("01.01.2008");
        Date to = parseDate("30.06.2008");
        BalanceSheet sheet = new BalanceSheet(accounts, from, to, TimeGroup.MONTH);
        int amount = 100;
        for (Account account : accounts) {
            for (int i = 1; i <= 6; i++) {
                sheet.addAccountBalance(new AccountBalanceForTimeGroup(account, new Integer(i), new BigDecimal(amount)
                        .setScale(2, RoundingMode.HALF_UP)));
            }
            amount += 100;
        }
        sheet.calculateMatrixColumns();

        // convert
        BalanceSheetDTO sheetDTO = new BudgetServiceImpl().toBalanceSheetDTO(sheet);

        // the accounts are ordered by name.
        // check accounts
        BalanceSheetRowDTO childRow = null;

        // Diverses (4): no children
        checkSheetRowDTO(sheet, sheetDTO.getRows().get(0), accounts.get(4));
        Assert.assertEquals(0, sheetDTO.getRows().get(0).getChildRows().size());

        // Wohnung (0): 2 direct children
        checkSheetRowDTO(sheet, sheetDTO.getRows().get(1), accounts.get(0));
        Assert.assertEquals(2, sheetDTO.getRows().get(1).getChildRows().size());
        // Heizkosten(1)
        childRow = sheetDTO.getRows().get(1).getChildRows().get(0);
        checkSheetRowDTO(sheet, childRow, accounts.get(1));
        // Miete(2): 1 direct children
        childRow = sheetDTO.getRows().get(1).getChildRows().get(1);
        checkSheetRowDTO(sheet, childRow, accounts.get(2));
        Assert.assertEquals(1, childRow.getChildRows().size());
        // Heizkosten(3)
        childRow = childRow.getChildRows().get(0);
        checkSheetRowDTO(sheet, childRow, accounts.get(3));

        // check total row
        BalanceSheetRowDTO row = sheetDTO.getRows().get(2);
        Assert.assertTrue(row.isTotalRow());
        List<BigDecimal> balances = sheet.getGroupTotals();
        List<BigDecimal> dtoBalances = row.getGroupTotals();
        for (int i = 0; i < balances.size(); i++) {
            Assert.assertEquals(balances.get(i).doubleValue(), dtoBalances.get(i).doubleValue());
        }
        Assert.assertEquals(sheet.getGrandTotal(), row.getRowTotal());
        Assert.assertEquals(sheet.getGrandAvg(), row.getRowAvg());

    }

    /**
     * check a sheet row DTO
     * 
     * @param sheet
     * @param row
     * @param rowAccount
     */
    private void checkSheetRowDTO(BalanceSheet sheet, BalanceSheetRowDTO row, Account rowAccount) {
        // check account title
        Assert.assertEquals(rowAccount.getName(), row.getTitle());
        // check groupBalances from row and the sheet
        List<BigDecimal> balances = sheet.getAccountBalances(rowAccount).getTimeGroupBalances();
        List<BigDecimal> dtoBalances = row.getGroupTotals();
        Assert.assertEquals(balances.size(), dtoBalances.size());
        for (int i = 0; i < balances.size(); i++) {
            Assert.assertEquals(balances.get(i), dtoBalances.get(i));
        }
        // check balance and avg of the row
        Assert.assertEquals(sheet.getAccountBalances(rowAccount).getBalance(), row.getRowTotal());
        Assert.assertEquals(sheet.getAccountBalances(rowAccount).getAvgBalance(), row.getRowAvg());
    }
}
