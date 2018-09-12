package edu.cmu.cs.cs214.hw4.core;

/**
 * Class to represent the position on the game board.
 */
public class Position {
	private int x;
	private int y;
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public int getX() { return x; }
	public int getY() { return y; }
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Position) {
			Position pos = (Position)obj;
			return x == pos.x && y == pos.y;
		}
		return false;
	}
	@Override
	public int hashCode() {
		return 31 * Integer.hashCode(x) + Integer.hashCode(y);
	}
	@Override
	public String toString() {
		return "x = " + Integer.toString(x) + ", y = " + Integer.toString(y);
	}
}
