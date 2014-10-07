package ch.tkayser.budget.test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BudgetBaseTest {

    // a logger
    protected Logger m_log = LoggerFactory.getLogger(getClass());

    protected Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

    protected BigDecimal toBigDecimal(String amount) {
        BigDecimal result = new BigDecimal(amount);
        return result.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}
