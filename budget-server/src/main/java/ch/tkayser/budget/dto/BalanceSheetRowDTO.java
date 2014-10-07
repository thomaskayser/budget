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
package ch.tkayser.budget.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ch.tkayser.budget.base.dto.BaseDTO;

/**
 * The row in a balance sheet
 * 
 * @author isc-kat
 * 
 */
public class BalanceSheetRowDTO  extends BaseDTO{

    private static final long serialVersionUID = 988558529967977720L;

    // the title for the row
    private String                   title;

    // is it the total row
    private boolean                  isTotalRow;

    // the child Rows
    private List<BalanceSheetRowDTO> childRows  = new ArrayList<BalanceSheetRowDTO>();

    // totals of the groups
    private List<BigDecimal>         groupTotals;

    // the total of the row
    private BigDecimal               rowTotal;

    // the avg of the row
    private BigDecimal               rowAvg;

    /**
     * add a child to this row
     * 
     * @param child
     */
    public void addChildRow(BalanceSheetRowDTO child) {
        if (childRows == null) {
            childRows = new ArrayList<BalanceSheetRowDTO>();
        }
        childRows.add(child);
    }

    /**
     * @return the childRows
     */
    public List<BalanceSheetRowDTO> getChildRows() {
        return childRows;
    }

    /**
     * @return the groupTotals
     */
    public List<BigDecimal> getGroupTotals() {
        return groupTotals;
    }

    /**
     * @return the rowAvg
     */
    public BigDecimal getRowAvg() {
        return rowAvg;
    }

    /**
     * @return the rowTotal
     */
    public BigDecimal getRowTotal() {
        return rowTotal;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param groupTotals
     *            the groupTotals to set
     */
    public void setGroupTotals(List<BigDecimal> groupTotals) {
        this.groupTotals = groupTotals;
    }

    /**
     * @return the isTotalRow
     */
    public boolean isTotalRow() {
        return isTotalRow;
    }

    /**
     * @param isTotalRow
     *            the isTotalRow to set
     */
    public void setTotalRow(boolean isTotalRow) {
        this.isTotalRow = isTotalRow;
    }

    /**
     * @param rowAvg
     *            the rowAvg to set
     */
    public void setRowAvg(BigDecimal rowAvg) {
        this.rowAvg = rowAvg;
    }

    /**
     * @param rowTotal
     *            the rowTotal to set
     */
    public void setRowTotal(BigDecimal rowTotal) {
        this.rowTotal = rowTotal;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

}
