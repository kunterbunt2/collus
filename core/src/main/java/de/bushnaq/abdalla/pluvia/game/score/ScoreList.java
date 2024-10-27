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

package de.bushnaq.abdalla.pluvia.game.score;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.bushnaq.abdalla.pluvia.desktop.Context;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.game.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.TreeSet;

/**
 * @author kunterbunt
 */
public class ScoreList extends TreeSet<Score> {
    private       long   changed;                                            // last time anything changed
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private       int    size;

    public ScoreList() {

    }

    public ScoreList(int size) {
        this.size = size;
    }

    public boolean add(Level level) {
        if (level.getScore() > 0) {
            return addScore(new Score(level));
        }
        return false;
    }

    private boolean addScore(Score score) {
        boolean newHeiscore = false;
        Score   s           = findScore(score);
        if (s != null) {
            if (s.getScore() < score.getScore()) {
                s.set(score);
                newHeiscore = true;
            }
        } else {
            add(score);
            newHeiscore = true;
        }
        if (newHeiscore) {
            changed();
            writeToDisk();

        }
        return newHeiscore;
    }

    public void changed() {
        changed = System.currentTimeMillis();
    }

    private Score findScore(Score score) {
        for (Score s : this) {
            if (s.getGame().equals(score.getGame()) /*&& (s.getSeed() == score.getSeed())*/)
                return s;
        }
        return null;
    }

    @JsonIgnore
    public long getChanged() {
        return changed;
    }

    @JsonIgnore
    public int getSize() {
        return size;
    }

    public boolean testValidity(GameEngine gameEngine) {
        for (Score s : this) {
            if (!s.testValidity(gameEngine))
                return false;
        }
        return true;
    }

    protected void writeToDisk() {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.writeValue(Gdx.files.external(Context.getConfigFolderName() + "/score.yaml").file(), this);
        } catch (StreamWriteException e) {
            logger.warn(e.getMessage(), e);
        } catch (DatabindException e) {
            logger.warn(e.getMessage(), e);
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

}
