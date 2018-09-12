package edu.cmu.cs.cs214.hw4.segments;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import edu.cmu.cs.cs214.hw4.core.Follower;
import edu.cmu.cs.cs214.hw4.core.Position;
import edu.cmu.cs.cs214.hw4.tileFeature.CityFeature;
import edu.cmu.cs.cs214.hw4.tileFeature.TileFeature;

/**
 * Class to represent a city segment on the game board.
 * One city segment is composed of a set of city features
 *
 */
public class City implements Segment{

	private HashSet<Position> coveredPos;
	/**
	 * Function to get the covered position of this city segment.
	 * @return the list of positions
	 */
	public HashSet<Position> getCoveredPos() { return coveredPos; }
	
	private HashSet<CityFeature> listCoveredFeatures;
	@Override
	public HashSet<CityFeature> getListCoveredFeatures(){ return listCoveredFeatures; }
	
	private HashMap<Position, CityFeature> coveredFeatures;
	/**
	 * Function to get the map of this segment, the key is position, and the value is city feature.
	 * @return the map of this segment
	 */
	public HashMap<Position, CityFeature> getCoveredFeatures() { return coveredFeatures; }
	
	private HashSet<Position> neighborPos;
	/**
	 * neighborPos represents the position where can be added a new tile to this city segment.
	 * if the length of neighbor position is 0, it means this segment is already completed
	 * @return the neighbor positions
	 */
	public HashSet<Position> getNeighborPos() { return neighborPos; }
	
	private LinkedList<Follower> followers;
	@Override
	public LinkedList<Follower> getFollowers() { return followers; }
	
	private int pennantNum;
	/**
	 * Function to get the pennant number in this city segment.
	 * @return the pennant number
	 */
	public int getPennantNum() { return pennantNum; }
	
	public City() {
		coveredPos = new HashSet<Position>();
		coveredFeatures = new HashMap<Position, CityFeature>();
		neighborPos = new HashSet<Position>();
		listCoveredFeatures = new HashSet<CityFeature>();
		followers = new LinkedList<Follower>();
		pennantNum = 0;
	}
	
	private void updateNeighborPos() {
		neighborPos = new HashSet<Position>();
		for(Position pos : coveredPos) {
			int x = pos.getX();
			int y = pos.getY();
			CityFeature cityFeature = coveredFeatures.get(pos);
			if(cityFeature.getDirection().contains(0)) {
				if(coveredFeatures.get(new Position(x, y - 1)) == null || 
						!coveredFeatures.get(new Position(x, y - 1)).getDirection().contains(2)) {
					neighborPos.add(new Position(x, y - 1));
				}
			}
			if(cityFeature.getDirection().contains(1)) {
				if(coveredFeatures.get(new Position(x - 1, y)) == null || 
						!coveredFeatures.get(new Position(x - 1, y)).getDirection().contains(3)) {
					neighborPos.add(new Position(x - 1, y));
				}
			}
			if(cityFeature.getDirection().contains(2)) {
				if(coveredFeatures.get(new Position(x, y + 1)) == null || 
						!coveredFeatures.get(new Position(x, y + 1)).getDirection().contains(0)) {
					neighborPos.add(new Position(x, y + 1));
				}
			}
			if(cityFeature.getDirection().contains(3)) {
				if(coveredFeatures.get(new Position(x + 1, y)) == null || 
						!coveredFeatures.get(new Position(x + 1, y)).getDirection().contains(1)) {
					neighborPos.add(new Position(x + 1, y));
				}
			}
		}
	}
	@Override
	public boolean checkValidPos(TileFeature tileFeature, Position pos) {
		if(!(tileFeature instanceof CityFeature)) return false;
		CityFeature cityFeature = (CityFeature) tileFeature;
		if(!neighborPos.contains(pos)) return false;
		int x = pos.getX();
		int y = pos.getY();
		boolean least = false;
		CityFeature neighbor = coveredFeatures.get(new Position(x, y - 1));
		if(neighbor != null) {
			if(cityFeature.getDirection().contains(0) && neighbor.getDirection().contains(2)) least = true;
		}
		neighbor = coveredFeatures.get(new Position(x - 1, y));
		if(neighbor != null) {
			if(cityFeature.getDirection().contains(1) && 
					neighbor.getDirection().contains(3)) least = true;
		}
		neighbor = coveredFeatures.get(new Position(x, y + 1));
		if(neighbor != null) {
			if(cityFeature.getDirection().contains(2) && 
					neighbor.getDirection().contains(0)) least = true;
		}
		neighbor = coveredFeatures.get(new Position(x + 1, y));
		if(neighbor != null) {
			if(cityFeature.getDirection().contains(3) && 
					neighbor.getDirection().contains(1)) least = true;
		}
		return least;
	}
	
	@Override
	public void addTile(TileFeature tileFeature, Position pos) {
		if(!(tileFeature instanceof CityFeature)) return;
		CityFeature cityFeature = (CityFeature) tileFeature;
		if(cityFeature.getHasPennant()) pennantNum += 1;
		cityFeature.setPos(pos);
		coveredPos.add(pos);
		if(coveredFeatures.get(pos) != null) {
			CityFeature newFeature = new CityFeature(coveredFeatures.get(pos).getHasPennant());
			for(Integer dir : coveredFeatures.get(pos).getDirection()) {
				newFeature.addDirection(dir);
			}
			for(Integer dir : cityFeature.getDirection()) {
				newFeature.addDirection(dir);
			}
			newFeature.setPos(pos);
			coveredFeatures.put(pos, newFeature);
			listCoveredFeatures.add(cityFeature);
		}else {
			coveredFeatures.put(pos, cityFeature);
			listCoveredFeatures.add(cityFeature);
		}
		updateNeighborPos();
	}
	
	@Override
	public boolean checkCompletion() {
		return neighborPos.size() == 0;
	}
	
	private final int cityUnitScore = 2;
	private final int pennantUnitScore = 2;

	@Override
	public int calculateScore() {
		if(!checkCompletion()) return 0;
		return cityUnitScore * coveredPos.size() + pennantUnitScore * pennantNum;
	}

	
	@Override
	public boolean checkOverlap(Segment segment) {
		if(!(segment instanceof City)) return false;
		City city = (City)segment;
		
		HashSet<CityFeature> tmp = new HashSet<CityFeature>(listCoveredFeatures);
		tmp.retainAll(city.getListCoveredFeatures());
		if(tmp.size() == 0) return false;
		Position pos = new Position(0, 0);
		for(CityFeature feature : tmp) {
			int x = feature.getPos().getX();
			int y = feature.getPos().getY();
			pos = new Position(x, y);
		}
		listCoveredFeatures.addAll(city.getListCoveredFeatures());
		coveredPos.addAll(city.getCoveredPos());
		coveredFeatures.putAll(city.getCoveredFeatures());
		followersPos.addAll(city.getFollowersPos());
		HashSet<Position> containedPos = new HashSet<Position>();
		for(Position checkPos : neighborPos) {
			if(city.getCoveredPos().contains(checkPos)) {
				containedPos.add(checkPos);
			}
		}
		for(Position checkPos : city.getNeighborPos()) {
			if(coveredPos.contains(checkPos)) {
				containedPos.add(checkPos);
			}
		}
		neighborPos.addAll(city.getNeighborPos());
		neighborPos.removeAll(containedPos);
		neighborPos.remove(pos);
		followers.addAll(city.getFollowers());
		pennantNum += city.getPennantNum();
		if(coveredFeatures.get(pos).getHasPennant()) pennantNum -= 1;
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
