/*
 * Created on 10.07.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import de.bushnaq.abdalla.engine.GameObject;
import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.game.model.stone.Stone;
import de.bushnaq.abdalla.pluvia.scene.AbstractScene;
import de.bushnaq.abdalla.pluvia.scene.BubblesScene;
import de.bushnaq.abdalla.pluvia.scene.RainScene;
import de.bushnaq.abdalla.pluvia.scene.model.digit.Digit;
import de.bushnaq.abdalla.pluvia.scene.model.digit.DigitType;
import net.mgsx.gltf.scene3d.model.ModelInstanceHack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kunterbunt
 */
public class LevelManager extends Level implements Serializable {
    private static final String                       INVALID_LEVEL_RECORDING_DETECTED = "Invalid Level Recording Detected";
    private static final String                       RESETTING_LEVEL                  = "\n\nResetting level...";
    private static final long                         serialVersionUID                 = 1L;
    private              RenderEngine3D<GameEngine>   renderEngine;
    private              int                          index                            = 0;
    private              Color                        infoColor;
    private final        List<GameObject<GameEngine>> renderModelInstances             = new ArrayList<>();
    private              Map<String, AbstractScene>   sceneList                        = new HashMap<>();
//	protected Font				TextFont							= new Font("SansSerif", Font.BOLD, 14);

    public LevelManager(RenderEngine3D<GameEngine> renderEngine, Game game) {
        super(game);
        this.renderEngine = renderEngine;
        sceneList.put(GameName.SMALL.name(), new BubblesScene(renderEngine, renderModelInstances));
//		sceneList.put(GameName.Rabbit.name(), new FireflyScene(renderEngine, renderModelInstances));
//		sceneList.put(GameName.Turtle.name(), new TurtlesScene(renderEngine, renderModelInstances));
//		sceneList.put(GameName.Dragon.name(), new NightFishScene(renderEngine, renderModelInstances));
//		sceneList.put(GameName.FIVE.name(), new FlyScene(renderEngine, renderModelInstances));
//		sceneList.put(GameName.SIX.name(), new DeepSeaScene(renderEngine, renderModelInstances));
        sceneList.put(GameName.UI.name(), new RainScene(renderEngine, renderModelInstances));
    }

    private void addToEngine() {
        for (GameObject uberModel : renderModelInstances) {
            renderEngine.addStatic(uberModel);
        }
    }

    @Override
    public void createLevelBackground() {
        if (renderEngine != null) {
            float cubeSize = 0.5f;
            float dx       = ((float) xSize) / 2;
            float dy       = (ySize);
            Model model    = renderEngine.getGameEngine().modelManager.levelCube;
            // level back
            int count = 0;
            if (game.ySize != 0 && false) {
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
                for (float y = this.ySize - 0.75f; y >= preview; y -= 0.5f) {
                    GameObject cube = new GameObject(new ModelInstanceHack(model), null);
                    cube.instance.transform.setToTranslationAndScaling(-0.75f - dx, -(y - dy), -0.25f, cubeSize, cubeSize, cubeSize);
                    renderModelInstances.add(cube);
                    count++;
                }
                // right side
                for (float y = this.ySize - 0.75f; y >= preview; y -= 0.5f) {
                    GameObject cube = new GameObject(new ModelInstanceHack(model), null);
                    cube.instance.transform.setToTranslationAndScaling(xSize - 0.25f - dx, -(y - dy), 0 - 0.25f, cubeSize, cubeSize, cubeSize);
                    renderModelInstances.add(cube);
                    count++;
                }
                // left lower side
                for (float x = -0.75f; x <= this.xSize / 2 - 1.5f - 1f; x += 0.5f) {
                    GameObject cube = new GameObject(new ModelInstanceHack(model), null);
                    cube.instance.transform.setToTranslationAndScaling(x - dx, -(ySize - dy) + 0.25f, 0 - 0.25f, cubeSize, cubeSize, cubeSize);
                    renderModelInstances.add(cube);
                    count++;
                }
                // game name
                int position = 0;
                for (float x = -0.75f + this.xSize / 2 - 0.5f - 1f; x <= this.xSize / 2 + .75f - 1.5f; x += 0.5f) {
                    renderEngine.getGameEngine().context.digitList.add(new Digit(renderModelInstances, renderEngine, x - dx, -(ySize - dy) + 0.25f - 0.5f, -0.25f, position, DigitType.name));
                    position++;
                    count++;
                }
                // game seed
                position = 0;
                for (float x = this.xSize / 2 + .75f - 1.5f + 0.5f; x <= -0.75f + this.xSize / 2 + 1.5f + 1f - 0.5f; x += 0.5f) {
                    renderEngine.getGameEngine().context.digitList.add(new Digit(renderModelInstances, renderEngine, x - dx, -(ySize - dy) + 0.25f - 0.5f, -0.25f, position, DigitType.seed));
                    position++;
                    count++;
                }
                // right lower side
                for (float x = -0.75f + this.xSize / 2 + 1.5f + 1f; x <= this.xSize; x += 0.5f) {
                    GameObject cube = new GameObject(new ModelInstanceHack(model), null);
                    cube.instance.transform.setToTranslationAndScaling(x - dx, -(ySize - dy) + 0.25f, 0 - 0.25f, cubeSize, cubeSize, cubeSize);
                    renderModelInstances.add(cube);
                    count++;
                }
                // score
                position = 0;
                for (float x = -0.75f - 2.5f; x <= -1.25; x += 0.5f) {
                    renderEngine.getGameEngine().context.digitList.add(new Digit(renderModelInstances, renderEngine, x - dx, -(ySize - dy) + 0.25f - 0.5f, -0.25f, position, DigitType.score));
                    position++;
                    count++;
                }
                // steps
                position = 0;
                for (float x = this.xSize + 0.25f; x <= this.xSize + 0.5 + 2f; x += 0.5f) {
                    renderEngine.getGameEngine().context.digitList.add(new Digit(renderModelInstances, renderEngine, x - dx, -(ySize - dy) + 0.25f - 0.5f, -0.25f, position, DigitType.steps));
                    position++;
                    count++;
                }
            }
            for (Digit digit : renderEngine.getGameEngine().context.digitList) {
                renderEngine.addStatic(digit.get3DRenderer());
            }
//            for (Stone stone : renderEngine.getGameEngine().context.stoneList) {
//                renderEngine.addStatic(stone.get3DRenderer());
//            }
            // logger.info(String.format("count=%d", count));
            renderEngine.removeAllEffects();
            sceneList.get(game.name).create();
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
            renderEngine.getGameEngine().context.turtleList.destroy(renderEngine);
            renderEngine.getGameEngine().context.digitList.destroy(renderEngine);
        }
    }

    @Override
    public void disposeLevel() {
        clear();
        destroyLevelBackground();
        game.reset();
        NrOfTotalStones = 0;
    }

    public Color getInfoColor() {
        return infoColor;
    }

    @Override
    protected boolean isEnableTime() {
        if (renderEngine != null)
            return renderEngine.getGameEngine().context.isEnableTime();
        else
            return true;
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

    public boolean testValidity() {
        return getRecording().testValidity(INVALID_LEVEL_RECORDING_DETECTED, RESETTING_LEVEL, renderEngine.getGameEngine(), (Game) renderEngine.getGameEngine().context.game.clone());
    }

    public void updateFps() {
        if (renderEngine.getGameEngine().context.isEnableTime()) {
            maxAnimationPhase = Math.max(1, Gdx.graphics.getFramesPerSecond() / 10);
        }
    }
}