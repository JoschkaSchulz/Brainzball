package de.brainzballs;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class BrainzBalls implements ApplicationListener {
	private Stage stage;

	public void create () {
		// Delete Me
	    stage = new Stage();
	    Gdx.input.setInputProcessor(stage);
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
}
