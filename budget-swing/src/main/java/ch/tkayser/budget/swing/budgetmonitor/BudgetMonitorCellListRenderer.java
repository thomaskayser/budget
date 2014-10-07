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

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import ch.tkayser.budget.dto.BudgetDTO;

/**
 * Renderer fuer Monitors in Lists
 */
public class BudgetMonitorCellListRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 1L;

    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList,
     * java.lang.Object, int, boolean, boolean)}
     * 
     * @param list
     * @param value
     * @param index
     * @param isSelected
     * @param cellHasFocus
     * @return
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel lbl = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        lbl.setText(((BudgetDTO)value).getName());
        return lbl;
    }

}
