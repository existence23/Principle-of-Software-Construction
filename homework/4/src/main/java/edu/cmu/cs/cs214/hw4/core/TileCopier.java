package edu.cmu.cs.cs214.hw4.core;

import java.util.LinkedList;

public class TileCopier {
	public Tile copy(Tile tile) {
		Tile tmp = new Tile();
		tmp.setItem(tile.getItem());
		tmp.setQuantity(1);
		LinkedList<String> newEdgeFeatures = new LinkedList<String>();
		for(int i = 0; i < tile.getEdgeFeatures().size(); i++) {
			newEdgeFeatures.add(new String(tile.getEdgeFeatures().get(i)));
		}
		tmp.setEdgeFeatures(new LinkedList<String>(newEdgeFeatures));
		
		tmp.setPosition(tile.getPosition());
		
		LinkedList<LinkedList<Integer>> newFarmFeatures = new LinkedList<LinkedList<Integer>>();
		for(int i = 0; i < tile.getFarmFeatures().size(); i++) {
			LinkedList<Integer> singleFeatures = new LinkedList<Integer>(tile.getFarmFeatures().get(i));
			newFarmFeatures.add(singleFeatures);
		}
		tmp.setFarmFeatures(new LinkedList(newFarmFeatures));
		
		tmp.setMiddleFeature(new String(tile.getMiddleFeature()));
		tmp.setPennant(new Boolean(tile.existPennant()));
		
		LinkedList<LinkedList<String>> newFarmNeighbor = new LinkedList<LinkedList<String>>();
		for(int i = 0; i < tile.getFarmNeighbor().size(); i++) {
			LinkedList<String> singleNeighbors = new LinkedList<String>(tile.getFarmNeighbor().get(i));
			newFarmNeighbor.add(singleNeighbors);
		}
		tmp.setFarmNeighbor(new LinkedList(newFarmNeighbor));
		
		LinkedList<LinkedList<Integer>> newAdjCity = new LinkedList<LinkedList<Integer>>();
		for(int i = 0; i < tile.getAdjCity().size(); i++) {
			LinkedList<Integer> singleAdj = new LinkedList<Integer>(tile.getAdjCity().get(i));
			newAdjCity.add(singleAdj);
		}
		tmp.setAdjCity(newAdjCity);
		
		return tmp;
		
	}
}
