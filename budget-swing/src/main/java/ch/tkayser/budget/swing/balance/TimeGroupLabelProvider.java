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
package ch.tkayser.budget.swing.balance;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

import ch.tkayser.budget.dto.TimeGroup;
import ch.tkayser.budget.swing.Constants;

/**
 * @author tom
 * 
 */
public class TimeGroupLabelProvider extends DefaultListCellRenderer {

    private static final long serialVersionUID = 1L;
    
    // the resources
    private ResourceMap resourceMap;

    /**
     * TODO: Bitte durch Konstruktor Beschreibung ersetzen
     * 
     */
    public TimeGroupLabelProvider() {
        // get the resource Map
        resourceMap = Application.getInstance().getContext().getResourceMap(getClass());

    }

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
        if (value != null) {
            lbl.setText(getTimeGroupText((TimeGroup)value));
        }
        return lbl;
    }

    /**
     * Text fuer Timegroup holen
     * 
     * @param value
     * @return
     */
    private String getTimeGroupText(TimeGroup value) {
        String key = null;
        switch (value) {
            case MONTH:
                key = Constants.LBL_MONTH;
                break;
            case QUARTER:
                key = Constants.LBL_QUARTER;
                break;
            case YEAR:
                key = Constants.LBL_YEAR;
                break;
            case NONE:
                key = "";
                break;
            default:
                return "";
        }
        return resourceMap.getString(key);
    }

}
