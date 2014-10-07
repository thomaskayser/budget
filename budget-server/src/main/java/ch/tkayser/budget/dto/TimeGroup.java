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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author isc-kat
 * 
 */
public enum TimeGroup {

    MONTH, QUARTER, YEAR, NONE;

    /**
     * get keys for the entries of a group
     */
    public static List<Integer> getGroupKeys(Date from, Date to, TimeGroup group) {

        // null group?
        if (group == null) {
            return null;
        }

        // init the titles
        List<Integer> keys = new ArrayList<Integer>();

        // get titles for differen time groups
        Calendar calFrom = Calendar.getInstance();
        calFrom.setTime(from);
        Calendar calTo = Calendar.getInstance();
        calTo.setTime(to);

        switch (group) {
        case MONTH:
            while (calFrom.before(calTo)) {
                // add one to month as january is 1
                keys.add(calFrom.get(Calendar.MONTH) + 1);
                calFrom.add(Calendar.MONTH, 1);
            }

            break;

        case QUARTER:

            // set month to start of current quarter
            int startMonthOfQuarter = (calFrom.get(Calendar.MONTH) / 3) * 3;
            calFrom.set(Calendar.MONTH, startMonthOfQuarter);
            while (calFrom.before(calTo)) {
                keys.add((calFrom.get(Calendar.MONTH) / 3)+1);
                calFrom.add(Calendar.MONTH, 3);
            }

            break;

        case YEAR:
            while (calFrom.before(calTo)) {
                keys.add(calFrom.get(Calendar.YEAR));
                calFrom.add(Calendar.YEAR, 1);
            }
            break;
            
        case NONE:
            break;

        }

        return keys;
    }

}
