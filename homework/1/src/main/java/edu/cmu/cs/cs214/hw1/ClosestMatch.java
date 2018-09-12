package edu.cmu.cs.cs214.hw1;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * 17-214 Homework 1.
 * 
 * @author Kai Ge
 * 
 * Andrew ID: kge
 */
public class ClosestMatch {
	/**
	 * main function to find the closest match in a set of documents.
	 * @param args is the address of these of documents
	 * @throws MalformedURLException throw exception during scanning
	 * @throws IOException throw exception scanning
	 */
	public static void main(String[] args) throws MalformedURLException, IOException {
		String a = "";
		String b = "";
		double minDistance = Integer.MAX_VALUE;
		for(int i =0; i < args.length - 1; i++) {
			Document docu1 = new Document(args[i]);
			for(int j = i + 1; j < args.length; j++) {
				Document docu2 = new Document(args[j]);
				double currDistance = docu1.distance(docu2);
				if(currDistance < minDistance) {
					minDistance = currDistance;
					a = args[i];
					b = args[j];
				}
			}
		}
		System.out.println("The two related article is" + "\n" + a + "\n" + b);
		Document test1 = new Document(args[0]);
		System.out.println(test1.distance(new Document(args[1])));
	}
}