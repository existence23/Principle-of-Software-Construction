package edu.cmu.cs.cs214.hw6.parallel;

import edu.cmu.cs.cs214.hw6.RevisionPair;

import java.util.List;
import java.util.Queue;

/**
 * The Consumer of the ParallelRepoComparator.
 */
public class ComparatorConsumer implements Runnable{
    Queue<RevisionPair> results;
    List<Double> similarities;
    int n;
    boolean exit = false;
    public ComparatorConsumer(final Queue<RevisionPair> results, int n, List<Double> similarities){
        this.results = results;
        this.n = n;
        this.similarities = similarities;
    }

    @Override
    public void run() {
        synchronized (this){
            for(int i = 0; i < n; i++){
                if(results.size() == 0){
                    try {
                        throw new Exception("The input N is bigger than the total size!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                consume();
            }
        }
    }

    /**
     * Function to consume the built PriorityQueue.
     * Pick the revision pair which has the biggest similarity each time
     */
    public void consume(){
        synchronized (results){
            RevisionPair revisionPair = results.poll();
            System.out.println("child_hash_1: " + revisionPair.getPair1().getChildName() + "," + "parent_hash_1: " + revisionPair.getPair1().getParentName() + ";");
            System.out.println("child_hash_2: " + revisionPair.getPair2().getChildName() + "," + "parent_hash_2: " + revisionPair.getPair2().getParentName() + ";");
            System.out.println("Similarity: " + revisionPair.getDistance().toString());
            similarities.add(revisionPair.getDistance());
        }
    }
}
