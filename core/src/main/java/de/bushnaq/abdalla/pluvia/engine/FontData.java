package de.bushnaq.abdalla.pluvia.engine;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * @author kunterbunt
 *
 */
public class FontData {
	public String		file;
	public BitmapFont	font;
	public int			fontSize;
	public String		name;

	public FontData(final String name, final String file, final int fontSize) {
		this.name = name;
		this.file = file;
		this.fontSize = fontSize;
	}
}
