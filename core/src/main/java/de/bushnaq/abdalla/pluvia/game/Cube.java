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

import de.bushnaq.abdalla.pluvia.game.model.stone.Stone;

public class Cube {
    protected Stone[][][] patch = null;
    private   int         xSize;
    private   int         ySize;
    private   int         zSize;

    public Cube(int xSize, int ySize, int zSize) {
        createPatch(xSize, ySize, zSize);
    }

    public boolean canVanish(int x, int y, int z) {
        Stone stone = get(x, y, z);
        if (stone != null)
            return stone.canVanish();
        return false;
    }

    public void clear(int x, int y, int z) {
        patch[x][y][z] = null;
    }

    private void createPatch(int xSize, int ySize, int zSize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
        patch      = new Stone[xSize][][];
        for (int x = 0; x < xSize; x++) {
            patch[x] = new Stone[ySize][];
            for (int y = 0; y < ySize; y++) {
                patch[x][y] = new Stone[zSize];
            }
        }
    }

    public Stone get(int x, int y, int z) {
        if (x < 0 || x >= xSize)
            return null;
        if (y < 0 || y >= ySize)
            return null;
        if (z < 0 || z >= zSize)
            return null;
        return patch[x][y][z];
    }

    public StoneOption getDropOptions(int x, int y, int z) {
        Stone stone = get(x, y, z);
        if (stone == null)
            return StoneOption.empty;
        else if (stone.isCannotDrop())
            return StoneOption.cannotDrop;
        else if (stone.isCanDrop())
            return StoneOption.canDrop;
        return StoneOption.undefined;
    }

    public boolean isOccupied(int x, int y, int z) {
        return get(x, y, z) != null;
    }

    public void resize(int newXSize, int newYSize, int newZSize) {
        Cube buffer = new Cube(newXSize, newYSize, newZSize);
        for (int z = 0; z < newZSize; z++) {
            for (int y = 0; y < newYSize; y++) {
                for (int x = 0; x < newXSize; x++) {
                    Stone stone = get(x, y, z);
                    buffer.set(x, y, z, stone);
                }
            }
        }
        set(buffer);
    }

    public void rotateCubeMinusX() {
        Cube buffer = new Cube(xSize, ySize, zSize);
        for (int z = 0; z < zSize; z++) {
            for (int y = 0; y < this.ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    Stone stone = get(x, y, z);
                    buffer.set(x, z, ySize - 1 - y, stone);
                }
            }
        }
        set(buffer);
    }

    public void rotateCubeMinusY() {
        Cube buffer = new Cube(xSize, ySize, zSize);
        for (int z = 0; z < zSize; z++) {
            for (int y = 0; y < ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    Stone stone = get(x, y, z);
                    buffer.set(xSize - 1 - z, y, x, stone);
                }
            }
        }
        set(buffer);
    }

    public void rotateCubeMinusZ() {
        Cube buffer = new Cube(xSize, ySize, zSize);
        for (int z = 0; z < zSize; z++) {
            for (int y = 0; y < ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    Stone stone = get(x, y, z);
                    buffer.set(y, ySize - 1 - x, z, stone);
                }
            }
        }
        set(buffer);
    }

    public void rotateCubePlusX() {
        Cube buffer = new Cube(xSize, ySize, zSize);
        for (int z = 0; z < zSize; z++) {
            for (int y = 0; y < ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    Stone stone = get(x, y, z);
                    buffer.set(x, zSize - 1 - z, y, stone);
                }
            }
        }
        set(buffer);
    }

    public void rotateCubePlusY() {
        Cube buffer = new Cube(xSize, ySize, zSize);
        for (int z = 0; z < zSize; z++) {
            for (int y = 0; y < ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    Stone stone = get(x, y, z);
                    buffer.set(z, y, zSize - 1 - x, stone);
                }
            }
        }
        set(buffer);
    }

    public void rotateCubePlusZ() {
        Cube buffer = new Cube(xSize, ySize, zSize);
        for (int z = 0; z < zSize; z++) {
            for (int y = 0; y < ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    Stone stone = get(x, y, z);
                    buffer.set(zSize - 1 - y, x, z, stone);
                }
            }
        }
        set(buffer);
    }

    public void set(int x, int y, int z, Stone stone) {
        patch[x][y][z] = stone;
        if (stone != null) {
            stone.set(x, y, z);
        }
    }

    public void set(Cube buffer) {
        if (buffer.xSize != xSize || buffer.ySize != ySize || buffer.zSize != zSize) {
            createPatch(buffer.xSize, buffer.ySize, buffer.zSize);
        }
        for (int z = 0; z < zSize; z++) {
            for (int y = 0; y < ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    set(x, y, z, buffer.get(x, y, z));
                }
            }
        }
    }
}
