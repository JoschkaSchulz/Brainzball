package de.brainzballs.game.footballfield;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;

public class Field extends Group {
	private Tiles[][] field;

	public Field(int width,int height) {
		field = new Tiles[width][height];
		
		for(int w = 0; w < width; w++) {
			for(int h = 0; h < height; h++) {
				field[w][h] = new Tiles();
				addActor(field[w][h]);
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
