package edu.cmu.cs.cs214.hw4.segments;

import java.util.HashSet;
import java.util.LinkedList;

import edu.cmu.cs.cs214.hw4.core.Follower;
import edu.cmu.cs.cs214.hw4.core.Position;
import edu.cmu.cs.cs214.hw4.tileFeature.TileFeature;

/**
 * Interface of represent a set of tile features.
 */
public interface Segment {
	/**
	 * Add the input tile feature to the covered tiles of this segment.
	 * @param tile is the input tile feature
	 * @param pos is the specific position the tile need to be placed
	 */
	public void addTile(TileFeature tileFeature, Position pos);
	/**
	 * Check this segment is whether completed or not.
	 * @return true if completed, false if not
	 */
	public boolean checkCompletion();
	/**
	 * Calculate the score of this segment.
	 * @return the calculated score
	 */
	public int calculateScore();

	/**
	 * Function to check if this segment is overlap with another segment.
	 * If overlapped, then combine the given segment to this segment
	 * @param segment the given segment to be checked
	 * @return true if overlapped, false if not
	 */
	public boolean checkOverlap(Segment segment);
	/**
	 * Function to check if the input tile feature could be validly placed into this segment.
	 * @param tileFeature is the input tile feature
	 * @param pos is the position to be placed
	 * @return true if can be placed, false if not
	 */
	public boolean checkValidPos(TileFeature tileFeature, Position pos);
	/**
	 * Function to get the followers of this segment.
	 * @return the list of followers
	 */
	public LinkedList<Follower> getFollowers();
	/**
	 * Function to add follower to this segment.
	 * @param pos
	 */
	public void addFollower(Position pos);
	/**
	 * Function to get the covered tile features of each segment.
	 * @return the covered tile features
	 */
	public HashSet getListCoveredFeatures();
	
	/**
	 * Function to get the followers position of this segment, even after the followers
	 * are removed if this segment is completed.
	 * @return the followers position
	 */
	public HashSet<Position> getFollowersPos();

}
