package de.bushnaq.abdalla.pluvia.scene.model.firefly;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
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
public class Firefly3DRenderer extends ObjectRenderer<GameEngine> {
	private static Color			DIAMON_BLUE_COLOR		= new Color(0x006ab6ff);
	private static Color			GRAY_COLOR				= new Color(0x404853ff);
	private static final float		NORMAL_LIGHT_INTENSITY	= 10f;
	private static Color			POST_GREEN_COLOR		= new Color(0x00614eff);
	private static Color			SCARLET_COLOR			= new Color(0xb00233ff);
	private static final float		SIZE_X					= 1.0f;
	private static final float		SIZE_Y					= 1.0f;
	private static final float		SIZE_Z					= 1.0f;
	private final Vector3			direction				= new Vector3();		// intermediate value
	private Firefly					firefly;
	private GameObject				instance;
	private final Vector3			lightDelta;
	private float					lightIntensity			= 0f;

	private boolean					lightIsOne				= false;

	private Vector3					lightPosition			= new Vector3();

	private final List<PointLight>	pointLight				= new ArrayList<>();

	private final Vector3			translation				= new Vector3();		// intermediate value

	public Firefly3DRenderer(final Firefly patch) {
		this.firefly = patch;
		lightDelta = new Vector3((float) Math.random() * firefly.getSize() * 2, (float) Math.random() * firefly.getSize() * 2, (float) Math.random() * firefly.getSize() * 2);
	}

	@Override
	public void create(final RenderEngine3D<GameEngine> renderEngine) {
		if (instance == null) {
			instance = new GameObject(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.firelyModel[firefly.getType()].scene.model), null);
			renderEngine.addDynamic(instance);
			instance.update();
		}
	}

	@Override
	public void destroy(final RenderEngine3D<GameEngine> renderEngine) {
		renderEngine.removeDynamic(instance);
		for (PointLight pl : pointLight) {
			renderEngine.remove(pl, true);
		}
	}

	private void tuneLightIntensity() {
		if (lightIsOne) {
			if (lightIntensity < NORMAL_LIGHT_INTENSITY)
				lightIntensity += Math.signum(NORMAL_LIGHT_INTENSITY - lightIntensity) * 0.1f;
			for (PointLight pl : pointLight) {
				pl.intensity = lightIntensity;
			}
		}
	}

	private void turnLightOff(final RenderEngine3D<GameEngine> renderEngine) {
		if (lightIsOne) {
			for (PointLight pl : pointLight) {
				renderEngine.remove(pl, true);
			}
			lightIsOne = false;
		}
	}

	private void turnLightOn(final RenderEngine3D<GameEngine> renderEngine) {
		if (!lightIsOne) {
			lightIntensity = 0f;
			Color[]				colors	= new Color[] { Color.WHITE, POST_GREEN_COLOR, SCARLET_COLOR, DIAMON_BLUE_COLOR, GRAY_COLOR, Color.CORAL, Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.GOLD,
					Color.MAGENTA, Color.YELLOW };
//			Color				color	= Color.RED;
//			if (gameEngine.renderEngine.isPbr()) {
//				Material			material	= instance.instance.model.materials.get(0);
//				Attribute			attribute	= material.get(PBRColorAttribute.BaseColorFactor);
//				PBRColorAttribute	a			= (PBRColorAttribute) attribute;
//				color = a.color;
//			} else {
//				Material		material	= instance.instance.model.materials.get(0);
//				Attribute		attribute	= material.get(ColorAttribute.Diffuse);
//				ColorAttribute	a			= (ColorAttribute) attribute;
//				color = a.color;
//			}

			final PointLight	light	= new PointLight().set(colors[firefly.getType()], 0f, 0f, 0f, lightIntensity);
			pointLight.add(light);
			renderEngine.add(light, true);
			lightIsOne = true;
		}

	}

	@Override
	public void update(final float x, final float y, final float z, final RenderEngine3D<GameEngine> renderEngine, final long currentTime, final float timeOfDay, final int index, final boolean selected) throws Exception {
		firefly.calculateEngineSpeed(renderEngine.getGameEngine().context.isEnableTime());
		if (firefly.position != null)
			firefly.speed.set(firefly.poi.x - firefly.position.x, 0, firefly.poi.z - firefly.position.z);
		else
			firefly.speed.set(1, 0, 1);
		firefly.speed.nor();
		firefly.speed.scl(firefly.currentMaxEngineSpeed);
		final float	scalex	= (firefly.poi.x - firefly.position.x);
		final float	scaley	= (firefly.poi.y - firefly.position.y);
		final float	scalez	= (firefly.poi.z - firefly.position.z);
		direction.set(scalex, scaley, scalez);
		translation.x = (firefly.position.x + (firefly.poi.x - firefly.position.x) * firefly.destinationPlanetDistanceProgress / firefly.destinationPlanetDistance);
		translation.y = (firefly.position.y + (firefly.poi.y - firefly.position.y) * firefly.destinationPlanetDistanceProgress / firefly.destinationPlanetDistance);
		translation.z = (firefly.position.z + (firefly.poi.z - firefly.position.z) * firefly.destinationPlanetDistanceProgress / firefly.destinationPlanetDistance);
		lightPosition.set(translation);
		lightPosition.add(lightDelta);
		lightDelta.rotate(Vector3.Y, (float) Math.random());
		lightDelta.rotate(Vector3.X, (float) Math.random());
		lightDelta.rotate(Vector3.Z, (float) Math.random());

		if (renderEngine.getGameEngine().context.isEnableTime()) {
			firefly.rotation.x += .2f;
			firefly.rotation.y += .2f;
			firefly.rotation.z += .2f;
		}
		// fly.positionDelta.rotate(Vector3.Y, (float) Math.random() * 2);
//		fly.positionDelta.rotate(Vector3.X, (float) Math.random() * 2);
//		fly.positionDelta.rotate(Vector3.Z, (float) Math.random() * 2);
		for (PointLight pl : pointLight) {
			pl.setPosition(lightPosition);
		}
		turnLightOn(renderEngine);
		tuneLightIntensity();
		{

			instance.instance.transform.setToTranslation(translation);
			instance.instance.transform.rotate(Vector3.X, firefly.rotation.x);
			instance.instance.transform.rotate(Vector3.Y, firefly.rotation.y);
			instance.instance.transform.rotate(Vector3.Z, firefly.rotation.z);

			instance.instance.transform.translate(firefly.positionDelta);
//			instance.instance.transform.rotateTowardDirection(direction, Vector3.Y);
			instance.instance.transform.scale(SIZE_X * firefly.getSize(), SIZE_Y * firefly.getSize(), SIZE_Z * firefly.getSize());
			instance.update();
		}

	}
}
