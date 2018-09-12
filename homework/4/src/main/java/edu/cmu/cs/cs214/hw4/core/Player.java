package edu.cmu.cs.cs214.hw4.core;

import java.util.LinkedList;

/**
 * Class to represent the Player in the game system.
 */
public class Player {
	private final int initialFollowers = 8;
	
	private int leftFollowers;
	/**
	 * Function to get the left followers number.
	 * @return the left followers number
	 */
	public int leftFollowers() { return leftFollowers; }
	/**
	 * Function to set the left follower number.
	 * every time when place a follower, the number would -1
	 * if one of the segments in the game board completed, then the followers would be return back to the player
	 * @param leftFollower is the left follower number to be set
	 */
	public void setLeftFollower(int leftFollower) { this.leftFollowers = leftFollower; }
	
	private LinkedList<Follower> usedFollowers;
	/**
	 * Function to get the followers of this player.
	 * @return the list of the followers
	 */
	public LinkedList<Follower> getFollowers(){ return usedFollowers; }
	
	public Player() {
		leftFollowers = initialFollowers;
		usedFollowers = new LinkedList<Follower>();
		score = 0;
	}
	
	private String name;
	/** Function to set the name of this player during game.*/
	public void setPlayerName(String name) {
		this.name = name;
	}
	/** Function to get the name of this player.*/
	public String getName() { return name; }
	
	/**
	 * Function to place one of the left followers.
	 * @param pos is the follower going to be placed, assuming the left follower number has already been checked
	 * which means there is still followers left to be placed
	 */
	public void placeFollower(Position pos) {
		leftFollowers -= 1;
		Follower newFollower = new Follower(pos);
		usedFollowers.add(newFollower);
	}
	
	private int score;
	/**
	 * Function to get the score of this player.
	 * @return the score
	 */
	public int getScore() { return score; }
	/**
	 * Function to update the score of this player.
	 * @param add is the score need to be added to this player
	 */
	public void addScore(int add) { score += add; }
}
