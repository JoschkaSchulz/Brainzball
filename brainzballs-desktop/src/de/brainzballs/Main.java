package de.brainzballs;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "brainzballs";
		cfg.useGL20 = true;
		cfg.resizable = false;
		cfg.width = 1920;
		cfg.height = 1080;
		
		new LwjglApplication(new BrainzBalls(), cfg);
	}
}
