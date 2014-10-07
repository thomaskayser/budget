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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.tkayser.budget.base.dto.BaseDTO;

public class BalanceSheetDTO extends BaseDTO {

    private static final long serialVersionUID = -5146751484270480872L;

    // the headers
    private List<String> headers = new ArrayList<String>();

    // the rows in the sheet
    private List<BalanceSheetRowDTO> rows = new ArrayList<BalanceSheetRowDTO>();

    // the timegroup for this sheet
    private TimeGroup groupedBy;

    // the group Keys for the totals
    private List<Integer> m_groupKeys = new ArrayList<Integer>();


    public TimeGroup getGroupedBy() {
        return this.groupedBy;
    }

    public List<Integer> getGroupKeys() {
        return this.m_groupKeys;
    }

    /**
     * @return the headers
     */
    public List<String> getHeaders() {
        return headers;
    }

    /**
     * @return the rows
     */
    public List<BalanceSheetRowDTO> getRows() {
        return rows;
    }

    /**
     * Order the rows by title (inkl. children)
     */
    public void orderByTitle() {
        // process parents
        orderByTitle(rows);
    }

    private void orderByTitle(List<BalanceSheetRowDTO> rows) {
        // no rows no sorting
        if (rows == null) {
            return;
        }
        // order rows by name
        Collections.sort(rows, new Comparator<BalanceSheetRowDTO>() {
            public int compare(BalanceSheetRowDTO o1, BalanceSheetRowDTO o2) {
                if (o1 == null || o2 == null) {
                    return 1;
                }
                // order by title
                return o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
            }
        });

        // order children recursive
        for (BalanceSheetRowDTO row : rows) {
            orderByTitle(row.getChildRows());
        }

    }

    public void setGroupedBy(TimeGroup timeGroup) {
        this.groupedBy = timeGroup;
    }

    public void setGroupKeys(List<Integer> m_groupKeys) {
        this.m_groupKeys = m_groupKeys;
    }

    /**
     * @param headers the headers to set
     */
    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(List<BalanceSheetRowDTO> rows) {
        this.rows = rows;
    }
}
