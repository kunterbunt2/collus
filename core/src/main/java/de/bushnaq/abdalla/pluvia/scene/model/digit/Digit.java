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

package de.bushnaq.abdalla.pluvia.scene.model.digit;

import de.bushnaq.abdalla.engine.GameObject;
import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.engine.Renderable;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.game.Level;

import java.util.List;

/**
 * @author kunterbunt
 */
public class Digit extends Renderable<GameEngine> {
    private       char                         digit = 0;
    private final int                          digitPosition;
    private final DigitType                    digitType;
    private final List<GameObject<GameEngine>> renderModelInstances;
    private       String                       text;
    float x = 0;
    float y = 0;
    float z = 0;

    public Digit(List<GameObject<GameEngine>> renderModelInstances, RenderEngine3D<GameEngine> renderEngine, float x, float y, float z, int digitPosition, DigitType digitType) {
        this.renderModelInstances = renderModelInstances;
        set3DRenderer(new Digit3DRenderer(this));
        this.x             = x;
        this.y             = y;
        this.z             = z;
        this.digitPosition = digitPosition;
        this.digitType     = digitType;
        get3DRenderer().create(renderEngine);
    }

    public char getDigit() {
        return digit;
    }

    public int getDigitPosition() {
        return digitPosition;
    }

    public DigitType getDigitType() {
        return digitType;
    }

    public List<GameObject<GameEngine>> getRenderModelInstances() {
        return renderModelInstances;
    }

    public String getText() {
        return text;
    }

    public void setDigit(char digit) {
        this.digit = digit;
    }

    public void update(Level level) {
        String sI = "";
        switch (getDigitType()) {
            case score:
                sI = String.format("%+05d", level.getScore());
                break;
            case steps:
                sI = String.format("%+05d", level.getSteps());
                break;
            case name:
                text = level.getName();
                break;
            case seed:
                sI = String.format("%4d", level.getSeed());
                break;
        }
        if (getDigitPosition() >= sI.length())
            setDigit('0');
        else {
            char c = sI.charAt(getDigitPosition());
            setDigit(c);
        }
    }

}