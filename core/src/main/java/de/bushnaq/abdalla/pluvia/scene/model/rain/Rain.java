package de.bushnaq.abdalla.pluvia.scene.model.rain;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.scene.model.fish.Fish;
import de.bushnaq.abdalla.pluvia.util.TimeUnit;

/**
 * @author kunterbunt
 */
public class Rain extends Fish {
//	int		pause	= 0;
	float radius = 10f;

	public Rain(RenderEngine3D<GameEngine> renderEngine, int type, float size, BoundingBox cage) {
		super();
		this.gameEngine = gameEngine;
		set3DRenderer(new Rain3DRenderer(this));
		this.type = type;
		this.size = size;
		this.cage = cage;
		position = new Vector3(0, 0, 0);
		choseStartingPoint(gameEngine);
		position.set(poi);
		get3DRenderer().create(renderEngine);
		setMaxSpeed(0.7f);
		setMinSpeed(0.7f);
		setAccellerationDistance(0f);
		currentMaxEngineSpeed = minSpeed;
	}

	@Override
	public void advanceInTime(long currentTime) {
		timeDelta = currentTime - lastTimeAdvancement;
		lastTimeAdvancement = currentTime;
		final float delta = (currentMaxEngineSpeed * timeDelta) / TimeUnit.TICKS_PER_DAY;
		position.y -= delta;

		{
			if (position.y < -1) {
				// we have arrived
				position = new Vector3(0, 0, 0);
				choseStartingPoint(gameEngine);
				position.set(poi);
			}
		}

	}

	@Override
	protected void choseStartingPoint(GameEngine gameEngine) {
		{
			poi.y = 10f + (float) Math.random() * 10;
		}
		{
			float	minX	= Math.max(cage.getCenterX() - cage.getWidth() / 2, position.x - radius);
			float	maxX	= Math.min(cage.getCenterX() + cage.getWidth() / 2, position.x + radius);
			poi.x = minX + (maxX - minX) * (float) Math.random();
		}
		{
			float	minZ	= Math.max(cage.getCenterZ() - cage.getDepth() / 2, position.z - radius);
			float	maxZ	= Math.min(cage.getCenterZ() + cage.getDepth() / 2, position.z + radius);
			poi.z = minZ + (maxZ - minZ) * (float) Math.random();
		}
	}

}