package de.brainzballs.game.footballfield;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.brainzballs.game.Game;
import de.brainzballs.game.footballfield.team.Team;
import de.brainzballs.helper.ResourceLoader;

public class Tile extends Group {

	private final int CONDITION_GOOD = 1;
	private final int CONDITION_NORMAL = 2;
	private final int CONDITION_BAD = 3;
	
	private int x, y;
	private int condition;
	private boolean border;
	private boolean goal;
	private boolean highlighted;
	private boolean mouseOver;
	private int goodId, normalId, badId; // Texture Array ID's
	private Label debugLabel;

	private Tile(int x, int y) {
		this.x = x;
		this.y = y;
		this.condition = CONDITION_GOOD;
		this.highlighted = false;
		this.mouseOver = false;
		this.debugLabel = new Label("X", ResourceLoader.SKIN);
		this.debugLabel.setPosition((x * 64) + 16, (y * 64) + 16);
		this.debugLabel.setName("debugLabel");
		goodId = (int) Math.round(Math.random()
				* (ResourceLoader.TILE_GOOD.length - 1));
		normalId = (int) Math.round(Math.random()
				* (ResourceLoader.TILE_NORMAL.length - 1));
		badId = (int) Math.round(Math.random()
				* (ResourceLoader.TILE_BAD.length - 1));
		
		this.addListener(new ClickListener() {

			@Override
			public boolean isOver() {
				return super.isOver();
			}
			
		});
	}

	public int getPositionX() {
		return this.x;
	}

	public int getPositionY() {
		return this.y;
	}
	
	public static Tile newInstance(int x, int y) {
		return new Tile(x, y);
	}

	public boolean isFree() {
		return getField().isFree(x, y);
	}
	
	public boolean isInTeam(Team team) {
		return getField().isTeamOnPosition(x, y, team);
	}
	
	public boolean isBorder() {
		return border;
	}

	public boolean isGoal() {
		return goal;
	}
	
	public boolean isHighlighted() {
		return highlighted;
	}
	
	public Field getField() {
		return (Field) getParent();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	public List<Tile> getNeighbours() {
		List<Tile> tiles = new ArrayList<Tile>();
		tiles = getNeighbours(tiles, getField().getTile(x, y + 1));
		tiles = getNeighbours(tiles, getField().getTile(x, y - 1));
		tiles = getNeighbours(tiles, getField().getTile(x + 1, y));
		tiles = getNeighbours(tiles, getField().getTile(x - 1, y));
		return tiles;
	}
	
	private List<Tile> getNeighbours(List<Tile> tiles, Tile tile) {
		if (tile != null) {
			tiles.add(tile);
		}
		return tiles;
	}
	
	public boolean hasOpponentNeighbour() {
		boolean result = false;
		/*result |= getField().isOpponentOnPosition(x + 1, y);
		result |= getField().isOpponentOnPosition(x - 1, y);
		result |= getField().isOpponentOnPosition(x, y + 1);
		result |= getField().isOpponentOnPosition(x, y - 1);*/
		result |= getField().isHealOpponentOnPosition(x + 1, y);
		result |= getField().isHealOpponentOnPosition(x - 1, y);
		result |= getField().isHealOpponentOnPosition(x, y + 1);
		result |= getField().isHealOpponentOnPosition(x, y - 1);
		return result;
	}
	
	public Label getDebugLabel() {
		return debugLabel;
	}
	
	private void mouseClick() {

		// Click is possible in choose state
		if (getField().getGame().getCurrentState() == Game.STATE_ACTION_CHOOSE) {	
		
			// If no player selected select one
			if (!getField().isCurrentPlayerSelected()) {
				getField().setCurrentPlayer(x, y);
			} else {
				
				// If this tile not reachable check if a new player is selected
				if (!getField().isTileReachable(this)) {
					getField().setCurrentPlayer(x, y);
				} else {
					if (getField().getCurrentFieldAction() == Field.FieldAction.PASS) {
						
						// Pass
						if (getField().isFriendOnPosition(x, y)) {
							System.out.println("PASS!");
						}
						
					} else if (getField().getCurrentFieldAction() == Field.FieldAction.MOVE) {
						
						// Move
						if (getField().isTileReachable(this)) {
							System.out.println("MOVE!");
							getField().beginFieldAction(this);
						}
						
					} else if (getField().getCurrentFieldAction() == Field.FieldAction.SHOT) {
						
						// Shot
						if (getField().isTileReachable(this)) {
							System.out.println("SHOT!");
						}
					}
				}
			}
		}
	}
	
	public int getCondition() {
		return condition;
	}
	
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		switch (condition) {
		default:
		case CONDITION_GOOD:
			batch.draw(ResourceLoader.TILE_GOOD[goodId], x * 64, y * 64);
			break;
		case CONDITION_NORMAL:
			batch.draw(ResourceLoader.TILE_NORMAL[normalId], x * 64, y * 64);
			break;
		case CONDITION_BAD:
			batch.draw(ResourceLoader.TILE_BAD[badId], x * 64, y * 64);
			break;
		}

		batch.draw(ResourceLoader.TILE_GRID, x * 64, y * 64);
		
		if (highlighted)  {
			if(findActor("debugLabel") == null) addActor(debugLabel);
			batch.draw(ResourceLoader.HIGHLIGHT, x * 64, y * 64);
		}else{
			if(findActor("debugLabel") != null) removeActor(debugLabel);
		}
		
		if (mouseOver) {
			batch.draw(ResourceLoader.HIGHLIGHT, x * 64, y * 64);
			
			if(Gdx.input.isTouched()) {
				mouseClick();
			}
		}
		
		super.draw(batch, parentAlpha);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
	}
}
