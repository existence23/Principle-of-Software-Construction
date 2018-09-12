package edu.cmu.cs.cs214.hw4.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Random;

import org.yaml.snakeyaml.Yaml;

import edu.cmu.cs.cs214.hw4.segments.City;
import edu.cmu.cs.cs214.hw4.segments.Cloister;
import edu.cmu.cs.cs214.hw4.segments.Farm;
import edu.cmu.cs.cs214.hw4.segments.Road;
import edu.cmu.cs.cs214.hw4.segments.Segment;
import edu.cmu.cs.cs214.hw4.tileFeature.CityFeature;
import edu.cmu.cs.cs214.hw4.tileFeature.CloisterFeature;
import edu.cmu.cs.cs214.hw4.tileFeature.FarmFeature;
import edu.cmu.cs.cs214.hw4.tileFeature.RoadFeature;
import edu.cmu.cs.cs214.hw4.tileFeature.TileFeature;

/**
 * Class to represent the game system of a Carcassonne game.
 */
public class GameSystem {

	/** The initial size boundary of the game system. */
	public final int GRID_SIZE = 20;

	private LinkedList<Tile> tiles;

	/**
	 * Function to get the initial tiles of the Carcassonne game.
	 * 
	 * @return the initial tiles list
	 */
	public LinkedList<Tile> getTiles() {
		return tiles;
	}

	protected LinkedList<Player> players;

	/**
	 * Function to get the players of the Carcassonne game.
	 * 
	 * @return the player list
	 */
	public LinkedList<Player> getPlayers() {
		return players;
	}

	private GameBoard gameBoard;

	/**
	 * Function to get the game board of the Carcassonne game.
	 * 
	 * @return the game board
	 */
	public GameBoard getGameBoard() {
		return gameBoard;
	}

	private GameSystemTile parse(String fileName) {
		Yaml yaml = new Yaml();
		try (InputStream is = new FileInputStream(fileName)) {
			return yaml.loadAs(is, GameSystemTile.class);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("File " + fileName + " not found!");
		} catch (IOException e) {
			throw new IllegalArgumentException("Error when reading " + fileName + "!");
		}
	}

	/**
	 * Constructor of GameSystem, set the initial tiles and the players according to
	 * the input player number.
	 * 
	 * @param playerNum
	 *            is the number of players this game has
	 */
	public GameSystem(int playerNum) {
		GameSystemTile totalTiles = parse("src/main/resources/Tile.yml");
		tiles = totalTiles.getInventory();
		players = new LinkedList<Player>();
		for (int i = 0; i < playerNum; i++) {
			Player newPlayer = new Player();
			Character name = new Character((char) (65 + i));
			newPlayer.setPlayerName(name.toString());
			players.add(newPlayer);
		}
		gameBoard = new GameBoard();
	}

	private int initialTileType = 24;
	private Random rand = new Random();
	private final TileCopier copier = new TileCopier();

	/**
	 * Helper function to test the game system, assuming the pick tile could be
	 * placed to game board validly.
	 * 
	 * @param tileNum
	 *            is the index of the tile in tiles
	 * @return the picked tile
	 */
	public Tile pickTile(char tileNum) {
		int index = tileNum - 65;
		Tile tile = copier.copy(tiles.get(index));
		tile.setQuantity(1);
		return tile;
	}

	private int leftTileNum = 72;
	/**
	 * Function to get the left tiles number.
	 * @return the left tile number
	 */
	public int getLeftTileNum() { return leftTileNum; }
	/**
	 * Function to pick one random tile from the system's left tile. Check this tile
	 * could be placed in the game board or not, if not, pick another random tile
	 * until pick the valid one
	 * 
	 * @return the random valid tile
	 * @throws Exception
	 *             if there is not valid tile left in the system in which condition,
	 *             game over
	 */
	public Tile pickRandomTile() {
		Tile randomTile = new Tile();

		while (true) {
			if (initialTileType == 0) {
				gameOver();
				// throw new Exception("Error: No Tile Left! GAME OVER!");
				return null;
			}
			leftTileNum -= 1;
			int index = rand.nextInt(initialTileType);
			randomTile = copier.copy(tiles.get(index));
			randomTile.setQuantity(1);
			int initialQuantity = tiles.get(index).getQuantity();
			if (initialQuantity != 1) {
				tiles.get(index).setQuantity(initialQuantity - 1);
			} else {
				tiles.remove(index);
				initialTileType -= 1;
			}
			if (gameBoard.isValidTile(randomTile))
				break;
		}
		return randomTile;
	}

	/**
	 * When no tiles left, this function would be called and print all the score of
	 * the players. farm segments score would be updated at this time
	 */
	public void gameOver() {
		updateScore();
		updateFarmScore();
		System.out.println("Game Over!");
		for (int i = 0; i < players.size(); i++) {
			int num = i + 1;
			System.out.println("Player" + num + " total score: " + players.get(i).getScore());
		}
	}

	/**
	 * Helper function to update the score of farm segments. farm score would only
	 * be updated when game over
	 */
	private void updateFarmScore() {
		for (Farm farm : gameBoard.getFarmSegments()) {
			for (City city : gameBoard.getCitySegments()) {
				if (city.checkCompletion()) {
					farm.updateNeighbor(city);
					updateSegScore(farm);
				}
			}
		}
	}

	// help to record the last updated position on the game board
	private Position lastPos = null;

	/**
	 * Function to place tile in the game board of this system.
	 * 
	 * @param tile
	 *            is the input tile
	 * @param pos
	 *            is the position one the game board the tile to be placed
	 * @return true if the tile is successfully placed and update the gameBoard
	 *         false if this tile could not be placed to this position
	 */
	public boolean placeTile(Tile tile, Position pos) {
		if (gameBoard.placeTile(tile, pos)) {
			lastPos = new Position(pos.getX(), pos.getY());
			return true;
		}
		return false;
	}

	/**
	 * Helper function to help to update the score of players if this segment is
	 * completed.
	 * 
	 * @param seg is the completed segment
	 */
	private void updateSegScore(Segment seg) {
		LinkedList<Integer> playerControl = new LinkedList<Integer>();
		for (Player player : players) {
			int control = 0;
			for (Follower follower : seg.getFollowers()) {
				if (player.getFollowers().contains(follower)) {
					control += 1;
					player.getFollowers().remove(follower);
					int initial = player.leftFollowers();
					player.setLeftFollower(initial + 1);
				}
			}
			playerControl.add(control);
		}
		int maxControl = Integer.MIN_VALUE;
		for (Integer controlNum : playerControl) {
			maxControl = Math.max(maxControl, controlNum);
		}
		if (maxControl == 0)
			return;
		for (int i = 0; i < players.size(); i++) {
			if (playerControl.get(i).equals(maxControl)) {
				players.get(i).addScore(seg.calculateScore());
			}
		}
		seg.getFollowers().removeAll(seg.getFollowers());
	}

	/**
	 * Function to update the scores of each player once on segment is
	 * completed(except farm).
	 * This function would be called when the game change to the next turn
	 * (which means the action of this turn has been completed)
	 */
	public void updateScore() {
		for (City city : gameBoard.getCitySegments()) {
			if (city.checkCompletion())
				updateSegScore(city);
		}
		for (Cloister cloister : gameBoard.getCloisterSegments()) {
			if (cloister.checkCompletion())
				updateSegScore(cloister);
		}
		for (Road road : gameBoard.getRoadSegments()) {
			if (road.checkCompletion())
				updateSegScore(road);
		}
	}

	/**
	 * Helper function to check to create the TileFeature of city feature, road
	 * feature cloister feature and the farm feature.
	 * @param tile is the tile to be checked
	 * @param position is the integer to represent the specified position on this tile
	 * 0 - 3 is to represent one of the edge of this tile
	 * 4 is to represent the middle of this tile
	 * 5 - 8 is to represent one of the vertex of this tile
	 * @return the created feature, if the position is invalid, return null
	 */
	private TileFeature createFeature(Tile tile, int position) {
		if (position < 0 || position > 8) return null;
		if(position > 4) {
			int vertex = position - 4;
			for(int i = 0; i < tile.getFarmFeatures().size(); i++) {
				if(tile.getFarmFeatures().get(i).contains(vertex)) {
					LinkedList<Integer> farmFeatures = new LinkedList<Integer>();
					farmFeatures.addAll(tile.getFarmFeatures().get(i));
					LinkedList<String> farmNeighbors = new LinkedList<String>();
					farmNeighbors.addAll(tile.getFarmNeighbor().get(i));
					FarmFeature farmFeature = new FarmFeature(farmFeatures, farmNeighbors);
					farmFeature.setPos(tile.getPosition());
					return farmFeature;
				}
			}
			return null;
		}
		/*
		 * Check the middle feature is cloister or not if is cloister, create and return
		 * the cloister feature
		 */
		if (position == 4) {
			if (!tile.getMiddleFeature().equals("cloister")) return null;
			CloisterFeature cloisterFeature = new CloisterFeature();
			cloisterFeature.setPos(tile.getPosition());
			return cloisterFeature;
		}
		// check the city feature
		if (tile.getEdgeFeatures().get(position).equals("city")) {
			CityFeature cityFeature = new CityFeature(tile.existPennant());
			cityFeature.setPos(tile.getPosition());
			cityFeature.addDirection(position);
			if (tile.getMiddleFeature().equals("city")) {
				for(int i = 0; i < 4; i++) {
					if(i != position && tile.getEdgeFeatures().get(i).equals("city")) {
						cityFeature.addDirection(i);
					}
				}
			}
			return cityFeature;
		}
		//check the road feature
		if(tile.getEdgeFeatures().get(position).equals("road")) {
			RoadFeature roadFeature = new RoadFeature();
			roadFeature.setPos(tile.getPosition());
			roadFeature.addDirection(position);
			if(!tile.getMiddleFeature().equals("cross")) {
				for(int i = 0; i < 4; i++) {
					if(i != position && tile.getEdgeFeatures().get(i).equals("road")) {
						roadFeature.addDirection(i);
					}
				}
			}
			return roadFeature;
		}
		return null;
	}
	
	/**
	 * Function to place follower on the chosen tile feature.
	 * 
	 * @param tileFeature
	 *            is the chosen feature in one specific tile
	 * @param playerNum
	 *            is the index of player who place this follower
	 * @return true if successfully placed, false if not This function would be
	 *         called after the placement of the tile, after the placement of the
	 *         follower the system would automatically update the score and return
	 *         the followers to players if any segments completed
	 */
	public boolean placeFollower(TileFeature feature, int index) {
		if (feature == null)
			return false;
		if (players.get(index).leftFollowers() == 0)
			return false;
		if (!lastPos.equals(feature.getPos()))
			return false;
		boolean couldPlace = false;
		for (City city : gameBoard.getCitySegments()) {
			if (city.getListCoveredFeatures().contains(feature)) {
				if (city.getFollowers().size() > 0)
					return false;
				couldPlace = true;
				players.get(index).placeFollower(feature.getPos());
				city.addFollower(feature.getPos());
			}
		}
		for (Farm farm : gameBoard.getFarmSegments()) {
			if (farm.getListCoveredFeatures().contains(feature)) {
				if (farm.getFollowers().size() > 0)
					return false;
				couldPlace = true;
				players.get(index).placeFollower(feature.getPos());
				farm.addFollower(feature.getPos());
			}
		}
		for (Cloister cloister : gameBoard.getCloisterSegments()) {
			if (cloister.getListCoveredFeatures().contains(feature)) {
				if (cloister.getFollowers().size() > 0)
					return false;
				couldPlace = true;
				players.get(index).placeFollower(feature.getPos());
				cloister.addFollower(feature.getPos());
			}
		}
		for (Road road : gameBoard.getRoadSegments()) {
			if (road.getListCoveredFeatures().contains(feature)) {
				if (road.getFollowers().size() > 0)
					return false;
				couldPlace = true;
				players.get(index).placeFollower(feature.getPos());
				road.addFollower(feature.getPos());
			}
		}
		updateScore();
		return couldPlace;
	}
	
	public boolean placeFollower(Tile tile, int position, int index) {
		if(tile == null) return false;
		TileFeature feature = createFeature(tile, position);
		if (feature == null)
			return false;
		if (players.get(index).leftFollowers() == 0)
			return false;
		if (!lastPos.equals(feature.getPos()))
			return false;
		boolean couldPlace = false;
		for (City city : gameBoard.getCitySegments()) {
			if (city.getListCoveredFeatures().contains(feature)) {
				if (city.getFollowers().size() > 0)
					return false;
				couldPlace = true;
				players.get(index).placeFollower(feature.getPos());
				city.addFollower(feature.getPos());
			}
		}
		for (Farm farm : gameBoard.getFarmSegments()) {
			if (farm.getListCoveredFeatures().contains(feature)) {
				if (farm.getFollowers().size() > 0)
					return false;
				couldPlace = true;
				players.get(index).placeFollower(feature.getPos());
				farm.addFollower(feature.getPos());
			}
		}
		for (Cloister cloister : gameBoard.getCloisterSegments()) {
			if (cloister.getListCoveredFeatures().contains(feature)) {
				if (cloister.getFollowers().size() > 0)
					return false;
				couldPlace = true;
				players.get(index).placeFollower(feature.getPos());
				cloister.addFollower(feature.getPos());
			}
		}
		for (Road road : gameBoard.getRoadSegments()) {
			if (road.getListCoveredFeatures().contains(feature)) {
				if (road.getFollowers().size() > 0)
					return false;
				couldPlace = true;
				players.get(index).placeFollower(feature.getPos());
				road.addFollower(feature.getPos());
			}
		}
		return couldPlace;
	}
}
