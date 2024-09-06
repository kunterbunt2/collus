/*
 * Created on Jul 25, 2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.game;

/**
 *
 * 9 rows of 4 columns, 6 different stones, with up to 3 stones that can fall at the same time
 *
 * @author kunterbunt
 *
 */
public class LegacyGame extends Game {
	public LegacyGame() {
		super("SMALL", 7, 7, 7, 14, 0,  7.2f+1, false);
		description = "9 rows of 4 columns\r\n"//
				+ "6 different stones\r\n"//
				+ "with up to 3 stones that can fall at the same time\r\n";
	}

}