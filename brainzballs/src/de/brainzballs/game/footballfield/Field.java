package de.brainzballs.game.footballfield;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;

import de.brainzballs.game.footballfield.team.Team;

public class Field extends Group {
	private Tile[][] field;
	private Ball ball;
	private Team team1, team2;
	
	public Field(int width,int height) {
		field = new Tile[width][height];
		
		for(int w = 0; w < width; w++) {
			for(int h = 0; h < height; h++) {
				field[w][h] = new Tile(w, h);
				addActor(field[w][h]);
			}
		}
		
		team1 = new Team();
		team2 = new Team();
		
		ball = new Ball();
	}

	public boolean isFree(int x, int y) {
		//TODO isFree(int int)
		return false;
	}
	
	private Tile getTile(int x, int y) {
		//TODO getTile(int int)
		return null;
	}
	
	private void resetHighlight() {
		for(int w = 0; w < field.length; w++) {
			for(int h = 0; h < field[0].length; h++) {
				field[w][h].setHighlighted(false);
			}
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
}
