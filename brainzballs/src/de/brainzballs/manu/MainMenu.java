package de.brainzballs.manu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

import de.brainzballs.BrainzBalls;
import de.brainzballs.game.Game;
import de.brainzballs.helper.RessourceLoader;

public class MainMenu extends Group {
	private Image background;
	private Image ground;
	
	private BrainzBalls brainzBalls;
	
	public MainMenu(BrainzBalls brainzBalls) {
		this.brainzBalls = brainzBalls;
		
		createBackground();
		createMenu();
	}
	
	private void createBackground() {
		background = new Image(RessourceLoader.MENU_BACKGROUND_SKY);
		addActor(background);
		
		ground = new Image(RessourceLoader.MENU_GROUND);
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
	
	private void createMenu() {		
		Table table = new Table();
		
		TextButton startButton = new TextButton("Starte Spiel", RessourceLoader.SKIN);
		startButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				startGame();
			}
		});
		
		TextButton creditsButton = new TextButton("Credits", RessourceLoader.SKIN);	
		creditsButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				showCredits();
			}
		});
		
		TextButton endButton = new TextButton("Beenden", RessourceLoader.SKIN);
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
