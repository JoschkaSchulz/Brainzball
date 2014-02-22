package de.brainzballs.game.footballfield;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Ball extends Actor {
	
	private int x, y;
	
	private Ball(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public static Ball newInstance(int x, int y) {
		return new Ball(x, y);
	}
	
	public Field getField() {
		return (Field)getParent();
	}
	
	public Tile getTile() {
		return getField().getTile(x, y);
	}
}
