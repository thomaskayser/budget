package ch.tkayser.budget.swing.widgets.daterange;

import java.util.Calendar;
import java.util.Date;

public enum DateRange {
    CURRENT_MONTH, 
    CURRENT_YEAR,    
    PREV_MONTH, 
    PREV_YEAR,  
    YEAR_TO_PREV_MONTH, 
    YEAR_TO_CURRENT_MONTH;

    /**
     * get the from date for the daterange
     * 
     * @return
     */
    public Date getDateFrom() {

	// init a calendar with the current date
	Calendar cal = Calendar.getInstance();
	cal.setTime(new Date());

	switch (this) {
	case CURRENT_MONTH:
	    // first of month
	    cal.set(Calendar.DAY_OF_MONTH, 1);
	    break;
	case PREV_MONTH:
	    // first of month minus one moth
	    cal.set(Calendar.DAY_OF_MONTH, 1);
	    cal.add(Calendar.MONTH, -1);
	    break;
	case CURRENT_YEAR:
	case YEAR_TO_CURRENT_MONTH:
	case YEAR_TO_PREV_MONTH:
	    // first of year
	    cal.set(Calendar.DAY_OF_YEAR, 1);
	    break;

	case PREV_YEAR:
	    // first of year minus one year
	    cal.set(Calendar.DAY_OF_YEAR, 1);
	    cal.add(Calendar.YEAR, -1);
	    break;
	}

	return cal.getTime();

    }

    /**
     * get the to date for the daterange
     * 
     * @return
     */
    public Date getDateTo() {

	// init a calendar with the current date
	Calendar cal = Calendar.getInstance();
	cal.setTime(new Date());

	switch (this) {
	case CURRENT_MONTH:
	case YEAR_TO_CURRENT_MONTH:
	    // first of next month minus one day
	    cal.set(Calendar.DAY_OF_MONTH, 1);
	    cal.add(Calendar.MONTH, 1);
	    cal.add(Calendar.DAY_OF_MONTH, -1);
	    break;
	case PREV_MONTH:
	case YEAR_TO_PREV_MONTH:
	    // first of month
	    cal.set(Calendar.DAY_OF_MONTH, 1);
	    // one day back
	    cal.add(Calendar.DAY_OF_MONTH, -1);
	    break;
	case CURRENT_YEAR:
	    // set to 31.12
	    cal.set(Calendar.MONTH, 11);
	    cal.set(Calendar.DAY_OF_MONTH , 31);
	    break;
	case PREV_YEAR:
	    // first of year minus one day
	    cal.set(Calendar.DAY_OF_YEAR, 1);
	    cal.add(Calendar.DAY_OF_YEAR, -1);	    
	    break;
	    
	}

	return cal.getTime();		
    }

}
