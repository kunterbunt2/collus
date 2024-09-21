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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import de.bushnaq.abdalla.engine.GameObject;
import de.bushnaq.abdalla.engine.ObjectRenderer;
import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.engine.physics.PhysicsEngine;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.model.ModelInstanceHack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kunterbunt
 */
public class Marble3DRenderer extends ObjectRenderer<GameEngine> {
    private static final Color                                   DIAMON_BLUE_COLOR      = new Color(0x006ab6ff);
    private static final Color                                   GRAY_COLOR             = new Color(0x404853ff);
    private static final float                                   NORMAL_LIGHT_INTENSITY = 2f;
    private static final Color                                   POST_GREEN_COLOR       = new Color(0x00614eff);
    private static final Color                                   SCARLET_COLOR          = new Color(0xb00233ff);
    private static final Vector3                                 localInertia           = new Vector3();
    public               btRigidBody.btRigidBodyConstructionInfo constructionInfo;
    private              GameObject<GameEngine>                  gameObject;
    private              float                                   lightIntensity         = 0f;
    private              boolean                                 lightIsOne             = false;
    protected            Logger                                  logger                 = LoggerFactory.getLogger(this.getClass());
    private final        Marble                                  marble;
    private final        float                                   mass                   = 1f;
    public               MyMotionState                           motionState;
    Vector3 negative = new Vector3(-1, -1, -1);
    private final List<PointLight> pointLight  = new ArrayList<>();
    private       btSphereShape    shape;
    private final Vector3          translation = new Vector3();        // intermediate value

    public Marble3DRenderer(final Marble patch) {
        this.marble = patch;
    }

    @Override
    public void create(final RenderEngine3D<GameEngine> renderEngine) {
        if (gameObject == null) {
            gameObject = new GameObject<>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.marbleModel[marble.getType()].scene.model), marble);
            renderEngine.addDynamic(gameObject);
//            gameObject.instance.transform.setToTranslation(0, 1, -5f);
            gameObject.update();
            shape = new btSphereShape(marble.getSize() / 2);
            if (mass > 0f)
                shape.calculateLocalInertia(mass, localInertia);
            else
                localInertia.set(0, 0, 0);
            this.constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, shape, localInertia);
//            constructionInfo.setFriction(.01f);
            motionState           = new MyMotionState();
            motionState.transform = gameObject.instance.transform;

            gameObject.body = new btRigidBody(constructionInfo);
//            gameObject.body.applyImpulse(new Vector3(-7, 0, 0), new Vector3(0, 0, 0));
            gameObject.body.setCollisionShape(shape);
            gameObject.body.setMotionState(motionState);
//            gameObject.body.setLinearFactor(new Vector3(1f, 0f, 1f));
            renderEngine.physicsEngine.add(gameObject, gameObject.body, PhysicsEngine.MARBLE_FLAG, PhysicsEngine.ALL_FLAG);
        }
    }

    @Override
    public void destroy(final RenderEngine3D<GameEngine> renderEngine) {
        renderEngine.removeDynamic(gameObject);
        for (PointLight pl : pointLight) {
            renderEngine.remove(pl, true);
        }
        renderEngine.physicsEngine.remove(gameObject, gameObject.body);
        gameObject.body.dispose();
        motionState.dispose();
        shape.dispose();
        constructionInfo.dispose();
    }

    public boolean onContactAdded(Object o0, Object o1) {
//        GameObject go0 = (GameObject) o0;
        GameObject go1 = (GameObject) o1;
        if (go1.interactive instanceof Marble m) {
//            logger.info(String.format("contact %d - %d", marble.type, m.type));
            Vector3 buffer = marble.speed.cpy();
            marble.speed.set(m.speed);
            m.speed.set(buffer);
        } else if (go1.interactive instanceof btBoxShape shape) {
            //a marble hit a wall
            Vector3 extend = shape.getHalfExtentsWithMargin();
            if (extend.x > extend.z) {
                marble.speed.z *= -1;
            } else
                marble.speed.x *= -1;
        }
        return true;
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
            gameObject.body.setWorldTransform(gameObject.instance.transform);
        }

    }

    static class MyMotionState extends btMotionState {
        Matrix4 transform;

        @Override
        public void getWorldTransform(Matrix4 worldTrans) {
            worldTrans.set(transform);
        }

        @Override
        public void setWorldTransform(Matrix4 worldTrans) {
            transform.set(worldTrans);
        }
    }

}
