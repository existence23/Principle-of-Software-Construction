package edu.cmu.cs.cs214.hw3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Abstract class in template method to construct the permutation generator.
 * @param <T> is the generic type of the array needed to be permutated
 */
public abstract class PermutationGenerator<T> {
	protected int n;
	protected T[] arr;
	protected ArrayList<ArrayList<T>> per;
	protected String str;
	
	/**
	 * Constructor of permutationGenerator.
	 * @param arr is the input array to generate all the permutations
	 */
	public PermutationGenerator(T[] arr) {
		this.arr = arr;
		this.n = arr.length;
		this.per = new ArrayList<ArrayList<T>>();
	}
	
	/**
	 * Invariant function to get all of the permutation results of the given array.
	 * @return all of the permutation results
	 */
	public ArrayList<ArrayList<T>> getPermutation(){
		permutate(n);
		return per;
	}
	/**
	 * Abstract function to permutate the given array.
	 * @param n is the length of the input array
	 */
	abstract void permutate(int n);
	
}
