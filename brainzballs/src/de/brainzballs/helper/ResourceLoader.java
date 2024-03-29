package de.brainzballs.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

public class ResourceLoader {

	public static TextureRegion OVERLAY_BACKGROUND;
	
	public static TextureRegion[] CLOUD;
	public static TextureRegion MENU_BACKGROUND_SKY;
	public static TextureRegion MENU_GROUND;

	public static TextureRegion HEART_FULL;
	public static TextureRegion HEART_EMPTY;
	
	public static TextureRegion TILE_GOOD[];
	public static TextureRegion TILE_NORMAL[];
	public static TextureRegion TILE_BAD[];
	public static TextureRegion TILE_GRID;
	public static TextureRegion FIELD_OVERLAY;
	public static TextureRegion HIGHLIGHT;
	
	public static BitmapFont BUTTON_FONT;
	public static TextureRegion BUTTON;
	public static TextureRegion BUTTON_PRESSED;
	
	
	public static Skin SKIN;
	public static void loadRessources() {
		boolean export = false;
		String path = (export ? "assets/data" : "data");
		
		Texture.setEnforcePotImages(false);
		CLOUD = new TextureRegion[3];
		CLOUD[0] = new TextureRegion(new Texture(Gdx.files.internal(path+"/Menu/Cloud1.png")));
		CLOUD[1] = new TextureRegion(new Texture(Gdx.files.internal(path+"/Menu/Cloud2.png")));
		CLOUD[2] = new TextureRegion(new Texture(Gdx.files.internal(path+"/Menu/Cloud3.png")));
		MENU_BACKGROUND_SKY = new TextureRegion(new Texture(Gdx.files.internal(path+"/Menu/MenuBackgroundSky.png")));
		MENU_GROUND = new TextureRegion(new Texture(Gdx.files.internal(path+"/Menu/MenuGround.png")));
		
		HEART_FULL = new TextureRegion(new Texture(Gdx.files.internal(path+"/heart_full.png")));
		HEART_EMPTY = new TextureRegion(new Texture(Gdx.files.internal(path+"/heart_empty.png")));
		
		TILE_GOOD = new TextureRegion[3];
		TILE_GOOD[0] = new TextureRegion(new Texture(Gdx.files.internal(path+"/Field/Tiles/GrassTileNormal1.png"))); 
		TILE_GOOD[1] = new TextureRegion(new Texture(Gdx.files.internal(path+"/Field/Tiles/GrassTileNormal2.png"))); 
		TILE_GOOD[2] = new TextureRegion(new Texture(Gdx.files.internal(path+"/Field/Tiles/GrassTileNormal3.png"))); 
		
		TILE_NORMAL = new TextureRegion[2];
		TILE_NORMAL[0] = new TextureRegion(new Texture(Gdx.files.internal(path+"/Field/Tiles/DirtTileNormal1.png"))); 
		TILE_NORMAL[1] = new TextureRegion(new Texture(Gdx.files.internal(path+"/Field/Tiles/DirtTileNormal2.png"))); 
		
		TILE_BAD = new TextureRegion[1];
		TILE_BAD[0] = new TextureRegion(new Texture(Gdx.files.internal(path+"/Field/Tiles/DirtTileBad1.png"))); 
		
		TILE_GRID = new TextureRegion(new Texture(Gdx.files.internal(path+"/Field/Tiles/Grid.png"))); 

		FIELD_OVERLAY = new TextureRegion(new Texture(Gdx.files.internal(path+"/Field/FieldOverlay.png")));
		
		HIGHLIGHT = new TextureRegion(new Texture(Gdx.files.internal(path+"/Field/Tiles/highlight.png"))); 

		OVERLAY_BACKGROUND = new TextureRegion(new Texture(Gdx.files.internal(path+"/Overlay/overlay_background.png"))); 
		
		BUTTON_FONT = new BitmapFont(Gdx.files.internal(path+"/GUI/Fonts/ButtonFont.fnt"),Gdx.files.internal("data/GUI/Fonts/ButtonFont.png"), false);
		BUTTON = new TextureRegion(new Texture(Gdx.files.internal(path+"/GUI/Buttons/Button.png")));
		BUTTON_PRESSED = new TextureRegion(new Texture(Gdx.files.internal(path+"/GUI/Buttons/ButtonPressed.png")));
	
		
		System.out.println("TEXTUREN FERTIG GELADEN!");
		
		//Creating the Skin
		SKIN = new Skin();
		
		SKIN.add("button_font", BUTTON_FONT);
		
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = new TextureRegionDrawable(BUTTON);
		textButtonStyle.down = new TextureRegionDrawable(BUTTON_PRESSED);
		textButtonStyle.over = new TextureRegionDrawable(BUTTON_PRESSED);
		textButtonStyle.checked = new TextureRegionDrawable(BUTTON_PRESSED);
		textButtonStyle.font = SKIN.get("button_font", BitmapFont.class);
		SKIN.add("default", textButtonStyle);
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = SKIN.get("button_font", BitmapFont.class);
		SKIN.add("default", labelStyle);
	}
}
