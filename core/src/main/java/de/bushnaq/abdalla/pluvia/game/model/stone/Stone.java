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
package de.bushnaq.abdalla.pluvia.game.model.stone;

import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.engine.Renderable;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.game.StoneOption;
import de.bushnaq.abdalla.pluvia.util.RcBoolean;

/**
 * @author kunterbunt
 */
public class Stone extends Renderable<GameEngine> implements Comparable<Stone> {
    public static final int       CLEAR_CUBE      = 13;
    public static final int       FIXED_CUBE      = 14;//is not affected by gravity
    public static final int       INVISIBLE_CUBE  = 14;
    public static final int       MAGNETIC_STONE1 = 10;//magnetically attaches to type MAGNETIC_STONE1 stones
    public static final int       MAGNETIC_STONE2 = 11;//magnetically attaches to type MAGNETIC_STONE2 stones
    public static final int       MAGNETIC_STONE3 = 12;//magnetically attaches to type MAGNETIC_STONE3 stones
    public static final int       UHU_CUBE        = 9;//magnetically attaches to any type stones
    private final       RcBoolean canDrop         = new RcBoolean(false);
    private final       RcBoolean canMoveLeft     = new RcBoolean(false);
    private final       RcBoolean canMoveRight    = new RcBoolean(false);
    private final       RcBoolean cannotDrop      = new RcBoolean(false);
    private final       RcBoolean cannotMoveLeft  = new RcBoolean(false);
    private final       RcBoolean cannotMoveRight = new RcBoolean(false);
    private final       RcBoolean dropping        = new RcBoolean(false);
    private final       RcBoolean movingLeft      = new RcBoolean(false);
    private final       RcBoolean movingRight     = new RcBoolean(false);
    public final        String    name;
    private final       RcBoolean pushingLeft     = new RcBoolean(false);
    private final       RcBoolean pushingRight    = new RcBoolean(false);
    public              int       score           = 0;
    public              float     tx              = 0;
    public              float     ty              = 0;
    public              int       type            = 0;
    public              float     tz              = 0;
    private final       RcBoolean vanishing       = new RcBoolean(false);
    public              int       x               = 0;
    private final       RcBoolean xNegAttached    = new RcBoolean(false);
    private final       RcBoolean xPosAttached    = new RcBoolean(false);
    public              int       y               = 0;
    private final       RcBoolean yNegAttached    = new RcBoolean(false);
    private final       RcBoolean yPosAttached    = new RcBoolean(false);
    public              int       z               = 0;
    private final       RcBoolean zNegAttached    = new RcBoolean(false);
    private final       RcBoolean zPosAttached    = new RcBoolean(false);

    public Stone(RenderEngine3D<GameEngine> renderEngine, int x, int y, int z, int aType) {
        set3DRenderer(new Stone3DRenderer(this));
        type   = aType;
        this.x = x;
        this.y = y;
        this.z = z;
        name   = String.format("x=%d y=%d z=%d", x, y, z);
        get3DRenderer().create(renderEngine);
    }

    public boolean canVanish() {
        switch (this.type) {
            case MAGNETIC_STONE1:
            case MAGNETIC_STONE2:
            case MAGNETIC_STONE3:
            case UHU_CUBE:
            case CLEAR_CUBE:
                return false;
            default:
                return true;
        }
    }

    public boolean clearCommandAttributes() {
        boolean     somethingHasChanged = false;
        RcBoolean[] attributeList       = {dropping, movingLeft, movingRight};
        for (RcBoolean element : attributeList) {
            if (element.getBooleanValue()) {
                element.setBooleanValue(false);
                somethingHasChanged = true;
            }
        }
        return somethingHasChanged;
    }

    public boolean clearPushingAttributes() {
        boolean     somethingHasChanged = false;
        RcBoolean[] attributeList       = {pushingLeft, pushingRight};
        for (RcBoolean element : attributeList) {
            if (element.getBooleanValue()) {
                element.setBooleanValue(false);
                somethingHasChanged = true;
            }
        }
        return somethingHasChanged;
    }

    public void clearTemporaryAttributes() {
        RcBoolean[] attributeList = {cannotDrop};
        for (RcBoolean element : attributeList) {
            element.setBooleanValue(false);
        }
    }

    @Override
    public int compareTo(Stone o) {
        int v1 = x + y * 100 + z * 10000;
        int v2 = o.x + o.y * 100 + o.z * 10000;
        if (v1 < v2) {
            return -1;
        } else if (v1 == v2) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if ((o == null) || (this.getClass() != o.getClass()))
            return false;
        Stone stone = (Stone) o;
        return x == stone.x && y == stone.y && z == stone.z;
    }

    public String getCanAttributesAsString() {
        String attribute = "Y:";
        if (isCanMoveLeft())
            attribute += "L";
        else
            attribute += "-";
        if (isCanDrop())
            attribute += "D";
        else
            attribute += "-";
        if (isCanMoveRight())
            attribute += "R";
        else
            attribute += "-";
        if (isVanishing())
            attribute += "V";
        else
            attribute += "-";
        return attribute;
    }

    public String getCannotAttributesAsString() {
        String attribute = "N:";
        if (isCannotMoveLeft())
            attribute += "L";
        else
            attribute += "-";
        if (isCannotDrop())
            attribute += "D";
        else
            attribute += "-";
        if (isCannotMoveRight())
            attribute += "R";
        else
            attribute += "-";
        return attribute;
    }

    public String getCoordinatesAsString() {
        return String.format("x=%d y=%d z=%d", x, y, z);
    }

    public String getDoingStatusAsString() {
        String attribute = "D:";
        if (isPushingLeft())
            attribute += "P";
        else
            attribute += "-";
        if (isPushingRight())
            attribute += "P";
        else
            attribute += "-";
        if (isMovingLeft())
            attribute += "M";
        else
            attribute += "-";
        if (isDropping())
            attribute += "D";
        else
            attribute += "-";
        if (isMovingRight())
            attribute += "M";
        else
            attribute += "-";
        return attribute;
    }

    public String getGlueStatusAsString() {
        String attribute = "G:";

        if (getXNegAttached())
            attribute += "L";
        else
            attribute += "_";


        if (getZNegAttached())
            attribute += "B";
        else
            attribute += "_";

        if (getZPosAttached())
            attribute += "F";
        else
            attribute += "_";

        if (getYNegAttached())
            attribute += "U";
        else
            attribute += "_";

        if (getYPosAttached())
            attribute += "D";
        else
            attribute += "_";

        if (getXPosAttached())
            attribute += "R";
        else
            attribute += "_";

        return attribute;
    }

    public StoneOption getMagneticOptions(Stone neighbor) {
        if (neighbor == null)
            return StoneOption.empty;
        else if (isMagneticTo(neighbor))
            return StoneOption.attached;
        return StoneOption.unattached;
    }

    public int getType() {
        return type;
    }

    public boolean getXNegAttached() {
        return xNegAttached.getBooleanValue();
    }

    public boolean getXPosAttached() {
        return xPosAttached.getBooleanValue();
    }

    public boolean getYNegAttached() {
        return yNegAttached.getBooleanValue();
    }

    public boolean getYPosAttached() {
        return yPosAttached.getBooleanValue();
    }

    public boolean getZNegAttached() {
        return zNegAttached.getBooleanValue();
    }

    public boolean getZPosAttached() {
        return zPosAttached.getBooleanValue();
    }

    public boolean isAffectedByGravity() {
        switch (this.type) {
            case FIXED_CUBE:
//            case UHU_CUBE:
                return false;
            default:
                return true;
        }
    }

    public boolean isCanDrop() {
        return canDrop.getBooleanValue() && isAffectedByGravity();
    }

    public boolean isCanMoveLeft() {
        return canMoveLeft.getBooleanValue();
    }

    public boolean isCanMoveRight() {
        return canMoveRight.getBooleanValue();
    }

    public boolean isCannotDrop() {
        return cannotDrop.getBooleanValue() || !isAffectedByGravity();
    }

    public boolean isCannotMoveLeft() {
        return cannotMoveLeft.getBooleanValue();
    }

    public boolean isCannotMoveRight() {
        return cannotMoveRight.getBooleanValue();
    }

    public boolean isDropping() {
        return dropping.getBooleanValue();
    }

    public boolean isMagneticTo(Stone neighbor) {
        if (neighbor != null) {
            switch (this.type) {
                case MAGNETIC_STONE1:
                case MAGNETIC_STONE2:
                case MAGNETIC_STONE3:
                    return this.type == neighbor.type;
                case UHU_CUBE:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    public boolean isMoving() {
        return isDropping() || isMovingLeft() || isMovingRight();
    }

    public boolean isMovingLeft() {
        return movingLeft.getBooleanValue();
    }

    public boolean isMovingRight() {
        return movingRight.getBooleanValue();
    }

    public boolean isPushingLeft() {
        return pushingLeft.getBooleanValue();
    }

    public boolean isPushingRight() {
        return pushingRight.getBooleanValue();
    }

    public boolean isVanishing() {
        return vanishing.getBooleanValue();
    }

    public boolean isVanishingWith(Stone neighbor) {
        if (neighbor == null)
            return false;
        if (neighbor.getType() == type) {
            switch (this.type) {
                case MAGNETIC_STONE1:
                case MAGNETIC_STONE2:
                case MAGNETIC_STONE3:
                case FIXED_CUBE:
                case CLEAR_CUBE:
                    return false;
                default:
                    return true;
            }
        }
        return false;
    }

    public void set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setCanDrop(boolean aCanDrop) {
        canDrop.setBooleanValue(aCanDrop);
    }

    public void setCanMoveLeft(boolean aCanMoveLeft) {
        canMoveLeft.setBooleanValue(aCanMoveLeft);
    }

    public void setCanMoveRight(boolean aCanMoveRight) {
        canMoveRight.setBooleanValue(aCanMoveRight);
    }

    public void setCannotDrop(boolean aCannotDrop) {
        cannotDrop.setBooleanValue(aCannotDrop);
    }

    public void setCannotMoveLeft(boolean aCannotMoveLeft) {
        cannotMoveLeft.setBooleanValue(aCannotMoveLeft);
    }

    public void setCannotMoveRight(boolean aCannotMoveRight) {
        cannotMoveRight.setBooleanValue(aCannotMoveRight);
    }

    public void setDropping(boolean aDropping) {
        dropping.setBooleanValue(aDropping);
    }

    public void setMovingLeft(boolean aMovingLeft) {
        movingLeft.setBooleanValue(aMovingLeft);
    }

    public void setMovingRight(boolean aMovingRight) {
        movingRight.setBooleanValue(aMovingRight);
    }

    public void setPushingLeft(boolean aPushingLeft) {
        pushingLeft.setBooleanValue(aPushingLeft);
    }

    public void setPushingRight(boolean aPushingRight) {
        pushingRight.setBooleanValue(aPushingRight);
    }

    public void setTx(float tx) {
        this.tx = tx;
    }

    public void setTy(float ty) {
        this.ty = ty;
    }

    public void setTz(float tz) {
        this.tz = tz;
    }

    public void setXNegAttached(boolean xNegAttached) {
        this.xNegAttached.setBooleanValue(xNegAttached);
    }

    public void setXPosAttached(boolean xPosAttached) {
        this.xPosAttached.setBooleanValue(xPosAttached);
    }

    public void setYNegAttached(boolean yNegAttached) {
        this.yNegAttached.setBooleanValue(yNegAttached);
    }

    public void setYPosAttached(boolean yPosAttached) {
        this.yPosAttached.setBooleanValue(yPosAttached);
    }

    public void setZNegAttached(boolean zNegAttached) {
        this.zNegAttached.setBooleanValue(zNegAttached);
    }

    public void setZPosAttached(boolean zPosAttached) {
        this.zPosAttached.setBooleanValue(zPosAttached);
    }

    public void setisVanishing(boolean aCanVanish) {
        vanishing.setBooleanValue(aCanVanish);
    }
}