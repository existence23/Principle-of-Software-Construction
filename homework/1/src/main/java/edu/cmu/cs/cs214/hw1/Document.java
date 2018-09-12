package edu.cmu.cs.cs214.hw1;


import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;


/**
 * 17-214 Homework 1.
 * 
 * @author Kai Ge
 * 
 *         Andrew ID: kge
 */
public class Document {
	/**
	 * private String member to record the URL address.
	 */
	private String urlString;
	/**
	 * private Map member to store the data.
	 */
	private Map<String, BigInteger> hash;
	/**
	 * private double member to calculate the euclidean norm.
	 */
	private double euclideanNorm;

	/**
	 * private help function to decide whether the input string is valid or not.
	 * 
	 * @param text
	 *            is the input String
	 * @return true if the file is empty, false if not
	 */
	private boolean isValidWord(String text) {
		return text.matches("[a-z,A-Z]+");
	}

	/**
	 * Constructor of class Document, use hashMap to hold the content.
	 * 
	 * @param urlString
	 *            is the URL address of the new Document
	 * @throws MalformedURLException
	 *             throw exception
	 * @throws IOException
	 *             throw exception
	 */
	public Document(String urlString) throws MalformedURLException, IOException {
		this.urlString = urlString;
		Scanner sc = null;
		euclideanNorm = 0.0;
		hash = new HashMap<String, BigInteger>();
		try {
			sc = new Scanner(new URL(urlString).openStream());
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] words = line.split("\\W");

				for (String word : words) {
					if (word != null && isValidWord(word)) {
						word = word.toLowerCase();
						if (hash.containsKey(word)) {
							hash.put(word, hash.get(word).add(new BigInteger("1")));
						} else {
							hash.put(word, new BigInteger("1"));
						}
					}
				}
			}
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
		Iterator<Map.Entry<String, BigInteger>> iter = hash.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, BigInteger> entry = iter.next();
			euclideanNorm = euclideanNorm + (entry.getValue().multiply(entry.getValue())).doubleValue();
		}
		euclideanNorm = Math.sqrt(euclideanNorm);

	}

	/**
	 * function to return the Map, which have recorded all the input String
	 * elements.
	 * 
	 * @return the built Map
	 */
	public Map<String, BigInteger> getMap() {
		Map<String, BigInteger> map = new HashMap<String, BigInteger>(hash);
		return map;
	}

	/**
	 * function to calculate the euclidean norm of the map.
	 * 
	 * @return the calculated euclidean norm
	 */
	public double euclideanNorm() {
		return euclideanNorm;
	}

	/**
	 * function to calculate the dot product between the hash Map and the given Map.
	 * 
	 * @param map
	 *            is the given Map
	 * @return the calculated double dot product
	 */
	public double dotProduct(Map<String, BigInteger> map) {
		if (map == null || map.isEmpty() || hash.isEmpty()) {
			return 0.0;
		}
		double dot = 0.0;
		Iterator<Map.Entry<String, BigInteger>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, BigInteger> entry = iter.next();
			if (hash.containsKey(entry.getKey())) {
				dot = dot + (hash.get(entry.getKey()).multiply(entry.getValue())).doubleValue();
			}
		}
		return dot;
	}

	/**
	 * function to calculate the norm of the given Map.
	 * 
	 * @param map
	 *            is the given Map needed to calculate the norm
	 * @return the norm of the given Map
	 */
	private double calculateNorm(Map<String, BigInteger> map) {
		if (map == null || map.isEmpty()) {
			return 0.0;
		}

		double norm = 0.0;
		Iterator<Map.Entry<String, BigInteger>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, BigInteger> entry = iter.next();
			norm += (entry.getValue().multiply(entry.getValue())).doubleValue();
		}
		return Math.sqrt(norm);
	}

	/**
	 * function to calculate the distance between this Document with the given
	 * Document.
	 * 
	 * @param newUrlString
	 *            is the given Document
	 * @return the double distance calculated
	 */
	public double distance(Document newUrlString) {
		Map<String, BigInteger> map = newUrlString.getMap();
		if (map == null || map.isEmpty() || hash.isEmpty()) {
			return Math.acos(0.0);
		}

		double distance = 0;
		distance = dotProduct(map) / (euclideanNorm() * calculateNorm(map));
		distance = Math.acos(distance);
		return distance;
	}

	@Override
	public String toString() {
		return urlString.toString();
	}
}
