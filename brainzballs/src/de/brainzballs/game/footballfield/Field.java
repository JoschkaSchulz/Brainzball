package de.brainzballs.game.footballfield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;

import de.brainzballs.game.Game;
import de.brainzballs.game.footballfield.team.Player;
import de.brainzballs.game.footballfield.team.Team;

public class Field extends Group {

	private class TileNode {
		
		private final boolean start;
		private final boolean end;
		
	 	private Tile previewTile;
		private int cost;
		
		public TileNode(Tile previewTile, boolean start, boolean end, int cost) {
			this.previewTile = previewTile;
			this.start = start;
			this.end = end;
			this.cost = cost;
		}
		
		public void setPreviewTile(Tile tile, int cost) {
			this.previewTile = tile;
			this.cost = cost;
		}
		
		public Tile getPreviewTile() {
			return previewTile;
		}
		
		public int getCost() {
			return cost;
		}
		
		public boolean isStart() {
			return start;
		}
		
		public boolean isEnd() {
			return end;
		}
		
		/*public static List<Tile> getPath() {
			List<Tile> result = new ArrayList<Tile>();
			return result;
		}*/
	}
	
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

		int gWidth = width * 64;
		int gHeight = height * 64;
		int fieldX = Gdx.graphics.getWidth() / 2 - gWidth / 2;
		int fieldY = Gdx.graphics.getHeight() / 2 - gHeight / 2;
		setBounds(fieldX, fieldY, gWidth, gHeight);

		// Create field
		field = new Tile[width][height];
		for (int w = 0; w < width; w++)
			for (int h = 0; h < height; h++) {
				field[w][h] = Tile.newInstance(w, h);
				addActor(field[w][h]);
			}

		// Set initial field action
		currentFieldAction = FieldAction.NONE;

		// Create ball
		int horizontalCenter = (int)(width / 2) + 1;
		int verticalCenter = (int)(height / 2) + 1;
		ball = Ball.newInstance(horizontalCenter, verticalCenter);

		// Create team1 on the left half
		List<Player> players;
		players = new ArrayList<Player>();
		players.add(Player.newInstance(0, verticalCenter,
				Player.PlayerType.KEEPER, Player.EAST));
		players.add(Player.newInstance(1, verticalCenter - 2,
				Player.PlayerType.DEFENDER, Player.EAST));
		players.add(Player.newInstance(1, verticalCenter + 2,
				Player.PlayerType.DEFENDER, Player.EAST));
		players.add(Player.newInstance(1, verticalCenter,
				Player.PlayerType.MIDFIELDER, Player.EAST));
		players.add(Player.newInstance(2, verticalCenter - 1,
				Player.PlayerType.STRIKER, Player.EAST));
		players.add(Player.newInstance(2, verticalCenter + 1,
				Player.PlayerType.STRIKER, Player.EAST));
		team1 = new Team(players);
		addActor(team1);

		// create team2 on the right half
		players = new ArrayList<Player>();
		players.add(Player.newInstance(width - 1, verticalCenter,
				Player.PlayerType.KEEPER, Player.WEST));
		players.add(Player.newInstance(width - 2, verticalCenter - 2,
				Player.PlayerType.DEFENDER, Player.WEST));
		players.add(Player.newInstance(width - 2, verticalCenter + 2,
				Player.PlayerType.DEFENDER, Player.WEST));
		players.add(Player.newInstance(width - 2, verticalCenter,
				Player.PlayerType.MIDFIELDER, Player.WEST));
		players.add(Player.newInstance(width - 3, verticalCenter - 1,
				Player.PlayerType.STRIKER, Player.WEST));
		players.add(Player.newInstance(width - 3, verticalCenter + 1,
				Player.PlayerType.STRIKER, Player.WEST));
		team2 = new Team(players);
		addActor(team2);
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

	public void setPlayer(Player player) {
		currentPlayer = player;
		updateTiles();
	}

	public void setFieldAction(FieldAction fieldAction) {
		currentFieldAction = fieldAction;
		updateTiles();
	}

	private void updateTiles() {
		resetHighlight();
		if (currentPlayer == null)
			return;

		if (currentFieldAction == FieldAction.PASS && currentPlayer.canPass()) {
			highlightPass(currentPlayer, 4);
		} else if (currentFieldAction == FieldAction.MOVE
				&& currentPlayer.canMove()) {
			highlightMove(currentPlayer, 4);
		} else if (currentFieldAction == FieldAction.SHOT
				&& currentPlayer.canShot()) {
			highlightShot(currentPlayer, 4);
		}
	}

	public boolean isFree(int x, int y) {
		return !isInTeam(x, y, getTeam1()) && !isInTeam(x, y, getTeam2());
	}
	
	public boolean isOpponent(int x, int y, Team currentTeam) {
		boolean result = false;
		if (team1 != currentTeam) {
			result = result || isInTeam(x, y, team1);
		} else {
			result = result || isInTeam(x, y, team2);
		}
		return false;
	}
	
	public boolean isInTeam(int x, int y, Team team) {
		for (Player p : team.getPlayers())
			if (p.getPositionX() == x && p.getPositionY() == y)
				return true;
		
		return false;
	}
	
	public void highlightPass(Player player, int radius) {
		// TODO
	}
	public void highlightShot(Player player, int radius) {
		// TODO
	}
	public Map<Tile, TileNode> highlightMove(Player player, int radius) {
		
		// Save all reachable tiles with their cost
		Map<Tile, TileNode> result = new HashMap<Tile, TileNode>();
		
		// Start at the player tile
		Tile startTile = player.getTile();
		TileNode startTileNode = new TileNode(null, true, startTile.hasOpponentNeighbour(player.getTeam()), 0);
		result.put(startTile, startTileNode);
		
		// Add start tile to visit list
		List<Tile> toBeVisited = startTile.getNeighbours();
		toBeVisited.add(startTile);
		
		// Iterate over tiles to visit
		while (!toBeVisited.isEmpty()) {
			
	        // Get first tile and remove it from list 
	        Tile currentTile = toBeVisited.get(0);
	        toBeVisited.remove(0);
	        
	        // Get information about current tile
	        TileNode currentTileNode = result.get(currentTile);
	        if (!currentTileNode.isEnd()) {	        
	        	
	            // Get all neighbours from current tile
	            List<Tile> nextTiles = currentTile.getNeighbours();
	            for (Tile nextTile : nextTiles) {
	            	
	            	// Check if the next file is free
	            	if (nextTile.isFree()) {
	            		
	            		// Calculate cost and insert or update the current information 
	            		boolean visitNextTile = false;
	            		int cost = currentTileNode.cost + currentTile.getCondition();
	           			TileNode nextTileNode = result.get(nextTile);
	           			if (nextTileNode == null) {
	           				nextTileNode = new TileNode(currentTile, false, nextTile.hasOpponentNeighbour(player.getTeam()), cost);
	           				result.put(nextTile, nextTileNode);
	           				visitNextTile = true;
	           			} else {
	           				if (nextTileNode.getCost() > cost) {
	           					nextTileNode.setPreviewTile(currentTile, cost); 
	           					visitNextTile = true;
	           				}
	           			}
	           			
	           			// If next tile is new or better insert tile to visit list
	           			for (int i = 0; i < toBeVisited.size(); i++) {
	           				
	           			}
	           		}
	           	}
			}
		}
		
		return result;
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

	public Game getGame() {
		return (Game)getParent();
	}
	
	private int mouseX, mouseY, overX, overY;
	private Tile overTile;

	@Override
	public void act(float delta) {
		
		resetMouseOverTile();
		
		mouseX = Gdx.input.getX();
		mouseY = Gdx.input.getY();

		if (mouseX > getX() && mouseX < (getX() + getWidth())
				&& mouseY > getY() && mouseY < (getY() + getHeight())) {
			mouseX -= getX();
			mouseY -= getY();

			overX = (int) (mouseX / 64);
			overY = (int) (mouseY / 64);


			overX = (int)(mouseX/64);
			overY = (int)(((getHeight())-mouseY)/64);
			
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
