package edu.cmu.cs.cs214.hw4.core;

/**
 * Class to represent the followers of the players in the Carcassonne game.
 *
 */
public class Follower {
	private Position pos;
	/**
	 * Function to get the position of this follower.
	 * @return the position
	 */
	public Position getPosition() { return pos; }
	
	public Follower(Position pos) {
		this.pos = new Position(pos.getX(), pos.getY());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Follower) {
			Follower newFollower = (Follower)obj;
			return pos.equals(newFollower.getPosition());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return pos.hashCode();
	}
}
