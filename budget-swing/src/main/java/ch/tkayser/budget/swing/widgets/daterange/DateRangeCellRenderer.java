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
package ch.tkayser.budget.swing.widgets.daterange;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 * @author tom
 * 
 */
@SuppressWarnings("serial")
public class DateRangeCellRenderer extends DefaultListCellRenderer {

    private ResourceMap resourceMap;

    // Resource Map

    /**
     * Ueberschreibt Methode von Superklasse {@see
     * javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.
     * swing.JList, java.lang.Object, int, boolean, boolean)}
     * 
     * @param list
     * @param value
     * @param index
     * @param isSelected
     * @param cellHasFocus
     * @return
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        // get the resource Map
        resourceMap = Application.getInstance().getContext().getResourceMap(getClass());

        // label erstellen
        JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        lbl.setText(getText(value));

        return lbl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
     */
    public String getText(Object object) {
        // evaluate DateRange
        if (object == null) {
            return "";
        }
        DateRange range = (DateRange) object;
        // name des ranges uebersetzen
        return resourceMap.getString(range.name().toLowerCase());
    }

}
