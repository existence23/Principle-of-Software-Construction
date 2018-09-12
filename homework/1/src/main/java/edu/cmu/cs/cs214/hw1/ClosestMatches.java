package edu.cmu.cs.cs214.hw1;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * 17-214 Homework 1.
 * 
 * @author Kai Ge
 * 
 * Andrew ID: kge
 *
 */
public class ClosestMatches {
	/**
	 * main function to find the closest match to each document in a set.
	 * @param args is the address of these of documents
	 * @throws MalformedURLException throw exception during scanning
	 * @throws IOException throw exception during scanning
	 */
	public static void main(String[] args) throws MalformedURLException, IOException {
		double[][] distance = new double[args.length][args.length];
		for(int i = 0; i < args.length - 1; i++) {
			Document docu1 = new Document(args[i]);
			for(int j = i + 1; j < args.length; j++) {
				Document docu2 = new Document(args[j]);
				distance[i][j] = docu1.distance(docu2);
				distance[j][i] = distance[i][j];
			}
		}
		for(int i = 0; i < args.length; i++) {
			System.out.print(args[i] + " BEST MATCH WITH ");
			String min = args[0];
			double minDistance = Integer.MAX_VALUE;
			for(int j = 0; j < args.length; j++) {
				if(j == i) continue;
				if(distance[i][j] < minDistance) {
					minDistance = distance[i][j];
					min = args[j];
				}
			}
			System.out.println(min);
		}
	}
}
