package de.brainzballs.game.footballfield.team;

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
	
	private int x, y;
	private PlayerType playerType;
	private int goals, fouls, passes, moves, shoots;
	private boolean jailed;
	private Animation idle, run;
	private AnimationState state;
	private Skeleton skeleton;
	SkeletonData skeletonData;
	private SkeletonRenderer renderer;
	
	private Player(int x, int y, PlayerType playerType) {
		this.x = x;
		this.y = y;
		this.playerType = playerType;
		
		//Loading Player Skeleton and Animation
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("Player.atlas"));
		SkeletonJson json = new SkeletonJson(atlas);
		skeletonData = json.readSkeletonData(Gdx.files.internal("Player.json"));
		
		run = skeletonData.findAnimation("run");
		idle = skeletonData.findAnimation("idle1");
		
		skeleton = new Skeleton(skeletonData);
		skeleton.updateWorldTransform();
		skeleton.setX(-50);
		skeleton.setY(20);
		
		AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
		stateData.setMix("walk", "jump", 0.2f);
		stateData.setMix("jump", "walk", 0.4f);
		stateData.setMix("jump", "jump", 0.2f);

		state = new AnimationState(stateData);
		
		//Set the default animation on idle
		state.setAnimation(0, "idle1", true);
	}
	
	public static Player newInstance(int x, int y, PlayerType playerType) {
		return new Player(x, y, playerType);
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
	
	public boolean canPass() {
		if (!hasBall())
			return false;
		
		// TODO
		return false;
	}
	
	public boolean canMove() {
		// TODO
		return false;
	}
	
	public boolean canShot() {
		if (!hasBall())
			return false;
		
		// TODO
		return false;
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
		// TODO
		return false;
	}
	
	public PlayerType getPlayerType() {
		return playerType;
	}
	
	public Team getTeam() {
		return (Team)getParent();
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
		
		renderer.draw(batch, skeleton);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		
		skeleton.updateWorldTransform();
		skeleton.update(delta);
	}
	
	
}
