package ch.tkayser.budget.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BudgetUtil {

	// the rounding mode
	private final static RoundingMode S_ROUNDING_MODE = RoundingMode.HALF_UP;
	
	// the scale
	private  final static int S_SCALE = 2;
	
	/**
	 * set the scalen on a big decimal
	 * 
	 * @param nr
	 * @return
	 */
	public static BigDecimal setScale(BigDecimal nr) {
		return nr.setScale(S_SCALE, S_ROUNDING_MODE);
	}

	/**
	 * divide a bigdecimal by another one
	 * 
	 * @param nr
	 * @param divisor
	 * @return
	 */
	public static BigDecimal divide(BigDecimal nr, BigDecimal divisor) {
		return nr.divide(divisor, S_SCALE, S_ROUNDING_MODE);
	}

	/**
	 * multiply a bigdecimal by another one
	 * 
	 * @param nr
	 * @param divisor
	 * @return
	 */
	public static BigDecimal multiply(BigDecimal nr, BigDecimal factor) {
		return setScale(nr.multiply(factor));
	}

	/**
	 * subtract a bigdecimal by another one
	 * 
	 * @param nr
	 * @param divisor
	 * @return
	 */
	public static BigDecimal subtract(BigDecimal nr, BigDecimal subtrahend) {
		return setScale(nr.subtract(subtrahend));
	}

}
