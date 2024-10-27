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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import de.bushnaq.abdalla.engine.IGameEngine;
import de.bushnaq.abdalla.engine.shader.effect.scheduled.ScheduledTask;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;

import java.util.List;

public class TranslateLegacyLevel<T extends IGameEngine> extends ScheduledTask<T> {

    private final List<String> files;
    private       String       lastFile;

    public TranslateLegacyLevel(T gameEngine, List<String> files) {
        super(gameEngine, 0);
        this.files = files;
    }

    @Override
    public boolean execute(float deltaTime) {
        String     legacyDirectory = "legacy-level/";
        String     directory       = "level/";
        GameEngine ge              = (GameEngine) gameEngine;
        if (lastFile != null) {
            int size = (int) (Gdx.graphics.getBackBufferHeight() * 0.8f);
            int x    = Gdx.graphics.getBackBufferWidth() / 2 - size / 2;
            int y    = Gdx.graphics.getBackBufferHeight() / 2 - size / 2;
            saveScreenshot(x, y, size, size, directory + lastFile + ".png");
        }
        if (files.isEmpty()) {
            logger.info("stop TranslateLegacyLevel");
            ge.context.levelManager.disposeLevel();
            ge.context.levelManager.readFromDisk(ge.context.getLevelNumber());
            return true;
        } else {
            logger.info("execute TranslateLegacyLevel");
            lastFile = files.remove(0);
            logger.info(lastFile);
            try {
                ge.context.levelManager.disposeLevel();
                ge.context.levelManager.readLegacy(legacyDirectory + lastFile);
                ge.context.levelManager.createLevel(String.format("level %03d", ge.context.getLevelNumber()));
                lastFile = lastFile.toLowerCase();
                ge.context.levelManager.writeLevelToDisk(directory + lastFile);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return false;
    }

    public void saveScreenshot(int x, int y, int width, int height, String fileName) {
        final byte[] pixels = ScreenUtils.getFrameBufferPixels(x, y, width, height, true);
        // This loop makes sure the whole screenshot is opaque and looks exactly like
        // what the user is seeing
        for (int i = 4; i < pixels.length; i += 4) {
            pixels[i - 1] = (byte) 255;
        }
        final Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
        final FileHandle handle = Gdx.files.local(fileName);
        PixmapIO.writePNG(handle, pixmap);
        pixmap.dispose();
    }

    @Override
    public long secondToRun() {
        return 0;
    }

    @Override
    public void subexecute(float deltaTime) {

    }
}
