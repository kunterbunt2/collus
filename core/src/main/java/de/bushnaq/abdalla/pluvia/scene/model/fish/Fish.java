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

package de.bushnaq.abdalla.pluvia.scene.model.fish;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.scene.model.AbstractActor;

/**
 * @author kunterbunt
 */
public class Fish extends AbstractActor {
//    private   float       accellerationDistance             = 1f;
//    protected BoundingBox cage;
//    public    float       currentMaxEngineSpeed;
//    public    float       destinationPlanetDistance;
//    public    float       destinationPlanetDistanceProgress = 0;
//    protected GameEngine  gameEngine;
//    public    long        lastTimeAdvancement               = 0;
//    protected float       maxSpeed                          = 0.03f;
//    protected float       minSpeed                          = 0.01f;
//    public    Vector3     poi                               = new Vector3();
//    public    Vector3     position;
//    protected float       size;
//    public    Vector3     speed                             = new Vector3(0, 0, 0);
//    protected long        timeDelta                         = 0;
//    public    int         type                              = 0;

    protected Fish() {
    }

    public Fish(RenderEngine3D<GameEngine> renderEngine, int type, float size, BoundingBox cage) {
        set3DRenderer(new Fish3DRenderer(this));
        this.type = type;
        this.size = size;
        this.cage = cage;
        position  = new Vector3(0, 0, 0);
        choseStartingPoint();
        position.set(poi);
        get3DRenderer().create(renderEngine);
        choseStartingPoint();
        currentMaxEngineSpeed = minSpeed;
    }

//    public void advanceInTime(long currentTime) {
//        timeDelta           = currentTime - lastTimeAdvancement;
//        lastTimeAdvancement = currentTime;
//        final float delta = (currentMaxEngineSpeed * timeDelta) / TimeUnit.TICKS_PER_DAY;
//        destinationPlanetDistanceProgress += delta;
//        if (destinationPlanetDistanceProgress >= destinationPlanetDistance /* && TimeUnit.isInt(currentTime) ( ( currentTime - (int)currentTime ) == 0.0f ) */) {
//            position.set(poi);
//            choseStartingPoint(gameEngine);
//        }
//
//    }
//
//    public void calculateEngineSpeed(boolean isEnableTime) {
//        if (isEnableTime) {
//            if (destinationPlanetDistanceProgress <= accellerationDistance && (destinationPlanetDistance - destinationPlanetDistanceProgress <= accellerationDistance)) {
//                currentMaxEngineSpeed = minSpeed + (float) Math.sin((Math.PI / 2) * destinationPlanetDistanceProgress / accellerationDistance) * (maxSpeed - minSpeed);
//            } else if (destinationPlanetDistance - destinationPlanetDistanceProgress <= accellerationDistance) {
//                currentMaxEngineSpeed = minSpeed + (float) Math.sin((Math.PI / 2) * (1.0f - (destinationPlanetDistance - destinationPlanetDistanceProgress) / accellerationDistance)) * (maxSpeed - minSpeed);
//            } else {
//                currentMaxEngineSpeed = minSpeed + (maxSpeed - minSpeed);
//            }
//        } else {
//            currentMaxEngineSpeed = 0f;
//        }
//    }
//
//    protected void choseStartingPoint(GameEngine gameEngine) {
//        poi.x = cage.getCenterX() + cage.getWidth() * (float) Math.random() - cage.getWidth() / 2;
//        poi.y = cage.getCenterY() + cage.getHeight() * (float) Math.random() - cage.getHeight() / 2;
//        poi.z = cage.getCenterZ() + cage.getDepth() * (float) Math.random() - cage.getDepth() / 2;
////        if (position.x == 0f && position.x == 0f && position.z == 0f)
////            destinationPlanetDistance = poi;
////        else
//        destinationPlanetDistance         = queryDistance(position, poi);
//        destinationPlanetDistanceProgress = 0;
//    }
//
//    float clamp(final float value, final float min, final float max) {
//        if (value > max)
//            return max;
//        if (value < min)
//            return min;
//        return value;
//    }
//
//    protected void drawTextCentered(Graphics2D aGraphics, String aText, int aX, int aY, int aStringWidth, int aStringHeight) {
//        aGraphics.drawString(aText, aX - aStringWidth / 2, aY + aStringHeight / 2);
//    }
//
//    public float getAccellerationDistance() {
//        return accellerationDistance;
//    }
//
//    public float getMaxSpeed() {
//        return maxSpeed;
//    }
//
//    public float getMinSpeed() {
//        return minSpeed;
//    }
//
//    public float getSize() {
//        return size;
//    }
//
//    public int getType() {
//        return type;
//    }
//
//    public float queryDistance(final Vector3 position, final Vector3 poi) {
//        return (float) Math.sqrt(Math.pow(position.x - poi.x, 2) + Math.pow(position.y - poi.y, 2) + Math.pow(position.z - poi.z, 2));
//    }
//
//    public void setAccellerationDistance(float accellerationDistance) {
//        this.accellerationDistance = accellerationDistance;
//    }
//
//    public void setMaxSpeed(float maxSpeed) {
//        this.maxSpeed = maxSpeed;
//    }
//
//    public void setMinSpeed(float minSpeed) {
//        this.minSpeed         = minSpeed;
//        currentMaxEngineSpeed = minSpeed;
//    }

}