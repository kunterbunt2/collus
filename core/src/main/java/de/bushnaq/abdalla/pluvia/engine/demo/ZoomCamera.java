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

import de.bushnaq.abdalla.engine.chronos.Task;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;

public class ZoomCamera extends Task {
    private final String name;
    private final int    zoomIndex;

    public ZoomCamera(GameEngine gameEngine, int zoomIndex, String name) {
        super(gameEngine, 0);
        this.zoomIndex = zoomIndex;
        this.name      = name;
    }

    @Override
    public boolean execute(float deltaTime) {
//        gameEngine.getCamController().setTargetZoomIndex(zoomIndex);
        gameEngine.getCamera().lookAt(gameEngine.getCamera().lookat);
        gameEngine.getCamera().update(true);
        gameEngine.getCamera().setDirty(true);
        return true;
    }

    @Override
    public long secondToRun() {
        return 0;
    }

    @Override
    public void subExecute(float deltaTime) {

    }
}
