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

import java.util.List;

/**
 * @author kunterbunt
 */
public class Marble extends AbstractActor {

    static        float      PHYSICS_MARGINE = 0.1f;
    private final GameEngine gameEngine;

    public Marble(RenderEngine3D<GameEngine> renderEngine, int type, float size, BoundingBox cage, List<Marble> marbleList) {
        super(renderEngine, type, size, cage);
        this.gameEngine = renderEngine.getGameEngine();
        setMaxSpeed(0.02f);
        setMinSpeed(0.002f);
        set3DRenderer(new Marble3DRenderer(this));
        choseStartingPoint(marbleList);
        choseStartingSpeed();
        get3DRenderer().create(renderEngine);
    }

    public void advanceInTime(long currentTime) {
        position.add(speed);
        //hitting the border
    }

    protected void choseStartingPoint(List<Marble> marbleList) {
        float w = cage.getWidth() - size - PHYSICS_MARGINE * 2;
        float d = cage.getDepth() - size - PHYSICS_MARGINE * 2;

        position.y = cage.getCenterY();//on the plane
        do {
            position.x = cage.getCenterX() - w / 2 + PHYSICS_MARGINE + ((float) Math.random() * w);
            position.z = cage.getCenterZ() - d / 2 + PHYSICS_MARGINE + ((float) Math.random() * d);
        }
        while (collisionFound(marbleList));

    }

    protected void choseStartingSpeed() {
        speed.x = Math.signum((float) Math.random() - 0.5f) * (minSpeed + (maxSpeed - minSpeed) * (float) Math.random());
        speed.y = 0;
        speed.z = Math.signum((float) Math.random() - 0.5f) * (minSpeed + (maxSpeed - minSpeed) * (float) Math.random());

//        if (type == 0) {
//            speed.x = maxSpeed;
//            speed.z = maxSpeed;
//        } else {
//            speed.x = -maxSpeed;
//            speed.z = 0;
//        }
    }

    private boolean collisionFound(List<Marble> marbleList) {
        for (Marble marble : marbleList) {
            float dx = Math.abs(marble.position.x - position.x);
            float dz = Math.abs(marble.position.z - position.z);
            if (dx * dx + dz * dz < size)
                return true;
        }
        return false;
    }
}