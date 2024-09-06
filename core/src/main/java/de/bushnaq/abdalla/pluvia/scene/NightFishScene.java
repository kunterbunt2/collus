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
public class NightFishScene extends AbstractScene {

	public NightFishScene(RenderEngine3D<GameEngine> renderEngine, List<GameObject<GameEngine>> renderModelInstances) {
		super(renderEngine, renderModelInstances);
	}

	@Override
	public void create() {
		super.create();
		logo.setColor(getInfoColor());
		version.setColor(getInfoColor());
		renderEngine.setSkyBox(false);
		renderEngine.setShadowEnabled(true);
		// time
		renderEngine.setAlwaysDay(true);
		renderEngine.setDynamicDayTime(false);
		renderEngine.setFixedDayTime(8);
		// fog
		renderEngine.getFog().setBeginDistance(20f);
		renderEngine.getFog().setFullDistance(50f);
		renderEngine.getFog().setColor(Color.BLACK);
		// water
		renderEngine.getWater().setPresent(true);
		renderEngine.getWater().setWaveStrength(0.05f);
		renderEngine.getWater().setWaveSpeed(0.01f);
		renderEngine.getWater().setRefractiveMultiplicator(4f);
		createWater();
		// mirror
		renderEngine.getMirror().setPresent(false);
		createCity(renderEngine, 0, 0, -CITY_SIZE * 5, false, 2f);
		createFish(1.0f, 0.2f);
	}

	@Override
	public Color getInfoColor() {
		return Color.WHITE;
	}

}
