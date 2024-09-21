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
import de.bushnaq.abdalla.pluvia.scene.model.firefly.Firefly;

import java.util.List;

/**
 * @author kunterbunt
 */
public class FireflyScene extends AbstractScene {

    public FireflyScene(RenderEngine3D<GameEngine> renderEngine, List<GameObject<GameEngine>> renderModelInstances) {
        super(renderEngine, renderModelInstances);
    }

    @Override
    public void create(String levelNameString) {
        super.create(levelNameString);
        logo.setColor(getInfoColor());
        version.setColor(getInfoColor());
        renderEngine.setSkyBox(false);
        renderEngine.setShadowEnabled(false);
        // time
        renderEngine.setAlwaysDay(true);
        renderEngine.setDynamicDayTime(true);
        renderEngine.setFixedDayTime(8);
        // fog
//		gameEngine.renderEngine.getFog().setBeginDistance(20f);
//		gameEngine.renderEngine.getFog().setFullDistance(50f);
        renderEngine.getFog().setBeginDistance(1120f);
        renderEngine.getFog().setFullDistance(1150f);
        renderEngine.getFog().setColor(Color.BLACK);
        // water
        renderEngine.getWater().setPresent(false);
        renderEngine.getWater().setWaveStrength(0.005f);
        renderEngine.getWater().setTiling(16f);
        renderEngine.getWater().setWaveSpeed(0.02f);
        renderEngine.getWater().setRefractiveMultiplicator(0f);
        createWater();
        // mirror
        renderEngine.getMirror().setPresent(false);
        renderEngine.getMirror().setReflectivity(0.9f);
        createMirror(Color.BLACK);
        // effect
//		gameEngine.renderEngine.addBloomEffect();

        createPlane(Color.BLACK);

        // generate instances
        createCity(renderEngine, 0, 0, -CITY_SIZE * 5, true, 2);
        createFirefly(0.05f, 0.05f);
    }

    private void createFirefly(float minSize, float maxSize) {
        Vector3 min = renderEngine.getSceneBox().min;
        Vector3 max = renderEngine.getSceneBox().max;
        for (int i = 0; i < Math.min(renderEngine.getGameEngine().context.getMaxSceneObjects(), 500); i++) {
            int         type = rand.nextInt(ModelManager.MAX_NUMBER_OF_FIREFLY_MODELS);
            float       size = minSize + (float) Math.random() * (maxSize - minSize);
            BoundingBox b    = new BoundingBox(new Vector3(min.x + 4f, 1 + size / 2, min.z), new Vector3(max.x - 4f, 4f, -10));
            Firefly     fly  = new Firefly(renderEngine, type, size, b);
            renderEngine.getGameEngine().context.flyList.add(fly);
        }
    }

    @Override
    public Color getInfoColor() {
        return Color.WHITE;
    }

}
