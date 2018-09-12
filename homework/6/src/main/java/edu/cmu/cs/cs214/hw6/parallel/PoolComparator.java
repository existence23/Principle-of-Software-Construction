package edu.cmu.cs.cs214.hw6.parallel;

import edu.cmu.cs.cs214.hw6.CommitPair;
import edu.cmu.cs.cs214.hw6.RevisionPair;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

public class PoolComparator {

    private String path1;
    private String path2;
    private ParallelRepoLoader loader1;
    private ParallelRepoLoader loader2;

    private List<CommitPair> commitPairs1;
    private List<CommitPair> commitPairs2;

    private List<Double> similarities;

    public PoolComparator(String path1, String path2) throws InterruptedException, GitAPIException, IOException {
        this.path1 = path1;
        this.path2 = path2;
        loader1 = new ParallelRepoLoader(path1);
        loader2 = new ParallelRepoLoader(path2);
        commitPairs1 = loader1.getCommitPairs();
        commitPairs2 = loader2.getCommitPairs();
        similarities = new ArrayList<>();
    }

    public List<Double> getMostSimilarPairs(int n) throws InterruptedException, ExecutionException {
        BlockingQueue<Future<RevisionPair>> futures =
                new PriorityBlockingQueue<>(commitPairs1.size() * commitPairs2.size(), new Comparator<Future<RevisionPair>>() {
                    @Override
                    public int compare(Future<RevisionPair> o1, Future<RevisionPair> o2) {
                        try {
                            return -(o1.get().getDistance().compareTo(o2.get().getDistance()));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        return -1;
                    }
                });

        int poolSize = 10;
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);
        Thread thread = new Thread(() ->{
            try {
                putNewRevisionPair(pool, futures, commitPairs1, commitPairs2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread.join();

        System.out.println(futures.size());
        for(int i = 0; i < n; i++){
            RevisionPair revisionPair = futures.take().get();
            System.out.println("child_hash_1: " + revisionPair.getPair1().getChildName() + "," + "parent_hash_1: " + revisionPair.getPair1().getParentName() + ";");
            System.out.println("child_hash_2: " + revisionPair.getPair2().getChildName() + "," + "parent_hash_2: " + revisionPair.getPair2().getParentName() + ";");
            System.out.println("Similarity: " + revisionPair.getDistance().toString());
            similarities.add(revisionPair.getDistance());
        }
        return similarities;
    }

    /**
     * Helper function to let the current thread work to add new Future<RevisionPair> to the priority queue.
     * @param pool is the ExecutorService pool which provides threads
     * @param futures is the queue of the results to store the revision pairs
     * @param commitPairs1 is the list of the commit pairs in the first repo
     * @param commitPairs2 is the list of the commit pairs in the second repo
     * @throws InterruptedException
     */
    private void putNewRevisionPair(ExecutorService pool, BlockingQueue<Future<RevisionPair>> futures,
                                           List<CommitPair> commitPairs1, List<CommitPair> commitPairs2) throws InterruptedException {
        for(CommitPair pair1 : commitPairs1){
            for(CommitPair pair2 : commitPairs2){
                RevisionPair pair = new RevisionPair(pair1, pair2);
                pair.setDistance(pair1.getSimilarity().distance(pair2.getSimilarity().getMap()));
                futures.put(pool.submit(() -> pair));
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException, GitAPIException {

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
        System.exit(0);
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
