package ch.tkayser.budget.service;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import ch.tkayser.budget.dto.TimeGroup;
import ch.tkayser.budget.test.BudgetBaseTest;
import ch.tkayser.budget.test.DatenHelper;

public class TimeGroupTest extends BudgetBaseTest {
    
    @Test
    public void testTitles() {


	// get titles for yearly groups
	Date dateFrom = DatenHelper.parseDate("01.01.2007");
	Date dateTo = DatenHelper.parseDate("31.12.2009");
	List<Integer> titles = TimeGroup.getGroupKeys(dateFrom, dateTo, TimeGroup.YEAR);
	
	Assert.assertEquals(3, titles.size());
	Assert.assertEquals(new Integer(2007), titles.get(0));
        Assert.assertEquals(new Integer(2008), titles.get(1));
        Assert.assertEquals(new Integer(2009), titles.get(2));

	// monthly groups
	dateFrom = DatenHelper.parseDate("15.05.2007");
	dateTo = DatenHelper.parseDate("28.09.2007");
	titles = TimeGroup.getGroupKeys(dateFrom, dateTo, TimeGroup.MONTH);
	Assert.assertEquals(5, titles.size());
        Assert.assertEquals(new Integer(5), titles.get(0));
        Assert.assertEquals(new Integer(6), titles.get(1));
        Assert.assertEquals(new Integer(7), titles.get(2));
        Assert.assertEquals(new Integer(8), titles.get(3));
        Assert.assertEquals(new Integer(9), titles.get(4));

	// quarterly groups
	dateFrom = DatenHelper.parseDate("01.01.2009");
	dateTo = DatenHelper.parseDate("31.12.2009");
	titles = TimeGroup.getGroupKeys(dateFrom, dateTo, TimeGroup.QUARTER);
	Assert.assertEquals(4, titles.size());
	Assert.assertEquals(new Integer(1), titles.get(0));
	Assert.assertEquals(new Integer(2), titles.get(1));
	Assert.assertEquals(new Integer(3), titles.get(2));
	Assert.assertEquals(new Integer(4), titles.get(3));
	
	dateFrom = DatenHelper.parseDate("01.02.2009");
	dateTo = DatenHelper.parseDate("15.05.2009");
	titles = TimeGroup.getGroupKeys(dateFrom, dateTo, TimeGroup.QUARTER);
	Assert.assertEquals(2, titles.size());
	Assert.assertEquals(new Integer(1), titles.get(0));
	Assert.assertEquals(new Integer(2), titles.get(1));

	dateFrom = DatenHelper.parseDate("01.03.2009");
	dateTo = DatenHelper.parseDate("15.05.2009");
	titles = TimeGroup.getGroupKeys(dateFrom, dateTo, TimeGroup.QUARTER);
	Assert.assertEquals(2, titles.size());
	Assert.assertEquals(new Integer(1), titles.get(0));
	Assert.assertEquals(new Integer(2), titles.get(1));
	
	dateFrom = DatenHelper.parseDate("01.04.2009");
	dateTo = DatenHelper.parseDate("15.05.2009");
	titles = TimeGroup.getGroupKeys(dateFrom, dateTo, TimeGroup.QUARTER);
	Assert.assertEquals(1, titles.size());
	Assert.assertEquals(new Integer(2), titles.get(0));
	
	dateFrom = DatenHelper.parseDate("01.04.2009");
	dateTo = DatenHelper.parseDate("15.07.2009");
	titles = TimeGroup.getGroupKeys(dateFrom, dateTo, TimeGroup.QUARTER);
	Assert.assertEquals(2, titles.size());
	Assert.assertEquals(new Integer(2), titles.get(0));
	Assert.assertEquals(new Integer(3), titles.get(1));
	
	
	// ungrouped
	dateFrom = DatenHelper.parseDate("15.05.2007");
	dateTo = DatenHelper.parseDate("28.09.2007");
	titles = TimeGroup.getGroupKeys(dateFrom, dateTo, TimeGroup.NONE);
	Assert.assertEquals(0, titles.size());

	
	
	

	
    }


}
