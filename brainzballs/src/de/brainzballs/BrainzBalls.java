package de.brainzballs;

import java.util.Arrays;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.brainzballs.game.Game;
import de.brainzballs.helper.ResourceLoader;
import de.brainzballs.manu.MainMenu;

public class BrainzBalls implements ApplicationListener {
	private Stage stage;
	
	private MainMenu mainMenu;
	private Game game;
	private Music music;
	
	public void create () {
	    stage = new Stage();
	    Gdx.input.setInputProcessor(stage);
	    
	    //Load Resources
	    ResourceLoader.loadRessources();
	    
	    //create main menu
	    mainMenu = new MainMenu(this);
	    
	    //create game
	    game = new Game(this);
	    
	    //Adding the menu to stage
	    stage.addActor(mainMenu);
	    
	    music = Gdx.audio.newMusic(Gdx.files.internal("data/music/music1.mp3"));
	    music.setLooping(true);
	    music.setVolume(0.15f);
	    music.play();
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
