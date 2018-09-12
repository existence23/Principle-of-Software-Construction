package edu.cmu.cs.cs214.hw3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class to find whether there exists solution to the cryptarithm.
 */
public class CryptarithmSolver {
	/**
	 * max distince integer number.
	 */
	private final int maxDistinctInteger = 10;
	/**
	 * private String[] to store the given expression.
	 */
	private String[] expression;
	/**
	 * private int to store the distinct character number of the input expression.
	 */
	private int letterNum;
	/**
	 * private list to store the distict characters of the input expression.
	 */
	private List<Character> list;
	/**
	 * private list to store all kinds of numberPermutation;
	 */
	private ArrayList<ArrayList<Integer>> numberPermutation;

	/**
	 * Constructor of this class.
	 * In which checks the distinct size of characters of the input expression and check the expression is valid or not
	 * and generate all kinds of permutation of 10 integers, which is used to calculate all possible value of the left
	 * expression and right expression
	 * @param expression is the given expression
	 */
	public CryptarithmSolver(String[] expression) {
		this.expression = expression;
		list = new ArrayList();
		for (int i = 0; i < expression.length; i++) {
			if (!expression[i].equals("+") && !expression[i].equals("-") && !expression[i].equals("*")
					&& !expression[i].equals("=")) {
				for (int j = 0; j < expression[i].length(); j++) {
					if (!list.contains(expression[i].charAt(j)))
						list.add(expression[i].charAt(j));
				}
			}
		}
		letterNum = list.size();

		Integer[] ascending = new Integer[maxDistinctInteger];
		for (int i = 0; i < maxDistinctInteger; i++) {
			ascending[i] = i;
		}
		TemplatePermutationGenerator<Integer> gen = new TemplatePermutationGenerator<Integer>(ascending);
		numberPermutation = gen.getPermutation();
	}

	/**
	 * HashMap to store the solution of this cryptarithm.
	 * @param numSolu is the given permutation of the 10 integer
	 * @return the relation between the character and the integer, return null if this permutation is not the solution
	 * @throws Exception if expression is invalid
	 */
	public HashMap<Character, Integer> solu(List<Integer> numSolu) throws Exception {
		Cryptarithm cryptarithm = new Cryptarithm(expression, numSolu);
		ArrayList<String> left = cryptarithm.getLeftExp();
		ArrayList<String> right = cryptarithm.getRightExp();
		int leftResult = cryptarithm.expressionResult(left);
		int rightResult = cryptarithm.expressionResult(right);
		if (leftResult == rightResult) {
			HashMap<Character, Integer> map = new HashMap();
			for (int i = 0; i < list.size(); i++) {
				map.put(list.get(i), numSolu.get(i));
			}
			for (int i = 0; i < expression.length; i++) {
				if (map.get(expression[i].charAt(0)) != null && map.get(expression[i].charAt(0)) == 0)
					return null;
			}
			return map;
		}
		return null;
	}

	/**
	 * Private help function to help to test two list is equal to each other or not.
	 * @param a is one of the given list
	 * @param b is the other one of the given list
	 * @return true if these two lists are equal to each other, false if not
	 */
	private boolean compareList(List<Integer> a, List<Integer> b) {
		if (a.size() != b.size())
			return false;
		for (int i = 0; i < a.size(); i++) {
			if (!a.get(i).equals(b.get(i)))
				return false;
		}
		return true;
	}

	/**
	 * Funtion to find all the solutions of the given Cryptarithm.
	 * @return all the solutions in List, and the element of each list is HashMap, which stores the
	 * relationship between each character and integer
	 * @throws Exception if the input expression is invalid
	 */
	public List<HashMap<Character, Integer>> getAllSolu() throws Exception {
		List<HashMap<Character, Integer>> allSolu = new ArrayList();
		List<Integer> forward = new ArrayList();
		for (int i = 0; i < numberPermutation.size(); i++) {
			int size = numberPermutation.get(i).size();
			List<Integer> subList = numberPermutation.get(i).subList(size - letterNum, size);
			if (!compareList(subList, forward)) {

				forward = subList;
				HashMap<Character, Integer> map = solu(subList);
				if (map != null) allSolu.add(map);
			}
		}
		return allSolu;
	}

	/**
	 * function to get the distinct letter number of the given expression.
	 * @return distict letter number
	 */
	public int getNumber() {
		return letterNum;
	}

}
