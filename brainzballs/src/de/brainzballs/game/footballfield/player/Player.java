package de.brainzballs.game.footballfield.player;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;

import de.brainzballs.game.footballfield.Field;
import de.brainzballs.game.footballfield.Tile;
import de.brainzballs.game.footballfield.team.Team;
import de.brainzballs.helper.ResourceLoader;

/**
 * The abstract class of a football player. It can be created via the 
 * {@code PlayerFactory} class.
 */
public abstract class Player extends Actor {
	
	/*************************************************************************************
	 *				variables
	 *************************************************************************************/
	
	//player type enum
	public enum PlayerType {
	    KEEPER, DEFENDER, MIDFIELDER, STRIKER
	}
	
	//static direction fields
	public static final int NORTH 	= 0;
	public static final int WEST 	= 1;
	public static final int SOUTH  	= 2;
	public static final int EAST 	= 3;
	
	//static animation fields
	public static final String ANIMATION_IDLE 		= "idle1";
	public static final String ANIMATION_IDLE_BALL 	= "idle1ball";
	public static final String ANIMATION_RUN 		= "run";
	public static final String ANIMATION_RUN_BALL	= "runball";
	public static final String ANIMATION_SHOOT_BALL	= "shootball";
	
	//common fields
	protected int maxHealth;				//the maximal health points of the player
	protected int currentHealth;			//the current health points of the player
	private LinkedList<Tile> moveList;		//a list of Tiles the player moves next	
	private Team team;						//the team of the player
	private int x, y;						//x and y position of the player in the matrix
	private int offended;					//how many turns is the player knockoff
	private PlayerType playerType;			//the type of the player
	private boolean jailed;					//true if the player if hold by an enemy
	
	//spine fields
	private AnimationState state;			//Animation state of the player
	protected Skeleton skeleton;			//the spline skeleton 
	private SkeletonRenderer renderer;		//the renderer for drawing the skeleton
	protected SkeletonData skeletonData;	//the skeelton data	
	private Animation[] specialIdle;		//a list of a amount of idle animations
	private float idleTimer;				//a timer for checking the next special idle animation
	
	//moving fields
	private boolean isRunning = false;			//Is the player running
	private Tile moveTile;						//The Tile the player moves next
	private float speed = 150;					//Animation speed
	
	//fighting strings
	protected String headString;				//the head index of the player
	private String teamString;					//the team index of the player
	private boolean zombie;						//is the player a zombie
	
	/*************************************************************************************
	 *				Constructor
	 *************************************************************************************/	
	
	/**
	 * The standard constructor of the player
	 * 
	 * @param x the x position of the player in the field matrix
	 * @param y the y position of the player in the field matrix
	 * @param playerType the type from the {@code PlayerType} for the player type
	 * @param direction the view direction from the player as int
	 * @param team the team the player is in
	 */
	protected Player(int x, int y, PlayerType playerType, int direction, Team team) {
		this.x = x;
		this.y = y;
		this.maxHealth = 5;
		this.currentHealth = maxHealth;
		this.offended = 0;
		this.playerType = playerType;
		this.idleTimer = (float) (Math.random()*10);
		this.team = team;
		this.moveList = new LinkedList<Tile>();
		this.zombie = direction == WEST;
		this.teamString = (zombie ? "RedTeam" : "WhiteTeam");
		
		loadSpineSkeleton();
	}
	
	/*************************************************************************************
	 *				getter and setter
	 *************************************************************************************/
	
	public void setBallIdle(boolean ballIdle) {
		if(ballIdle) {
			state.setAnimation(0, ANIMATION_IDLE_BALL, true);
		}else{
			state.setAnimation(0, ANIMATION_IDLE, true);
		}
	}
	
	public String getHeadString() {
		return headString;
	}

	public void setHeadString(String headString) {
		this.headString = headString;
	}

	public String getTeamString() {
		return teamString;
	}

	public void setTeamString(String teamString) {
		this.teamString = teamString;
	}

	public boolean isZombie() {
		return zombie;
	}

	public void setZombie(boolean zombie) {
		this.zombie = zombie;
	}

	public boolean isOffended() {
		return offended > 0;
	}
	
	public int getMoveRadius() {
		if (playerType == PlayerType.KEEPER)
			return 2;
		if (playerType == PlayerType.DEFENDER)
			return 4;
		if (playerType == PlayerType.MIDFIELDER)
			return 6;
		if (playerType == PlayerType.STRIKER)
			return 8;
		else
			return 0;
	}
	
	public int getPassRadius() {
		if (playerType == PlayerType.KEEPER)
			return 10;
		if (playerType == PlayerType.DEFENDER)
			return 8;
		if (playerType == PlayerType.MIDFIELDER)
			return 6;
		if (playerType == PlayerType.STRIKER)
			return 4;
		else
			return 0;
	}
	
	public int getShotRadius() {
		if (playerType == PlayerType.KEEPER)
			return 10;
		if (playerType == PlayerType.DEFENDER)
			return 4;
		if (playerType == PlayerType.MIDFIELDER)
			return 6;
		if (playerType == PlayerType.STRIKER)
			return 8;
		else
			return 0;
	}
	
	public boolean hasBall() {
		return getField().isBallOnPosition(x, y);
	}
	
	public PlayerType getPlayerType() {
		return playerType;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public Field getField() {
		return getTeam().getField();
	}
	
	public Tile getTile() {
		return getField().getTile(x, y);
	}
	
	public int getPositionX() {
		return x;
	}
	
	public int getPositionY() {
		return y;
	}
	
	public boolean isJailed() {
		return jailed;
	}
	
	public void setPositionXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public AnimationState getState() {
		return state;
	}
	
	public boolean isDead() {
		return currentHealth <= 0;
	}
	
	/*************************************************************************************
	 *				methods
	 *************************************************************************************/
	
	/**
	 * This method loads all the Spine data
	 */
	protected void loadSpineSkeleton() {
		// Loading Player Skeleton and Animation
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/Field/"+(isZombie()? "Zombie/Zombie" : "Player/Player")+".atlas"));
		SkeletonJson jsonSkeleton = new SkeletonJson(atlas);
		jsonSkeleton.setScale(0.5f);
		
		//load the skeleton data from the json
		skeletonData = jsonSkeleton.readSkeletonData(Gdx.files.internal("data/Field/"+(isZombie()? "Zombie/Zombie" : "Player/Player")+".json"));
		
		//create a renderer for the skeleton
		renderer = new SkeletonRenderer();
		
		//fill a animation array for random idle animations
		specialIdle = new Animation[5];
		specialIdle[0] = skeletonData.findAnimation("idle2");
		specialIdle[1] = skeletonData.findAnimation("idle3");
		specialIdle[2] = skeletonData.findAnimation("idle4");
		specialIdle[3] = skeletonData.findAnimation("idle5");
		specialIdle[4] = skeletonData.findAnimation("idle6");
		
		//create the skeleton from the skeleton data and update it with the world coordinates
		skeleton = new Skeleton(skeletonData);
		skeleton.updateWorldTransform();
		
		//setting up the team
		if(zombie) skeleton.setFlipX(true);
		skeleton.setSkin(teamString);
		skeleton.setToSetupPose();
		
		//set an default animation mix from spline on 0.5f
		AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
		stateData.setDefaultMix(0.5f);

		//initiate a new animation state
		state = new AnimationState(stateData);
		
		//Set the default animation on idle
		state.addAnimation(0, "idle1", true,0);
		
		//set the skeelton on the default coordinates
		skeleton.setX((x*64)+32);
		skeleton.setY((y*64)+32);
	}
	
	/**
	 * This method increments the oddended for this player
	 */
	public void incrementOffended() {
		if(offended <= 0 && !isDead()) {
			state.setAnimation(0, "stunned", false);
			state.addAnimation(0, "stunnedloop", true, state.getCurrent(0).getTime());
		}
		offended++;
	}
	
	/**
	 * This method decrements the offended for this player
	 */
	public void decrementOffended() {
		if (offended > 0){
			offended--;
			currentHealth--;
			if(offended <= 0 && !isDead()) {
				state.setAnimation(0, "stunnedstandup", false);
				state.addAnimation(0, "idle1", true, state.getCurrent(0).getTime());
			}else if(isDead()) {
				state.setAnimation(0, "die", false);
			}
		}
	}
	
	/**
	 * hashCode for equasls
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/**
	 * equals method
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	/**
	 * Add a List of points to the movement of this player. The list will be 
	 * added on the end of the movement list from the player
	 * 
	 * @param list a list of Tiles for each movement
	 */
	public void addMovePoints(List<Tile> list) {
		if(!isDead()) this.moveList.addAll(list);
	}
	
	/**
	 * This method is fired after the last move of the player
	 */
	private  void lastMove() {
		setPositionXY(moveTile.getPositionX(), moveTile.getPositionY());
		state.setAnimation(0, ANIMATION_IDLE, true);
		isRunning = false;
		
		if(hasBall()) {
			state.setAnimation(0, ANIMATION_IDLE_BALL, true);
			getField().getBall().setVisible(false);
		}
		
		getField().endFieldAction();
	}
		
	/**
	 * handles the movement list of the player, if an element is in it
	 */
	private void hanldeMoveList() {
		//First start
		if(!isRunning && moveList.size() > 0) {
			isRunning = true;
			if(hasBall()) {
				state.setAnimation(0, ANIMATION_RUN_BALL, true);
			}else{
				state.setAnimation(0, ANIMATION_RUN, true);
			}
			
			if(hasBall()) getField().getBall().setPositionXY(moveList.getLast().getPositionX(), moveList.getLast().getPositionY());
			
		}
		
		if(moveList.size() > 0 && moveTile == null) {
			moveTile = moveList.getFirst();
			System.out.println("MapTile: " + moveTile.getPositionX() + ":" + moveTile.getPositionY());
			if(moveTile.getPositionX() < getPositionX()) {
				skeleton.setFlipX(true);
				skeleton.setToSetupPose();
			}else{
				skeleton.setFlipX(false);
				skeleton.setToSetupPose();
			}
			moveList.removeFirst();
		}
	}
		
	/**
	 * libGDX draw method
	 */
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		for(int i = 0; i < maxHealth; i++) {
			if(currentHealth > i) {
				batch.draw(ResourceLoader.HEART_FULL, skeleton.getX()-48, skeleton.getY()+(i*16), 16, 16);
			}else{
				batch.draw(ResourceLoader.HEART_EMPTY, skeleton.getX()-48, skeleton.getY()+(i*16), 16, 16);
			}
		}
		
		renderer.draw(batch, skeleton);
	}
	
	/**
	 * libGDX act method
	 */
	@Override
	public void act(float delta) {
		super.act(delta);
		hanldeMoveList();
		if(moveTile == null) {
			this.idleTimer += delta;
		
			if(idleTimer >= 15 && !hasBall() && this.offended <= 0) {
				idleTimer = 0;
				state.setAnimation(0, specialIdle[(int) Math.round(Math.random()*(specialIdle.length-1))], false);
				state.addAnimation(0, ANIMATION_IDLE, true, state.getCurrent(0).getTime());
			}
		}else{
			if(moveTile.getPositionX() > getPositionX()) {
				skeleton.setX(skeleton.getX() + (delta*speed));
				if((moveTile.getPositionX()*64)+32 < skeleton.getX()) {
					skeleton.setX((moveTile.getPositionX()*64)+32);
					if(moveList.size() == 0) {
						lastMove();
					}
					setPositionXY(moveTile.getPositionX(), moveTile.getPositionY());
					getField().orderPlayers();
					moveTile = null;
				}
			}else if(moveTile.getPositionX() < getPositionX()) {
				skeleton.setX(skeleton.getX() - (delta*speed));
				if((moveTile.getPositionX()*64)+32 > skeleton.getX()) {
					skeleton.setX((moveTile.getPositionX()*64)+32);
					if(moveList.size() == 0) {
						lastMove();
					}
					setPositionXY(moveTile.getPositionX(), moveTile.getPositionY());
					getField().orderPlayers();
					moveTile = null;
				}
			}else if(moveTile.getPositionY() < getPositionY()) {
				skeleton.setY(skeleton.getY() - (delta*speed));
				if((moveTile.getPositionY()*64)+32 > skeleton.getY()) {
					skeleton.setY((moveTile.getPositionY()*64)+32);
					if(moveList.size() == 0) {
						lastMove();
					}
					setPositionXY(moveTile.getPositionX(), moveTile.getPositionY());
					getField().orderPlayers();
					moveTile = null;
				}
			}else if(moveTile.getPositionY() > getPositionY()) {
				skeleton.setY(skeleton.getY() + (delta*speed));
				if((moveTile.getPositionY()*64)+32 < skeleton.getY()) {
					skeleton.setY((moveTile.getPositionY()*64)+32);
					if(moveList.size() == 0) {
						lastMove();
					}
					setPositionXY(moveTile.getPositionX(), moveTile.getPositionY());
					getField().orderPlayers();
					moveTile = null;
				}
			}
		}
		
		state.update(delta);
		state.apply(skeleton);
		skeleton.updateWorldTransform(); 
	}
	
	
}
