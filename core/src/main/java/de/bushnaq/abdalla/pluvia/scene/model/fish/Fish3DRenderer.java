package de.bushnaq.abdalla.pluvia.scene.model.fish;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Vector3;

import de.bushnaq.abdalla.engine.GameObject;
import de.bushnaq.abdalla.engine.ObjectRenderer;
import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.model.ModelInstanceHack;

/**
 * @author kunterbunt
 *
 */
public class Fish3DRenderer extends ObjectRenderer<GameEngine> {
	private static final float		FISH_X_SIZE				= 1f;
	private static final float		FISH_Y_SIZE				= 1f;
	private static final float		FISH_Z_SIZE				= 1f;
	private static final float		NORMAL_LIGHT_INTENSITY	= 5f;
	private final Vector3			direction				= new Vector3();	// intermediate value
	private Fish					fish;
	private GameObject				instance;
	// private GameObject poiInstance;
	private float					lightIntensity			= 0f;
	private boolean					lightIsOne				= false;
	private final List<PointLight>	pointLight				= new ArrayList<>();
	private final Vector3			translation				= new Vector3();	// intermediate value

	public Fish3DRenderer(final Fish patch) {
		this.fish = patch;
	}

	@Override
	public void create(final RenderEngine3D<GameEngine> renderEngine) {
		if (instance == null) {
			instance = new GameObject(new ModelInstanceHack(renderEngine.getGameEngine().modelManager.fishCube[fish.getType()]), null);
//			poiInstance = new GameObject(new ModelInstanceHack(gameEngine.modelManager.fishCubePbr[0]), fish);
			renderEngine.addDynamic(instance);
//			gameEngine.renderEngine.addDynamic(poiInstance);
			instance.update();
//			poiInstance.update();
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
				lightIntensity += Math.signum(NORMAL_LIGHT_INTENSITY - lightIntensity) * 1f;
			for (PointLight pl : pointLight) {
				pl.intensity = lightIntensity;
			}
		}
	}

	private void turnLightOff(final GameEngine gameEngine) {
		if (lightIsOne) {
			for (PointLight pl : pointLight) {
				gameEngine.renderEngine.remove(pl, true);
			}
			lightIsOne = false;
		}
	}

	private void turnLightOn(final RenderEngine3D<GameEngine> renderEngine) {
		if (!lightIsOne) {
			lightIntensity = 0f;
			Color color;
			if (renderEngine.isPbr()) {
				Material			material	= instance.instance.model.materials.get(0);
				Attribute			attribute	= material.get(PBRColorAttribute.BaseColorFactor);
				PBRColorAttribute	a			= (PBRColorAttribute) attribute;
				color = a.color;
			} else {
				Material		material	= instance.instance.model.materials.get(0);
				Attribute		attribute	= material.get(ColorAttribute.Diffuse);
				ColorAttribute	a			= (ColorAttribute) attribute;
				color = a.color;
			}

			final PointLight light = new PointLight().set(color, 0f, 0f, 0f, lightIntensity);
			pointLight.add(light);
			renderEngine.add(light, true);
			lightIsOne = true;
		}

	}

	@Override
	public void update(final float x, final float y, final float z, final RenderEngine3D<GameEngine> renderEngine, final long currentTime, final float timeOfDay, final int index, final boolean selected) throws Exception {
		fish.calculateEngineSpeed(renderEngine.getGameEngine().context.isEnableTime());
		if (fish.position != null)
			fish.speed.set(fish.poi.x - fish.position.x, 0, fish.poi.z - fish.position.z);
		else
			fish.speed.set(1, 0, 1);
		fish.speed.nor();
		fish.speed.scl(fish.currentMaxEngineSpeed);
		final float	scalex	= (fish.poi.x - fish.position.x);
		final float	scaley	= (fish.poi.y - fish.position.y);
		final float	scalez	= (fish.poi.z - fish.position.z);
		direction.set(scalex, scaley, scalez);
		translation.x = (fish.position.x + (fish.poi.x - fish.position.x) * fish.destinationPlanetDistanceProgress / fish.destinationPlanetDistance);
		translation.y = (fish.position.y + (fish.poi.y - fish.position.y) * fish.destinationPlanetDistanceProgress / fish.destinationPlanetDistance);
		translation.z = (fish.position.z + (fish.poi.z - fish.position.z) * fish.destinationPlanetDistanceProgress / fish.destinationPlanetDistance);

		for (PointLight pl : pointLight) {
			pl.setPosition(translation);
		}
		turnLightOn(renderEngine);
		tuneLightIntensity();
		{
			instance.instance.transform.setToTranslation(translation);
			instance.instance.transform.rotateTowardDirection(direction, Vector3.Y);
			instance.instance.transform.scale(FISH_X_SIZE * fish.getSize(), FISH_Y_SIZE * fish.getSize(), FISH_Z_SIZE * fish.getSize());
			instance.update();

//			poiInstance.instance.transform.setToTranslation(fish.poi);
//			poiInstance.instance.transform.scale(FISH_X_SIZE, FISH_Y_SIZE, FISH_Z_SIZE);
//			poiInstance.update();
		}

	}
}
