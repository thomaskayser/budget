/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-04-05 08:28:25 +0200 (Di, 05 Apr 2011) $
 *   $Author: tom $
 *   $Revision: 2058 $ 
 */
package ch.tkayser.budget.swing.budgetmonitor;

import java.util.Date;

import ch.tkayser.budget.dto.BudgetDTO;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueModel;

/**
 * Calculation Model fuer Monitore. Setzt des MonitorDate beim wechseln des Beans
 */
public class BudgetMonitorCalculationModell extends PresentationModel<BudgetDTO> {

    private static final long serialVersionUID = 1L;

    /**
     * 
     * 
     * @param beanChannel
     */
    public BudgetMonitorCalculationModell(ValueModel beanChannel) {
        super(beanChannel);
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see com.jgoodies.binding.PresentationModel#beforeBeanChange(java.lang.Object,
     * java.lang.Object)}
     * 
     * @param oldBean
     * @param newBean
     */
    @Override
    public void beforeBeanChange(BudgetDTO oldBean, BudgetDTO newBean) {
        super.beforeBeanChange(oldBean, newBean);
        // init Monitor Date to today when new bean is selected
        if (newBean != null) {
            newBean.setMonitorDate(new Date());
        }
    }

}
