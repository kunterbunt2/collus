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
import de.bushnaq.abdalla.engine.GameObject;
import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;

import java.util.List;

/**
 * @author kunterbunt
 */
public class MarbleScene extends AbstractScene {

    public static final float MIRROR_LEVEL = -4.949f;

    public MarbleScene(RenderEngine3D<GameEngine> renderEngine, List<GameObject<GameEngine>> renderModelInstances) {
        super(renderEngine, renderModelInstances);
    }

    @Override
    public void create(String levelNameString) {
        super.create(levelNameString);
        logo.setColor(getInfoColor());
        version.setColor(getInfoColor());
        //fx
        renderEngine.getDepthOfFieldEffect().setFocalDepth(15f);
        renderEngine.getDepthOfFieldEffect().setEnabled(true);
        renderEngine.getDepthOfFieldEffect().setThreshold(1);
//        renderEngine.getDepthOfFieldEffect().setGain(2);
        renderEngine.setRenderBokeh(false);


        renderEngine.setSkyBox(false);
        renderEngine.setShadowEnabled(true);
        // time
        renderEngine.setAlwaysDay(true);
        renderEngine.setDynamicDayTime(true);
        renderEngine.setFixedDayTime(8);
        // white fog
        renderEngine.getFog().setColor(Color.WHITE);
        renderEngine.getFog().setBeginDistance(20f);
        renderEngine.getFog().setFullDistance(50f);
        renderEngine.getFog().setEnabled(true);
        // water
        renderEngine.getWater().setPresent(false);
        // mirror
        renderEngine.getMirror().setPresent(true);
        renderEngine.getMirror().setReflectivity(0.5f);
        renderEngine.getMirror().setMirrorLevel(MIRROR_LEVEL);
        createMirror(Color.WHITE);

        createMarbles(.5f, .5f, MIRROR_LEVEL);
    }

    @Override
    public Color getInfoColor() {
        return Color.BLACK;
    }

}
