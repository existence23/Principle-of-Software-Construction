package edu.cmu.cs.cs214.hw4.segments;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import edu.cmu.cs.cs214.hw4.core.Follower;
import edu.cmu.cs.cs214.hw4.core.Position;
import edu.cmu.cs.cs214.hw4.tileFeature.CityFeature;
import edu.cmu.cs.cs214.hw4.tileFeature.FarmFeature;
import edu.cmu.cs.cs214.hw4.tileFeature.RoadFeature;
import edu.cmu.cs.cs214.hw4.tileFeature.TileFeature;

/**
 * This class represents one single farm segment.
 * The farm segment is composed of a set of farm features
 */
public class Farm implements Segment{
	private HashSet<Position> coveredPos;
	/**
	 * Function to get the covered position of this farm segment.
	 * @return the set of covered positions
	 */
	public HashSet<Position> getCoveredPos() { return coveredPos; }
	
	private HashSet<FarmFeature> listCoveredFeatures;
	@Override
	public HashSet<FarmFeature> getListCoveredFeatures(){ return listCoveredFeatures; }
	
	private HashMap<Position, FarmFeature> coveredFeatures;
	/**
	 * Function to get the map of this farm segment, the key is position and the value is corresponding farm feature.
	 * @return the map of this segment
	 */
	public HashMap<Position, FarmFeature> getCoveredFeatures() { return coveredFeatures; }
	
	/**
	 * Function to set the neighbor completed city number of this segment.
	 * @param neighborCompletedCity is the number of neigbor completed city number
	 */
	public void setNeighborCompletedCity(int neighborCompletedCity) { this.neighborCompletedCity = neighborCompletedCity; }
	
	private LinkedList<Follower> followers;
	@Override
	public LinkedList<Follower> getFollowers() { return followers; }
	
	private LinkedList<CityFeature> adjCityFeatures;
	/**
	 * Function to get the adjacent city features of this farm segment.
	 * @return the adjacent city features of this farm segment
	 */
	public LinkedList<CityFeature> getAdiCityFeatures() { return adjCityFeatures; }
	/**
	 * Function to add adjacent city features to this farm segment.
	 * @param add is the list of adjacent city features
	 */
	public void addAdjCityFeature(LinkedList<CityFeature> add) {
		for(CityFeature feature : add) {
			if(!adjCityFeatures.contains(feature)) adjCityFeatures.add(feature);
		}
	}
	
	private HashSet<Position> neighborPos;
	/**
	 * neighborPos represents the position where can be added a new tile to this road segment.
	 * if the length of neighbor position is 0, it means this segment is already completed
	 * @return the neighbor positions
	 */
	public HashSet<Position> getNeighborPos() { return neighborPos; }
	
	public Farm() {
		coveredPos = new HashSet<Position>();
		listCoveredFeatures = new HashSet<FarmFeature>();
		coveredFeatures = new HashMap<Position, FarmFeature>();
		followers = new LinkedList<Follower>();
		adjCityFeatures = new LinkedList<CityFeature>();
		neighborCompletedCity = 0;
		neighborPos = new HashSet<Position>();
	}
	
	private void updateNeighborPos() {
		neighborPos = new HashSet<Position>();
		for(Position pos : coveredPos) {
			int x = pos.getX();
			int y = pos.getY();
			FarmFeature farmFeature = coveredFeatures.get(pos);
			if(farmFeature.getFarmFeatures().contains(1) && farmFeature.getFarmNeighbors().contains("up")) {
				if(coveredFeatures.get(new Position(x, y - 1)) == null || 
						!(coveredFeatures.get(new Position(x, y - 1)).getFarmNeighbors().contains("down") && 
								coveredFeatures.get(new Position(x, y - 1)).getFarmFeatures().contains(2))) {
					neighborPos.add(new Position(x, y - 1));
				}
			}
			if(farmFeature.getFarmFeatures().contains(4) && farmFeature.getFarmNeighbors().contains("up")) {
				if(coveredFeatures.get(new Position(x, y - 1)) == null || 
						!(coveredFeatures.get(new Position(x, y - 1)).getFarmNeighbors().contains("down") && 
								!coveredFeatures.get(new Position(x, y - 1)).getFarmFeatures().contains(3))) {
					neighborPos.add(new Position(x, y - 1));
				}
			}
			if(farmFeature.getFarmFeatures().contains(1) && farmFeature.getFarmNeighbors().contains("left")) {
				if(coveredFeatures.get(new Position(x - 1, y)) == null || 
						!(coveredFeatures.get(new Position(x - 1, y)).getFarmNeighbors().contains("right") && 
								coveredFeatures.get(new Position(x - 1, y)).getFarmFeatures().contains(4))) {
					neighborPos.add(new Position(x - 1, y));
				}
			}
			if(farmFeature.getFarmFeatures().contains(2) && farmFeature.getFarmNeighbors().contains("left")) {
				if(coveredFeatures.get(new Position(x - 1, y)) == null || 
						!(coveredFeatures.get(new Position(x - 1, y)).getFarmNeighbors().contains("right") && 
								coveredFeatures.get(new Position(x - 1, y)).getFarmFeatures().contains(3))) {
					neighborPos.add(new Position(x - 1, y));
				}
			}
			if(farmFeature.getFarmFeatures().contains(2) && farmFeature.getFarmNeighbors().contains("down")) {
				if(coveredFeatures.get(new Position(x, y + 1)) == null || 
						!(coveredFeatures.get(new Position(x, y + 1)).getFarmNeighbors().contains("up") && 
								coveredFeatures.get(new Position(x, y + 1)).getFarmFeatures().contains(1))) {
					neighborPos.add(new Position(x, y + 1));
				}
			}
			if(farmFeature.getFarmFeatures().contains(3) && farmFeature.getFarmNeighbors().contains("down")) {
				if(coveredFeatures.get(new Position(x, y + 1)) == null || 
						!(coveredFeatures.get(new Position(x, y + 1)).getFarmNeighbors().contains("up") && 
								coveredFeatures.get(new Position(x, y + 1)).getFarmFeatures().contains(4))) {
					neighborPos.add(new Position(x, y + 1));
				}
			}
			if(farmFeature.getFarmFeatures().contains(3) && farmFeature.getFarmNeighbors().contains("right")) {
				if(coveredFeatures.get(new Position(x + 1, y)) == null || 
						!(coveredFeatures.get(new Position(x + 1, y)).getFarmNeighbors().contains("left") && 
								coveredFeatures.get(new Position(x + 1, y)).getFarmFeatures().contains(2))) {
					neighborPos.add(new Position(x + 1, y));
				}
			}
			if(farmFeature.getFarmFeatures().contains(4) && farmFeature.getFarmNeighbors().contains("right")) {
				if(coveredFeatures.get(new Position(x + 1, y)) == null || 
						!(coveredFeatures.get(new Position(x + 1, y)).getFarmNeighbors().contains("left") && 
								coveredFeatures.get(new Position(x + 1, y)).getFarmFeatures().contains(1))) {
					neighborPos.add(new Position(x + 1, y));
				}
			}
		}
	}
	
	@Override
	public boolean checkValidPos(TileFeature tileFeature, Position pos) {
		if(!(tileFeature instanceof FarmFeature)) return false;
		FarmFeature farmFeature = (FarmFeature)tileFeature;
		if(!neighborPos.contains(pos)) return false;
		int x = pos.getX();
		int y = pos.getY();
		
		//to guarantee guarantee at least one of the edge matching with the neighbor
		boolean least = false;
		FarmFeature neighbor = coveredFeatures.get(new Position(x, y - 1));
		if(neighbor != null) {
			least = least || farmFeature.checkUpMatch(neighbor);
		}
		neighbor = coveredFeatures.get(new Position(x - 1, y));
		if(neighbor != null) {
			least = least || farmFeature.checkLeftMatch(neighbor);
		}
		neighbor = coveredFeatures.get(new Position(x, y + 1));
		if(neighbor != null) {
			least = least || farmFeature.checkDownMatch(neighbor);
		}
		neighbor = coveredFeatures.get(new Position(x + 1, y));
		if(neighbor != null) {
			least = least || farmFeature.checkRightMatch(neighbor);
		}
		return least;
	}
	
	@Override
	public void addTile(TileFeature tileFeature, Position pos) {
		if(!(tileFeature instanceof FarmFeature)) return;
		FarmFeature farmFeature = (FarmFeature)tileFeature;
		farmFeature.setPos(pos);
		coveredPos.add(pos);
		if(coveredFeatures.get(pos) != null) {
			LinkedList<Integer> farmfeatures = new LinkedList<Integer>();
			for(Integer dir : coveredFeatures.get(pos).getFarmFeatures()) {
				if(!farmfeatures.contains(dir)) farmfeatures.add(dir);
			}
			for(Integer dir : farmFeature.getFarmFeatures()) {
				if(!farmfeatures.contains(dir)) farmfeatures.add(dir);
			}
			LinkedList<String> neighborfeatures = new LinkedList<String>();
			for(String nei : coveredFeatures.get(pos).getFarmNeighbors()) {
				if(!neighborfeatures.contains(nei)) neighborfeatures.add(nei);
			}
			for(String nei : farmFeature.getFarmNeighbors()) {
				if(!neighborfeatures.contains(nei)) neighborfeatures.add(nei);
			}
			FarmFeature newFeature = new FarmFeature(farmfeatures, neighborfeatures);
			newFeature.setPos(pos);
			coveredFeatures.put(pos, newFeature);
			listCoveredFeatures.add(farmFeature);
		}else {
			coveredFeatures.put(pos, farmFeature);
			listCoveredFeatures.add(farmFeature);
		}
		updateNeighborPos();
	}

	@Override
	public boolean checkCompletion() {
		// farm segments would never be completed
		return false;
	}

	private int neighborCompletedCity;
	public int getNeighborCompletedCity() { return neighborCompletedCity; }
	public void updateNeighbor(City city) {
		for(CityFeature adj : adjCityFeatures) {
			if(city.getListCoveredFeatures().contains(adj)) {
				neighborCompletedCity += 1;
				return;
			}
		}
	}
	
	private final int completedCityUnitScore = 3;
	@Override
	public int calculateScore() {
		return neighborCompletedCity * completedCityUnitScore;
	}

	@Override
	public boolean checkOverlap(Segment segment) {
		if(!(segment instanceof Farm)) return false;
		Farm farm = (Farm)segment;
		
		HashSet<FarmFeature> tmp = new HashSet<FarmFeature>(listCoveredFeatures);
		tmp.retainAll(farm.getListCoveredFeatures());
		if(tmp.size() == 0) return false;
		Position pos = new Position(0, 0);
		for(FarmFeature feature : tmp) {
			int x = feature.getPos().getX();
			int y = feature.getPos().getY();
			pos = new Position(x, y);
		}
		listCoveredFeatures.addAll(farm.getListCoveredFeatures());
		coveredPos.addAll(farm.getCoveredPos());
		combineCoveredFeatures(farm.getCoveredFeatures());
		followersPos.addAll(farm.getFollowersPos());
		followers.addAll(farm.getFollowers());
		adjCityFeatures.addAll(farm.getAdiCityFeatures());
		updateNeighborPos();
		return true;
	}
	
	private void combineCoveredFeatures(HashMap<Position, FarmFeature> covered) {
		for(Position position : covered.keySet()) {
			if(coveredFeatures.get(position) == null) {
				coveredFeatures.put(position, covered.get(position));
			}else {
				FarmFeature feature1 = coveredFeatures.get(position);
				FarmFeature feature2 = covered.get(position);
				LinkedList<Integer> farmfeature = new LinkedList<Integer>();
				for(Integer dir : feature1.getFarmFeatures()) {
					if(!farmfeature.contains(dir)) farmfeature.add(dir);
				}
				for(Integer dir : feature2.getFarmFeatures()) {
					if(!farmfeature.contains(dir)) farmfeature.add(dir);
				}
				LinkedList<String> farmneighbor = new LinkedList<String>();
				for(String str : feature1.getFarmNeighbors()) {
					if(!farmneighbor.contains(str)) farmneighbor.add(str);
				}
				for(String str : feature2.getFarmNeighbors()) {
					if(!farmneighbor.contains(str)) farmneighbor.add(str);
				}
				FarmFeature newFeature = new FarmFeature(farmfeature, farmneighbor);
				coveredFeatures.put(position, newFeature);
			}
		}
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
