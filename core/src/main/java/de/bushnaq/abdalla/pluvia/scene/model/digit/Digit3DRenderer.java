package de.bushnaq.abdalla.pluvia.scene.model.digit;

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
 *
 */
public class Digit3DRenderer extends ObjectRenderer<GameEngine> {
	private static final Color	DIGIT_COLOR			= new Color(0f, 0f, 0f, 0.5f);
	private static final float	MINIMUM_DISTANCE	= 0.01f;
	private static final float	TRADER_SIZE_X		= 0.5f;
	private static final float	TRADER_SIZE_Y		= 0.5f;
	private static final float	TRADER_SIZE_Z		= 0.5f;
	private BitmapFont			font;
	private GameObject			instance;
	private Digit				stone;
	private final Vector3		translation			= new Vector3();				// intermediate value

	public Digit3DRenderer(final Digit patch) {
		this.stone = patch;
	}

	@Override
	public void create(final RenderEngine3D<GameEngine> renderEngine) {
		if (instance == null) {
			instance = new GameObject(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.levelCube), null);
			stone.getRenderModelInstances().add(instance);
			translation.x = stone.x;
			translation.y = -stone.y;
			translation.z = stone.z;
			instance.instance.transform.setToTranslation(translation);
			instance.instance.transform.scale(0.5f, 0.5f, 0.5f);
			instance.update();
			font =renderEngine.getGameEngine().getAtlasManager().aeroFont;
		}
	}

//	@Override
//	public void destroy(final GameEngine gameEngine) {
//		gameEngine.renderEngine.removeDynamic(instance);
//	}

	@Override
	public void renderText(final RenderEngine3D<GameEngine> renderEngine, final int index, final boolean selected) {
		Color color = DIGIT_COLOR;
		switch (stone.getDigitType()) {
		case score: {
			renderTextOnFrontSide(renderEngine, 0, 0, "" + stone.getDigit(), TRADER_SIZE_Y, color);
			if (stone.getDigitPosition() == 0) {
				renderTextOnTopSide(renderEngine, -TRADER_SIZE_X / 2 + (5f * TRADER_SIZE_X) / 2.0f, 0, 0, "Score", TRADER_SIZE_Y, color);
			}
		}
			break;
		case steps: {
			renderTextOnFrontSide(renderEngine, 0, 0, "" + stone.getDigit(), TRADER_SIZE_Y, color);
			if (stone.getDigitPosition() == 0) {
				renderTextOnTopSide(renderEngine, -TRADER_SIZE_X / 2 + (5f * TRADER_SIZE_X) / 2.0f, 0, 0, "Steps", TRADER_SIZE_Y, color);
			}
		}
			break;
		case name: {
			if (stone.getDigitPosition() == 0) {
				renderTextOnFrontSide(renderEngine, -TRADER_SIZE_X / 2 + (4f * TRADER_SIZE_X) / 2.0f, 0, stone.getText(), TRADER_SIZE_Y, color);
				renderTextOnTopSide(renderEngine, -TRADER_SIZE_X / 2 + (5f * TRADER_SIZE_X) / 2.0f, 0, 0, "Game", TRADER_SIZE_Y, color);
			}
		}
			break;
		case seed: {
			renderTextOnFrontSide(renderEngine, 0, 0, "" + stone.getDigit(), TRADER_SIZE_Y, color);
			if (stone.getDigitPosition() == 0) {
				renderTextOnTopSide(renderEngine, -TRADER_SIZE_X / 2 + (5f * TRADER_SIZE_X) / 2.0f, 0, 0, "Level", TRADER_SIZE_Y, color);
			}
		}
			break;
		}
	}

	private void renderTextOnFrontSide(final RenderEngine3D<GameEngine> renderEngine, final float dx, final float dy, final String text, final float size, final Color color) {
		final PolygonSpriteBatch batch = renderEngine.renderEngine25D.batch;
		{
			final Matrix4		m			= new Matrix4();
			final float			fontSize	= font.getLineHeight();
			final float			scaling		= size / fontSize;
			final GlyphLayout	layout		= new GlyphLayout();
			layout.setText(font, text);
			final float	width	= layout.width;		// contains the width of the current set text
			final float	height	= layout.height;	// contains the height of the current set text
			// on top
			{
				m.setToTranslation(translation.x, translation.y, translation.z + TRADER_SIZE_Z / 2f + MINIMUM_DISTANCE);
				m.translate(-width * scaling / 2.0f + dx, height * scaling / 2.0f + dy, 0);
				m.scale(scaling, scaling, 1f);

			}
			batch.setTransformMatrix(m);
			font.setColor(color);
			font.draw(batch, text, 0, 0);
		}
	}

	private void renderTextOnTopSide(final RenderEngine3D<GameEngine> renderEngine, final float dx, final float dy, final float dz, final String text, final float size, final Color color) {
		final PolygonSpriteBatch batch = renderEngine.renderEngine25D.batch;
		{
			final Matrix4		m			= new Matrix4();
			final float			fontSize	= font.getLineHeight();
			final float			scaling		= size / fontSize;
			final GlyphLayout	layout		= new GlyphLayout();
			layout.setText(font, text);
			final float	width	= layout.width;		// contains the width of the current set text
			final float	height	= layout.height;	// contains the height of the current set text
			// on top
			{
				m.setToTranslation(translation.x - width * scaling / 2.0f + dx, translation.y + TRADER_SIZE_Y / 2.0f + MINIMUM_DISTANCE, translation.z - height * scaling / 2.0f - dy);
				m.rotate(Vector3.X, -90);
				m.scale(scaling, scaling, 1f);

			}
			batch.setTransformMatrix(m);
			font.setColor(color);
			font.draw(batch, text, 0, 0);
		}
	}

}
