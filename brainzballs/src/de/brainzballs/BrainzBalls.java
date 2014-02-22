package de.brainzballs;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.brainzballs.game.Game;
import de.brainzballs.helper.RessourceLoader;
import de.brainzballs.manu.MainMenu;

public class BrainzBalls implements ApplicationListener {
	private Stage stage;
	
	private MainMenu mainMenu;
	private Game game;
	
	public void create () {
	    stage = new Stage();
	    Gdx.input.setInputProcessor(stage);
	    
	    //Load Resources
	    RessourceLoader.loadRessources();
	    
	    //create main menu
	    mainMenu = new MainMenu(this);
	    
	    //create game
	    game = new Game(this);
	    
	    //Adding the menu to stage
	    stage.addActor(mainMenu);
	}

	public void resize (int width, int height) {
	    stage.setViewport(width, height, true);
	}

	public void render () {
	    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	    stage.act(Gdx.graphics.getDeltaTime());
	    stage.draw();
	}

	public void dispose() {
	    stage.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	public void startGame() {
		stage.clear();
		stage.addActor(game);
	}
	
	public void startMenu() {
		stage.clear();
		stage.addActor(mainMenu);
	}
}
