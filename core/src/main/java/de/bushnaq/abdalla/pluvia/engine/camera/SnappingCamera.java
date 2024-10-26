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

package de.bushnaq.abdalla.pluvia.engine.camera;

import com.badlogic.gdx.math.Vector3;
import de.bushnaq.abdalla.engine.camera.MovingCamera;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnappingCamera extends MovingCamera {
    private final Logger  logger = LoggerFactory.getLogger(this.getClass());
    private       Vector3 snapBackPosition;

    public SnappingCamera(float fieldOfViewY, int width, int height) {
        super(fieldOfViewY, width, height);
    }

    public void snapBack(Vector3 snapBackPosition) {
//        logger.info("snapping camera");
        this.snapBackPosition = snapBackPosition;
    }

    public void snapBack() {
//        logger.info("snapping back camera to {}", snapBackPosition.toString());
        position.set(snapBackPosition);
        up.set(0, 1, 0);
        lookAt(lookat);
        update();
        setDirty(true);
    }
}
