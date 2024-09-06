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
    private static final float                  NORMAL_LIGHT_INTENSITY     = 10f;
    private static final Color                  SELECTED_TRADER_NAME_COLOR = Color.BLACK;
    private static final Color                  TRADER_NAME_COLOR          = Color.BLACK;
    private static final float                  TRADER_SIZE_X              = 1f;
    private static final float                  TRADER_SIZE_Y              = 1f;
    private static final float                  TRADER_SIZE_Z              = 1f;
    private static final float                  VANISHING_LIGHT_INTENSITY  = 1000;
    private              BitmapFont             font;
    private              GameObject<GameEngine> gameObject;
    private final        float                  lightIntensity             = 0f;
    private final        boolean                lightIsOne                 = false;
    //	private final List<PointLight>	pointLight					= new ArrayList<>();
    private final        Stone                  stone;
    private final        Vector3                translation                = new Vector3();    // intermediate value

    public Stone3DRenderer(final Stone patch) {
        this.stone = patch;
    }

    @Override
    public void create(final RenderEngine3D<GameEngine> renderEngine) {
        if (gameObject == null && renderEngine != null) {
            gameObject = new GameObject<GameEngine>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.stone[stone.getType()].scene.model), stone);
//			gameObject = new GameObject<GameEngine>(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.stone[0].scene.model), stone);
//			final Material            material =gameObject.instance.model.materials.get(0);
//			PBRColorAttribute                 attribute = (PBRColorAttribute)material.get(PBRColorAttribute.BaseColorFactor);
//			attribute.color = Color.RED;
            renderEngine.addDynamic(gameObject);
            renderEngine.addDynamic(this);
            gameObject.update();
            font = renderEngine.getGameEngine().getAtlasManager().modelFont;
        }
    }

    @Override
    public void destroy(final RenderEngine3D<GameEngine> renderEngine) {
        if (renderEngine != null) {
            renderEngine.removeDynamic(gameObject);
            renderEngine.removeDynamic(this);
//			for (PointLight pl : pointLight) {
//				renderEngine.remove(pl, true);
//			}
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
//			float h = TRADER_SIZE_Y / 4;
//			renderTextOnFrontSide(renderEngine, 0, 0 + h, "" + stone.getType(), TRADER_SIZE_Y / 2, color);
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

//	private void tuneLightIntensity() {
//		if (lightIsOne) {
//			if (stone.isVanishing()) {
//				if (lightIntensity < VANISHING_LIGHT_INTENSITY)
//					lightIntensity += Math.signum(VANISHING_LIGHT_INTENSITY - lightIntensity) * 0.3f;
//			} else {
//				if (lightIntensity < NORMAL_LIGHT_INTENSITY)
//					lightIntensity += Math.signum(NORMAL_LIGHT_INTENSITY - lightIntensity) * 0.1f;
//			}
//			for (PointLight pl : pointLight) {
//				pl.intensity = lightIntensity;
//			}
//		}
//	}

//	private void turnLightOff(final GameEngine gameEngine) {
//		if (lightIsOne) {
//			for (PointLight pl : pointLight) {
//				gameEngine.renderEngine.remove(pl, true);
//			}
//			lightIsOne = false;
//		}
//	}

//	private void turnLightOn(final GameEngine gameEngine) {
//		if (!lightIsOne) {
//			lightIntensity = 0f;
//			Color color;
//			if (gameEngine.renderEngine.isPbr()) {
//				Material			material	= gameObject.instance.model.materials.get(0);
//				Attribute			attribute	= material.get(PBRColorAttribute.BaseColorFactor);
//				PBRColorAttribute	a			= (PBRColorAttribute) attribute;
//				color = a.color;
//			} else {
//				Material		material	= gameObject.instance.model.materials.get(0);
//				Attribute		attribute	= material.get(ColorAttribute.Diffuse);
//				ColorAttribute	a			= (ColorAttribute) attribute;
//				color = a.color;
//			}
//
//			final PointLight light = new PointLight().set(color, 0f, 0f, 0f, lightIntensity);
//			pointLight.add(light);
//			gameEngine.renderEngine.add(light, true);
//			lightIsOne = true;
//		}
//
//	}

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

        if (stone.isRightAttached())
            scaleDx += 0.2f;
        if (stone.isLeftAttached())
            scaleDx += 0.2f;

        if (stone.isRightAttached() && !stone.isLeftAttached())
            translation.x = stone.tx + 0.1f;
        else if (!stone.isRightAttached() && stone.isLeftAttached())
            translation.x = stone.tx - 0.1f;
        else
            translation.x = stone.tx;
        translation.y = -stone.ty;
        translation.z = stone.tz;
//		for (PointLight pl : pointLight) {
//			pl.setPosition(translation);
//		}

        {
            gameObject.instance.transform.setToRotation(Vector3.Y, renderEngine.getGameEngine().getViewAngle());
            gameObject.instance.transform.rotate(Vector3.X, renderEngine.getGameEngine().getRotateCubeXAngle());
            gameObject.instance.transform.rotate(Vector3.Z, renderEngine.getGameEngine().getRotateCubeZAngle());
            gameObject.instance.transform.translate(translation);
            if (stone.isVanishing()) {
                gameObject.instance.transform.scale(fraction, fraction, fraction);
            } else {
                gameObject.instance.transform.scale(1f + scaleDx, 1f, 1f);
            }
            gameObject.update();

//            gameObject.instance.transform.translate(new Vector3(0, 1, 0));
//            gameObject.instance.transform.scale(1f, 1f, 1f);
//            gameObject.update();
        }

    }
}
