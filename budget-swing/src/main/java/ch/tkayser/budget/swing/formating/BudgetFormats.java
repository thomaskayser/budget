package ch.tkayser.budget.swing.formating;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.text.DefaultFormatterFactory;

public class BudgetFormats {

    // date formatstrings / formats
    public static final String DATE_FORMAT_STRING = "dd.MM.yyyy";

    public static DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STRING);

    // number formatstrings / formats
    public static final String NUMBER_DISPLAY_FORMATSTRING = "###,###,##0.00";

    public static final String NUMBER_EDIT_FORMATSTRING = "###,###,##0.00";

    public static NumberFormat BIGDECIMAL_DISPLAY_FORMAT = getBigDecimalFormat(NUMBER_DISPLAY_FORMATSTRING);

    public static NumberFormat BIGDECIMAL_EDIT_FORMAT = getBigDecimalFormat(NUMBER_EDIT_FORMATSTRING);

    /**
     * BigDecimalFormat erstellen
     * 
     * @param formatString
     * @return
     */
    private static NumberFormat getBigDecimalFormat(String formatString) {
        DecimalFormat format = new DecimalFormat(formatString);
        format.setParseBigDecimal(true);
        return format;
    }

    /**
     * get a formatter Factory for JFormattedTextFields for Date Fields
     * 
     * @return
     */
    public static AbstractFormatterFactory getDateFormatterFactory() {
        return new DefaultFormatterFactory(new BudgetFormater(DATE_FORMAT), new BudgetFormater(DATE_FORMAT), new BudgetFormater(
                DATE_FORMAT));
    }

    /**
     * get a formatter Factory for JFormattedTextFields for Date Fields
     * 
     * @return
     */
    public static AbstractFormatterFactory getBigDecimalFormatterFactory() {
        return new DefaultFormatterFactory(new BudgetFormater(BIGDECIMAL_DISPLAY_FORMAT), new BudgetFormater(BIGDECIMAL_DISPLAY_FORMAT),
                new BudgetFormater(BIGDECIMAL_EDIT_FORMAT));
    }

}
