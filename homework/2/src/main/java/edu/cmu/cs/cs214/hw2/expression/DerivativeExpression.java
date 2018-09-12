package edu.cmu.cs.cs214.hw2.expression;

import edu.cmu.cs.cs214.hw2.operator.AdditionOperator;
import edu.cmu.cs.cs214.hw2.operator.DivisionOperator;
import edu.cmu.cs.cs214.hw2.operator.SubtractionOperator;

/**
 * DerivativeExpression class which implements Expression interface to represent the derivative of the given function.
 * 
 * @author Kai Ge
 */
public class DerivativeExpression implements Expression {
	//private double data to store the value of the given function at f(x).
	private double funcVal;
	//private double data to store the value of the given function at f(x + deltaX).
	private double newFuncVal;

	private AdditionOperator addition;
	private SubtractionOperator subtraction;
	private DivisionOperator division;
	private final double deltaX = 1e-9;

	/**
	 * constructor of the DerivativeExpression class.
	 * 
	 * @param fn is the given function
	 * @param independentVar is the variable and the given function is based on this variable
	 * this variable stores the value, which should be used to find the derivative at the specific point of this function
	 * @throws Exception 
	 */
	public DerivativeExpression(Expression fn, VariableExpression independentVar) throws Exception {
		if(fn == null) {
			throw new IllegalArgumentException("Expression could not be null!");
		}
		funcVal = fn.eval();
		addition = new AdditionOperator();
		subtraction = new SubtractionOperator();
		division = new DivisionOperator();
		double newVar = addition.apply(independentVar.eval(), deltaX);
		independentVar.store(newVar);
		newFuncVal = fn.eval();
	}

	@Override
	public double eval() {
		double dividend = subtraction.apply(newFuncVal, funcVal);
		double divisor = deltaX;
		return division.apply(dividend, divisor);
	}

}
