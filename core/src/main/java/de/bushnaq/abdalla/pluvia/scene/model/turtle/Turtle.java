/*
 * Copyright (C) 2024 Abdalla Bushnaq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        position  = new Vector3(0, 0, 0);
        choseStartingPoint(gameEngine);
        position.set(poi);
        get3DRenderer().create(renderEngine);
        choseStartingPoint(gameEngine);
        setMaxSpeed(0.1f);
        setMinSpeed(0.02f);
        setAccellerationDistance(5f);
        currentMaxEngineSpeed = minSpeed;
    }

    protected void choseStartingPoint(GameEngine gameEngine) {
        poi.x = cage.getCenterX() + cage.getWidth() * (float) Math.random() - cage.getWidth() / 2;
        poi.y = cage.getCenterY() + getSize() / 2;
        poi.z = cage.getCenterZ() + cage.getDepth() * (float) Math.random() - cage.getDepth() / 2;
//        if (position.x == 0f && position.x == 0f && position.z == 0f)
//            destinationPlanetDistance = poi;
//        else
        destinationPlanetDistance         = queryDistance(position, poi);
        destinationPlanetDistanceProgress = 0;
    }

}