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

/*
 * Created on 10.07.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import de.bushnaq.abdalla.engine.GameObject;
import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.game.model.stone.Stone;
import de.bushnaq.abdalla.pluvia.scene.AbstractScene;
import de.bushnaq.abdalla.pluvia.scene.BubblesScene;
import de.bushnaq.abdalla.pluvia.scene.MarbleScene;
import net.mgsx.gltf.scene3d.model.ModelInstanceHack;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author kunterbunt
 */
public class LevelManager extends Level implements Serializable {
    private static final int                          ANIMATION_PHASES_SPEED           = 10;
    private static final String                       INVALID_LEVEL_RECORDING_DETECTED = "Invalid Level Recording Detected";
    private static final String                       RESETTING_LEVEL                  = "\n\nResetting level...";
    private static final long                         serialVersionUID                 = 1L;
    //	protected Font				TextFont							= new Font("SansSerif", Font.BOLD, 14);
    private              StonePlane                   cubeXNegPlane;
    private              Vector3                      cubeXNegPlaneTranslation;    // intermediate value
    private              StonePlane                   cubeXPosPlane;
    private              Vector3                      cubeXPosPlaneTranslation;    // intermediate value
    private              StonePlane                   cubeYNegPlane;
    private              Vector3                      cubeYNegPlaneTranslation;    // intermediate value
    private              StonePlane                   cubeYPosPlane;
    private              Vector3                      cubeYPosPlaneTranslation;    // intermediate value
    private              StonePlane                   cubeZNegPlane;
    private              Vector3                      cubeZNegPlaneTranslation;    // intermediate value
    private              StonePlane                   cubeZPosPlane;
    private              Vector3                      cubeZPosPlaneTranslation;    // intermediate value
    private final        int                          index                            = 0;
    private              Color                        infoColor;
    private final        RenderEngine3D<GameEngine>   renderEngine;
    private final        List<GameObject<GameEngine>> renderModelInstances             = new ArrayList<>();
    private final        Map<String, AbstractScene>   sceneList                        = new HashMap<>();
    private final        Vector3                      translationBuffer                = new Vector3();

    public LevelManager(RenderEngine3D<GameEngine> renderEngine, Game game, boolean editMode) {
        super(game, editMode);
        this.renderEngine = renderEngine;
        sceneList.put(GameName.LEGACY.name(), new BubblesScene(renderEngine, renderModelInstances));
        sceneList.put(GameName.EDIT_MODE.name(), new BubblesScene(renderEngine, renderModelInstances));
//		sceneList.put(GameName.Turtle.name(), new TurtlesScene(renderEngine, renderModelInstances));
//		sceneList.put(GameName.Dragon.name(), new NightFishScene(renderEngine, renderModelInstances));
//		sceneList.put(GameName.FIVE.name(), new FlyScene(renderEngine, renderModelInstances));
//		sceneList.put(GameName.SIX.name(), new DeepSeaScene(renderEngine, renderModelInstances));
        sceneList.put(GameName.UI.name(), new MarbleScene(renderEngine, renderModelInstances));
    }

    private void addToEngine() {
        for (GameObject uberModel : renderModelInstances) {
            renderEngine.addStatic(uberModel);
        }
    }

    void createCube() {
        float xShift = ((float) xSize) / 2;
        float yShift = ((float) ySize) / 2;
        float zShift = ((float) zSize) / 2;
        {
            cubeYNegPlane            = new StonePlane(xSize, zSize);
            cubeYNegPlaneTranslation = new Vector3(0, -yShift - .1f, 0);
            for (int x = 0; x < xSize; x++) {
                for (int z = 0; z < zSize; z++) {
                    GameObject<GameEngine> go = new GameObject<GameEngine>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.transparentPlane), null);
                    go.update();
                    renderEngine.addDynamic(go);
                    cubeYNegPlane.set(x, z, go);
                }
            }
        }
        {
            cubeYPosPlane            = new StonePlane(xSize, zSize);
            cubeYPosPlaneTranslation = new Vector3(0, yShift + .1f, 0);
            for (int x = 0; x < xSize; x++) {
                for (int z = 0; z < zSize; z++) {
                    GameObject<GameEngine> go = new GameObject<GameEngine>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.transparentPlane), null);
                    go.update();
                    renderEngine.addDynamic(go);
                    cubeYPosPlane.set(x, z, go);
                }
            }
        }
        {
            cubeZNegPlane            = new StonePlane(xSize, zSize);
            cubeZNegPlaneTranslation = new Vector3(0, 0, -zShift - .1f);
            for (int x = 0; x < xSize; x++) {
                for (int z = 0; z < zSize; z++) {
                    GameObject<GameEngine> go = new GameObject<GameEngine>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.transparentPlane), null);
                    go.update();
                    renderEngine.addDynamic(go);
                    cubeZNegPlane.set(x, z, go);
                }
            }
        }
        {
            cubeZPosPlane            = new StonePlane(xSize, zSize);
            cubeZPosPlaneTranslation = new Vector3(0, 0, zShift + .1f);
            for (int x = 0; x < xSize; x++) {
                for (int z = 0; z < zSize; z++) {
                    GameObject<GameEngine> go = new GameObject<GameEngine>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.transparentPlane), null);
                    go.update();
                    renderEngine.addDynamic(go);
                    cubeZPosPlane.set(x, z, go);
                }
            }
        }
        {
            cubeXNegPlane            = new StonePlane(ySize, zSize);
            cubeXNegPlaneTranslation = new Vector3(-xShift - .1f, 0, 0);
            for (int y = 0; y < ySize; y++) {
                for (int z = 0; z < zSize; z++) {
                    GameObject<GameEngine> go = new GameObject<GameEngine>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.transparentPlane), null);
                    go.update();
                    renderEngine.addDynamic(go);
                    cubeXNegPlane.set(y, z, go);
                }
            }
        }
        {
            cubeXPosPlane            = new StonePlane(ySize, zSize);
            cubeXPosPlaneTranslation = new Vector3(xShift + .1f, 0, 0);
            for (int y = 0; y < ySize; y++) {
                for (int z = 0; z < zSize; z++) {
                    GameObject<GameEngine> go = new GameObject<GameEngine>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.transparentPlane), null);
                    go.update();
                    renderEngine.addDynamic(go);
                    cubeXPosPlane.set(y, z, go);
                }
            }
        }
    }

    public void createLevel(String levelName) {
        super.createLevel(levelName);
    }

    @Override
    public void createLevelBackground(String levelNameString) {
        if (renderEngine != null) {
//            float cubeSize = 0.5f;
//            float dx       = ((float) xSize) / 2;
//            float dy       = (ySize);
//            Model model    = renderEngine.getGameEngine().modelManager.levelCube;
            // level back
//            int count = 0;
//            if (false) {
            // back side
//		{
//			Model m;
//			m = gameEngine.modelManager.backPlate;
//			GameObject cube = new GameObject(new ModelInstanceHack(m), null);
//			cube.instance.transform.setToTranslationAndScaling(-0.5f, height / 2 + 0.75f, -0.5f, width, height - 0.5f, 1);
//			cube.instance.transform.rotate(Vector3.X, 90);
//			renderModelInstances.add(cube);
//		}
            // left side
//                for (float y = this.ySize - 0.75f; y >= preview; y -= 0.5f) {
//                    GameObject<GameEngine> cube = new GameObject<>(new ModelInstanceHack(model), null);
//                    cube.instance.transform.setToTranslationAndScaling(-0.75f - dx, -(y - dy), -0.25f, cubeSize, cubeSize, cubeSize);
//                    renderModelInstances.add(cube);
//                    count++;
//                }
            // right side
//                for (float y = this.ySize - 0.75f; y >= preview; y -= 0.5f) {
//                    GameObject<GameEngine> cube = new GameObject<>(new ModelInstanceHack(model), null);
//                    cube.instance.transform.setToTranslationAndScaling(xSize - 0.25f - dx, -(y - dy), 0 - 0.25f, cubeSize, cubeSize, cubeSize);
//                    renderModelInstances.add(cube);
//                    count++;
//                }
            // left lower side
//                for (float x = -0.75f; x <= this.xSize / 2 - 1.5f - 1f; x += 0.5f) {
//                    GameObject<GameEngine> cube = new GameObject<>(new ModelInstanceHack(model), null);
//                    cube.instance.transform.setToTranslationAndScaling(x - dx, -(ySize - dy) + 0.25f, 0 - 0.25f, cubeSize, cubeSize, cubeSize);
//                    renderModelInstances.add(cube);
//                    count++;
//                }
            // game name
//                int position = 0;
//                for (float x = -0.75f + this.xSize / 2 - 0.5f - 1f; x <= this.xSize / 2 + .75f - 1.5f; x += 0.5f) {
//                    renderEngine.getGameEngine().context.digitList.add(new Digit(renderModelInstances, renderEngine, x - dx, -(ySize - dy) + 0.25f - 0.5f, -0.25f, position, DigitType.name));
//                    position++;
//                    count++;
//                }
            // game seed
//                position = 0;
//                for (float x = this.xSize / 2 + .75f - 1.5f + 0.5f; x <= -0.75f + this.xSize / 2 + 1.5f + 1f - 0.5f; x += 0.5f) {
//                    renderEngine.getGameEngine().context.digitList.add(new Digit(renderModelInstances, renderEngine, x - dx, -(ySize - dy) + 0.25f - 0.5f, -0.25f, position, DigitType.seed));
//                    position++;
//                    count++;
//                }
            // right lower side
//                for (float x = -0.75f + this.xSize / 2 + 1.5f + 1f; x <= this.xSize; x += 0.5f) {
//                    GameObject<GameEngine> cube = new GameObject<>(new ModelInstanceHack(model), null);
//                    cube.instance.transform.setToTranslationAndScaling(x - dx, -(ySize - dy) + 0.25f, 0 - 0.25f, cubeSize, cubeSize, cubeSize);
//                    renderModelInstances.add(cube);
//                    count++;
//                }
            // score
//                position = 0;
//                for (float x = -0.75f - 2.5f; x <= -1.25; x += 0.5f) {
//                    renderEngine.getGameEngine().context.digitList.add(new Digit(renderModelInstances, renderEngine, x - dx, -(ySize - dy) + 0.25f - 0.5f, -0.25f, position, DigitType.score));
//                    position++;
//                    count++;
//                }
            // steps
//                position = 0;
//                for (float x = this.xSize + 0.25f; x <= this.xSize + 0.5 + 2f; x += 0.5f) {
//                    renderEngine.getGameEngine().context.digitList.add(new Digit(renderModelInstances, renderEngine, x - dx, -(ySize - dy) + 0.25f - 0.5f, -0.25f, position, DigitType.steps));
//                    position++;
//                    count++;
//                }
//            }
//            for (Digit digit : renderEngine.getGameEngine().context.digitList) {
//                renderEngine.addStatic(digit.get3DRenderer());
//            }
//            for (Stone stone : renderEngine.getGameEngine().context.stoneList) {
//                renderEngine.addStatic(stone.get3DRenderer());
//            }
            // logger.info(String.format("count=%d", count));
            renderEngine.removeAllEffects();
            sceneList.get(game.name).create(levelNameString);
            infoColor = sceneList.get(game.name).getInfoColor();
            addToEngine();
        }
    }

    @Override
    protected Stone createStone(int x, int y, int z, int type) {
        Stone stone = new Stone(renderEngine, x, y, z, type);
        if (renderEngine != null) {
            renderEngine.getGameEngine().context.stoneList.add(stone);
//			renderEngine.addStatic(stone.get3DRenderer());
        }
        return stone;
    }

    public void destroy() {
        disposeLevel();
        destroyCube();
    }

    private void destroy(StonePlane stonePlane) {
        if (stonePlane != null) {
            for (int x = 0; x < stonePlane.getxSize(); x++) {
                for (int y = 0; y < stonePlane.getySize(); y++) {
                    renderEngine.removeDynamic(stonePlane.get(x, y));
                }
            }
        }
    }

    private void destroyCube() {
        destroy(cubeYNegPlane);
        destroy(cubeYPosPlane);
        destroy(cubeZNegPlane);
        destroy(cubeZPosPlane);
        destroy(cubeXNegPlane);
        destroy(cubeXPosPlane);
    }

    public void destroyLevelBackground() {
        if (renderEngine != null) {
            renderEngine.removeAllText2D();
            renderEngine.removeAllStaticText3D();
            for (GameObject go : renderModelInstances) {
                renderEngine.removeStatic(go);
            }
            renderEngine.getGameEngine().context.fishList.destroy(renderEngine);
            renderEngine.getGameEngine().context.fireflyList.destroy(renderEngine);
            renderEngine.getGameEngine().context.flyList.destroy(renderEngine);
            renderEngine.getGameEngine().context.rainList.destroy(renderEngine);
            renderEngine.getGameEngine().context.bubbleList.destroy(renderEngine);
            renderEngine.getGameEngine().context.marbleList.destroy(renderEngine);
            renderEngine.getGameEngine().context.digitList.destroy(renderEngine);
            sceneList.get(game.name).destroy();
        }
    }

    @Override
    public void disposeLevel() {
        clear();
        destroyLevelBackground();
        destroyCube();
        game.reset();
//        NrOfTotalStones = 0;
    }

    public Color getInfoColor() {
        return infoColor;
    }

    public AbstractScene getScene() {
        return sceneList.get(game.name);
    }

    @Override
    protected boolean isEnableTime() {
        if (renderEngine != null)
            return renderEngine.getGameEngine().context.isEnableTime();
        else
            return true;
    }

    public Set<String> listFilesUsingFilesList(String dir) throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(dir))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public void playSound(String tag) {
        if (renderEngine != null)
            renderEngine.getGameEngine().getAudioManager().play(tag);
    }

    @Override
    protected void removeStone(Stone stone) {
        if (renderEngine != null) {
            stone.get3DRenderer().destroy(renderEngine);
            logger.info(String.format("removed stone %s", stone.name));
            renderEngine.getGameEngine().context.stoneList.remove(stone);
//			renderEngine.removeStatic(stone.get3DRenderer());
        }
    }

    public void render(final long currentTime) {
        renderCube(currentTime);
    }

    private void renderCube(final long currentTime) {
        float      xShift     = ((float) xSize) / 2;
        float      yShift     = ((float) ySize) / 2;
        float      zShift     = ((float) zSize) / 2;
        float      scale      = .9f;
        GameEngine gameEngine = renderEngine.getGameEngine();
        if (gameEngine.context.game.getySize() != 0) {
            if (cubeYNegPlane != null) {
                for (int x = 0; x < xSize; x++) {
                    for (int z = 0; z < zSize; z++) {
                        {
                            GameObject<GameEngine> go = cubeYNegPlane.get(x, z);
                            go.instance.transform.setToRotation(Vector3.Y, gameEngine.getViewAngle() + gameEngine.getRotateCubeYAngle());
                            go.instance.transform.rotate(Vector3.X, gameEngine.getRotateCubeXAngle());
                            go.instance.transform.rotate(Vector3.Z, gameEngine.getRotateCubeZAngle());
                            translationBuffer.set(cubeYNegPlaneTranslation);
                            translationBuffer.x += x - xShift + .5f;
                            translationBuffer.z += z - zShift + .5f;
                            go.instance.transform.translate(translationBuffer);
                            go.instance.transform.scale(scale, 0.1f, scale);
//                        go.instance.transform.rotate(Vector3.Z, -90);
                            go.update();
                        }
                        {
                            GameObject<GameEngine> go = cubeYPosPlane.get(x, z);
                            go.instance.transform.setToRotation(Vector3.Y, gameEngine.getViewAngle() + gameEngine.getRotateCubeYAngle());
                            go.instance.transform.rotate(Vector3.X, gameEngine.getRotateCubeXAngle());
                            go.instance.transform.rotate(Vector3.Z, gameEngine.getRotateCubeZAngle());
                            translationBuffer.set(cubeYPosPlaneTranslation);
                            translationBuffer.x += x - xShift + .5f;
                            translationBuffer.z += z - zShift + .5f;
                            go.instance.transform.translate(translationBuffer);
                            go.instance.transform.scale(scale, 0.1f, scale);
                            go.instance.transform.rotate(Vector3.Z, 180);
                            go.update();
                        }
                    }
                }
            }

            if (cubeZNegPlane != null) {
                for (int x = 0; x < xSize; x++) {
                    for (int y = 0; y < ySize; y++) {
                        {
                            GameObject<GameEngine> go = cubeZNegPlane.get(x, y);
                            go.instance.transform.setToRotation(Vector3.Y, gameEngine.getViewAngle() + gameEngine.getRotateCubeYAngle());
                            go.instance.transform.rotate(Vector3.X, gameEngine.getRotateCubeXAngle());
                            go.instance.transform.rotate(Vector3.Z, gameEngine.getRotateCubeZAngle());
                            translationBuffer.set(cubeZNegPlaneTranslation);
                            translationBuffer.x += x - xShift + .5f;
                            translationBuffer.y += y - yShift + .5f;
                            go.instance.transform.translate(translationBuffer);
                            go.instance.transform.scale(scale, scale, 0.1f);
                            go.instance.transform.rotate(Vector3.X, 90);
                            go.update();
                        }
                        {
                            GameObject<GameEngine> go = cubeZPosPlane.get(x, y);
                            go.instance.transform.setToRotation(Vector3.Y, gameEngine.getViewAngle() + gameEngine.getRotateCubeYAngle());
                            go.instance.transform.rotate(Vector3.X, gameEngine.getRotateCubeXAngle());
                            go.instance.transform.rotate(Vector3.Z, gameEngine.getRotateCubeZAngle());
                            translationBuffer.set(cubeZPosPlaneTranslation);
                            translationBuffer.x += x - xShift + .5f;
                            translationBuffer.y += y - yShift + .5f;
                            go.instance.transform.translate(translationBuffer);
                            go.instance.transform.scale(scale, scale, 0.1f);
                            go.instance.transform.rotate(Vector3.X, -90);
                            go.update();
                        }
                    }
                }
            }

            if (cubeXNegPlane != null) {
                for (int y = 0; y < ySize; y++) {
                    for (int z = 0; z < zSize; z++) {
                        {
                            GameObject<GameEngine> go = cubeXNegPlane.get(y, z);
                            go.instance.transform.setToRotation(Vector3.Y, gameEngine.getViewAngle() + gameEngine.getRotateCubeYAngle());
                            go.instance.transform.rotate(Vector3.X, gameEngine.getRotateCubeXAngle());
                            go.instance.transform.rotate(Vector3.Z, gameEngine.getRotateCubeZAngle());
                            translationBuffer.set(cubeXNegPlaneTranslation);
                            translationBuffer.y += y - yShift + .5f;
                            translationBuffer.z += z - zShift + .5f;
                            go.instance.transform.translate(translationBuffer);
                            go.instance.transform.scale(0.1f, scale, scale);
                            go.instance.transform.rotate(Vector3.Z, -90);
                            go.update();
                        }
                        {
                            GameObject<GameEngine> go = cubeXPosPlane.get(y, z);
                            go.instance.transform.setToRotation(Vector3.Y, gameEngine.getViewAngle() + gameEngine.getRotateCubeYAngle());
                            go.instance.transform.rotate(Vector3.X, gameEngine.getRotateCubeXAngle());
                            go.instance.transform.rotate(Vector3.Z, gameEngine.getRotateCubeZAngle());
                            translationBuffer.set(cubeXPosPlaneTranslation);
                            translationBuffer.y += y - yShift + .5f;
                            translationBuffer.z += z - zShift + .5f;
                            go.instance.transform.translate(translationBuffer);
                            go.instance.transform.scale(0.1f, scale, scale);
                            go.instance.transform.rotate(Vector3.Z, 90);
                            go.update();
                        }
                    }
                }
            }
        }
    }

    public boolean testValidity() {
        return getRecording().testValidity(INVALID_LEVEL_RECORDING_DETECTED, RESETTING_LEVEL, renderEngine.getGameEngine(), (Game) renderEngine.getGameEngine().context.game.clone());
    }

    public void translateLegacyLevels() {
        String legacyDirectory = "legacy-level/";
        try {
            Set<String> files = listFilesUsingFilesList(legacyDirectory);
            renderEngine.getScheduledEffectEngine().add(new TranslateLegacyLevel<>(renderEngine.getGameEngine(), new ArrayList<>(files)));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void updateFps() {
        if (renderEngine.getGameEngine().context.isEnableTime()) {
            maxAnimationPhase = Math.max(1, Gdx.graphics.getFramesPerSecond() / ANIMATION_PHASES_SPEED);
        }
    }
}