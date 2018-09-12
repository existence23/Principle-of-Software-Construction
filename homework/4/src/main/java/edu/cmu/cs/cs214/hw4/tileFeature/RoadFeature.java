package edu.cmu.cs.cs214.hw4.tileFeature;

import java.util.LinkedList;

import edu.cmu.cs.cs214.hw4.core.Position;

/**
 * This class is used to represent one single road feature of a tile.
 *
 */
public class RoadFeature implements TileFeature{
	private LinkedList<Integer> direction;
	/**
	 * Function to get the road edge feature direction of this feature.
	 * @return the list of road edge feature directions
	 */
	public LinkedList<Integer> getDirection() { return direction; }
	/**
	 * Function to add edge feature direction to the list.
	 * @param add the direction to be added
	 */
	public void addDirection(Integer add) { direction.add(add); }
	
	private Position pos;
	@Override
	public void setPos(Position pos) { this.pos = pos; }
	@Override
	public Position getPos() { return pos; }
	
	public RoadFeature() {
		direction = new LinkedList<Integer>();
	}
	@Override
	public String toString() { return direction.toString(); }
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof RoadFeature)) return false;
		RoadFeature newFeature = (RoadFeature) obj;
		if(!pos.equals(newFeature.getPos())) return false;
		if(direction.size() != newFeature.getDirection().size()) return false;
		for(Integer dir : direction) {
			if(!newFeature.getDirection().contains(dir)) return false;
		}
		return true;
	}
	@Override
	public int hashCode() {
		int hash = 0;
		for(int i = 0; i < direction.size(); i++) {
			hash = direction.get(i).hashCode() + 31 * hash;
		}
		hash = 31 * hash + pos.hashCode();
		return hash;
	}
}
