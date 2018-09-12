package edu.cmu.cs.cs214.hw3;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Main function to genrate anagrams.
 * @author Kai Ge
 */
public class GenerateAnagrams {
	/**
	 * Generate anagrams.
	 * @param args the first string in args is the name of dictionary file
	 * and the left are the string inputs needed to generate anagrams
	 * which are rowdy, silent and zghq here
	 * @throws Exception if the input string array is invalid
	 */
	public static void main(String[] args) throws Exception {
		AnagramGenerator generator = new AnagramGenerator(args);
		generator.generateAnagram();
	}
}
