package de.bushnaq.abdalla.pluvia.scene;

import java.util.List;

import com.badlogic.gdx.graphics.Color;

import de.bushnaq.abdalla.engine.GameObject;
import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;

/**
 * @author kunterbunt
 *
 */
public class DeepSeaScene extends AbstractScene {
	public DeepSeaScene(RenderEngine3D<GameEngine> renderEngine, List<GameObject<GameEngine>> renderModelInstances) {
		super(renderEngine, renderModelInstances);
	}

	@Override
	public void create() {
		super.create();
		logo.setColor(getInfoColor());
		version.setColor(getInfoColor());
		renderEngine.setShadowEnabled(false);
		renderEngine.getFog().setColor(Color.BLUE);
		renderEngine.getFog().setBeginDistance(13f);
		renderEngine.getFog().setFullDistance(25f);
		renderEngine.getWater().setPresent(false);
//		createCity(gameEngine, 0, 0, -CITY_SIZE * 5);
		createFish(0.1f, 3f);
//		createFirefly();
	}

	@Override
	public Color getInfoColor() {
		return Color.WHITE;
	}

}
