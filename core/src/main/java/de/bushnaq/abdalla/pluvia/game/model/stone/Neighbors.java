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

package de.bushnaq.abdalla.pluvia.game.model.stone;

public class Neighbors {
    public boolean xNegAttached;
    public boolean xPosAttached;
    public boolean yNegAttached;
    public boolean yPosAttached;
    public boolean zNegAttached;
    public boolean zPosAttached;

    public Neighbors() {

    }

    public Neighbors(Neighbors copy) {
        this.xNegAttached = copy.xNegAttached;
        this.xPosAttached = copy.xPosAttached;
        this.yNegAttached = copy.yNegAttached;
        this.yPosAttached = copy.yPosAttached;
        this.zNegAttached = copy.zNegAttached;
        this.zPosAttached = copy.zPosAttached;
    }

    public boolean getXNegAttached() {
        return xNegAttached;
    }

    public boolean getXPosAttached() {
        return xPosAttached;
    }

    public boolean getYNegAttached() {
        return yNegAttached;
    }

    public boolean getYPosAttached() {
        return yPosAttached;
    }

    public boolean getZNegAttached() {
        return zNegAttached;
    }

    public boolean getZPosAttached() {
        return zPosAttached;
    }

    public void rotateMinusX() {
        Neighbors copy = new Neighbors(this);
//        xNegAttached = copy.xNegAttached;
//        xPosAttached = copy.xPosAttached;
        zPosAttached = copy.yNegAttached;
        zNegAttached = copy.yPosAttached;
        yNegAttached = copy.zNegAttached;
        yPosAttached = copy.zPosAttached;
    }

    public void rotateMinusY() {
        Neighbors copy = new Neighbors(this);
        zNegAttached = copy.xNegAttached;
        zPosAttached = copy.xPosAttached;
//        yNegAttached = copy.yNegAttached;
//        yPosAttached = copy.yPosAttached;
        xPosAttached = copy.zNegAttached;
        xNegAttached = copy.zPosAttached;
    }

    public void rotateMinusZ() {
        Neighbors copy = new Neighbors(this);
        yPosAttached = copy.xNegAttached;
        yNegAttached = copy.xPosAttached;
        xNegAttached = copy.yNegAttached;
        xPosAttached = copy.yPosAttached;
//        zNegAttached = copy.zNegAttached;
//        zPosAttached = copy.zPosAttached;
    }

    public void rotatePlusX() {
        Neighbors copy = new Neighbors(this);
//        xNegAttached = copy.xNegAttached;
//        xPosAttached = copy.xPosAttached;
        zNegAttached = copy.yNegAttached;
        zPosAttached = copy.yPosAttached;
        yPosAttached = copy.zNegAttached;
        yNegAttached = copy.zPosAttached;
    }

    public void rotatePlusY() {
        Neighbors copy = new Neighbors(this);
        zPosAttached = copy.xNegAttached;
        zNegAttached = copy.xPosAttached;
//        yNegAttached = copy.yNegAttached;
//        yPosAttached = copy.yPosAttached;
        xNegAttached = copy.zNegAttached;
        xPosAttached = copy.zPosAttached;
    }

    public void rotatePlusZ() {
        Neighbors copy = new Neighbors(this);
        yNegAttached = copy.xNegAttached;
        yPosAttached = copy.xPosAttached;
        xPosAttached = copy.yNegAttached;
        xNegAttached = copy.yPosAttached;
//        zNegAttached = copy.zNegAttached;
//        zPosAttached = copy.zPosAttached;
    }

    public void setXNegAttached(boolean xNegAttached) {
        this.xNegAttached = xNegAttached;
    }

    public void setXPosAttached(boolean xPosAttached) {
        this.xPosAttached = xPosAttached;
    }

    public void setYNegAttached(boolean yNegAttached) {
        this.yNegAttached = yNegAttached;
    }

    public void setYPosAttached(boolean yPosAttached) {
        this.yPosAttached = yPosAttached;
    }

    public void setZNegAttached(boolean zNegAttached) {
        this.zNegAttached = zNegAttached;
    }

    public void setZPosAttached(boolean zPosAttached) {
        this.zPosAttached = zPosAttached;
    }
}
