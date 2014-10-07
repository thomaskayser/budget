/*
 * Software is written by:
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2011
 * 
 */
package ch.tkayser.budget.swing.budgetmonitor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.SpinnerModel;

import ch.tkayser.budget.dto.BudgetDTO;
import ch.tkayser.budget.swing.MainPresenter;
import ch.tkayser.budget.swing.common.AbstractBudgetTask;
import ch.tkayser.budget.swing.presenter.AbstractBudgetEditPresenter;
import ch.tkayser.budget.swing.validation.BudgetValidator;
import ch.tkayser.budget.swing.widgets.AbstractBudgetDialog;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;

/**
 * @author tom
 * 
 */
public class BudgetMonitorPresenter extends AbstractBudgetEditPresenter<BudgetDTO>  {

    /**
     * Task zum loeschen eines Monitors
     */
    private class DeleteMonitorTask extends AbstractBudgetTask<Void, Void> {

        // the budget to save
        private final BudgetDTO monitor;

        public DeleteMonitorTask(BudgetDTO monitor) {
            this.monitor = monitor;
        }

        protected Void doInBackground() throws Exception {
            // delete the monitor
            getService().deleteBudget(monitor);
            // refresh the list
            refreshMonitors();
            
            return null;
        }

    }

    /**
     * Task zum speichern eines Monitors
     */
    private class SaveMonitorTask extends AbstractBudgetTask<Void, Void> {

        // the budget to save
        private final BudgetDTO monitor;

        public SaveMonitorTask(BudgetDTO monitor) {
            this.monitor = monitor;
        }

        protected Void doInBackground() throws Exception {
            // save the bean and refresh the nonitors
            getService().saveBudget(monitor);
            refreshMonitors();
            return null;
        }

    }

    //@formatter:off
    // lists with the monitors we provied
    private List<SelectionInList<BudgetDTO>> providedMonitors = new ArrayList<SelectionInList<BudgetDTO>>();
    
    // the edit model, validator and dialog
    private PresentationModel<BudgetDTO>     editModel;
    private BudgetValidator<BudgetDTO>       validator;
    private BudgetMonitorEditView            editDialog;
    //@formatter:on


    /**
     * @param mainPresenter
     */
    public BudgetMonitorPresenter(MainPresenter mainPresenter) {
        super(mainPresenter);
    }


    /**
     * return a presentation model for calculations.
     * 
     * the model is extended with functionality to calculate the current value
     * 
     * @param model
     * @return
     */
    public PresentationModel<BudgetDTO> getCalculationPresentationModel(ValueModel model) {
        return new BudgetMonitorCalculationModell(model);
    }


    /**
     * Spinner Model erstellen fuer den aktuellen Monat
     * 
     * @return
     */
    public SpinnerModel getMonitorDateSpinnerModel(ValueModel model) {

        // calculate start and end of the current month
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date startOfMonth = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date endOfMonth = cal.getTime();

        // create spinner model bound to the value model
        return SpinnerAdapterFactory.createDateAdapter(model, new Date(), startOfMonth, endOfMonth, Calendar.DAY_OF_MONTH);
    }

    /**
     * get a ListModel for the monitos
     * 
     * @return
     */
    public SelectionInList<BudgetDTO> getMonitorListModel() {
        SelectionInList<BudgetDTO> selection = new SelectionInList<BudgetDTO>(getService().getBudgets());
        providedMonitors.add(selection);
        return selection;
    }

    /**
     * refresh all monitors we provied
     * 
     */
    private void refreshMonitors() {
        // refresh monitor lists
        List<BudgetDTO> budgets = getService().getBudgets();
        for (SelectionInList<BudgetDTO> selection : providedMonitors) {
            selection.setList(budgets);
            selection.fireContentsChanged(0, selection.getSize());
        }
    }

    
    
    
    
//    /**
//     * Ueberschreibt Methode von Superklasse {@see ch.tkayser.budget.swing.presenter.AbstractBudgetEditPresenter#createEditModel()}
//     *
//     * @return
//     */
//    @Override
//    protected PresentationModel<BudgetDTO> createEditModel() {
//        return new PresentationModel<BudgetDTO>(new BudgetDTO());        
//    }
//
//    /**
//     * Ueberschreibt Methode von Superklasse {@see ch.tkayser.budget.swing.presenter.AbstractBudgetEditPresenter#createEditDialog()}
//     *
//     * @return
//     */
//    @Override
//    protected AbstractBudgetDialog<BudgetDTO> createEditDialog() {
//        return new BudgetMonitorEditView(this);
//        
//    }
//
//    /**
//     * Ueberschreibt Methode von Superklasse {@see ch.tkayser.budget.swing.presenter.AbstractBudgetEditPresenter#createValidator(com.jgoodies.binding.PresentationModel)}
//     *
//     * @param editModel
//     * @return
//     */
//    @Override
//    protected BudgetValidator<BudgetDTO> createValidator(PresentationModel<BudgetDTO> editModel) {
//        return new BudgetValidator<BudgetDTO>(editModel);
//        
//    }


    /**
     * Ueberschreibt Methode von Superklasse {@see ch.tkayser.budget.swing.presenter.Presenter#delete(ch.tkayser.budget.base.dto.BaseDTO)}
     *
     * @param bean
     */
    @Override
    public void delete(BudgetDTO bean) {
        executeTask(new DeleteMonitorTask(bean));
    }


    /**
     * Ueberschreibt Methode von Superklasse {@see ch.tkayser.budget.swing.presenter.Presenter#save(ch.tkayser.budget.base.dto.BaseDTO)}
     *
     * @param bean
     */
    @Override
    public void save(BudgetDTO bean) {
        executeTask(new SaveMonitorTask(bean));
    }


    /**
     * Ueberschreibt Methode von Superklasse {@see ch.tkayser.budget.swing.presenter.Presenter#getEditModel()}
     *
     * @return
     */
    @Override
    public synchronized PresentationModel<BudgetDTO> getEditModel() {
        if (editModel == null) {
            editModel = new PresentationModel<BudgetDTO>(new BudgetDTO());
        }
        return editModel;
    }


    /**
     * Ueberschreibt Methode von Superklasse {@see ch.tkayser.budget.swing.presenter.Presenter#getEditValidator()}
     *
     * @return
     */
    @Override
    public synchronized BudgetValidator<BudgetDTO> getEditValidator() {
        if (validator == null) {
            validator = new BudgetValidator<BudgetDTO>(getEditModel());
        }
        return validator;        
    }


    /**
     * Ueberschreibt Methode von Superklasse {@see ch.tkayser.budget.swing.presenter.AbstractBudgetPresenter#getEditDialog()}
     *
     * @return
     */
    @Override
    public synchronized AbstractBudgetDialog<BudgetDTO> getEditDialog() {
        if (editDialog == null) {
            editDialog = new BudgetMonitorEditView(this);
        }
        return editDialog;        
    }


    /**
     * Ueberschreibt Methode von Superklasse {@see ch.tkayser.budget.swing.presenter.AbstractBudgetEditPresenter#getNewBeanInstance()}
     *
     * @return
     */
    @Override
    protected BudgetDTO getNewBeanInstance() {
        return new BudgetDTO();
    }

}
