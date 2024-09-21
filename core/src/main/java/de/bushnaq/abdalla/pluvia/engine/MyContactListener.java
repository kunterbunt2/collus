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

package de.bushnaq.abdalla.pluvia.engine;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import de.bushnaq.abdalla.engine.GameObject;
import de.bushnaq.abdalla.pluvia.scene.model.marble.Marble;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MyContactListener extends ContactListener {
    private final GameEngine gameEngine;
    protected     Logger     logger = LoggerFactory.getLogger(this.getClass());

    public MyContactListener(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    public boolean onContactAdded(int userValue0, int partId0, int index0, int userValue1, int partId1, int index1) {
        GameObject go0 = (GameObject) gameEngine.renderEngine.physicsEngine.instances.get(userValue0);
        GameObject go1 = (GameObject) gameEngine.renderEngine.physicsEngine.instances.get(userValue1);
        if (go0.interactive instanceof Marble m) {
            //go0 is a marble
            m.get3DRenderer().onContactAdded(go0, go1);
        } else if (go1.interactive instanceof Marble m) {
            //go1 is a marble
            m.get3DRenderer().onContactAdded(go1, go0);
        }
//        else
//            logger.info("contact!");
        return true;
    }
}
