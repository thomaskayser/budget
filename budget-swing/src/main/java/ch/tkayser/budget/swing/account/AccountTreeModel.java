/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-04-04 20:41:09 +0200 (Mo, 04 Apr 2011) $
 *   $Author: tom $
 *   $Revision: 2057 $ 
 */
package ch.tkayser.budget.swing.account;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import ch.tkayser.budget.dto.AccountDTO;

/**
 * Account Tree Modell
 */
public class AccountTreeModel implements TreeModel {

    // the accounts
    private List<AccountDTO> accounts;

    // kuenstlicher root Node
    private String rootNode = "Konti";
    
    // listeners
    private EventListenerList listeners = new EventListenerList();


    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)}
     * 
     * @param l
     */
    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listeners.add(TreeModelListener.class, l);
    }

    /**
     * The Model was changed
     * 
     */
    private void fireModelChanged() {
        TreeModelEvent evt = new TreeModelEvent(this, new Object[] {rootNode});
        for (TreeModelListener listener : listeners.getListeners(TreeModelListener.class)) {
            listener.treeStructureChanged(evt);
        }
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)}
     * 
     * @param parent
     * @param index
     * @return
     */
    @Override
    public Object getChild(Object parent, int index) {
        if (isRootNode(parent)) {
            return getParentAccounts().get(index);
        }
        return ((AccountDTO)parent).getChildren().get(index);
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)}
     * 
     * @param parent
     * @return
     */
    @Override
    public int getChildCount(Object parent) {
        if (isRootNode(parent)) {
            return getParentAccounts().size();           
        }
        return ((AccountDTO)parent).getChildren().size();
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)}
     * 
     * @param parent
     * @param child
     * @return
     */
    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (isRootNode(parent)) {
            return getParentAccounts().indexOf(child);
        }
        return ((AccountDTO)parent).getChildren().indexOf(child);
    }

    /** 
     * Liest alle Parent Accounts
     *
     * @return
     */
    private List<AccountDTO> getParentAccounts() {
        List<AccountDTO> parents = new ArrayList<AccountDTO>();
        for (AccountDTO account: accounts) {
            if (account.getParent() == null) {
                parents.add(account);
            }
        }
        return parents;
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.tree.TreeModel#getRoot()}
     * 
     * @return
     */
    @Override
    public Object getRoot() {
        return rootNode;
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)}
     * 
     * @param node
     * @return
     */
    @Override
    public boolean isLeaf(Object node) {
        if (isRootNode(node)) {
            return false;
        }
        // ja wenn keine children
        return ((AccountDTO)node).getChildren().size() == 0;
    }

    /** 
     * Pruefen ob es der RootNode ist
     *
     * @param node
     * @return
     */
    private boolean isRootNode(Object node) {
        return (node instanceof String && node.equals(rootNode));
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)}
     * 
     * @param l
     */
    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(TreeModelListener.class, l);
    }

    /**
     * set the accounts to display
     * 
     * @param accounts
     */
    public void setAccounts(List<AccountDTO> accounts) {
        this.accounts = accounts;
        fireModelChanged();
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath,
     * java.lang.Object)}
     * 
     * @param path
     * @param newValue
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

}
