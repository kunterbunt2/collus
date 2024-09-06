/*
 * Created on 10.07.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.scene.model.turtle;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.scene.model.fish.Fish;

/**
 * @author kunterbunt
 */
public class Turtle extends Fish {

	public Turtle(RenderEngine3D<GameEngine> renderEngine, int type, float size, BoundingBox cage) {
		super();
		this.gameEngine = gameEngine;
		set3DRenderer(new Turtle3DRenderer(this));
		this.type = type;
		this.size = size;
		this.cage = cage;
		position = new Vector3(0, 0, 0);
		choseStartingPoint(gameEngine);
		position.set(poi);
		get3DRenderer().create(renderEngine);
		choseStartingPoint(gameEngine);
		setMaxSpeed(0.01f);
		setMinSpeed(0.002f);
		setAccellerationDistance(5f);
		currentMaxEngineSpeed = minSpeed;
	}

}