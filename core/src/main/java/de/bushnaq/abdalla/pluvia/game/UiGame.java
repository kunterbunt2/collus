/*
 * Created on Jul 25, 2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.game;

import de.bushnaq.abdalla.pluvia.game.model.stone.Stone;

/**
 *
 * 9 rows of 4 columns, 6 different stones, with up to 3 stones that can fall at the same time
 *
 * @author kunterbunt
 *
 */
public class UiGame extends Game {
	public UiGame() {
		super(GameName.UI.name(), 0, 0,0, 0, 0, 7.2f+5, false);
	}
	protected boolean queryTilt(Stone patch[][][]) {
		return false;
	}

}