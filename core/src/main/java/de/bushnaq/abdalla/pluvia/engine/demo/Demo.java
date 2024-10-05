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
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Demo {
    public static final float    CAMERA_OFFSET_X = 1f;
    public static final float    CAMERA_OFFSET_Y = 7f;
    public static final float    CAMERA_OFFSET_Z = 15f;
    //    private void startAmbientMusic() throws OpenAlException {
//        gameEngine.oggPlayer = gameEngine.audioEngine.createAudioProducer(OggPlayer.class);
//        playNext();
//    }
    static              int      NUMBER_OF_TASKS = 2;
    //    public        int                     index           = 0;
    private             boolean  enabled;
    public              String[] files           = {"01-morning.ogg", "02-methodica.ogg", "05-massive.ogg", "06-abyss.ogg"};
    boolean firstTask = true;
    private final GameEngine          gameEngine;
    public        int                 index  = 0;
    private final Logger              logger = LoggerFactory.getLogger(this.getClass());
    //    public        MercatorRandomGenerator randomGenerator = new MercatorRandomGenerator(1, null);
    public        long                startTime;//start time of demo
    private final List<ScheduledTask> tasks  = new ArrayList<>();
    private final List<DemoString>    text   = new ArrayList<>();
    private final float               textX  = 100;
    //    private void renderAmbientMusic() throws OpenAlException {
//        if (!gameEngine.oggPlayer.isPlaying()) {
//            playNext();
//        }
//    }
    private       float               textY  = 0;

    public Demo(GameEngine gameEngine, boolean enabled) throws OpenAlException {
        this.gameEngine = gameEngine;
        this.enabled    = enabled;
        if (enabled) {
            startDemoMode();
        }
    }

//    private void playNext() throws OpenAlException {
//        index++;
//        index = index % 5;
//        gameEngine.oggPlayer.setFile(Gdx.files.internal(AtlasManager.getAssetsFolderName() + "/audio/" + files[index]));
//        gameEngine.oggPlayer.setGain(.1f);
//        gameEngine.oggPlayer.play();
//    }

    private void executeTasks(float deltaTime) throws OpenAlException {
        if (tasks.isEmpty()) startDemoMode();
        if (tasks.get(0).execute(deltaTime)) {
            tasks.remove(0);
        }
    }

    private void export(final String fileName, final List<DemoString> Strings) throws IOException {
        final FileWriter  fileWriter  = new FileWriter(fileName);
        final PrintWriter printWriter = new PrintWriter(fileWriter);
        for (final DemoString demoString : Strings) {
            printWriter.println(demoString.text);
        }
        printWriter.close();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void renderDemo(float deltaTime) throws IOException, OpenAlException {

        if (enabled) {
            executeTasks(deltaTime);
//            final float lineHeightFactor = 2f;
//            if (text.isEmpty()) {
//                text.add(new DemoString("Collus", gameEngine.getAtlasManager().bold128Font));
//                text.add(new DemoString("A computer game implementation of a closed economical simulation.", gameEngine.getAtlasManager().demoMidFont));
//                text.add(new DemoString(String.format("The current world is generated proceduraly and includes %d cities, %d factories, %d traders and %d sims.", gameEngine.universe.planetList.size(), gameEngine.universe.planetList.size() * 2, gameEngine.universe.traderList.size(), gameEngine.universe.simList.size()), gameEngine.getAtlasManager().demoMidFont));
//                text.add(new DemoString(String.format("There exist %d static models, %d dynamic models, %d audio producers.", gameEngine.renderEngine.staticGameObjects.size, gameEngine.renderEngine.dynamicGameObjects.size, gameEngine.audioEngine.getNumberOfAudioProducers()), gameEngine.getAtlasManager().demoMidFont));
//                text.add(new DemoString("The amount of wealth in the system, including products and money is constant at all times. ", gameEngine.getAtlasManager().demoMidFont));
//                text.add(new DemoString("Factories pay wages to sims to produce goods that are sold on a free market.", gameEngine.getAtlasManager().demoMidFont));
//                text.add(new DemoString("Some sims are traders that buy products in one city and sell them with profit in another city.", gameEngine.getAtlasManager().demoMidFont));
//                text.add(new DemoString("All sims have needs that they need to fulfill else they die.", gameEngine.getAtlasManager().demoMidFont));
//                text.add(new DemoString("All sims have cravings that they need to fulfill to keep their satisfaction level up.", gameEngine.getAtlasManager().demoMidFont));
//                text.add(new DemoString("All sounds are generated by a openal based audio render engine for libgdx supporting procedurally generated audio using HRTF.", gameEngine.getAtlasManager().demoMidFont));
//                //				demoText.add(new DemoString("Demo song is 'abyss' by Abdalla Bushnaq.", render2DMaster.atlasManager.demoMidFont));
//                text.add(new DemoString("Work in progress...", gameEngine.getAtlasManager().demoMidFont));
//                text.add(new DemoString("Developed using libgdx and gdx-gltf open source frameworks.", gameEngine.getAtlasManager().demoMidFont));
//                export("target/demo.txt", text);
//            }
//
//            Color demoTextColor;
//            demoTextColor = new Color(1f, 1f, 1f, 0.2f);
//            float deltaY = 0;
//
//            final GlyphLayout layout = new GlyphLayout();
//            layout.setText(text.get(0).font, text.get(0).text);
//            final float width = layout.width;// contains the width of the current set text
//
//            final float topMargin    = 50f;
//            final float bottomMargin = 200f;
//            for (final DemoString ds : text) {
//                ds.font.setColor(demoTextColor);
//                final float y = textY - deltaY;
//                if (y < bottomMargin) {
//                    ds.font.setColor(demoTextColor);
//                    ds.font.getColor().a = 0f;
//                } else if (y < bottomMargin * 2) {
//                    ds.font.setColor(demoTextColor);
//                    ds.font.getColor().a = demoTextColor.a * (y - bottomMargin) / bottomMargin;
//                } else if (y > gameEngine.renderEngine.renderEngine2D.height - topMargin) {
//                    ds.font.setColor(demoTextColor);
//                    ds.font.getColor().a = 0;
//                } else if (y > gameEngine.renderEngine.renderEngine2D.height - topMargin * 2) {
//                    ds.font.setColor(demoTextColor);
//                    ds.font.getColor().a = demoTextColor.a * (1 - (y - gameEngine.renderEngine.renderEngine2D.height + topMargin * 2) / topMargin);
//                } else {
//                    ds.font.setColor(demoTextColor);
//                }
//                final GlyphLayout lastLayout = ds.font.draw(gameEngine.renderEngine.renderEngine2D.batch, ds.text, textX, y, width, Align.left, true);
//                deltaY += lastLayout.height * lineHeightFactor;
//            }
//            textY += 100 * deltaTime;
//            if (textY - deltaY > gameEngine.renderEngine.renderEngine2D.height * lineHeightFactor) textY = 0;
        } else {
            gameEngine.renderEngine.getFadeEffect().setEnabled(false);
            gameEngine.getCamera().fieldOfView = GameEngine.FIELD_OF_VIEW_Y;
        }
    }

    public long runningSince() {
        if (tasks.isEmpty())
            return 0;
        return tasks.get(0).secondToRun();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void startDemoMode() throws OpenAlException {
        float taskDuration      = 10;
        float fadeInOutDuration = .5f;
//        int    secondsDelta    = 18 / 2;
        Random random = new Random(1);
        gameEngine.renderEngine.getFadeEffect().setEnabled(true);
        BoundingBox sceneBoundingBox = gameEngine.context.levelManager.getScene().getSceneBoundingBox();

        for (int t = 0; t < NUMBER_OF_TASKS; t++) {
            float angle = -30 + random.nextInt(60);
            if (firstTask) {
                Vector3 position = new Vector3(0, 7, gameEngine.context.game.cameraZPosition);
                Vector3 lookat   = new Vector3(0, 0, 0);
                tasks.add(new PositionCamera(gameEngine, 0, position, lookat, gameEngine.getCamera().fieldOfView));
//                tasks.add(new RotatingCamera(gameEngine, 20, 10));
//                tasks.add(new RotateCamera(gameEngine, angle));
                int firstSecondsDelta = 19 / 2;
                tasks.add(new PauseTask(gameEngine, taskDuration - fadeInOutDuration));
                firstTask = false;
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
                tasks.add(new PositionCamera(gameEngine, 0, position, lookat, fieldOfView));
//                tasks.add(new RotatingCamera(gameEngine, 20, 10));
//                tasks.add(new RotateCamera(gameEngine, angle));
                tasks.add(new FadeInTask(gameEngine, fadeInOutDuration));
                tasks.add(new PauseTask(gameEngine, taskDuration - fadeInOutDuration * 2));
            }
            tasks.add(new FadeOutTask(gameEngine, fadeInOutDuration));
        }

        textY     = 0;
        startTime = System.currentTimeMillis();
    }

}
