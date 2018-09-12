package edu.cmu.cs.cs214.hw3;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Implement an anagram generator class to find of the permutations which are contained in
 * the given dictionary of all of the given strings.
 */
public class AnagramGenerator {
	private String fileName;
	private File file;
	private String[] arguments;
	private List<String> dictionary;
	
	/**
	 * Constructor of this class.
	 * The constructor build an list to store all of the valid words in dictionary and then use
	 * this list to check the permutation is valid or not
	 * @param arguments the first arguments is the name of the dictionary, which used to compare
	 * with the permutations of each string
	 * @throws Exception when the dictionary cannot be found or the input string array is invalid
	 */
	public AnagramGenerator(String[] arguments) throws Exception {
		if(arguments.length <= 0 || arguments == null) {
			throw new IllegalArgumentException("Missing String input.");
		}
		this.fileName = arguments[0];
		this.arguments = arguments;
		dictionary = new ArrayList<String>();
		File file = new File(fileName);
		Scanner scan = null;
		try {
			scan = new Scanner(file);
		}catch(Exception e) {
			throw new Exception("Cannot find file.");
		}
		while(scan.hasNext()) {
			dictionary.add(scan.next());
		}
	}
	
	/**
	 * Generate the anagram of single string.
	 * @param input is the input string
	 * @return the anagram of the input string
	 */
	public ArrayList<String> generateSingleAnagram(String input){
		Character[] ch = new Character[input.length()];
		for(int j = 0; j < ch.length; j++) {
			ch[j] =  new Character(input.charAt(j));
		}
		TemplatePermutationGenerator<Character> gen = 
				new TemplatePermutationGenerator<Character>(ch);
		ArrayList<ArrayList<Character>> permutate = gen.getPermutation();
		ArrayList<String> result = new ArrayList();
		for(List<Character> subList : permutate) {
			StringBuilder str = new StringBuilder();
			for(Character cha : subList) {
				str.append(cha);
			}
			if(!str.toString().equals(input) && dictionary.contains(str.toString())) {
				result.add(str.toString());
			}
		}
		return result;
	}
	
	/**
	 * Generate the anagram of the input string array.
	 * which is consists of the single anagram generates from the single string elements
	 * @return the anagram of the input string array
	 */
	public ArrayList<ArrayList<String>> generateAnagram() {
		ArrayList<ArrayList<String>> anagramResult = new ArrayList();
		for(int i = 1; i < arguments.length; i++) {
			ArrayList<String> result = generateSingleAnagram(arguments[i]);
			anagramResult.add(result);
			System.out.print(arguments[i] + ":" + " " + "[");
			for(int j = 0; j < result.size() - 1; j++) {
				System.out.print(result.get(j) + ", ");
			}
			if(result.size() > 0) {
				System.out.print(result.get(result.size() - 1));
			}
			System.out.println("]");
		}
		return anagramResult;
	}
}
