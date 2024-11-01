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

package de.bushnaq.abdalla.pluvia.engine.demo;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import de.bushnaq.abdalla.engine.audio.OpenAlException;
import de.bushnaq.abdalla.engine.chronos.*;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

public class Demo {
    public static final  float      CAMERA_OFFSET_X = 1f;
    public static final  float      CAMERA_OFFSET_Y = 7f;
    public static final  float      CAMERA_OFFSET_Z = 15f;
    //    private void startAmbientMusic() throws OpenAlException {
//        gameEngine.oggPlayer = gameEngine.audioEngine.createAudioProducer(OggPlayer.class);
//        playNext();
//    }
    private static final int        NUMBER_OF_TASKS = 2;
    public               String[]   files           = {"01-morning.ogg", "02-methodica.ogg", "05-massive.ogg", "06-abyss.ogg"};
    private              boolean    firstTimeEver   = true;
    private final        GameEngine gameEngine;
    public               int        index           = 0;
    private final        Logger     logger          = LoggerFactory.getLogger(this.getClass());
    //    public        MercatorRandomGenerator randomGenerator = new MercatorRandomGenerator(1, null);
    //    private void renderAmbientMusic() throws OpenAlException {
//        if (!gameEngine.oggPlayer.isPlaying()) {
//            playNext();
//        }
//    }
    Random random = new Random(1);

//    private void playNext() throws OpenAlException {
//        index++;
//        index = index % 5;
//        gameEngine.oggPlayer.setFile(Gdx.files.internal(AtlasManager.getAssetsFolderName() + "/audio/" + files[index]));
//        gameEngine.oggPlayer.setGain(.1f);
//        gameEngine.oggPlayer.play();
//    }

    public Demo(GameEngine gameEngine, boolean enabled) throws OpenAlException {
        this.gameEngine = gameEngine;
//        this.enabled    = enabled;
        if (enabled) {
            startDemoMode(true);
        }
    }

    private void export(final String fileName, final List<TextData> Strings) throws IOException {
        final FileWriter  fileWriter  = new FileWriter(fileName);
        final PrintWriter printWriter = new PrintWriter(fileWriter);
        for (final TextData textData : Strings) {
            printWriter.println(textData.text);
        }
        printWriter.close();
    }

    public void startDemoMode() {
        startDemoMode(false);
    }

    public void startDemoMode(boolean reset) {
//        logger.info("start demo mode");
        float taskDuration      = 10;
        float fadeInOutDuration = .5f;
        gameEngine.renderEngine.getFadeEffect().setEnabled(true);
        BoundingBox sceneBoundingBox = gameEngine.context.levelManager.getScene().getSceneBoundingBox();

        gameEngine.getRenderEngine().getScheduledEffectEngine().resetAllEffects();
        for (int t = 0; t < NUMBER_OF_TASKS; t++) {
            float angle = -30 + random.nextInt(60);
            if (reset) {
                random = new Random(1);
                angle  = -30 + random.nextInt(60);
                Vector3 position = new Vector3(0, 7, gameEngine.context.game.cameraZPosition);
                Vector3 lookat   = new Vector3(0, 0, 0);
                gameEngine.getRenderEngine().getScheduledEffectEngine().add(new PositionCamera<>(gameEngine, 0, position, lookat, gameEngine.getCamera().fieldOfView));
//                tasks.add(new RotatingCamera(gameEngine, 20, 10));
//                tasks.add(new RotateCamera(gameEngine, angle));
                if (!firstTimeEver) {
                    gameEngine.getRenderEngine().getScheduledEffectEngine().add(new FadeIn<>(gameEngine, fadeInOutDuration));
                }
                gameEngine.getRenderEngine().getScheduledEffectEngine().add(new Pause<>(gameEngine, taskDuration - fadeInOutDuration));
                gameEngine.getRenderEngine().getScheduledEffectEngine().add(new FadeOut<>(gameEngine, fadeInOutDuration));
                reset = false;
            } else {
                Vector3 min = new Vector3();
                sceneBoundingBox.getMin(min);
                Vector3 max = new Vector3();
                sceneBoundingBox.getMax(max);
                float   margin = 2f;
                float   x      = min.x + margin + random.nextFloat(max.x - min.x - margin * 2);
                float   y      = gameEngine.renderEngine.getMirror().getMirrorLevel();
                float   z      = min.z + margin + random.nextFloat(max.z - min.z - margin * 2);
                Vector3 lookat = new Vector3(x, y, z);
                y = gameEngine.renderEngine.getMirror().getMirrorLevel() + 2f + random.nextFloat(7f - 2f);
                z += gameEngine.context.game.cameraZPosition;
                Vector3 position    = new Vector3(x, y, z);
                float   fieldOfView = 30 + random.nextFloat(60);
                gameEngine.getRenderEngine().getScheduledEffectEngine().add(new PositionCamera<>(gameEngine, 0, position, lookat, fieldOfView));
//                tasks.add(new RotatingCamera(gameEngine, 20, 10));
//                tasks.add(new RotateCamera(gameEngine, angle));
                gameEngine.getRenderEngine().getScheduledEffectEngine().add(new FadeIn<>(gameEngine, fadeInOutDuration));
                gameEngine.getRenderEngine().getScheduledEffectEngine().add(new Pause<>(gameEngine, taskDuration - fadeInOutDuration * 2));
                gameEngine.getRenderEngine().getScheduledEffectEngine().add(new FadeOut<>(gameEngine, fadeInOutDuration));
                gameEngine.getRenderEngine().getScheduledEffectEngine().add(new RestartDemoTask<>(gameEngine));
            }
        }
        firstTimeEver = false;
    }

}
