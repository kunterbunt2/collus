package de.bushnaq.abdalla.pluvia.game;

/**
 * @author kunterbunt
 */
public class GameDataObject {
    private String                name;
    private StoneDataObject[][][] patch        = null;
    private int                   randCalls    = 0;
    private long                  relativeTime = 0;
    private int                   score        = 0;
    private int                   seed         = 0;
    private int                   steps        = 0;
    private long                  time         = 0;
    private String                userName;

    public GameDataObject() {

    }

    public GameDataObject(Level level) {
        this.score        = level.game.score;
        this.steps        = level.game.steps;
        this.seed         = level.rand.getSeed();
        this.randCalls    = level.rand.getCalls();
        this.relativeTime = level.game.relativeTime;
        this.name         = level.game.getName();
        this.time         = System.currentTimeMillis();
        this.userName     = System.getProperty("user.name");
        patch             = new StoneDataObject[level.game.xSize][][];
        for (int x = 0; x < patch.length; x++) {
            patch[x] = new StoneDataObject[level.game.ySize][];
        }
        for (int y = level.game.ySize - 1; y >= 0; y--) {
            for (int x = 0; x < level.game.xSize; x++) {
//                if (level.patch[x][y][z] != null)
                {
                    patch[x][y] = new StoneDataObject[level.game.zSize];
                }
            }
        }
//        for (int z = 0; z < level.game.zSize; z++) {
//            for (int y = level.game.ySize - 1; y >= 0; y--) {
//                for (int x = 0; x < level.game.xSize; x++) {
//                    if (level.patch[x][y] != null) {
//                        patch[x][y][z] = new StoneDataObject(level.patch[x][y][z].type, level.patch[x][y][z].score);
//                    }
//                }
//            }
//        }
    }

    public String getName() {
        return name;
    }

    public StoneDataObject[][][] getPatch() {
        return patch;
    }

    public int getRandCalls() {
        return randCalls;
    }

    public long getRelativeTime() {
        return relativeTime;
    }

    public int getScore() {
        return score;
    }

    public int getSeed() {
        return seed;
    }

    public int getSteps() {
        return steps;
    }

    public long getTime() {
        return time;
    }

    public String getUserName() {
        return userName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPatch(StoneDataObject[][][] patch) {
        this.patch = patch;
    }

    public void setRandCalls(int randCalls) {
        this.randCalls = randCalls;
    }

    public void setRelativeTime(long relativeTime) {
        this.relativeTime = relativeTime;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
