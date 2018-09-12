package edu.cmu.cs.cs214.hw4.tileFeature;

import java.util.LinkedList;

import edu.cmu.cs.cs214.hw4.core.Position;

/**
 * FarmFeature class represents the single farm feature in one tile.
 */
public class FarmFeature implements TileFeature {
	
	/**
	 * farmFeatures is the corner number of this single feature.
	 * in which 1 represents the left-up corner
	 * 2 represents the left-down corner
	 * 3 represents the right-down corner
	 * 4 represents the right-up corner
	 */
	private LinkedList<Integer> farmFeatures;
	/**
	 * Function get the list of farm features.
	 * @return the list of farm features
	 */
	public LinkedList<Integer> getFarmFeatures() { return farmFeatures; }
	
	/**
	 * farmNeighbors represents the neighbor position of this single farm feature.
	 */
	private LinkedList<String> farmNeighbors;
	/**
	 * Function get the list of neighbor positions.
	 * @return the neighbors of this farm feature
	 */
	public LinkedList<String> getFarmNeighbors() { return farmNeighbors; }
	
	public FarmFeature(LinkedList<Integer> farmFeatures, LinkedList<String> farmNeighbors) {
		this.farmFeatures = farmFeatures;
		this.farmNeighbors = farmNeighbors;
	}
	
	private Position pos;
	@Override
	public void setPos(Position pos) { this.pos = pos; }
	@Override
	public Position getPos() { return pos; }
	
	/**
	 * Helper function to whether can be combined with the up farm feature into one farm segment.
	 * @param feature is the up farm feature
	 * @return true if can be combined, false if not
	 */
	public boolean checkUpMatch(FarmFeature feature) {
		if(farmFeatures.contains(1) && farmNeighbors.contains("up")) {
			if(feature.getFarmFeatures().contains(2) && feature.getFarmNeighbors().contains("down"))
				return true;
		}
		if(farmFeatures.contains(4) && farmNeighbors.contains("up")) {
			if(feature.getFarmFeatures().contains(3) && feature.getFarmNeighbors().contains("down"))
				return true;
		}
		
//		if(farmFeatures.contains(1) && !feature.getFarmFeatures().contains(2)) return false;
//		if(farmFeatures.contains(4) && !feature.getFarmFeatures().contains(3)) return false;
//		if(!farmNeighbors.contains("up") || 
//				!feature.getFarmNeighbors().contains("down")) return false;
		return false;
	}
	/**
	 * Helper function to whether can be combined with the left farm feature into one farm segment.
	 * @param feature is the left farm feature
	 * @return true if can be combined, false if not
	 */
	public boolean checkLeftMatch(FarmFeature feature) {
//		if(farmFeatures.contains(1) && !feature.getFarmFeatures().contains(4)) return false;
//		if(farmFeatures.contains(2) && !feature.getFarmFeatures().contains(3)) return false;
//		if(!farmNeighbors.contains("left") || 
//				!feature.getFarmNeighbors().contains("right")) return false;
		if(farmFeatures.contains(1) && farmNeighbors.contains("left")) {
			if(feature.getFarmFeatures().contains(4) && feature.getFarmNeighbors().contains("right"))
				return true;
		}
		if(farmFeatures.contains(2) && farmNeighbors.contains("left")) {
			if(feature.getFarmFeatures().contains(3) && feature.getFarmNeighbors().contains("right"))
				return true;
		}
		return false;
	}
	/**
	 * Helper function to whether can be combined with the down farm feature into one farm segment.
	 * @param feature is the down farm feature
	 * @return true if can be combined, false if not
	 */
	public boolean checkDownMatch(FarmFeature feature) {
//		if(farmFeatures.contains(2) && !feature.getFarmFeatures().contains(1)) return false;
//		if(farmFeatures.contains(3) && !feature.getFarmFeatures().contains(4)) return false;
//		if(!farmNeighbors.contains("down") || 
//				!feature.getFarmNeighbors().contains("up")) return false;
		if(farmFeatures.contains(2) && farmNeighbors.contains("down")) {
			if(feature.getFarmFeatures().contains(1) && feature.getFarmNeighbors().contains("up"))
				return true;
		}
		if(farmFeatures.contains(3) && farmNeighbors.contains("down")) {
			if(feature.getFarmFeatures().contains(4) && feature.getFarmNeighbors().contains("up"))
				return true;
		}
		return false;
	}
	/**
	 * Helper function to whether can be combined with the right farm feature into one farm segment.
	 * @param feature is the right farm feature
	 * @return true if can be combined, false if not
	 */
	public boolean checkRightMatch(FarmFeature feature) {
//		if(farmFeatures.contains(3) && !feature.getFarmFeatures().contains(2)) return false;
//		if(farmFeatures.contains(4) && !feature.getFarmFeatures().contains(1)) return false;
//		if(!farmNeighbors.contains("right") || 
//				!feature.getFarmNeighbors().contains("left")) return false;
		if(farmFeatures.contains(3) && farmNeighbors.contains("right")) {
			if(feature.getFarmFeatures().contains(2) && feature.getFarmNeighbors().contains("left"))
				return true;
		}
		if(farmFeatures.contains(4) && farmNeighbors.contains("right")) {
			if(feature.getFarmFeatures().contains(1) && feature.getFarmNeighbors().contains("left"))
				return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof FarmFeature)) return false;
		FarmFeature newFeature = (FarmFeature)obj;
		if(!pos.equals(newFeature.getPos())) return false;
		if(farmFeatures.size() != newFeature.getFarmFeatures().size()) return false;
		for(Integer edge : farmFeatures) {
			if(!newFeature.getFarmFeatures().contains(edge)) return false;
		}
		if(farmNeighbors.size() != newFeature.getFarmNeighbors().size()) return false;
		for(String neighbor : farmNeighbors) {
			if(!newFeature.getFarmNeighbors().contains(neighbor)) return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		if(farmFeatures.contains(1)) hash = hash * 31 + Integer.hashCode(1);
		if(farmFeatures.contains(2)) hash = hash * 31 + Integer.hashCode(2);
		if(farmFeatures.contains(3)) hash = hash * 31 + Integer.hashCode(3);
		if(farmFeatures.contains(4)) hash = hash * 31 + Integer.hashCode(4);
		if(farmNeighbors.contains("up")) hash = hash * 31 + "up".hashCode();
		if(farmNeighbors.contains("left")) hash = hash * 31 + "left".hashCode();
		if(farmNeighbors.contains("down")) hash = hash * 31 + "down".hashCode();
		if(farmNeighbors.contains("right")) hash = hash * 31 + "right".hashCode();
		return 31 * pos.getX() + pos.getY();
	}
}
