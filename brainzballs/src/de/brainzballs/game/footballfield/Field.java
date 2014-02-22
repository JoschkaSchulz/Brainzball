package de.brainzballs.game.footballfield;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;

import de.brainzballs.game.footballfield.team.Player;
import de.brainzballs.game.footballfield.team.Team;

public class Field extends Group {

	public enum FieldAction {
		PASS, MOVE, SHOT, NONE
	}

	public static final int FIELD_WIDTH_MIN = 11;
	public static final int FIELD_WIDTH_MAX = 21;
	public static final int FIELD_HEIGHT_MIN = 7;
	public static final int FIELD_HEIGHT_MAX = 15;

	private Tile[][] field;
	private Ball ball;
	private Team team1, team2;

	private Player currentPlayer;
	private FieldAction currentFieldAction;

	private Field(int width, int height) {
		
		int gWidth = width*64;
		int gHeight = height*64;
		int fieldX = Gdx.graphics.getWidth()/2 -gWidth/2;
		int fieldY = Gdx.graphics.getHeight()/2 -gHeight/2;
		setBounds(fieldX, fieldY, gWidth, gHeight);
		
		// Create field
		field = new Tile[width][height];
		for (int w = 0; w < width; w++)
			for (int h = 0; h < height; h++) {
				field[w][h] = Tile.newInstance(w, h);
				addActor(field[w][h]);
			}

		// Set initial field action
		fieldAction = FieldAction.NONE;

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

	public boolean setPlayerFieldAction(Player player, FieldAction fieldAction) {
		if (player.isJailed())
			return false;

		boolean result = false;
		if (fieldAction == FieldAction.PASS && player.canPass()) {
			result = true;
		} else if (fieldAction == FieldAction.MOVE && player.canMove()) {
			result = true;
		} else if (fieldAction == FieldAction.SHOT && player.canShot()) {
			result = true;
		}
		return result;
	}

	/*
	 * private boolean canPlayerPass(Player player) { // TODO return false; }
	 * 
	 * private boolean canPlayerMove(Player player) { // TODO return false; }
	 * 
	 * private boolean canPlayerShot(Player player) { // TODO return false; }
	 */

	public boolean isFree(int x, int y) {
		return isFree(x, y, getTeam1().getPlayers())
				|| isFree(x, y, getTeam2().getPlayers());
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
		if (x < 0 || x >= field.length)
			return null;
		if (y < 0 || y >= field[0].length)
			return null;

		return field[x][y];
	}

	public void resetHighlight() {
		for (int w = 0; w < field.length; w++)
			for (int h = 0; h < field[0].length; h++)
				field[w][h].setHighlighted(false);
	}

	public void resetMouseOverTile() {
		for (int w = 0; w < field.length; w++) {
			for (int h = 0; h < field[0].length; h++) {
				field[w][h].setMouseOver(false);
			}
		}
	}
	
	private int mouseX, mouseY, overX, overY;
	private Tile overTile;
	@Override
	public void act(float delta) {
		
		resetMouseOverTile();
		
		mouseX = Gdx.input.getX();
		mouseY = Gdx.input.getY();
		
		if(mouseX > getX() && mouseX < (getX()+getWidth()) &&
				mouseY > getY() && mouseY < (getY()+getHeight())) {
			mouseX -= getX();
			mouseY -= getY();
			overX = (int)(mouseX/64);
			overY = (int)(mouseY/64);
			
			overTile = getTile(overX, overY);
			overTile.setMouseOver(true);
		}
		
		
		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
}
