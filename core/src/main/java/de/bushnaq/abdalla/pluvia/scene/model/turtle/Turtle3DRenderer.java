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

package de.bushnaq.abdalla.pluvia.scene.model.turtle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Vector3;
import de.bushnaq.abdalla.engine.GameObject;
import de.bushnaq.abdalla.engine.ObjectRenderer;
import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.model.ModelInstanceHack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kunterbunt
 */
public class Turtle3DRenderer extends ObjectRenderer<GameEngine> {
    private static final Color      DIAMON_BLUE_COLOR      = new Color(0x006ab6ff);
    private static final Color      GRAY_COLOR             = new Color(0x404853ff);
    private static final float      NORMAL_LIGHT_INTENSITY = 2f;
    private static final Color      POST_GREEN_COLOR       = new Color(0x00614eff);
    private static final Color      SCARLET_COLOR          = new Color(0xb00233ff);
    private static final float      TURTLE_X_SIZE          = 1f;
    private static final float      TURTLE_Y_SIZE          = 1f;
    private static final float      TURTLE_Z_SIZE          = 1f;
    private final        Vector3    direction              = new Vector3();        // intermediate value
    private              GameObject instance;
    private              float      lightIntensity         = 0f;

    private boolean lightIsOne = false;

    private final List<PointLight> pointLight = new ArrayList<>();

    private final Vector3 translation = new Vector3();        // intermediate value

    private final Turtle turtle;

    public Turtle3DRenderer(final Turtle patch) {
        this.turtle = patch;
    }

    @Override
    public void create(final RenderEngine3D<GameEngine> renderEngine) {
        if (instance == null) {
            instance = new GameObject(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.turtleCube[turtle.getType()].scene.model), null);
            renderEngine.addDynamic(instance);
            instance.update();
        }
    }

    @Override
    public void destroy(final RenderEngine3D<GameEngine> renderEngine) {
        renderEngine.removeDynamic(instance);
        for (PointLight pl : pointLight) {
            renderEngine.remove(pl, true);
        }
    }

    private void tuneLightIntensity() {
        if (lightIsOne) {
            if (lightIntensity < NORMAL_LIGHT_INTENSITY)
                lightIntensity += Math.signum(NORMAL_LIGHT_INTENSITY - lightIntensity) * 0.1f;
            for (PointLight pl : pointLight) {
                pl.intensity = lightIntensity;
            }
        }
    }

    private void turnLightOff(final RenderEngine3D<GameEngine> renderEngine) {
        if (lightIsOne) {
            for (PointLight pl : pointLight) {
                renderEngine.remove(pl, true);
            }
            lightIsOne = false;
        }
    }

    private void turnLightOn(final RenderEngine3D<GameEngine> renderEngine) {
        if (!lightIsOne) {
            lightIntensity = 0f;
            Color color;
            if (renderEngine.isPbr()) {
                Material          material  = instance.instance.model.materials.get(0);
                Attribute         attribute = material.get(PBRColorAttribute.BaseColorFactor);
                PBRColorAttribute a         = (PBRColorAttribute) attribute;
                color = a.color;
            } else {
                Material       material  = instance.instance.model.materials.get(0);
                Attribute      attribute = material.get(ColorAttribute.Diffuse);
                ColorAttribute a         = (ColorAttribute) attribute;
                color = a.color;
            }
            Color[] colors = new Color[]{Color.WHITE, POST_GREEN_COLOR, SCARLET_COLOR, DIAMON_BLUE_COLOR, GRAY_COLOR, Color.CORAL, Color.RED, Color.GREEN, Color.BLUE, Color.GOLD, Color.MAGENTA, Color.YELLOW};
            color = colors[(int) (colors.length * Math.random())];
            final PointLight light = new PointLight().set(color, 0f, 0f, 0f, lightIntensity);
            pointLight.add(light);
            renderEngine.add(light, true);
            lightIsOne = true;
        }

    }

    @Override
    public void update(final float x, final float y, final float z, final RenderEngine3D<GameEngine> renderEngine, final long currentTime, final float timeOfDay, final int index, final boolean selected) throws Exception {
        turtle.calculateEngineSpeed(renderEngine.getGameEngine().context.isEnableTime());
        if (turtle.position != null)
            turtle.speed.set(turtle.poi.x - turtle.position.x, 0, turtle.poi.z - turtle.position.z);
        else
            turtle.speed.set(1, 0, 1);
        turtle.speed.nor();
        turtle.speed.scl(turtle.currentMaxEngineSpeed);
        final float scalex = (turtle.poi.x - turtle.position.x);
        final float scaley = (turtle.poi.y - turtle.position.y);
        final float scalez = (turtle.poi.z - turtle.position.z);
        direction.set(scalex, scaley, scalez);
        translation.x = (turtle.position.x + (turtle.poi.x - turtle.position.x) * turtle.destinationPlanetDistanceProgress / turtle.destinationPlanetDistance);
        translation.y = (turtle.position.y + (turtle.poi.y - turtle.position.y) * turtle.destinationPlanetDistanceProgress / turtle.destinationPlanetDistance);
        translation.z = (turtle.position.z + (turtle.poi.z - turtle.position.z) * turtle.destinationPlanetDistanceProgress / turtle.destinationPlanetDistance);

        for (PointLight pl : pointLight) {
            pl.setPosition(translation);
        }
        turnLightOn(renderEngine);
        tuneLightIntensity();
        {
            instance.instance.transform.setToTranslation(translation);
            instance.instance.transform.rotateTowardDirection(direction, Vector3.Y);
            instance.instance.transform.scale(turtle.getSize(), turtle.getSize(), turtle.getSize());
            instance.update();

//			poiInstance.instance.transform.setToTranslation(fish.poi);
//			poiInstance.instance.transform.scale(FISH_X_SIZE, FISH_Y_SIZE, FISH_Z_SIZE);
//			poiInstance.update();
        }

    }

}
