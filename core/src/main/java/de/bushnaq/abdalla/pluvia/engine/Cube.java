package de.bushnaq.abdalla.pluvia.engine;

import com.badlogic.gdx.graphics.Color;

/**
 * @author kunterbunt
 *
 */
public class Cube {
	Color	color;
	String	gltfModel;

	public Cube(Color color, String gltfModel) {
		this.color = color;
		this.gltfModel = gltfModel;
	}
}
