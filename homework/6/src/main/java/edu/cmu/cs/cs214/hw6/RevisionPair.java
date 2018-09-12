package edu.cmu.cs.cs214.hw6;

/**
 * Class to represent a pair of revision between two repositories.
 */
public class RevisionPair{
    private CommitPair pair1;
    private CommitPair pair2;
    private Double distance;

    /**
     * Constructor of the RevisionPair.
     * @param pair1 is the revision in one repository.
     * @param pair2 is the revision on the other one repository.
     */
    public RevisionPair(CommitPair pair1, CommitPair pair2){
        this.pair1 = pair1;
        this.pair2 = pair2;
        distance = calculateDistance();
    }

    /**
     * Function to get the CommitPair in the first repository.
     * @return the CommitPair in the first repository
     */
    public CommitPair getPair1(){
        return pair1;
    }

    /**
     * Function to get the CommitPair in the second repository.
     * @return the CommitPair in the second repository.
     */
    public CommitPair getPair2(){
        return pair2;
    }

    /**
     * Helper function to calculate the distance between two revisions.
     * @return the calculated distance between these two revisions
     */
    private Double calculateDistance(){
        Similarity similarity1 = pair1.getSimilarity();
        Similarity similarity2 = pair2.getSimilarity();
        return similarity1.distance(similarity2.getMap());
    }

    public void setDistance(Double distance){ this.distance = new Double(distance); }

    /**
     * Function to get the distance between these two revisions.
     * @return the distance between these two revisions
     */
    public Double getDistance(){
        return distance;
    }
}
