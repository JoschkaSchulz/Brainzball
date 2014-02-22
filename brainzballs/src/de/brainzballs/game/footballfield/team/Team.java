package de.brainzballs.game.footballfield.team;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Group;

import de.brainzballs.game.footballfield.Field;

public class Team extends Group {
	
	private List<Player> players;
	
	public Team(List<Player> players) {
		this.players = players;
	}
	
	public int getPasses() {
		int result = 0;
		for (Player player : players)
			result += player.getPasses();
		return result;
	}

	public int getMoves() {
		int result = 0;
		for (Player player : players)
			result += player.getMoves();
		return result;
	}

	public int getShoots() {
		int result = 0;
		for (Player player : players)
			result += player.getShoots();
		return result;
	}

	public int getGoals() {
		int result = 0;
		for (Player player : players)
			result += player.getGoals();
		return result;
	}

	public int getFouls() {
		int result = 0;
		for (Player player : players)
			result += player.getFouls();
		return result;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public Field getField() {
		return (Field)getParent();
	}
}
