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

package de.bushnaq.abdalla.pluvia.engine.demo;

import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.bushnaq.abdalla.pluvia.engine.demo.DemoMode.*;

public abstract class WaitAfterExecuteAbstractTask extends ScheduledTask {
    protected final Logger   logger = LoggerFactory.getLogger(this.getClass());
    private         DemoMode mode   = START;

    public WaitAfterExecuteAbstractTask(GameEngine gameEngine, float afterSeconds) {
        super(gameEngine, afterSeconds);
    }

    public boolean execute(float deltaTime) {
        boolean returnValue = false;
        switch (mode) {
            case EXECUTE -> {
                subexecute(deltaTime);
                mode = WAIT;
            }
            case START -> {
                mode          = EXECUTE;
                taskStartTime = System.currentTimeMillis();
            }
            case WAIT -> {
                if (taskStartTime + durationMs <= System.currentTimeMillis()) {
                    returnValue = true;
                }
            }
        }
        return returnValue;
    }

    @Override
    public long secondToRun() {
        return taskStartTime + durationMs - System.currentTimeMillis();
    }

}
