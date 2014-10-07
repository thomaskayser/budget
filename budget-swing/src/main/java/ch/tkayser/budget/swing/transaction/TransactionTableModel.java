/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-04-14 16:26:43 +0200 (Do, 14 Apr 2011) $
 *   $Author: tom $
 *   $Revision: 2100 $ 
 */
package ch.tkayser.budget.swing.transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.swing.Constants;

/**
 * Table Model fuer Transactions
 */
public class TransactionTableModel implements TableModel {

    /**
     * Column Information
     */
    private class ColumnInfo {
        private String columName;

        private Class<?> columnClass;

        public ColumnInfo(String columName, Class<?> columnClass) {
            super();
            this.columName = columName;
            this.columnClass = columnClass;
        }

        public String getColumName() {
            return this.columName;
        }

        public Class<?> getColumnClass() {
            return this.columnClass;
        }

    }

    // the transactions to display
    private List<TransactionDTO> transactions = new ArrayList<TransactionDTO>();

    // listeners
    private EventListenerList listeners = new EventListenerList();

    // columns names and classes
    private List<ColumnInfo> columns = new ArrayList<TransactionTableModel.ColumnInfo>();

    /** 
     * 
     *
     */
    public TransactionTableModel() {
        // get a resourceMap
        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(getClass());
        // init column Names
        columns.add(new ColumnInfo(resourceMap.getString(Constants.LBL_VALUTA), Date.class));
        columns.add(new ColumnInfo(resourceMap.getString(Constants.LBL_RECIPIENT), String.class));
        columns.add(new ColumnInfo(resourceMap.getString(Constants.LBL_SENDER), String.class));
        columns.add(new ColumnInfo(resourceMap.getString(Constants.LBL_BOOKINGTEXT), String.class));
        columns.add(new ColumnInfo(resourceMap.getString(Constants.LBL_ACCOUNT), String.class));
        columns.add(new ColumnInfo(resourceMap.getString(Constants.LBL_AMOUNT), BigDecimal.class));
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see
     * javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)}
     * 
     * @param l
     */
    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(TableModelListener.class, l);
    }

    /**
     * Transaktion hinzufügen
     * 
     * @param tx
     */
    public void addTransaction(TransactionDTO tx) {
        transactions.add(tx);
        fireTxChanged();
    }

    /** 
     * Löscht das Model komplett
     *
     */
    public void clear() {
        transactions.clear();
        fireTxChanged();
    }

    /**
     * Listeners benachrichtigen
     * 
     */
    private void fireTxChanged() {
        TableModelEvent evt = new TableModelEvent(this);
        for (TableModelListener l : listeners.getListeners(TableModelListener.class)) {
            l.tableChanged(evt);
        }
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.table.TableModel#getColumnClass(int)}
     * 
     * @param columnIndex
     * @return
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columns.get(columnIndex).getColumnClass();
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.table.TableModel#getColumnCount()}
     * 
     * @return
     */
    @Override
    public int getColumnCount() {
        return columns.size();
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.table.TableModel#getColumnName(int)}
     * 
     * @param columnIndex
     * @return
     */
    @Override
    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex).getColumName();
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.table.TableModel#getRowCount()}
     * 
     * @return
     */
    @Override
    public int getRowCount() {
        return transactions.size();
    }

    /**
     * gibt die position einer Tx in der Tabelle zurueck
     * 
     * @param dto
     * @return
     */
    public int getRowInModel(TransactionDTO dto) {
        return transactions.indexOf(dto);
    }

    /**
     * gibt die Transaktion an einer bestimmten zeile zurueck
     * 
     * @param row
     * @return
     */
    public TransactionDTO getTransaction(int row) {
        return transactions.get(row);
    }

    /**
     * gibt die Transactionen zurueck
     * 
     * @return
     */
    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.table.TableModel#getValueAt(int, int)}
     * 
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TransactionDTO tx = transactions.get(rowIndex);
        if (tx != null) {
            switch (columnIndex) {
                case 0:
                    return tx.getValuta();
                case 1:
                    return tx.getReciever();
                case 2:
                    return tx.getSender();
                case 3:
                    return tx.getBookingText();
                case 4:
                    if (tx.getAccount() != null) {
                        return tx.getAccount().getName();
                    } else {
                        return "";
                    }
                case 5:
                    return tx.getAmount();
            }
        }
        return null;
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.table.TableModel#isCellEditable(int, int)}
     * 
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;

    }

    /**
     * Ueberschreibt Methode von Superklasse {@see
     * javax.swing.table.TableModel#removeTableModelListener(javax.swing.event.TableModelListener)}
     * 
     * @param l
     */
    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(TableModelListener.class, l);
    }

    /**
     * entfernt eine Tx aus dem table model
     * 
     * @param tx
     */
    public void removeTransaction(int row) {
        transactions.remove(row);
        fireTxChanged();

    }

    /**
     * ersetzt eine Transaction
     * 
     * @param tx
     */
    public void replaceTransaction(int row, TransactionDTO tx) {
        // entfernen und hinzufuegen
        removeTransaction(row);
        addTransaction(tx);
    }

    /**
     * setzt die Transactions
     * 
     * @param transactions
     */
    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
        fireTxChanged();
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)}
     * 
     * @param aValue
     * @param rowIndex
     * @param columnIndex
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

}
