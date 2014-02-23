package de.brainzballs.game.footballfield.team;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Group;

import de.brainzballs.game.footballfield.Field;

public class Team {
	
	private List<Player> players;
	private Field field;
	
	public Team(Field field) {
		this.field = field;
		this.players = new ArrayList<Player>();
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
}
