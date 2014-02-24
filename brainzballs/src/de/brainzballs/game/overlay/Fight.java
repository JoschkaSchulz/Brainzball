package de.brainzballs.game.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationState.AnimationStateListener;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;

import de.brainzballs.game.footballfield.team.Player;
import de.brainzballs.helper.ResourceLoader;

public class Fight extends Group {

	public static final int SELECTION_SCISSORS 	= 0;
	public static final int SELECTION_ROCK 		= 1;
	public static final int SELECTION_PAPER 	= 2;
	
	private static int STATE_CHOOSE		= 0;
	private static int STATE_CALCINIT	= 1;
	private static int STATE_CALC		= 2;
	private static int STATE_FIGHTINIT	= 3;
	private static int STATE_FIGHTANIM	= 4;
	private static int STATE_END		= 5;
	
	private int enemySelection, playerSelection;
	private Label enemyLabel, playerLabel;
	private int state;
	private Table actions;
	private float timer;
	
	private Player left, right;
	
	public Fight(Player left, Player right) {
		this.left = left;
		this.right = right;
		
		this.enemySelection = (int)Math.round(Math.random()*2);
		
		switch(enemySelection) {
			default:
			case SELECTION_PAPER:
				enemyLabel = new Label("Gegener suchte Kopf aus", ResourceLoader.SKIN);
				break;
			case SELECTION_ROCK:
				enemyLabel = new Label("Gegener suchte Torso aus", ResourceLoader.SKIN);
				break;
			case SELECTION_SCISSORS:
				enemyLabel = new Label("Gegener suchte Beine aus", ResourceLoader.SKIN);
				break;
		}
		enemyLabel.setColor(1f, 0f, 0f, 1f);
		enemyLabel.setPosition((Gdx.graphics.getWidth()/2) + 25, Gdx.graphics.getHeight() - 125);
		enemyLabel.setFontScale(0.65f);
		
		this.state = STATE_CHOOSE;
		
		createActionUI();
		
		createLeftPlayer();
		createRightPlayer();
	}

	private void createActionUI() {
		actions = new Table();
		TextButton pass = new TextButton("Kopf", ResourceLoader.SKIN);
		pass.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				state = STATE_CALCINIT;
				playerSelection = SELECTION_PAPER;
			}
		});
		TextButton shot = new TextButton("Torso", ResourceLoader.SKIN);
		shot.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				state = STATE_CALCINIT;
				playerSelection = SELECTION_ROCK;
			}
		});
		TextButton move = new TextButton("Beine", ResourceLoader.SKIN);
		move.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				state = STATE_CALCINIT;
				playerSelection = SELECTION_SCISSORS;
			}
		});
		
		actions.add(pass).size(250f, 64f).padRight(50f);
		actions.add(shot).size(250f, 64f).padRight(50f);
		actions.add(move).size(250f, 64f).padRight(50f);
		
		actions.setPosition(Gdx.graphics.getWidth()/2, (Gdx.graphics.getHeight()/2)-250);
		addActor(actions);
	}

	private SkeletonData leftSkeletonData;
	private SkeletonRenderer leftRenderer;
	private Animation leftRun, leftRunBall;
	private Skeleton leftSkeleton;
	private AnimationState leftState;
	private void createLeftPlayer() {
		//Loading Player Skeleton and Animation
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/Field/"+(left.isZombie()?"Zombie/Zombie":"Player/Player")+".atlas"));
		SkeletonJson jsonSkeleton = new SkeletonJson(atlas);
		leftSkeletonData = jsonSkeleton.readSkeletonData(Gdx.files.internal("data/Field/"+(left.isZombie()?"Zombie/Zombie":"Player/Player")+".json"));
		
		leftRenderer = new SkeletonRenderer();
		
		leftSkeleton = new Skeleton(leftSkeletonData);
		leftSkeleton.updateWorldTransform();
		leftSkeleton.setX(300);
		leftSkeleton.setY(180);
		
		leftSkeleton.setSkin(left.getTeamString());
		leftSkeletonData.findSlot("Head").setAttachmentName(left.getHeadString());
		leftSkeleton.setToSetupPose();
		
		AnimationStateData stateData = new AnimationStateData(leftSkeletonData); // Defines mixing (crossfading) between animations.
		stateData.setDefaultMix(0.5f);

		leftState = new AnimationState(stateData);
		
		//Set the default animation on idle
		leftState.setAnimation(0, "run", true);
	}
	
	private SkeletonData rightSkeletonData;
	private SkeletonRenderer rightRenderer;
	private Animation rightRun, rightrunBall;
	private Skeleton rightSkeleton;
	private AnimationState rightState;
	private void createRightPlayer() {
		//Loading Player Skeleton and Animation
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/Field/"+(right.isZombie()?"Zombie/Zombie":"Player/Player")+".atlas"));
		SkeletonJson jsonSkeleton = new SkeletonJson(atlas);
		rightSkeletonData = jsonSkeleton.readSkeletonData(Gdx.files.internal("data/Field/"+(right.isZombie()?"Zombie/Zombie":"Player/Player")+".json"));
		
		rightRenderer = new SkeletonRenderer();
		
		rightSkeleton = new Skeleton(rightSkeletonData);
		rightSkeleton.updateWorldTransform();
		rightSkeleton.setX(1000);
		rightSkeleton.setY(180);
		
		rightSkeleton.setFlipX(true);
		rightSkeleton.setSkin(right.getTeamString());
		rightSkeletonData.findSlot("Head").setAttachmentName(right.getHeadString());
		rightSkeleton.setToSetupPose();
		
		AnimationStateData stateData = new AnimationStateData(rightSkeletonData); // Defines mixing (crossfading) between animations.
		stateData.setDefaultMix(0.5f);

		rightState = new AnimationState(stateData);
		
		//Set the default animation on idle
		rightState.setAnimation(0, "run", true);
	}
	
	
	/**
	 * 1 for player
	 * 2 for computer/enemy
	 * 0 for draw
	 */
	private int whoWon() {
		if(playerSelection == enemySelection) return 0;
		
		if(playerSelection == SELECTION_PAPER && enemySelection == SELECTION_ROCK) {
			return 1;
		}else if(playerSelection == SELECTION_PAPER && enemySelection == SELECTION_SCISSORS) {
			return 2;
		}else if(playerSelection == SELECTION_ROCK && enemySelection == SELECTION_PAPER) {
			return 2;
		}else if(playerSelection == SELECTION_ROCK && enemySelection == SELECTION_SCISSORS) {
			return 1;
		}else if(playerSelection == SELECTION_SCISSORS && enemySelection == SELECTION_PAPER) {
			return 1;
		}else if(playerSelection == SELECTION_SCISSORS && enemySelection == SELECTION_ROCK) {
			return 2;
		}else{
			return 0;	
		}
	}
	
	private float animationChooser(final String left, final String right) {		
		int won = whoWon();
		
		if(won == 1) {
			leftState.setAnimation(0, left, false);
			leftState.addListener(new AnimationStateListener() {
				@Override
				public void start(int trackIndex) {
				}
				@Override
				public void event(int trackIndex, Event event) {
					if(event.getString().equals("HitEvent")) {
						rightState.setAnimation(0, right, false);
						rightState.addListener(new AnimationStateListener() {
							
							@Override
							public void start(int trackIndex) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void event(int trackIndex, Event event) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void end(int trackIndex) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void complete(int trackIndex, int loopCount) {
								state = STATE_END;
							}
						});
					}
				}
				@Override
				public void end(int trackIndex) {
				}	
				@Override
				public void complete(int trackIndex, int loopCount) {
				}
			});
			
			float tmpleft = leftState.getCurrent(0).getTime();
			float tmpright = rightState.getCurrent(0).getTime();
		}else if(won == 2) {
			rightState.setAnimation(0, right, false);
			rightState.addListener(new AnimationStateListener() {
				@Override
				public void start(int trackIndex) {
				}
				@Override
				public void event(int trackIndex, Event event) {
					if(event.getString().equals("HitEvent")) {
						leftState.setAnimation(0, left, false);
						leftState.addListener(new AnimationStateListener() {
							
							@Override
							public void start(int trackIndex) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void event(int trackIndex, Event event) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void end(int trackIndex) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void complete(int trackIndex, int loopCount) {
								state = STATE_END;
							}
						});
					}
				}
				@Override
				public void end(int trackIndex) {
				}	
				@Override
				public void complete(int trackIndex, int loopCount) {
				}
			});
			
			float tmpleft = leftState.getCurrent(0).getTime();
			float tmpright = rightState.getCurrent(0).getTime();
		}else{
			rightState.setAnimation(0, right, false);
			leftState.setAnimation(0, left, false);
			leftState.addListener(new AnimationStateListener() {
				
				@Override
				public void start(int trackIndex) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void event(int trackIndex, Event event) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void end(int trackIndex) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void complete(int trackIndex, int loopCount) {
					state = STATE_END;
				}
			});
		}
		
		return 1000;//(tmpleft > tmpright ? tmpleft : tmpright);
	}

	//head torso leg - attack - win lose - ball
	private String stringAnimationBuilder(int selection, boolean win, boolean ball) {
		String result = "";
		switch(selection) {
			default:
			case SELECTION_PAPER:
				result += "head";
				break;
			case SELECTION_ROCK:
				result += "torso";
				break;
			case SELECTION_SCISSORS:
				result += "leg";
				break;
		}
		
		result += "attack";
		
		if(win) result += "win";
		else result += "lose";
		
		if(ball) result += "ball";
		
		return result;
	}
	
	private float fightAnimCounter = 1;
	@Override
	public void act(float delta) {
		super.act(delta);
		timer += delta;
		
		leftState.update(delta);
		leftState.apply(leftSkeleton);
		leftSkeleton.updateWorldTransform(); 
		
		rightState.update(delta);
		rightState.apply(rightSkeleton);
		rightSkeleton.updateWorldTransform(); 
		
		if(state == STATE_FIGHTINIT) {
			if(leftSkeleton.getX() + 150 < rightSkeleton.getX()) {
				leftSkeleton.setX(leftSkeleton.getX() + (delta  * 250));
				rightSkeleton.setX(rightSkeleton.getX() - (delta  * 250));
			}else{
				
				int won = whoWon();
				
				String left = stringAnimationBuilder(playerSelection, won == 1, false);
				String right = stringAnimationBuilder(enemySelection, won == 2, false);
				fightAnimCounter = animationChooser(left, right);
				state = STATE_FIGHTANIM;
			}
		}else if(state == STATE_FIGHTANIM && fightAnimCounter >= 0) {
			fightAnimCounter -= delta;
		}else if(fightAnimCounter <= 0){
			state = STATE_END;
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		//drawing the overlay
		batch.draw(ResourceLoader.OVERLAY_BACKGROUND, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	
		batch.draw(ResourceLoader.MENU_BACKGROUND_SKY, (Gdx.graphics.getWidth()/2)-512, (Gdx.graphics.getHeight()/2)-300, 1024, 600);
	
		batch.draw(ResourceLoader.MENU_GROUND, (Gdx.graphics.getWidth()/2)-512, (Gdx.graphics.getHeight()/2)-300, 1024, 200);
	
		leftRenderer.draw(batch, leftSkeleton);
		rightRenderer.draw(batch, rightSkeleton);
		
		if(state == STATE_CALCINIT) {
			removeActor(actions);
			switch(playerSelection) {
			default:
				case SELECTION_PAPER:
					playerLabel = new Label("Du hast Kopf ausgesucht", ResourceLoader.SKIN);
					break;
				case SELECTION_ROCK:
					playerLabel = new Label("Du hast Torso ausgesucht", ResourceLoader.SKIN);
					break;
				case SELECTION_SCISSORS:
					playerLabel = new Label("Du hast Beine ausgesucht", ResourceLoader.SKIN);
					break;
			}
			playerLabel.setColor(0.25f, 0.75f, 0.25f, 1f);
			playerLabel.setPosition(150, Gdx.graphics.getHeight() - 125);
			playerLabel.setFontScale(0.65f);
			
			state = STATE_CALC;
			timer = 0;
		}else if(state == STATE_CALC) {
			addActor(enemyLabel);
			addActor(playerLabel);
			state = STATE_FIGHTINIT;
		}else if(state == STATE_END) {
			int result = whoWon();
			if (result == 0) {
				left.incrementOffended();
				right.incrementOffended();
			} else if (result == 1) {
				right.incrementOffended();
				if(right.hasBall()) {
					left.getField().getBall().setPositionXY(left.getPositionX(), left.getPositionY());
					left.setBallIdle(true);
					left.getTeam().setPoints(left.getTeam().getPoints()+1);
					right.setBallIdle(false);
				}
			} else if (result == 2) {
				left.incrementOffended();
				if(left.hasBall()) {
					right.getField().getBall().setPositionXY(right.getPositionX(), right.getPositionY());
					right.setBallIdle(true);
					right.getTeam().setPoints(left.getTeam().getPoints()+1);
					left.setBallIdle(false);
				}
			}
			getParent().removeActor(this);
		}
		
		super.draw(batch, parentAlpha);
	}
	
}
