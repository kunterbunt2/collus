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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import de.bushnaq.abdalla.engine.GameObject;
import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.engine.Text2D;
import de.bushnaq.abdalla.engine.physics.PhysicsEngine;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.engine.ModelManager;
import de.bushnaq.abdalla.pluvia.scene.model.fish.Fish;
import de.bushnaq.abdalla.pluvia.scene.model.marble.Marble;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRFloatAttribute;
import net.mgsx.gltf.scene3d.model.ModelInstanceHack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static de.bushnaq.abdalla.pluvia.engine.ModelManager.MAX_NUMBER_OF_MARBLE_MODELS;

/**
 * @author kunterbunt
 */
public abstract class AbstractScene {
    protected static final float CITY_SIZE    = 3;
    public static final    float MIRROR_LEVEL = -4.949f;
    private static final   float WATER_X      = 1000;
    private static final   float WATER_Y      = -3.5f;
    private static final   float WATER_Z      = 1000;
    GameObject<GameEngine> boundariesXNegGameObject;
    GameObject<GameEngine> boundariesXPosGameObject;
    GameObject<GameEngine> boundariesZNegGameObject;
    GameObject<GameEngine> boundariesZPosGameObject;
    public    btRigidBody.btRigidBodyConstructionInfo constructionInfo;
    protected int                                     index  = 0;
    protected Text2D                                  levelName;
    protected Logger                                  logger = LoggerFactory.getLogger(this.getClass());
    protected Text2D                                  logo;
    public    MyMotionState                           motionState;
    GameObject<GameEngine> planeGameObject;
    protected Random                       rand;
    protected RenderEngine3D<GameEngine>   renderEngine;
    protected List<GameObject<GameEngine>> renderModelInstances;
    BoundingBox sceneBoundingBox;
    btBoxShape  shape1;
    btBoxShape  shape2;
    btBoxShape  shape3;
    btBoxShape  shape4;
    protected Text2D version;

    public AbstractScene(RenderEngine3D<GameEngine> renderEngine, List<GameObject<GameEngine>> renderModelInstances) {
        this.renderEngine         = renderEngine;
        this.renderModelInstances = renderModelInstances;
        this.rand                 = new Random(System.currentTimeMillis());
    }

    public void create(String levelNameString) {
        logo = new Text2D("Collus", 100, Gdx.graphics.getHeight() - 200, Color.RED, renderEngine.getGameEngine().getAtlasManager().logoFont);
        renderEngine.add(logo);
        try {
            String            v      = renderEngine.getGameEngine().context.getAppVersion();
            final GlyphLayout layout = new GlyphLayout();
            layout.setText(renderEngine.getGameEngine().getAtlasManager().logoFont, "Collus");
            float h1 = layout.height;
            layout.setText(renderEngine.getGameEngine().getAtlasManager().versionFont, v);
            float h2 = layout.height;
            version = new Text2D(v, 400 + 30, Gdx.graphics.getHeight() - 200 - (int) (h1 - h2), Color.GREEN, renderEngine.getGameEngine().getAtlasManager().versionFont);
            renderEngine.add(version);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        levelName = new Text2D(levelNameString, 100, Gdx.graphics.getHeight() - 300, Color.RED, renderEngine.getGameEngine().getAtlasManager().modelFont);
        renderEngine.add(levelName);
    }

    protected void createCity(final RenderEngine3D<GameEngine> renderEngine, final float x, final float y, final float z, boolean up, float ScaleY) {
        final int   maxIteration          = 27;
        final int   iteration             = maxIteration;
        final float scaleX                = (CITY_SIZE * 5) / (iteration - 1);
        final float scaleY                = (ScaleY) / (iteration - 1);
        final float scaleZ                = (CITY_SIZE * 5) / (iteration - 1);
        final float averrageBuildingHight = 5f;
        createCity(renderEngine, x, y, z, iteration, maxIteration, scaleX, scaleY, scaleZ, averrageBuildingHight, up);
    }

    private void createCity(final RenderEngine3D<GameEngine> renderEngine, final float x, final float y, final float z, int iteration, int maxIteration, final float scaleX, final float scaleY, final float scaleZ, float averrageBuildingHight, boolean up) {
        // we are responsible for the 4 corners
        final float screetSize = 0.02f;
        iteration /= 2;
        // System.out.println(String.format("iteration=%d scale=%f x=%f z=%f", iteration, scale, x, z));
        // int i = 0;
        averrageBuildingHight = averrageBuildingHight + averrageBuildingHight * (0.5f - rand.nextFloat());
        final TwinBuilding[][] twinChances = {{new TwinBuilding(0.3f, 0.0f, 1, 1)/* 0,0 */, new TwinBuilding(0.0f, 0.3f, 1, -1)}/* 0,1 */, {new TwinBuilding(0.0f, 0.3f, -1, 1)/* 1,0 */, new TwinBuilding(0.3f, 0.0f, -1, -1)}/* 1,1 */};
        // z=1, x=1, x=0
        // 0,0
        // 0,1
        // 1,0
        // 1,1

        for (int xi = 0; xi < 2; xi++) {
            for (int zi = 0; zi < 2; zi++) {
                final TwinBuilding twinChance = twinChances[xi][zi];
                if (!twinChance.occupided) {
                    twinChance.occupided = true;
                    float xx = x + (xi * 2 - 1) * iteration * scaleX;
                    float zz = z + (zi * 2 - 1) * iteration * scaleZ;
                    // the bigger the block, the lower the change for it to be one building
                    final float changceOfOneBuilding = 0.5f / iteration;
                    final float changceOfNoBuilding  = 0.0f / iteration;
                    if (rand.nextFloat() < changceOfNoBuilding) {

                    } else if (iteration > 1f && rand.nextFloat() > changceOfOneBuilding)
                        // create smaller buildings
                        createCity(renderEngine, xx, y, zz, iteration, maxIteration, scaleX, scaleY, scaleZ, averrageBuildingHight, up);
                    else {
                        float twinFactorXs = 1f;
                        float twinFactorZs = 1f;
                        if (rand.nextFloat() < twinChance.chanceHorizontal) {
                            final TwinBuilding twin = twinChances[xi + twinChance.deltaX][zi];
                            if (!twin.occupided) {
                                twinFactorXs   = 2f;
                                xx             = x;
                                twin.occupided = true;
                            }
                        }
                        if (rand.nextFloat() < twinChance.chanceVertical) {
                            final TwinBuilding twin = twinChances[xi][zi + twinChance.deltaZ];
                            if (!twin.occupided) {
                                twinFactorZs   = 2f;
                                zz             = z;
                                twin.occupided = true;
                            }
                        }

                        final GameObject<GameEngine> inst = instanciateBuilding(renderEngine, index++);
                        final float                  xs   = iteration * scaleX * twinFactorXs - screetSize;
                        // the bigger the building, the lower the change for it to get big
                        final float ys = (maxIteration + 1 - iteration) * scaleY /* ;averrageBuildingHight */ * (0.1f + 3 * rand.nextFloat());
                        final float zs = iteration * scaleZ * twinFactorZs - screetSize;
                        // System.out.println(String.format(" xx=%f zz=%f xs=%f", xx, zz, xs));
                        if (up) inst.instance.transform.setToTranslationAndScaling(xx, y /* + ys / 2 + 0.1f */, zz, xs, ys, zs);
                        else inst.instance.transform.setToTranslationAndScaling(xx, y - ys / 2 - 0.1f, zz, xs, ys, zs);
                        inst.update();
//						gameEngine.renderEngine.addStatic(inst);
                        renderModelInstances.add(inst);
                    }
                }
                // i++;
            }
        }
    }

    private Model createCube(float x, float y, float z) {
        final ModelBuilder modelBuilder = new ModelBuilder();
        final Attribute    color        = new PBRColorAttribute(PBRColorAttribute.BaseColorFactor, Color.WHITE);
        final Attribute    metallic     = PBRFloatAttribute.createMetallic(0.2f);
        final Attribute    roughness    = PBRFloatAttribute.createRoughness(0.5f);
        final Material     material     = new Material(metallic, roughness, color);
        return modelBuilder.createBox(x, y, z, material, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    }

    protected void createFish(float minSize, float maxSize) {
        Vector3     min = renderEngine.getSceneBox().min;
        Vector3     max = renderEngine.getSceneBox().max;
        BoundingBox b   = new BoundingBox(new Vector3(min.x, -5f, min.z), new Vector3(max.x, 0f, 0));
        for (int i = 0; i < Math.min(renderEngine.getGameEngine().context.getMaxSceneObjects(), 100); i++) {
            int   type = rand.nextInt(ModelManager.MAX_NUMBER_OF_FISH_MODELS);
            float size = minSize + (float) Math.random() * (maxSize - minSize);
            Fish  fish = new Fish(renderEngine, type, size, b);
            renderEngine.getGameEngine().context.fishList.add(fish);
        }
    }

    protected void createMarbles(float minSize, float maxSize, float planeLevel) {
        Vector3 min = renderEngine.getSceneBox().min;
        Vector3 max = renderEngine.getSceneBox().max;
        sceneBoundingBox      = new BoundingBox(new Vector3(min.x - 30, planeLevel, min.z - 30), new Vector3(max.x + 30, planeLevel + maxSize, 5));
        this.constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(0f, null, null, new Vector3(0, 0, 0));
//        {
//            Matrix4 m = new Matrix4();
//            m.setToTranslation(boundingBox.getCenterX(), boundingBox.getCenterY() - 0.5f, boundingBox.getCenterZ());
//
//            planeGameObject = new GameObject<>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.cube), null);
//            planeGameObject.instance.transform.set(m);
//            renderEngine.addDynamic(planeGameObject);
//            planeGameObject.update();
//
//            shape1                = new btBoxShape(new Vector3(boundingBox.getWidth() / 2 - .1f, .9f / 2, boundingBox.getDepth() / 2 - .1f));
//            this.constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(0f, null, shape1, new Vector3(0, 0, 0));
//            motionState           = new MyMotionState();
//            motionState.transform = planeGameObject.instance.transform;
//
//            planeGameObject.body = new btRigidBody(constructionInfo);
//            planeGameObject.body.setCollisionShape(shape1);
//            planeGameObject.body.setMotionState(motionState);
//            renderEngine.physicsEngine.add(planeGameObject, planeGameObject.body, PhysicsEngine.MARBLE_FLAG, PhysicsEngine.ALL_FLAG);
//
//        }
        {
            Matrix4 m = new Matrix4();
            m.setToTranslation(sceneBoundingBox.getCenterX(), sceneBoundingBox.getCenterY() - .6f, sceneBoundingBox.getCenterZ() - sceneBoundingBox.getDepth() / 2 - .5f);
            shape1 = new btBoxShape(new Vector3(sceneBoundingBox.getWidth() / 2, .9f / 2, .1f));

            boundariesZNegGameObject = new GameObject<>(new ModelInstanceHack(createCube(sceneBoundingBox.getWidth(), 1, .1f)), shape1);
            boundariesZNegGameObject.instance.transform.set(m);
            renderEngine.addDynamic(boundariesZNegGameObject);
            boundariesZNegGameObject.update();

            this.constructionInfo.setMass(0);
            this.constructionInfo.setCollisionShape(shape1);
            motionState           = new MyMotionState();
            motionState.transform = boundariesZNegGameObject.instance.transform;

            boundariesZNegGameObject.body = new btRigidBody(constructionInfo);
            boundariesZNegGameObject.body.setCollisionShape(shape1);
            boundariesZNegGameObject.body.setMotionState(motionState);
            renderEngine.physicsEngine.add(boundariesZNegGameObject, boundariesZNegGameObject.body, PhysicsEngine.MARBLE_FLAG, PhysicsEngine.ALL_FLAG);
        }
        {
            Matrix4 m = new Matrix4();
            m.setToTranslation(sceneBoundingBox.getCenterX(), sceneBoundingBox.getCenterY() - .6f, sceneBoundingBox.getCenterZ() + sceneBoundingBox.getDepth() / 2 + .5f);
            shape2 = new btBoxShape(new Vector3(sceneBoundingBox.getWidth() / 2 - .1f, .9f / 2, .1f));

            boundariesZPosGameObject = new GameObject<>(new ModelInstanceHack(createCube(sceneBoundingBox.getWidth(), 1, .1f)), shape2);
            boundariesZPosGameObject.instance.transform.set(m);
            renderEngine.addDynamic(boundariesZPosGameObject);
            boundariesZPosGameObject.update();

            this.constructionInfo.setMass(0);
            this.constructionInfo.setCollisionShape(shape2);
            motionState           = new MyMotionState();
            motionState.transform = boundariesZPosGameObject.instance.transform;

            boundariesZPosGameObject.body = new btRigidBody(constructionInfo);
            boundariesZPosGameObject.body.setCollisionShape(shape2);
            boundariesZPosGameObject.body.setMotionState(motionState);
            renderEngine.physicsEngine.add(boundariesZPosGameObject, boundariesZPosGameObject.body, PhysicsEngine.MARBLE_FLAG, PhysicsEngine.ALL_FLAG);
        }
        {
            Matrix4 m = new Matrix4();
            m.setToTranslation(sceneBoundingBox.getCenterX() - sceneBoundingBox.getWidth() / 2 - .5f, sceneBoundingBox.getCenterY() - .6f, sceneBoundingBox.getCenterZ());
            shape3 = new btBoxShape(new Vector3(.1f, .45f, sceneBoundingBox.getDepth() / 2));

            boundariesXPosGameObject = new GameObject<>(new ModelInstanceHack(createCube(.1f, 1, sceneBoundingBox.getDepth())), shape3);
            boundariesXPosGameObject.instance.transform.set(m);
            renderEngine.addDynamic(boundariesXPosGameObject);
            boundariesXPosGameObject.update();

            this.constructionInfo.setMass(0);
            this.constructionInfo.setCollisionShape(shape3);
            motionState           = new MyMotionState();
            motionState.transform = boundariesXPosGameObject.instance.transform;

            boundariesXPosGameObject.body = new btRigidBody(constructionInfo);
            boundariesXPosGameObject.body.setCollisionShape(shape3);
            boundariesXPosGameObject.body.setMotionState(motionState);
            renderEngine.physicsEngine.add(boundariesXPosGameObject, boundariesXPosGameObject.body, PhysicsEngine.MARBLE_FLAG, PhysicsEngine.ALL_FLAG);
        }
        {
            Matrix4 m = new Matrix4();
            m.setToTranslation(sceneBoundingBox.getCenterX() + sceneBoundingBox.getWidth() / 2 + .5f, sceneBoundingBox.getCenterY() - .6f, sceneBoundingBox.getCenterZ());
            shape4 = new btBoxShape(new Vector3(.1f, .45f, sceneBoundingBox.getDepth() / 2));

            boundariesXNegGameObject = new GameObject<>(new ModelInstanceHack(createCube(.1f, 1, sceneBoundingBox.getDepth())), shape4);
            boundariesXNegGameObject.instance.transform.set(m);
            renderEngine.addDynamic(boundariesXNegGameObject);
            boundariesXNegGameObject.update();

            this.constructionInfo.setMass(0);
            this.constructionInfo.setCollisionShape(shape4);
            motionState           = new MyMotionState();
            motionState.transform = boundariesXNegGameObject.instance.transform;

            boundariesXNegGameObject.body = new btRigidBody(constructionInfo);
            boundariesXNegGameObject.body.setCollisionShape(shape4);
            boundariesXNegGameObject.body.setMotionState(motionState);
            renderEngine.physicsEngine.add(boundariesXNegGameObject, boundariesXNegGameObject.body, PhysicsEngine.MARBLE_FLAG, PhysicsEngine.ALL_FLAG);
        }


        List<Marble> marbleList = new ArrayList<>();
        for (int i = 0; i < Math.max(renderEngine.getGameEngine().context.getMaxSceneObjects(), 10); i++) {
            int         type   = i % MAX_NUMBER_OF_MARBLE_MODELS/*rand.nextInt(MAX_NUMBER_OF_MARBLE_MODELS)*/;
            float       size   = minSize + (float) Math.random() * (maxSize - minSize);
            BoundingBox b      = new BoundingBox(new Vector3(min.x, planeLevel + size / 2, min.z), new Vector3(max.x, planeLevel + size / 2, 0));
            Marble      marble = new Marble(renderEngine, type, size, b, marbleList);
            marbleList.add(marble);
            renderEngine.getGameEngine().context.marbleList.add(marble);
        }
        constructionInfo.dispose();
    }

    protected void createMirror(Color color) {
        if (renderEngine.isMirrorPresent()) {
            Model model;
            model = renderEngine.getGameEngine().modelManager.mirror;
            GameObject<GameEngine> cube = new GameObject<>(new ModelInstanceHack(model), null);
            cube.instance.materials.get(0).set(ColorAttribute.createDiffuse(color));
            Vector3 min = new Vector3();
            sceneBoundingBox.getMin(min);
            Vector3 max = new Vector3();
            sceneBoundingBox.getMax(max);
            cube.instance.transform.setToTranslationAndScaling(0f, renderEngine.getMirror().getMirrorLevel(), min.z + (max.z - min.z) / 2, max.x - min.x + 1, 0.1f, max.z - min.z + 1);
            cube.update();
            renderModelInstances.add(cube);
        }
    }

    protected void createPlane(Color color) {
        Model model;
        model = renderEngine.getGameEngine().modelManager.square;
        GameObject<GameEngine> cube = new GameObject<>(new ModelInstanceHack(model), null);
        Vector3                min  = new Vector3();
        sceneBoundingBox.getMin(min);
        Vector3 max = new Vector3();
        sceneBoundingBox.getMax(max);
        Material m = cube.instance.materials.get(0);
        m.set(PBRColorAttribute.createBaseColorFactor(color));
        cube.instance.transform.setToTranslationAndScaling(0f, renderEngine.getMirror().getMirrorLevel(), min.z + (max.z - min.z) / 2, max.x - min.x + 1, 0.1f, max.z - min.z + 1);
        cube.update();
        renderModelInstances.add(cube);
    }

    protected void createWater() {
        // water
        if (renderEngine.isWaterPresent()) {
            final GameObject<GameEngine> water = new GameObject<>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.water), null);
            Vector3                      min   = new Vector3();
            sceneBoundingBox.getMin(min);
            Vector3 max = new Vector3();
            sceneBoundingBox.getMax(max);
            water.instance.transform.setToTranslationAndScaling(0, WATER_Y, min.z + (max.z - min.z) / 2, max.x - min.x, 1f, max.z - min.z);
            water.update();
            renderModelInstances.add(water);
            // plane below the water
            {
                Model model;
                model = renderEngine.getGameEngine().modelManager.square;
                GameObject<GameEngine> cube = new GameObject<>(new ModelInstanceHack(model), null);
                if (renderEngine.isPbr()) {
                    cube.instance.materials.get(0).set(PBRColorAttribute.createBaseColorFactor(Color.GREEN));
                } else {
                    cube.instance.materials.get(0).set(ColorAttribute.createDiffuse(Color.GREEN));
                }
                cube.instance.transform.setToTranslationAndScaling(0f, -30f, min.z + (max.z - min.z) / 2, max.x - min.x, 0.1f, max.z - min.z);
                cube.update();
                renderModelInstances.add(cube);
            }
        }
    }

    public void destroy() {
        destroy(boundariesXNegGameObject);
        boundariesXNegGameObject = null;
        destroy(boundariesXPosGameObject);
        boundariesXPosGameObject = null;
        destroy(boundariesZNegGameObject);
        boundariesZNegGameObject = null;
        destroy(boundariesZPosGameObject);
        boundariesZPosGameObject = null;
    }

    private void destroy(GameObject<GameEngine> gameObject) {
        if (gameObject != null) {
            renderEngine.removeDynamic(gameObject);
            renderEngine.physicsEngine.remove(gameObject, gameObject.body);
            gameObject.body.getMotionState().dispose();
            gameObject.body.getCollisionShape().dispose();
            gameObject.body.dispose();
        }
    }

    public abstract Color getInfoColor();

    public BoundingBox getSceneBoundingBox() {
        return sceneBoundingBox;
    }

    private GameObject<GameEngine> instanciateBuilding(final RenderEngine3D<GameEngine> renderEngine, final int index) {
//		int i = rand.nextInt(ModelManager.MAX_NUMBER_OF_BUILDING_MODELS);
        final GameObject<GameEngine> go = new GameObject<>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.buildingCube[(int) (Math.random() * ModelManager.MAX_NUMBER_OF_BUILDING_MODELS)]), null);
        Material                     m  = go.instance.model.materials.get(0);
        if (renderEngine.isPbr()) {
            m.set(PBRColorAttribute.createBaseColorFactor(Color.BLACK));
            return go;
        } else {
            m.set(ColorAttribute.createDiffuse(Color.GRAY));
            return go;
        }

    }

    public void setLevelName(String levelName) {
        if (this.levelName != null) this.levelName.setText(levelName);
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
