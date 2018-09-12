package edu.cmu.cs.cs214.hw2.expression;

import edu.cmu.cs.cs214.hw2.operator.AbsoluteOperator;
import edu.cmu.cs.cs214.hw2.operator.AdditionOperator;
import edu.cmu.cs.cs214.hw2.operator.DivisionOperator;
import edu.cmu.cs.cs214.hw2.operator.ExponentiationOperator;
import edu.cmu.cs.cs214.hw2.operator.MultiplicationOperator;
import edu.cmu.cs.cs214.hw2.operator.NegationOperator;
import edu.cmu.cs.cs214.hw2.operator.SubtractionOperator;

/**
 * An OperationExpression class implements the Expression interface.
 * build the OperationExpression based on one or two subExpression and one operator
 * @author Kai Ge
 *
 */
public class OperationExpression implements Expression {
	
	//private Expression 01 to store the given Expression.
	private Expression arg1;
	//private Expression 02 to store the given Expression.
	private Expression arg2;
	//private String to store the given operator.
	private String operator;
	
	/**
	 * Constructor of OperationExpression when have only one Expression argument with the operator.
	 * 
	 * @param arg1 the given subExpression
	 * @param operator the given operator
	 */
	public OperationExpression(Expression arg1, String operator) {
		this.arg1 = arg1;
		this.arg2 = null;
		this.operator = operator;
	}
	
	/**
	 * Constructor of OperationExpression when have two Expression arguments with the operator.
	 * 
	 * @param arg1 the given subExpression 01
	 * @param arg2 the given subExpression 02
	 * @param operator the given operator
	 */
	public OperationExpression(Expression arg1, Expression arg2, String operator) {
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.operator = operator;
	}
	
	@Override
	public double eval() {
		if(operator.equals("+")) {
			AdditionOperator sum = new AdditionOperator();
			return sum.apply(arg1.eval(), arg2.eval());
		}
		if(operator.equals("-")) {
			if(arg2 != null) {
				SubtractionOperator sub = new SubtractionOperator();
			    return sub.apply(arg1.eval(), arg2.eval());
			}else {
				NegationOperator nega = new NegationOperator();
				return nega.apply(arg1.eval());
			}
			
		}
		if(operator.equals("*")) {
			MultiplicationOperator mul = new MultiplicationOperator();
			return mul.apply(arg1.eval(), arg2.eval());
		}
		if(operator.equals("/")) {
			DivisionOperator div = new DivisionOperator();
			return div.apply(arg1.eval(), arg2.eval());
		}
		if(operator.equals("^")) {
			ExponentiationOperator expo = new ExponentiationOperator();
			return expo.apply(arg1.eval(), arg2.eval());
		}
		if(operator.equals("abs")) {
			AbsoluteOperator abs = new AbsoluteOperator();
			return abs.apply(arg1.eval());
		}
		return 0.0;
	}


	@Override
	public String toString() {
		if(operator == "abs") {
			return "abs(" + arg1.toString() + ")";
		}
		else if(operator == "-" && arg2 == null) {
			return operator + arg1.toString();
		}
		else {
			return "(" + arg1.toString() + operator + arg2.toString() + ")";
		}
	}
}
