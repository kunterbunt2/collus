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

package de.bushnaq.abdalla.pluvia.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import de.bushnaq.abdalla.pluvia.desktop.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kunterbunt
 */
public class AudioManager {
    public static final String  DROP   = "drop";
    public static final String  SCORE  = "score";
    public static final String  STICKY = "sticky";
    public static final String  TILT   = "score";
    public static final String  VANISH = "vanish";
    private final       Context context;
    Map<String, Sound> map = new HashMap<>();

    public AudioManager(Context context) {
        this.context = context;
        add(SCORE, "/sound/score.mp3");
        add(TILT, "/sound/tilt.mp3");
        add(STICKY, "/sound/magnet.mp3");
        add(DROP, "/sound/drop.mp3");
        add(VANISH, "/sound/vanish.mp3");
    }

    private void add(String tag, String fileName) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(AtlasManager.getAssetsFolderName() + fileName));
        map.put(tag, sound);
    }

    public void dispose() {
        for (Sound sound : map.values()) {
            sound.dispose();
        }
        map.clear();
    }

//	public Sound get(String tag) {
//		return map.get(tag);
//	}

    public void play(String tag) {
        map.get(tag).play((context.getAmbientAudioVolumenProperty()) / 100f);
    }
}
