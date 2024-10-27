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

import de.bushnaq.abdalla.engine.GameObject;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;

public class StonePlane {
    protected     GameObject<GameEngine>[][] patch = null;
    private final int                        xSize;
    private final int                        ySize;

    public StonePlane(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        patch      = new GameObject[xSize][];
        for (int x = 0; x < xSize; x++) {
            patch[x] = new GameObject[ySize];
        }
    }

    public void clear(int x, int y) {
        patch[x][y] = null;
    }

    public GameObject<GameEngine> get(int x, int y) {
        return patch[x][y];
    }

    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }

    public void set(int x, int y, GameObject<GameEngine> stone) {
        patch[x][y] = stone;
//        if (stone != null) {
//            stone.set(x, y, z);
//        }
    }

//    public void set(StonePlane buffer) {
//            for (int y = 0; y < ySize; y++) {
//                for (int x = 0; x < xSize; x++) {
//                    set(x, y, z, buffer.get(x, y, z));
//                }
//            }
//    }
}
