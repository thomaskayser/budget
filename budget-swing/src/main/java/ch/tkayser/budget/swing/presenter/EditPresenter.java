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
import ch.tkayser.budget.swing.validation.BudgetValidator;

import com.jgoodies.binding.PresentationModel;

/**
 * Interface fuer einen Presenter mit Edit Funktionalität
 */
public interface EditPresenter<T extends BaseDTO> {

    /**
     * create a new bean
     */
    public void create();

    /**
     * delete a bean
     * 
     * @param bean
     */
    public void delete(T bean);

    /**
     * edit a bean
     * 
     * @param bean
     */
    public void edit(T bean);

    /**
     * save a bean
     * 
     * @param bean
     */
    public void save(T bean);

    /**
     * liefert das model zum editieren zurueck
     * 
     * @return
     */
    public PresentationModel<T> getEditModel();

    /**
     * liefert einen mit dem editierModel verknuepften Validator zurueck
     * 
     * @return
     */
    public BudgetValidator<T> getEditValidator();

}
