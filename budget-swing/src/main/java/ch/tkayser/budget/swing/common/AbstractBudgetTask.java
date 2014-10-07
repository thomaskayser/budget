/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-04-14 16:26:43 +0200 (Do, 14 Apr 2011) $
 *   $Author: tom $
 *   $Revision: 2100 $ 
 */
package ch.tkayser.budget.swing.common;

import org.jdesktop.application.Task;

import ch.tkayser.budget.swing.Constants;
import ch.tkayser.budget.swing.MainApplication;

/**
 * Abstrakte Superklasse fuer Tasks.
 */
public abstract class AbstractBudgetTask<T, V> extends Task<T, V> {

    /** 
     * 
     *
     * @param application
     */
    public AbstractBudgetTask() {
        super(MainApplication.getInstance());        
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see org.jdesktop.application.Task#failed(java.lang.Throwable)}
     *
     * @param cause
     */
    protected void failed(Throwable cause) {
        String key = Constants.ERROR_TASKERROR;
        message(key);
        // uebersetzen fuer dialog
        String message = key;
        if (getResourceMap() != null) {
            message = getResourceMap().getString(key);
        }
        ExceptionDialog.showDialg(message, cause);
    }


}
