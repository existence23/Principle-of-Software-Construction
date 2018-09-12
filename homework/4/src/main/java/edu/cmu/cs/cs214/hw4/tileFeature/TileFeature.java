package edu.cmu.cs.cs214.hw4.tileFeature;

import edu.cmu.cs.cs214.hw4.core.Position;

/**
 * Interface of tile features.
 */
public interface TileFeature {

	/**
	 * Function to set the position of this feature.
	 * @param pos is the position to be set
	 */
	public void setPos(Position pos);
	/**
	 * Function to get the position of this feature.
	 * @return the position of this feature
	 */
	public Position getPos();
}
