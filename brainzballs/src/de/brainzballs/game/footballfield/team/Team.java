package de.brainzballs.game.footballfield.team;

import java.util.ArrayList;
import java.util.List;

import de.brainzballs.game.footballfield.Field;
import de.brainzballs.game.footballfield.Tile;
import de.brainzballs.game.overlay.Fight;

public class Team {
	
	private List<Player> players;
	private Field field;
	private int points;
	
	public Team(Field field) {
		this.field = field;
		this.players = new ArrayList<Player>();
	}
	
	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
		getField().updatePoints();
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
		
	public Field getField() {
		return field;
	}
	
	public void decrementOffended() {
		for (Player p : players)
			p.decrementOffended();
	}
	
	public boolean hasBall() {
		for (Player p : players)
			if (p.hasBall())
				return true;
		
		return false;
	}
	
	public void fight() {
		for (Player p : players) {
			if (!p.isOffended()) {
				List<Tile> nextTiles = p.getTile().getNeighbours();
				for (Tile nextTile : nextTiles) {
					Player enemy = getField().getOpponentPlayerOnPosition(nextTile.getPositionX(), nextTile.getPositionY());
					if (enemy != null)
						getField().getGame().addActor(new Fight(p, enemy));
				}
			}
		}
	}
}
