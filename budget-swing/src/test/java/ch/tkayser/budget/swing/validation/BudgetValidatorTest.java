/*
 * Software is written by:
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2011
 * 
 */
package ch.tkayser.budget.swing.validation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.tkayser.budget.dto.BudgetDTO;
import ch.tkayser.budget.swing.test.SwingBaseTest;
import ch.tkayser.budget.util.BudgetUtil;

import com.jgoodies.binding.PresentationModel;

/**
 * @author tom
 * 
 */
public class BudgetValidatorTest extends SwingBaseTest  {

    // formatter:off
    // a validator    
    private BudgetValidator<BudgetDTO> validator;
    private BudgetDTO budget;
    private PresentationModel<BudgetDTO> model;
    private ViolationListener listener;
    private String attr1 = "attribute1";
    private String attr2 = "attribute2";
    // formatter:on



    
    @Before
    public void createObjects() {
        // init validator / listener
        budget = new BudgetDTO();
        budget.setName("test");
        budget.setAmountPerMonth(new BigDecimal(1));
        model = new PresentationModel<BudgetDTO>(budget);
        validator = new BudgetValidator<BudgetDTO>(model);
        listener = new ViolationListener();
        validator.addPropertyChangeListener(listener);

    }
    
    private void expectAttriubteViolationsEvent(String attributeName) {
        Assert.assertTrue(listener.getEvents(attributeName).size() == 1);
    }
    

    private void expectValidationOk() {
        Assert.assertTrue(validator.getValidationOk());
    }

    private void expectValidationOkEvent() {
        Assert.assertTrue(listener.getEvents(BudgetValidator.PROPERTY_VALIDATION_OK).size() == 1);        
    }

    private void expectValidationWrong() {
        Assert.assertFalse(validator.getValidationOk());
    }

    private void expectViolationsEvent() {
        Assert.assertTrue(listener.getEvents(BudgetValidator.PROPERTY_VIOLATIONS).size() == 1);
    }

    
    @Test
    public void setInputViolation() {

        // no validations
        Assert.assertTrue(validator.getValidationOk());

        // add single violation attr1
        validator.setInputViolation(attr1, "Test");
        
        // assert results
        expectValidationWrong();
        expectValidationOkEvent();
        expectViolationsEvent();
        expectAttriubteViolationsEvent(attr1);        

    }

    @Test
    public void setInputViolations() {

        // list of violations attr2
        List<String> vioaltions = Arrays.asList("error1", "error2");
        validator.setInputViolations(attr2, vioaltions);
        
        // assert results
        expectValidationWrong();
        expectValidationOkEvent();
        expectViolationsEvent();
        expectAttriubteViolationsEvent(attr2);

    }
    
    @Test
    public void clearInputViolation() {

        // add single violation attr1
        validator.setInputViolation(attr1, "Test");
        Assert.assertFalse(validator.getValidationOk());
        listener.clearEvents();

        // clear attr1
        validator.clearInputViolations(attr1);
        
        // assert results
        expectValidationOk();
        expectValidationOkEvent();
        expectViolationsEvent();
        expectAttriubteViolationsEvent(attr1);
    }    

    @Test
    public void setInputViolationsWithEmptyList() {

        // add single violation attr1
        validator.setInputViolation(attr1, "Test");
        Assert.assertFalse(validator.getValidationOk());
        listener.clearEvents();

        // clear attr1 (with empty list)
        listener.clearEvents();        
        List<String> violations = new ArrayList<String>();
        validator.setInputViolations(attr1, violations);  

        // assert results
        expectValidationOk();
        expectValidationOkEvent();
        expectViolationsEvent();
        expectAttriubteViolationsEvent(attr1);        
    }

    @Test
    public void setInputViolationsWithNull() {

        // add single violation attr1
        validator.setInputViolation(attr2, "Test");
        Assert.assertFalse(validator.getValidationOk());
        listener.clearEvents();

        // clear attr2 (with null as list)
        validator.setInputViolation(attr2, null);

        // assert results
        expectValidationOk();
        expectValidationOkEvent();
        expectViolationsEvent();
        expectAttriubteViolationsEvent(attr2);       
    }
    
    @Test
    public void setInputViolationWithNull() {
        // add single violation attr1
        validator.setInputViolation(attr2, "Test");
        Assert.assertFalse(validator.getValidationOk());
        listener.clearEvents();

        // clear attr2 (with null argument)
        validator.setInputViolation(attr2, null);
        
        // assert results
        expectValidationOk();
        expectValidationOkEvent();
        expectViolationsEvent();
        expectAttriubteViolationsEvent(attr2);       
    }
    
    @Test
    public void setMultipleAttributInputViolations() {
        Assert.assertTrue(validator.getValidationOk());
        
        // add violation for one attribute
        listener.clearEvents();
        validator.setInputViolation(attr1, "test");
        
        // assert results
        expectValidationWrong();
        expectValidationOkEvent();
        expectViolationsEvent();
        expectAttriubteViolationsEvent(attr1);       

        // add violation for another attribute
        listener.clearEvents();
        validator.setInputViolation(attr2, "test");

        // assert results
        expectValidationWrong();
        expectViolationsEvent();
        expectAttriubteViolationsEvent(attr2);       

        // remove violation for first attribute
        listener.clearEvents();
        validator.clearInputViolations(attr1);

        // assert results
        expectValidationWrong();
        expectViolationsEvent();
        expectAttriubteViolationsEvent(attr1);       

        // remove violations for second attribute
        listener.clearEvents();
        validator.clearInputViolations(attr2);

        // assert results
        expectValidationOk();
        expectValidationOkEvent();
        expectViolationsEvent();
        expectAttriubteViolationsEvent(attr2);       
    }
    
    @Test
    public void validateBean() {
        // setup validator
        PresentationModel<BudgetDTO> model = new PresentationModel<BudgetDTO>(new BudgetDTO());
        validator = new BudgetValidator<BudgetDTO>(model);
        validator.addPropertyChangeListener(listener);
        
        // check initial validation from creation
        Assert.assertFalse(validator.getValidationOk());
        
        // Budget with no values set
        BudgetDTO budget = new BudgetDTO();
        model.setBean(budget);
        listener.clearEvents();
        Assert.assertFalse(validator.getValidationOk());
        Assert.assertEquals(2, validator.getViolations().size());
        for (String violation: validator.getViolations()) {
            logger.debug(violation);            
        }
        
        // set the name
        listener.clearEvents();
        budget.setName("Test");
        // events for both properties
        Assert.assertTrue(listener.getEvents(BudgetDTO.PROP_NAME).size() > 0);
        Assert.assertTrue(listener.getEvents(BudgetDTO.PROP_AMOUNT_PER_MONTH).size() > 0);
        // still invalid, with one violation
        Assert.assertFalse(validator.getValidationOk());
        Assert.assertEquals(1, validator.getViolations().size());
        
        // set amount (now its valid)
        budget.setAmountPerMonth(new BigDecimal("1500"));
        // events for one properties
        Assert.assertTrue(listener.getEvents(BudgetDTO.PROP_AMOUNT_PER_MONTH).size() > 0);
        Assert.assertTrue(validator.getValidationOk());
        Assert.assertEquals(0, validator.getViolations().size());

    }
    
    @Test
    public void violations() {
        listener.clearEvents();
        
        // set empty Budget;
        BudgetDTO newBudget = new BudgetDTO();
        model.setBean(newBudget);
        // validation is wrong and violations have changed
        Assert.assertFalse(validator.getValidationOk());
        Assert.assertTrue(listener.getEvents(BudgetValidator.PROPERTY_VIOLATIONS).size() > 0);
        
        // set name (violations must have changed)
        listener.clearEvents();
        newBudget.setName("changed");
        Assert.assertTrue(listener.getEvents(BudgetValidator.PROPERTY_VIOLATIONS).size() > 0);
        
        // set amount (violations must have changed)
        listener.clearEvents();
        newBudget.setAmountPerMonth(BudgetUtil.setScale(new BigDecimal(15)));
        Assert.assertTrue(listener.getEvents(BudgetValidator.PROPERTY_VIOLATIONS).size() > 0);
        
        // set input violation (violations must have changed)
        listener.clearEvents();
        validator.setInputViolation(attr1, "Test");
        Assert.assertTrue(listener.getEvents(BudgetValidator.PROPERTY_VIOLATIONS).size() > 0);

    }

}
