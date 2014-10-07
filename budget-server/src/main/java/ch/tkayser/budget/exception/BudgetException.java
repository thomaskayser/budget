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
package ch.tkayser.budget.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class BudgetException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public BudgetException(String message, Throwable cause) {
        super(message, cause);
    }

    public BudgetException(String message) {
        super(message);
    }

}
