package de.brainzballs.game.footballfield;

public class TileNode {
	
	public boolean start;
	public boolean hasOpponentNeighbour;
	public boolean free;
	public Tile previewTile;
	public int cost;

	public TileNode(Tile previewTile, boolean start, boolean hasOpponentNeighbour, boolean free, int cost) {
		this.previewTile = previewTile;
		this.start = start;
		this.hasOpponentNeighbour = hasOpponentNeighbour;
		this.free = free;
		this.cost = cost;
	}
	
	public void setPreviewTile(Tile tile, int cost) {
		this.previewTile = tile;
		this.cost = cost;
	}
	
	public Tile getPreviewTile() {
		return previewTile;
	}
	
	public int getCost() {
		return cost;
	}
	
	public boolean isStart() {
		return start;
	}
	
	public boolean hasOpponentNeighbour() {
		return hasOpponentNeighbour;
	}
	
	public boolean isFree() {
		return free;
	}
}