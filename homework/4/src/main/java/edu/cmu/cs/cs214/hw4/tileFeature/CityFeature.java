package edu.cmu.cs.cs214.hw4.tileFeature;

import java.util.LinkedList;

import edu.cmu.cs.cs214.hw4.core.Position;

/**
 * This class represents one single city feature.
 */
public class CityFeature implements TileFeature{
	private LinkedList<Integer> direction;
	/**
	 * Function to get the city edge feature direction of this feature.
	 * @return the list of city edge feature directions
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
	
	private boolean hasPennant;
	/**
	 * Function to get whether this city feature has a pennant or not.
	 * @return true if it has a pennant, false if not
	 */
	public boolean getHasPennant() { return hasPennant; }
	
	public CityFeature(boolean hasPennant) {
		direction = new LinkedList<Integer>();
		this.hasPennant = hasPennant;
	}
	@Override
	public String toString() { return direction.toString(); }
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof CityFeature)) return false;
		CityFeature newFeature = (CityFeature) obj;
		if(!pos.equals(newFeature.getPos())) return false;
		if(hasPennant != newFeature.hasPennant) return false;
		if(direction.size() != newFeature.getDirection().size()) return false;
		for(Integer dir : direction) {
			if(!newFeature.getDirection().contains(dir)) return false;
		}
		return true;
	}
	@Override
	public int hashCode() {
		int hash = 0;
		hash = 31 * hash + pos.hashCode();
		hash = 31 * hash + Boolean.hashCode(hasPennant);
		return hash;
	}
}
