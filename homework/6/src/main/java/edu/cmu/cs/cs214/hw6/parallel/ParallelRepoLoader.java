package edu.cmu.cs.cs214.hw6.parallel;

import edu.cmu.cs.cs214.hw6.CommitPair;
import edu.cmu.cs.cs214.hw6.Difference;
import edu.cmu.cs.cs214.hw6.Similarity;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class ParallelRepoLoader {
    private String repoPath;
    private Map<String, RevCommit> commitList;
    private List<CommitPair> commitPairs;
    private final int N_THREAD = 4;

    /**
     * Constructor of this RepoLoader.
     * @param repoPath is the path of this repository on the local computer
     * @throws IOException when the path is not correct
     * @throws GitAPIException when the loaded repository has no HEAD
     */
    public ParallelRepoLoader(String repoPath) throws IOException, GitAPIException, InterruptedException {
        this.repoPath = repoPath;
        commitList = new HashMap<>();
        commitPairs = new ArrayList<>();
        loadRepository();
    }

    /**
     * Helper function to load the git repository.
     * Find and store all the parent and child commits
     * Calculate the Similarity of each CommitPair
     * @throws IOException when the path is not correct
     * @throws GitAPIException when the loaded repository has no HEAD
     */
    private void loadRepository() throws IOException, GitAPIException, InterruptedException {
        FileRepository repo = new FileRepository(repoPath);
        Git git = new Git(repo);
        Iterable<RevCommit> commits = git.log().all().call();
        for(RevCommit commit : commits){
            commitList.put(commit.getName(), commit);
            for(int i = 0; i < commit.getParentCount(); i++){
                commitPairs.add(new CommitPair(commit.getParent(i).getName(), commit.getName()));
            }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DiffFormatter df = new DiffFormatter(out);
        RawTextComparator cmp = RawTextComparator.DEFAULT;

        df.setRepository(repo);
        df.setDiffComparator(cmp);
        df.setDetectRenames(true);

        int size = commitPairs.size();
        int threadCapacity = size / N_THREAD + 1;
        for(int i = 0; i < N_THREAD; i++){
            int max;
            if((i + 1) * threadCapacity > size){
                max = size;
            }else{
                max = (i + 1) * threadCapacity;
            }
            int index = i;
            Thread thread = new Thread(() ->{
                for(int j = index * threadCapacity; j < max; j++){
                    CommitPair pair = commitPairs.get(j);
                    Difference diff = new Difference("");
                    List<DiffEntry> diffEntries = null;
                    try {
                        diffEntries = df.scan(commitList.get(pair.getParentName()).getTree(), commitList.get(pair.getChildName()).getTree());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for(DiffEntry diffEntry : diffEntries){
                        try {
                            df.format(diffEntry);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String diffText = out.toString();
                        Difference newDiff = new Difference(diffText);
                        diff.combineDiff(newDiff);
                        out.reset();
                    }
                    Similarity similarity = new Similarity(diff.getContent());
                    commitPairs.get(j).setSimilarity(similarity);
                }
            });
            thread.start();
            thread.join();
        }
    }

    /**
     * Function to get all the CommitPair
     * @return the set of all the CommitPair
     */
    public List<CommitPair> getCommitPairs(){
        return commitPairs;
    }
}
