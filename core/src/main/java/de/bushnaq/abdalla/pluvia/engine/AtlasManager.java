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

import de.bushnaq.abdalla.pluvia.desktop.Context;

/**
 * @author kunterbunt
 *
 */
public class AtlasManager {
	private static String assetsFolderName;

	public static String getAssetsFolderName() {
		return assetsFolderName;
	}

	public BitmapFont	aeroFont;
	public TextureAtlas	atlas;
	public FontData[]	fontDataList;
	public BitmapFont	logoFont;
	public BitmapFont	modelFont;
	public BitmapFont	smallFont;
	public AtlasRegion	systemTextureRegion;

	public BitmapFont	versionFont;

	public AtlasManager() {
	}

	public void dispose() {
		for (final FontData fontData : fontDataList) {
			fontData.font.dispose();
		}
		atlas.dispose();
		VisUI.dispose();
	}

	public void init() {
		assetsFolderName = Context.getAppFolderName() + "/assets";

		FontData[] temp = { //
				new FontData("model-font", assetsFolderName + "/fonts/Roboto-Bold.ttf", 64), //
				new FontData("Aero-font", assetsFolderName + "/fonts/Aero.ttf", 128), //
				new FontData("logo-font", assetsFolderName + "/fonts/Roboto-Thin.ttf", 128), //
				new FontData("small-font", assetsFolderName + "/fonts/Roboto-Bold.ttf", 10), //
				new FontData("version-font", assetsFolderName + "/fonts/Roboto-Thin.ttf", 16), //
		};
		fontDataList = temp;

		initTextures();
		initFonts();
		for (Texture texture : atlas.getTextures()) {
			texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		}
	}

	private void initFonts() {
		for (FontData element : fontDataList) {
			final FontData		fontData	= element;
			final AtlasRegion	atlasRegion	= atlas.findRegion(fontData.name);
			atlasRegion.getRegionWidth();
			atlasRegion.getRegionHeight();
			final PixmapPacker			packer		= new PixmapPacker(atlasRegion.getRegionWidth(), atlasRegion.getRegionHeight(), Format.RGBA8888, 1, false);
			final FreeTypeFontGenerator	generator	= new FreeTypeFontGenerator(Gdx.files.internal(fontData.file));
			final FreeTypeFontParameter	parameter	= new FreeTypeFontParameter();
			parameter.size = (fontData.fontSize);
			parameter.packer = packer;
			final BitmapFont generateFont = generator.generateFont(parameter);
			generator.dispose(); // don't forget to dispose to avoid memory leaks!
			fontData.font = new BitmapFont(generateFont.getData(), atlas.findRegion(fontData.name), true);
			packer.dispose();
			generateFont.dispose();
			fontData.font.setUseIntegerPositions(false);
		}
		modelFont = fontDataList[0].font;
		aeroFont = fontDataList[1].font;
		logoFont = fontDataList[2].font;
		smallFont = fontDataList[3].font;
		versionFont = fontDataList[4].font;
	}

	private void initTextures() {
		atlas = new TextureAtlas(Gdx.files.internal(assetsFolderName + "/atlas/atlas.atlas"));
		systemTextureRegion = atlas.findRegion("system");
		VisUI.load(SkinScale.X2);
		VisUI.getSkin().getFont("default-font").getData().markupEnabled = true;
		VisUI.getSkin().getFont("small-font").getData().markupEnabled = true;
		Colors.put("BOLD", new Color(0x1BA1E2FF));
	}
}
