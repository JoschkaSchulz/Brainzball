package de.brainzballs.game.footballfield;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import de.brainzballs.game.Game;
import de.brainzballs.game.footballfield.team.Player;
import de.brainzballs.game.footballfield.team.Team;
import de.brainzballs.helper.ResourceLoader;

public class Field extends Group {

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
	private Image overlay;

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

		overlay = new Image(ResourceLoader.FIELD_OVERLAY);
		addActor(overlay);
		
		// Create ball
		int horizontalCenter = (int)(width / 2);
		int verticalCenter = (int)(height / 2);
		ball = Ball.newInstance(horizontalCenter, verticalCenter);
		addActor(ball);
		
		// Create team1 on the left half
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

		// create team2 on the right half
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
		orderPlayers();
		
		// Set initial field action
		currentPlayer = team1.getPlayers().get(0);
		currentFieldAction = FieldAction.MOVE;
		currentTiles = new HashMap<Tile, TileNode>();
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
		
		for(Actor a : getChildren()) {
			if(a instanceof Player) {
				players.add((Player)a);
			}
		}
		
		for(int i = 0; i < players.size(); i++) {
			for(int o = 0; o < players.size(); o++) {
				if(players.get(i).getPositionY() > players.get(o).getPositionY()) {
					swapActor(players.get(i), players.get(o));
					Collections.swap(players, i, o);
				}
			}
		}
	}
	
	/*public Player getPlayer(int x, int y) {
		Player result = getPlayer(x, y, team1);
		if (result == null)
			result = getPlayer(x, y, team2);
		
		return result;
	}
	
	public Player getPlayer(int x, int y, Team team) {
		for (Player p : team.getPlayers())
			if (p.getPositionX() == x && p.getPositionY() == y)
				return p;
			
		return null;
	}*/
	
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
		
		// Save all reachable tiles with their cost
		currentTiles = new HashMap<Tile, TileNode>();
		
		// If player not null calculate the reachable tiles
		if (currentPlayer != null) {
			
			// Start at the player tile
			Tile startTile = currentPlayer.getTile();
			TileNode startTileNode = new TileNode(null, true, startTile.hasOpponentNeighbour(currentPlayer.getTeam()), startTile.isFree(), 0);
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
	           				nextTileNode = new TileNode(currentTile, false, nextTile.hasOpponentNeighbour(player.getTeam()), nextTile.isFree(), cost);
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
	           				nextTileNode = new TileNode(currentTile, false, nextTile.hasOpponentNeighbour(player.getTeam()), nextTile.isFree(), cost);
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
		           				nextTileNode = new TileNode(currentTile, false, nextTile.hasOpponentNeighbour(player.getTeam()), nextTile.isFree(), cost);
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
					currentPlayer = null;
					updateCurrentTiles();
					getGame().setCurrentState(Game.STATE_ACTION_BEGIN);
				} else if (currentFieldAction == FieldAction.SHOT) {
					
				}
			}
		}
	}
	
	public void endFieldAction() {
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
			result.add(tile);
		}
		Collections.reverse(result);
		return result;
	}
	
	public boolean isFree(int x, int y) {
		return !isTeamOnPosition(x, y, getTeam1()) && !isTeamOnPosition(x, y, getTeam2());
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
