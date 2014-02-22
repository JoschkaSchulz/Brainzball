package de.brainzballs.game.footballfield.team;

import javax.swing.text.StyledEditorKit.BoldAction;

import com.badlogic.gdx.scenes.scene2d.Actor;

import de.brainzballs.game.footballfield.Field;
import de.brainzballs.game.footballfield.Tile;

public class Player extends Actor {
	
	public enum PlayerType {
	    KEEPER, DEFENDER, MIDFIELDER, STRIKER
	}
	
	private int x, y;
	private PlayerType playerType;
	private int goals, fouls, passes, moves, shoots;
	private boolean jailed;
	
	private Player(int x, int y, PlayerType playerType) {
		this.x = x;
		this.y = y;
		this.playerType = playerType;
	}
	
	public static Player newInstance(int x, int y, PlayerType playerType) {
		return new Player(x, y, playerType);
	}
	
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
	
	public boolean canPass() {
		if (!hasBall())
			return false;
		
		// TODO
		return false;
	}
	
	public boolean canMove() {
		// TODO
		return false;
	}
	
	public boolean canShot() {
		if (!hasBall())
			return false;
		
		// TODO
		return false;
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
		// TODO
		return false;
	}
	
	public PlayerType getPlayerType() {
		return playerType;
	}
	
	public Team getTeam() {
		return (Team)getParent();
	}
	
	public Field getField() {
		return getTeam().getField();
	}
	
	public Tile getTile() {
		return getField().getTile(x, y);
	}
	
	public int getPositionX() {
		return x;
	}
	
	public int getPositionY() {
		return y;
	}
	
	public boolean isJailed() {
		return jailed;
	}
}
