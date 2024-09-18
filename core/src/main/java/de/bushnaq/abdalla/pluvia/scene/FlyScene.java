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

package de.bushnaq.abdalla.pluvia.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import de.bushnaq.abdalla.engine.GameObject;
import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.engine.ModelManager;
import de.bushnaq.abdalla.pluvia.scene.model.fly.Fly;

import java.util.List;

/**
 * @author kunterbunt
 */
public class FlyScene extends AbstractScene {

    public FlyScene(RenderEngine3D<GameEngine> renderEngine, List<GameObject<GameEngine>> renderModelInstances) {
        super(renderEngine, renderModelInstances);
    }

    @Override
    public void create(String levelNameString) {
        super.create(levelNameString);
        logo.setColor(getInfoColor());
        version.setColor(getInfoColor());
        renderEngine.setSkyBox(false);
        renderEngine.setShadowEnabled(true);
        // time
        renderEngine.setAlwaysDay(true);
        renderEngine.setDynamicDayTime(false);
        renderEngine.setFixedDayTime(8);
        // white fog
        renderEngine.getFog().setBeginDistance(20f);
        renderEngine.getFog().setFullDistance(50f);
        renderEngine.getFog().setColor(Color.WHITE);
        // water
        renderEngine.getWater().setPresent(false);
        // mirror
        renderEngine.getMirror().setPresent(false);

        // generate instances
        createPlane(Color.WHITE);

        createFly(0.02f, 0.02f);
    }

    protected void createFly(float minSize, float maxSize) {
        Vector3 min = renderEngine.getSceneBox().min;
        Vector3 max = renderEngine.getSceneBox().max;
        for (int i = 0; i < Math.min(renderEngine.getGameEngine().context.getMaxSceneObjects(), 500); i++) {
            int         type    = rand.nextInt(ModelManager.MAX_NUMBER_OF_FLY_MODELS);
            float       size    = minSize + (float) Math.random() * (maxSize - minSize);
            BoundingBox b       = new BoundingBox(new Vector3(min.x + 4f, size / 2, min.z + 5), new Vector3(max.x - 4f, 4f, 0));
            Fly         firefly = new Fly(renderEngine, type, size, b);
//			fish.setMaxSpeed(0.3f);
//			fish.setAccellerationDistance(5f);
            renderEngine.getGameEngine().context.fireflyList.add(firefly);
        }
    }

    @Override
    public Color getInfoColor() {
        return Color.BLACK;
    }

}
