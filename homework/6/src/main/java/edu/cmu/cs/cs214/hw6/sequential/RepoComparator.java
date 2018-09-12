package edu.cmu.cs.cs214.hw6.sequential;

import edu.cmu.cs.cs214.hw6.CommitPair;
import edu.cmu.cs.cs214.hw6.RevisionPair;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.*;

/**
 * Class to compare two different git repositories.
 */
public class RepoComparator {

    private String path1;
    private String path2;

    private RepoLoader loader1;
    private RepoLoader loader2;

    private List<Double> similarities;

    /**
     * Constructor of the RepoComparator.
     * @param path1 is the path of the first git repository on the local computer
     * @param path2 is the path of the other git repository on the local computer
     * @throws IOException when the path is not correct
     * @throws GitAPIException when the loaded repository has no HEAD
     */
    public RepoComparator(String path1, String path2) throws IOException, GitAPIException {
        this.path1 = path1;
        this.path2 = path2;
        loader1 = new RepoLoader(this.path1);
        loader2 = new RepoLoader(this.path2);
        similarities = new ArrayList<>();
    }

    /**
     * Function to print all the n most similar revisions between these two repositories.
     * @param n is the given n
     * @return the list of the similarities
     */
    public List<Double> getMostSimilarPairs(int n){
        Set<CommitPair> commitPairs1 = loader1.getCommitPairs();
        Set<CommitPair> commitPairs2 = loader2.getCommitPairs();
        List<RevisionPair> revisionPairs = new ArrayList<>();

        for(CommitPair pair1 : commitPairs1){
            for(CommitPair pair2 : commitPairs2){
                revisionPairs.add(new RevisionPair(pair1, pair2));
            }
        }

        if(revisionPairs.size() < n){
            System.out.println("Illegal n! n is bigger than size of revision pairs!");
            return null;
        }

        Collections.sort(revisionPairs, new Comparator<RevisionPair>() {
            @Override
            public int compare(RevisionPair pair1, RevisionPair pair2) {
                return pair1.getDistance().compareTo(pair2.getDistance());
            }
        });

        for(int i = 0; i < n; i++){
            RevisionPair revisionPair = revisionPairs.get(revisionPairs.size() - 1 - i);
            System.out.println("child_hash_1: " + revisionPair.getPair1().getChildName() + "," +
                    "parent_hash_1: " + revisionPair.getPair1().getParentName() + ";");
            System.out.println("child_hash_2: " + revisionPair.getPair2().getChildName() + "," +
                    "parent_hash_2: " + revisionPair.getPair2().getParentName() + ";");
            System.out.println("Similarity: " + revisionPair.getDistance().toString());
            similarities.add(revisionPair.getDistance());
        }
        return similarities;
    }

    /**
     * The main function in the RepoComparator class to compare two different git repositories.
     * @param args is the given arguments of this function
     *             Assume that the length of args should be 3
     *             First is the path of the first git repository on your local computer
     *             Second is the path of the other one git repository on your local computer
     *             Third is the set N, which is the number of the most similar revisions needed to be found
     * @throws IOException when the path is not correct
     * @throws GitAPIException when the loaded repository has no HEAD
     */
    public static void main(String[] args) throws IOException, GitAPIException {
        long startTime = System.nanoTime();

        if(args.length < 3) return;
        if(!validNumber(args[2])){
            System.out.println("The input number N is not valid!");
            return;
        }
        String path1 = args[0];
        String path2 = args[1];
        int n = Integer.parseInt(args[2]);
        //e.g. path1 = "/Users/kaige/desktop/17214/kge/.git";
        //     path2 = "/Users/kaige/desktop/17214/Team15/.git";
        RepoComparator comparator = new RepoComparator(path1, path2);
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
