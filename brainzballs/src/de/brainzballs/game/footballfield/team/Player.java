package de.brainzballs.game.footballfield.team;

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

public class Player extends Actor {
	
	public enum PlayerType {
	    KEEPER, DEFENDER, MIDFIELDER, STRIKER
	}
	
	public static final int NORTH 	= 0;
	public static final int WEST 	= 1;
	public static final int SOUTH  	= 2;
	public static final int EAST 	= 3;
	
	private LinkedList<Tile> moveList;
	private Team team;
	private int x, y;
	private int offended;
	private PlayerType playerType;
	private boolean jailed;
	private Animation idle, idleBall, run, runBall, shootBall;
	private Animation[] specialIdle;
	private float idleTimer;
	private AnimationState state;
	private Skeleton skeleton;
	private SkeletonRenderer renderer;
	private SkeletonData skeletonData;
	
	private String headString;
	private String teamString;
	private boolean zombie;
	
	private Player(int x, int y, PlayerType playerType, int direction, Team team) {
		this.x = x;
		this.y = y;
		this.offended = 0;
		this.playerType = playerType;
		this.idleTimer = (float) (Math.random()*10);
		this.team = team;
		this.moveList = new LinkedList<Tile>();
		
		// Loading Player Skeleton and Animation
		if(direction == WEST) {
			TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/Field/Zombie/Zombie.atlas"));
			SkeletonJson jsonSkeleton = new SkeletonJson(atlas);
			jsonSkeleton.setScale(0.5f);
			skeletonData = jsonSkeleton.readSkeletonData(Gdx.files.internal("data/Field/Zombie/Zombie.json"));
			this.zombie = true;
		}else{
			TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/Field/Player/Player.atlas"));
			SkeletonJson jsonSkeleton = new SkeletonJson(atlas);
			jsonSkeleton.setScale(0.5f);
			skeletonData = jsonSkeleton.readSkeletonData(Gdx.files.internal("data/Field/Player/Player.json"));
			this.zombie = false;
		}
		
		renderer = new SkeletonRenderer();
		
		run = skeletonData.findAnimation("run");
		runBall = skeletonData.findAnimation("runball");
		shootBall = skeletonData.findAnimation("shootball");
		idle = skeletonData.findAnimation("idle1");
		specialIdle = new Animation[5];
		specialIdle[0] = skeletonData.findAnimation("idle2");
		specialIdle[1] = skeletonData.findAnimation("idle3");
		specialIdle[2] = skeletonData.findAnimation("idle4");
		specialIdle[3] = skeletonData.findAnimation("idle5");
		specialIdle[4] = skeletonData.findAnimation("idle6");
		idleBall = skeletonData.findAnimation("idle1ball");
		
		skeleton = new Skeleton(skeletonData);
		skeleton.updateWorldTransform();
		
		if(playerType == PlayerType.DEFENDER) {
			skeletonData.findSlot("Head").setAttachmentName("Head2");
			this.headString = "Head2";
		}else if(playerType == PlayerType.KEEPER){
			skeletonData.findSlot("Head").setAttachmentName("Head4");
			this.headString = "Head4";
		}else if(playerType == PlayerType.MIDFIELDER){
			skeletonData.findSlot("Head").setAttachmentName("Head3");
			this.headString = "Head3";
		}else if(playerType == PlayerType.STRIKER){
			skeletonData.findSlot("Head").setAttachmentName("Head");
			this.headString = "Head";
		}else{
			skeletonData.findSlot("Head").setAttachmentName("Head");
			this.headString = "Head";
		}
		
		if(direction == WEST) {
			skeleton.setFlipX(true);
			skeleton.setSkin("RedTeam");
			teamString = "RedTeam";
		}else{
			skeleton.setSkin("WhiteTeam");
			teamString = "WhiteTeam";
		}
		skeleton.setToSetupPose();
		
		AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
		stateData.setDefaultMix(0.5f);

		state = new AnimationState(stateData);
		
		//Set the default animation on idle
		state.addAnimation(0, "idle1", true,0);
		
		skeleton.setX((x*64)+32);
		skeleton.setY((y*64)+32);
	}
	
	public static Player newInstance(int x, int y, PlayerType playerType, int direction, Team team) {
		return new Player(x, y, playerType, direction, team);
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
	
	public void incrementOffended() {
		offended++;
	}
	
	public void decrementOffended() {
		if (offended > 0)
			offended--;
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
		Player other = (Player) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public void addMovePoints(List<Tile> list) {
		this.moveList.addAll(list);
	}
	
	public void setPositionXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	private  void checkLastMove() {
		setPositionXY(moveTile.getPositionX(), moveTile.getPositionY());
		state.setAnimation(0, idle, true);
		isRunning = false;
		
		if(hasBall()) {
			state.setAnimation(0, idleBall, true);
			getField().getBall().setVisible(false);
		}
		
		getField().endFieldAction();
	}
	
	private boolean isRunning = false;
	private Tile moveTile;
	private void hanldeMoveList() {
		//First start
		if(!isRunning && moveList.size() > 0) {
			isRunning = true;
			if(hasBall()) {
				state.setAnimation(0, runBall, true);
			}else{
				state.setAnimation(0, run, true);
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
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		renderer.draw(batch, skeleton);
	}

	private float speed = 150;
	@Override
	public void act(float delta) {
		super.act(delta);
		hanldeMoveList();
		if(moveTile == null) {
			this.idleTimer += delta;
		
			if(idleTimer >= 15 && !hasBall()) {
				idleTimer = 0;
				state.setAnimation(0, specialIdle[(int) Math.round(Math.random()*(specialIdle.length-1))], false);
				state.addAnimation(0, idle, true, state.getCurrent(0).getTime());
			}
		}else{
			if(moveTile.getPositionX() > getPositionX()) {
				skeleton.setX(skeleton.getX() + (delta*speed));
				if((moveTile.getPositionX()*64)+32 < skeleton.getX()) {
					skeleton.setX((moveTile.getPositionX()*64)+32);
					if(moveList.size() == 0) {
						checkLastMove();
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
						checkLastMove();
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
						checkLastMove();
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
						checkLastMove();
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
