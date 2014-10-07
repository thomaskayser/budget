/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate$
 *   $Author$
 *   $Revision$ 
 */
package ch.tkayser.budget.swing.balance;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.treetable.TreeTableModel;

import ch.tkayser.budget.dto.BalanceSheetDTO;
import ch.tkayser.budget.dto.BalanceSheetRowDTO;
import ch.tkayser.budget.dto.TimeGroup;
import ch.tkayser.budget.swing.Constants;

/**
 * TODO: Bitte durch Klassenbeschreibung ersetzen
 */
public class BalanceTreeTableModel implements TreeTableModel {

    // listeners
    private EventListenerList listenerList = new EventListenerList();

    // the balances
    private BalanceSheetDTO sheet = new BalanceSheetDTO();

    // a resource map
    private ResourceMap resourceMap;

    // spaltennamen
    private List<String> columnNames;

    /**
     * TODO: Bitte durch Konstruktor Beschreibung ersetzen
     * 
     */
    public BalanceTreeTableModel() {
        // get a resourceMap
        this.resourceMap = Application.getInstance().getContext().getResourceMap(getClass());
        // spaltennamen init
        initColumnNames();
    }

    /**
     * spaltenNamen init
     * 
     */
    private void initColumnNames() {
        columnNames = new ArrayList<String>();

        // konto
        columnNames.add(resourceMap.getString(Constants.LBL_ACCOUNT));

        // gruppentotale
        for (Integer key : sheet.getGroupKeys()) {
            if (sheet.getGroupedBy().equals(TimeGroup.MONTH)) {
                // monate keys formatiert
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.MONTH, key.intValue() - 1);
                String title = new SimpleDateFormat("MMM").format(cal.getTime());
                columnNames.add(title);
            } else {
                // sonst nur key (quartal/jarh)
                columnNames.add(key.toString());
            }
        }

        // avg / sum
        columnNames.add(resourceMap.getString(Constants.LBL_AVG));
        columnNames.add(resourceMap.getString(Constants.LBL_SUM));

    }

    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.tree.TreeModel#getRoot()}
     * 
     * @return
     */
    @Override
    public Object getRoot() {
        return sheet;
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
        // return child of sheet or row
        if (parent instanceof BalanceSheetDTO) {
            return ((BalanceSheetDTO)parent).getRows().get(index);
        } else {
            return ((BalanceSheetRowDTO)parent).getChildRows().get(index);
        }
    }

    /**
     * @param sheet the sheet to set
     */
    public void setSheet(BalanceSheetDTO sheet) {
        this.sheet = sheet;
        initColumnNames();
        fireModelChaned();
    }
    
    /**
     * 
     */
    private void fireModelChaned() {
        TreeModelEvent evt = new TreeModelEvent(this, new Object[] { sheet });
        for (TreeModelListener listener : listenerList.getListeners(TreeModelListener.class)) {
            listener.treeStructureChanged(evt);
        }

    }
    
    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)}
     * 
     * @param parent
     * @return
     */
    @Override
    public int getChildCount(Object parent) {
        int children = 0;
        if (parent instanceof BalanceSheetDTO) {
            children = ((BalanceSheetDTO)parent).getRows().size();
        } else {
            children = ((BalanceSheetRowDTO)parent).getChildRows().size();            
        }
        return children;
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)}
     * 
     * @param node
     * @return
     */
    @Override
    public boolean isLeaf(Object node) {
        // root is never leaf
        if (node instanceof BalanceSheetDTO) {
            return false;
        }
        // has it childRows?
        boolean leaf = ((BalanceSheetRowDTO)node).getChildRows().size() == 0;
        return leaf;
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

    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)}
     * 
     * @param parent
     * @param child
     * @return
     */
    @Override
    public int getIndexOfChild(Object parent, Object child) {
        // pos of sheet or row child
        if (parent instanceof BalanceSheetDTO) {
            return ((BalanceSheetDTO)parent).getRows().indexOf(child);
        } else {
            return ((BalanceSheetRowDTO)parent).getChildRows().indexOf(child);
        } 
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)}
     * 
     * @param l
     */
    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(TreeModelListener.class, l);        
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)}
     * 
     * @param l
     */
    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(TreeModelListener.class, l);
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see org.jdesktop.swingx.treetable.TreeTableModel#getColumnClass(int)}
     * 
     * @param paramInt
     * @return
     */
    @Override
    public Class<?> getColumnClass(int paramInt) {
        if (paramInt == 0) {
            return String.class;
        } else {
            return BigDecimal.class;
        }
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see org.jdesktop.swingx.treetable.TreeTableModel#getColumnCount()}
     * 
     * @return
     */
    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see org.jdesktop.swingx.treetable.TreeTableModel#getColumnName(int)}
     * 
     * @param paramInt
     * @return
     */
    @Override
    public String getColumnName(int paramInt) {
        return columnNames.get(paramInt);        
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see org.jdesktop.swingx.treetable.TreeTableModel#getHierarchicalColumn()}
     * 
     * @return
     */
    @Override
    public int getHierarchicalColumn() {
        return 0;

    }

    /**
     * Ueberschreibt Methode von Superklasse {@see org.jdesktop.swingx.treetable.TreeTableModel#getValueAt(java.lang.Object, int)}
     * 
     * @param paramObject
     * @param paramInt
     * @return
     */
    @Override
    public Object getValueAt(Object paramObject, int paramInt) {
        // daten aus row holen
        if (!(paramObject instanceof BalanceSheetRowDTO)) {
            return null;
        }
        BalanceSheetRowDTO row = (BalanceSheetRowDTO) paramObject;


        // first column -> account
        if (paramInt == 0) {
            return row.getTitle();
        }

        // group totals?
        int indexInGroupTotals = paramInt -1;        
        if (indexInGroupTotals < row.getGroupTotals().size()) {
            return row.getGroupTotals().get(indexInGroupTotals);
        }

        // one of total or avg
        int indexAfterGroupTotals = paramInt - row.getGroupTotals().size();
        if (indexAfterGroupTotals == 1) {
            return row.getRowAvg();
        } else {
            return row.getRowTotal();
        }
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see org.jdesktop.swingx.treetable.TreeTableModel#isCellEditable(java.lang.Object, int)}
     * 
     * @param paramObject
     * @param paramInt
     * @return
     */
    @Override
    public boolean isCellEditable(Object paramObject, int paramInt) {
        return false;
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see org.jdesktop.swingx.treetable.TreeTableModel#setValueAt(java.lang.Object,
     * java.lang.Object, int)}
     * 
     * @param paramObject1
     * @param paramObject2
     * @param paramInt
     */
    @Override
    public void setValueAt(Object paramObject1, Object paramObject2, int paramInt) {
    }

}
