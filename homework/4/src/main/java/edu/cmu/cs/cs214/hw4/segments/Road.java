package edu.cmu.cs.cs214.hw4.segments;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import edu.cmu.cs.cs214.hw4.core.Follower;
import edu.cmu.cs.cs214.hw4.core.Position;
import edu.cmu.cs.cs214.hw4.tileFeature.CityFeature;
import edu.cmu.cs.cs214.hw4.tileFeature.RoadFeature;
import edu.cmu.cs.cs214.hw4.tileFeature.TileFeature;

/**
 * Class to represent a road segment on the game board.
 *
 */
public class Road implements Segment{
	
	private HashSet<Position> coveredPos;
	/**
	 * Function to get the covered position of this segment.
	 * @return the set of covered positions
	 */
	public HashSet<Position> getCoveredPos() { return coveredPos; }
	
	private HashSet<RoadFeature> listCoveredFeatures;
	@Override
	public HashSet<RoadFeature> getListCoveredFeatures(){ return listCoveredFeatures; }
	
	private HashMap<Position, RoadFeature> coveredFeatures;
	/**
	 * Function to get the map of this segment, the key is position and the value is road feature.
	 * @return the map of this segment
	 */
	public HashMap<Position, RoadFeature> getCoveredFeatures() { return coveredFeatures; }
	
	private HashSet<Position> neighborPos;
	/**
	 * neighborPos represents the position where can be added a new tile to this road segment.
	 * if the length of neighbor position is 0, it means this segment is already completed
	 * @return the neighbor positions
	 */
	public HashSet<Position> getNeighborPos() { return neighborPos; }
	
	private LinkedList<Follower> followers;
	@Override
	public LinkedList<Follower> getFollowers() { return followers; }
	
	public Road() {
		coveredPos = new HashSet<Position>();
		coveredFeatures = new HashMap<Position, RoadFeature>();
		neighborPos = new HashSet<Position>();
		listCoveredFeatures = new HashSet<RoadFeature>();
		followers = new LinkedList<Follower>();
	}
	private void updateNeighborPos() {
		neighborPos = new HashSet<Position>();
		for(Position pos : coveredPos) {
			int x = pos.getX();
			int y = pos.getY();
			RoadFeature roadFeature = coveredFeatures.get(pos);
			if(roadFeature.getDirection().contains(0)) {
				if(coveredFeatures.get(new Position(x, y - 1)) == null || 
						!coveredFeatures.get(new Position(x, y - 1)).getDirection().contains(2)) {
					neighborPos.add(new Position(x, y - 1));
				}
			}
			if(roadFeature.getDirection().contains(1)) {
				if(coveredFeatures.get(new Position(x - 1, y)) == null || 
						!coveredFeatures.get(new Position(x - 1, y)).getDirection().contains(3)) {
					neighborPos.add(new Position(x - 1, y));
				}
			}
			if(roadFeature.getDirection().contains(2)) {
				if(coveredFeatures.get(new Position(x, y + 1)) == null || 
						!coveredFeatures.get(new Position(x, y + 1)).getDirection().contains(0)) {
					neighborPos.add(new Position(x, y + 1));
				}
			}
			if(roadFeature.getDirection().contains(3)) {
				if(coveredFeatures.get(new Position(x + 1, y)) == null || 
						!coveredFeatures.get(new Position(x + 1, y)).getDirection().contains(1)) {
					neighborPos.add(new Position(x + 1, y));
				}
			}
		}
	}
	@Override
	public boolean checkValidPos(TileFeature tileFeature, Position pos) {
		if(!(tileFeature instanceof RoadFeature)) return false;
		RoadFeature roadFeature = (RoadFeature)tileFeature;
		if(!neighborPos.contains(pos)) return false;
		int x = pos.getX();
		int y = pos.getY();
		boolean least = false;
		
		RoadFeature neighbor = coveredFeatures.get(new Position(x, y - 1));
		if(neighbor != null) {
			if(roadFeature.getDirection().contains(0) && 
					neighbor.getDirection().contains(2)) least = true;
		}
		
		neighbor = coveredFeatures.get(new Position(x - 1, y));
		if(neighbor != null) {
			if(roadFeature.getDirection().contains(1) && 
					neighbor.getDirection().contains(3)) least = true;
		}
		
		neighbor = coveredFeatures.get(new Position(x, y + 1));
		if(neighbor != null) {
			if(roadFeature.getDirection().contains(2) && 
					neighbor.getDirection().contains(0)) least = true;
		}
		
		neighbor = coveredFeatures.get(new Position(x + 1, y));
		if(neighbor != null) {
			if(roadFeature.getDirection().contains(3) && 
					neighbor.getDirection().contains(1)) least = true;
		}
		return least;
	}
	
	@Override
	public void addTile(TileFeature tileFeature, Position pos) {
		if(!(tileFeature instanceof RoadFeature)) return;
		RoadFeature roadFeature = (RoadFeature)tileFeature;
		roadFeature.setPos(pos);
		coveredPos.add(pos);
		if(coveredFeatures.get(pos) != null) {
			RoadFeature newFeature = new RoadFeature();
			for(Integer dir : coveredFeatures.get(pos).getDirection()) {
				newFeature.addDirection(dir);
			}
			for(Integer dir : roadFeature.getDirection()) {
				newFeature.addDirection(dir);
			}
			newFeature.setPos(pos);
			coveredFeatures.put(pos, newFeature);
			listCoveredFeatures.add(roadFeature);
		}else {
			coveredFeatures.put(pos, roadFeature);
			listCoveredFeatures.add(roadFeature);
		}
		updateNeighborPos();
	}
	
	@Override
	public boolean checkCompletion() {
		return neighborPos.size() == 0;
	}

	private final int roadUnitScore = 1;
	@Override
	public int calculateScore() {
		if(!checkCompletion()) return 0;
		return coveredPos.size() * roadUnitScore;
	}

	@Override
	public boolean checkOverlap(Segment segment) {
		if(!(segment instanceof Road)) return false;
		Road road = (Road)segment;
		
		HashSet<RoadFeature> tmp = new HashSet<RoadFeature>(listCoveredFeatures);
		tmp.retainAll(road.getListCoveredFeatures());
		if(tmp.size() == 0) return false;
		Position pos = new Position(0, 0);
		for(RoadFeature feature : tmp) {
			int x = feature.getPos().getX();
			int y = feature.getPos().getY();
			pos = new Position(x, y);
		}
		listCoveredFeatures.addAll(road.getListCoveredFeatures());
		coveredPos.addAll(road.getCoveredPos());
		coveredFeatures.putAll(road.getCoveredFeatures());
		followersPos.addAll(road.getFollowersPos());
		HashSet<Position> containedPos = new HashSet<Position>();
		for(Position checkPos : neighborPos) {
			if(road.getCoveredPos().contains(checkPos)) {
				containedPos.add(checkPos);
			}
		}
		for(Position checkPos : road.getNeighborPos()) {
			if(coveredPos.contains(checkPos)) {
				containedPos.add(checkPos);
			}
		}
		neighborPos.addAll(road.getNeighborPos());
		neighborPos.removeAll(containedPos);
		neighborPos.remove(pos);
		followers.addAll(road.getFollowers());
		return true;
	}

	@Override
	public void addFollower(Position pos) {
		followers.add(new Follower(pos));
		followersPos.add(pos);
	}
	private HashSet<Position> followersPos = new HashSet<Position>();
	@Override
	public HashSet<Position> getFollowersPos() {
		return followersPos;
	}

}
