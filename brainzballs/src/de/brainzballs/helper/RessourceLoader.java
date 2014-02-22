package de.brainzballs.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RessourceLoader {

	public static TextureRegion[] CLOUD;
	public static TextureRegion MENU_BACKGROUND_SKY;
	public static TextureRegion MENU_GROUND;
	
	public static BitmapFont BUTTON_FONT;
	public static TextureRegion BUTTON;
	public static TextureRegion BUTTON_PRESSED;
	
	public static void loadRessources() {
		Texture.setEnforcePotImages(false);
		CLOUD = new TextureRegion[3];
		CLOUD[0] = new TextureRegion(new Texture(Gdx.files.internal("data/Menu/Cloud1.png")));
		CLOUD[1] = new TextureRegion(new Texture(Gdx.files.internal("data/Menu/Cloud2.png")));
		CLOUD[2] = new TextureRegion(new Texture(Gdx.files.internal("data/Menu/Cloud3.png")));
		
		MENU_BACKGROUND_SKY = new TextureRegion(new Texture(Gdx.files.internal("data/Menu/MenuBackgroundSky.png")));
	
		MENU_GROUND = new TextureRegion(new Texture(Gdx.files.internal("data/Menu/MenuGround.png")));
		
		BUTTON_FONT = new BitmapFont(Gdx.files.internal("data/GUI/Fonts/ButtonFont.fnt"),Gdx.files.internal("data/GUI/Fonts/ButtonFont.png"), false);
		BUTTON = new TextureRegion(new Texture(Gdx.files.internal("data/GUI/Buttons/Button.png")));
		BUTTON_PRESSED = new TextureRegion(new Texture(Gdx.files.internal("data/GUI/Buttons/ButtonPressed.png")));
	}
}
