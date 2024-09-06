package de.bushnaq.abdalla.pluvia.game;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import de.bushnaq.abdalla.pluvia.game.score.Score;
import de.bushnaq.abdalla.pluvia.game.score.ScoreList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestScoreList {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@BeforeEach
	void init(TestInfo testInfo) {
		logger.info(String.format("%s", testInfo.getDisplayName()));
	}

	private void printResult(ScoreList scoreList) {
//		for (String game : scoreList.keySet()) {
		int lastScore = Integer.MAX_VALUE;
		for (Score score : scoreList) {
			assertTrue(lastScore >= score.getScore(), String.format("sorting is wrong score %d is sorted after %d", score.getScore(), lastScore));
			logger.info(String.format("%s %s %d", score.getGame(), score.getUserName(), score.getScore()));
			lastScore = score.getScore();
		}
//		}
	}

//	@Test
//	public void sortOneGame() {
//		ScoreList scoreList = new ScoreList(3);
//		{
//			scoreList.add("game1", 0, 100, 15, 0, "user-1");
//			scoreList.add("game1", 0, 101, 16, 0, "user-2");
//			scoreList.add("game1", 0, 102, 17, 0, "user-3");
//			scoreList.add("game1", 0, 103, 18, 0, "user-4");
//		}
//
//		printResult(scoreList);
//	}
//
//	@Test
//	public void sortTwoGames() {
//		ScoreList scoreList = new ScoreList(3);
//		{
//			scoreList.add("game1", 0, 103, 18, 0, "user-4");
//			scoreList.add("game2", 0, 103, 18, 0, "user-4");
//			scoreList.add("game1", 0, 50, 18, 0, "user-4");
//			scoreList.add("game2", 0, 117, 18, 0, "user-4");
//			scoreList.add("game1", 0, 100, 15, 0, "user-1");
//			scoreList.add("game2", 0, 101, 16, 0, "user-2");
//			scoreList.add("game1", 0, 102, 17, 0, "user-3");
//			scoreList.add("game1", 0, 103, 18, 0, "user-4");
//		}
//
//		printResult(scoreList);
//	}

}
