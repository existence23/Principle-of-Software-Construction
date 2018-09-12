package edu.cmu.cs.cs214.hw3;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Subclass to implement the customizable parts of the abstract class.
 * @param <T> the generic type
 */
public class TemplatePermutationGenerator<T> extends PermutationGenerator {
	
	/**
	 * Constructor of this class, which inherit from the abstract directly.
	 * @param arr is the given array need to generate permutations
	 */
	public TemplatePermutationGenerator(T[] arr) {
		super(arr);
	}

	/**
	 * Use heap algorithm here to generate the permutation.
	 */
	@Override
	void permutate(int n) {
		if(n <= 0) return;
		if(n == 1) {
			ArrayList<T> addList = new ArrayList<T>();
			for(int i = 0; i < arr.length; i++) {
				addList.add((T)arr[i]);
			}
			per.add(addList);
		}
		else {
			for(int i = 0; i < n - 1; i++) {
				permutate(n - 1);
				if(n % 2 == 0) {
					swap((T[]) arr, i , n - 1);
				}else {
					swap((T[]) arr, 0, n - 1);
				}
			}
			permutate(n - 1);
		}
		
	}
	
	/**
	 * Help function to swap two specific elements in given array.
	 * @param arr - the given array
	 * @param m the position of the element to be swapped
	 * @param n the position of the element to be swapped
	 */
	private void swap(T[] arr, int m, int n) {
		T temp = arr[m];
		arr[m] = arr[n];
		arr[n] = temp;
	}
}
