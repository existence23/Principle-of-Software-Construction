package edu.cmu.cs.cs214.hw2.operator;

import java.math.BigDecimal;

/**
 * negationOperator - an arithmetic negation operator class implements UnaryOperator.
 * 
 * @author kaige
 */
public class NegationOperator implements UnaryOperator {

	@Override
	public double apply(double arg) {
		// TODO Auto-generated method stub
		BigDecimal a1 = new BigDecimal(Double.toString(0.0));
		BigDecimal a2 = new BigDecimal(Double.toString(arg));
		return a1.subtract(a2).doubleValue();
	}

}
