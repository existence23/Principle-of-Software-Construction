package edu.cmu.cs.cs214.hw4.core;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import edu.cmu.cs.cs214.hw4.segments.City;
import edu.cmu.cs.cs214.hw4.segments.Cloister;
import edu.cmu.cs.cs214.hw4.segments.Farm;
import edu.cmu.cs.cs214.hw4.segments.Road;
import edu.cmu.cs.cs214.hw4.tileFeature.CityFeature;
import edu.cmu.cs.cs214.hw4.tileFeature.FarmFeature;
import edu.cmu.cs.cs214.hw4.tileFeature.RoadFeature;

/**
 * **********************************************************
 * test class to test the Carcassonne game.
 * Some of the explanation and the drawn pictures are contained in the readme file
 * It's better to understanding with the visualization
 *
 */
public class CarcassonneTest {
	
	private final TileCopier copier = new TileCopier();

	private GameSystemTile parse(String fileName) {
		Yaml yaml = new Yaml();
		try(InputStream is = new FileInputStream(fileName)){
			return yaml.loadAs(is, GameSystemTile.class);
		}catch(FileNotFoundException e) {
			throw new IllegalArgumentException("File " + fileName + " not found!");
		}catch (IOException e) {
		    throw new IllegalArgumentException("Error when reading " + fileName + "!");
		}
	}
	
	@Test
	public void testTile() {
		GameSystemTile carcassonne = parse("src/main/resources/Tile.yml");
		LinkedList<Tile> tiles = carcassonne.getInventory();
		Tile tile = tiles.get(0);
		assertEquals(tile.getQuantity(), 2);
		assertFalse(tile.existPennant());
		
		tile.rotate();
		LinkedList<String> edge = new LinkedList<String>();
		edge.add("farm");
		edge.add("farm");
		edge.add("farm");
		edge.add("road");
		for(int i = 0; i < edge.size(); i++) {
			assertEquals(edge.get(i), tile.getEdgeFeatures().get(i));
		}
		LinkedList<LinkedList<Integer>> farmFeature = new LinkedList<LinkedList<Integer>>();
		LinkedList<Integer> oneFarmFeature = new LinkedList<Integer>();
		oneFarmFeature.add(1);
		oneFarmFeature.add(2);
		oneFarmFeature.add(3);
		oneFarmFeature.add(4);
		farmFeature.add(oneFarmFeature);
		assertEquals(farmFeature.size(), tile.getFarmFeatures().size());
		for(int i = 0; i < oneFarmFeature.size(); i++) {
			Integer tmp = oneFarmFeature.get(i);
			assertTrue(tile.getFarmFeatures().get(0).contains(tmp));
		}
		
		Tile tmp = copier.copy(tile);
		assertEquals(tile.getEdgeFeatures(), tmp.getEdgeFeatures());
		tmp.rotate();
		assertFalse(tile.getEdgeFeatures().equals(tmp.getEdgeFeatures()));
	}
	
	/**
	 * Test function to test position class.
	 */
	@Test
	public void testPosition() {
		Position pos1 = new Position(1, 2);
		Position pos2 = null;
		pos2 = new Position(1, 2);
		assertEquals(pos1, pos2);
		assertEquals(pos1.hashCode(), pos2.hashCode());
		assertTrue(pos1.getX() == pos2.getX() && pos1.getY() == pos2.getY());
	}
	
	@Test
	public void testFollower() {
		Follower follower = new Follower(new Position(1, 2));
		Follower newFollower = new Follower(new Position(1, 2));
		assertEquals(follower, newFollower);
		assertEquals(follower.hashCode(), newFollower.hashCode());
	}

	@Test
	public void testPlayer() {
		Player player = new Player();
		assertEquals(player.leftFollowers(), 8);
		assertEquals(player.getFollowers().size(), 0);
		assertEquals(player.getScore(), 0);
		player.placeFollower(new Position(1, 2));
		player.addScore(2);
		assertEquals(player.leftFollowers(), 7);
		assertEquals(player.getFollowers().size(), 1);
		assertEquals(player.getScore(), 2);
	}

	@Test
	public void testInitialGameSystem() throws Exception {
		int playerNum = 3;
		GameSystem  carcassonne = new GameSystem(playerNum);
		assertEquals(carcassonne.getTiles().size(), 24);
		assertEquals(carcassonne.getPlayers().size(), 3);
		
		//test the initial total tile number
		int totalTileNumber = 0;
		for(int i = 0; i < carcassonne.getTiles().size(); i++) {
			totalTileNumber += carcassonne.getTiles().get(i).getQuantity();
		}
		assertEquals(totalTileNumber, 72);
		
		GameBoard gameBoard = carcassonne.getGameBoard();
		assertEquals(gameBoard.getTileMap().size(), 0);
		assertEquals(gameBoard.getAvailablePosition().size(), 1);
		//when game board is empty, all the tiles could be placed on the game board validly
		assertTrue(gameBoard.isValidTile(carcassonne.getTiles().getFirst()));
	}
	
	@Test
	public void testRoadSegments() {
		GameBoard gameBoard = new GameBoard();
		GameSystemTile carcassonne = parse("src/main/resources/Tile.yml");
		LinkedList<Tile> tiles = carcassonne.getInventory();
		Tile tile = tiles.get(11);
		tile.setQuantity(1);
		assertEquals(gameBoard.getAvailablePosition().size(), 1);
		assertEquals(gameBoard.getAvailablePosition().get(0), new Position(0, 0));
		assertFalse(gameBoard.placeTile(tile, new Position(1, 0)));
		assertTrue(gameBoard.placeTile(tile, new Position(0, 0)));
		assertEquals(gameBoard.getRoadSegments().size(), 3);
		assertEquals(gameBoard.getTileMap().size(), 1);
		
		Tile tile1 = tiles.get(1);
		tile1.setQuantity(1);
		assertFalse(gameBoard.isValidTile(tile1));
		
		Tile tile2 = tiles.get(21);
		tile2.setQuantity(1);
		assertEquals(gameBoard.getAvailablePosition().size(), 4);
		assertNull(gameBoard.getTileMap().get(new Position(0, -1)));
		assertTrue(gameBoard.isValidTile(tile2));
		assertFalse(gameBoard.placeTile(tile2, new Position(0, 1)));
		assertTrue(gameBoard.placeTile(tile2, new Position(0, -1)));
		assertEquals(gameBoard.getAvailablePosition().size(), 6);
		assertEquals(gameBoard.getRoadSegments().size(), 3);
		
		
		Tile tile3 = tiles.get(9);
		assertTrue(gameBoard.isValidTile(tile3));
		assertFalse(gameBoard.placeTile(tile3, new Position(0, 1)));
		assertTrue(gameBoard.placeTile(tile3, new Position(-1, -1)));
		assertEquals(gameBoard.getRoadSegments().size(), 3);
		assertEquals(gameBoard.getAvailablePosition().size(), 7);
		assertEquals(gameBoard.getFarmSegments().size(), 3);

		Tile tile4 = copier.copy(tile);
		assertTrue(gameBoard.isValidTile(tile4));
		assertFalse(gameBoard.placeTile(tile4, new Position(-1, 0)));
		tile4.rotate();
		tile4.rotate();
		assertTrue(gameBoard.placeTile(tile4, new Position(-1, 0)));
		assertEquals(gameBoard.getRoadSegments().size(), 4);
		int completeNum = 0;
		for(Road road : gameBoard.getRoadSegments()) {
			if(road.checkCompletion()) {
				completeNum += 1;
				assertEquals(road.getNeighborPos().size(), 0);
				assertEquals(road.calculateScore(), road.getCoveredPos().size() * 1);
			}else {
				assertEquals(road.calculateScore(), 0);
			}
		}
		assertEquals(completeNum, 2);
		assertEquals(gameBoard.getFarmSegments().size(), 3);
		
		gameBoard = new GameBoard();
		Tile tile5 = tiles.get(11);
		tile5.rotate();
		tile5.rotate();
		assertTrue(gameBoard.placeTile(tile5, new Position(0, 0)));
		assertEquals(gameBoard.getRoadSegments().size(), 3);
		
		Tile tile6 = tiles.get(10);
		assertTrue(gameBoard.placeTile(tile6, new Position(1, 0)));
		Tile tile7 = tiles.get(21);
		assertTrue(gameBoard.placeTile(tile7, new Position(1, 1)));
		assertEquals(gameBoard.getRoadSegments().size(), 4);
		
		Tile tile8 = copier.copy(tile7);
		assertTrue(gameBoard.isValidTile(tile8));
		assertFalse(gameBoard.placeTile(tile8, new Position(0, 1)));
		tile8.rotate();
		tile8.rotate();
		assertTrue(gameBoard.placeTile(tile8, new Position(0, 1)));
		assertEquals(gameBoard.getRoadSegments().size(), 3);
		completeNum = 0;
		for(Road road : gameBoard.getRoadSegments()) {
			if(road.checkCompletion()) {
				completeNum += 1;
			}
		}
		assertEquals(completeNum, 0);
		assertEquals(gameBoard.getCitySegments().size(), 2);
		assertEquals(gameBoard.getFarmSegments().size(), 3);
	}
	
	@Test
	public void testCitySegments() {
		GameBoard gameBoard = new GameBoard();
		GameSystemTile carcassonne = parse("src/main/resources/Tile.yml");
		LinkedList<Tile> tiles = carcassonne.getInventory();
		Tile tile = tiles.get(5);
		tile.setQuantity(1);
		assertFalse(gameBoard.placeTile(tile, new Position(1, 0)));
		assertTrue(gameBoard.placeTile(tile, new Position(0, 0)));
		assertEquals(gameBoard.getCitySegments().size(), 1);
		
		gameBoard = new GameBoard();
		Tile tile1 = tiles.get(8);
		assertTrue(gameBoard.placeTile(tile1, new Position(0, 0)));
		assertEquals(gameBoard.getCitySegments().size(), 2);
		for(City city : gameBoard.getCitySegments()) {
			assertEquals(city.getNeighborPos().size(), 1);
		}
		
		Tile tile2 = copier.copy(tiles.get(12));
		assertFalse(gameBoard.placeTile(tile2, new Position(1, 1)));
		tile2.rotate();
		assertTrue(gameBoard.placeTile(tile2, new Position(1, 0)));
		assertEquals(gameBoard.getCitySegments().size(), 2);
		
		Tile tile3 = copier.copy(tiles.get(13));
		tile3.rotate();
		tile3.rotate();
		tile3.rotate();
		assertTrue(gameBoard.placeTile(tile3, new Position(0, 1)));
		assertEquals(gameBoard.getCitySegments().size(), 2);
		
		Tile tile4 = tiles.get(2);
		assertTrue(gameBoard.placeTile(tile4, new Position(1, 1)));
		assertEquals(gameBoard.getCitySegments().size(), 1);
		for(City city : gameBoard.getCitySegments()) {
			assertFalse(city.checkCompletion());
			assertEquals(city.getNeighborPos().size(), 2);
			assertEquals(city.calculateScore(), 0);
		}
		assertEquals(gameBoard.getFarmSegments().size(), 3);
		
		gameBoard = new GameBoard();
		Tile tile5 = tiles.get(8);
		assertTrue(gameBoard.placeTile(tile5, new Position(0, 0)));
		Tile tile6 = copier.copy(tiles.get(12));
		tile6.rotate();
		assertTrue(gameBoard.placeTile(tile6, new Position(1, 0)));
		Tile tile7 = copier.copy(tiles.get(13));
		tile7.rotate();
		tile7.rotate();
		tile7.rotate();
		assertTrue(gameBoard.placeTile(tile7, new Position(0, 1)));
		assertEquals(gameBoard.getCitySegments().size(), 2);
		
		Tile tile8 = tiles.get(12);
		assertTrue(gameBoard.placeTile(tile8, new Position(1, 1)));
		assertEquals(gameBoard.getCitySegments().size(), 1);
		for(City city : gameBoard.getCitySegments()) {
			assertTrue(city.checkCompletion());
			assertEquals(city.calculateScore(), 12);
		}
		assertEquals(gameBoard.getFarmSegments().size(), 4);
	}
	
	@Test
	public void testCloisterSegment() {
		GameBoard gameBoard = new GameBoard();
		GameSystemTile carcassonne = parse("src/main/resources/Tile.yml");
		LinkedList<Tile> tiles = carcassonne.getInventory();
		Tile tile1 = tiles.get(20);
		gameBoard.placeTile(tile1, new Position(0, 0));
		assertEquals(gameBoard.getCloisterSegments().size(), 0);
		Tile tile2 = tiles.get(1);
		assertTrue(gameBoard.placeTile(tile2, new Position(1, 0)));
		assertEquals(gameBoard.getCloisterSegments().size(), 1);
		Tile tile3 = tiles.get(9);
		assertTrue(gameBoard.placeTile(tile3, new Position(0, -1)));
		Tile tile4 = copier.copy(tile1);
		tile4.rotate();
		assertTrue(gameBoard.placeTile(tile4, new Position(1, -1)));
		Tile tile5 = tiles.get(11);
		assertTrue(gameBoard.placeTile(tile5, new Position(2, -1)));
		Tile tile6 = copier.copy(tiles.get(21));
		tile6.rotate();
		tile6.rotate();
		assertTrue(gameBoard.placeTile(tile6, new Position(2, 0)));
		Tile tile7 = copier.copy(tiles.get(4));
		tile7.rotate();
		assertTrue(gameBoard.placeTile(tile7, new Position(2, 1)));
		Tile tile8 = tiles.get(7);
		assertTrue(gameBoard.placeTile(tile8, new Position(1, 1)));
		Tile tile9 = tiles.get(10);
		assertTrue(gameBoard.placeTile(tile9, new Position(0, 1)));
		
		assertEquals(gameBoard.getCloisterSegments().size(), 1);
		for(Cloister cloister : gameBoard.getCloisterSegments()) {
			assertTrue(cloister.checkCompletion());
			assertEquals(cloister.calculateScore(), 9);
		}
		assertEquals(gameBoard.getCitySegments().size(), 4);
		assertEquals(gameBoard.getRoadSegments().size(), 3);
		assertEquals(gameBoard.getFarmSegments().size(), 3);
	}
	
	/**
	 * Test function to test the game system.
	 * because it's hard to test the game with randomly pick tiles
	 * I add one pick tile function in game system to help to test the left functions in GameSystem
	 */
	@Test
	public void testGameSystem() {
		int playerNum = 3;
		GameSystem  carcassonne = new GameSystem(playerNum);
		GameBoard gameBoard = carcassonne.getGameBoard();
		Player player1 = carcassonne.getPlayers().get(0);
		Player player2 = carcassonne.getPlayers().get(1);
		
		Tile tile1 = carcassonne.pickTile('D');
		tile1.rotate();
		tile1.rotate();
		tile1.rotate();
		assertFalse(carcassonne.placeTile(tile1, new Position(0, -1)));
		assertTrue(carcassonne.placeTile(tile1, new Position(0, 0)));
		assertEquals(gameBoard.getFarmSegments().size(), 2);
		assertEquals(gameBoard.getRoadSegments().size(), 1);
		assertEquals(gameBoard.getCitySegments().size(), 1);
		CityFeature cityFeature = new CityFeature(false);
		cityFeature.setPos(new Position(0, 0));
		cityFeature.addDirection(2);
		assertTrue(carcassonne.placeFollower(cityFeature, 0));
		assertEquals(player1.getFollowers().size(), 1);
		assertEquals(player1.leftFollowers(), 7);
		
		Tile tile2 = carcassonne.pickTile('H');
		tile2.rotate();
		assertTrue(carcassonne.placeTile(tile2, new Position(0, 1)));
		assertEquals(gameBoard.getFarmSegments().size(), 3);
		assertEquals(gameBoard.getRoadSegments().size(), 1);
		assertEquals(gameBoard.getCitySegments().size(), 2);
		cityFeature = new CityFeature(false);
		cityFeature.setPos(new Position(0, 1));
		cityFeature.addDirection(0);
		assertFalse(carcassonne.placeFollower(cityFeature, 0));
		LinkedList<Integer> farmFeatures = new LinkedList<Integer>();
		farmFeatures.add(1);
		farmFeatures.add(2);
		farmFeatures.add(3);
		farmFeatures.add(4);
		LinkedList<String> farmNeighbors = new LinkedList<String>();
		farmNeighbors.add("left");
		farmNeighbors.add("right");
		FarmFeature farmFeature = new FarmFeature(farmFeatures, farmNeighbors);
		farmFeature.setPos(new Position(0, 0));
		assertFalse(carcassonne.placeFollower(farmFeature, 0));
		farmFeature.setPos(new Position(0, 1));
		assertTrue(carcassonne.placeFollower(farmFeature, 0));
		assertEquals(player1.leftFollowers(), 7);
		//one city segment completed, one follower was returned back to player1
		assertEquals(player1.getFollowers().size(), 1);
		
		Tile tile3 = carcassonne.pickTile('V');
		tile3.rotate();
		tile3.rotate();
		tile3.rotate();
		assertTrue(carcassonne.placeTile(tile3, new Position(1, 0)));
		assertEquals(gameBoard.getFarmSegments().size(), 3);
		assertEquals(gameBoard.getRoadSegments().size(), 1);
		assertEquals(gameBoard.getCitySegments().size(), 2);
		farmFeatures = new LinkedList<Integer>();
		farmFeatures.add(2);
		farmFeatures.add(3);
		farmFeatures.add(4);
		farmNeighbors = new LinkedList<String>();
		farmNeighbors.add("left");
		farmNeighbors.add("down");
		farmNeighbors.add("right");
		farmNeighbors.add("up");
		farmFeature = new FarmFeature(farmFeatures, farmNeighbors);
		farmFeature.setPos(new Position(1, 0));
		assertTrue(carcassonne.placeFollower(farmFeature, 1));
		assertEquals(player1.leftFollowers(), 7);
		assertEquals(player1.getFollowers().size(), 1);
		assertEquals(player2.leftFollowers(), 7);
		assertEquals(player2.getFollowers().size(), 1);
		
		Tile tile4 = carcassonne.pickTile('V');
		tile4.rotate();
		assertTrue(carcassonne.placeTile(tile4, new Position(1, 1)));
		assertEquals(gameBoard.getFarmSegments().size(), 3);
		assertEquals(gameBoard.getRoadSegments().size(), 2);
		assertEquals(gameBoard.getCitySegments().size(), 2);
		
		farmFeatures = new LinkedList<Integer>();
		farmFeatures.add(1);
		farmFeatures.add(2);
		farmFeatures.add(4);
		farmNeighbors = new LinkedList<String>();
		farmNeighbors.add("left");
		farmNeighbors.add("down");
		farmNeighbors.add("right");
		farmNeighbors.add("up");
		farmFeature = new FarmFeature(farmFeatures, farmNeighbors);
		farmFeature.setPos(new Position(1, 1));
		assertFalse(carcassonne.placeFollower(farmFeature, 0));
		farmFeatures = new LinkedList<Integer>();
		farmFeatures.add(3);
		farmNeighbors = new LinkedList<String>();
		farmNeighbors.add("down");
		farmNeighbors.add("right");
		farmFeature = new FarmFeature(farmFeatures, farmNeighbors);
		farmFeature.setPos(new Position(1, 1));
		assertTrue(carcassonne.placeFollower(farmFeature, 0));
		assertEquals(player1.leftFollowers(), 6);
		assertEquals(player1.getFollowers().size(), 2);
		carcassonne.gameOver();
	}
	
	@Test
	public void newTestGameSystem() {
		int playerNum = 3;
		GameSystem  carcassonne = new GameSystem(playerNum);
		GameBoard gameBoard = carcassonne.getGameBoard();
		Player player1 = carcassonne.getPlayers().get(0);
		Player player2 = carcassonne.getPlayers().get(1);
		
		Tile tile1 = carcassonne.pickTile('D');
		tile1.rotate();
		tile1.rotate();
		tile1.rotate();
		assertFalse(carcassonne.placeTile(tile1, new Position(0, -1)));
		assertTrue(carcassonne.placeTile(tile1, new Position(0, 0)));
		assertEquals(gameBoard.getFarmSegments().size(), 2);
		assertEquals(gameBoard.getRoadSegments().size(), 1);
		assertEquals(gameBoard.getCitySegments().size(), 1);
		assertTrue(carcassonne.placeFollower(tile1, 2, 0));
		assertEquals(player1.getFollowers().size(), 1);
		assertEquals(player1.leftFollowers(), 7);
		
		Tile tile2 = carcassonne.pickTile('H');
		tile2.rotate();
		assertTrue(carcassonne.placeTile(tile2, new Position(0, 1)));
		assertEquals(gameBoard.getFarmSegments().size(), 3);
		assertEquals(gameBoard.getRoadSegments().size(), 1);
		assertEquals(gameBoard.getCitySegments().size(), 2);
		assertFalse(carcassonne.placeFollower(tile2, 0, 0));
		assertTrue(carcassonne.placeFollower(tile2, 5, 0));
		carcassonne.updateScore();
		assertEquals(player1.leftFollowers(), 7);
		//one city segment completed, one follower was returned back to player1
		assertEquals(player1.getFollowers().size(), 1);
		
		Tile tile3 = carcassonne.pickTile('V');
		tile3.rotate();
		tile3.rotate();
		tile3.rotate();
		assertTrue(carcassonne.placeTile(tile3, new Position(1, 0)));
		assertEquals(gameBoard.getFarmSegments().size(), 3);
		assertEquals(gameBoard.getRoadSegments().size(), 1);
		assertEquals(gameBoard.getCitySegments().size(), 2);
		assertTrue(carcassonne.placeFollower(tile3, 8, 1));
		assertEquals(player1.leftFollowers(), 7);
		assertEquals(player1.getFollowers().size(), 1);
		assertEquals(player2.leftFollowers(), 7);
		assertEquals(player2.getFollowers().size(), 1);
		
		Tile tile4 = carcassonne.pickTile('V');
		tile4.rotate();
		assertTrue(carcassonne.placeTile(tile4, new Position(1, 1)));
		assertEquals(gameBoard.getFarmSegments().size(), 3);
		assertEquals(gameBoard.getRoadSegments().size(), 2);
		assertEquals(gameBoard.getCitySegments().size(), 2);
		assertTrue(carcassonne.placeFollower(tile4, 7, 0));
		assertEquals(player1.leftFollowers(), 6);
		assertEquals(player1.getFollowers().size(), 2);
		carcassonne.gameOver();
	}
	
	@Test
	public void newCityTest() {
		int playerNum = 3;
		GameSystem  carcassonne = new GameSystem(playerNum);
		Player player1 = carcassonne.getPlayers().get(0);
		
		Tile tile1 = carcassonne.pickTile('O');
		tile1.rotate();
		tile1.rotate();
		tile1.rotate();
		assertTrue(carcassonne.placeTile(tile1, new Position(0, 0)));
		Tile tile2 = carcassonne.pickTile('C');
		assertTrue(carcassonne.placeTile(tile2, new Position(1, 0)));
		
		Tile tile3 = carcassonne.pickTile('S');
		assertTrue(carcassonne.placeTile(tile3, new Position(2, 0)));
		
		Tile tile4 = carcassonne.pickTile('M');
		assertTrue(carcassonne.placeTile(tile4, new Position(3, 0)));

		Tile tile5 = carcassonne.pickTile('M');
		tile5.rotate();
		assertTrue(carcassonne.placeTile(tile5, new Position(3, -1)));
		
		
		Tile tile6 = carcassonne.pickTile('I');
		assertTrue(carcassonne.placeTile(tile6, new Position(2, -1)));
		
		Tile tile7 = carcassonne.pickTile('E');
		tile7.rotate();
		tile7.rotate();
		assertTrue(carcassonne.placeTile(tile7, new Position(1, -1)));
		assertTrue(carcassonne.placeFollower(tile7, 2, 0));
		
		Tile tile8 = carcassonne.pickTile('E');
		tile8.rotate();
		tile8.rotate();
		assertTrue(carcassonne.placeTile(tile8, new Position(0, -1)));
		
		Tile tile9 = carcassonne.pickTile('K');
		tile9.rotate();
		assertTrue(carcassonne.placeTile(tile9, new Position(1, 1)));
		carcassonne.updateScore();
		assertEquals(player1.getScore(), 28);
	}
	
	@Test
	public void newCloisterTest() {
		int playerNum = 3;
		GameSystem  carcassonne = new GameSystem(playerNum);
		Player player1 = carcassonne.getPlayers().get(0);
		Player player2 = carcassonne.getPlayers().get(1);
		
		Tile tile1 = carcassonne.pickTile('A');
		tile1.rotate();
		tile1.rotate();
		assertTrue(carcassonne.placeTile(tile1, new Position(0, 0)));
		Tile tile2 = carcassonne.pickTile('S');
		assertTrue(carcassonne.placeTile(tile2, new Position(0, -1)));
		
		Tile tile3 = carcassonne.pickTile('M');
		tile3.rotate();
		tile3.rotate();
		tile3.rotate();
		assertTrue(carcassonne.placeTile(tile3, new Position(-1, -1)));
		
		Tile tile4 = carcassonne.pickTile('B');
		assertTrue(carcassonne.placeTile(tile4, new Position(-2, -1)));

		Tile tile5 = carcassonne.pickTile('B');
		assertTrue(carcassonne.placeTile(tile5, new Position(-1, 0)));
		assertTrue(carcassonne.placeFollower(tile5, 4, 1));
		
		Tile tile6 = carcassonne.pickTile('B');
		assertTrue(carcassonne.placeTile(tile6, new Position(-2, 0)));
		assertTrue(carcassonne.placeFollower(tile6, 4, 0));
		
		Tile tile7 = carcassonne.pickTile('F');
		assertTrue(carcassonne.placeTile(tile7, new Position(0, 1)));
		
		Tile tile8 = carcassonne.pickTile('F');
		assertTrue(carcassonne.placeTile(tile8, new Position(-1, 1)));
		
		Tile tile9 = carcassonne.pickTile('E');
		tile9.rotate();
		tile9.rotate();
		tile9.rotate();
		assertTrue(carcassonne.placeTile(tile9, new Position(-2, 1)));
		carcassonne.updateScore();
		assertEquals(player1.getScore(), 0);
		assertEquals(player2.getScore(), 9);
	}
	
	@Test
	public void newFarmTest() {
		int playerNum = 3;
		GameSystem  carcassonne = new GameSystem(playerNum);
		GameBoard gameBoard = carcassonne.getGameBoard();
		
		Tile tile1 = carcassonne.pickTile('X');
		assertTrue(carcassonne.placeTile(tile1, new Position(0, 0)));
		assertEquals(gameBoard.getFarmSegments().size(), 4);
		Tile tile2 = carcassonne.pickTile('K');
		tile2.rotate();
		tile2.rotate();
		tile2.rotate();
		assertTrue(carcassonne.placeTile(tile2, new Position(-1, 0)));
		assertEquals(gameBoard.getFarmSegments().size(), 4);
		
		Tile tile3 = carcassonne.pickTile('V');
		assertTrue(carcassonne.placeTile(tile3, new Position(0, -1)));
		assertEquals(gameBoard.getFarmSegments().size(), 4);
		
		Tile tile4 = carcassonne.pickTile('V');
		tile4.rotate();
		assertTrue(carcassonne.placeTile(tile4, new Position(-1, -1)));
		assertEquals(gameBoard.getFarmSegments().size(), 3);
		
		
		Tile tile5 = carcassonne.pickTile('K');
		tile5.rotate();
		assertTrue(carcassonne.placeTile(tile5, new Position(-1, 1)));
		assertEquals(gameBoard.getFarmSegments().size(), 5);
		
		Tile tile6 = carcassonne.pickTile('K');
		tile6.rotate();
		tile6.rotate();
		tile6.rotate();
		assertTrue(carcassonne.placeTile(tile6, new Position(0, 1)));
		assertEquals(gameBoard.getFarmSegments().size(), 4);
	}
	
	@Test
	public void newRoadTest() {
		int playerNum = 3;
		GameSystem  carcassonne = new GameSystem(playerNum);
		GameBoard gameBoard = carcassonne.getGameBoard();
		
		Tile tile1 = carcassonne.pickTile('W');
		tile1.rotate();
		assertTrue(carcassonne.placeTile(tile1, new Position(0, 0)));

		Tile tile2 = carcassonne.pickTile('J');
		tile2.rotate();
		assertTrue(carcassonne.placeTile(tile2, new Position(0, 1)));
		assertEquals(gameBoard.getRoadSegments().size(), 3);
		
		Tile tile3 = carcassonne.pickTile('K');
		assertTrue(carcassonne.placeTile(tile3, new Position(1, 1)));
		
		Tile tile4 = carcassonne.pickTile('J');
		tile4.rotate();
		tile4.rotate();
		tile4.rotate();
		assertTrue(carcassonne.placeTile(tile4, new Position(1, 0)));
		assertEquals(gameBoard.getRoadSegments().size(), 2);
	}
}
