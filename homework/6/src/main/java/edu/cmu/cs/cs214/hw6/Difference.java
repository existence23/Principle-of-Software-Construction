package edu.cmu.cs.cs214.hw6;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to load and handle the difference between two commits.
 */
public class Difference {
    private String diff;
    private String[] lines;
    private List<String> content;

    /**
     * Constructor of Difference.
     * @param diff is the content of the difference in string format of two commits
     */
    public Difference(String diff){
        this.diff = diff;
        lines = diff.split("\r\n|\r|\n");
        content = new ArrayList<>();
    }

    /**
     * Separate the content of the difference in lines and get the list of lines.
     * @return the list of lines
     */
    public List<String> getContent(){
        if(lines[lines.length - 1].equals("Binary files differ")){
            return content;
        }else{
            boolean start = false;
            for(String line : lines){
                if(!start){
                    if(line.length() >= 2 && line.substring(0, 2).equals("@@")){
                        start = true;
                    }
                    continue;
                }
                if(line.length() > 0 &&
                        (line.charAt(0) == '+' || line.charAt(0) == '-')){
                    content.add(line);
                }
            }
            return content;
        }
    }

    /**
     * Function to combine the content of this difference with the content of another Difference to this Difference.
     * The reason why need to combine difference is because there may be one more diff between two
     * commits, and we need to combine all the diff between two commits to compare the modified part
     * @param diff is the other Difference needed to be combined.
     */
    public void combineDiff(Difference diff){
        for(String line : diff.getContent()){
            content.add(line);
        }
    }
}
