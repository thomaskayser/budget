/*
 * Software is written by:
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2011
 * 
 */
package ch.tkayser.budget.swing.account;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import ch.tkayser.budget.dto.AccountDTO;

/**
 * Renderer fuer Account in a Tree. Zeigt auch den Namen des Parents an
 * 
 * @author tom
 * 
 */
@SuppressWarnings("serial")
public class AccountListCellRenderer extends DefaultListCellRenderer {

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
        JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof AccountDTO) {
            AccountDTO account = (AccountDTO) value;
            StringBuilder name = new StringBuilder();
            name.append(account.getName());
            if (account.getParent() != null) {
                name.append(" (");
                name.append(account.getParent().getName());
                name.append(")");
            }
            lbl.setText(name.toString());
        }
        return lbl;
    }

}
