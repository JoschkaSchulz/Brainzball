package de.brainzballs.game.footballfield.player;

import de.brainzballs.game.footballfield.player.Player.PlayerType;
import de.brainzballs.game.footballfield.team.Team;

public class Defender extends Player {
	
	/*************************************************************************************
	 *				variables
	 *************************************************************************************/	
	
	/*************************************************************************************
	 *				Constructor
	 *************************************************************************************/	
	
	protected Defender(int x, int y, int direction, Team team) {
		super(x, y, Player.PlayerType.DEFENDER , direction, team);
		maxHealth = 5;
		currentHealth = maxHealth;
	}
	
	public static Defender newInstance(int x, int y, int direction, Team team) {
		return new Defender(x, y, direction, team);
	}
	
	/*************************************************************************************
	 *				getter and setter
	 *************************************************************************************/	
	@Override
	public int getMoveRadius() {
		return 4;
	}
	
	@Override
	public int getPassRadius() {
		return 8;
	}
	
	@Override
	public int getShotRadius() {
		return 4;
	}
	/*************************************************************************************
	 *				methods
	 *************************************************************************************/	
	
	@Override
	protected void loadSpineSkeleton() {
		super.loadSpineSkeleton();
		
		skeletonData.findSlot("Head").setAttachmentName("Head2");
		this.headString = "Head2";
		skeleton.setToSetupPose();
	}
}
