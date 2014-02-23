package de.brainzballs.manu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;

import de.brainzballs.BrainzBalls;
import de.brainzballs.game.Game;
import de.brainzballs.game.footballfield.team.Player.PlayerType;
import de.brainzballs.game.overlay.Fight;
import de.brainzballs.helper.ResourceLoader;

public class MainMenu extends Group {
	private Image background;
	private Image ground;
	
	private BrainzBalls brainzBalls;
	private List<Image> clouds;
	
	public MainMenu(BrainzBalls brainzBalls) {
		this.brainzBalls = brainzBalls;
		this.clouds = new ArrayList<Image>();
		this.clouds.add(new Image(ResourceLoader.CLOUD[0]));
		this.clouds.add(new Image(ResourceLoader.CLOUD[0]));
		this.clouds.add(new Image(ResourceLoader.CLOUD[1]));
		this.clouds.add(new Image(ResourceLoader.CLOUD[2]));
		this.clouds.add(new Image(ResourceLoader.CLOUD[2]));
		
		createBackground();
		
		for(Image cloud : clouds) {
			cloud.setScale(0.75f);
			cloud.setPosition((int)(Gdx.graphics.getWidth() + (Math.random() * 1500)),
					(int) (300 + (Math.random() * 500)));
			addActor(cloud);
		}
		
		createHumanPlayer();
		
		createMenu();
	}
	
	private SkeletonData leftSkeletonData;
	private SkeletonRenderer leftRenderer;
	private Animation leftRun, leftRunBall;
	private Skeleton leftSkeleton;
	private AnimationState leftState;
	private void createHumanPlayer() {
		//Loading Player Skeleton and Animation
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/Field/Player/Player.atlas"));
		SkeletonJson jsonSkeleton = new SkeletonJson(atlas);
		leftSkeletonData = jsonSkeleton.readSkeletonData(Gdx.files.internal("data/Field/Player/Player.json"));
		
		leftRenderer = new SkeletonRenderer();
		
		leftSkeleton = new Skeleton(leftSkeletonData);
		leftSkeleton.updateWorldTransform();
		leftSkeleton.setX(-200);
		leftSkeleton.setY(120);
		
		leftSkeleton.setSkin("WhiteTeam");
		leftSkeleton.setToSetupPose();
		
		AnimationStateData stateData = new AnimationStateData(leftSkeletonData); // Defines mixing (crossfading) between animations.
		stateData.setDefaultMix(0.5f);

		leftState = new AnimationState(stateData);
		
		//Set the default animation on idle
		leftState.setAnimation(0, "run", true);
	}
	
	private void createBackground() {
		background = new Image(ResourceLoader.MENU_BACKGROUND_SKY);
		addActor(background);
		
		ground = new Image(ResourceLoader.MENU_GROUND);
		addActor(ground);
	}
	
	private void startGame() {
		brainzBalls.startGame();
	}
	
	private void showCredits() {
		
	}
	
	private void endGame() {
		Gdx.app.exit();
	}
	
	
	
	@Override
	public void act(float delta) {
		leftState.update(delta);
		leftState.apply(leftSkeleton);
		leftSkeleton.updateWorldTransform(); 
		
		leftSkeleton.setX(leftSkeleton.getX() + (150 * delta));
		
		if(leftSkeleton.getX() > Gdx.graphics.getWidth() + 200) {
			leftSkeleton.setX(-200);
			int head = (int) (Math.round(Math.random() * 4)-1);
			leftSkeletonData.findSlot("Head").setAttachmentName((head == 0 ? "Head" : "Head"+head));
			leftSkeleton.setToSetupPose();
		}
		
		for(Image cloud : clouds) {
			cloud.moveBy(-100 * delta, 0);
			if(cloud.getX() < -200) {
				cloud.setPosition((int)(Gdx.graphics.getWidth() + (Math.random() * 500)),
						(int) (300 + (Math.random() * 500)));
			}
		}

		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		leftRenderer.draw(batch, leftSkeleton);

	}

	private void createMenu() {		
		Table table = new Table();
		
		TextButton startButton = new TextButton("Starte Spiel", ResourceLoader.SKIN);
		startButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				startGame();
			}
		});
		
		TextButton creditsButton = new TextButton("Credits", ResourceLoader.SKIN);	
		creditsButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				showCredits();
			}
		});
		
		TextButton endButton = new TextButton("Beenden", ResourceLoader.SKIN);
		endButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				endGame();
			}
		});
		
		table.add(startButton).row().pad(15f);
		table.add(creditsButton).row();
		table.add(endButton);
		
		table.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/1.5f);
		addActor(table);
	}
}
