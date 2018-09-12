package edu.cmu.cs.cs214.hw4.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

public class script {
	private static GameSystemTile parse(String fileName) {
		Yaml yaml = new Yaml();
		try(InputStream is = new FileInputStream(fileName)){
			return yaml.loadAs(is, GameSystemTile.class);
		}catch(FileNotFoundException e) {
			throw new IllegalArgumentException("File " + fileName + " not found!");
		}catch (IOException e) {
		    throw new IllegalArgumentException("Error when reading " + fileName + "!");
		}
	}
	
	public static void main(String[] args) {
		  GameSystemTile store = parse("src/main/resources/Tile.yml");
		  System.out.println(store.getName());
		  List<Tile> inv = store.getInventory();
		  inv.get(9).rotate();
		  System.out.println(inv.get(9).getEdgeFeatures());
		  System.out.println(inv.get(9).getFarmFeatures());
		  System.out.println(inv.get(9).getFarmNeighbor());
		  
		  
	}
	
	

}
