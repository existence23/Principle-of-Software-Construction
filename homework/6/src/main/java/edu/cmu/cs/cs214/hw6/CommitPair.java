package edu.cmu.cs.cs214.hw6;

/**
 * Class to represent a pair of the parent commit name and child commit name.
 */
public class CommitPair {
    private String parentName;
    private String childName;
    private Similarity similarity;

    /**
     * Constructor of the CommitPair.
     * @param parentName is the given parent name
     * @param childName is the given child name
     */
    public CommitPair(String parentName, String childName){
        this.parentName = parentName;
        this.childName = childName;
    }

    /**
     * Function to set the Similarity of this CommitPair to store the revision features
     * between these two commits.
     * @param similarity is the given similarity
     */
    public void setSimilarity(Similarity similarity){
        this.similarity = similarity;
    }

    /**
     * Function to get the Similarity of this CommitPair.
     * @return the Similarity of this CommitPair
     */
    public Similarity getSimilarity(){
        return similarity;
    }

    /**
     * Function to get the parent name of this CommitPair.
     * @return the parent name
     */
    public String getParentName() { return parentName; }

    /**
     * Function to get the child name of this CommitPair.
     * @return the child name
     */
    public String getChildName() { return childName; }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof CommitPair)) return false;
        CommitPair pair = (CommitPair)obj;
        return pair.getChildName().equals(childName) &&
                pair.getParentName().equals(parentName);
    }

    @Override
    public int hashCode(){
        return parentName.hashCode() * 31 + childName.hashCode();
    }

    @Override
    public String toString(){
        return "Parent Name: " + parentName + "; Child Name: " + childName;
    }
}
