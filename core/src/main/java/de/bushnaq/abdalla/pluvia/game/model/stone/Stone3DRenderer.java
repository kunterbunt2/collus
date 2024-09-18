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

package de.bushnaq.abdalla.pluvia.game.model.stone;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import de.bushnaq.abdalla.engine.GameObject;
import de.bushnaq.abdalla.engine.ObjectRenderer;
import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.model.ModelInstanceHack;

import static de.bushnaq.abdalla.pluvia.game.model.stone.Stone.*;

/**
 * @author kunterbunt
 */
public class Stone3DRenderer extends ObjectRenderer<GameEngine> {
    private static final int        NUMBER_OF_FRAME_ELEMENTS   = 24;
    private static final Color      SELECTED_TRADER_NAME_COLOR = Color.BLACK;
    private static final Color      TRADER_NAME_COLOR          = Color.BLACK;
    private static final float      TRADER_SIZE_X              = 1f;
    private static final float      TRADER_SIZE_Y              = 1f;
    private static final float      TRADER_SIZE_Z              = 1f;
    private final        boolean    bothSides                  = true;
    private              BitmapFont font;
    boolean[][] needed = new boolean[6][];
    private final Stone                    stone;
    private final GameObject<GameEngine>[] stoneFrame        = new GameObject[NUMBER_OF_FRAME_ELEMENTS];
    private       GameObject<GameEngine>   stoneGO;
    private       GameObject<GameEngine>   stoneXNegGO;
    private       GameObject<GameEngine>   stoneXPosGO;
    private       GameObject<GameEngine>   stoneYNegGO;
    private       GameObject<GameEngine>   stoneYPosGO;
    private       GameObject<GameEngine>   stoneZNegGO;
    private       GameObject<GameEngine>   stoneZPosGO;
    private final Vector3                  translation       = new Vector3();    // intermediate value
    private final Vector3                  translationBuffer = new Vector3();    // intermediate value

    public Stone3DRenderer(final Stone patch) {
        this.stone = patch;
    }

    @Override
    public void create(final RenderEngine3D<GameEngine> renderEngine) {
        needed = new boolean[][]{//
                {//xpos
                        false, false, false, false,//
                        false, false, true, false,//
                        false, false, false, false,//
                        true, false, false, false,//
                        false, false, false, true,//
                        false, true, false, false//
                },//
                {//xneg
                        false, false, false, false,//
                        true, false, false, false,//
                        false, false, false, false,//
                        false, false, true, false,//
                        false, true, false, false,//
                        false, false, false, true//
                },//
                {//zpos
                        true, false, false, false,//
                        false, false, false, false,//
                        false, false, true, false,//
                        false, false, false, false,//
                        true, false, false, false,//
                        true, false, false, false//
                },//
                {//zneg
                        false, false, true, false,//
                        false, false, false, false,//
                        true, false, false, false,//
                        false, false, false, false,//
                        false, false, true, false,//
                        false, false, true, false//
                },//
                {//ypos
                        false, true, false, false,//
                        false, true, false, false,//
                        false, true, false, false,//
                        false, true, false, false,//
                        false, false, false, false,//
                        false, false, false, false//
                },//
                {//yneg
                        false, false, false, true,//
                        false, false, false, true,//
                        false, false, false, true,//
                        false, false, false, true,//
                        false, false, false, false,//
                        false, false, false, false//
                },//
        };
//        for (int i = 0; i < 6; i++) {
//            needed[i] = new boolean[24];
//        }

        Model shadow = getShadowModel(renderEngine);
        if (stoneGO == null && renderEngine != null) {
            renderEngine.addDynamic(this);
            {
                stoneGO = new GameObject<>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.stone[stone.getType()].scene.model), stone);
                renderEngine.addDynamic(stoneGO);
                stoneGO.update();
            }
            if (stone.type > 8) {
                for (int i = 0; i < NUMBER_OF_FRAME_ELEMENTS; i++) {
                    GameObject<GameEngine> go = new GameObject<>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.stoneFrameElement.scene.model), stone);
                    Material               m  = go.instance.materials.get(0);
                    m.clear();
                    for (Material material : stoneGO.instance.model.materials) {
                        if (material.id.equals("m.base")) {
                            for (Attribute value : material) {
                                if (value.type == PBRColorAttribute.BaseColorFactor)
                                    m.set(PBRColorAttribute.createBaseColorFactor(Color.RED));
                                else
                                    m.set(value);
                            }
                        }
                    }
                    renderEngine.addDynamic(go);
                    go.update();
                    stoneFrame[i] = go;
                }
            }
            {
                stoneXNegGO = new GameObject<>(new ModelInstanceHack(shadow), null);
                renderEngine.addDynamic(stoneXNegGO);
                stoneXNegGO.update();
            }
            {
                stoneYNegGO = new GameObject<>(new ModelInstanceHack(shadow), null);
                renderEngine.addDynamic(stoneYNegGO);
                stoneYNegGO.update();
            }
            {
                stoneZNegGO = new GameObject<>(new ModelInstanceHack(shadow), null);
                renderEngine.addDynamic(stoneZNegGO);
                stoneZNegGO.update();
            }
            if (bothSides) {
                stoneXPosGO = new GameObject<>(new ModelInstanceHack(shadow), null);
                renderEngine.addDynamic(stoneXPosGO);
                stoneXPosGO.update();
            }
            if (bothSides) {
                stoneYPosGO = new GameObject<>(new ModelInstanceHack(shadow), null);
                renderEngine.addDynamic(stoneYPosGO);
                stoneYPosGO.update();
            }
            if (bothSides) {
                stoneZPosGO = new GameObject<>(new ModelInstanceHack(shadow), null);
                renderEngine.addDynamic(stoneZPosGO);
                stoneZPosGO.update();
            }
            font = renderEngine.getGameEngine().getAtlasManager().modelFont;
        }
    }

    @Override
    public void destroy(final RenderEngine3D<GameEngine> renderEngine) {
        if (renderEngine != null) {
            renderEngine.removeDynamic(stoneGO);
            for (int i = 0; i < NUMBER_OF_FRAME_ELEMENTS; i++)
                 renderEngine.removeDynamic(stoneFrame[i]);
            renderEngine.removeDynamic(stoneXNegGO);
            renderEngine.removeDynamic(stoneYNegGO);
            renderEngine.removeDynamic(stoneZNegGO);
            renderEngine.removeDynamic(stoneXPosGO);
            renderEngine.removeDynamic(stoneYPosGO);
            renderEngine.removeDynamic(stoneZPosGO);
            renderEngine.removeDynamic(this);
        }
    }

    private Model getShadowModel(final RenderEngine3D<GameEngine> renderEngine) {
        switch (stone.getType()) {
            case MAGNETIC_STONE1:
            case MAGNETIC_STONE2:
            case MAGNETIC_STONE3:
            case CLEAR_CUBE:
            case FIXED_CUBE:
                return renderEngine.getGameEngine().modelManager.shadow2.scene.model;
            default:
                return renderEngine.getGameEngine().modelManager.shadow1.scene.model;
        }

    }

    @Override
    public void renderText(final RenderEngine3D<GameEngine> renderEngine, final int index, final boolean selected) {
        if (renderEngine.isDebugMode()) {
            Color color;
            if (selected) {
                color = SELECTED_TRADER_NAME_COLOR;
            } else {
                color = TRADER_NAME_COLOR;
            }
            float h = TRADER_SIZE_Y / 8;
//            renderTextOnFrontSide(renderEngine, 0, 0 + 2 * h, stone.name, TRADER_SIZE_Y / 6, color);
            renderTextOnFrontSide(renderEngine, 0, 0 + 2 * h, String.format("%d", stone.getType()), TRADER_SIZE_Y / 6, color);
            renderTextOnFrontSide(renderEngine, 0, 0 + 1 * h, stone.getCoordinatesAsString(), TRADER_SIZE_Y / 8, color);
            renderTextOnFrontSide(renderEngine, 0, 0 - 0 * h, stone.getCannotAttributesAsString(), TRADER_SIZE_Y / 8, color);
            renderTextOnFrontSide(renderEngine, 0, 0 - 1 * h, stone.getCanAttributesAsString(), TRADER_SIZE_Y / 8, color);
            renderTextOnFrontSide(renderEngine, 0, 0 - 2 * h, stone.getGlueStatusAsString(), TRADER_SIZE_Y / 6, color);
            renderTextOnFrontSide(renderEngine, 0, 0 - 3 * h, stone.getDoingStatusAsString(), TRADER_SIZE_Y / 8, color);
        }
    }

    private void renderTextOnFrontSide(final RenderEngine3D<GameEngine> renderEngine, final float dx, final float dy, final String text, final float size, final Color color) {
        final PolygonSpriteBatch batch = renderEngine.renderEngine25D.batch;
        {
            final Matrix4     m        = new Matrix4();
            final float       fontSize = font.getLineHeight();
            final float       scaling  = size / fontSize;
            final GlyphLayout layout   = new GlyphLayout();
            layout.setText(font, text);
            final float width  = layout.width;        // contains the width of the current set text
            final float height = layout.height;    // contains the height of the current set text
            // on top
            {
                m.setToRotation(Vector3.Y, renderEngine.getGameEngine().getViewAngle());
//				gameObject.instance.transform.translate(translation);
                m.translate(translation.x, translation.y, translation.z + TRADER_SIZE_Z / 2f + 0.01f);
                m.translate(-width * scaling / 2.0f + dx, height * scaling / 2.0f + dy, 0);
                m.scale(scaling, scaling, 1f);

            }
            batch.setTransformMatrix(m);
            font.setColor(color);
            font.draw(batch, text, 0, 0);
        }
    }

    @Override
    public void update(final float x, final float y, final float z, final RenderEngine3D<GameEngine> renderEngine, final long currentTime, final float timeOfDay, final int index, final boolean selected) throws Exception {
        GameEngine gameEngine = renderEngine.getGameEngine();
        float      fraction   = (float) (gameEngine.context.levelManager.animationPhase) / ((float) gameEngine.context.levelManager.maxAnimationPhase);
        if (stone.isVanishing()) {
            stone.setTx(x);
            stone.setTy(y);
            stone.setTz(z);
        } else if (stone.isDropping()) {
            stone.setTx(x);
            stone.setTy(y - fraction * TRADER_SIZE_Y);
            stone.setTz(z);
        } else if (stone.isMovingLeft()) {
            stone.setTx(x + fraction * TRADER_SIZE_X);
            stone.setTy(y);
            stone.setTz(z);
        } else if (stone.isMovingRight()) {
            stone.setTx(x - fraction * TRADER_SIZE_X);
            stone.setTy(y);
            stone.setTz(z);
        } else {
            stone.setTx(x);
            stone.setTy(y);
            stone.setTz(z);
        }
        float scaleDx = 0;
        float scaleDz = 0;
        float scaleDy = 0;

//        if (stone.getXPosAttached()) scaleDx += 0.2f;
//        if (stone.getXNegAttached()) scaleDx += 0.2f;
//        if (stone.getZPosAttached()) scaleDz += 0.2f;
//        if (stone.getZNegAttached()) scaleDz += 0.2f;
//        if (stone.getYPosAttached()) scaleDy += 0.2f;
//        if (stone.getYNegAttached()) scaleDy += 0.2f;

//        if (stone.getXPosAttached() && !stone.getXNegAttached()) translation.x = stone.tx + 0.1f;
//        else if (!stone.getXPosAttached() && stone.getXNegAttached()) translation.x = stone.tx - 0.1f;
//        else
        translation.x = stone.tx;
        translation.y = -stone.ty;
        translation.z = stone.tz;

        {
            stoneGO.instance.transform.setToRotation(Vector3.Y, gameEngine.getViewAngle() + gameEngine.getRotateCubeYAngle());
            stoneGO.instance.transform.rotate(Vector3.X, gameEngine.getRotateCubeXAngle());
            stoneGO.instance.transform.rotate(Vector3.Z, gameEngine.getRotateCubeZAngle());
            stoneGO.instance.transform.translate(translation);
            if (stone.isVanishing()) {
                stoneGO.instance.transform.scale(fraction, fraction, fraction);
            } else {
                stoneGO.instance.transform.scale(1f + scaleDx, 1f + scaleDy, 1f + scaleDz);
            }
            stoneGO.update();
        }
        if (stone.type > 8) {
            Vector3[] rotation = {
                    new Vector3(0f, 0f, 0f),//xpos - left
                    new Vector3(-90f, 0f, 0f),//xpos - top
                    new Vector3(-180f, 0f, 0f),//xpos - right
                    new Vector3(-270f, 0f, 0f),//xpos - down

                    new Vector3(0f, -90f, 0f),//zpos - left
                    new Vector3(-90f, -90f, 0f),//zpos - top
                    new Vector3(-180f, -90f, 0f),//zpos - right
                    new Vector3(-270f, -90f, 0f),//zpos - down

                    new Vector3(0f, -180f, 0f),//xneg - left
                    new Vector3(-90f, -180f, 0f),//xneg - top
                    new Vector3(-180f, -180f, 0f),//xneg - right
                    new Vector3(-270f, -180f, 0f),//xneg - down

                    new Vector3(0f, -270f, 0f),//zneg - left
                    new Vector3(-90f, -270f, 0f),//zneg - top
                    new Vector3(-180f, -270f, 0f),//zneg - right
                    new Vector3(-270f, -270f, 0f),//zneg - down

                    new Vector3(0f, 0f, 90f),//ypos - left
                    new Vector3(-90f, 0f, 90f),//ypos - top
                    new Vector3(-180f, 0f, 90f),//ypos - right
                    new Vector3(-270f, 0f, 90f),//ypos - down

                    new Vector3(0f, 0f, -90f),//yneg - left
                    new Vector3(-90f, 0f, -90f),//yneg - top
                    new Vector3(-180f, 0f, -90f),//yneg - right
                    new Vector3(-270f, 0f, -90f),//yneg - down
            };
            boolean[] visible = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
//            if (stone.getXPosAttached()) {
//                for (int i = 0; i < 24; i++) {
//                    boolean value = needed[0][i];
//                    if (value) {
//                        visible[i] = value;
//                    }
//                }
//            }
//            if (stone.getXNegAttached()) {
//                for (int i = 0; i < 24; i++) {
//                    boolean value = needed[1][i];
//                    if (value) {
//                        visible[i] = value;
//                    }
//                }
//            }
//            if (stone.getZPosAttached()) {
//                for (int i = 0; i < 24; i++) {
//                    boolean value = needed[2][i];
//                    if (value) {
//                        visible[i] = value;
//                    }
//                }
//            }
//            if (stone.getZNegAttached()) {
//                for (int i = 0; i < 24; i++) {
//                    boolean value = needed[3][i];
//                    if (value) {
//                        visible[i] = value;
//                    }
//                }
//            }
            if (stone.getYPosAttached()) {
                for (int i = 0; i < 24; i++) {
                    boolean value = needed[4][i];
                    if (value) {
                        visible[i] = value;
                    }
                }
            }
            if (stone.getYNegAttached()) {
                for (int i = 0; i < 24; i++) {
                    boolean value = needed[5][i];
                    if (value) {
                        visible[i] = value;
                    }
                }
            }
            Vector3 hide = new Vector3(0, 0, 1000);
            for (int i = 0; i < NUMBER_OF_FRAME_ELEMENTS; i++) {
                if (visible[i]) {
                    stoneFrame[i].instance.transform.setToRotation(Vector3.Y, gameEngine.getViewAngle() + gameEngine.getRotateCubeYAngle());
                    stoneFrame[i].instance.transform.rotate(Vector3.X, gameEngine.getRotateCubeXAngle());
                    stoneFrame[i].instance.transform.rotate(Vector3.Z, gameEngine.getRotateCubeZAngle());
                    stoneFrame[i].instance.transform.translate(translation);
                    stoneFrame[i].instance.transform.rotate(Vector3.Z, rotation[i].z);
                    stoneFrame[i].instance.transform.rotate(Vector3.Y, rotation[i].y);
                    stoneFrame[i].instance.transform.rotate(Vector3.X, rotation[i].x);
                    if (stone.isVanishing()) {
                        stoneFrame[i].instance.transform.scale(fraction, fraction, fraction);
                    } else {
                        stoneFrame[i].instance.transform.scale(1f, 1f, 1f);
                    }
                } else {
                    stoneFrame[i].instance.transform.setToTranslation(hide);
                }
                stoneFrame[i].update();
            }
        }
        float distance = 3.55f;
        float scaling  = 1f;
        {
            translationBuffer.set(translation);
            translationBuffer.x = -distance;
            stoneXNegGO.instance.transform.setToRotation(Vector3.Y, gameEngine.getViewAngle() + gameEngine.getRotateCubeYAngle());
            stoneXNegGO.instance.transform.rotate(Vector3.X, gameEngine.getRotateCubeXAngle());
            stoneXNegGO.instance.transform.rotate(Vector3.Z, gameEngine.getRotateCubeZAngle());
            stoneXNegGO.instance.transform.translate(translationBuffer);
            stoneXNegGO.instance.transform.rotate(Vector3.Z, -90);
            if (stone.isVanishing()) {
                stoneXNegGO.instance.transform.scale(fraction, fraction, fraction);
            } else {
                stoneXNegGO.instance.transform.scale(scaling + scaleDx, scaling, scaling);
            }
            stoneXNegGO.update();
        }
        if (bothSides) {
            translationBuffer.set(translation);
            translationBuffer.x = distance;
            stoneXPosGO.instance.transform.setToRotation(Vector3.Y, gameEngine.getViewAngle() + gameEngine.getRotateCubeYAngle());
            stoneXPosGO.instance.transform.rotate(Vector3.X, gameEngine.getRotateCubeXAngle());
            stoneXPosGO.instance.transform.rotate(Vector3.Z, gameEngine.getRotateCubeZAngle());
            stoneXPosGO.instance.transform.translate(translationBuffer);
            stoneXPosGO.instance.transform.rotate(Vector3.Z, 90);
            if (stone.isVanishing()) {
                stoneXPosGO.instance.transform.scale(fraction, fraction, fraction);
            } else {
                stoneXPosGO.instance.transform.scale(scaling + scaleDx, scaling, scaling);
            }
            stoneXPosGO.update();
        }
        {
            translationBuffer.set(translation);
            translationBuffer.y = -distance;
            stoneYNegGO.instance.transform.setToRotation(Vector3.Y, gameEngine.getViewAngle() + gameEngine.getRotateCubeYAngle());
            stoneYNegGO.instance.transform.rotate(Vector3.X, gameEngine.getRotateCubeXAngle());
            stoneYNegGO.instance.transform.rotate(Vector3.Z, gameEngine.getRotateCubeZAngle());
            stoneYNegGO.instance.transform.translate(translationBuffer);
//            stoneYGO.instance.transform.rotate(Vector3.Z, -90);
            if (stone.isVanishing()) {
                stoneYNegGO.instance.transform.scale(fraction, fraction, fraction);
            } else {
                stoneYNegGO.instance.transform.scale(scaling + scaleDx, scaling, scaling);
            }
            stoneYNegGO.update();
        }
        if (bothSides) {
            translationBuffer.set(translation);
            translationBuffer.y = distance;
            stoneYPosGO.instance.transform.setToRotation(Vector3.Y, gameEngine.getViewAngle() + gameEngine.getRotateCubeYAngle());
            stoneYPosGO.instance.transform.rotate(Vector3.X, gameEngine.getRotateCubeXAngle());
            stoneYPosGO.instance.transform.rotate(Vector3.Z, gameEngine.getRotateCubeZAngle());
            stoneYPosGO.instance.transform.translate(translationBuffer);
            stoneYPosGO.instance.transform.rotate(Vector3.Z, 180);
            if (stone.isVanishing()) {
                stoneYPosGO.instance.transform.scale(fraction, fraction, fraction);
            } else {
                stoneYPosGO.instance.transform.scale(scaling + scaleDx, scaling, scaling);
            }
            stoneYPosGO.update();
        }
        {
            translationBuffer.set(translation);
            translationBuffer.z = -distance;
            stoneZNegGO.instance.transform.setToRotation(Vector3.Y, gameEngine.getViewAngle() + gameEngine.getRotateCubeYAngle());
            stoneZNegGO.instance.transform.rotate(Vector3.X, gameEngine.getRotateCubeXAngle());
            stoneZNegGO.instance.transform.rotate(Vector3.Z, gameEngine.getRotateCubeZAngle());
            stoneZNegGO.instance.transform.translate(translationBuffer);
            stoneZNegGO.instance.transform.rotate(Vector3.X, 90);
            if (stone.isVanishing()) {
                stoneZNegGO.instance.transform.scale(fraction, fraction, fraction);
            } else {
                stoneZNegGO.instance.transform.scale(scaling + scaleDx, scaling, scaling);
            }
            stoneZNegGO.update();
        }
        if (bothSides) {
            translationBuffer.set(translation);
            translationBuffer.z = distance;
            stoneZPosGO.instance.transform.setToRotation(Vector3.Y, gameEngine.getViewAngle() + gameEngine.getRotateCubeYAngle());
            stoneZPosGO.instance.transform.rotate(Vector3.X, gameEngine.getRotateCubeXAngle());
            stoneZPosGO.instance.transform.rotate(Vector3.Z, gameEngine.getRotateCubeZAngle());
            stoneZPosGO.instance.transform.translate(translationBuffer);
            stoneZPosGO.instance.transform.rotate(Vector3.X, -90);
            if (stone.isVanishing()) {
                stoneZPosGO.instance.transform.scale(fraction, fraction, fraction);
            } else {
                stoneZPosGO.instance.transform.scale(scaling + scaleDx, scaling, scaling);
            }
            stoneZPosGO.update();
        }

    }
}
