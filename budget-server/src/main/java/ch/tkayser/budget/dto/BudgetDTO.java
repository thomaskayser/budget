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
import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import ch.tkayser.budget.base.dto.BaseDTO;
import ch.tkayser.budget.util.BudgetUtil;

public class BudgetDTO extends BaseDTO  {

    private static final long serialVersionUID = 3326591308393372510L;

    //@formatter:off
    public static final String PROP_NAME             = "name";
    public static final String PROP_MONITOR_DATE     = "monitorDate";
    public static final String PROP_AMOUNT_PER_MONTH = "amountPerMonth";
    public static final String PROP_AMOUNT_PER_DAY   = "amountPerDay";
    public static final String PROP_AMOUNT_REMAINING = "amountRemaining";
    public static final String PROP_AMOUNT_UPTODAY   = "amountUpToDay";
    //@formatter:on

    // id of the budget
    private Long id;

    // name of the budget
    @NotNull
    @Length(min=1, max=255)
    private String name;

    // the version
    private Integer version;

    // monthly amount of the budget
    @NotNull
    private BigDecimal amountPerMonth;

    // date for the monitor calculation
    private Date monitorDate;

    // the amount up to day
    private BigDecimal amountPerDay;

    // the amount up to day
    private BigDecimal amountUpToDay;

    // the remaining amount for the month
    private BigDecimal amountRemaining;

    public BudgetDTO() {
        super();
        // init  amounts
        this.amountPerDay = new BigDecimal(0);
        this.amountUpToDay = new BigDecimal(0);
        this.amountRemaining = new BigDecimal(0);        
    }

    /**
     * calculate the budget amounts
     *
     */
    private void calculateBudgetAmounts() {

        // null amonter per month?
        if (getAmountPerMonth() == null) {
            setAmountPerDay(BudgetUtil.setScale(new  BigDecimal(0)));
            setAmountUpToDay(BudgetUtil.setScale(new  BigDecimal(0)));
            setAmountRemaining(BudgetUtil.setScale(new  BigDecimal(0)));
            
            return;
        }
        
        // init monitor date with current day if not set
        if (getMonitorDate() == null) {
            setMonitorDate(new Date());
        }
        // init calendar with the monitor date
        Calendar cal = Calendar.getInstance();
        cal.setTime(getMonitorDate());

        // get days in the month
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // get monitor day
        int monitorDay = cal.get(Calendar.DAY_OF_MONTH);

        // calculate the daily and current amount
        setAmountPerDay(BudgetUtil.divide(getAmountPerMonth(), new BigDecimal(daysInMonth)));
        setAmountUpToDay(BudgetUtil.multiply(getAmountPerDay(), new BigDecimal(monitorDay)));
        setAmountRemaining(BudgetUtil.subtract(getAmountPerMonth(), getAmountUpToDay()));

    }


    public BigDecimal getAmountPerDay() {
        return this.amountPerDay;
    }

    public BigDecimal getAmountPerMonth() {
        return this.amountPerMonth;
    }

    public BigDecimal getAmountRemaining() {
        return this.amountRemaining;
    }

    public BigDecimal getAmountUpToDay() {
        return this.amountUpToDay;
    }

    public Long getId() {
        return id;
    }

    public Date getMonitorDate() {
        return this.monitorDate;
    }

    public String getName() {
        return name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setAmountPerDay(BigDecimal amountPerDay) {
        BigDecimal oldAmount = this.amountPerDay;
        this.amountPerDay = amountPerDay;
        firePropertyChange(PROP_AMOUNT_PER_DAY, oldAmount, this.amountPerDay);
    }

    public void setAmountPerMonth(BigDecimal amountPerMonth) {
        BigDecimal oldAmount = this.amountPerMonth;
        this.amountPerMonth = amountPerMonth;
        calculateBudgetAmounts();
        firePropertyChange(PROP_AMOUNT_PER_MONTH, oldAmount, this.amountPerMonth);
    }

    public void setAmountRemaining(BigDecimal amountRemaining) {
        BigDecimal oldAmount = this.amountRemaining;
        this.amountRemaining = amountRemaining;
        firePropertyChange(PROP_AMOUNT_REMAINING, oldAmount, this.amountRemaining);
    }

    public void setAmountUpToDay(BigDecimal amountUpToDay) {
        BigDecimal oldAmount = this.amountUpToDay;
        this.amountUpToDay = amountUpToDay;
        firePropertyChange(PROP_AMOUNT_UPTODAY, oldAmount, this.amountUpToDay);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMonitorDate(Date monitorDate) {
        Date oldMonitorDate = this.monitorDate;
        this.monitorDate = monitorDate;
        calculateBudgetAmounts();
        firePropertyChange(PROP_MONITOR_DATE, oldMonitorDate, this.monitorDate);
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        firePropertyChange(PROP_NAME, oldName, this.name);
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "BudgetDTO [amountPerDay=" + amountPerDay + ", amountPerMonth=" + amountPerMonth + ", amountRemaining="
                + amountRemaining + ", amountUpToDay=" + amountUpToDay + ", id=" + id + ", monitorDate=" + monitorDate
                + ", name=" + name + "]";
    }
    
}
