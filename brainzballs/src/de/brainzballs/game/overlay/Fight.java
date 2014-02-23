package de.brainzballs.game.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;

import de.brainzballs.helper.ResourceLoader;

public class Fight extends Group {

	public static final int SELECTION_ROCK 		= 0;
	public static final int SELECTION_PAPER 	= 1;
	public static final int SELECTION_SCISSORS 	= 2;
	
	private static int STATE_CHOOSE		= 0;
	private static int STATE_CALCINIT	= 1;
	private static int STATE_CALC		= 2;
	private static int STATE_FIGHTANIM	= 3;
	private static int STATE_END		= 4;
	
	private int enemySelection, playerSelection;
	private Label enemyLabel, playerLabel;
	private int state;
	private Table actions;
	private float timer;
	
	public Fight() {
		this.enemySelection = (int)Math.round(Math.random()*2);
		
		switch(enemySelection) {
			default:
			case SELECTION_PAPER:
				enemyLabel = new Label("Gegener suchte Kopf aus", ResourceLoader.SKIN);
				break;
			case SELECTION_ROCK:
				enemyLabel = new Label("Gegener suchte Torso aus", ResourceLoader.SKIN);
				break;
			case SELECTION_SCISSORS:
				enemyLabel = new Label("Gegener suchte Beine aus", ResourceLoader.SKIN);
				break;
		}
		enemyLabel.setColor(1f, 0f, 0f, 1f);
		enemyLabel.setPosition((Gdx.graphics.getWidth()/2) + 25, Gdx.graphics.getHeight() - 125);
		enemyLabel.setFontScale(0.65f);
		
		this.state = STATE_CHOOSE;
		
		createActionUI();
		
		createLeftPlayer();
		createRightPlayer();
	}

	private void createActionUI() {
		actions = new Table();
		TextButton pass = new TextButton("Kopf", ResourceLoader.SKIN);
		pass.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				state = STATE_CALCINIT;
				playerSelection = SELECTION_PAPER;
			}
		});
		TextButton shot = new TextButton("Torso", ResourceLoader.SKIN);
		shot.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				state = STATE_CALCINIT;
				playerSelection = SELECTION_ROCK;
			}
		});
		TextButton move = new TextButton("Beine", ResourceLoader.SKIN);
		move.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				state = STATE_CALCINIT;
				playerSelection = SELECTION_SCISSORS;
			}
		});
		
		actions.add(pass).size(250f, 64f).padRight(50f);
		actions.add(shot).size(250f, 64f).padRight(50f);
		actions.add(move).size(250f, 64f).padRight(50f);
		
		actions.setPosition(Gdx.graphics.getWidth()/2, (Gdx.graphics.getHeight()/2)-250);
		addActor(actions);
	}

	private SkeletonData leftSkeletonData;
	private SkeletonRenderer leftRenderer;
	private Animation leftRun, leftRunBall;
	private Skeleton leftSkeleton;
	private AnimationState leftState;
	private void createLeftPlayer() {
		//Loading Player Skeleton and Animation
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/Field/Player/Player.atlas"));
		SkeletonJson jsonSkeleton = new SkeletonJson(atlas);
		leftSkeletonData = jsonSkeleton.readSkeletonData(Gdx.files.internal("data/Field/Player/Player.json"));
		
		leftRenderer = new SkeletonRenderer();
		
		leftRun = leftSkeletonData.findAnimation("run");
		leftRunBall = leftSkeletonData.findAnimation("runball");
		
		leftSkeleton = new Skeleton(leftSkeletonData);
		leftSkeleton.updateWorldTransform();
		leftSkeleton.setX(300);
		leftSkeleton.setY(180);
		
		leftSkeleton.setSkin("WhiteTeam");
		leftSkeleton.setToSetupPose();
		
		AnimationStateData stateData = new AnimationStateData(leftSkeletonData); // Defines mixing (crossfading) between animations.
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

		leftState = new AnimationState(stateData);
		
		//Set the default animation on idle
		leftState.setAnimation(0, "run", true);
	}
	
	private SkeletonData rightSkeletonData;
	private SkeletonRenderer rightRenderer;
	private Animation rightRun, rightrunBall;
	private Skeleton rightSkeleton;
	private AnimationState rightState;
	private void createRightPlayer() {
		//Loading Player Skeleton and Animation
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/Field/Player/Player.atlas"));
		SkeletonJson jsonSkeleton = new SkeletonJson(atlas);
		rightSkeletonData = jsonSkeleton.readSkeletonData(Gdx.files.internal("data/Field/Player/Player.json"));
		
		rightRenderer = new SkeletonRenderer();
		
		rightRun = rightSkeletonData.findAnimation("run");
		rightrunBall = rightSkeletonData.findAnimation("runball");
		
		rightSkeleton = new Skeleton(rightSkeletonData);
		rightSkeleton.updateWorldTransform();
		rightSkeleton.setX(1000);
		rightSkeleton.setY(180);
		
		rightSkeleton.setFlipX(true);
		rightSkeleton.setSkin("RedTeam");
		rightSkeleton.setToSetupPose();
		
		AnimationStateData stateData = new AnimationStateData(rightSkeletonData); // Defines mixing (crossfading) between animations.
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

		rightState = new AnimationState(stateData);
		
		//Set the default animation on idle
		rightState.setAnimation(0, "run", true);
	}
	
	
	/**
	 * 1 for player
	 * 2 for computer/enemy
	 * 0 for draw
	 */
	private int whoWon() {
		if(playerSelection == enemySelection) return 0;
		
		if(playerSelection == SELECTION_PAPER && enemySelection == SELECTION_ROCK) {
			return 1;
		}else if(playerSelection == SELECTION_PAPER && enemySelection == SELECTION_SCISSORS) {
			return 2;
		}else if(playerSelection == SELECTION_ROCK && enemySelection == SELECTION_PAPER) {
			return 2;
		}else if(playerSelection == SELECTION_ROCK && enemySelection == SELECTION_SCISSORS) {
			return 1;
		}else if(playerSelection == SELECTION_SCISSORS && enemySelection == SELECTION_PAPER) {
			return 1;
		}else if(playerSelection == SELECTION_SCISSORS && enemySelection == SELECTION_ROCK) {
			return 2;
		}else{
			return 0;	
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		timer += delta;
		
		leftState.update(delta);
		leftState.apply(leftSkeleton);
		leftSkeleton.updateWorldTransform(); 
		
		rightState.update(delta);
		rightState.apply(rightSkeleton);
		rightSkeleton.updateWorldTransform(); 
		
		if(state == STATE_FIGHTANIM) {
			if(leftSkeleton.getX() + 200 < rightSkeleton.getX()) {
				leftSkeleton.setX(leftSkeleton.getX() + (delta  * 50));
				rightSkeleton.setX(rightSkeleton.getX() - (delta  * 50));
			}else{
				state = STATE_END;
			}
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		//drawing the overlay
		batch.draw(ResourceLoader.OVERLAY_BACKGROUND, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	
		batch.draw(ResourceLoader.MENU_BACKGROUND_SKY, (Gdx.graphics.getWidth()/2)-512, (Gdx.graphics.getHeight()/2)-300, 1024, 600);
	
		batch.draw(ResourceLoader.MENU_GROUND, (Gdx.graphics.getWidth()/2)-512, (Gdx.graphics.getHeight()/2)-300, 1024, 200);
	
		leftRenderer.draw(batch, leftSkeleton);
		rightRenderer.draw(batch, rightSkeleton);
		
		if(state == STATE_CALCINIT) {
			removeActor(actions);
			switch(playerSelection) {
			default:
				case SELECTION_PAPER:
					playerLabel = new Label("Du hast Kopf ausgesucht", ResourceLoader.SKIN);
					break;
				case SELECTION_ROCK:
					playerLabel = new Label("Du hast Torso ausgesucht", ResourceLoader.SKIN);
					break;
				case SELECTION_SCISSORS:
					playerLabel = new Label("Du hast Beine ausgesucht", ResourceLoader.SKIN);
					break;
			}
			playerLabel.setColor(0.25f, 0.75f, 0.25f, 1f);
			playerLabel.setPosition(150, Gdx.graphics.getHeight() - 125);
			playerLabel.setFontScale(0.65f);
			
			state = STATE_CALC;
			timer = 0;
		}else if(state == STATE_CALC) {
			addActor(enemyLabel);
			addActor(playerLabel);
			if(timer >= 5f) state = STATE_FIGHTANIM;
		}else if(state == STATE_FIGHTANIM) {
//			state = STATE_END;
		}else if(state == STATE_END) {
			getParent().removeActor(this);
		}
		
		super.draw(batch, parentAlpha);
	}
	
}
