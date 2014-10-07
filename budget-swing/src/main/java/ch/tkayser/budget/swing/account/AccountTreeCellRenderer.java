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

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import ch.tkayser.budget.dto.AccountDTO;

/**
 * Renderer fuer Account in a Tree. zeigt nur den Namen an
 * 
 * @author tom
 * 
 */
@SuppressWarnings("serial")
public class AccountTreeCellRenderer extends DefaultTreeCellRenderer {

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent
     * (javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int,
     * boolean)
     */
    @Override
    public Component getTreeCellRendererComponent(JTree arg0, Object object, boolean arg2, boolean arg3, boolean arg4,
            int arg5, boolean arg6) {
        JLabel lbl = (JLabel) super.getTreeCellRendererComponent(arg0, object, arg2, arg3, arg4, arg5, arg6);
        if (object instanceof AccountDTO) {
            lbl.setText(((AccountDTO) object).getName());
        }
        return lbl;
    }

}
