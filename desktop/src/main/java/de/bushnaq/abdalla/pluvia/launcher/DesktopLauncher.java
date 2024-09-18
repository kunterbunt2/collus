package de.bushnaq.abdalla.pluvia.launcher;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration.GLEmulation;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import de.bushnaq.abdalla.pluvia.desktop.Context;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.util.MavenPropertiesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kunterbunt
 * 
 */
public class DesktopLauncher implements ApplicationListener {
    //private static final int UNIVERSE_GENERATION_RANDOM_SEED = 1;
    //private static final int UNIVERSE_SIZE                   = 10;
	DesktopContextFactory	contextFactory	= new DesktopContextFactory();
    //LaunchMode            launchMode     = LaunchMode.normal;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static void main(final String[] args) throws Exception {
		DesktopLauncher desktopLauncher = new DesktopLauncher();
		desktopLauncher.start();
	}


	@Override
	public void create() {
		contextFactory.create();
		Gdx.app.exit();// exit the gdx environment that we created just to read the options
	}

	private Lwjgl3ApplicationConfiguration createConfig(Context context) {
		int								foregroundFPS	= context.getForegroundFPSProperty();
		Lwjgl3ApplicationConfiguration	config;
		config = new Lwjgl3ApplicationConfiguration();
		config.useVsync(context.getVsyncProperty());
		config.setForegroundFPS(foregroundFPS);
		config.setResizable(true);
		config.setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.GL32, 3, 2);

//		if (Context.getOeratingSystemType() == OperatingSystem.osx)
		{
			ShaderProgram.prependVertexCode = "#version 330\n"//
					+ "#define GLSL3\n"//
					+ "#ifdef GLSL3\n"//
					+ "#define attribute in\n"//
					+ "#define varying out\n"//
					+ "#endif\n";//
			ShaderProgram.prependFragmentCode = "#version 330\n"//
					+ "#define GLSL3\n"//
					+ "#ifdef GLSL3\n"//
					+ "#define textureCube texture\n"//
					+ "#define texture2D texture\n"//
					+ "#define varying in\n"//
					+ "#endif\n";//
		}

		config.setBackBufferConfig(8, 8, 8, 8, 24, 0, context.getMSAASamples());
		config.setTitle("Collus");
		config.setAutoIconify(true);
		final Monitor[]		monitors		= Lwjgl3ApplicationConfiguration.getMonitors();
		int					monitor			= context.getMonitorProperty();
		final DisplayMode	primaryMode		= Lwjgl3ApplicationConfiguration.getDisplayMode(monitors[monitor]);
		boolean				fullScreenMode	= context.getFullscreenModeProperty();
		if (fullScreenMode)
			config.setFullscreenMode(primaryMode);
		return config;
	}

	@Override
	public void dispose() {
	}

	private void loop() throws Exception {
		boolean restart = false;
		do {
			if (restart)
				logger.info(String.format("Restarting pluvia v%s.", MavenPropertiesProvider.getProperty("module.version")));
			{
				// initialize a gdx environment, so that we can access files
				Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
				new Lwjgl3Application(this, config);
			}

			final GameEngine						gameEngine	= new GameEngine(contextFactory);
			final Lwjgl3ApplicationConfiguration	config		= createConfig(contextFactory.getContext());
            //final GameEngine3D gameEngine = new GameEngine3D(contextFactory, universe, launchMode);
            //universe.create(gameEngine, UNIVERSE_GENERATION_RANDOM_SEED, UNIVERSE_SIZE, 10L * TimeUnit.DAYS_PER_YEAR);
            //final Lwjgl3ApplicationConfiguration config = createConfig(contextFactory.getContext());
			try {
				contextFactory.getContext().restart = false;
				new Lwjgl3Application(gameEngine, config);
			} catch (Throwable e) {
				logger.error(e.getMessage(), e);
			}
			contextFactory.getContext().dispose();
			restart = contextFactory.getContext().restart;
		} while (restart);
	}

	@Override
	public void pause() {
	}

	@Override
	public void render() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void resume() {
	}

	private void start() throws InterruptedException, Exception {
		logger.info("------------------------------------------------------------------------------");
		logger.info(String.format("Starting pluvia v%s.", MavenPropertiesProvider.getProperty("module.version")));
		logger.info("------------------------------------------------------------------------------");
		String property = System.getProperty("user.home");
		logger.info("user.home = " + property);
		loop();
		logger.info("------------------------------------------------------------------------------");
		logger.info(String.format("Shutting down pluvia v%s.", MavenPropertiesProvider.getProperty("module.version")));
		logger.info("------------------------------------------------------------------------------");
	}

}
