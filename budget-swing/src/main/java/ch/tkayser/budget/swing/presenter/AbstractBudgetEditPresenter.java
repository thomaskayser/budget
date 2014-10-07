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

import ch.tkayser.budget.base.dto.BaseDTO;
import ch.tkayser.budget.swing.MainPresenter;
import ch.tkayser.budget.swing.widgets.AbstractBudgetDialog;
import ch.tkayser.budget.util.ObjectCloner;

/**
 * Abstrakte Superklasse fuer die Presenter zum Bearbeiten
 */
public abstract class AbstractBudgetEditPresenter<T extends BaseDTO> extends AbstractBudgetPresenter implements
        EditPresenter<T> {

    /**
     * @param mainPresenter
     */
    public AbstractBudgetEditPresenter(MainPresenter mainPresenter) {
        super(mainPresenter);
    }

    /**
     * return the edit Dialog
     */
    public abstract AbstractBudgetDialog<T> getEditDialog();

    /**
     * Eine neue Instanz der Bean Klasse erstellen zum erfassen
     * 
     * @return
     */
    protected abstract T getNewBeanInstance();

    /**
     * Ueberschreibt Methode von Superklasse {@see
     * ch.tkayser.budget.swing.presenter.Presenter#create()}
     * 
     */
    @Override
    public void create() {
        showEditor(getNewBeanInstance());
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see
     * ch.tkayser.budget.swing.presenter.Presenter#edit(ch.tkayser.budget.base.
     * dto.BaseDTO)}
     * 
     * @param bean
     */
    @SuppressWarnings("unchecked")
    @Override
    public void edit(T bean) {
        showEditor((T) ObjectCloner.deepCopy(bean));
    }

    /**
     * Bean im Editor anzeigen
     * 
     * @param account
     * 
     */
    private void showEditor(T bean) {
        // bean im modell setzen
        getEditModel().setBean(bean);
        // dialog anzeigen
        getEditDialog().displayDialog();

    }

}
