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

		// Create ball
		int horizontalCenter = (int)height / 2;
		int verticalCenter = (int)width / 2;
		ball = Ball.newInstance(horizontalCenter, verticalCenter);
		
		// Create team1 on the left half
		List<Player> players;
		players = new ArrayList<Player>();
		players.add(Player.newInstance(0, verticalCenter, Player.PlayerType.KEEPER));
		players.add(Player.newInstance(1, verticalCenter - 2, Player.PlayerType.DEFENDER));
		players.add(Player.newInstance(1, verticalCenter + 2, Player.PlayerType.DEFENDER));
		players.add(Player.newInstance(1, verticalCenter, Player.PlayerType.MIDFIELDER));
		players.add(Player.newInstance(2, verticalCenter - 1, Player.PlayerType.STRIKER));
		players.add(Player.newInstance(2, verticalCenter + 1, Player.PlayerType.STRIKER));
		team1 = new Team(players);

		// create team2 on the right half
		players = new ArrayList<Player>();
		players.add(Player.newInstance(width - 1, verticalCenter, Player.PlayerType.KEEPER));
		players.add(Player.newInstance(width - 2, verticalCenter - 2, Player.PlayerType.DEFENDER));
		players.add(Player.newInstance(width - 2, verticalCenter + 2, Player.PlayerType.DEFENDER));
		players.add(Player.newInstance(width - 2, verticalCenter, Player.PlayerType.MIDFIELDER));
		players.add(Player.newInstance(width - 3, verticalCenter - 1, Player.PlayerType.STRIKER));
		players.add(Player.newInstance(width - 3, verticalCenter + 1, Player.PlayerType.STRIKER));
		team1 = new Team(players);
	}

	public static Field newInstance(int width, int height) {
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
