package de.brainzballs.game.footballfield.player;

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
