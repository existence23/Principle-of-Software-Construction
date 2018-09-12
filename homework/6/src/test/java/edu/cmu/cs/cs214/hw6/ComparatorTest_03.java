package edu.cmu.cs.cs214.hw6;

import edu.cmu.cs.cs214.hw6.parallel.ParallelRepoComparator;
import edu.cmu.cs.cs214.hw6.parallel.PoolComparator;
import edu.cmu.cs.cs214.hw6.sequential.RepoComparator;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static junit.framework.TestCase.assertEquals;

public class ComparatorTest_03 {

    private static String path1 = "/Users/kaige/desktop/17214/kge/homework/6/resources/diligent-computing/.git";
    private static String path2 = "/Users/kaige/desktop/17214/kge/homework/6/resources/CheesyBusiness/.git";
    private static int n = 20;

    @Test
    public void test_01() throws IOException, GitAPIException, InterruptedException {
        RepoComparator comparator = new RepoComparator(path1, path2);
        List<Double> similarities1 = comparator.getMostSimilarPairs(n);
        ParallelRepoComparator parallelRepoComparator = new ParallelRepoComparator(path1, path2);
        List<Double> similarities2 = parallelRepoComparator.getMostSimilarPairs(n);
        assertEquals(similarities1.size(), similarities2.size());
        for(int i = 0; i < similarities1.size(); i++){
            assertEquals(similarities1.get(i), similarities2.get(i));
        }

    }
    @Test
    public void test_02() throws IOException, GitAPIException, InterruptedException, ExecutionException {
        RepoComparator comparator = new RepoComparator(path1, path2);
        List<Double> similarities1 = comparator.getMostSimilarPairs(n);
        PoolComparator poolComparator = new PoolComparator(path1, path2);
        List<Double> similarities3 = poolComparator.getMostSimilarPairs(n);
        assertEquals(similarities1.size(), similarities3.size());
        for(int i = 0; i < similarities1.size(); i++){
            assertEquals(similarities1.get(i), similarities3.get(i));
        }
    }
}
