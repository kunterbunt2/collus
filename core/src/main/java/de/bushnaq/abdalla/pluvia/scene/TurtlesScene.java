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
public class TurtlesScene extends AbstractScene {

	public TurtlesScene(RenderEngine3D<GameEngine> renderEngine, List<GameObject<GameEngine>> renderModelInstances) {
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
		renderEngine.setDynamicDayTime(true);
		renderEngine.setFixedDayTime(8);
		// white fog
		renderEngine.getFog().setColor(Color.WHITE);
		renderEngine.getFog().setBeginDistance(20f);
		renderEngine.getFog().setFullDistance(50f);
		// water
		renderEngine.getWater().setPresent(false);
		// mirror
		renderEngine.getMirror().setPresent(false);
		renderEngine.getMirror().setReflectivity(0.5f);
//		createMirror(Color.WHITE);

//		createPlane(Color.WHITE);
		createTurtles(1f, 1f);
	}

	@Override
	public Color getInfoColor() {
		return Color.BLACK;
	}

}
