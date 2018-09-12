package edu.cmu.cs.cs214.hw6;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class to represent the similarity features of one single string.
 */
public class Similarity {

    /**
     * private double member to calculate the euclidean norm.
     */
    private double euclideanNorm;
    /**
     * private Map member to store the data.
     */
    private Map<String, Integer> hash;

    /**
     * constructor when argument type is String. use hashMap to hold the content.
     *
     * @param content is the input String needed to be added into the hashMap
     */
    public Similarity(List<String> content) {

        euclideanNorm = 0.0;
        hash = new HashMap<String, Integer>();
        if (content.size() == 0) {
            return;
        }
        for(String line : content){
            String[] words = line.split("\\W");
            for(String word : words){
                if(word != null && word.length() > 0){
                    word = word.toLowerCase();
                    if (hash.containsKey(word)) {
                        hash.put(word, new Integer(hash.get(word) + 1));
                    } else {
                        hash.put(word, new Integer(1));
                    }
                }
            }
        }

        Iterator<Map.Entry<String, Integer>> iter = hash.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Integer> entry = iter.next();
            euclideanNorm = euclideanNorm + new Integer(entry.getValue() * entry.getValue()).doubleValue();
        }

        euclideanNorm = Math.sqrt(euclideanNorm);
    }

    /**
     * function to calculate the euclidean norm of the map.
     *
     * @return the calculated euclidean norm of the map
     */
    public double euclideanNorm() {
        double norm = euclideanNorm;
        return norm;
    }

    /**
     * function to calculate the dot product between the hash Map and the given Map.
     *
     * @param map is the given Map
     * @return the calculated double dot product
     */
    public double dotProduct(Map<String, Integer> map) {
        if (map == null || map.isEmpty() || hash.isEmpty()) {
            return 0.0;
        }

        double dot = 0.0;
        Iterator<Map.Entry<String, Integer>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Integer> entry = iter.next();
            if (hash.containsKey(entry.getKey())) {
                dot = dot + new Integer(hash.get(entry.getKey()) * entry.getValue()).doubleValue();
            }
        }
        return dot;
    }

    /**
     * function to calculate the norm of the given Map.
     *
     * @param map is the given Map needed to calculate the norm
     * @return the norm of the given Map
     */
    private double calculateNorm(Map<String, Integer> map) {
        if (map == null || map.isEmpty()) {
            return 0.0;
        }
        double norm = 0.0;
        Iterator<Map.Entry<String, Integer>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Integer> entry = iter.next();
            norm += new Integer(entry.getValue() * entry.getValue()).doubleValue();
        }
        return Math.sqrt(norm);
    }

    /**
     * help function to decide whether the given Map is same with the hash Map.
     *
     * @param map is the given Map needed to be compared
     * @return true if these two Maps is the same, false if not
     */
    private boolean compareMap(Map<String, Integer> map) {

        if (map == null) {
            return false;
        }

        if (hash.size() == 0 && map.size() == 0) {
            return false;
        }

        Iterator<Map.Entry<String, Integer>> iter1 = hash.entrySet().iterator();
        while (iter1.hasNext()) {
            Map.Entry<String, Integer> entry1 = iter1.next();
            Integer value1 = entry1.getValue();
            Integer value2 = map.get(entry1.getKey());

            if (!value1.equals(value2)) {
                return false;
            }
        }
        return true;

    }

    /**
     * function to calculate the distance between the hash Map with the given Map.
     * @param map is the given Map needed to be compared
     * @return the double distance calculated
     */
    public double distance(Map<String, Integer> map) {
        if (map == null || map.isEmpty() || hash.isEmpty()) {
            return 0.0;
        }

        if (compareMap(map)) {
            return 1.0;
        }

        double distance = 0;
        distance = dotProduct(map) / (euclideanNorm() * calculateNorm(map));
        return distance;
    }

    /**
     * function to return the Map, which have recorded all the input String elements.
     * @return the built Map
     */
    public Map<String, Integer> getMap() {
        Map<String, Integer> map = new HashMap<String, Integer>(hash);
        return map;
    }
}
