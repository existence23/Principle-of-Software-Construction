package edu.cmu.cs.cs214.hw3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.cmu.cs.cs214.hw2.expression.VariableExpression;

/**
 * Class to tackle the input String array into expression.
 */
public class Cryptarithm {

	private String[] expression;
	private ArrayList<String> leftExp;
	private ArrayList<String> rightExp;
	private ArrayList<Character> list;
	private HashMap<Character, Integer> map;
	private final int maxDistinctInteger = 10;

	/**
	 * Constructor of this class.
	 * @param expression is the input String array
	 * @param numSolu is one of the permutation of all of ten different characters
	 */
	public Cryptarithm(String[] expression, List<Integer> numSolu) {
		this.expression = expression;
		check(expression);
		map = new HashMap<Character, Integer>();
		setHashMap(numSolu);
	}

	/**
	 * Help function to check the input String array can be transform to a valid expression or not.
	 * If the expression is valid, construct the left String array and teh right String array
	 * which store the expression at the left of the equals operator and the expression at the right part
	 * of the equals operator separately
	 * @param expression is the input String array
	 */
	public void check(String[] expression) {
		boolean containsEqual = false;
		boolean isLeft = true;
		list = new ArrayList<Character>();
		leftExp = new ArrayList<String>();
		rightExp = new ArrayList<String>();
		for (int i = 0; i < expression.length; i++) {
			if (expression[i].equals("=")) {
				containsEqual = true;
				isLeft = false;
				continue;
			}else if (!expression[i].equals("+") && !expression[i].equals("-") && !expression[i].equals("*")){
				for (int j = 0; j < expression[i].length(); j++) {
					char c = expression[i].toUpperCase().charAt(j);
					if (Character.isAlphabetic(c)) {
						if (!list.contains(c))
							list.add(c);
					} else {
						throw new IllegalArgumentException("The input expression is invalid.");
					}
				}
			}
			if (isLeft)
				leftExp.add(expression[i]);
			else
				rightExp.add(expression[i]);
		}
		if (!containsEqual || list.size() > maxDistinctInteger) {
			throw new IllegalArgumentException("The input expression is invalid.");
		}
	}

	/**
	 * Set a HashMap to store the given permutation with characters.
	 * @param numSolu is one of the permutation of 10 Integers
	 */
	public void setHashMap(List<Integer> numSolu) {
		for (int i = 0; i < list.size(); i++) {
			map.put(list.get(i), numSolu.get(i));
		}
	}

	/**
	 * ArrayList to store the left Strings expression.
	 * @return the left expression
	 */
	public ArrayList<String> getLeftExp() {
		return leftExp;
	}

	/**
	 * ArrayList to store the right Strings expression.
	 * @return the right expression
	 */
	public ArrayList<String> getRightExp() {
		return rightExp;
	}

	/**
	 * Set the variable of given String.
	 * @param variableExp is the given string
	 * @return a VariableExpression and value has been stored based on the given permutation
	 */
	public VariableExpression setVariable(String variableExp) {
		VariableExpression var = new VariableExpression(variableExp);
		int value = 0;
		for (int i = 0; i < variableExp.length(); i++) {
			char c = variableExp.charAt(i);
			value = value * maxDistinctInteger + map.get(c);
		}
		var.store(value);
		return var;
	}

	/**
	 * Calculate the expression result of whichever left expression or right expression.
	 * @param exp is the given expression
	 * @return the calculated result of the expression
	 */
	public int expressionResult(ArrayList<String> exp) {
		if (exp.size() <= 0)
			return 0;
		int result = 0;
		result += setVariable(exp.get(0)).eval();
		for (int i = 2; i < exp.size(); i += 2) {
			if (exp.get(i - 1).equals("+")) {
				result += setVariable(exp.get(i)).eval();
			} else if (exp.get(i - 1).equals("-")) {
				result -= setVariable(exp.get(i)).eval();
			} else if (exp.get(i - 1).equals("*")) {
				result *= setVariable(exp.get(i)).eval();
			}
		}
		return result;
	}
}
