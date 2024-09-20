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

package de.bushnaq.abdalla.pluvia.scene.model.marble;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import de.bushnaq.abdalla.engine.GameObject;
import de.bushnaq.abdalla.engine.ObjectRenderer;
import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.engine.physics.PhysicsEngine;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.model.ModelInstanceHack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kunterbunt
 */
public class Marble3DRenderer extends ObjectRenderer<GameEngine> {
    private static final Color                  DIAMON_BLUE_COLOR      = new Color(0x006ab6ff);
    private static final Color                  GRAY_COLOR             = new Color(0x404853ff);
    private static final float                  NORMAL_LIGHT_INTENSITY = 2f;
    private static final Color                  POST_GREEN_COLOR       = new Color(0x00614eff);
    private static final Color                  SCARLET_COLOR          = new Color(0xb00233ff);
    private              btCollisionObject      ballObject;
    private              btSphereShape          ballShape;
    private              GameObject<GameEngine> gameObject;
    private              float                  lightIntensity         = 0f;
    private              boolean                lightIsOne             = false;
    private final        Marble                 marble;
    private final        List<PointLight>       pointLight             = new ArrayList<>();
    private final        Vector3                translation            = new Vector3();        // intermediate value

    public Marble3DRenderer(final Marble patch) {
        this.marble = patch;
    }

    @Override
    public void create(final RenderEngine3D<GameEngine> renderEngine) {
        if (gameObject == null) {
            gameObject = new GameObject<>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.marbleModel[marble.getType()].scene.model), null);
            renderEngine.addDynamic(gameObject);
            gameObject.update();
            ballObject = new btCollisionObject();
            ballShape  = new btSphereShape(marble.getSize());
            ballObject.setCollisionShape(ballShape);
            ballObject.setWorldTransform(gameObject.instance.transform);
            renderEngine.physicsEngine.add(gameObject, ballObject, PhysicsEngine.MARBLE_FLAG, PhysicsEngine.ALL_FLAG);
        }
    }

    @Override
    public void destroy(final RenderEngine3D<GameEngine> renderEngine) {
        renderEngine.removeDynamic(gameObject);
        for (PointLight pl : pointLight) {
            renderEngine.remove(pl, true);
        }
        renderEngine.physicsEngine.remove(gameObject, ballObject);
        ballObject.dispose();
        ballShape.dispose();
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
                Material          material  = gameObject.instance.model.materials.get(0);
                Attribute         attribute = material.get(PBRColorAttribute.BaseColorFactor);
                PBRColorAttribute a         = (PBRColorAttribute) attribute;
                color = a.color;
            } else {
                Material       material  = gameObject.instance.model.materials.get(0);
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
        translation.set(marble.position);

        for (PointLight pl : pointLight) {
            pl.setPosition(translation);
        }
        turnLightOn(renderEngine);
        tuneLightIntensity();
        {
            gameObject.instance.transform.setToTranslation(translation);
//            instance.instance.transform.rotateTowardDirection(direction, Vector3.Y);
            gameObject.instance.transform.scale(marble.getSize(), marble.getSize(), marble.getSize());
            gameObject.update();
            ballObject.setWorldTransform(gameObject.instance.transform);
        }

    }

}
