package de.bushnaq.abdalla.pluvia.game;

/**
 * @author kunterbunt
 *
 */
public class StoneDataObject {

	public int	score	= 0;

	public int	type	= 0;

	public StoneDataObject() {

	}

	public StoneDataObject(int type, int score) {
		this.type = type;
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public int getType() {
		return type;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setType(int type) {
		this.type = type;
	}

}
