package edu.cmu.cs.cs214.hw2.operator;

/**
 * absoluteOperator - an arithmetic absolute operator class implements UnaryOperator.
 * 
 * @author kaige
 */
public class AbsoluteOperator implements UnaryOperator {
	@Override
	public double apply(double arg) {
		// TODO Auto-generated method stub
		return Math.abs(arg);
	}
}
