package de.brainzballs.game.footballfield.team;

import com.badlogic.gdx.scenes.scene2d.Actor;

import de.brainzballs.game.footballfield.Field;
import de.brainzballs.game.footballfield.Tile;

public class Player extends Actor {
	
	private int x, y;
	private int goals, fouls, passes, moves, shoots;
	
	public void pass(Tile tile) {
		passes++;
		// TODO
	}
	
	public void move(Tile tile) {
		moves++;
		// TODO
	}
	
	public void shoot(Tile tile) {
		shoots++;
		// TODO
	}
	
	public int getPasses() {
		return passes;
	}
	
	public int getMoves() {
		return moves;
	}
	
	public int getShoots() {
		return shoots;
	}
	
	public int getGoals() {
		return goals;
	}
	
	public int getFouls() {
		return fouls;
	}
	
	public boolean hasBall() {
		return false;
	}
	
	public Team getTeam() {
		return (Team)getParent();
	}
	
	public Field getField() {
		return (Field)getTeam().getParent();
	}
	
	public Tile getTile() {
		return getField().getTile(x, y);
	}
}
