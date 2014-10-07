/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-04-16 20:13:02 +0200 (Sa, 16 Apr 2011) $
 *   $Author: tom $
 *   $Revision: 2102 $ 
 */
package ch.tkayser.budget.swing.presenter;

import javax.swing.Action;

import org.jdesktop.application.Application;
import org.jdesktop.application.Task;

import ch.tkayser.budget.service.BudgetService;
import ch.tkayser.budget.swing.MainApplication;
import ch.tkayser.budget.swing.MainPresenter;

/**
 * Abstrakte Superklasse fuer die Presenter
 */
public abstract class AbstractBudgetPresenter {

    // @formatter:off
    // the main presenter
    private MainPresenter mainPresenter;

    // @formatter:on

    public AbstractBudgetPresenter(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;
    }

    /**
     * execute a task
     * 
     * @param task
     */
    protected void executeTask(Task<?, ?> task) {
        MainApplication.getInstance().getContext().getTaskService().execute(task);

    }

    /**
     * get an Action
     * 
     * @param actionName
     * @return
     */
    public Action getAction(String actionName) {
        return Application.getInstance(MainApplication.class).getContext().getActionMap(getClass(), this)
                .get(actionName);
    }

    /**
     * @return the mainPresenter
     */
    protected MainPresenter getMainPresenter() {
        return mainPresenter;
    }

    /**
     * get the budget service
     * 
     * @return
     */
    protected BudgetService getService() {
        return MainApplication.getService();
    }

}
