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

		// Create field
		field = new Tile[width][height];
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				field[w][h] = Tile.newInstance(w, h);
				addActor(field[w][h]);
			}
		}

		// Create ball
		int horizontalCenter = (int) height / 2;
		int verticalCenter = (int) width / 2;
		ball = Ball.newInstance(horizontalCenter, verticalCenter);

		// Create team1 on the left half
		List<Player> players;
		players = new ArrayList<Player>();
		players.add(Player.newInstance(0, verticalCenter,
				Player.PlayerType.KEEPER));
		players.add(Player.newInstance(1, verticalCenter - 2,
				Player.PlayerType.DEFENDER));
		players.add(Player.newInstance(1, verticalCenter + 2,
				Player.PlayerType.DEFENDER));
		players.add(Player.newInstance(1, verticalCenter,
				Player.PlayerType.MIDFIELDER));
		players.add(Player.newInstance(2, verticalCenter - 1,
				Player.PlayerType.STRIKER));
		players.add(Player.newInstance(2, verticalCenter + 1,
				Player.PlayerType.STRIKER));
		team1 = new Team(players);

		// create team2 on the right half
		players = new ArrayList<Player>();
		players.add(Player.newInstance(width - 1, verticalCenter,
				Player.PlayerType.KEEPER));
		players.add(Player.newInstance(width - 2, verticalCenter - 2,
				Player.PlayerType.DEFENDER));
		players.add(Player.newInstance(width - 2, verticalCenter + 2,
				Player.PlayerType.DEFENDER));
		players.add(Player.newInstance(width - 2, verticalCenter,
				Player.PlayerType.MIDFIELDER));
		players.add(Player.newInstance(width - 3, verticalCenter - 1,
				Player.PlayerType.STRIKER));
		players.add(Player.newInstance(width - 3, verticalCenter + 1,
				Player.PlayerType.STRIKER));
		team1 = new Team(players);
	}

	public static Field newInstance(int width, int height) {
		width = (width < FIELD_WIDTH_MIN ? FIELD_WIDTH_MIN : width);
		width = (width > FIELD_WIDTH_MAX ? FIELD_WIDTH_MAX : width);
		width += (width % 2 == 0 ? 1 : 0);
		height = (height < FIELD_HEIGHT_MIN ? FIELD_HEIGHT_MIN : height);
		height = (height > FIELD_HEIGHT_MAX ? FIELD_HEIGHT_MAX : height);
		height += (height % 2 == 0 ? 1 : 0);
		return new Field(width, height);
	}

	public boolean isFree(int x, int y) {
		return isFree(x, y, getTeam1().getPlayers()) || isFree(x, y, getTeam2().getPlayers());
	}
	
	public boolean isFree(int x, int y, List<Player> players) {
		for (Player p : players)
			if (p.getPositionX() == x && p.getPositionY() == y)
				return true;
		return false;
	}

	public Ball getBall() {
		return ball;
	}

	public Team getTeam1() {
		return team1;
	}

	public Team getTeam2() {
		return team2;
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
