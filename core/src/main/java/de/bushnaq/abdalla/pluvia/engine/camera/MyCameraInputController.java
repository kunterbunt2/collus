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

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import de.bushnaq.abdalla.engine.camera.MovingCamera;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kunterbunt
 */
public class MyCameraInputController extends CameraInputController {

    private final GameEngine gameEngine;
    private final Logger     logger = LoggerFactory.getLogger(this.getClass());
    private final Vector3    tmpV1  = new Vector3();

    public MyCameraInputController(final Camera camera, GameEngine gameEngine) throws Exception {
        super(camera);
        this.gameEngine = gameEngine;
        rotateButton    = Buttons.RIGHT;
        pinchZoomFactor = 0.02f;
    }

    @Override
    protected boolean process(final float deltaX, final float deltaY, final int button) {
        logger.info("process");
        if (gameEngine.renderEngine.isDebugMode()) {
            try {
                final MovingCamera movingCamera = (MovingCamera) camera;
                if (button == rotateButton) {
                    tmpV1.set(movingCamera.direction).crs(movingCamera.up)/*.y = 0f*/;
                    movingCamera.rotateAround(movingCamera.lookat, tmpV1.nor(), deltaY * rotateAngle);
                    movingCamera.rotateAround(movingCamera.lookat, Vector3.Y, deltaX * -rotateAngle);
                    movingCamera.setDirty(true);
                    //				notifyListener(movingCamera);
                } else {
                    return false;
                }
                if (autoUpdate)
                    movingCamera.update();
            } catch (final Exception e) {
                logger.error(e.getMessage(), e);
            }
            return true;
        } else
            return false;
    }

    @Override
    public boolean zoom(final float amount) {
        try {
            if (!alwaysScroll && activateKey != 0 && !activatePressed)
                return false;
            final MovingCamera myCamera = (MovingCamera) camera;
            myCamera.translate(0, 0, -amount * pinchZoomFactor);
            myCamera.setDirty(true);
            // notifyListener(myCamera);
            if (autoUpdate)
                camera.update();
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
        }
        return true;
    }

}
