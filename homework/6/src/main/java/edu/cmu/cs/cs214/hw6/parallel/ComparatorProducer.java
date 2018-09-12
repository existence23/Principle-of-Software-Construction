package edu.cmu.cs.cs214.hw6.parallel;

import edu.cmu.cs.cs214.hw6.CommitPair;
import edu.cmu.cs.cs214.hw6.RevisionPair;

import java.util.List;
import java.util.Queue;

/**
 * The Producer of the ParallelRepoComparator.
 */
public class ComparatorProducer implements Runnable {
    private static int index = 0;
    private int size2;

    Queue<RevisionPair> results;
    int size;
    List<CommitPair> commitPairs1;
    List<CommitPair> commitPairs2;

    public ComparatorProducer(final Queue<RevisionPair> results,
                              List<CommitPair> commitPairs1, List<CommitPair> commitPairs2){
        this.results = results;
        this.commitPairs1 = commitPairs1;
        this.commitPairs2 = commitPairs2;
        size = commitPairs1.size() * commitPairs2.size();
        size2 = commitPairs2.size();
    }

    @Override
    public void run() {
        synchronized (this){
            while(index < commitPairs1.size() * commitPairs2.size()){
                int index1 = index / size2;
                int index2 = index % size2;
                CommitPair pair1 = commitPairs1.get(index1);
                CommitPair pair2 = commitPairs2.get(index2);
                produce(new RevisionPair(pair1, pair2));
                index++;
            }
        }

    }

    /**
     * Function to produce to build the PriorityQueue according to the similarity of each revision pair.
     * Add one new built revision pair to the PriorityQueue each time
     * @param pair is the given new built revision pair
     */
    public void produce(RevisionPair pair){
        synchronized (results){
//            System.out.println("produce "+ index + "Similarity = " + pair.getDistance());
            results.add(pair);
            results.notifyAll();
        }
    }
}
