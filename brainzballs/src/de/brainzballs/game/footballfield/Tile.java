package de.brainzballs.game.footballfield;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.brainzballs.game.footballfield.team.Player;
import de.brainzballs.helper.ResourceLoader;

public class Tile extends Actor {
	
	private final int CONDITION_GOOD 	= 1;
	private final int CONDITION_NORMAL	= 2;
	private final int CONDITION_BAD		= 3;
	
	private int x, y;
	private Field field;
	private int state;
	private boolean border;
	private boolean goal;
	private boolean highlighted;
	private int goodId, normalId, badId;	//Texture Array ID's
	
	private Tile(int x, int y) {
		this.x = x;
		this.y = y;
		this.field = (Field) getParent();
		goodId = (int) Math.round(Math.random()*(ResourceLoader.TILE_GOOD.length-1));
		normalId = (int) Math.round(Math.random()*(ResourceLoader.TILE_NORMAL.length-1));
		badId = (int) Math.round(Math.random()*(ResourceLoader.TILE_BAD.length-1));
	}
	
	public static Tile newInstance(int x, int y) {
		return new Tile(x, y);
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

	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		switch(state) {
			default:
			case CONDITION_GOOD:
				batch.draw(ResourceLoader.TILE_GOOD[goodId], x*64, y*64);
				break;
			case CONDITION_NORMAL:
				batch.draw(ResourceLoader.TILE_NORMAL[normalId], x*64, y*64);
				break;
			case CONDITION_BAD:
				batch.draw(ResourceLoader.TILE_BAD[badId], x*64, y*64);
				break;
		}
	}
	
}
