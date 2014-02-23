package de.brainzballs.game.footballfield.team;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.BoneData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;

import de.brainzballs.game.footballfield.Field;
import de.brainzballs.game.footballfield.Tile;
import de.brainzballs.helper.ResourceLoader;

public class Player extends Actor {
	
	public enum PlayerType {
	    KEEPER, DEFENDER, MIDFIELDER, STRIKER
	}
	
	public static final int NORTH 	= 0;
	public static final int WEST 	= 1;
	public static final int SOUTH  	= 2;
	public static final int EAST 	= 3;
	
	private Team team;
	private int direction;
	private int x, y;
	private PlayerType playerType;
	private int goals, fouls, passes, moves, shoots;
	private boolean jailed;
	private Animation idle, idleBall, run, runBall, shootBall;
	private Animation[] specialIdle;
	private float idleTimer;
	private AnimationState state;
	private Skeleton skeleton;
	private SkeletonRenderer renderer;
	private PolygonSpriteBatch polyBatch;
	private SkeletonData skeletonData;
	
	private Player(int x, int y, PlayerType playerType, int direction, Team team) {
		this.x = x;
		this.y = y;
		this.playerType = playerType;
		this.idleTimer = (float) (Math.random()*10);
		this.direction = direction;
		this.team = team;
		
		polyBatch = new PolygonSpriteBatch();
		
		//Loading Player Skeleton and Animation
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/Field/Player/Player.atlas"));
		SkeletonJson jsonSkeleton = new SkeletonJson(atlas);
		jsonSkeleton.setScale(0.5f);
		skeletonData = jsonSkeleton.readSkeletonData(Gdx.files.internal("data/Field/Player/Player.json"));
		
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
		}else if(playerType == PlayerType.KEEPER){
			skeletonData.findSlot("Head").setAttachmentName("Head4");
		}else if(playerType == PlayerType.MIDFIELDER){
			skeletonData.findSlot("Head").setAttachmentName("Head3");
		}else if(playerType == PlayerType.STRIKER){
			skeletonData.findSlot("Head").setAttachmentName("Head");
		}else{
			skeletonData.findSlot("Head").setAttachmentName("Head");
		}
		
		if(direction == WEST) {
			skeleton.setFlipX(true);
			skeleton.setSkin("RedTeam");
		}else{
			skeleton.setSkin("WhiteTeam");
		}
		skeleton.setToSetupPose();
		
		AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
		stateData.setMix("run", "idle1", 0.2f);
		stateData.setMix("idle1", "run", 0.2f);
		stateData.setMix("idle1", "idle2", 0.2f);
		stateData.setMix("idle1", "idle3", 0.2f);
		stateData.setMix("idle1", "idle4", 0.2f);
		stateData.setMix("idle1", "idle5", 0.2f);
		stateData.setMix("idle1", "idle6", 0.2f);
		stateData.setMix("idle2", "idle1", 0.2f);
		stateData.setMix("idle3", "idle1", 0.2f);
		stateData.setMix("idle4", "idle1", 0.2f);
		stateData.setMix("idle5", "idle1", 0.2f);
		stateData.setMix("idle6", "idle1", 0.2f);

		state = new AnimationState(stateData);
		
		//Set the default animation on idle
		state.addAnimation(0, "idle1", true,0);
	}
	
	public static Player newInstance(int x, int y, PlayerType playerType, int direction, Team team) {
		return new Player(x, y, playerType, direction, team);
	}
	
	public void pass(Tile tile) {
		passes++;
		// TODO
	}
	
	public void move(Tile tile) {
		moves++;
		// TODO
	}
	
	public void shoot(Tile tile) {
		shoots++;
		// TODO
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
	
	public int getPasses() {
		return passes;
	}
	
	public int getMoves() {
		return moves;
	}
	
	public int getShoots() {
		return shoots;
	}
	
	public int getGoals() {
		return goals;
	}
	
	public int getFouls() {
		return fouls;
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
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		skeleton.setX((x*64)+32);
		skeleton.setY((y*64)+32);
		
		renderer.draw(batch, skeleton);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		this.idleTimer += delta;
		
		if(idleTimer >= 15) {
			idleTimer = 0;
			state.setAnimation(0, specialIdle[(int) Math.round(Math.random()*(specialIdle.length-1))], false);
			state.addAnimation(0, idle, true, state.getCurrent(0).getTime());
		}
		
		state.update(delta);
		state.apply(skeleton);
		skeleton.updateWorldTransform(); 
	}
	
	
}
