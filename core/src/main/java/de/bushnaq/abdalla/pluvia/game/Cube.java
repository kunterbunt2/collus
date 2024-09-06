package de.bushnaq.abdalla.pluvia.game;

import de.bushnaq.abdalla.pluvia.game.model.stone.Stone;

public class Cube {
    private final int xSize;
    private final int ySize;
    private final int zSize;
    protected Stone[][][] patch                = null;

    public Cube(int xSize, int ySize, int zSize)
    {
        this.xSize=xSize;
        this.ySize=ySize;
        this.zSize=zSize;
        patch = new Stone[xSize][][];
        for (int x = 0; x < xSize; x++) {
            patch[x] = new Stone[ySize][];
            for (int y = 0; y < ySize; y++) {
                patch[x][y] = new Stone[zSize];
            }
        }
    }

    public void clear(int x, int y, int z) {
        patch[x][y][z] = null;
    }

    public Stone get(int x, int y, int z) {
        return patch[x][y][z];
    }

    public void set(int x, int y, int z, Stone stone) {
        patch[x][y][z] = stone;
        if(stone!=null) {
            stone.set(x,y,z);
        }
    }

    public void set(Cube buffer) {
        for (int z = 0; z < zSize; z++) {
            for (int y = 0; y < ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    set(x, y, z, buffer.get(x, y, z));
                }
            }
        }
    }
}
