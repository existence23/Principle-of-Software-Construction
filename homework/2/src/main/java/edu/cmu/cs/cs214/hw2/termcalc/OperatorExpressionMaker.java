package edu.cmu.cs.cs214.hw2.termcalc;

import edu.cmu.cs.cs214.hw2.expression.Expression;
import edu.cmu.cs.cs214.hw2.expression.NumberExpression;
import edu.cmu.cs.cs214.hw2.expression.OperationExpression;

/**
 * OperatorExpressionMaker - an operator expression maker implements ExpressionMaker interface.
 * 
 * @author kaige
 */
public class OperatorExpressionMaker implements ExpressionMaker {

	@Override
	public Expression sumExpression(Expression addend1, Expression addend2) {
		return new OperationExpression(addend1, addend2, "+");
	}

	@Override
	public Expression differenceExpression(Expression op1, Expression op2) {
		return new OperationExpression(op1, op2, "-");
	}

	@Override
	public Expression productExpression(Expression factor1, Expression factor2) {
		return new OperationExpression(factor1, factor2, "*");
	}

	@Override
	public Expression divisionExpression(Expression dividend, Expression divisor) {
		return new OperationExpression(dividend, divisor, "/");
	}

	@Override
	public Expression exponentiationExpression(Expression base, Expression exponent) {
		return new OperationExpression(base, exponent, "^");
	}

	@Override
	public Expression negationExpression(Expression operand) {
		return new OperationExpression(operand, "-");
	}

	@Override
	public Expression absoluteValueExpression(Expression value) {
		return new OperationExpression(value, "abs");
	}

	@Override
	public Expression numberExpression(double value) {
		return new NumberExpression(value);
	}
}
