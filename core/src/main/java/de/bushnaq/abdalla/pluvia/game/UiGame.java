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
 * Created on Jul 25, 2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.game;

import de.bushnaq.abdalla.pluvia.game.model.stone.Stone;

/**
 * 9 rows of 4 columns, 6 different stones, with up to 3 stones that can fall at the same time
 *
 * @author kunterbunt
 */
public class UiGame extends Game {
    public UiGame() {
        super(GameName.UI.name(), 0, 0, 0, 0, 7 + 2, false, false);
    }

    protected boolean queryTilt(Stone[][][] patch) {
        return false;
    }

}