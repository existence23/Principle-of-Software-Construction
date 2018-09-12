package edu.cmu.cs.cs214.hw4.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import edu.cmu.cs.cs214.hw4.segments.City;
import edu.cmu.cs.cs214.hw4.segments.Cloister;
import edu.cmu.cs.cs214.hw4.segments.Farm;
import edu.cmu.cs.cs214.hw4.segments.Road;
import edu.cmu.cs.cs214.hw4.tileFeature.CityFeature;
import edu.cmu.cs.cs214.hw4.tileFeature.FarmFeature;
import edu.cmu.cs.cs214.hw4.tileFeature.RoadFeature;

/**
 * Function to represent the game board of the Carcassonne game.
 */
public class GameBoard {
	private HashMap<Position, Tile> map;
	/**
	 * Function to get the map of this game board, the key is position and the value is covered tiles.
	 * @return the map of this game board
	 */
	public HashMap<Position, Tile> getTileMap() { return map; }
	
	private LinkedList<Position> availablePos;
	/**
	 * Function to get the list of available position of this game board that new tile could be placed.
	 * @return the list of available positions
	 */
	public LinkedList<Position> getAvailablePosition() { return availablePos; }
	
	private List<Road> roadSegments;
	/**
	 * Function to get the list of road segments.
	 * @return the road segments list
	 */
	public List<Road> getRoadSegments() { return roadSegments; }
	
	private List<City> citySegments;
	/**
	 * Function to get the list of city segments.
	 * @return the city segments list
	 */
	public List<City> getCitySegments() { return citySegments; }
	
	private List<Cloister> cloisterSegments;
	/**
	 * Function to get the list of cloister segments.
	 * @return the cloister segments
	 */
	public List<Cloister> getCloisterSegments() { return cloisterSegments; }
	
	private List<Farm> farmSegments;
	/**
	 * Function to get the list of farm segments.
	 * @return the farm segments
	 */
	public List<Farm> getFarmSegments() { return farmSegments; }
	
	private final TileCopier copier = new TileCopier();
	
	/**
	 * Constructor of GameBoard, initialize map and availablePos.
	 */
	public GameBoard() {
		map = new HashMap<Position, Tile>();
		availablePos = new LinkedList<Position>();
		//when the game board is empty, then the initial available position set to be (0, 0)
		availablePos.add(new Position(0, 0));
		roadSegments = new LinkedList<Road>();
		citySegments = new LinkedList<City>();
		cloisterSegments = new LinkedList<Cloister>();
		farmSegments = new LinkedList<Farm>();
	}
	
	/**
	 * This help function used to check when one specific tile is placed in one specific position is valid or not.
	 * @param tile the tile needed to be placed
	 * @param pos the position needed to be checked
	 * @return true if valid, false if not
	 */
	private boolean isValidPosition(Tile tile, Position pos) {
		int x = pos.getX();
		int y = pos.getY();
		//check up tile
		if(map.get(new Position(x, y - 1)) != null && 
				!map.get(new Position(x, y - 1)).getEdgeFeatures().get(2).equals(tile.getEdgeFeatures().get(0))) return false;
		//check down tile
		if(map.get(new Position(x, y + 1)) != null && 
				!map.get(new Position(x, y + 1)).getEdgeFeatures().get(0).equals(tile.getEdgeFeatures().get(2))) return false;
		//check left tile
		if(map.get(new Position(x - 1, y)) != null && 
				!map.get(new Position(x - 1, y)).getEdgeFeatures().get(3).equals(tile.getEdgeFeatures().get(1))) return false;
		//check right tile
		if(map.get(new Position(x + 1, y)) != null && 
				!map.get(new Position(x + 1, y)).getEdgeFeatures().get(1).equals(tile.getEdgeFeatures().get(3))) return false;
		return true;
	}
	/**
	 * Check the input tile whether can be placed in this game board.
	 * @param tile the input tile
	 * @return true if it can be placed validly, false if not
	 */
	public boolean isValidTile(Tile tile) {
		Tile tmpTile = copier.copy(tile);
		for(Position pos : availablePos) {
			for(int j = 0; j < 4; j++) {
				if(isValidPosition(tmpTile, pos)) return true;
				tmpTile.rotate();
			}
		}
		return false;
	}
	
	/**
	 * This function is aim to add a tile on the game board at the position the user choose to place.
	 * if this position is valid, add this tile to the map and update the available position, then return true
	 * if is invalid, return false
	 * @param tile the tile need to be placed
	 * @param pos the chosen position
	 * @return true if valid, false if not
	 */
	public boolean placeTile(Tile tile, Position pos) {
		if(!availablePos.contains(pos) || map.get(pos) != null ||
				 isValidPosition(tile, pos) == false) return false;
		map.put(pos, tile);
		tile.setPosition(pos);
		//update available position
		availablePos.remove(pos);
		int x = pos.getX();
		int y = pos.getY();
		//check left position
		if(map.get(new Position(x - 1, y)) == null) {
			Position newPos = new Position(x - 1, y);
			if(!availablePos.contains(newPos)) availablePos.add(new Position(x - 1, y));
		}
		//check right position
		if(map.get(new Position(x + 1, y)) == null) {
			Position newPos = new Position(x + 1, y);
			if(!availablePos.contains(newPos)) availablePos.add(new Position(x + 1, y));
		}
		//check up position
		if(map.get(new Position(x, y - 1)) == null) {
			Position newPos = new Position(x, y - 1);
			if(!availablePos.contains(newPos)) availablePos.add(new Position(x, y - 1));
		}
		//check down position
		if(map.get(new Position(x, y + 1)) == null) {
			Position newPos = new Position(x, y + 1);
			if(!availablePos.contains(newPos)) availablePos.add(new Position(x, y + 1));
		}
		
		//add this tile to segments
		checkRoad(tile, pos);
		checkCity(tile, pos);
		checkCloister(tile, pos);
		checkFarm(tile, pos);
		
		return true;
	}
	
	/**
	 * Helper function to calculate the number of the road edge feature of one tile.
	 * @param tile the input tile
	 * @return the road feature number
	 */
	private int roadFeature(Tile tile) {
		int roadNum = 0;
		for(int i = 0; i < 4; i++) {
			if(tile.getEdgeFeatures().get(i).equals("road")) roadNum += 1;
		}
		return roadNum;
	}
	
	/**
	 * Helper function to check whether is there any overlap between different segments.
	 * if exists overlap, combine these two segments and remove the abundant one
	 * @return true is exists overlap, false if not
	 */
	private boolean existRoadOverlap() {
		for(int i = 0; i < roadSegments.size(); i++) {
			for(int j = i + 1; j < roadSegments.size(); j++) {
				if(roadSegments.get(i).checkOverlap(roadSegments.get(j))) {
					roadSegments.remove(j);
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Helper function to check whether is there any overlap between different segments.
	 * If exists overlap, then combine these two segments
	 */
	private void checkRoadOverlap() {
		while(true) {
			if(!existRoadOverlap()) return;
		}
	}

	/**
	 * Function to check the input tile could be added to any road segment or not.
	 * @param tile is the input tile
	 * @param pos the position where the tile to be placed
	 */
	public void checkRoad(Tile tile, Position pos) {
		int roadNum = roadFeature(tile);
		if(roadNum == 0) return;
		LinkedList<RoadFeature> roadFeatures = new LinkedList<RoadFeature>();
		if(roadNum <= 2) {
			RoadFeature feature = new RoadFeature();
			for(int i = 0; i < 4; i++) {
				if(tile.getEdgeFeatures().get(i).equals("road")) {
					feature.addDirection(i);
				}
			}
			feature.setPos(pos);
			roadFeatures.add(feature);
		}else {
			for(int i = 0; i < 4; i++) {
				if(tile.getEdgeFeatures().get(i).equals("road")) {
					RoadFeature feature = new RoadFeature();
					feature.addDirection(i);
					feature.setPos(pos);
					roadFeatures.add(feature);
				}
			}
		}
		if(roadSegments.size() == 0) {
			for(int i = 0; i < roadFeatures.size(); i++) {
				Road road = new Road();
				road.addTile(roadFeatures.get(i), pos);
				roadSegments.add(road);
			}
		}else {
			int length = roadSegments.size();
			for(RoadFeature feature : roadFeatures) {
				boolean couldPlace = false;
				for(int i = 0; i < length; i++) {
					if(roadSegments.get(i).checkValidPos(feature, pos)) {
						couldPlace = true;
						roadSegments.get(i).addTile(feature, pos);
					} 
				}
				if(couldPlace == false) {
					Road newRoad = new Road();
					newRoad.addTile(feature, pos);
					roadSegments.add(newRoad);
				}
			}
		}
		checkRoadOverlap();
	}
	
	/**
	 * Helper function to calculate the number of the city edge features of the given tile.
	 * @param tile is given city
	 * @return the edge city number
	 */
	private int cityFeatureNum(Tile tile) {
		int num = 0;
		for(int i = 0; i < tile.getEdgeFeatures().size(); i++) {
			if(tile.getEdgeFeatures().get(i).equals("city")) num += 1;
		}
		return num;
	}
	
	/**
	 * Helper function to check whether is there any overlap between different city segments.
	 * @return true if exists, false if not
	 */
	private boolean existCityOverlap() {
		for(int i = 0; i < citySegments.size(); i++) {
			for(int j = i + 1; j < citySegments.size(); j++) {
				if(citySegments.get(i).checkOverlap(citySegments.get(j))) {
					citySegments.remove(j);
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Helper function to check whether is there any overlap between different city segments.
	 * If exists overlap, then combine these two segments
	 */
	private void checkCityOverlap() {
		while(true) {
			if(!existCityOverlap()) return;
		}
	}
	
	/**
	 * Function to check the input tile could be added to any city segment or not.
	 * @param tile is the input tile
	 * @param pos the position where the tile to be placed
	 */
	public void checkCity(Tile tile, Position pos) {
		int cityNum = cityFeatureNum(tile);
		if(cityNum == 0) return;
		LinkedList<CityFeature> cityFeatures = new LinkedList<CityFeature>();
		if(cityNum == 2 && !tile.getMiddleFeature().equals("city")) {
			for(int i = 0; i < tile.getEdgeFeatures().size(); i++) {
				if(tile.getEdgeFeatures().get(i).equals("city")) {
					CityFeature feature = new CityFeature(tile.existPennant());
					feature.addDirection(i);
					feature.setPos(pos);
					cityFeatures.add(feature);
				}
			}
		}else {
			CityFeature feature = new CityFeature(tile.existPennant());
			for(int i = 0; i < 4; i++) {
				if(tile.getEdgeFeatures().get(i).equals("city")) feature.addDirection(i);
			}
			feature.setPos(pos);
			cityFeatures.add(feature);
		}
		
		if(citySegments.size() == 0) {
			for(int i = 0; i < cityFeatures.size(); i++) {
				City city = new City();
				city.addTile(cityFeatures.get(i), pos);
				citySegments.add(city);
			}
		}else {
			int length = citySegments.size();
			for(CityFeature feature : cityFeatures) {
				boolean couldPlace = false;
				for(int i = 0; i < length; i++) {
					if(citySegments.get(i).checkValidPos(feature, pos)) {
						couldPlace = true;
						citySegments.get(i).addTile(feature, pos);
					}
				}
				if(couldPlace == false) {
					City newCity = new City();
					newCity.addTile(feature, pos);
					citySegments.add(newCity);
				}
			}
		}
		checkCityOverlap();
	}
	
	/**
	 * Helper function to get the 9 surrounding position of the given position.
	 * @param pos is the given position
	 * @return the list of surrounding position
	 */
	private LinkedList<Position> surroundPos(Position pos){
		LinkedList<Position> surrounding = new LinkedList<Position>();
		int x = pos.getX();
		int y = pos.getY();
		surrounding.add(new Position(x, y - 1));
		surrounding.add(new Position(x - 1, y - 1));
		surrounding.add(new Position(x - 1, y));
		surrounding.add(new Position(x - 1, y + 1));
		surrounding.add(new Position(x, y + 1));
		surrounding.add(new Position(x + 1, y + 1));
		surrounding.add(new Position(x + 1, y));
		surrounding.add(new Position(x + 1, y - 1));
		return surrounding;
	}
	/**
	 * Function to check the input tile could be added to any cloister segment or not.
	 * @param tile is the input tile
	 * @param pos the position where the tile to be placed
	 */
	public void checkCloister(Tile tile, Position pos) {
		if(tile.getMiddleFeature().equals("cloister")) {
			Cloister cloister = new Cloister(pos);
			cloister.addTile(pos);
			for(Position surround : surroundPos(pos)) {
				if(map.get(surround) != null) { cloister.addTile(surround); }
			}
			cloisterSegments.add(cloister);
		}
			for(Cloister cloister : cloisterSegments) {
				for(Position surround : surroundPos(pos)) {
					if(cloister.getCloisterPos().equals(surround)) {
						cloister.addTile(pos);
					}
				}
			}
		
	}
	
	/**
	 * Helper function to check whether is there any overlap between different farm segments.
	 * If exists overlap, then combine these two segments and remove the abundant one
	 * @return true if exists overlap, false if not
	 */
	private boolean existFarmOverlap() {
		for(int i = 0; i < farmSegments.size(); i++) {
			for(int j = i + 1; j < farmSegments.size(); j++) {
				if(farmSegments.get(i).checkOverlap(farmSegments.get(j))) {
					farmSegments.remove(j);
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Helper function to check whether is there any overlap between different farm segments.
	 * If exists overlap, then combine these two segments
	 */
	private void checkFarmOverlap() {
		while(true) {
			if(!existFarmOverlap()) return;
		}
	}
	private LinkedList<CityFeature> tileCityFeature(Tile tile, 
			LinkedList<Integer> adjCity, Position pos){
		LinkedList<CityFeature> features = new LinkedList<CityFeature>();
		if(adjCity.size() == 2 && !tile.getMiddleFeature().equals("city")) {
			for(int i = 0; i < adjCity.size(); i++) {
				CityFeature cityFeature = new CityFeature(false);
				cityFeature.addDirection(adjCity.get(i) - 1);
				cityFeature.setPos(pos);
				features.add(cityFeature);
			}
		}else {
			CityFeature cityFeature = new CityFeature(tile.existPennant());
			for(Integer dir : adjCity) {
				cityFeature.addDirection(dir - 1);
			}
			cityFeature.setPos(pos);
			features.add(cityFeature);
		}
		return features;
	}
	/**
	 * Function to check the input tile could be added to any farm segment or not.
	 * @param tile is the input tile
	 * @param pos the position where the tile to be placed
	 */
	public void checkFarm(Tile tile, Position pos) {
		int farmNum = tile.getFarmFeatures().size();
		if(farmNum == 0) return;
		LinkedList<FarmFeature> farmFeatures = new LinkedList<FarmFeature>();
		LinkedList<LinkedList<Integer>> adjCity = new LinkedList<LinkedList<Integer>>();
		for(int i = 0; i < farmNum; i++) {
			FarmFeature feature = new FarmFeature(tile.getFarmFeatures().get(i), tile.getFarmNeighbor().get(i));
			feature.setPos(pos);
			farmFeatures.add(feature);
			adjCity.add(new LinkedList<Integer>(tile.getAdjCity().get(i)));
		}
		if(farmSegments.size() == 0) {
			for(int i = 0; i < farmFeatures.size(); i++) {
				Farm farm = new Farm();
				farm.addTile(farmFeatures.get(i), pos);
				if(adjCity.get(i).size() > 0) {
					LinkedList<CityFeature> listAdjCity = tileCityFeature(tile, adjCity.get(i), pos);
				    farm.addAdjCityFeature(listAdjCity);
				}
				farmSegments.add(farm);
			}
		}else {
			
			int length = farmSegments.size();
			for(int i = 0; i < farmFeatures.size(); i++) {
				boolean couldPlace = false;
				for(int j = 0; j < length; j++) {
					if(farmSegments.get(j).checkValidPos(farmFeatures.get(i), pos)) {
						couldPlace = true;
						farmSegments.get(j).addTile(farmFeatures.get(i), pos);
						if(adjCity.get(i).size() > 0) {
							farmSegments.get(j).addAdjCityFeature(tileCityFeature(tile, adjCity.get(i), pos));
						}
					}
				}
				if(couldPlace == false) {
					Farm newFarm = new Farm();
					newFarm.addTile(farmFeatures.get(i), pos);
					if(adjCity.get(i).size() > 0) {
						newFarm.addAdjCityFeature(tileCityFeature(tile, adjCity.get(i), pos));
					}
					
					farmSegments.add(newFarm);
				}
			}
		}
		checkFarmOverlap();
	}
}
