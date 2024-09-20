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
import de.bushnaq.abdalla.pluvia.scene.model.rain.Rain;

import java.util.List;

/**
 * @author kunterbunt
 */
public class RainScene extends AbstractScene {

    public RainScene(RenderEngine3D<GameEngine> renderEngine, List<GameObject<GameEngine>> renderModelInstances) {
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
        // white fog
        renderEngine.getFog().setBeginDistance(20f);
        renderEngine.getFog().setFullDistance(50f);
        renderEngine.getFog().setColor(Color.WHITE);
        renderEngine.getFog().setEnabled(true);
        // water
        renderEngine.getWater().setPresent(false);
//        createWater();
        // mirror
        renderEngine.getMirror().setPresent(true);
        renderEngine.getMirror().setReflectivity(0.5f);
        renderEngine.getMirror().setMirrorLevel(-4.949f);
        createMirror(Color.WHITE);
        createMarbles(.3f, 2f, -4.949f);
        createRain(.02f, .01f);
    }

    protected void createRain(float minSize, float maxSize) {
        Vector3 min = renderEngine.getSceneBox().min;
        Vector3 max = renderEngine.getSceneBox().max;
        for (int i = 0; i < Math.min(renderEngine.getGameEngine().context.getMaxSceneObjects(), 500); i++) {
            int         type = rand.nextInt(ModelManager.MAX_NUMBER_OF_RAIN_MODELS);
            float       size = minSize + (float) Math.random() * (maxSize - minSize);
            BoundingBox b    = new BoundingBox(new Vector3(min.x + 10f, size / 2, min.z + 25), new Vector3(max.x - 10f, 4f, 0));
            Rain        rain = new Rain(renderEngine, type, size, b);
            renderEngine.getGameEngine().context.rainList.add(rain);
        }
    }

    @Override
    public Color getInfoColor() {
        return Color.BLACK;
    }

}
