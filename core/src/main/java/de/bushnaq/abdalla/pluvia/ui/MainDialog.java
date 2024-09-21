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
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisList;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import de.bushnaq.abdalla.pluvia.engine.AtlasManager;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author kunterbunt
 */
public class MainDialog extends AbstractDialog {
    protected static final int DIALOG_HEIGHT = 150 * 4;
//    private                VisLabel descriptionLabel;

    private final VisList<String> listView = new VisList<>();
    Music music;
    private final VisTable table1 = new VisTable(true);
    private final VisTable table2 = new VisTable(true);
    private final VisTable table3 = new VisTable(true);

    public MainDialog(GameEngine gameEngine, final Batch batch, final InputMultiplexer inputMultiplexer) throws Exception {
        super(gameEngine, batch, inputMultiplexer);
        createStage("", false);
    }

    @Override
    public void create() {
        final Sizes sizes = VisUI.getSizes();
        {
            getTable().row();
//            VisLabel label = new VisLabel("Main Menu");
//            label.setColor(LIGHT_BLUE_COLOR);
//            label.setAlignment(Align.center);
//            getTable().add(label)./* width(DIALOG_WIDTH * 4 * sizes.scaleFactor). */pad(0, 16, 16, 16).center().colspan(3);
        }
        {
            getTable().row();
            getTable().add(table1);
            getTable().add(table2);
            getTable().add(table3).height(DIALOG_HEIGHT);
        }

        {
            table1.row();
            VisTextButton button = new VisTextButton("Start Game", "blue");
            addHoverEffect(button);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    startAction();
                }

            });

            table1.add(button).center().width(BUTTON_WIDTH * sizes.scaleFactor);
        }
        {
            table1.row();
            VisTextButton button = new VisTextButton("High Score");
            addHoverEffect(button);
            button.addListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    getGameEngine().getScoreDialog().push(MainDialog.this);
                }
            });
            table1.add(button).center().width(BUTTON_WIDTH * sizes.scaleFactor);
        }
        {
            table1.row();

            VisTextButton button = new VisTextButton("About Pluvia");

            addHoverEffect(button);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    getGameEngine().getAboutDialog().push(MainDialog.this);
                }
            });
            table1.add(button).center().width(BUTTON_WIDTH * sizes.scaleFactor);
        }

        {
            table1.row();
            VisTextButton button = new VisTextButton("Options");
            addHoverEffect(button);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    getGameEngine().getOptionsDialog().push(MainDialog.this);
                }
            });
            table1.add(button).center().width(BUTTON_WIDTH * sizes.scaleFactor);
        }

//		{
//			table3.row();
//			VisLabel label = new VisLabel("Game Mode");
//			label.setAlignment(Align.center);
//			label.setColor(LIGHT_BLUE_COLOR);
//			table3.add(label).center().pad(12);
//		}
        {
            table2.row();
            listView.setItems("Bird", "Rabbit", "Turtle", "Dragon");
            listView.setAlignment(Align.center);
//            listView.addListener(new ChangeListener() {
//                @Override
//                public void changed(ChangeEvent event, Actor actor) {
//                    updateDescription(sizes);
//                }
//            });

            table2.add(listView).center().width(BUTTON_WIDTH * sizes.scaleFactor).pad(16);
        }
        {
            getTable().row();
            VisTextButton button = new VisTextButton("Exit Game");
            addHoverEffect(button);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
//					logger.info("quit game");
                    Gdx.app.exit();
                }
            });
            getTable().add(button).width(BUTTON_WIDTH * sizes.scaleFactor).center().colspan(2);
        }
        {
            table3.pad(32);
//			table3.setWidth(DIALOG_WIDTH * 4 * sizes.scaleFactor);
//            updateDescription(sizes);
        }

    }

    private void createAudio() {
        if (music == null) {
            music = Gdx.audio.newMusic(Gdx.files.internal(AtlasManager.getAssetsFolderName() + String.format("/sound/pluvia-%02d.mp3", (int) (Math.random() * 2 + 1))));
        }
        music.setVolume((getGameEngine().context.getAmbientAudioVolumenProperty()) / 100f);
        music.play();
        logger.info("music is playing=" + music.isPlaying());
    }

    @Override
    public void dispose() {
        disposeAudio();
        super.dispose();
    }

    private void disposeAudio() {
        if (music != null) {
            music.dispose();
            music = null;
        }

    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    protected void enterAction() {
        startAction();
    }

    public String readFile(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    @Override
    public void setVisible(final boolean visible) {
        super.setVisible(visible);
        if (visible) {
            createGame(1, false, -1);
            if (getGameEngine().context.getAmbientAudioProperty()) {
//				createAudio();
            }
        } else {
            disposeAudio();
        }
    }

    private void startAction() {
        setVisible(false);
        int checkedIndex = listView.getSelectedIndex();
        createGame(checkedIndex, true, -1);
    }

//    private void updateDescription(Sizes sizes) {
//        try {
//            String   fileName    = listView.getSelected() + ".txt";
//            String   description = readFile(this.getClass().getResourceAsStream(fileName));
//            String[] split       = description.split("\n");
//            table3.clear();
//            for (String line : split) {
//                table3.row();
//                descriptionLabel = new VisLabel(line);
//                descriptionLabel.setWrap(true);
//                descriptionLabel.setAlignment(Align.topLeft);
//                table3.add(descriptionLabel).width(DIALOG_WIDTH * 3 * sizes.scaleFactor).pad(0).space(0, 0, 3, 0).left().top();
//            }
//            packAndPosition();
//        } catch (IOException e) {
//            logger.error(e.getMessage(), e);
//        }
//    }
}
