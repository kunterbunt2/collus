/*
 * Copyright (C) 2024 Abdalla Bushnaq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Created on 18.07.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.game.score;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.game.Game;
import de.bushnaq.abdalla.pluvia.game.GameDataObject;
import de.bushnaq.abdalla.pluvia.game.Level;
import de.bushnaq.abdalla.pluvia.game.recording.Recording;

/**
 * @author kunterbunt
 */
public class Score implements Comparable<Score> {
    private static final String    INVALID_SCORE_DETECTED = "Invalid Score Detected";
    private static final String    RESETTING_HIGH_SCORE   = "\n\nResetting high score...";
    private              Recording recording;

    public Score() {
    }

    public Score(Level level) {
        recording = level.getRecording();
        recording.setGdo(new GameDataObject(level));
    }

    public int compare(int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    @Override
    public int compareTo(Score another) {
        if (!another.getGame().equals(this.getGame())) {
            return this.getGame().compareTo(another.getGame());
        } else {
//            if (another.getSeed() != this.getSeed())
//                return compare(another.getSeed(), this.getSeed());
//            else
            return compare(another.getScore(), this.getScore());
        }
    }

    @JsonIgnore
    public String getGame() {
        return recording.getGdo().getName();
    }

    public Recording getRecording() {
        return recording;
    }

    @JsonIgnore
    public long getRelativeTime() {
        return recording.getGdo().getRelativeTime();
    }

    @JsonIgnore
    public Integer getScore() {
        return recording.getGdo().getScore();
    }

//	@JsonIgnore
//	public int getSeed() {
//		return recording.getGdo().getSeed();
//	}

    @JsonIgnore
    public Integer getSteps() {
        return recording.getGdo().getSteps();
    }

    @JsonIgnore
    public long getTime() {
        return recording.getGdo().getTime();
    }

//	@JsonIgnore
//	public String getUserName() {
//		return recording.getGdo().getUserName();
//	}

    public void set(Score score) {
        this.recording = score.recording;
    }

    public void setRecording(Recording recording) {
        this.recording = recording;
    }

    public boolean testValidity(GameEngine gameEngine) {
        Game game = (Game) gameEngine.context.gameList.get(gameEngine.context.getGameIndex(getGame())).clone();
        return recording.testValidity(INVALID_SCORE_DETECTED, RESETTING_HIGH_SCORE, gameEngine, game);
    }

}