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
public class DeepSeaScene extends AbstractScene {
    public DeepSeaScene(RenderEngine3D<GameEngine> renderEngine, List<GameObject<GameEngine>> renderModelInstances) {
        super(renderEngine, renderModelInstances);
    }

    @Override
    public void create(String levelNameString) {
        super.create(levelNameString);
        logo.setColor(getInfoColor());
        version.setColor(getInfoColor());
        renderEngine.setShadowEnabled(false);
        renderEngine.getFog().setColor(Color.BLUE);
        renderEngine.getFog().setBeginDistance(13f);
        renderEngine.getFog().setFullDistance(25f);
        renderEngine.getWater().setPresent(false);
//		createCity(gameEngine, 0, 0, -CITY_SIZE * 5);
        createFish(0.1f, 3f);
//		createFirefly();
    }

    @Override
    public Color getInfoColor() {
        return Color.WHITE;
    }

}
