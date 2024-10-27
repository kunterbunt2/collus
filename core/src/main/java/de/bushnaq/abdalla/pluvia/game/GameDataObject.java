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

package de.bushnaq.abdalla.pluvia.game;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kunterbunt
 */
public class GameDataObject {
    private String                name;
    private long                  relativeTime = 0;
    private int                   score        = 0;
    private int                   steps        = 0;
    private List<StoneDataObject> stones       = new ArrayList<>();
    private long                  time         = 0;
    private int                   xSize;
    private int                   ySize;
    private int                   zSize;

    public GameDataObject() {

    }

    public GameDataObject(Level level) {
        this.steps        = level.game.steps;
        this.relativeTime = level.game.relativeTime;
        this.name         = level.getName();
        this.time         = System.currentTimeMillis();
        this.xSize        = level.cube.xSize;
        this.ySize        = level.cube.ySize;
        this.zSize        = level.cube.zSize;
        for (int z = 0; z < level.game.zSize; z++) {
            for (int y = level.game.ySize - 1; y >= 0; y--) {
                for (int x = 0; x < level.game.xSize; x++) {
                    if (level.cube.isOccupied(x, y, z)) {
                        stones.add(new StoneDataObject(level.cube.get(x, y, z).type, x, y, z));
                    }
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public long getRelativeTime() {
        return relativeTime;
    }

    public int getScore() {
        return score;
    }

    public int getSteps() {
        return steps;
    }

    public List<StoneDataObject> getStones() {
        return stones;
    }

    public long getTime() {
        return time;
    }

    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }

    public int getzSize() {
        return zSize;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRelativeTime(long relativeTime) {
        this.relativeTime = relativeTime;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setStones(List<StoneDataObject> stones) {
        this.stones = stones;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setxSize(int xSize) {
        this.xSize = xSize;
    }

    public void setySize(int ySize) {
        this.ySize = ySize;
    }

    public void setzSize(int zSize) {
        this.zSize = zSize;
    }

}
