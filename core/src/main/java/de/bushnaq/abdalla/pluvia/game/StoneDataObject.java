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

package de.bushnaq.abdalla.pluvia.game;

/**
 * @author kunterbunt
 */
public class StoneDataObject {

    //    public int score = 0;
    public int type = 0;
    public int x;
    public int y;
    public int z;

    public StoneDataObject() {

    }

    public StoneDataObject(int type/*, int score*/, int x, int y, int z) {
        this.type = type;
//        this.score = score;
        this.x = x;
        this.y = y;
        this.z = z;
    }

//    public int getScore() {
//        return score;
//    }

    public int getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

//    public void setScore(int score) {
//        this.score = score;
//    }

    public void setType(int type) {
        this.type = type;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

}
