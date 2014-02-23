package de.brainzballs.game.footballfield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	}
	
	public enum FieldAction {
		PASS, MOVE, SHOT
	}

	public static final int FIELD_WIDTH_MIN = 11;
	public static final int FIELD_WIDTH_MAX = 21;
	public static final int FIELD_HEIGHT_MIN = 7;
	public static final int FIELD_HEIGHT_MAX = 15;

	private Tile[][] field;
	private Ball ball;
	private Team team1, team2;

	//private int currentState;
	private Player currentPlayer;
	private FieldAction currentFieldAction;
	private Map<Tile, TileNode> currentTiles;

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

		// Create ball
		int horizontalCenter = (int)(width / 2);
		int verticalCenter = (int)(height / 2);
		ball = Ball.newInstance(horizontalCenter, verticalCenter);

		// Create team1 on the left half
		team1 = new Team(this);
		List<Player> players = new ArrayList<Player>();
		players.add(Player.newInstance(0, verticalCenter,
				Player.PlayerType.KEEPER, Player.EAST, team1));
		players.add(Player.newInstance(1, verticalCenter - 2,
				Player.PlayerType.DEFENDER, Player.EAST, team1));
		players.add(Player.newInstance(1, verticalCenter + 2,
				Player.PlayerType.DEFENDER, Player.EAST, team1));
		players.add(Player.newInstance(1, verticalCenter,
				Player.PlayerType.MIDFIELDER, Player.EAST, team1));
		players.add(Player.newInstance(2, verticalCenter - 1,
				Player.PlayerType.STRIKER, Player.EAST, team1));
		players.add(Player.newInstance(2, verticalCenter + 1,
				Player.PlayerType.STRIKER, Player.EAST, team1));
		for (Player p : players)
			addActor(p);
		team1.setPlayers(players);

		// create team2 on the right half
		team2 = new Team(this);
		players = new ArrayList<Player>();
		players.add(Player.newInstance(width - 1, verticalCenter,
				Player.PlayerType.KEEPER, Player.WEST, team2));
		players.add(Player.newInstance(width - 2, verticalCenter - 2,
				Player.PlayerType.DEFENDER, Player.WEST, team2));
		players.add(Player.newInstance(width - 2, verticalCenter + 2,
				Player.PlayerType.DEFENDER, Player.WEST, team2));
		players.add(Player.newInstance(width - 2, verticalCenter,
				Player.PlayerType.MIDFIELDER, Player.WEST, team2));
		players.add(Player.newInstance(width - 3, verticalCenter - 1,
				Player.PlayerType.STRIKER, Player.WEST, team2));
		players.add(Player.newInstance(width - 3, verticalCenter + 1,
				Player.PlayerType.STRIKER, Player.WEST, team2));
		for (Player p : players)
			addActor(p);
		team2.setPlayers(players);
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
	
	public void setCurrentPlayer(int x, int y) {
		currentPlayer = null;
		if (getGame().getState() == Game.STATE_TEAM1) {
			setCurrentPlayer(x, y, team1);
		} else if (getGame().getState() == Game.STATE_TEAM2) {
			setCurrentPlayer(x, y, team2);
		}
		updateCurrentTiles();
	}
	
	private void setCurrentPlayer(int x, int y, Team team) {
		for (Player p : team.getPlayers())
			if (p.getPositionX() == x && p.getPositionY() == y)
				currentPlayer = p;
	}
	
	public void setCurrentFieldAction(FieldAction fieldAction) {
		currentFieldAction = fieldAction;
		updateCurrentTiles();
	}
	
	public FieldAction getCurrentFieldAction() {
		return currentFieldAction;
	}

	private void updateCurrentTiles() {
		resetHighlight();
		
		currentTiles = new HashMap<Tile, Field.TileNode>();
		if (currentPlayer != null) {
			if (currentFieldAction == FieldAction.PASS) {
				currentTiles = getCurrentTilesForPass(currentPlayer);
			} else if (currentFieldAction == FieldAction.MOVE) {
				currentTiles = getCurrentTilesForMove(currentPlayer);
			} else if (currentFieldAction == FieldAction.SHOT) {
				currentTiles = getCurrentTilesForShot(currentPlayer);
			}
		}
		
		// Highlight all tiles with cost
		for (Tile tile : currentTiles.keySet()) {
			TileNode tileNode = currentTiles.get(tile);
			tile.setHighlighted(true);
			tile.getDebugLabel().setText(String.valueOf(tileNode.cost));
		}
	}
	
	public Map<Tile, TileNode> getCurrentTilesForPass(Player player) {
		
		// Save all reachable tiles with their cost
		Map<Tile, TileNode> closedMap = new HashMap<Tile, TileNode>();
		
		// Start at the player tile
		Tile startTile = player.getTile();
		TileNode startTileNode = new TileNode(null, true, startTile.hasOpponentNeighbour(player.getTeam()), 0);
		closedMap.put(startTile, startTileNode);
		
		// Add start tile to visit list
		List<Tile> openList = new ArrayList<Tile>();
		openList.add(startTile);
		
		// Iterate over tiles to visit
		while (!openList.isEmpty()) {
			
	        // Get first tile and remove it from list 
	        Tile currentTile = openList.get(0);
	        openList.remove(0);
	        
	        // Get information about current tile
	        TileNode currentTileNode = closedMap.get(currentTile);
	        if (!currentTileNode.isEnd()) {	        
	        	
	            // Get all neighbours from current tile
	            List<Tile> nextTiles = currentTile.getNeighbours();
	            for (Tile nextTile : nextTiles) {
	            	
	            	// Check if the next file is free
	            	if (nextTile.isFree()) {
	            		
	            		// Calculate cost and insert or update the
	            		// current information
	            		int cost = currentTileNode.cost + currentTile.getCondition();
	            		if (cost <= player.getMoveRadius()) {
	            			boolean visitNextTile = false;
		           			TileNode nextTileNode = closedMap.get(nextTile);
		           			if (nextTileNode == null) {
		           				nextTileNode = new TileNode(currentTile, false, nextTile.hasOpponentNeighbour(player.getTeam()), cost);
		           				closedMap.put(nextTile, nextTileNode);
		           				visitNextTile = true;
		           			} else {
		           				if (!nextTileNode.end && nextTileNode.getCost() > cost) {
		           					nextTileNode.setPreviewTile(currentTile, cost); 
		           					visitNextTile = true;
		           				}
		           			}
		           			
		           			// If next tile is new or better than the one before
		           			// insert tile into visit list
		           			if (visitNextTile) {
		           				int i = 0;
		           				while (i < openList.size()) {
		           					TileNode tileNode = closedMap.get(openList.get(i));
		           					if (tileNode.getCost() > cost) {
		           						break;
		           					} else {
		           						i++;
		           					}
		           				}
		           				openList.add(i, nextTile);
		           			}
	            		}
	           		}
	           	}
			}
		}
		
		return closedMap;
	}
	
	public Map<Tile, TileNode> getCurrentTilesForShot(Player player) {

		// Save all reachable tiles with their cost
		Map<Tile, TileNode> closedMap = new HashMap<Tile, TileNode>();
		
		// Start at the player tile
		Tile startTile = player.getTile();
		TileNode startTileNode = new TileNode(null, true, startTile.hasOpponentNeighbour(player.getTeam()), 0);
		closedMap.put(startTile, startTileNode);
		
		// Add start tile to visit list
		List<Tile> openList = new ArrayList<Tile>();
		openList.add(startTile);
		
		// Iterate over tiles to visit
		while (!openList.isEmpty()) {
			
	        // Get first tile and remove it from list 
	        Tile currentTile = openList.get(0);
	        openList.remove(0);
	        
	        // Get information about current tile
	        TileNode currentTileNode = closedMap.get(currentTile);
	        if (!currentTileNode.isEnd()) {	        
	        	
	            // Get all neighbours from current tile
	            List<Tile> nextTiles = currentTile.getNeighbours();
	            for (Tile nextTile : nextTiles) {
	            	
	            	// Check if the next file is free
	            	if (nextTile.isFree()) {
	            		
	            		// Calculate cost and insert or update the
	            		// current information
	            		int cost = currentTileNode.cost + currentTile.getCondition();
	            		if (cost <= player.getMoveRadius()) {
	            			boolean visitNextTile = false;
		           			TileNode nextTileNode = closedMap.get(nextTile);
		           			if (nextTileNode == null) {
		           				nextTileNode = new TileNode(currentTile, false, nextTile.hasOpponentNeighbour(player.getTeam()), cost);
		           				closedMap.put(nextTile, nextTileNode);
		           				visitNextTile = true;
		           			} else {
		           				if (!nextTileNode.end && nextTileNode.getCost() > cost) {
		           					nextTileNode.setPreviewTile(currentTile, cost); 
		           					visitNextTile = true;
		           				}
		           			}
		           			
		           			// If next tile is new or better than the one before
		           			// insert tile into visit list
		           			if (visitNextTile) {
		           				int i = 0;
		           				while (i < openList.size()) {
		           					TileNode tileNode = closedMap.get(openList.get(i));
		           					if (tileNode.getCost() > cost) {
		           						break;
		           					} else {
		           						i++;
		           					}
		           				}
		           				openList.add(i, nextTile);
		           			}
	            		}
	           		}
	           	}
			}
		}
		
		return closedMap;
	}
	
	public Map<Tile, TileNode> getCurrentTilesForMove(Player player) {
		
		// Save all reachable tiles with their cost
		Map<Tile, TileNode> closedMap = new HashMap<Tile, TileNode>();
		
		// Start at the player tile
		Tile startTile = player.getTile();
		TileNode startTileNode = new TileNode(null, true, startTile.hasOpponentNeighbour(player.getTeam()), 0);
		closedMap.put(startTile, startTileNode);
		
		// Add start tile to visit list
		List<Tile> openList = new ArrayList<Tile>();
		openList.add(startTile);
		
		// Iterate over tiles to visit
		while (!openList.isEmpty()) {
			
	        // Get first tile and remove it from list 
	        Tile currentTile = openList.get(0);
	        openList.remove(0);
	        
	        // Get information about current tile
	        TileNode currentTileNode = closedMap.get(currentTile);
	        if (!currentTileNode.isEnd()) {	        
	        	
	            // Get all neighbours from current tile
	            List<Tile> nextTiles = currentTile.getNeighbours();
	            for (Tile nextTile : nextTiles) {
	            	
	            	// Check if the next file is free
	            	if (nextTile.isFree()) {
	            		
	            		// Calculate cost and insert or update the
	            		// current information
	            		int cost = currentTileNode.cost + currentTile.getCondition();
	            		if (cost <= player.getMoveRadius()) {
	            			boolean visitNextTile = false;
		           			TileNode nextTileNode = closedMap.get(nextTile);
		           			if (nextTileNode == null) {
		           				nextTileNode = new TileNode(currentTile, false, nextTile.hasOpponentNeighbour(player.getTeam()), cost);
		           				closedMap.put(nextTile, nextTileNode);
		           				visitNextTile = true;
		           			} else {
		           				if (!nextTileNode.end && nextTileNode.getCost() > cost) {
		           					nextTileNode.setPreviewTile(currentTile, cost); 
		           					visitNextTile = true;
		           				}
		           			}
		           			
		           			// If next tile is new or better than the one before
		           			// insert tile into visit list
		           			if (visitNextTile) {
		           				int i = 0;
		           				while (i < openList.size()) {
		           					TileNode tileNode = closedMap.get(openList.get(i));
		           					if (tileNode.getCost() > cost) {
		           						break;
		           					} else {
		           						i++;
		           					}
		           				}
		           				openList.add(i, nextTile);
		           			}
	            		}
	           		}
	           	}
			}
		}
		
		return closedMap;
	}
	
	public List<Tile> getPathForTile(Tile destination) {
		//if (currentTiles.size() == 0)
		List<Tile> result = new ArrayList<Tile>();
		
		return result;
	}
	
	public boolean isFree(int x, int y) {
		return !isTeamOnPosition(x, y, getTeam1()) && !isTeamOnPosition(x, y, getTeam2());
	}
	
	public boolean isOpponentOnPosition(int x, int y) {
		boolean result = false;
		if (getGame().getState() == Game.STATE_TEAM1) {
			result = isTeamOnPosition(x, y, team2);
		} else if (getGame().getState() == Game.STATE_TEAM2) {
			result = isTeamOnPosition(x, y, team1);
		}
		return result;
	}
	
	public boolean isFriendOnPosition(int x, int y) {
		boolean result = false;
		if (getGame().getState() == Game.STATE_TEAM1) {
			result = isTeamOnPosition(x, y, team1);
		} else if (getGame().getState() == Game.STATE_TEAM2) {
			result = isTeamOnPosition(x, y, team2);
		}
		return result;
	}
	
	public boolean isBallOnPosition(int x, int y) {
		return (ball.getPositionX() == x && ball.getPositionY() == y);
	}
	
	public boolean isTeamOnPosition(int x, int y, Team team) {
		for (Player p : team.getPlayers())
			if (p.getPositionX() == x && p.getPositionY() == y)
				return true;
		
		return false;
	}
	
	public boolean isCurrentPlayerSelected() {
		return currentPlayer != null;
	}
	
	public boolean isTileReachable(Tile tile) {
		return currentTiles.containsKey(tile);
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
