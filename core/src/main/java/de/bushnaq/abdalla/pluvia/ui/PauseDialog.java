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

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;

/**
 * @author kunterbunt
 */
public class PauseDialog extends AbstractDialog {

    public PauseDialog(GameEngine gameEngine, final Batch batch, final InputMultiplexer inputMultiplexer) throws Exception {
        super(gameEngine, batch, inputMultiplexer);
        createStage("", true);
    }

    @Override
    protected void close() {
        setVisible(false);
    }
//	@Override
//	protected void enterAction() {
//		setVisible(false);
//	}
//
//	protected void escapeAction() {
//		setVisible(false);
//	}

    @Override
    public void create() {
        Sizes sizes = VisUI.getSizes();
        {
            getTable().row();
            VisLabel label = new VisLabel("Pause Menu");
            label.setColor(LIGHT_BLUE_COLOR);
            label.setAlignment(Align.center);
            getTable().add(label).width(DIALOG_WIDTH * sizes.scaleFactor).pad(0, 16, 16, 16).center();
        }
        {
            getTable().row();
            VisTextButton button = new VisTextButton("Resume Game", "blue");
            addHoverEffect(button);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    close();
                }
            });
            getTable().add(button).center().width(BUTTON_WIDTH * sizes.scaleFactor);
        }
        {
            getTable().row();
            VisTextButton button = new VisTextButton("View Score");
            addHoverEffect(button);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    getGameEngine().getScoreDialog().push(PauseDialog.this);
                }
            });
            getTable().add(button).center().width(BUTTON_WIDTH * sizes.scaleFactor);
        }
        {
            getTable().row();
            VisTextButton button = new VisTextButton("New Game");
            addHoverEffect(button);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
//					getGameEngine().context.levelManager.disposeLevel();
//					getGameEngine().context.levelManager.createLevel();
                    createGame(getGameEngine().context.getGameIndex(), false, -1, false);
                    close();
                }
            });
            getTable().add(button).center().width(BUTTON_WIDTH * sizes.scaleFactor);
        }
        {
            getTable().row();
            VisTextButton button = new VisTextButton("Back to Main Menu");
            addHoverEffect(button);
//			button.setColor(Color.RED);
            button.addListener(new ClickListener() {

                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    setVisible(false);
                    getGameEngine().context.game.updateTimer();
                    getGameEngine().context.levelManager.writeToDisk();
                    getGameEngine().getMainDialog().setVisible(true);
                }
            });
            getTable().add(button).center().width(BUTTON_WIDTH * sizes.scaleFactor).pad(16);
        }

//		getTable().pack();
        positionWindow();
    }

}
