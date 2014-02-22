package de.brainzballs.game.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

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
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		timer += delta;
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
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		//drawing the overlay
		batch.draw(ResourceLoader.OVERLAY_BACKGROUND, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	
		batch.draw(ResourceLoader.MENU_BACKGROUND_SKY, (Gdx.graphics.getWidth()/2)-512, (Gdx.graphics.getHeight()/2)-300, 1024, 600);
	
		batch.draw(ResourceLoader.MENU_GROUND, (Gdx.graphics.getWidth()/2)-512, (Gdx.graphics.getHeight()/2)-300, 1024, 200);
	
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
			state = STATE_END;
		}else if(state == STATE_END) {
			getParent().removeActor(this);
		}
		
		super.draw(batch, parentAlpha);
	}
	
}
