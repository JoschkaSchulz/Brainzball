package de.brainzballs.game.footballfield;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import de.brainzballs.game.Game;
import de.brainzballs.game.footballfield.team.Player;
import de.brainzballs.game.footballfield.team.Team;
import de.brainzballs.game.overlay.Fight;
import de.brainzballs.helper.ResourceLoader;

public class Field extends Group {

	public enum FieldAction {
		PASS, MOVE, SHOT
	}

	public static final int FIELD_WIDTH_MIN = 11;
	public static final int FIELD_WIDTH_MAX = 21;
	public static final int FIELD_HEIGHT_MIN = 7;
	public static final int FIELD_HEIGHT_MAX = 15;

	// Data object
	private Tile[][] field;
	private Ball ball;
	private Team team1, team2;
	
	// Current selection for action
	private Player currentPlayer;
	private FieldAction currentFieldAction;
	private Map<Tile, TileNode> currentTiles;
	
	// Decoration
	private Image overlay;

	
	private Field(int width, int height) {

		// Set bounds for the field
		int gWidth = width * 64;
		int gHeight = height * 64;
		int fieldX = Gdx.graphics.getWidth() / 2 - gWidth / 2;
		int fieldY = Gdx.graphics.getHeight() / 2 - gHeight / 2;
		setBounds(fieldX, fieldY, gWidth, gHeight);

		// Create field with tiles
		field = new Tile[width][height];
		for (int w = 0; w < width; w++)
			for (int h = 0; h < height; h++) {
				field[w][h] = Tile.newInstance(w, h);
				addActor(field[w][h]);
			}

		// Add field lines as decoration
		overlay = new Image(ResourceLoader.FIELD_OVERLAY);
		addActor(overlay);
		
		// Create ball
		int horizontalCenter = (width / 2);
		int verticalCenter = (height / 2);
		ball = Ball.newInstance(horizontalCenter, verticalCenter);
		addActor(ball);
		
		// Create team1 with players on the left
		team1 = new Team(this);
		List<Player> players = new ArrayList<Player>();
		players.add(Player.newInstance(0, verticalCenter,
				Player.PlayerType.KEEPER, Player.EAST, team1));
		players.add(Player.newInstance(2, verticalCenter - 3,
				Player.PlayerType.DEFENDER, Player.EAST, team1));
		players.get(players.size()-1).setName("down");
		players.add(Player.newInstance(2, verticalCenter + 3,
				Player.PlayerType.DEFENDER, Player.EAST, team1));
		players.get(players.size()-1).setName("up");
		players.add(Player.newInstance(4, verticalCenter,
				Player.PlayerType.MIDFIELDER, Player.EAST, team1));
		players.get(players.size()-1).setName("mid");
		players.add(Player.newInstance(6, verticalCenter - 1,
				Player.PlayerType.STRIKER, Player.EAST, team1));
		players.add(Player.newInstance(6, verticalCenter + 1,
				Player.PlayerType.STRIKER, Player.EAST, team1));
		for (Player p : players)
			addActor(p);
		team1.setPlayers(players);

		// create team2 with players on the right
		team2 = new Team(this);
		players = new ArrayList<Player>();
		players.add(Player.newInstance(width - 1, verticalCenter,
				Player.PlayerType.KEEPER, Player.WEST, team2));
		players.add(Player.newInstance(width - 3, verticalCenter - 3,
				Player.PlayerType.DEFENDER, Player.WEST, team2));
		players.add(Player.newInstance(width - 3, verticalCenter + 3,
				Player.PlayerType.DEFENDER, Player.WEST, team2));
		players.add(Player.newInstance(width - 5, verticalCenter,
				Player.PlayerType.MIDFIELDER, Player.WEST, team2));
		players.add(Player.newInstance(width - 7, verticalCenter - 1,
				Player.PlayerType.STRIKER, Player.WEST, team2));
		players.add(Player.newInstance(width - 7, verticalCenter + 1,
				Player.PlayerType.STRIKER, Player.WEST, team2));
		for (Player p : players)
			addActor(p);
		team2.setPlayers(players);
		
		// Order players for correct z buffer position
		orderPlayers();
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

	private void orderPlayers() {
		List<Player> players = new ArrayList<Player>();
		
		// Get all players
		for(Actor a : getChildren())
			if(a instanceof Player)
				players.add((Player)a);
		
		// Reorder players on z axis
		for(int i = 0; i < players.size(); i++)
			for(int o = 0; o < players.size(); o++)
				if(players.get(i).getPositionY() > players.get(o).getPositionY()) {
					swapActor(players.get(i), players.get(o));
					Collections.swap(players, i, o);
				}
	}
	
	public void setCurrentPlayer(int x, int y) {
		currentPlayer = null;
		if (getGame().getCurrentTeam() == Game.TEAM_1) {
			setCurrentPlayer(x, y, team1);
		} else if (getGame().getCurrentTeam() == Game.TEAM_2) {
			setCurrentPlayer(x, y, team2);
		}
		updateCurrentTiles();
	}
	
	private void setCurrentPlayer(int x, int y, Team team) {
		for (Player p : team.getPlayers())
			if (p.getPositionX() == x && p.getPositionY() == y && !p.isOffended())
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
		
		// Save all reachable tiles with their cost
		currentTiles = new HashMap<Tile, TileNode>();
		
		// If player not null calculate the reachable tiles
		if (currentPlayer != null && getGame().getCurrentState() == Game.STATE_ACTION_CHOOSE) {
			
			// Start at the player tile
			Tile startTile = currentPlayer.getTile();
			TileNode startTileNode = new TileNode(null, true, startTile.hasOpponentNeighbour(), startTile.isFree(), 0);
			currentTiles.put(startTile, startTileNode);
			
			// Add start tile to visit list
			List<Tile> openList = new ArrayList<Tile>();
			openList.add(startTile);
			
			// Search for all reachable tiles
			if (currentFieldAction == FieldAction.PASS && currentPlayer.hasBall()) {
				currentTiles = getCurrentTilesForPass(currentPlayer, currentTiles, openList);
			} else if (currentFieldAction == FieldAction.MOVE) {
				currentTiles = getCurrentTilesForMove(currentPlayer, currentTiles, openList);
			} else if (currentFieldAction == FieldAction.SHOT) {
				currentTiles = getCurrentTilesForShot(currentPlayer, currentTiles, openList);
			}
		}
		
		// Highlight all tiles with cost
		resetHighlight();
		for (Tile tile : currentTiles.keySet()) {
			TileNode tileNode = currentTiles.get(tile);
			if (!tileNode.isStart()) {
				tile.setHighlighted(true);
				tile.getDebugLabel().setText(String.valueOf(tileNode.cost));
			}
		}
	}
	
	private Map<Tile, TileNode> getCurrentTilesForPass(Player player, Map<Tile, TileNode> closedMap, List<Tile> openList) {

		// Iterate over tiles to visit
		while (!openList.isEmpty()) {
			
	        // Get first tile and remove it from list 
	        Tile currentTile = openList.get(0);
	        openList.remove(0);
	        
	        // Get information about current tile
	        TileNode currentTileNode = closedMap.get(currentTile);
	        if (!currentTileNode.hasOpponentNeighbour()) {	        
	        	
	            // Get all neighbours from current tile
	            List<Tile> nextTiles = currentTile.getNeighbours();
	            for (Tile nextTile : nextTiles) {
	            	
            		// Calculate cost and insert or update the current information
            		int cost = currentTileNode.cost + currentTile.getCondition();
            		if (currentTileNode.hasOpponentNeighbour())
            			cost += 2;
            		
            		// If cost to damn high dont pass
            		if (cost <= player.getPassRadius()) {
            			boolean visitNextTile = false;
	           			TileNode nextTileNode = closedMap.get(nextTile);
	           			if (nextTileNode == null) {
	           				nextTileNode = new TileNode(currentTile, false, nextTile.hasOpponentNeighbour(), nextTile.isFree(), cost);
	           				closedMap.put(nextTile, nextTileNode);
	           				visitNextTile = true;
	           			} else {
	           				if (!nextTileNode.hasOpponentNeighbour && nextTileNode.getCost() > cost) {
	           					nextTileNode.setPreviewTile(currentTile, cost); 
	           					visitNextTile = true;
	           				}
	           			}
	           			
	           			// If next tile is new or better than the tile before visit it
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
		
		// Remove all occupied tiles
		Iterator<Map.Entry<Tile, TileNode>> iterator = closedMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Tile, TileNode> entry = iterator.next();
			if (entry.getValue().isFree())
				iterator.remove();
		}
		
		return closedMap;
	}
	
	private Map<Tile, TileNode> getCurrentTilesForShot(Player player, Map<Tile, TileNode> closedMap, List<Tile> openList) {
		
		// Iterate over tiles to visit
		while (!openList.isEmpty()) {
			
	        // Get first tile and remove it from list 
	        Tile currentTile = openList.get(0);
	        openList.remove(0);
	        
	        // Get information about current tile
	        TileNode currentTileNode = closedMap.get(currentTile);
	        if (!currentTileNode.hasOpponentNeighbour()) {	        
	        	
	            // Get all neighbours from current tile
	            List<Tile> nextTiles = currentTile.getNeighbours();
	            for (Tile nextTile : nextTiles) {
	            	
            		// If cost to damn high dont shot
            		int cost = currentTileNode.cost + 1;
            		if (cost <= player.getMoveRadius()) {
            			boolean visitNextTile = false;
	           			TileNode nextTileNode = closedMap.get(nextTile);
	           			if (nextTileNode == null) {
	           				nextTileNode = new TileNode(currentTile, false, nextTile.hasOpponentNeighbour(), nextTile.isFree(), cost);
	           				closedMap.put(nextTile, nextTileNode);
	           				visitNextTile = true;
	           			} else {
	           				if (!nextTileNode.hasOpponentNeighbour && nextTileNode.getCost() > cost) {
	           					nextTileNode.setPreviewTile(currentTile, cost); 
	           					visitNextTile = true;
	           				}
	           			}
	           			
	           			// If next tile is new or better than the tile before visit it
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
		
		// Remove all occupied tiles
		Iterator<Map.Entry<Tile, TileNode>> iterator = closedMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Tile, TileNode> entry = iterator.next();
			if (!entry.getValue().isFree())
				iterator.remove();
		}
		
		return closedMap;
	}
	
	private Map<Tile, TileNode> getCurrentTilesForMove(Player player, Map<Tile, TileNode> closedMap, List<Tile> openList) {
		
		// Iterate over tiles to visit
		while (!openList.isEmpty()) {
			
	        // Get first tile and remove it from list 
	        Tile currentTile = openList.get(0);
	        openList.remove(0);
	        
	        // Get information about current tile
	        TileNode currentTileNode = closedMap.get(currentTile);
	        if (!currentTileNode.hasOpponentNeighbour()) {	        
	        	
	            // Get all neighbours from current tile
	            List<Tile> nextTiles = currentTile.getNeighbours();
	            for (Tile nextTile : nextTiles) {
	            	
	            	// Check if the next file is free
	            	if (nextTile.isFree()) {
	            		
	            		// If cost to damn high dont move
	            		int cost = currentTileNode.cost + currentTile.getCondition();
	            		if (cost <= player.getMoveRadius()) {
	            			boolean visitNextTile = false;
		           			TileNode nextTileNode = closedMap.get(nextTile);
		           			if (nextTileNode == null) {
		           				nextTileNode = new TileNode(currentTile, false, nextTile.hasOpponentNeighbour(), nextTile.isFree(), cost);
		           				closedMap.put(nextTile, nextTileNode);
		           				visitNextTile = true;
		           			} else {
		           				if (!nextTileNode.hasOpponentNeighbour && nextTileNode.getCost() > cost) {
		           					nextTileNode.setPreviewTile(currentTile, cost); 
		           					visitNextTile = true;
		           				}
		           			}
		           			
		           			// If next tile is new or better than the tile before visit it
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
	
	public void beginFieldAction(Tile tile) {
		if (getGame().getCurrentState() == Game.STATE_ACTION_CHOOSE) {
			List<Tile> path = getPathToTile(tile);
			if (path.size() > 0) {			
				if (currentFieldAction == FieldAction.PASS) {
					
				} else if (currentFieldAction == FieldAction.MOVE) {
					currentPlayer.addMovePoints(path);
					getGame().setCurrentState(Game.STATE_ACTION_BEGIN);
				} else if (currentFieldAction == FieldAction.SHOT) {
					
				}
			}
			updateCurrentTiles();
		}
	}
	
	public void endFieldAction() {
		
		// At the end of an action the current team heal
		if (getGame().getCurrentTeam() == Game.TEAM_1) {
			team1.decrementOffended();
		} else if (getGame().getCurrentTeam() == Game.TEAM_2) {
			team2.decrementOffended();
		}
		
		// Do some cool fights with the current team
		if (getGame().getCurrentTeam() == Game.TEAM_1) {
			team1.fight();
		} else if (getGame().getCurrentTeam() == Game.TEAM_2) {
			team2.fight();
		}
		
		getGame().setCurrentState(Game.STATE_ACTION_END);
	}
	
	private List<Tile> getPathToTile(Tile tile) {
		List<Tile> result = new ArrayList<Tile>();
		if (isTileReachable(tile)) {
			TileNode tileNode = currentTiles.get(tile);
			while (!tileNode.isStart()) {
				result.add(tile);
				tile = tileNode.previewTile;
				tileNode = currentTiles.get(tile);
			}
		}
		Collections.reverse(result);
		return result;
	}
	
	public boolean isFree(int x, int y) {
		return !isTeamOnPosition(x, y, team1) && !isTeamOnPosition(x, y, team2);
	}
	
	public boolean isHealOpponentOnPosition(int x, int y) {
		boolean result = false;
		Player enemy = getOpponentPlayerOnPosition(x, y);
		if (enemy != null)
			result = !enemy.isOffended();
		
		return result;
	}
	
	public boolean isOpponentOnPosition(int x, int y) {
		boolean result = false;
		if (getGame().getCurrentTeam() == Game.TEAM_1) {
			result = isTeamOnPosition(x, y, team2);
		} else if (getGame().getCurrentTeam() == Game.TEAM_2) {
			result = isTeamOnPosition(x, y, team1);
		}
		return result;
	}
	
	public Player getOpponentPlayerOnPosition(int x, int y) {
		Player result = null;
		if (getGame().getCurrentTeam() == Game.TEAM_1) {
			result = getOpponentPlayerOnPosition(x, y, team2);
		} else if (getGame().getCurrentTeam() == Game.TEAM_2) {
			result = getOpponentPlayerOnPosition(x, y, team1);
		}
		return result;
	}
	
	private Player getOpponentPlayerOnPosition(int x, int y, Team team) {
		for (Player p : team.getPlayers())
			if (p.getPositionX() == x && p.getPositionY() == y)
				return p;
		
		return null;
	}
	
	public boolean isFriendOnPosition(int x, int y) {
		boolean result = false;
		if (getGame().getCurrentTeam() == Game.TEAM_1) {
			result = isTeamOnPosition(x, y, team1);
		} else if (getGame().getCurrentTeam() == Game.TEAM_2) {
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
		boolean result = false;
		if (currentTiles.containsKey(tile)) {
			TileNode tileNode = currentTiles.get(tile);
			result = !tileNode.isStart();
		}
		return result;
	}
	
	public Ball getBall() {
		return ball;
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
		for (int w = 0; w < field.length; w++)
			for (int h = 0; h < field[0].length; h++)
				field[w][h].setMouseOver(false);
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
			overX = (mouseX / 64);
			overY = (int)(((getHeight()) - mouseY) / 64);
			overTile = getTile(overX, overY);
			overTile.setMouseOver(true);
		}
		super.act(delta);
	}
}
