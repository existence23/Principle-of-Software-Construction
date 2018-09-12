package edu.cmu.cs.cs214.hw2.operator;

/**
 * exponentiationOperator - an arithmetic exponentiation operator class implements BinaryOperator.
 * 
 * @author kaige
 */
public class ExponentiationOperator implements BinaryOperator {

	@Override
	public double apply(double arg1, double arg2) {
		return Math.pow(arg1, arg2);
	}
}
