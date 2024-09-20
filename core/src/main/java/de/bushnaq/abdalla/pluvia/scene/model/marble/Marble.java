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

/*
 * Created on 10.07.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.scene.model.marble;

import com.badlogic.gdx.math.collision.BoundingBox;
import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.scene.model.AbstractActor;

/**
 * @author kunterbunt
 */
public class Marble extends AbstractActor {

    private final GameEngine gameEngine;

    public Marble(RenderEngine3D<GameEngine> renderEngine, int type, float size, BoundingBox cage) {
        super(renderEngine, type, size, cage);
        this.gameEngine = renderEngine.getGameEngine();
        setMaxSpeed(0.05f);
        setMinSpeed(0.1f);
        set3DRenderer(new Marble3DRenderer(this));
        choseStartingPoint();
        choseStartingSpeed();
        get3DRenderer().create(renderEngine);
    }

    public void advanceInTime(long currentTime) {
        position.add(speed);
        //hitting the border
    }

    protected void choseStartingPoint() {
        float w = cage.getWidth() - size;
        float d = cage.getDepth() - size;

        position.x = cage.getCenterX() - w / 2 + ((float) Math.random() * w);
        position.y = cage.getCenterY();//on the plane
        position.z = cage.getCenterZ() - d / 2 + ((float) Math.random() * d);

        if (type == 0) {
            position.x = cage.getCenterX() - w / 2 + 3;
            position.z = cage.getCenterZ();
        } else {
            position.x = cage.getCenterX() + w / 2 - 3;
            position.z = cage.getCenterZ();
        }
    }

    protected void choseStartingSpeed() {
        speed.x = Math.signum((float) Math.random() - 0.5f) * (minSpeed + (maxSpeed - minSpeed) * (float) Math.random());
        speed.y = 0;
        speed.z = Math.signum((float) Math.random() - 0.5f) * (minSpeed + (maxSpeed - minSpeed) * (float) Math.random());

        if (type == 0) {
            speed.x = maxSpeed;
            speed.z = 0;
        } else {
            speed.x = -maxSpeed;
            speed.z = 0;
        }
    }
}