/*
 * Software is written by:
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2011
 * 
 */
package ch.tkayser.budget.swing.transaction;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import ch.tkayser.budget.parser.TransactionParser;

/**
 * CellRenderer fuer TransactionParser
 * 
 * @author tom
 * 
 */
public class TransactionParserListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax
     * .swing.JList, java.lang.Object, int, boolean, boolean)
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        // TODO Auto-generated method stub
        JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null) {
            lbl.setText(((TransactionParser) value).getParserName());
        }
        return lbl;
    }

}
