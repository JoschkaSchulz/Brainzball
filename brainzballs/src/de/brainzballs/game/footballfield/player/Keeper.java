package de.brainzballs.game.footballfield.player;

import de.brainzballs.game.footballfield.player.Player.PlayerType;
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
	}
	
	public static Keeper newInstance(int x, int y, int direction, Team team) {
		return new Keeper(x, y, direction, team);
	}
	
	/*************************************************************************************
	 *				getter and setter
	 *************************************************************************************/	
	@Override
	public int getMoveRadius() {
		return 2;
	}
	
	@Override
	public int getPassRadius() {
		return 10;
	}
	
	@Override
	public int getShotRadius() {
		return 10;
	}
	
	@Override
	public int getMaxHealth() {
		return 5;
	}

	@Override
	public String getHeadString() {
		return "Head4";
	}
}
