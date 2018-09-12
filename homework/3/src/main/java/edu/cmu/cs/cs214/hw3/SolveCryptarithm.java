package edu.cmu.cs.cs214.hw3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Main function to solving given cryptarithms.
 */
public class SolveCryptarithm {
	/**
	 * Solving given cryptarithms.
	 * @param args is the given string array style of expression
	 * @throws Exception if the given expression is invalid
	 */
	public static void main(String[] args) throws Exception {
		
		CryptarithmSolver solver = new CryptarithmSolver(args);
		List<HashMap<Character, Integer>> list = solver.getAllSolu();
		System.out.println(list.size() + " solution(s):");
		int letterNum = solver.getNumber();
		
		for(int i = 0; i < list.size(); i++) {
			System.out.print("{");
			Iterator iter = list.get(i).entrySet().iterator();
			int m = 0;
			while(iter.hasNext()) {
				m += 1;
				Map.Entry<Character, Integer> entry = (Entry<Character, Integer>) iter.next();
				System.out.print(entry.getKey() + "=" + entry.getValue());
				if(m != letterNum) System.out.print(", ");
			}
			System.out.println("}");
		}
	}
}
