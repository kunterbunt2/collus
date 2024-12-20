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

package de.bushnaq.abdalla.pluvia.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import de.bushnaq.abdalla.pluvia.desktop.Context;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.engine.camera.SnappingCamera;
import de.bushnaq.abdalla.pluvia.game.Game;
import de.bushnaq.abdalla.pluvia.game.LevelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

//enum BlurMode {
//	up, down
//}

/**
 * @author kunterbunt
 */
public abstract class AbstractDialog {
    protected static final int                 BUTTON_WIDTH     = 150;
    protected static final int                 DIALOG_WIDTH     = 150;
    protected static final int                 LABEL_WIDTH      = 250;
    protected static final Color               LIGHT_BLUE_COLOR = new Color(0x1BA1E2FF);
    private final          Batch               batch;
    private final          List<VisTextButton> buttonList       = new ArrayList<>();
    protected              int                 currentButton    = 0;
    private                VisDialog           dialog;
    Event event = new Event();
    private final GameEngine           gameEngine;
    private final InputMultiplexer     inputMultiplexer;
    private final List<InputProcessor> inputProcessorCache = new ArrayList<>();
    // private float blurAmount = 1f;
//	private int						blurPasses			= 1;
//	private BlurMode				blurMode			= BlurMode.up;
    final         Logger               logger              = LoggerFactory.getLogger(this.getClass());
    protected     boolean              modal               = false;
    private       AbstractDialog       parent;
    private       Stage                stage;
    private final VisTable             table               = new VisTable(true);
    private       boolean              visible             = false;

    public AbstractDialog(GameEngine gameEngine, final Batch batch, final InputMultiplexer inputMultiplexer) throws Exception {
        this.gameEngine       = gameEngine;
        this.batch            = batch;
        this.inputMultiplexer = inputMultiplexer;
    }

    void add(VisTextButton button) {
        buttonList.add(button);
    }

    protected void addHoverEffect(final VisTextButton button) {
        button.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.setFocusBorderEnabled(true);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.setFocusBorderEnabled(false);
            }
        });
    }

    protected void afterInvisible() {
    }

    protected void beforeVisible() {
    }

    protected void close() {
        pop();
    }

    protected abstract void create();

    protected void createGame(int gameIndex, boolean resume/*, int seed*/, boolean editMode) {
        if (getGameEngine().context.levelManager != null)
            getGameEngine().context.levelManager.disposeLevel();
        getGameEngine().context.selectGame(gameIndex);
        Game game = getGameEngine().context.game;
        getGameEngine().context.levelManager = new LevelManager(getGameEngine().renderEngine, game, editMode);
//		universe.GameThread.clearLevel();
        if (editMode) {
            getGameEngine().context.levelManager.createLevel();
        } else {
            if (!getGameEngine().context.levelManager.readFromDisk(getGameEngine().context.getLevelNumber())) {
                // we failed to read the level
                // What is the next seed?
                getGameEngine().context.levelManager.disposeLevel();
//                int lastGameSeed = getGameEngine().context.getLastGameSeed();
//                getGameEngine().context.levelManager.setGameSeed(lastGameSeed + 1);
            }
//            else {
//                if (!getGameEngine().context.levelManager.testValidity()) {
//                    // we failed to validate the level
//                    logger.error("invalid recording file");
//                    // What is the next seed?
//                    getGameEngine().context.levelManager.disposeLevel();
//                    int lastGameSeed = getGameEngine().context.getLastGameSeed();
//                    getGameEngine().context.levelManager.setGameSeed(lastGameSeed + 1);
//                }
//            }
        }
//        else {
//            if (seed == -1) {
//                // next seed
//                int lastGameSeed = getGameEngine().context.getLastGameSeed();
//                getGameEngine().context.levelManager.setGameSeed(lastGameSeed + 1);
//            } else {
//                // seed is defined by user choice (high score)
//                getGameEngine().context.levelManager.setGameSeed(seed);
//            }
//        }
        getGameEngine().context.levelManager.createLevel("");
        game.startTimer();
        {
            SnappingCamera camera   = getGameEngine().getCamera();
            Vector3        position = new Vector3();
            Vector3        lookAt   = new Vector3();
            if (game.getySize() == 0) {
                //menu
                position.x = 0;
                position.y = 7;
                position.z = game.cameraZPosition;
                lookAt.x   = 0;
                lookAt.y   = 0;
                lookAt.z   = 0;
                if (getGameEngine().getDemo() != null)
                    getGameEngine().getDemo().startDemoMode(true);
            } else {
                position.x = 0;
                position.y = (float) game.getySize();
                position.z = game.cameraZPosition;
                lookAt.x   = 0;
                lookAt.y   = 0;
                lookAt.z   = 0;
                getGameEngine().renderEngine.getScheduledEffectEngine().resetAllEffects();
            }
            camera.position.set(position);
            camera.up.set(0, 1, 0);
            camera.lookAt(lookAt);
            camera.snapBack(position);
            camera.update();
            camera.setDirty(true);

        }
    }

    public void createStage(String title, boolean closeOnEscape) {
        if (stage == null) {
            stage = new Stage(new ScreenViewport(), batch);
//            if (closeOnEscape)
            {
                stage.addListener(new InputListener() {
                    @Override
                    public boolean keyDown(InputEvent event, int keycode) {
                        switch (event.getKeyCode()) {
                            case Input.Keys.ESCAPE:
                                if (closeOnEscape) {
                                    escapeAction();
                                    return true;
                                } else {
                                    return false;
                                }
                            case Input.Keys.UP:
//                                if (currentButton > 0)
                            {
                                {
                                    VisTextButton button = buttonList.get(currentButton);
                                    button.setStyle(VisUI.getSkin().get(VisTextButton.VisTextButtonStyle.class));
                                }
                                currentButton--;
                                currentButton = (currentButton + buttonList.size()) % buttonList.size();
//                                    logger.info(String.format("currentButton=%d", currentButton));
                                {
                                    VisTextButton button = buttonList.get(currentButton);
                                    button.setStyle(VisUI.getSkin().get("blue", VisTextButton.VisTextButtonStyle.class));
                                }
                                draw();
                            }
                            return true;
                            case Input.Keys.DOWN:
                                if (currentButton < buttonList.size()) {
                                    {
                                        VisTextButton button = buttonList.get(currentButton);
                                        button.setStyle(VisUI.getSkin().get(VisTextButton.VisTextButtonStyle.class));
                                    }
                                    currentButton++;
                                    currentButton %= buttonList.size();
//                                    logger.info(String.format("currentButton=%d", currentButton));
                                    {
                                        VisTextButton button = buttonList.get(currentButton);
                                        button.setStyle(VisUI.getSkin().get("blue", VisTextButton.VisTextButtonStyle.class));
                                    }
                                    draw();
                                }
                                return true;
                            case Input.Keys.ENTER:
                                VisTextButton button = buttonList.get(currentButton);
                                DelayedRemovalArray<EventListener> listeners = button.getListeners();
                                for (int i = 0; i < listeners.size; i++) {
                                    if (listeners.get(i) instanceof ClickListener) {
                                        ((ClickListener) listeners.get(i)).clicked(null, 0, 0);
                                    }
                                }
//                                enterAction();
                                return true;
                        }
                        return false;
                    }
                });
            }
//            stage.addListener(new InputListener() {
//
//                @Override
//                public boolean keyDown(InputEvent event, int keycode) {
//                    switch (event.getKeyCode()) {
//                        case Input.Keys.ENTER:
//                            enterAction();
//                            return true;
//                    }
//                    return false;
//                }
//
//            });
            stage.addActor(createWindow(title));
            create();
            packAndPosition();
        }
    }

    protected VisDialog createWindow(String title) {
        dialog = new VisDialog(title);
        dialog.setColor(1.0f, 1.0f, 1.0f, 1f);
        dialog.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        dialog.setMovable(false);
        getDialog().setBackground((Drawable) null);
//		table.setDebug(true);
        table.pad(0, 16, 16, 16);
        dialog.getContentTable().add(this.table);
        return dialog;
    }

    public void dispose() {
        inputMultiplexer.removeProcessor(stage);
        stage.dispose();
    }

    public void draw() {
//		switch (blurMode) {
//		case up:
//			if (blurPasses < 32)
//				blurPasses += 1;
//			else {
//				int a = 0;
//			}
//			break;
//		case down:
//			if (blurPasses > 1)
//				blurPasses -= 1;
//			else {
//				gameEngine.renderEngine.removeBlurEffect();
//				this.visible = false;
//			}
//			break;
//		}
//		gameEngine.renderEngine.updateBlurEffect(blurPasses, blurAmount);

        stage.act();
        stage.draw();
    }

    protected void enterAction() {
        close();
    }

//	public void disposeWindow() {
//		stage.clear();
//	}

    protected void escapeAction() {
        close();
    }

    public VisDialog getDialog() {
        return dialog;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public VisTable getTable() {
        return table;
    }

    public Viewport getViewport() {
        return stage.getViewport();
    }

    public boolean isVisible() {
        return visible;
    }

    protected void packAndPosition() {
        dialog.pack();
        positionWindow();
    }

    public void pop() {
        setVisible(false);
        parent.setVisible(true);
    }

    protected void positionWindow() {
        dialog.setPosition(Gdx.graphics.getWidth() / 2 - dialog.getWidth() / 2, Gdx.graphics.getHeight() / 2 - (dialog.getHeight() - GameEngine.FONT_SIZE - 2) / 2);
    }

    /**
     * switch to another dialog
     *
     * @param parent parent dialog to switch back to when calling pop().
     */
    public void push(AbstractDialog parent) {
        this.parent = parent;
        parent.setVisible(false);
        setVisible(true);
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
        if (visible) {
            beforeVisible();
            for (InputProcessor ip : inputMultiplexer.getProcessors()) {
                inputProcessorCache.add(ip);
            }
            inputMultiplexer.clear();
            inputMultiplexer.addProcessor(stage);
//			gameEngine.renderEngine.updateBlurEffect(1, 1f);
//			gameEngine.renderEngine.addBlurEffect();
//			gameEngine.renderEngine.updateBlurEffect(32, 1f);
//			gameEngine.renderEngine.addBloomEffect();
//			blurMode = BlurMode.up;
//			blurAmount = 1f;
//			blurPasses = 1;
        } else {
            inputMultiplexer.removeProcessor(stage);
            for (InputProcessor ip : inputProcessorCache) {
                inputMultiplexer.addProcessor(ip);
            }
            inputProcessorCache.clear();
//			blurMode = BlurMode.down;
//			gameEngine.renderEngine.removeBlurEffect();
//			gameEngine.renderEngine.removeBloomEffect();
            afterInvisible();
        }
    }

    public void update(final Context universe) {
    }

}
