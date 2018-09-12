package edu.cmu.cs.cs214.hw6.parallel;

import edu.cmu.cs.cs214.hw6.CommitPair;
import edu.cmu.cs.cs214.hw6.RevisionPair;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.*;

/**
 * Class to load all the commit pairs and compare each other between the two repositories.
 */
public class ParallelRepoComparator {

    private String path1;
    private String path2;
    private ParallelRepoLoader loader1;
    private ParallelRepoLoader loader2;

    private List<CommitPair> commitPairs1;
    private List<CommitPair> commitPairs2;

    private List<Double> similarities;

    public ParallelRepoComparator(String path1, String path2) throws InterruptedException, GitAPIException, IOException {
        this.path1 = path1;
        this.path2 = path2;
        loader1 = new ParallelRepoLoader(path1);
        loader2 = new ParallelRepoLoader(path2);
        commitPairs1 = loader1.getCommitPairs();
        commitPairs2 = loader2.getCommitPairs();
        similarities = new ArrayList<>();
    }

    /**
     * Get the N most similar revision pairs between these two repositories.
     * @param n is the given number
     * @return the list of the similarities
     * @throws InterruptedException
     */
    public List<Double> getMostSimilarPairs(int n) throws InterruptedException {
        Queue<RevisionPair> results =
                new PriorityQueue<>(commitPairs1.size() * commitPairs2.size(), new Comparator<RevisionPair>() {
                    @Override
                    public int compare(RevisionPair o1, RevisionPair o2) {
                        return -(o1.getDistance().compareTo(o2.getDistance()));
                    }
                });

        final int producerThreadSize = 10;
        for(int i = 0; i < producerThreadSize; i++){
            Thread thread = new Thread(() ->{
                Runnable producer = new ComparatorProducer(results, commitPairs1, commitPairs2);
                producer.run();
            });
            thread.start();
            thread.join();
        }

        Thread consumerThread = new Thread(() ->{
            Runnable consumer = new ComparatorConsumer(results, n, similarities);
            consumer.run();
        });
        consumerThread.start();
        consumerThread.join();

        return similarities;
    }

    public static void main(String[] args) throws InterruptedException, GitAPIException, IOException {
        long startTime = System.nanoTime();
        if(args.length < 3) return;
        if(!validNumber(args[2])){
            System.out.println("The input number N is not valid!");
            return;
        }
        int n = Integer.parseInt(args[2]);

        ParallelRepoComparator comparator = new ParallelRepoComparator(args[0], args[1]);
        comparator.getMostSimilarPairs(n);

        double totalTime = (System.nanoTime() - startTime) / 1_000_000_000.0;

        System.err.printf("It took %.2f seconds to find %d most similar revision pairs.\n", totalTime, n);
    }

    /**
     * Helper function to decide the given string could be cast to a valid number or not.
     * @param num is the given string
     * @return true if the string could be cast to integer validly, false if not
     */
    private static boolean validNumber(String num){
        for(int i = 0; i < num.length(); i++){
            if(num.charAt(i) < 48 || num.charAt(i) > 57) return false;
        }
        return true;
    }
}
