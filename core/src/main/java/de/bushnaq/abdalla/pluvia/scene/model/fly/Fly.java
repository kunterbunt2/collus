package de.bushnaq.abdalla.pluvia.scene.model.fly;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.scene.model.fish.Fish;
import de.bushnaq.abdalla.pluvia.util.TimeUnit;

/**
 * @author kunterbunt
 */
public class Fly extends Fish {
	int		pause	= 0;
	float	radius	= 5f;

	public Fly(RenderEngine3D<GameEngine> renderEngine, int type, float size, BoundingBox cage) {
		super();
		this.gameEngine = gameEngine;
		set3DRenderer(new Fly3DRenderer(this));
		this.type = type;
		this.size = size;
		this.cage = cage;
		position = new Vector3(0, 0, 0);
		choseStartingPoint(gameEngine);
		position.set(poi);
		get3DRenderer().create(renderEngine);
		choseStartingPoint(gameEngine);
		setMaxSpeed(0.2f);
		setMinSpeed(0.02f);
		setAccellerationDistance(5f);
		currentMaxEngineSpeed = minSpeed;
	}

	@Override
	public void advanceInTime(long currentTime) {
		timeDelta = currentTime - lastTimeAdvancement;
		lastTimeAdvancement = currentTime;
		final float delta = (currentMaxEngineSpeed * timeDelta) / TimeUnit.TICKS_PER_DAY;
//		if (TimeUnit.isInt(currentTime))
		{
			if (destinationPlanetDistanceProgress + delta >= destinationPlanetDistance) {
				// we have arrived
				if (position.y == cage.getCenterY() - cage.getHeight() / 2) {
					// we started from the ground
					destinationPlanetDistanceProgress += delta;
					position.set(poi);
					choseStartingPoint(gameEngine);
				} else {
					// we started from above, so should be now sitting on the ground
					if (pause == 0) {
						pause = (int) (Math.random() * 1000);
					} else {
						pause--;
						if (pause == 0) {
							position.set(poi);
							choseStartingPoint(gameEngine);
						}
					}
				}
			} else {
				destinationPlanetDistanceProgress += delta;
			}

		}

	}

//	public void calculateEngineSpeed() {
//		currentMaxEngineSpeed = maxSpeed;
//	}

	@Override
	protected void choseStartingPoint(GameEngine gameEngine) {
		if (position.y == cage.getCenterY() - cage.getHeight() / 2) {
			float	minY	= Math.max(cage.getCenterY() - cage.getHeight() / 2, position.y - radius);
			float	maxY	= Math.min(cage.getCenterY() + cage.getHeight() / 2, position.y + radius);
			poi.y = minY + (maxY - minY) * (float) Math.random();
//			poi.y = cage.getCenterY() + cage.getHeight() * (float) Math.random() - cage.getHeight() / 2;
		} else {
			poi.y = cage.getCenterY() - cage.getHeight() / 2;
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
//		{
//			float	minY	= Math.max(cage.getCenterY() - cage.getHeight() / 2, position.y - radius);
//			float	maxY	= Math.min(cage.getCenterY() + cage.getHeight() / 2, position.y + radius);
//			poi.y = minY + (maxY - minY) * (float) Math.random();
//		}

//		poi.x = cage.getCenterX() + cage.getWidth() * (float) Math.random() - cage.getWidth() / 2;
//		poi.z = cage.getCenterZ() + cage.getDepth() * (float) Math.random() - cage.getDepth() / 2;
		destinationPlanetDistance = queryDistance(position, poi);
		destinationPlanetDistanceProgress = 0;
	}

}