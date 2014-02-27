package de.brainzballs.game.footballfield.player;

import de.brainzballs.game.footballfield.team.Team;

public class Keeper extends Player {
	
	/*************************************************************************************
	 *				variables
	 *************************************************************************************/	
	
	/*************************************************************************************
	 *				Constructor
	 *************************************************************************************/	
	
	protected Keeper(int x, int y, int direction, Team team) {
		super(x, y, Player.PlayerType.KEEPER , direction, team);
		maxHealth = 5;
		currentHealth = maxHealth;
	}
	
	public static Keeper newInstance(int x, int y, int direction, Team team) {
		return new Keeper(x, y, direction, team);
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
		
		skeletonData.findSlot("Head").setAttachmentName("Head4");
		this.headString = "Head4";
		skeleton.setToSetupPose();
	}
}
