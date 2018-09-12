package edu.cmu.cs.cs214.hw4.core;

import java.util.LinkedList;

public class GameSystemTile {
	private String name;
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	private LinkedList<Tile> inventory;
	public LinkedList<Tile> getInventory() { return inventory; }
	public void setInventory(LinkedList<Tile> inventory) { this.inventory = inventory; }
}
