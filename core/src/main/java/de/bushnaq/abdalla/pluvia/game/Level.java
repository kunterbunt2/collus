/*
 * Created on 10.07.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.game;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.bushnaq.abdalla.pluvia.desktop.Context;
import de.bushnaq.abdalla.pluvia.engine.AudioManager;
import de.bushnaq.abdalla.pluvia.game.model.stone.Stone;
import de.bushnaq.abdalla.pluvia.game.recording.Interaction;
import de.bushnaq.abdalla.pluvia.game.recording.Recording;
import de.bushnaq.abdalla.pluvia.util.PersistentRandomGenerator;
import de.bushnaq.abdalla.pluvia.util.RcBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author kunterbunt
 */
public abstract class Level {
    public        int        animationPhase       = 0;
    private final Cube       cube;
    protected     Set<Stone> droppingStones       = new HashSet<>();
    protected     Set<Stone> droppingStonesBuffer = new HashSet<>();
    protected     Game       game                 = null;
    public        GamePhase  gamePhase            = GamePhase.waiting;
    protected     Logger     logger               = LoggerFactory.getLogger(this.getClass());
    public        int        maxAnimationPhase    = 12;
    public        int        xSize                = 0;                                        // number of columns
    public        int        ySize                = 0;                                        // number of rows in the level
    protected     int        nrOfStones           = 0;                                        // Number of different patches (colors) in the game
    protected     int        NrOfTotalStones      = 0;                                        // The sum of all follen patches within this game
    protected     int        preview              = 0;                                        // Number of rows that the user can preview before they actually drop into the game
    private       Set<Stone> pushingLeftStones    = new HashSet<>();
    private       Set<Stone> pushingRightStones   = new HashSet<>();
    PersistentRandomGenerator rand;
    private      Recording recording;
    private      boolean   tilt        = false;                                    // mark that game has finished
    private      boolean   userReacted = false;                                    // user has either moved a stones left or right and we need to generate new stones.
    public final int       zSize;

    public Level(Game game) {
        this.xSize      = game.xSize;
        this.ySize      = game.ySize;
        this.zSize      = game.zSize;
        this.preview    = game.preview;
        this.nrOfStones = game.nrOfStones;
        this.game       = game;
        cube            = new Cube(xSize, ySize, zSize);
        rand            = new PersistentRandomGenerator();
        game.reset();
        game.setReset(false);
        recording = new Recording();
    }

    protected void clear() {
        for (int z = 0; z < this.zSize; z++) {
            for (int y = this.ySize - 1; y >= 0; y--) {
                for (int x = 0; x < this.xSize; x++) {
                    Stone stone = cube.get(x, y, z);
                    if (stone != null) {
                        removeStone(stone);
                        cube.clear(x, y, z);
                    } else {
                    }
                }
            }
        }
    }

    protected boolean clearCommandAttributes() {
        boolean somethingHasChanged = false;
        for (int z = 0; z < this.zSize; z++) {
            for (int y = this.ySize - 1; y >= 0; y--) {
                for (int x = 0; x < this.xSize; x++) {
                    if (cube.get(x, y, z) != null) {
                        if (cube.get(x, y, z).clearCommandAttributes()) somethingHasChanged = true;
                    } else {
                    }
                }
            }
        }
        return somethingHasChanged;
    }

    protected boolean clearPuchingAttributes() {
        boolean somethingHasChanged = false;
        for (int z = 0; z < this.zSize; z++) {
            for (int y = this.ySize - 1; y >= 0; y--) {
                for (int x = 0; x < this.xSize; x++) {
                    if (cube.get(x, y, z) != null) {
                        if (cube.get(x, y, z).clearPushingAttributes()) somethingHasChanged = true;
                    } else {
                    }
                }
            }
        }
        return somethingHasChanged;
    }

    protected void clearTemporaryAttributes() {
        for (int z = 0; z < this.zSize; z++) {
            for (int y = this.ySize - 1; y >= 0; y--) {
                for (int x = 0; x < this.xSize; x++) {
                    if (cube.get(x, y, z) != null) {
                        cube.get(x, y, z).clearTemporaryAttributes();
                    } else {
                    }
                }
            }
        }
    }

    public void createLevel() {
        // if this is a level that already (resumed game) has stones, we do not fill it, and we do not generate stones right at the start
        if (queryHeapHeight() == 0) {
//            fillLevel();
            generateStones();
        }
        createLevelBackground();
    }

    abstract void createLevelBackground();

    protected abstract Stone createStone(int x, int y, int z, int type);

    protected Stone createStoneAndUpdateScore(int x, int y, int z, int type) {
        game.addStoneScore();
        return createStone(x, y, z, type);
    }

    public void deleteFile() {
        deleteFile(String.format(Context.getConfigFolderName() + "/%s.yaml", game.name));
    }

    private void deleteFile(String fileName) {
        Gdx.files.external(fileName).delete();
    }

    public abstract void disposeLevel();

    protected void dropStones() {
        for (int z = 0; z < this.zSize; z++) {
            for (int y = this.ySize - 2; y >= preview; y--) {
                for (int x = 0; x < this.xSize; x++) {
                    if ((cube.get(x, y, z) != null) && cube.get(x, y, z).isCanDrop() /* && !Patch[x][y].isVanishing() */) {
                        cube.get(x, y, z).y++;
                        cube.set(x, y + 1, z, cube.get(x, y, z));
                        cube.get(x, y + 1, z).setDropping(true);
                        cube.clear(x, y, z);
                    }
                }
            }
        }
    }

    public void fillLevel() {
//        logger.info(String.format("fillLevel %d %d %d", xSize, ySize, zSize));
//        for (int z = 0; z < zSize; z++) {
//            for (int y = 0; y < ySize; y++) {
//                for (int x = 0; x < xSize; x++) {
//                    // GenerateStones();
//                    if (rand.nextInt(100) < 25) {
//                        cube.set(x, y, z, createStoneAndUpdateScore(x, y, z, rand.nextInt(nrOfStones)));
//                        NrOfTotalStones++;
//                    }
//                }
//            }
//        }

        createDemoLevel();
        boolean Changed = false;
    }

    private void createDemoLevel() {
        if (zSize > 0) {
            for (int z = 0; z < zSize; z++) {
                for (int x = 0; x < xSize; x++) {
                    createDemoStone(x, 0, z, 0);
                }
            }
        }
    }

    private void createDemoStone(int x, int y, int z, int type) {
        cube.set(x, y, z, createStoneAndUpdateScore(x, y, z, type));
    }

    public void generateStones() {
        for (int z = 0; z < this.zSize; z++) {
            for (int y = preview; y > 0; y--) {
                for (int x = 0; x < this.xSize; x++) {
                    if (cube.get(x, y - 1, z) != null) {
                        cube.set(x, y, z, cube.get(x, y - 1, z));
                        cube.clear(x, y - 1, z);
                    }
                }
            }
        }
//        for (int i = 0; i < nrOfFallingStones; i++) {
//            int location = rand.nextInt(nrOfColumns);
//            if (patch[location][0] == null) {
//                patch[location][0] = createStoneAndUpdateScore(location, 0, rand.nextInt(nrOfStones));
//                NrOfTotalStones++;
//            }
//        }
        game.steps++;
    }

//	protected String getLastGameName() {
//		return "save/" + game.getUserName() + "/" + game.getName() + ".xml";
//	}

//	public int getScore() {
//		if (queryHeapHeight() == 0)
//			return game.getScore();
//		return -1;
//	}

    public String getName() {
        return game.name;
    }

    public Recording getRecording() {
        return recording;
    }

    public int getScore() {
        return game.getScore();
    }

    public int getSeed() {
        return rand.getSeed();
    }

    public int getSteps() {
        return game.steps;
    }

    public Stone getStone(int x, int y, int z) {
        return cube.get(x, y, z);
    }

    protected abstract boolean isEnableTime();

    public boolean isTilt() {
        return gamePhase.equals(GamePhase.tilt);
    }

    protected boolean isUserReacted() {
        return userReacted;
    }

    public void markMoveLeftOption() {
        // ---Stones of same type on top of each other should vanish
//		clearTemporaryAttributes();
        RcBoolean changedOnce = new RcBoolean(false);
        do {
            changedOnce.setFalse();
            for (int z = 0; z < this.zSize; z++) {
                for (int y = this.ySize - 1; y >= preview; y--) {
                    for (int x = 0; x < this.xSize; x++) {
                        if (cube.get(x, y, z) != null) {
                            markStoneMoveLeftOption(changedOnce, x, y, z);
                        }
                    }
                }
            }
        } while (changedOnce.getBooleanValue());
    }

    public void markMoveRightOption() {
        // ---Stones of same type on top of each other should vanish
//		clearTemporaryAttributes();
        RcBoolean changedOnce = new RcBoolean(false);
        do {
            changedOnce.setFalse();
            for (int z = 0; z < this.zSize; z++) {
                for (int y = this.ySize - 1; y >= preview; y--) {
                    for (int x = 0; x < this.xSize; x++) {
                        if (cube.get(x, y, z) != null) {
                            markStoneMoveRightOption(changedOnce, x, y, z);
                        }
                    }
                }
            }
        } while (changedOnce.getBooleanValue());
    }

    protected void markStickyPatches() {
        for (int z = 0; z < this.zSize; z++) {
            for (int y = preview; y < this.ySize; y++) {
                for (int x = 0; x < this.xSize; x++) {

                    boolean stickyLeft  = false;
                    boolean stickyRight = false;
                    // int attribute =
                    // (RcPatch.ATTRIBUTE_DROPPING|RcPatch.ATTRIBUTE_MOVINGLEFT|RcPatch.ATTRIBUTE_MOVINGRIGHT)
                    // ;
                    // ---CHECK FOR STICKY STONES
                    Stone stone = cube.get(x, y, z);
                    if (stone != null) {
                        if ((x < this.xSize - 1) && (cube.get(x + 1, y, z) != null) && (stone.getType() == cube.get(x + 1, y, z).getType()))
//						if ((Patch[x][y].isDropping() == Patch[x + 1][y].isDropping())
//								&& (Patch[x][y].isMovingRight() == Patch[x + 1][y].isMovingRight())
//								&& (Patch[x][y].isMovingLeft() == Patch[x + 1][y].isMovingLeft()))
                            stickyRight = true;
                        if ((x > 0) && (cube.get(x - 1, y, z) != null) && (stone.getType() == cube.get(x - 1, y, z).getType()))
//						if ((Patch[x][y].isDropping() == Patch[x - 1][y].isDropping())
//								&& (Patch[x][y].isMovingRight() == Patch[x - 1][y].isMovingRight())
//								&& (Patch[x][y].isMovingLeft() == Patch[x - 1][y].isMovingLeft()))
                            stickyLeft = true;
                        if (stickyRight && !stone.isRightAttached()) {
                            playSound(AudioManager.STICKY);
//						Tools.play(AtlasManager.getAssetsFolderName() + "/sound/sticky.wav");
                            stone.setRightAttached(true);
                        }
                        if (!stickyRight && stone.isRightAttached()) {
                            stone.setRightAttached(false);
                        }
                        if (stickyLeft && !stone.isLeftAttached()) {
                            playSound(AudioManager.STICKY);
//						Tools.play(AtlasManager.getAssetsFolderName() + "/sound/sticky.wav");
                            stone.setLeftAttached(true);
                        }
                        if (!stickyLeft && stone.isLeftAttached()) {
                            stone.setLeftAttached(false);
                        }
                    }
                }
            }
        }
    }

    protected void markStoneDroppingOption(RcBoolean aThereWasAChange, int x, int y, int z) {
        // ---CHECK IF WE CANNOT DROP
        boolean cannotDrop = false;
        // ---IF WE CAN VANISH, WE CANNOT DROP OR MOVE!
        Stone stone = cube.get(x, y, z);
        if (stone.isVanishing()) cannotDrop = true;

            // ---WE ARE At THE BOTTOM
        else if (y == this.ySize - 1) cannotDrop = true;

            // ---THERE IS A STONE UNDER US THAT CANNOT DROP
        else if ((cube.get(x, y + 1, z) != null) && cube.get(x, y + 1, z).isCannotDrop()) cannotDrop = true;

            // ---THERE IS A STICKY STONE LEFT FROM US THAT CANNOT DROP
        else if ((x != 0) && (cube.get(x - 1, y, z) != null) && (stone.getType() == cube.get(x - 1, y, z).getType()) && cube.get(x - 1, y, z).isCannotDrop()) cannotDrop = true;

            // ---THERE IS A STICKY STONE RIGHT FROM US THAT CANNOT DROP
        else if ((x != this.xSize - 1) && (cube.get(x + 1, y, z) != null) && (stone.getType() == cube.get(x + 1, y, z).getType()) && cube.get(x + 1, y, z).isCannotDrop()) cannotDrop = true;
        if (cannotDrop && !stone.isCannotDrop()) {
            aThereWasAChange.setTrue();
            stone.setCannotDrop(true);
            droppingStones.remove(stone);
            if (stone.isCanDrop()) {
                stone.setCanDrop(false);
            }
        }
        if (!cannotDrop && stone.isCannotDrop()) {
            aThereWasAChange.setTrue();
            stone.setCannotDrop(false);
        }
        // ---CHECK IF WE CAN DROP
        if (!cannotDrop) {
            // ---THERE IS SPACE FOR DROPPING OR A DROPPING STONE UNDER US
            if ((y != this.ySize - 1) && ((cube.get(x, y + 1, z) == null) || cube.get(x, y + 1, z).isCanDrop())) {
                // ---THERE IS NO STICKY STONE LEFT FROM US OR IT CAN DROP TOO
                if ((x == 0) || (cube.get(x - 1, y, z) == null) || (stone.getType() != cube.get(x - 1, y, z).getType()) || !cube.get(x - 1, y, z).isCannotDrop()) {
                    // ---THERE IS NO STICKY STONE RIGHT FROM US OR IT CAN DROP TOO
                    if ((x == this.xSize - 1) || (cube.get(x + 1, y, z) == null) || (stone.getType() != cube.get(x + 1, y, z).getType()) || !cube.get(x + 1, y, z).isCannotDrop()) {
//						if (x == 1 && y == 5)
//							logger.info(String.format("1=%b 2=%b 3=%b", (x == width - 1) || (patch[x + 1][y] == null), (patch[x + 1][y] != null) && (patch[x][y].getType() != patch[x + 1][y].getType()),(patch[x + 1][y] != null) && !patch[x + 1][y].isCannotDrop()));
                        if (!stone.isCanDrop()) {
                            stone.setCanDrop(true);
                            droppingStones.add(stone);
                            aThereWasAChange.setTrue();
                        } else {
                            droppingStones.add(stone);
                        }
                    } else logger.error("3");
                } else logger.error("2");
            } else logger.error("1");
        }
    }

    protected void markStoneMoveLeftOption(RcBoolean aThereWasAChange, int x, int y, int z) {
        // ---CHECK IF WE CANNOT MOVE
        boolean notFree = false;
        // ---IF WE CAN DROP OR VANISH, WE CANNOT MOVE!
        Stone stone = cube.get(x, y, z);
        if (stone.isVanishing()) notFree = true;
        else if (stone.isCanDrop()) notFree = true;
            // ---CHECK IF WE CANNOT MOVE LEFT
        else if ((x == 0) || ((cube.get(x - 1, y, z) != null) && cube.get(x - 1, y, z).isCannotMoveLeft())) notFree = true;
        if (notFree && !stone.isCannotMoveLeft()) {
            stone.setCannotMoveLeft(true);
            pushingLeftStones.remove(stone);
            stone.setCanMoveLeft(false);
            aThereWasAChange.setTrue();
        } else if (!notFree && stone.isCannotMoveLeft()) {
            stone.setCannotMoveLeft(false);
            aThereWasAChange.setTrue();
        }
        {
            boolean isfree = false;
            // ---CHECK IF WE CAN MOVE LEFT
            {
                if (!notFree)
                    if ((x != 0) && ((cube.get(x - 1, y, z) == null) || cube.get(x - 1, y, z).isCanMoveLeft())) isfree = true;
                if (isfree && !stone.isCanMoveLeft()) {
                    stone.setCanMoveLeft(true);
                    aThereWasAChange.setTrue();
                }
                if (!isfree && stone.isCanMoveLeft()) {
                    stone.setCanMoveLeft(false);
                    aThereWasAChange.setTrue();
                }
            }
            // ---INHERIT OUR PUSH TO THE LEFT STONE AND STICKING RIGHT ONE
            if (isfree) {
                if (stone.isPushingLeft()) {
                    pushingLeftStones.add(stone);
                    if ((x != 0) && (cube.get(x - 1, y, z) != null) && !cube.get(x - 1, y, z).isPushingLeft()) {
                        cube.get(x - 1, y, z).setPushingLeft(true);
                        pushingLeftStones.add(cube.get(x - 1, y, z));
                        aThereWasAChange.setTrue();
                    }
                    if ((x != this.xSize - 1) && (cube.get(x + 1, y, z) != null) && (stone.getType() == cube.get(x + 1, y, z).getType()) && !cube.get(x + 1, y, z).isPushingLeft()) {
                        cube.get(x + 1, y, z).setPushingLeft(true);
                        pushingLeftStones.add(cube.get(x + 1, y, z));
                        aThereWasAChange.setTrue();
                    }
                }
            }
        }
    }

    protected void markStoneMoveRightOption(RcBoolean aThereWasAChange, int x, int y, int z) {
//		boolean canMove = false;
        // ---CHECK IF WE CANNOT MOVE
        boolean notFree = false;
        // ---IF WE CAN VANISH, WE CANNOT DROP OR MOVE!
        Stone stone = cube.get(x, y, z);
        if (stone.isVanishing()) notFree = true;
            // ---IF WE CAN DROP OR VANISH, WE CANNOT MOVE!
        else if (stone.isCanDrop()) notFree = true;
            // ---CHECK IF WE CANNOT MOVE RIGHT
        else if ((x == this.xSize - 1) || ((cube.get(x + 1, y, z) != null) && cube.get(x + 1, y, z).isCannotMoveRight())) notFree = true;
        if (notFree && !stone.isCannotMoveRight()) {
            stone.setCannotMoveRight(true);
            pushingRightStones.remove(stone);
            stone.setCanMoveRight(false);
            aThereWasAChange.setTrue();
        }
        if (!notFree && stone.isCannotMoveRight()) {
            stone.setCannotMoveRight(false);
            aThereWasAChange.setTrue();
        }
        {
            boolean isfree = false;
            // ---CHECK IF WE CAN MOVE RIGHT
            {
                if (!notFree)
                    if ((x != this.xSize - 1) && ((cube.get(x + 1, y, z) == null) || cube.get(x + 1, y, z).isCanMoveRight())) isfree = true;
                if (isfree && !stone.isCanMoveRight()) {
                    stone.setCanMoveRight(true);
                    aThereWasAChange.setTrue();
//					canMove = true;
                }
                if (!isfree && stone.isCanMoveRight()) {
                    stone.setCanMoveRight(false);
                    aThereWasAChange.setTrue();
                }
            }
            // ---INHERIT OUR PUSH TO THE RIGHT STONE AND STICKING LEFT ONE
            if (isfree) {
                if (stone.isPushingRight()) {
                    pushingRightStones.add(stone);
                    if ((x != this.xSize - 1) && (cube.get(x + 1, y, z) != null) && !cube.get(x + 1, y, z).isPushingRight()) {
                        cube.get(x + 1, y, z).setPushingRight(true);
                        pushingRightStones.add(cube.get(x + 1, y, z));
                        aThereWasAChange.setTrue();
                    }
                    if ((x != 0) && (cube.get(x - 1, y, z) != null) && (stone.getType() == cube.get(x - 1, y, z).getType()) && !cube.get(x - 1, y, z).isPushingRight()) {
                        cube.get(x - 1, y, z).setPushingRight(true);
                        pushingRightStones.add(cube.get(x - 1, y, z));
                        aThereWasAChange.setTrue();
                    }
                }
            }
        }
//		if (x == W - 1 && canMove)
//			logger.error("Error");
//		return canMove;
    }

    protected boolean markVanishingOption(RcBoolean aThereWasAChange, int x, int y, int z) {
        boolean vanish = false;
        // ---The patch below us might be the same
        Stone stone = cube.get(x, y, z);
        if ((y != this.ySize - 1) && (cube.get(x, y + 1, z) != null))
            if (stone.getType() == cube.get(x, y + 1, z).getType()) vanish = true;
        // ---The patch above us might be the same
        if ((y != preview) && (cube.get(x, y - 1, z) != null))
            if (stone.getType() == cube.get(x, y - 1, z).getType()) vanish = true;
        if (vanish && !stone.isVanishing()) {
            aThereWasAChange.setTrue();
            stone.setisVanishing(true);
        }
        if (!vanish && stone.isVanishing()) {
            stone.setisVanishing(false);
            aThereWasAChange.setTrue();
        }
        return vanish;
    }

    protected boolean moveOneStepLeft() {
        markMoveLeftOption();
        // setStoneOptions();
        boolean ChangedOnce = false;
        for (int z = 0; z < this.zSize; z++) {
            for (int y = this.ySize - 1; y >= preview; y--) {
                for (int x = 0; x < this.xSize; x++) {
                    Stone stone = cube.get(x, y, z);
                    if ((stone != null) && stone.isCanMoveLeft() && stone.isPushingLeft()) {
                        cube.set(x - 1, y, z, cube.get(x, y, z));
                        stone.x--;
                        cube.clear(x, y, z);
                        cube.get(x - 1, y, z).setMovingLeft(true);
                        ChangedOnce = true;
                    }
                }
            }
        }
        return ChangedOnce;
    }

    protected boolean moveOneStepRight() {
        markMoveRightOption();
        // setStoneOptions();
        boolean ChangedOnce = false;
        for (int z = 0; z < this.zSize; z++) {
            for (int y = this.ySize - 1; y >= preview; y--) {
                for (int x = this.xSize - 1; x >= 0; x--) {
                    Stone stone = cube.get(x, y, z);
                    if ((stone != null) && (stone.isCanMoveRight()) && (stone.isPushingRight())) {
                        cube.set(x + 1, y, z, stone);
                        stone.x++;
                        cube.clear(x, y, z);
                        cube.get(x + 1, y, z).setMovingRight(true);
                        ChangedOnce = true;
                    }
                }
            }
        }
        return ChangedOnce;
    }

    public void nextRound() {
        if (userCanReact()) {
            getRecording().addFrame(game.steps, Interaction.next);
            generateStones();
            removeVanishedStones();
            clearCommandAttributes();
            gamePhase = setStoneOptions();
        }
    }

    protected abstract void playSound(String tag);

    protected int queryHeapHeight() {
        for (int z = 0; z < this.zSize; z++)
            for (int y = preview; y < this.ySize; y++)
                for (int x = 0; x < this.xSize; x++)
                    if (cube.get(x, y, z) != null) return this.ySize - y;
        return 0;
    }

    protected boolean queryTilt() {
        return game.queryTilt(cube);
    }

    protected boolean queryWin() {
        return game.queryWin();
    }

    public boolean reactLeft(Object selected) {
        if (selected != null && userCanReact()) {
            if (Stone.class.isInstance(selected)) {
                Stone selectedStone = (Stone) selected;
                if ((selectedStone.y >= preview)) {
                    selectedStone.setPushingLeft(true);
                    pushingLeftStones.add(selectedStone);
                    int x = selectedStone.x;
                    int y = selectedStone.y;
                    int z = selectedStone.z;
                    if (moveOneStepLeft()) {
                        getRecording().addFrame(x, y, z, game.steps, Interaction.left);
                        animationPhase = maxAnimationPhase;
                        setUserReacted(true);
                        return true;
                    }
                }

            }
        } else {
        }
        return false;
    }

    public boolean reactRight(Object selected) {
        if (selected != null && userCanReact()) {
            if (Stone.class.isInstance(selected)) {
                Stone selectedStone = (Stone) selected;
                if ((selectedStone.y >= preview)) {
                    selectedStone.setPushingRight(true);
                    pushingRightStones.add(selectedStone);
                    int x = selectedStone.x;
                    int y = selectedStone.y;
                    int z = selectedStone.z;
                    if (moveOneStepRight()) {
                        getRecording().addFrame(x, y, z, game.steps, Interaction.right);
                        animationPhase = maxAnimationPhase;
                        setUserReacted(true);
                        return true;
                    }
                }

            }
        } else {
        }
        return false;
    }

    public boolean readFromDisk() {
        // only if this is a real game type and not the UI type
        if (!game.name.equals(GameName.UI.name())) {
            try
            {
//                {
//                    File         recordingFile = new File(String.format(Context.getConfigFolderName() + "/%s.yaml", game.name));
//                    ObjectMapper mapper        = new ObjectMapper(new YAMLFactory());
//                    recording = mapper.readValue(recordingFile, getRecording().getClass());
//                    update(getRecording().getGdo());
//                }
                readLegacy();
                return true;
            }
            catch (IOException e) {
                logger.info(e.getMessage(), e);
            }
        }
        return false;
    }

    private void readLegacy() throws IOException {
        //C array[z][y][x] is stored as
        // [0][0][0],[0][0][1],[0][0][2],[0][1][0],[0][1][1],[0][1][2]
        // [1][0][0],[1][0][1],[1][0][2],[1][1][0],[1][1][1],[1][1][2]
        // [2][0][0],[2][0][1],[2][0][2],[2][1][0],[2][1][1],[2][1][2]
        //        int level = 2;
        try (FileInputStream fis = new FileInputStream("level/level004.lvl");
             DataInputStream dis = new DataInputStream(fis)) {
            for (int z = 0; z < this.zSize; z++) {
                for (int y = 0; y < this.ySize; y++) {
                    for (int x = 0; x < this.xSize; x++) {
                        int stoneType = dis.readByte();
                        if (stoneType != 0) {
                            cube.set(y, z, x, createStoneAndUpdateScore(y, z, x, stoneType - 1));
                        }
                    }
                }
            }
        }
    }

    protected abstract void removeStone(Stone stone);

    private void removeVanishedStones() {
        boolean Changed = false;
        do {
            Changed = false;
            {
                for (int z = 0; z < this.zSize; z++) {
                    for (int y = preview; y < this.ySize; y++) {
                        for (int x = 0; x < this.xSize; x++) {
                            if (cube.get(x, y, z) != null && cube.get(x, y, z).isVanishing()) {
                                removeStone(cube.get(x, y, z));
                                cube.clear(x, y, z);
                                Changed = true;
                            }
                        }
                    }
                }
            }
        } while (Changed);
//		game.updateScore(queryHeapHeight());
    }

    public void rotateCubePlusX() {
        Cube buffer = new Cube(xSize, ySize, zSize);
        for (int z = 0; z < zSize; z++) {
            for (int y = 0; y < ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    Stone stone = cube.get(x, y, z);
                    buffer.set(x, z, zSize - 1 - y, stone);
                }
            }
        }
        cube.set(buffer);
        setUserReacted(true);
    }

    public void rotateCubePlusZ() {
        Cube buffer = new Cube(xSize, ySize, zSize);
        for (int z = 0; z < zSize; z++) {
            for (int y = 0; y < ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    Stone stone = cube.get(x, y, z);
                    buffer.set( y, ySize-1-x, z, stone);
                }
            }
        }
        cube.set(buffer);
        setUserReacted(true);
    }

    public void rotateCubeMinusZ() {
        Cube buffer = new Cube(xSize, ySize, zSize);
        for (int z = 0; z < zSize; z++) {
            for (int y = 0; y < ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    Stone stone = cube.get(x, y, z);
                    buffer.set(xSize-1-y, x, z, stone);
                }
            }
        }
        cube.set(buffer);
        setUserReacted(true);
    }

    public void rotateCubeMinusX() {
        Cube buffer = new Cube(xSize, ySize, zSize);
        for (int z = 0; z < zSize; z++) {
            for (int y = 0; y < ySize; y++) {
                for (int x = 0; x < xSize; x++) {
                    Stone stone = cube.get(x, y, z);
                    buffer.set(x, ySize - 1 - z, y, stone);
//                    if(stone!=null) {
//                        stone.set(stone.x,ySize - 1 -z,y);
//                    }
                }
            }
        }
        cube.set(buffer);
        setUserReacted(true);
    }

    public void setGameSeed(int seed) {
        rand.setSeed(seed);
    }

    public GamePhase setStoneOptions() {
        // ---Stones of same type on top of each other should vanish
        clearTemporaryAttributes();
        pushingLeftStones.clear();
        pushingRightStones.clear();
        RcBoolean changedOnce = new RcBoolean(false);
        boolean   canVanish   = false;
        do {
            changedOnce.setFalse();
            for (int z = 0; z < this.zSize; z++) {
                for (int y = this.ySize - 1; y >= preview; y--) {
                    for (int x = 0; x < this.xSize; x++) {
                        if (cube.get(x, y, z) != null) {
                            if (markVanishingOption(changedOnce, x, y, z))
                                canVanish = true;
                        }
                    }
                }
            }
        } while (changedOnce.getBooleanValue());
//		game.updateScore(patch);
        droppingStonesBuffer.addAll(droppingStones);// remember which stones where dropping
        droppingStones.clear();
        do {
            changedOnce.setFalse();
            for (int z = 0; z < this.zSize; z++) {
                for (int y = this.ySize - 1; y >= preview; y--) {
                    for (int x = 0; x < this.xSize; x++) {
                        if (cube.get(x, y, z) != null) {
                            markStoneDroppingOption(changedOnce, x, y, z);
                            markStoneMoveLeftOption(changedOnce, x, y, z);
                            markStoneMoveRightOption(changedOnce, x, y, z);
                        }
                    }
                }
            }
        } while (changedOnce.getBooleanValue());
        droppingStonesBuffer.removeAll(droppingStones);
        for (Stone stone : droppingStonesBuffer) {
            // stones that where dropping but now cannot drop
            playSound(AudioManager.DROP);
//			Tools.play(AtlasManager.getAssetsFolderName() + "/sound/drop.wav");
        }
        droppingStonesBuffer.clear();

        if (canVanish) {
            playSound(AudioManager.VANISH);
//			Tools.play(AtlasManager.getAssetsFolderName() + "/sound/vanish.wav");
            return GamePhase.vanishing;
        }
        markStickyPatches();
        testIntegrity();
        if (!droppingStones.isEmpty()) return GamePhase.dropping;
        if (!pushingLeftStones.isEmpty()) return GamePhase.pushingLeft;
        if (!pushingRightStones.isEmpty()) return GamePhase.pushingRight;
        return GamePhase.waiting;
    }

    protected void setUserReacted(boolean userReacted) {
        this.userReacted = userReacted;
    }

    private void testIntegrity() {
        for (int z = 0; z < this.zSize; z++) {
            for (int y = this.ySize - 1; y >= preview; y--) {
                for (int x = 0; x < this.xSize; x++) {
                    Stone stone = cube.get(x, y, z);
                    if (stone != null) {
                        if (stone.isPushingLeft() && stone.isCanMoveLeft() && pushingLeftStones.isEmpty()) {
                            logger.error(String.format("pushingLeftStones is empty, but at least one stone is still pushing left"));
                            System.exit(1);
                        }
                        if (stone.isPushingRight() && stone.isCanMoveRight() && pushingRightStones.isEmpty()) {
                            logger.error(String.format("pushingRightStones is empty, but at least one stone is still pushing right"));
                            System.exit(1);
                        }
                        if (stone.isDropping() && droppingStones.isEmpty()) {
                            logger.error(String.format("droppingStones is empty, but at least one stone is still dropping"));
                            System.exit(1);
                        }
                    }
                }
            }
        }
    }

    public void tilt() {
        gamePhase = GamePhase.tilt;
    }

    public boolean update() {

        if (animationPhase == 0) {
            removeVanishedStones();
            clearCommandAttributes();
            gamePhase = setStoneOptions();
            switch (gamePhase) {
                case vanishing:
                case pushingLeft:
                case pushingRight:
                case dropping: {
//				logger.info(String.format("gamePhase=%s", gamePhase.name()));
                    dropStones();
                    moveOneStepRight();
                    moveOneStepLeft();
                    animationPhase = maxAnimationPhase;
                }
                break;
                case waiting:
                    if (queryTilt()) {
                        tilt = true;
                    }
                    if (queryWin()) {
                        tilt = true;
                    }
                    if (isUserReacted()) {
                        setUserReacted(false);
                        generateStones();
                    }
                    clearPuchingAttributes();
                    removeVanishedStones();
                    clearCommandAttributes();
                    gamePhase = setStoneOptions();
                    break;
                case tilt:
                    break;
                default:
                    break;
            }
        } else {
            if (isEnableTime()) animationPhase--;
        }
        return tilt;
    }

    private void update(GameDataObject gdo) {
        this.game.score = gdo.getScore();
        this.game.steps = gdo.getSteps();
        rand.set(gdo.getSeed(), gdo.getRandCalls());
        this.game.relativeTime = gdo.getRelativeTime();

        for (int z = 0; z < this.zSize; z++) {
            for (int y = this.ySize - 1; y >= 0; y--) {
                for (int x = 0; x < this.xSize; x++) {
                    if (gdo.getPatch()[x][y][z] != null) {
                        cube.set(x, y, z, createStone(x, y, z, gdo.getPatch()[x][y][z].getType()));
                        cube.get(x, y, z).score = gdo.getPatch()[x][y][z].getScore();
                    }
                }
            }
        }
    }

    protected boolean userCanReact() {
        return gamePhase.equals(GamePhase.waiting) && animationPhase == 0;
    }

    public void writeResultToDisk() {
        writeToDisk(String.format("%s/%s-%d.yaml", Context.getConfigFolderName(), game.name, rand.getSeed()));
    }

    public void writeToDisk() {
        writeToDisk(String.format(Context.getConfigFolderName() + "/%s.yaml", game.name));
    }

    public void writeToDisk(String fileName) {
        try {
            getRecording().setGdo(new GameDataObject(this));
            // store the recording
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.writeValue(Gdx.files.external(fileName).file(), getRecording());
        } catch (StreamWriteException e) {
            logger.warn(e.getMessage(), e);
        } catch (DatabindException e) {
            logger.warn(e.getMessage(), e);
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

}