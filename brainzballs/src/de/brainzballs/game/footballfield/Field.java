package de.brainzballs.game.footballfield;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;

import de.brainzballs.game.footballfield.team.Player;
import de.brainzballs.game.footballfield.team.Team;

public class Field extends Group {

	public static final int FIELD_WIDTH_MIN = 11;
	public static final int FIELD_WIDTH_MAX = 21;
	public static final int FIELD_HEIGHT_MIN = 7;
	public static final int FIELD_HEIGHT_MAX = 15;

	private Tile[][] field;
	private Ball ball;
	private Team team1, team2;

	private Field(int width, int height) {
		field = new Tile[width][height];

		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				field[w][h] = new Tile(w, h);
				addActor(field[w][h]);
			}
		}

		// Create team1 on the left half
		List<Player> players;
		players = new ArrayList<Player>();
		players.add(new Player()); // Keeper
		players.add(new Player()); 
		players.add(new Player());
		players.add(new Player());
		players.add(new Player());
		team1 = new Team(players);

		// create team2 on the right half
		players = new ArrayList<Player>();
		players.add(new Player());
		players.add(new Player());
		players.add(new Player());
		players.add(new Player());
		players.add(new Player());
		team1 = new Team(players);

		ball = new Ball();
	}

	public Field newInstance(int width, int height) {
		if (width < FIELD_WIDTH_MIN || width > FIELD_WIDTH_MAX) {
			System.out.println("width out of range");
			return null;
		}
		if (height < FIELD_HEIGHT_MIN || height > FIELD_HEIGHT_MAX) {
			System.out.println("height out of range");
			return null;
		}
		if (width % 2 == 0 || height % 2 == 0) {
			System.out.println("height and width have to be odd");
			return null;
		}
		return new Field(width, height);
	}

	public boolean isFree(int x, int y) {
		// TODO isFree(int int)
		return false;
	}

	public Tile getTile(int x, int y) {
		// TODO getTile(int int)
		return null;
	}

	public void resetHighlight() {
		for (int w = 0; w < field.length; w++) {
			for (int h = 0; h < field[0].length; h++) {
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
