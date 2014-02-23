package de.brainzballs.game;

import java.util.Arrays;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.brainzballs.BrainzBalls;
import de.brainzballs.game.footballfield.Field;
import de.brainzballs.game.footballfield.Field.FieldAction;
import de.brainzballs.game.overlay.Fight;
import de.brainzballs.helper.ResourceLoader;

public class Game extends Group {
	public static final int STATE_TEAM1 	= 0;
	public static final int STATE_TEAM2 	= 1;
	public static final int STATE_ACTION 	= 2;
	
	private Field field;
	private int state;
	private BrainzBalls brainzBalls;
	
	private Table points;
	private Label team1Points, team2Points;
	private Table actions;
	
	public Game(BrainzBalls brainzBalls) {
		this.brainzBalls = brainzBalls;
		
		this.field = Field.newInstance(19, 9);
		addActor(this.field);
		
		//Drawing the labels with the points
		createPointUI();
		
		//Drawing the Buttons with the actions
		createActionUI();
		showActions();
		
		state = STATE_TEAM1;
		
		debugOverlay();
	}
	
	private void debugOverlay() {
		Fight fight = new Fight();
		addActor(fight);
	}
	
	private void createPointUI() {
		points = new Table();
		team1Points = new Label("0 Punkte", ResourceLoader.SKIN);
		team2Points = new Label("0 Punkte", ResourceLoader.SKIN);
		points.add(team1Points).padRight(700f);
		points.add(team2Points);
		points.setPosition(field.getX() + (field.getWidth()/2), 
				field.getY() + field.getHeight() + (team1Points.getHeight()/2));
		addActor(points);
	}
	
	public void showActions() {
		addActor(actions);
	}
	
	public void hideActions() {
		removeActor(actions);
	}
	
	private void createActionUI() {
		actions = new Table();
		TextButton pass = new TextButton("Passen", ResourceLoader.SKIN);
		pass.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				field.setCurrentFieldAction(FieldAction.PASS);
			}
		});
		TextButton shot = new TextButton("shot", ResourceLoader.SKIN);
		shot.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				field.setCurrentFieldAction(FieldAction.SHOT);
			}
		});
		TextButton move = new TextButton("move", ResourceLoader.SKIN);
		move.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				field.setCurrentFieldAction(FieldAction.MOVE);
			}
		});
		TextButton random = new TextButton("???", ResourceLoader.SKIN);
		
		actions.add(pass).size(250f, 64f).padRight(50f);
		actions.add(shot).size(250f, 64f).padRight(50f);
		actions.add(move).size(250f, 64f).padRight(50f);
//		actions.add(random).size(250f, 64f);
		
		actions.setPosition(field.getX() + (field.getWidth()/2),
				field.getY() + - (pass.getHeight()/2));
	}
	
	private void eventHandling() {
		
	}
	
	private void actionHandling() {
		
	}
	
	public void nextState() {
		state++;
		
		if(state == STATE_ACTION) {
			eventHandling();
			actionHandling();
			state = STATE_TEAM1;
		}
	}
}
