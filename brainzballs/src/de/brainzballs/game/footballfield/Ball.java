package de.brainzballs.game.footballfield;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;

public class Ball extends Actor {
	
	private int x, y;
	
	private AnimationState state;
	private Skeleton skeleton;
	private SkeletonRenderer renderer;
	private SkeletonData skeletonData;
	
	private Ball(int x, int y) {
		this.x = x;
		this.y = y;
		this.visible = true;
		
		//Loading Player Skeleton and Animation
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/Field/Player/Ball.atlas"));
		SkeletonJson jsonSkeleton = new SkeletonJson(atlas);
		jsonSkeleton.setScale(0.5f);
		skeletonData = jsonSkeleton.readSkeletonData(Gdx.files.internal("data/Field/Player/Ball.json"));
		
		renderer = new SkeletonRenderer();
		
		skeleton = new Skeleton(skeletonData);
		skeleton.updateWorldTransform();
		
		AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.

		state = new AnimationState(stateData);
		
		//Set the default animation on idle
		//still, pass, shot
		state.setAnimation(0, "still", true);
	}
	
	public static Ball newInstance(int x, int y) {
		return new Ball(x, y);
	}
	
	public void setPositionXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getPositionX() {
		return x;
	}
	
	public int getPositionY() {
		return y;
	}
	
	public Field getField() {
		return (Field)getParent();
	}
	
	private boolean visible;
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public Tile getTile() {
		return getField().getTile(x, y);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		
		state.update(delta);
		state.apply(skeleton);
		skeleton.updateWorldTransform();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		if(visible) {
			skeleton.setX((x*64)+32);
			skeleton.setY((y*64)+32);
			
			renderer.draw(batch, skeleton);
		}
	}

	
	
	
}
