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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import de.bushnaq.abdalla.engine.GameObject;
import de.bushnaq.abdalla.engine.ObjectRenderer;
import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import net.mgsx.gltf.scene3d.model.ModelInstanceHack;

/**
 * @author kunterbunt
 */
public class Stone3DRenderer extends ObjectRenderer<GameEngine> {
    private static final Color                  SELECTED_TRADER_NAME_COLOR = Color.BLACK;
    private static final Color                  TRADER_NAME_COLOR          = Color.BLACK;
    private static final float                  TRADER_SIZE_X              = 1f;
    private static final float                  TRADER_SIZE_Y              = 1f;
    private static final float                  TRADER_SIZE_Z              = 1f;
    private final        boolean                bothSides                  = false;
    private              BitmapFont             font;
    private final        Stone                  stone;
    private              GameObject<GameEngine> stoneGO;
    private              GameObject<GameEngine> stoneXNegGO;
    private              GameObject<GameEngine> stoneXPosGO;
    private              GameObject<GameEngine> stoneYNegGO;
    private              GameObject<GameEngine> stoneYPosGO;
    private              GameObject<GameEngine> stoneZNegGO;
    private              GameObject<GameEngine> stoneZPosGO;
    private final        Vector3                translation                = new Vector3();    // intermediate value
    private final        Vector3                translationBuffer          = new Vector3();    // intermediate value

    public Stone3DRenderer(final Stone patch) {
        this.stone = patch;
    }

    @Override
    public void create(final RenderEngine3D<GameEngine> renderEngine) {
        if (stoneGO == null && renderEngine != null) {
            renderEngine.addDynamic(this);
            {
                stoneGO = new GameObject<>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.stone[stone.getType()].scene.model), stone);
                renderEngine.addDynamic(stoneGO);
                stoneGO.update();
            }
            {
                stoneXNegGO = new GameObject<>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.shadow), null);
                renderEngine.addDynamic(stoneXNegGO);
                stoneXNegGO.update();
            }
            {
                stoneYNegGO = new GameObject<>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.shadow), null);
                renderEngine.addDynamic(stoneYNegGO);
                stoneYNegGO.update();
            }
            {
                stoneZNegGO = new GameObject<>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.shadow), null);
                renderEngine.addDynamic(stoneZNegGO);
                stoneZNegGO.update();
            }
            if (bothSides) {
                stoneXPosGO = new GameObject<>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.shadow), null);
                renderEngine.addDynamic(stoneXPosGO);
                stoneXPosGO.update();
            }
            if (bothSides) {
                stoneYPosGO = new GameObject<>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.shadow), null);
                renderEngine.addDynamic(stoneYPosGO);
                stoneYPosGO.update();
            }
            if (bothSides) {
                stoneZPosGO = new GameObject<>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.shadow), null);
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
            renderEngine.removeDynamic(this);
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
            renderTextOnFrontSide(renderEngine, 0, 0 + 2 * h, stone.name, TRADER_SIZE_Y / 8, color);
            renderTextOnFrontSide(renderEngine, 0, 0 + 1 * h, stone.getCoordinatesAsString(), TRADER_SIZE_Y / 8, color);
            renderTextOnFrontSide(renderEngine, 0, 0 - 0 * h, stone.getCannotAttributesAsString(), TRADER_SIZE_Y / 8, color);
            renderTextOnFrontSide(renderEngine, 0, 0 - 1 * h, stone.getCanAttributesAsString(), TRADER_SIZE_Y / 8, color);
            renderTextOnFrontSide(renderEngine, 0, 0 - 2 * h, stone.getGlueStatusAsString(), TRADER_SIZE_Y / 8, color);
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
        float fraction = (float) (renderEngine.getGameEngine().context.levelManager.animationPhase) / ((float) renderEngine.getGameEngine().context.levelManager.maxAnimationPhase);
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

        if (stone.isRightAttached()) scaleDx += 0.2f;
        if (stone.isLeftAttached()) scaleDx += 0.2f;

        if (stone.isRightAttached() && !stone.isLeftAttached()) translation.x = stone.tx + 0.1f;
        else if (!stone.isRightAttached() && stone.isLeftAttached()) translation.x = stone.tx - 0.1f;
        else translation.x = stone.tx;
        translation.y = -stone.ty;
        translation.z = stone.tz;

        {
            stoneGO.instance.transform.setToRotation(Vector3.Y, renderEngine.getGameEngine().getViewAngle());
            stoneGO.instance.transform.rotate(Vector3.X, renderEngine.getGameEngine().getRotateCubeXAngle());
            stoneGO.instance.transform.rotate(Vector3.Z, renderEngine.getGameEngine().getRotateCubeZAngle());
            stoneGO.instance.transform.translate(translation);
            if (stone.isVanishing()) {
                stoneGO.instance.transform.scale(fraction, fraction, fraction);
            } else {
                stoneGO.instance.transform.scale(1f + scaleDx, 1f, 1f);
            }
            stoneGO.update();
        }
        float distance = 3.6f;
        float scaling  = 0.9f;
        {
            translationBuffer.set(translation);
            translationBuffer.x = -distance;
            stoneXNegGO.instance.transform.setToRotation(Vector3.Y, renderEngine.getGameEngine().getViewAngle());
            stoneXNegGO.instance.transform.rotate(Vector3.X, renderEngine.getGameEngine().getRotateCubeXAngle());
            stoneXNegGO.instance.transform.rotate(Vector3.Z, renderEngine.getGameEngine().getRotateCubeZAngle());
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
            stoneXPosGO.instance.transform.setToRotation(Vector3.Y, renderEngine.getGameEngine().getViewAngle());
            stoneXPosGO.instance.transform.rotate(Vector3.X, renderEngine.getGameEngine().getRotateCubeXAngle());
            stoneXPosGO.instance.transform.rotate(Vector3.Z, renderEngine.getGameEngine().getRotateCubeZAngle());
            stoneXPosGO.instance.transform.translate(translationBuffer);
            stoneXPosGO.instance.transform.rotate(Vector3.Z, -90);
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
            stoneYNegGO.instance.transform.setToRotation(Vector3.Y, renderEngine.getGameEngine().getViewAngle());
            stoneYNegGO.instance.transform.rotate(Vector3.X, renderEngine.getGameEngine().getRotateCubeXAngle());
            stoneYNegGO.instance.transform.rotate(Vector3.Z, renderEngine.getGameEngine().getRotateCubeZAngle());
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
            stoneYPosGO.instance.transform.setToRotation(Vector3.Y, renderEngine.getGameEngine().getViewAngle());
            stoneYPosGO.instance.transform.rotate(Vector3.X, renderEngine.getGameEngine().getRotateCubeXAngle());
            stoneYPosGO.instance.transform.rotate(Vector3.Z, renderEngine.getGameEngine().getRotateCubeZAngle());
            stoneYPosGO.instance.transform.translate(translationBuffer);
//            stoneYPosGO.instance.transform.rotate(Vector3.Z, 180);
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
            stoneZNegGO.instance.transform.setToRotation(Vector3.Y, renderEngine.getGameEngine().getViewAngle());
            stoneZNegGO.instance.transform.rotate(Vector3.X, renderEngine.getGameEngine().getRotateCubeXAngle());
            stoneZNegGO.instance.transform.rotate(Vector3.Z, renderEngine.getGameEngine().getRotateCubeZAngle());
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
            stoneZPosGO.instance.transform.setToRotation(Vector3.Y, renderEngine.getGameEngine().getViewAngle());
            stoneZPosGO.instance.transform.rotate(Vector3.X, renderEngine.getGameEngine().getRotateCubeXAngle());
            stoneZPosGO.instance.transform.rotate(Vector3.Z, renderEngine.getGameEngine().getRotateCubeZAngle());
            stoneZPosGO.instance.transform.translate(translationBuffer);
            stoneZPosGO.instance.transform.rotate(Vector3.X, 90);
            if (stone.isVanishing()) {
                stoneZPosGO.instance.transform.scale(fraction, fraction, fraction);
            } else {
                stoneZPosGO.instance.transform.scale(scaling + scaleDx, scaling, scaling);
            }
            stoneZPosGO.update();
        }

    }
}
