/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-04-05 08:28:25 +0200 (Di, 05 Apr 2011) $
 *   $Author: tom $
 *   $Revision: 2058 $ 
 */
package ch.tkayser.budget.swing.validation;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import ch.tkayser.budget.dto.BudgetDTO;
import ch.tkayser.budget.swing.test.SwingBaseTest;

/**
 * Test fuer den BudgetValidator
 */
public class ValdiatorTest extends SwingBaseTest {
    
    
    private Validator validator;
    
    @Before
    public void setUpValidator() {
        // create a validator
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    
    @Test
    public void testValidation() {
        BudgetDTO budget =new BudgetDTO();
        Set<ConstraintViolation<BudgetDTO>> violations = validator.validate(budget);
        for (ConstraintViolation<BudgetDTO> violation: violations) {
            System.out.println(violation.getMessage());
            System.out.println(violation.getMessageTemplate());
            Path propertyPath = violation.getPropertyPath();
            for (Iterator<Node> it = propertyPath.iterator(); it.hasNext();) {
                System.out.println(it.next().getName());
            }
            
        }
        
        budget.setName("test");
    }

}
