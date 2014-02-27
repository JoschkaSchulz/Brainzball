package de.brainzballs.game.footballfield.player;

import de.brainzballs.game.footballfield.player.Player.PlayerType;
import de.brainzballs.game.footballfield.team.Team;

public class Midfielder extends Player {
	
	/*************************************************************************************
	 *				variables
	 *************************************************************************************/	
	
	/*************************************************************************************
	 *				Constructor
	 *************************************************************************************/	
	
	protected Midfielder(int x, int y, int direction, Team team) {
		super(x, y, Player.PlayerType.MIDFIELDER , direction, team);
		maxHealth = 5;
		currentHealth = maxHealth;
	}
	
	public static Midfielder newInstance(int x, int y, int direction, Team team) {
		return new Midfielder(x, y, direction, team);
	}
	
	/*************************************************************************************
	 *				getter and setter
	 *************************************************************************************/	
	@Override
	public int getMoveRadius() {
		return 6;
	}
	
	@Override
	public int getPassRadius() {
		return 6;
	}
	
	@Override
	public int getShotRadius() {
		return 6;
	}
	/*************************************************************************************
	 *				methods
	 *************************************************************************************/	
	
	@Override
	protected void loadSpineSkeleton() {
		super.loadSpineSkeleton();
		
		skeletonData.findSlot("Head").setAttachmentName("Head3");
		this.headString = "Head3";
		skeleton.setToSetupPose();
	}
}
