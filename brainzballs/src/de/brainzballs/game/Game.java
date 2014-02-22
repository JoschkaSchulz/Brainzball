package de.brainzballs.game;

import com.badlogic.gdx.scenes.scene2d.Group;

import de.brainzballs.BrainzBalls;
import de.brainzballs.game.footballfield.Field;

public class Game extends Group {
	public static final int STATE_TEAM1 	= 0;
	public static final int STATE_TEAM2 	= 1;
	public static final int STATE_EVENTS 	= 2;
	public static final int STATE_ACTION 	= 3;
	
	private Field field;
	private int state;
	private BrainzBalls brainzBalls;
	
	public Game(BrainzBalls brainzBalls) {
		this.brainzBalls = brainzBalls;
		
		this.field = Field.newInstance(21, 11);
		addActor(this.field);
	}
}
