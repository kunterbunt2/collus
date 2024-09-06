/*
 * Created on 18.07.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.game;

import de.bushnaq.abdalla.pluvia.game.model.stone.Stone;

/**
 * @author kunterbunt
 */
public class Game implements Cloneable {
    public    float  cameraZPosition;
    protected String description;
    protected String name  = null;            // ---Game name
    protected int    xSize = 0;            // ---Size of board
    protected int    ySize = 0;            // ---Size of board

    public int getzSize() {
        return zSize;
    }

    public void setzSize(int zSize) {
        this.zSize = zSize;
    }

    protected int     zSize        = 0;
    protected int     nrOfStones   = 0;            // ---Max count of different stone types (colors)
    protected int     preview      = 0;            // ---Rows of visible stones that will drop in the next moves
    protected long    relativeTime = 0;
    protected boolean reset        = false;
    protected int     score        = 0;
    protected int     steps        = 0;
    private   long    timer;
    protected String  userName     = "Test User";

    public Game(String aName, int x, int y, int z, int aNrOfStones, int aPreview, float cameraZPosition, boolean aReset) {
        this.name            = aName;
        this.xSize           = x;
        this.ySize           = y;
        this.zSize           = z;
        this.nrOfStones      = aNrOfStones;
        this.preview         = aPreview;
        this.reset           = aReset;
        this.cameraZPosition = cameraZPosition;
    }

    public void addStoneScore() {
        score += getStoneScore();
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }

    public int getNrOfStones() {
        return nrOfStones;
    }

    public int getPreview() {
        return preview;
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

    protected int getStoneScore() {
        return 1;
    }

    public String getUserName() {
        return userName;
    }

    /**
     * @return Returns the reset.
     */
    public boolean isReset() {
        return reset;
    }

    protected int queryHeapHeight(Stone patch[][][]) {
        for (int z = 0; z < this.zSize; z++)
            for (int y = preview; y < this.ySize; y++)
                for (int x = 0; x < this.xSize; x++)
                    if (patch[x][y][z] != null) return this.ySize - y;
        return 0;
    }

    protected boolean queryTilt(Cube cube) {
        for (int z = 0; z < this.zSize; z++) {
            for (int y = 0; y < this.ySize; y++) {
                for (int x = 0; x < this.xSize; x++) {
                    if (cube.get(x,y,z) != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    protected boolean queryWin() {
        return false;
    }

    protected void reset() {
        score        = -xSize * ySize * getStoneScore();
        steps        = 0;
        relativeTime = 0;
    }

    public void setName(String aName) {
        name = aName;
    }

    public void setxSize(int aNrOfColumns) {
        xSize = aNrOfColumns;
    }

    public void setySize(int aNrOfRows) {
        ySize = aNrOfRows;
    }

    public void setNrOfStones(int aNrOfStones) {
        nrOfStones = aNrOfStones;
    }

    public void setPreview(int aPreview) {
        preview = aPreview;
    }

    /**
     * @param aReset The reset to set.
     */
    public void setReset(boolean aReset) {
        reset = aReset;
    }

    public void setScore(int aScore) {
        score = aScore;
    }

    public void setUserName(String aUserName) {
        userName = aUserName;
    }

    public void startTimer() {
        timer = System.currentTimeMillis();
    }

    public void updateTimer() {
        relativeTime += System.currentTimeMillis() - timer;
        timer = 0;
    }

}