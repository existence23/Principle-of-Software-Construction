package edu.cmu.cs.cs214.hw2.zerofinder;

import java.math.BigDecimal;

import edu.cmu.cs.cs214.hw2.expression.DerivativeExpression;
import edu.cmu.cs.cs214.hw2.expression.Expression;
import edu.cmu.cs.cs214.hw2.expression.VariableExpression;
import edu.cmu.cs.cs214.hw2.operator.AbsoluteOperator;
import edu.cmu.cs.cs214.hw2.operator.DivisionOperator;
import edu.cmu.cs.cs214.hw2.operator.SubtractionOperator;

/**
 * Finds zeros of arbitrary functions using Newton's method.
 */
public class ZeroFinder {
	/**
	 * function to calculate the zero of the given expression which is near to the given estimated zero.
	 * @param fn is the given function
	 * @param x is the variable expression of the given function
	 * @param approxZero is the given approximated zero
	 * @param tolerance is the tolerance of the zero we find
	 * @return the found zero of this function within tolerance
	 * @throws Exception 
	 */
	public static double zero(Expression fn, VariableExpression x,
			 double approxZero, double tolerance) throws Exception {
		x.store(approxZero);
		
		DivisionOperator division = new DivisionOperator();
		SubtractionOperator subtraction = new SubtractionOperator();
		AbsoluteOperator absolute = new AbsoluteOperator();
		
		double data1 = fn.eval();
		
		int count = 0;
		while(absolute.apply(data1) > tolerance) {
			if(count >= 10000) {
				throw new Exception("There is no zero of this function");
			}
			DerivativeExpression der = new DerivativeExpression(fn, x);
			double derVal = der.eval();
			double div = division.apply(fn.eval(), derVal);
			double newValX = subtraction.apply(x.eval(), div);
			x.store(newValX);
			data1 = fn.eval();
			count++;
		}
		return x.eval();
		
	}
}
