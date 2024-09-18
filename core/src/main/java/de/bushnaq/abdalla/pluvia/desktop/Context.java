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

package de.bushnaq.abdalla.pluvia.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.bushnaq.abdalla.engine.IContext;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.game.Game;
import de.bushnaq.abdalla.pluvia.game.GameList;
import de.bushnaq.abdalla.pluvia.game.LevelManager;
import de.bushnaq.abdalla.pluvia.game.model.stone.StoneList;
import de.bushnaq.abdalla.pluvia.game.score.Score;
import de.bushnaq.abdalla.pluvia.game.score.ScoreList;
import de.bushnaq.abdalla.pluvia.scene.model.bubble.Bubble;
import de.bushnaq.abdalla.pluvia.scene.model.digit.Digit;
import de.bushnaq.abdalla.pluvia.scene.model.firefly.Firefly;
import de.bushnaq.abdalla.pluvia.scene.model.fish.Fish;
import de.bushnaq.abdalla.pluvia.scene.model.fly.Fly;
import de.bushnaq.abdalla.pluvia.scene.model.rain.Rain;
import de.bushnaq.abdalla.pluvia.scene.model.turtle.Turtle;
import de.bushnaq.abdalla.pluvia.util.MavenPropertiesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

enum OperatingSystem {
    android, applet, headlessDesktop, ios, iosSimulator, linux, osx, unknonw, webgl, windows
}

/**
 * @author kunterbunt
 */
public abstract class Context extends ApplicationProperties implements IContext {
    private static   String             appFolderName    = "app";
    private          String             appVersion       = "0.0.0";
    public           ModelList<Bubble>  bubbleList       = new ModelList<>();
    private static   String             configFolderName = "app/config";
    public           long               currentTime      = 8L * 10000;
    public           ModelList<Digit>   digitList        = new ModelList<>();
    private          boolean            enableTime       = true;
    public           ModelList<Fly>     fireflyList      = new ModelList<>();
    public           ModelList<Fish>    fishList         = new ModelList<>();
    public           ModelList<Firefly> flyList          = new ModelList<>();
    public           Game               game             = null;                // the current game
    public           GameList           gameList         = new GameList();
    private static   String             homeFolderName;
    private final    String             installationFolder;
    private          long               lastTime         = 0;
    public           LevelManager       levelManager     = null;
    private          int                levelNumber      = 1;
    protected static Logger             logger           = LoggerFactory.getLogger(Context.class);
    private final    OperatingSystem    operatingSystem;
    public           ModelList<Rain>    rainList         = new ModelList<>();
    public           boolean            restart          = false;
    protected        ScoreList          scoreList        = new ScoreList(3);
    public           Object             selected         = null;
    public final     StoneList          stoneList        = new StoneList();
    public           long               timeDelta        = 0L;
    public           ModelList<Turtle>  turtleList       = new ModelList<>();

    public Context() {
        try {
            appVersion = MavenPropertiesProvider.getProperty("module.version");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        homeFolderName   = "app";
        operatingSystem  = getOperatingSystemType();
        configFolderName = homeFolderName + "/config";
        switch (operatingSystem) {
            case windows:
            default:
                if (isRunningInEclipse()) {
                    logger.info("Detected 'Windows' system and we are running inside of 'Eclipse'.");
                    installationFolder = cleanupPath(getInstallationFolder() + "/../../..");
                    appFolderName      = installationFolder + "/app";
                } else {
                    logger.info("Detected 'Windows' system.");
                    installationFolder = cleanupPath(getInstallationFolder() + "/../..");
                    appFolderName      = installationFolder + "/app";
                }
                break;
            case linux:
                if (isRunningInEclipse()) {
                    logger.info("Detected 'Linux' system and we are running inside of 'Eclipse'.");
                    installationFolder = cleanupPath(getInstallationFolder() + "/../../..");
                    appFolderName      = installationFolder + "/app";
                } else {
                    logger.info("Detected 'Linux' system.");
                    installationFolder = cleanupPath(getInstallationFolder() + "/../../../bin");
                    appFolderName      = cleanupPath(installationFolder + "/../lib/app");
                }
                break;
            case osx:
                if (isRunningInEclipse()) {
                    logger.info("Detected 'macOS' system and we are running inside of 'Eclipse'.");
                    installationFolder = cleanupPath(getInstallationFolder() + "/../../..");
                    appFolderName      = installationFolder + "/app";
                } else {
                    logger.info("Detected 'macOS' system.");
                    installationFolder = cleanupPath(getInstallationFolder() + "/../../MacOS");
                    appFolderName      = cleanupPath(installationFolder + "/../app");
                }
                break;
            case iosSimulator: {
                logger.info("Detected 'iOS' system and we are running inside of 'simulator'.");
                homeFolderName     = ".";
                installationFolder = ".";
                appFolderName      = installationFolder;
            }
            break;
            case ios: {
                logger.info("Detected 'iOS' system.");
                homeFolderName     = ".";
                installationFolder = ".";
                appFolderName      = installationFolder;
            }
            break;

        }
        logger.info("Detected 'home' folder = " + homeFolderName);
        logger.info("Detected 'installation' folder = " + installationFolder);
        logger.info("Detected 'app' folder = " + appFolderName);
        logger.info("Detected 'configuration' folder = " + configFolderName);
        createFolder(homeFolderName);
        createFolder(configFolderName);
        init();

    }

    public void advanceInTime() throws Exception {
        advanceInTime(enableTime);
    }

    public void advanceInTime(final boolean enable) throws Exception {
        if (enable) {
            levelManager.updateFps();
            timeDelta = System.currentTimeMillis() - lastTime;
            if (timeDelta > 1000)
                timeDelta = 0;// we probably just started
            {
                currentTime += timeDelta;
                for (Fish fish : fishList) {
                    fish.advanceInTime(currentTime);
                }
                for (Fly firefly : fireflyList) {
                    firefly.advanceInTime(currentTime);
                }
                for (Firefly fly : flyList) {
                    fly.advanceInTime(currentTime);
                }
                for (Bubble bubble : bubbleList) {
                    bubble.advanceInTime(currentTime);
                }
                for (Turtle turtle : turtleList) {
                    turtle.advanceInTime(currentTime);
                }
                for (Rain rain : rainList) {
                    rain.advanceInTime(currentTime);
                }
            }
            lastTime = System.currentTimeMillis();
        }
    }

    protected static String cleanupPath(String path) {
        try {
            path = new File(path).getCanonicalPath();// get rid of all the /..
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return path;
    }

    private void createFolder(String folderName) {
        FileHandle local = Gdx.files.external(folderName);
        if (!local.exists()) {
            local.mkdirs(); // If you require it to make the entire directory path including parents, use directory.mkdirs(); here instead.
        }
    }

    public void dispose() {
        levelManager.destroy();
        levelManager = null;
    }

    public static String getAppFolderName() {
        return appFolderName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public static String getConfigFolderName() {
        return configFolderName;
    }

    public int getGameIndex() {
        int index = 0;
        for (Game g : gameList) {
            if (g.getName().equals(game.getName()))
                return index;
            index++;
        }
        return -1;
    }

    public int getGameIndex(String name) {
        int index = 0;
        for (Game g : gameList) {
            if (g.getName().equals(name))
                return index;
            index++;
        }
        return -1;
    }

    protected abstract String getInstallationFolder();

    public int getLastGameSeed() {
        int lastGameSeed = -1;
        for (Score s : scoreList) {
            if (game.getName().equals(s.getGame()))
                lastGameSeed = Math.max(lastGameSeed, s.getSeed());
        }
        return lastGameSeed;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public static OperatingSystem getOperatingSystemType() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return OperatingSystem.windows;
        } else if (os.contains("mac")) {
            return OperatingSystem.osx;
        } else if (os.contains("nix") || os.contains("nux")) {
            return OperatingSystem.linux;
        } else if (os.contains("ios simulator")) {
            return OperatingSystem.iosSimulator;
        } else if (os.contains("ios")) {
            return OperatingSystem.ios;
        } else
            return OperatingSystem.unknonw;
    }

    public ScoreList getScoreList() {
        return scoreList;
    }

    public boolean isEnableTime() {
        return enableTime;
    }

    public static boolean isIos() {
        return getOperatingSystemType().equals(OperatingSystem.ios) || getOperatingSystemType().equals(OperatingSystem.iosSimulator);
    }

    public static boolean isRunningInEclipse() {
        String  path      = System.getProperty("java.class.path").toLowerCase();
        boolean isEclipse = path.contains(".m2");
        return isEclipse;
    }

    public void readScoreFromDisk(GameEngine gameEngine) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            File file = new File(Context.getConfigFolderName() + "/score.yaml");
            scoreList = mapper.readValue(file, ScoreList.class);
            if (!scoreList.testValidity(gameEngine)) {
                logger.error("invalid score file");
                scoreList.clear();
                file.delete();
            }
            scoreList.changed();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            logger.warn("score file corrupt, cleared score!");
            scoreList.clear();
        }
    }

    public void selectGame(int gameIndex) {
        game = gameList.get(gameIndex);
    }

    public void selectGame(String name) {
        for (Game g : gameList) {
            if (g.getName().equals(name))
                game = g;
        }
    }

    public void setEnableTime(boolean enableTime) {
        this.enableTime = enableTime;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public void setScoreList(ScoreList aScoreList) {
        scoreList = aScoreList;
    }

    public void setSelected(final Object selected, final boolean setDirty) throws Exception {
        this.selected = selected;
    }

}
