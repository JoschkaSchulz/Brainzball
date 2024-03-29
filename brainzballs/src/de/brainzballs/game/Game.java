package de.brainzballs.game;

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
	
	public static final int TEAM_1 	= 0;
	public static final int TEAM_2 	= 1;
	
	public static final int STATE_ACTION_CHOOSE	= 0;
	public static final int STATE_ACTION_BEGIN	= 1;
	public static final int STATE_ACTION_END	= 2;
	
	private Field field;
	private int currentState, currentTeam;
	private BrainzBalls brainzBalls;
	
	private Table points;
	private Label team1Points, team2Points;
	private int t1Points, t2Points;
	private Table actions;
	
	public Game(BrainzBalls brainzBalls) {
		this.brainzBalls = brainzBalls;
		this.t1Points = this.t2Points = 0;
		
		this.field = Field.newInstance(19, 9);
		addActor(this.field);
		
		//Drawing the labels with the points
		createPointUI();
		
		//Drawing the Buttons with the actions
		createActionUI();
		showActions();
		
		setCurrentState(STATE_ACTION_CHOOSE);
	}
	
	public void setPointsTeam1(int points) {
		this.team1Points.setText(points + " Punkte");
		System.out.println(points + " Punkte");
	}
	
	public void setPointsTeam2(int points) {
		this.team2Points.setText(points + " Punkte");
		System.out.println(points + " Punkte");
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
	
	public void setCurrentState(int state) {
		currentState = state;
		if (currentState == STATE_ACTION_CHOOSE) {
		
			// Choose action
			currentTeam = (currentTeam == TEAM_1 ? TEAM_2 : TEAM_1);
		
		} else if (currentState == STATE_ACTION_BEGIN){
			
			// Action begins
			
		} else if (currentState == STATE_ACTION_END) {
			
			// Action ends
			setCurrentState(STATE_ACTION_CHOOSE);
		}
	}
	
	public int getCurrentState() {
		return currentState;
	}
	
	public int getCurrentTeam() {
		return currentTeam;
	}
}
