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
	
	@Override
	public int getMaxHealth() {
		return 2;
	}

	@Override
	public String getHeadString() {
		return "Head3";
	}
}
