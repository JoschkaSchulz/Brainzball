package de.brainzballs.game.footballfield;

import com.badlogic.gdx.scenes.scene2d.Actor;

import de.brainzballs.game.footballfield.team.Player;

public class Tile extends Actor {
	private int CONDITION_GOOD 		= 1;
	private int CONDITION_NORMAL	= 2;
	private int CONDITION_BAD		= 3;
	
	private int x, y;
	private Field field;
	private int state;
	private boolean border;
	private boolean goal;
	
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
		this.field = (Field) getParent();
	}
	
	public boolean isFree() {
		return field.isFree(x, y);
	}
	
	public boolean isBorder() {
		return border;
	}
	public boolean isGoal() {
		return goal;
	}
	
	
}
