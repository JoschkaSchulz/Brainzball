package de.brainzballs.game.footballfield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.brainzballs.game.footballfield.team.Player;
import de.brainzballs.game.footballfield.team.Team;
import de.brainzballs.helper.ResourceLoader;

public class Tile extends Actor {

	private final int CONDITION_GOOD = 1;
	private final int CONDITION_NORMAL = 2;
	private final int CONDITION_BAD = 3;

	private int x, y;
	private int condition;
	private boolean border;
	private boolean goal;
	private boolean highlighted;
	private boolean mouseOver;
	private int goodId, normalId, badId; // Texture Array ID's

	private Tile(int x, int y) {
		this.x = x;
		this.y = y;
		this.condition = CONDITION_GOOD;
		this.highlighted = false;
		this.mouseOver = false;
		goodId = (int) Math.round(Math.random()
				* (ResourceLoader.TILE_GOOD.length - 1));
		normalId = (int) Math.round(Math.random()
				* (ResourceLoader.TILE_NORMAL.length - 1));
		badId = (int) Math.round(Math.random()
				* (ResourceLoader.TILE_BAD.length - 1));
	}

	public static Tile newInstance(int x, int y) {
		return new Tile(x, y);
	}

	public boolean isFree() {
		return getField().isFree(x, y);
	}
	
	public boolean isInTeam(Team team) {
		return getField().isInTeam(x, y, team);
	}
	
	public boolean isBorder() {
		return border;
	}

	public boolean isGoal() {
		return goal;
	}
	
	public boolean isHighlighted() {
		return highlighted;
	}
	
	public Field getField() {
		return (Field) getParent();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	
	
	
	
	
	
	
	
	/*public Map<Tile, Integer> getNeighbours() {
		return getNeighbours(1);
	}*/
	
	/*public Map<Tile, Integer> getNeighbours(int depth) {
		return getNeighbours(new HashMap<Tile, Integer>(), depth);
	}*/
	
	/*public Map<Tile, Integer> getNeighbours(Map<Tile, Integer> tiles, int depth) {
		if (depth == 0)
			return tiles;
		
		tiles = getNeighbours(tiles, getField().getTile(x, y + 1), depth);
		tiles = getNeighbours(tiles, getField().getTile(x, y - 1), depth);
		tiles = getNeighbours(tiles, getField().getTile(x + 1, y), depth);
		tiles = getNeighbours(tiles, getField().getTile(x - 1, y), depth);
		
		return tiles;
	}*/
	
	public List<Tile> getNeighbours() {
		List<Tile> tiles = new ArrayList<Tile>();
		tiles = getNeighbours(tiles, getField().getTile(x, y + 1));
		tiles = getNeighbours(tiles, getField().getTile(x, y - 1));
		tiles = getNeighbours(tiles, getField().getTile(x + 1, y));
		tiles = getNeighbours(tiles, getField().getTile(x - 1, y));
		return tiles;
	}
	
	private List<Tile> getNeighbours(List<Tile> tiles, Tile tile) {
		if (tile != null) {
			tiles.add(tile);
		}
		return tiles;
	}
	
	public boolean hasOpponentNeighbour(Team team) {
		
		
		return false;
	}
	
	private boolean hasOpponentNeighbour(Tile tile) {
		boolean result = false;
		
		return result;
	}
	
	private void mouseClick(Tile tile) {
		//TODO: GO RAIMUND GO!!!
	}
	
	public int getCondition() {
		return condition;
	}
	
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		switch (condition) {
		default:
		case CONDITION_GOOD:
			batch.draw(ResourceLoader.TILE_GOOD[goodId], x * 64, y * 64);
			break;
		case CONDITION_NORMAL:
			batch.draw(ResourceLoader.TILE_NORMAL[normalId], x * 64, y * 64);
			break;
		case CONDITION_BAD:
			batch.draw(ResourceLoader.TILE_BAD[badId], x * 64, y * 64);
			break;
		}

		batch.draw(ResourceLoader.TILE_GRID, x * 64, y * 64);
		
		if (highlighted) batch.draw(ResourceLoader.HIGHLIGHT, x * 64, y * 64);
		
		if (mouseOver) {
			batch.draw(ResourceLoader.HIGHLIGHT, x * 64, y * 64);
			
			if(Gdx.input.isTouched()) {
				mouseClick(getField().getTile(x * 64, y * 64));
			}
		}
	}

}
