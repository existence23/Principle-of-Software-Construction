package edu.cmu.cs.cs214.hw2.operator;

import java.math.BigDecimal;

/**
 * additionOperator - an arithmetic addition operator class implements BinaryOperator.
 * 
 * @author kaige
 */
public class AdditionOperator implements BinaryOperator{
	@Override
	public double apply(double arg1, double arg2) {
		BigDecimal a1 = new BigDecimal(Double.toString(arg1));
		BigDecimal a2 = new BigDecimal(Double.toString(arg2));
		return a1.add(a2).doubleValue();
	}
	
}
