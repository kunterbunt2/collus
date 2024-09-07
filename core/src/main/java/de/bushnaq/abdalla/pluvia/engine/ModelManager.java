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

package de.bushnaq.abdalla.pluvia.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import net.mgsx.gltf.loaders.glb.GLBLoader;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRFloatAttribute;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

import java.util.Iterator;

/**
 * Loads models and textures, instantiates the SceneManager class
 *
 * @author kunterbunt
 */
public class ModelManager {
    private static final Color        DIAMON_BLUE_COLOR             = new Color(0x006ab6ff);
    private static final Color        GRAY_COLOR                    = new Color(0x404853ff);
    public static final  int          MAX_NUMBER_OF_BUBBLE_MODELS   = 10;
    public static final  int          MAX_NUMBER_OF_FIRELY_MODELS   = 10;
    public static final  int          MAX_NUMBER_OF_FLY_MODELS      = 10;
    public static final  int          MAX_NUMBER_OF_RAIN_MODELS     = 12;
    private static final int          MAX_NUMBER_OF_STONE_MODELS    = 14;
    public static final  int          MAX_NUMBER_OF_TURTLE_MODELS   = 1;
    private static final Color        POST_GREEN_COLOR              = new Color(0x00614eff);
    private static final Color        SCARLET_COLOR                 = new Color(0xb00233ff);
    public static        int          MAX_NUMBER_OF_BUILDING_MODELS = 8;
    public static        int          MAX_NUMBER_OF_FISH_MODELS     = 8;
    public               Model        backPlate;                                                                        // game grid glass background
    public               SceneAsset[] bubbleModel                   = new SceneAsset[MAX_NUMBER_OF_BUBBLE_MODELS];    // for bubbles
    public               Model[]      buildingCube                  = new Model[MAX_NUMBER_OF_BUILDING_MODELS];        // for city scene
    public               Model        cube;
    public               Model        cubeTrans1;
    public               SceneAsset[] firelyModel                   = new SceneAsset[MAX_NUMBER_OF_FIRELY_MODELS];    // for fly
    public               Model[]      fishCube                      = new Model[MAX_NUMBER_OF_FISH_MODELS];            // for fish
    public               SceneAsset[] flyModel                      = new SceneAsset[MAX_NUMBER_OF_FLY_MODELS];        // for firefly
    public               Model        levelCube;                                                                        // level edges
    public               Model        mirror;                                                                            // mirror square
    public               SceneAsset[] rainModel                     = new SceneAsset[MAX_NUMBER_OF_RAIN_MODELS];    // for firefly
    public               Model        shadow;
    public               Model        square;                                                                            // used for the ground
    public               SceneAsset[] stone                         = new SceneAsset[MAX_NUMBER_OF_STONE_MODELS];    // for stones
    public               SceneAsset[] turtleCube                    = new SceneAsset[MAX_NUMBER_OF_TURTLE_MODELS];    // for turtles
    public               Model        water;                                                                            // water square

    public ModelManager() {
    }

    public void create(boolean isPbr) throws Exception {
        final ModelBuilder modelBuilder = new ModelBuilder();
        createStoneModels(isPbr);
        createBuildingModels(isPbr, modelBuilder);
        createFishModels(isPbr, modelBuilder);
        createFireflyModels(isPbr, modelBuilder);
        createFlyModels(isPbr, modelBuilder);
        createBubbleModels(isPbr, modelBuilder);
        createTurtleModels(isPbr);
        createRainModels(isPbr, modelBuilder);
        createLevelModels(isPbr, modelBuilder);
        createWaterModel(modelBuilder);
        createMirrorModel(modelBuilder);
        createSquareModel(isPbr, modelBuilder);
        createBackPlateModel(isPbr, modelBuilder);
        createTransparentCube(modelBuilder);
        createShadowModel(modelBuilder);
        createCube(modelBuilder);
    }

    private void createBackPlateModel(boolean isPbr, final ModelBuilder modelBuilder) {
        if (isPbr) {
            final Attribute color     = new PBRColorAttribute(PBRColorAttribute.BaseColorFactor, Color.WHITE);
            final Attribute metallic  = PBRFloatAttribute.createMetallic(0.5f);
            final Attribute roughness = PBRFloatAttribute.createRoughness(0.5f);
            final Attribute blending  = new BlendingAttribute(1.0f);                                            // opacity is set by pbrMetallicRoughness below
            final Material  material  = new Material(metallic, roughness, color, blending);
            backPlate = createSquare(modelBuilder, 0.5f, 0.5f, material);
        } else {
            final ColorAttribute diffuseColor = ColorAttribute.createDiffuse(Color.WHITE);
            final Material       material     = new Material(diffuseColor);
            backPlate = createSquare(modelBuilder, 0.5f, 0.5f, material);
        }
    }

    private void createBubbleModels(boolean isPbr, final ModelBuilder modelBuilder) {
        Color[] colors = new Color[]{Color.WHITE, POST_GREEN_COLOR, SCARLET_COLOR, DIAMON_BLUE_COLOR, GRAY_COLOR, Color.CORAL, Color.RED, Color.GREEN, Color.BLUE, Color.GOLD, Color.MAGENTA, Color.YELLOW, Color.BLACK};
        if (isPbr) {
            for (int i = 0; i < MAX_NUMBER_OF_BUBBLE_MODELS; i++) {
                bubbleModel[i] = new GLBLoader().load(Gdx.files.internal(String.format(AtlasManager.getAssetsFolderName() + "/models/bubble.glb")));
                Material m = bubbleModel[i].scene.model.materials.get(0);
                m.set(PBRColorAttribute.createBaseColorFactor(colors[i]));
                final Attribute blending = new BlendingAttribute(0.03f); // opacity is set by pbrMetallicRoughness below
                m.set(blending);
            }
        } else {
            for (int i = 0; i < MAX_NUMBER_OF_BUBBLE_MODELS; i++) {

                bubbleModel[i] = new GLBLoader().load(Gdx.files.internal(String.format(AtlasManager.getAssetsFolderName() + "/models/bubble.glb")));
                removePbrNature(bubbleModel[i]);
                Material m = bubbleModel[i].scene.model.materials.get(0);
                m.set(ColorAttribute.createDiffuse(colors[i]));
                m.set(ColorAttribute.createSpecular(Color.WHITE));
                m.set(FloatAttribute.createShininess(16f));
                final Attribute blending = new BlendingAttribute(0.03f); // opacity is set by pbrMetallicRoughness below
                m.set(blending);
            }
        }
    }

    private void createBuildingModels(boolean isPbr, final ModelBuilder modelBuilder) {
        Color[] buildingColors = new Color[]{Color.WHITE, POST_GREEN_COLOR, SCARLET_COLOR, DIAMON_BLUE_COLOR, GRAY_COLOR, Color.CORAL, Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.GOLD, Color.MAGENTA, Color.YELLOW};
        if (isPbr) {
            for (int i = 0; i < MAX_NUMBER_OF_BUILDING_MODELS; i++) {
                final Attribute color     = new PBRColorAttribute(PBRColorAttribute.BaseColorFactor, buildingColors[i]);
                final Attribute metallic  = PBRFloatAttribute.createMetallic(0.5f);
                final Attribute roughness = PBRFloatAttribute.createRoughness(0.5f);
                final Material  material  = new Material(metallic, roughness, color);
                buildingCube[i] = modelBuilder.createBox(1.0f, 1.0f, 1.0f, material, Usage.Position | Usage.Normal);
            }
        } else {
            for (int i = 0; i < MAX_NUMBER_OF_BUILDING_MODELS; i++) {
                final ColorAttribute diffuseColor = ColorAttribute.createDiffuse(buildingColors[i]);
                final Material       material     = new Material(diffuseColor);
                buildingCube[i] = modelBuilder.createBox(1.0f, 1.0f, 1.0f, material, Usage.Position | Usage.Normal);
            }
        }
    }

    private void createCube(final ModelBuilder modelBuilder) {
//        final Attribute color     = new PBRColorAttribute(PBRColorAttribute.BaseColorFactor, Color.GRAY);
//        final Attribute metallic  = PBRFloatAttribute.createMetallic(0f);
//        final Attribute roughness = PBRFloatAttribute.createRoughness(1f);
//        final Material  material  = new Material(metallic, roughness, color);
////        cube = modelBuilder.createBox(1f, 1f, 1f, material, Usage.Position | Usage.Normal);
//        cube = new GLBLoader().load(Gdx.files.internal(AtlasManager.getAssetsFolderName() + "/models/stone1.glb"));
//        Material m = cube.scene.model.materials.get(0);
//        m.set(PBRColorAttribute.createBaseColorFactor(Color.WHITE));
        final Attribute color     = new PBRColorAttribute(PBRColorAttribute.BaseColorFactor, Color.DARK_GRAY);
        final Attribute metallic  = PBRFloatAttribute.createMetallic(0.5f);
        final Attribute roughness = PBRFloatAttribute.createRoughness(0.5f);
        final Attribute blending  = new BlendingAttribute(0.2f); // opacity is set by pbrMetallicRoughness below
        final Material  material  = new Material(metallic, roughness, color, blending);
        cube = createSquare(modelBuilder, 0.5f, 0.5f, material);

    }

    private void createFireflyModels(boolean isPbr, final ModelBuilder modelBuilder) {
        Color[] colors = new Color[]{Color.WHITE, POST_GREEN_COLOR, SCARLET_COLOR, DIAMON_BLUE_COLOR, GRAY_COLOR, Color.CORAL, Color.RED, Color.GREEN, Color.BLUE, Color.GOLD, Color.MAGENTA, Color.YELLOW, Color.BLACK};
        if (isPbr) {
            for (int i = 0; i < MAX_NUMBER_OF_FLY_MODELS; i++) {
                firelyModel[i] = new GLBLoader().load(Gdx.files.internal(String.format(AtlasManager.getAssetsFolderName() + "/models/firefly.glb")));
                Material m = firelyModel[i].scene.model.materials.get(0);
                m.set(PBRColorAttribute.createBaseColorFactor(colors[i]));
            }
        } else {
            for (int i = 0; i < MAX_NUMBER_OF_FLY_MODELS; i++) {
                firelyModel[i] = new GLBLoader().load(Gdx.files.internal(String.format(AtlasManager.getAssetsFolderName() + "/models/firefly.glb")));
                removePbrNature(firelyModel[i]);
                Material m = firelyModel[i].scene.model.materials.get(0);
                m.set(ColorAttribute.createDiffuse(colors[i]));
                m.set(ColorAttribute.createSpecular(Color.WHITE));
                m.set(FloatAttribute.createShininess(16f));
//				ColorAttribute	color	= ColorAttribute.createDiffuse(Color.RED);
//				Material		m		= firelyModel[i].scene.model.materials.get(0);
//				m.set(color);
            }
        }
    }

    private void createFishModels(boolean isPbr, final ModelBuilder modelBuilder) {
        Color[] colors = new Color[]{POST_GREEN_COLOR, SCARLET_COLOR, DIAMON_BLUE_COLOR, GRAY_COLOR, Color.CORAL, Color.RED, Color.GREEN, Color.BLUE, Color.GOLD, Color.MAGENTA, Color.YELLOW, Color.WHITE, Color.BLACK};
        if (isPbr) {
            for (int i = 0; i < MAX_NUMBER_OF_FISH_MODELS; i++) {
                final Attribute color     = new PBRColorAttribute(PBRColorAttribute.BaseColorFactor, colors[i]);
                final Attribute metallic  = PBRFloatAttribute.createMetallic(1.0f);
                final Attribute roughness = PBRFloatAttribute.createRoughness(0.1f);
                final Material  material  = new Material(metallic, roughness, color);
                fishCube[i] = modelBuilder.createBox(1.0f, 1.0f, 1.0f, material, Usage.Position | Usage.Normal);
            }
        } else {
            for (int i = 0; i < MAX_NUMBER_OF_FISH_MODELS; i++) {
                final ColorAttribute diffuseColor = ColorAttribute.createDiffuse(colors[i]);
                final Material       material     = new Material(diffuseColor);
                fishCube[i] = modelBuilder.createBox(1.0f, 1.0f, 1.0f, material, Usage.Position | Usage.Normal);
            }
        }
    }

    private void createFlyModels(boolean isPbr, final ModelBuilder modelBuilder) {
//		Color[] colors = new Color[] { Color.WHITE, POST_GREEN_COLOR, SCARLET_COLOR, DIAMON_BLUE_COLOR, GRAY_COLOR, Color.CORAL, Color.RED, Color.GREEN, Color.BLUE, Color.GOLD, Color.MAGENTA, Color.YELLOW, Color.BLACK };
        if (isPbr) {
            for (int i = 0; i < MAX_NUMBER_OF_FIRELY_MODELS; i++) {
                flyModel[i] = new GLBLoader().load(Gdx.files.internal(String.format(AtlasManager.getAssetsFolderName() + "/models/fly.glb")));
//				Material m = flyModelPbr[i].scene.model.materials.get(0);
//				m.set(PBRColorAttribute.createBaseColorFactor(colors[i]));
            }
        } else {
            for (int i = 0; i < MAX_NUMBER_OF_FIRELY_MODELS; i++) {

                flyModel[i] = new GLBLoader().load(Gdx.files.internal(String.format(AtlasManager.getAssetsFolderName() + "/models/fly.glb")));
                removePbrNature(flyModel[i]);
                ColorAttribute color = ColorAttribute.createDiffuse(Color.BLACK);
                Material       m     = flyModel[i].scene.model.materials.get(0);
                m.set(color);
            }
        }
    }

    private void createLevelModels(boolean isPbr, final ModelBuilder modelBuilder) {
        if (isPbr) {
            final Attribute color     = new PBRColorAttribute(PBRColorAttribute.BaseColorFactor, Color.WHITE);
            final Attribute metallic  = PBRFloatAttribute.createMetallic(0.5f);
            final Attribute roughness = PBRFloatAttribute.createRoughness(0.5f);
            final Material  material  = new Material(metallic, roughness, color);
            levelCube = modelBuilder.createBox(1f, 1f, 1f, material, Usage.Position | Usage.Normal);
        } else {
//			final ColorAttribute	diffuseColor	= ColorAttribute.createDiffuse(Color.LIGHT_GRAY);
//			final ColorAttribute	specularColor	= ColorAttribute.createSpecular(Color.LIGHT_GRAY);
//			IntAttribute			cullFace		= IntAttribute.createCullFace(1);
//			final Material			material		= new Material(diffuseColor, specularColor, cullFace);
//			levelCube = createSquare(modelBuilder, 0.5f, 0.5f, material);

            final ColorAttribute diffuseColor = ColorAttribute.createDiffuse(Color.GRAY);
            final Material       material     = new Material(diffuseColor);
            levelCube = modelBuilder.createBox(1f, 1f, 1f, material, Usage.Position | Usage.Normal);
        }
    }

    private void createMirrorModel(final ModelBuilder modelBuilder) {
        final ColorAttribute diffuseColor = ColorAttribute.createDiffuse(Color.WHITE);
//		final TextureAttribute	diffuseTexture	= TextureAttribute.createDiffuse(texture);
        final Material material = new Material(diffuseColor/* , diffuseTexture */);
        material.id = "mirror";
        mirror      = createSquare(modelBuilder, 0.5f, 0.5f, material);
    }

    private void createRainModels(boolean isPbr, final ModelBuilder modelBuilder) {
//		Color[] colors = new Color[] { Color.WHITE, POST_GREEN_COLOR, SCARLET_COLOR, DIAMON_BLUE_COLOR, GRAY_COLOR, Color.CORAL, Color.RED, Color.GREEN, Color.BLUE, Color.GOLD, Color.MAGENTA, Color.YELLOW, Color.BLACK };
        if (isPbr) {
            for (int i = 0; i < MAX_NUMBER_OF_RAIN_MODELS; i++) {
                rainModel[i] = new GLBLoader().load(Gdx.files.internal(String.format(AtlasManager.getAssetsFolderName() + "/models/rain.glb")));
                Material m = rainModel[i].scene.model.materials.get(0);
//				m.set(PBRColorAttribute.createBaseColorFactor(colors[i]));
                final Attribute blending = new BlendingAttribute(0.9f);                // opacity is set by pbrMetallicRoughness below
                m.set(blending);
            }
        } else {
            for (int i = 0; i < MAX_NUMBER_OF_RAIN_MODELS; i++) {
                rainModel[i] = new GLBLoader().load(Gdx.files.internal(String.format(AtlasManager.getAssetsFolderName() + "/models/rain.glb")));
                removePbrNature(rainModel[i]);
                Material        m        = rainModel[i].scene.model.materials.get(0);
                final Attribute blending = new BlendingAttribute(0.3f);                // opacity is set by pbrMetallicRoughness below
                m.set(blending);
            }
        }
    }

    private void createShadowModel(final ModelBuilder modelBuilder) {
        final Attribute color     = new PBRColorAttribute(PBRColorAttribute.BaseColorFactor, Color.DARK_GRAY);
        final Attribute metallic  = PBRFloatAttribute.createMetallic(0.5f);
        final Attribute roughness = PBRFloatAttribute.createRoughness(0.5f);
//        final Attribute blending  = new BlendingAttribute(0.2f); // opacity is set by pbrMetallicRoughness below
        final Material material = new Material(metallic, roughness, color);
        shadow = createSquare(modelBuilder, 0.5f, 0.5f, material);
    }

    private Model createSquare(final ModelBuilder modelBuilder, final float sx, final float sz, final Material material) {
        return modelBuilder.createRect(-sx, 0f, sz, sx, 0f, sz, sx, 0f, -sz, -sx, 0f, -sz, 0f, 1f, 0f, material, Usage.Position | Usage.Normal | Usage.TextureCoordinates);
    }

    private void createSquareModel(boolean isPbr, final ModelBuilder modelBuilder) {
        if (isPbr) {
            final Attribute color     = new PBRColorAttribute(PBRColorAttribute.BaseColorFactor, Color.WHITE);
            final Attribute metallic  = PBRFloatAttribute.createMetallic(0.5f);
            final Attribute roughness = PBRFloatAttribute.createRoughness(0.5f);
            final Material  material  = new Material(metallic, roughness, color);
            square = createSquare(modelBuilder, 0.5f, 0.5f, material);
        } else {
            final ColorAttribute diffuseColor = ColorAttribute.createDiffuse(Color.LIGHT_GRAY);
//			final ColorAttribute	specularColor	= ColorAttribute.createSpecular(Color.WHITE);
            final Material material = new Material(diffuseColor/* , specularColor */);
            square = createSquare(modelBuilder, 0.5f, 0.5f, material);
        }
    }

    private void createStoneModels(boolean isPbr) {
        Color[] colorList = {Color.NAVY, Color.RED, Color.GREEN, Color.CORAL, Color.GOLD, Color.MAGENTA, Color.YELLOW, Color.DARK_GRAY, Color.DARK_GRAY, Color.BROWN, POST_GREEN_COLOR, SCARLET_COLOR, DIAMON_BLUE_COLOR, GRAY_COLOR, Color.WHITE};


        CubeModel[] cubes = new CubeModel[]{ //
                new CubeModel(null, AtlasManager.getAssetsFolderName() + "/models/stone1.glb"), //
//				new Cube(null, AtlasManager.getAssetsFolderName() + "/models/stone2.glb"), //
//				new Cube(null, AtlasManager.getAssetsFolderName() + "/models/stone3.glb"), //
//				new Cube(null, AtlasManager.getAssetsFolderName() + "/models/stone4.glb"), //
//				new Cube(null, AtlasManager.getAssetsFolderName() + "/models/stone5.glb"), //
//				new Cube(null, AtlasManager.getAssetsFolderName() + "/models/stone6.glb"), //
//				new Cube(null, AtlasManager.getAssetsFolderName() + "/models/stone7.glb"), //
//				new Cube(null, AtlasManager.getAssetsFolderName() + "/models/stone8.glb") //
        };
        if (isPbr) {
            for (int i = 0; i < MAX_NUMBER_OF_STONE_MODELS; i++) {
                stone[i] = new GLBLoader().load(Gdx.files.internal(cubes[i % cubes.length].gltfModel));
//                if (cubes[i % cubes.length].color != null)
                {
                    Material m = stone[i].scene.model.materials.get(0);
                    m.set(PBRColorAttribute.createBaseColorFactor(colorList[i]));
                }
            }
        } else {
            for (int i = 0; i < MAX_NUMBER_OF_STONE_MODELS; i++) {
                stone[i] = new GLBLoader().load(Gdx.files.internal(cubes[i % cubes.length].gltfModel));
                removePbrNature(stone[i]);
                for (Material m : stone[i].scene.model.materials) {
                    if (m.id.equals("Frame")) {
                        m.set(ColorAttribute.createSpecular(Color.WHITE));
                    }
                    if (m.id.equals("Faces")) {
                        ColorAttribute attribute = (ColorAttribute) m.get(ColorAttribute.Diffuse);
                        m.set(ColorAttribute.createSpecular(attribute.color));
                    }
                }
            }
        }
    }

    private void createTransparentCube(ModelBuilder modelBuilder) {
        {
            final Attribute metallic  = PBRFloatAttribute.createMetallic(0.0f);
            final Attribute roughness = PBRFloatAttribute.createRoughness(0.9f);
            final Attribute color     = new PBRColorAttribute(PBRColorAttribute.BaseColorFactor, Color.WHITE);
            final Attribute blending  = new BlendingAttribute(0.04f); // opacity is set by pbrMetallicRoughness below
            final Material  material  = new Material(metallic, roughness, color, blending);
            cubeTrans1 = modelBuilder.createBox(1.0f, 1.0f, 1.0f, material, Usage.Position | Usage.Normal);
        }
    }

    private void createTurtleModels(boolean isPbr) {
        if (isPbr) {
            for (int i = 0; i < MAX_NUMBER_OF_TURTLE_MODELS; i++) {
                turtleCube[i] = new GLBLoader().load(Gdx.files.internal(String.format(AtlasManager.getAssetsFolderName() + "/models/turtle.glb")));
//				Material m = turtleCubePbr[i].scene.model.materials.get(0);
//				m.set(PBRColorAttribute.createBaseColorFactor(Color.BLACK));
            }
        } else {
            for (int i = 0; i < MAX_NUMBER_OF_TURTLE_MODELS; i++) {
                turtleCube[i] = new GLBLoader().load(Gdx.files.internal(String.format(AtlasManager.getAssetsFolderName() + "/models/turtle.glb")));
                removePbrNature(turtleCube[i]);
//				Material			m	= turtleCube[i].scene.model.materials.get(0);
//				PBRColorAttribute	ca	= (PBRColorAttribute) m.get(PBRColorAttribute.BaseColorFactor);
//				m.set(ColorAttribute.createDiffuse(ca.color));
            }
        }
    }

    private void createWaterModel(final ModelBuilder modelBuilder) {
        final ColorAttribute diffuseColor = ColorAttribute.createDiffuse(Color.WHITE);
//		final TextureAttribute	diffuseTexture	= TextureAttribute.createDiffuse(texture);
        final Material material = new Material(diffuseColor/* , diffuseTexture */);
        material.id = "water";
        water       = createSquare(modelBuilder, 0.5f, 0.5f, material);
    }

    private void removePbrNature(SceneAsset sceneAsset) {
        for (Material m : sceneAsset.scene.model.materials) {
            PBRColorAttribute ca = (PBRColorAttribute) m.get(PBRColorAttribute.BaseColorFactor);
            m.set(ColorAttribute.createDiffuse(ca.color));
            m.remove(PBRColorAttribute.BaseColorFactor);
            m.remove(PBRFloatAttribute.Metallic);
            m.remove(PBRFloatAttribute.Roughness);
        }
    }

    void set(final Material material1, final Material material2) {
        final Iterator<Attribute> i = material1.iterator();
        material2.clear();
        while (i.hasNext()) {
            final Attribute a = i.next();
            material2.set(a);
        }
    }

}
