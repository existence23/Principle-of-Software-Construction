package edu.cmu.cs.cs214.hw2.operator;

import java.math.BigDecimal;

/**
 * divisionOperator - an arithmetic division operator class implements BinaryOperator.
 * 
 * @author kaige
 */
public class DivisionOperator implements BinaryOperator{

	@Override
	public double apply(double arg1, double arg2) {
		final int len = 10;
		BigDecimal a1 = new BigDecimal(Double.toString(arg1));
		BigDecimal a2 = new BigDecimal(Double.toString(arg2));
		return a1.divide(a2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

}
