package edu.cmu.cs.cs214.hw6.sequential;

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

/**
 * Class to load one repository and store the CommitPair and the according Similarity.
 * Assume the repository is in local computer
 */
public class RepoLoader {

    private String repoPath;
    private Map<String, RevCommit> commitList;
    private Set<CommitPair> commitPairs;

    /**
     * Constructor of this RepoLoader.
     * @param repoPath is the path of this repository on the local computer
     * @throws IOException when the path is not correct
     * @throws GitAPIException when the loaded repository has no HEAD
     */
    public RepoLoader(String repoPath) throws IOException, GitAPIException {
        this.repoPath = repoPath;
        commitList = new HashMap<>();
        commitPairs = new HashSet<>();
        loadRepository();
    }

    /**
     * Helper function to load the git repository.
     * Find and store all the parent and child commits
     * Calculate the Similarity of each CommitPair
     * @throws IOException when the path is not correct
     * @throws GitAPIException when the loaded repository has no HEAD
     */
    private void loadRepository() throws IOException, GitAPIException {
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

        for(CommitPair pair : commitPairs){
            Difference diff = new Difference("");
            List<DiffEntry> diffEntries = df.scan(commitList.get(pair.getParentName()).getTree(),
                    commitList.get(pair.getChildName()).getTree());
            for(DiffEntry diffEntry : diffEntries){
                df.format(diffEntry);
                String diffText = out.toString();
                Difference newDiff = new Difference(diffText);
                diff.combineDiff(newDiff);
                out.reset();
            }
            Similarity similarity = new Similarity(diff.getContent());
            pair.setSimilarity(similarity);
        }
    }

    /**
     * Function to get all the CommitPair
     * @return the set of all the CommitPair
     */
    public Set<CommitPair> getCommitPairs(){
        return commitPairs;
    }
}
