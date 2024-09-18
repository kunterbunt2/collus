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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;
import de.bushnaq.abdalla.engine.util.AtlasGenerator;
import de.bushnaq.abdalla.engine.util.FontData;
import de.bushnaq.abdalla.pluvia.desktop.Context;

import java.io.File;

/**
 * @author kunterbunt
 */
public class AtlasManager {
    public         BitmapFont    aeroFont;
    private static String        assetsFolderName;
    public         TextureAtlas  atlas;
    public         FontData[]    fontData = {
            new FontData("model-font", Context.getAppFolderName() + "/assets/fonts/Roboto-Bold.ttf", 64),//
            new FontData("Aero-font", Context.getAppFolderName() + "/assets/fonts/Aero.ttf", 128), //
            new FontData("logo-font", Context.getAppFolderName() + "/assets/fonts/Roboto-Thin.ttf", 128), //
            new FontData("small-font", Context.getAppFolderName() + "/assets/fonts/Roboto-Bold.ttf", 10), //
            new FontData("version-font", Context.getAppFolderName() + "/assets/fonts/Roboto-Thin.ttf", 16) //
    };
    //    public         FontData[]   fontData;
    public         BitmapFont    logoFont;
    public         BitmapFont    modelFont;
    public         BitmapFont    smallFont;
    public         AtlasRegion[] stoneTextureRegion;
    public         AtlasRegion   systemTextureRegion;
    public         BitmapFont    versionFont;

    public AtlasManager() {
    }

    public void dispose() {
        for (final FontData fontData : fontData) {
            fontData.font.dispose();
        }
        atlas.dispose();
        VisUI.dispose();
    }

    public static String getAssetsFolderName() {
        return assetsFolderName;
    }

    public void init() throws Exception {
        assetsFolderName = Context.getAppFolderName() + "/assets/";

        initTextures();
        initFonts();
        for (Texture texture : atlas.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
    }

    private void initFonts() {
        for (FontData element : fontData) {
            final FontData    fontData    = element;
            final AtlasRegion atlasRegion = atlas.findRegion(fontData.name);
            atlasRegion.getRegionWidth();
            atlasRegion.getRegionHeight();
            final PixmapPacker          packer    = new PixmapPacker(atlasRegion.getRegionWidth(), atlasRegion.getRegionHeight(), Format.RGBA8888, 1, false);
            final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontData.file));
            final FreeTypeFontParameter parameter = new FreeTypeFontParameter();
            parameter.size   = (fontData.fontSize);
            parameter.packer = packer;
            final BitmapFont generateFont = generator.generateFont(parameter);
            generator.dispose(); // don't forget to dispose to avoid memory leaks!
            fontData.font = new BitmapFont(generateFont.getData(), atlas.findRegion(fontData.name), true);
            packer.dispose();
            generateFont.dispose();
            fontData.font.setUseIntegerPositions(false);
        }
        modelFont   = fontData[0].font;
        aeroFont    = fontData[1].font;
        logoFont    = fontData[2].font;
        smallFont   = fontData[3].font;
        versionFont = fontData[4].font;
    }

    private void initTextures() throws Exception {
        AtlasGenerator atlasGenerator = new AtlasGenerator();
        atlasGenerator.setOutputFolder(getAssetsFolderName() + "atlas/");
        atlasGenerator.setInputFolders(new File[]{new File(getAssetsFolderName() + "textures/"), new File(getAssetsFolderName() + "ui/")});
        atlasGenerator.setFontData(fontData);
        atlasGenerator.generateIfNeeded();
        atlas = new TextureAtlas(Gdx.files.internal(assetsFolderName + "/atlas/atlas.atlas"));

        systemTextureRegion   = atlas.findRegion("system");
        stoneTextureRegion    = new AtlasRegion[6];
        stoneTextureRegion[0] = atlas.findRegion("stone-01");
        stoneTextureRegion[1] = atlas.findRegion("stone-02");
        stoneTextureRegion[2] = atlas.findRegion("stone-03");
        stoneTextureRegion[3] = atlas.findRegion("stone-04");
        stoneTextureRegion[4] = atlas.findRegion("stone-05");
        stoneTextureRegion[5] = atlas.findRegion("stone-06");
        VisUI.load(SkinScale.X2);
        VisUI.getSkin().getFont("default-font").getData().markupEnabled = true;
        VisUI.getSkin().getFont("small-font").getData().markupEnabled   = true;
        Colors.put("BOLD", new Color(0x1BA1E2FF));
    }
}
