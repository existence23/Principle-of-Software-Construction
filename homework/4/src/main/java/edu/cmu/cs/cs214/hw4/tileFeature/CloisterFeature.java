package edu.cmu.cs.cs214.hw4.tileFeature;

import edu.cmu.cs.cs214.hw4.core.Position;

/**
 * Class to represent a single cloister feature in the tile.
 */
public class CloisterFeature implements TileFeature {

	private Position pos;
	@Override
	public void setPos(Position pos) { this.pos = new Position(pos.getX(), pos.getY()); }
	@Override
	public Position getPos() {
		return pos;
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof CloisterFeature)) return false;
		CloisterFeature newFeature = (CloisterFeature) obj;
		if(!pos.equals(newFeature.getPos())) return false;
		return true;
	}
	@Override
	public int hashCode() {
		return pos.hashCode();
	}

}
