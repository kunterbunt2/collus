package de.bushnaq.abdalla.pluvia.game.recording;

import java.util.ArrayList;
import java.util.List;

import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.game.Game;
import de.bushnaq.abdalla.pluvia.game.GameDataObject;
import de.bushnaq.abdalla.pluvia.game.GamePhase;
import de.bushnaq.abdalla.pluvia.game.LevelManager;
import de.bushnaq.abdalla.pluvia.game.model.stone.Stone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kunterbunt
 *
 */
public class Recording {
	private static final String	NOT_EXPECTED_SCORE											= "Recording playback does not result in expected score. Recording is not genuine.";
	private static final String	RECORDING_IS_TRYING_TO_ACCESS_A_STONE_THAT_DOES_NOT_EXIST	= "Recording is trying to access a stone that does not exist. Recording is not genuine.";
	private static final String	RECORDING_LENGTH_DOES_NOT_MATCH_NUMBER_OF_STEPS				= "Recording length does not match number of steps. Recording is not genuine.";
	private static final String	RECORDING_TOO_LONG											= "Recording not fully played back, but game is already exited. Recording is not genuine.";
	private static final String	STONE_CANNOT_MOVE_LEFT										= "Recording is trying to move a stone to the left, but it cannot move. Recording is not genuine.";
	private static final String	STONE_CANNOT_MOVE_TO_THE_RIGHT								= "Recording is trying to move a stone to the right, but it cannot move. Recording is not genuine.";
	private GameDataObject gdo       = new GameDataObject();
	private Logger         logger    = LoggerFactory.getLogger(this.getClass());
	private List<Frame>    recording = new ArrayList<>();

	public Recording() {

	}

	public void addFrame(int x, int y, int z, int step, Interaction interaction) {
		recording.add(new Frame(x, y, z, step, interaction));
	}

	public void addFrame(int step, Interaction interaction) {
		recording.add(new Frame(step, interaction));
	}

	public GameDataObject getGdo() {
		return gdo;
	}

	public List<Frame> getRecording() {
		return recording;
	}

	public void setGdo(GameDataObject gdo) {
		this.gdo = gdo;
	}

	public void setRecording(List<Frame> recording) {
		this.recording = recording;
	}

	public boolean testValidity(String title, String reaction, GameEngine gameEngine, Game game) {
		return true;
//		LevelManager levelManager = new LevelManager(null, game);
//		levelManager.setGameSeed(gdo.getSeed());
//		levelManager.createLevel();
//		if (((gdo.getSteps() == 0) && (gdo.getSteps() != recording.size())) || ((gdo.getSteps() != 0) && (gdo.getSteps() != recording.size() + 1))) {
//			gameEngine.getMessageDialog().showModal(title, RECORDING_LENGTH_DOES_NOT_MATCH_NUMBER_OF_STEPS + reaction);
//			logger.error(RECORDING_LENGTH_DOES_NOT_MATCH_NUMBER_OF_STEPS + reaction);
//			return false;
//		}
//
//		for (Frame frame : recording) {
//			do {
//				if (levelManager.update()) {
//					gameEngine.getMessageDialog().showModal(title, RECORDING_TOO_LONG + reaction);
//					logger.error(RECORDING_TOO_LONG + reaction);
//					return false;
//				}
//			} while (levelManager.gamePhase != GamePhase.waiting || (levelManager.animationPhase != 0));
//			switch (frame.getInteraction()) {
//			case left: {
//				Stone stone = levelManager.getStone(frame.getX(), frame.getY(), frame.getZ());
//				if (stone != null) {
//					if (!levelManager.reactLeft(stone)) {
//						gameEngine.getMessageDialog().showModal(title, STONE_CANNOT_MOVE_LEFT + reaction);
//						logger.error(STONE_CANNOT_MOVE_LEFT + reaction);
//						return false;
//					}
//					levelManager.animationPhase = 0;
//				} else {
//					gameEngine.getMessageDialog().showModal(title, RECORDING_IS_TRYING_TO_ACCESS_A_STONE_THAT_DOES_NOT_EXIST + reaction);
//					logger.error(RECORDING_IS_TRYING_TO_ACCESS_A_STONE_THAT_DOES_NOT_EXIST + reaction);
//					return false;
//				}
//			}
//				break;
//			case right: {
//				Stone stone = levelManager.getStone(frame.getX(), frame.getY(), frame.getZ());
//				if (stone != null) {
//					if (!levelManager.reactRight(stone)) {
//						gameEngine.getMessageDialog().showModal(title, STONE_CANNOT_MOVE_TO_THE_RIGHT + reaction);
//						logger.error(STONE_CANNOT_MOVE_TO_THE_RIGHT + reaction);
//						return false;
//					}
//					levelManager.animationPhase = 0;
//				} else {
//					gameEngine.getMessageDialog().showModal(title, RECORDING_IS_TRYING_TO_ACCESS_A_STONE_THAT_DOES_NOT_EXIST + reaction);
//					logger.error(RECORDING_IS_TRYING_TO_ACCESS_A_STONE_THAT_DOES_NOT_EXIST + reaction);
//					return false;
//				}
//			}
//				break;
//			case next: {
//				levelManager.nextRound();
//				levelManager.animationPhase = 0;
//			}
//				break;
//			}
//		}
//		do {
//			levelManager.update();
//		} while (levelManager.gamePhase != GamePhase.waiting || (levelManager.animationPhase != 0));
//		if (gdo.getScore() != levelManager.getScore()) {
//			gameEngine.getMessageDialog().showModal(title, NOT_EXPECTED_SCORE + reaction);
//			logger.error(NOT_EXPECTED_SCORE + reaction);
//			return false;
//		}
//		levelManager.destroy();
//		return true;
	}

}
