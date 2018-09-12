package edu.cmu.cs.cs214.hw4.core;

import java.util.LinkedList;

/**
 * Class to represent the real tile in the Carcassonne game.
 *
 */
public class Tile {
	private InventoryItemTile item;
	public InventoryItemTile getItem() { return item; }
	public void setItem(InventoryItemTile item) { this.item = item; }
	
	public String getName() { return item.toString(); }
	
	private int quantity;
	/**
	 * Function to get the quantity of the same tile.
	 * @return the same tiles number
	 */
	public int getQuantity() { return quantity; }
	public void setQuantity(int quantity ) { this.quantity = quantity; }
	
	private LinkedList<String> edgeFeatures;
	/**
	 * Function to get the edge features of this tile.
	 * edge feature could be farm, road or city
	 * @return the edgge features of this tile
	 */
	public LinkedList<String> getEdgeFeatures() { return edgeFeatures; }
	public void setEdgeFeatures(LinkedList<String> edgeFeatures) { this.edgeFeatures = edgeFeatures; }

	private LinkedList<LinkedList<Integer>> farmFeatures;
	/**
	 * Function to get the farm features of this tile.
	 * the size of the farm features represents the number of the farm features this tile has
	 * for each LinkedList<Integer> in this list, it represents the vertex of the farm feature
	 * @return the farm feature of this tile
	 */
	public LinkedList<LinkedList<Integer>> getFarmFeatures() { return farmFeatures; }
	public void setFarmFeatures(LinkedList<LinkedList<Integer>> farmFeatures) { this.farmFeatures = farmFeatures; }

	private String middleFeature;
	/**
	 * Function to get the middle feature of this tile
	 * the middle feature is to help to identify is there cloister exists in this tile
	 * or help to identify if the two same edge features is connected or not
	 * @return
	 */
	public String getMiddleFeature() { return middleFeature; }
	public void setMiddleFeature(String middleFeature) {this.middleFeature = middleFeature; }

	private boolean pennant;
	/**
	 * Function to get is there any pennant exists in this tile.
	 * @return true if exists, false if not
	 */
	public boolean existPennant() { return pennant; }
	public void setPennant(boolean pennant) { this.pennant = pennant; }

	private LinkedList<LinkedList<String>> farmNeighbor;
	/**
	 * Function to get the list of neighbor directions for each farm feature.
	 * @return the neighbor directions for each farm feature
	 */
	public LinkedList<LinkedList<String>> getFarmNeighbor() { return farmNeighbor; }
	public void setFarmNeighbor(LinkedList<LinkedList<String>> farmNeighbor) { this.farmNeighbor = farmNeighbor; }
	
	private LinkedList<LinkedList<Integer>> adjCity;
	/**
	 * Function to get the list of city directions for each farm feature.
	 * @return the list of city directions for rach farm feature
	 */
	public LinkedList<LinkedList<Integer>> getAdjCity() { return adjCity; }
	public void setAdjCity(LinkedList<LinkedList<Integer>> adjCity) { this.adjCity = adjCity; }

	/**
	 * Function to rotate the tile in counterclockwise direction.
	 */
	public void rotate() {
		LinkedList<String> newEdge = edgeFeatures;
		String end = newEdge.remove(newEdge.size() - 1);
		newEdge.add(0, end);
		setEdgeFeatures(newEdge);
		
		for(int i = 0; i < farmFeatures.size(); i++) {
			for(int j = 0; j < farmFeatures.get(i).size(); j++) {
				int newEdgeFarm = (farmFeatures.get(i).get(j) % 4) + 1;
				farmFeatures.get(i).set(j, newEdgeFarm);
			}
		}
		
		for(int i = 0; i < farmNeighbor.size(); i++) {
			for(int j = 0; j < farmNeighbor.get(i).size(); j++) {
				if(farmNeighbor.get(i).get(j).equals("up")) { farmNeighbor.get(i).set(j, "left"); }
				else if(farmNeighbor.get(i).get(j).equals("left")) { farmNeighbor.get(i).set(j, "down"); }
				else if(farmNeighbor.get(i).get(j).equals("down")) { farmNeighbor.get(i).set(j, "right"); }
				else { farmNeighbor.get(i).set(j, "up"); }
			}
		}
		
		for(int i = 0; i < adjCity.size(); i++) {
			for(int j = 0; j < adjCity.get(i).size(); j++) {
				int newAdj = (adjCity.get(i).get(j) + 1) % 4;
				adjCity.get(i).set(j, newAdj);
			}
		}
	}
	
	private Position pos;
	/**
	 * Function to set the position of this tile on the gameBoard.
	 * @param pos is the position to be set
	 */
	public void setPosition(Position pos) { this.pos = pos; }
	/**
	 * Function to get the position of this tile.
	 * @return
	 */
	public Position getPosition() { return pos; }
}
