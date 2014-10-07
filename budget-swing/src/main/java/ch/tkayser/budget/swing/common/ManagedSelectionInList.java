/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-04-05 15:36:05 +0200 (Di, 05 Apr 2011) $
 *   $Author: tom $
 *   $Revision: 2064 $ 
 */
package ch.tkayser.budget.swing.common;

import java.util.ArrayList;
import java.util.List;

import com.jgoodies.binding.list.SelectionInList;

/**
 * A managed SelectionInList. Holds the selection and metadata about the selection
 */
public class ManagedSelectionInList<T> {

    // is the selection mandatory
    private boolean mandatory;
    
    // the values in this selection
    private SelectionInList<T> selectionInList;
    
    /** 
     *
     * @param mandatory
     * @param selectionInList
     */
    public ManagedSelectionInList(boolean mandatory, SelectionInList<T> selectionInList) {
        super();
        this.mandatory = mandatory;
        this.selectionInList = selectionInList;
    }
    /**
     * Fill the selection with a copy of the list with the elements. if the selection is not mandatory add an empty entry
     *
     * @param elements
     */
    public void fillSelection(List<T> elements) {
        List<T> copy = new ArrayList<T>(elements);        
        selectionInList.setList(copy);
        if (!mandatory) {
            selectionInList.getList().add(null);
        }
    }
    public SelectionInList<T> getSelectionInList() {
        return this.selectionInList;
    }
    public boolean isMandatory() {
        return this.mandatory;
    }
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }
    
    public void setSelectionInList(SelectionInList<T> selection) {
        this.selectionInList = selection;
    }
    
    
}
