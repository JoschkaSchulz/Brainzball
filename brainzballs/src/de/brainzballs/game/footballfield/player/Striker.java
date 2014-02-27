package de.brainzballs.game.footballfield.player;

import de.brainzballs.game.footballfield.player.Player.PlayerType;
import de.brainzballs.game.footballfield.team.Team;

public class Striker extends Player {
	
	/*************************************************************************************
	 *				variables
	 *************************************************************************************/	
	
	/*************************************************************************************
	 *				Constructor
	 *************************************************************************************/	
	
	protected Striker(int x, int y, int direction, Team team) {
		super(x, y, Player.PlayerType.STRIKER , direction, team);
		maxHealth = 5;
		currentHealth = maxHealth;
	}
	
	public static Striker newInstance(int x, int y, int direction, Team team) {
		return new Striker(x, y, direction, team);
	}
	
	/*************************************************************************************
	 *				getter and setter
	 *************************************************************************************/	
	@Override
	public int getMoveRadius() {
		return 8;
	}
	
	@Override
	public int getPassRadius() {
		return 4;
	}
	
	@Override
	public int getShotRadius() {
		return 8;
	}
	/*************************************************************************************
	 *				methods
	 *************************************************************************************/	
	
	@Override
	protected void loadSpineSkeleton() {
		super.loadSpineSkeleton();
		
		skeletonData.findSlot("Head").setAttachmentName("Head");
		this.headString = "Head";
		skeleton.setToSetupPose();
	}
}
