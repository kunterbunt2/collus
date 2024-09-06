package de.bushnaq.abdalla.pluvia.desktop;

import java.util.ArrayList;

import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.engine.Renderable;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;

/**
 * @author kunterbunt
 *
 */
public class ModelList<T extends Renderable> extends ArrayList<T> {
	public void destroy(RenderEngine3D<GameEngine> renderEngine) {
		for (T t : this) {
			t.get3DRenderer().destroy(renderEngine);
		}
		clear();
	}
}
