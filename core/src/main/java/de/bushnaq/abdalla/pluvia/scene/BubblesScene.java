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

import java.util.List;

/**
 * @author kunterbunt
 */
public class BubblesScene extends AbstractScene {

    public BubblesScene(RenderEngine3D<GameEngine> renderEngine, List<GameObject<GameEngine>> renderModelInstances) {
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
        renderEngine.setDynamicDayTime(false);
//		gameEngine.renderEngine.setFixedDayTime(8);
        // fog
        renderEngine.getFog().setBeginDistance(20f);
        renderEngine.getFog().setFullDistance(50f);
        renderEngine.getFog().setColor(Color.WHITE);
        renderEngine.getFog().setEnabled(true);
        // water
        renderEngine.getWater().setPresent(false);
        // mirror
        renderEngine.getMirror().setPresent(true);
        renderEngine.getMirror().setReflectivity(0.1f);
        renderEngine.getMirror().setMirrorLevel(MIRROR_LEVEL);
//        renderEngine.setDayAmbientLight(.5f, .5f, .5f, 10f);
//        renderEngine.setNightAmbientLight(.8f, .8f, .8f, 1f);
//        createPlane(Color.WHITE);

//        createMarbles(.5f, .5f, MIRROR_LEVEL);
        createBubble(0.02f, 0.5f, MIRROR_LEVEL);
//        createTurtles(1f, 1f, -4.949f);
        createMirror(Color.WHITE);
    }

    private void createBubble(float minSize, float maxSize, float planeLevel) {
        Vector3 min = renderEngine.getSceneBox().min;
        Vector3 max = renderEngine.getSceneBox().max;
        sceneBoundingBox = new BoundingBox(new Vector3(min.x - 30, planeLevel, min.z - 30), new Vector3(max.x + 30, planeLevel + maxSize, 5));
//        Vector3 min = renderEngine.getSceneBox().min;
//        Vector3 max = renderEngine.getSceneBox().max;
//        for (int i = 0; i < Math.min(renderEngine.getGameEngine().context.getMaxSceneObjects(), 500); i++) {
//            int         type   = rand.nextInt(ModelManager.MAX_NUMBER_OF_BUBBLE_MODELS);
//            float       size   = minSize + (float) Math.random() * (maxSize - minSize);
//            BoundingBox b      = new BoundingBox(new Vector3(min.x + 4f, 1 + size / 2, min.z), new Vector3(max.x - 4f, 4f, -2));
//            Bubble      bubble = new Bubble(renderEngine, type, size, b);
//            renderEngine.getGameEngine().context.bubbleList.add(bubble);
//        }
    }

    @Override
    public Color getInfoColor() {
        return Color.BLACK;
    }

}
