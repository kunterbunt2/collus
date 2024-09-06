package de.bushnaq.abdalla.pluvia.launcher;

import de.bushnaq.abdalla.engine.IContextFactory;
import de.bushnaq.abdalla.pluvia.desktop.Context;

/**
 * @author kunterbunt
 * 
 */
public class DesktopContextFactory implements IContextFactory {
	private Context context;

	@Override
	public Context create() {
		if (context == null) {
			context = new DesktopContext();
		}
		return getContext();
	}

	Context getContext() {
		return context;
	}

}
