package de.brainzballs.game.footballfield.player;

import de.brainzballs.game.footballfield.team.Team;

public class PlayerFactory {
	/**
	 * a factory method for creating a striker
	 *  
	 * @param x the x position of the player in the field matrix
	 * @param y the y position of the player in the field matrix
	 * @param direction the view direction from the player as int
	 * @param team the team the player is in
	 * @return a instance of an new player
	 */
	public static Striker newStrikerInstance(int x, int y, int direction, Team team) {
		return Striker.newInstance(x, y, direction, team);
	}
	
	/**
	 * a factory method for creating a midfielder
	 *  
	 * @param x the x position of the player in the field matrix
	 * @param y the y position of the player in the field matrix
	 * @param direction the view direction from the player as int
	 * @param team the team the player is in
	 * @return a instance of an new player
	 */
	public static Midfielder newMidfielderInstance(int x, int y, int direction, Team team) {
		return Midfielder.newInstance(x, y, direction, team);
	}
	
	/**
	 * a factory method for creating a keeper
	 *  
	 * @param x the x position of the player in the field matrix
	 * @param y the y position of the player in the field matrix
	 * @param direction the view direction from the player as int
	 * @param team the team the player is in
	 * @return a instance of an new player
	 */
	public static Keeper newKeeperInstance(int x, int y, int direction, Team team) {
		return Keeper.newInstance(x, y, direction, team);
	}
	
	/**
	 * a factory method for creating a defender
	 *  
	 * @param x the x position of the player in the field matrix
	 * @param y the y position of the player in the field matrix
	 * @param direction the view direction from the player as int
	 * @param team the team the player is in
	 * @return a instance of an new player
	 */
	public static Defender newDefenderInstance(int x, int y, int direction, Team team) {
		return Defender.newInstance(x, y, direction, team);
	}
}
