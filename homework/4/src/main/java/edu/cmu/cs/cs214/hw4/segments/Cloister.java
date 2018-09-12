package edu.cmu.cs.cs214.hw4.segments;

import java.util.HashSet;
import java.util.LinkedList;

import edu.cmu.cs.cs214.hw4.core.Follower;
import edu.cmu.cs.cs214.hw4.core.Position;
import edu.cmu.cs.cs214.hw4.tileFeature.CloisterFeature;
import edu.cmu.cs.cs214.hw4.tileFeature.TileFeature;

/**
 * Class to represent the cloister segment.
 * one cloister segment only has one cloister feature
 * but this cloister feature was surrounded by other tiles
 */
public class Cloister implements Segment{

	private HashSet<Position> coveredPos;
	/**
	 * Function to get the covered position of this cloister segment.
	 * @return the list of covered position
	 */
	public HashSet<Position> getCoveredPos() { return coveredPos; }
	
	private Position cloisterPos;
	/**
	 * Function to get the cloister position.
	 * @return the cloister position
	 */
	public Position getCloisterPos() { return cloisterPos; }
	
	public LinkedList<Follower> followers;
	/**
	 * Notice that each cloister segment could only have one follower.
	 */
	@Override
	public LinkedList<Follower> getFollowers() { return followers; }
	
	private CloisterFeature cloisterFeature = new CloisterFeature();
	
	public Cloister(Position cloisterPos) {
		coveredPos = new HashSet<Position>();
		this.cloisterPos = cloisterPos;
		cloisterFeature.setPos(cloisterPos);
		followers = new LinkedList<Follower>();
	}
	
	/**
	 * Function to add covered position to this segment.
	 * @param pos is the position to be covered
	 */
	public void addTile(Position pos) {
		coveredPos.add(pos);
	}
	
	@Override
	public void addTile(TileFeature tileFeature, Position pos) {
		
	}

	private final int completeCloisterNum = 9;
	@Override
	public boolean checkCompletion() {
		return coveredPos.size() == completeCloisterNum;
	}

	private final int cloisterUnitScore = 1;
	@Override
	public int calculateScore() {
		if(!checkCompletion()) return 0;
		return cloisterUnitScore * completeCloisterNum;
	}


	@Override
	public boolean checkOverlap(Segment segment) {
		return false;
	}

	@Override
	public boolean checkValidPos(TileFeature tileFeature, Position pos) {
		return true;
	}

	@Override
	public void addFollower(Position pos) {
		followers.add(new Follower(pos));
		followersPos.add(pos);
	}

	@Override
	public HashSet<CloisterFeature> getListCoveredFeatures() {
		HashSet<CloisterFeature> listCoveredFeatures = new HashSet<CloisterFeature>();
		listCoveredFeatures.add(cloisterFeature);
		return listCoveredFeatures;
	}
	
	private HashSet<Position> followersPos = new HashSet<Position>();
	@Override
	public HashSet<Position> getFollowersPos() {
		return followersPos;
	}

}
