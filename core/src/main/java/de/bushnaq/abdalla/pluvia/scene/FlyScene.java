package de.bushnaq.abdalla.pluvia.scene;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.bushnaq.abdalla.engine.GameObject;
import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.engine.ModelManager;
import de.bushnaq.abdalla.pluvia.scene.model.fly.Fly;

/**
 * @author kunterbunt
 *
 */
public class FlyScene extends AbstractScene {

	public FlyScene(RenderEngine3D<GameEngine> renderEngine, List<GameObject<GameEngine>> renderModelInstances) {
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
		// white fog
		renderEngine.getFog().setBeginDistance(20f);
		renderEngine.getFog().setFullDistance(50f);
		renderEngine.getFog().setColor(Color.WHITE);
		// water
		renderEngine.getWater().setPresent(false);
		// mirror
		renderEngine.getMirror().setPresent(false);

		// generate instances
		createPlane(Color.WHITE);

		createFly(0.02f, 0.02f);
	}

	protected void createFly(float minSize, float maxSize) {
		Vector3	min	= renderEngine.getSceneBox().min;
		Vector3	max	= renderEngine.getSceneBox().max;
		for (int i = 0; i < Math.min(renderEngine.getGameEngine().context.getMaxSceneObjects(), 500); i++) {
			int			type	= rand.nextInt(ModelManager.MAX_NUMBER_OF_FLY_MODELS);
			float		size	= minSize + (float) Math.random() * (maxSize - minSize);
			BoundingBox	b		= new BoundingBox(new Vector3(min.x + 4f, size / 2, min.z + 5), new Vector3(max.x - 4f, 4f, 0));
			Fly			firefly	= new Fly(renderEngine, type, size, b);
//			fish.setMaxSpeed(0.3f);
//			fish.setAccellerationDistance(5f);
			renderEngine.getGameEngine().context.fireflyList.add(firefly);
		}
	}

	@Override
	public Color getInfoColor() {
		return Color.BLACK;
	}

}
