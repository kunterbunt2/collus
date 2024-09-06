package de.bushnaq.abdalla.pluvia.launcher;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import de.bushnaq.abdalla.pluvia.desktop.ApplicationProperties;
import de.bushnaq.abdalla.pluvia.desktop.Context;
import org.lwjgl.opengl.GL30C;

import java.io.File;
import java.net.URISyntaxException;

/**
 * @author kunterbunt
 */
public class DesktopContext extends Context {


    @Override
    public void disableClipping() {
        if (!Context.isIos())
            Gdx.gl.glDisable(GL30C.GL_CLIP_DISTANCE0);
    }

    @Override
    public void enableClipping() {
        if (!Context.isIos())
            Gdx.gl.glEnable(GL30C.GL_CLIP_DISTANCE0);

    }


    @Override
    public int getMonitorProperty() {
        final Monitor[] monitors = Lwjgl3ApplicationConfiguration.getMonitors();
        int             monitor  = readIntegerProperty(ApplicationProperties.GAME_MONITOR, 0, 0, monitors.length);
        if (monitor < 0 || monitor >= monitors.length) {
            monitor = 0;
            logger.error(String.format("pluvia.monitiro=%d cannot be negative or higher than the number of monitors %d.", monitor, monitors.length));
        }
        return monitor;
    }

    @Override
    public int getNumberOfMonitors() {
        final Monitor[] monitors = Lwjgl3ApplicationConfiguration.getMonitors();
        return monitors.length;
    }

    @Override
    public void setMonitor(int value) {
        properties.setProperty(ApplicationProperties.GAME_MONITOR, "" + value);
    }

    @Override
    protected String getInstallationFolder() {
        try {
            String path = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            if (path.endsWith(".jar")) {
                logger.info("path = " + path);
                logger.info("last index = " + path.lastIndexOf(File.separator));
                path = path.substring(0, path.lastIndexOf(File.separator) - 1);
            }
            path = cleanupPath(path);
            return path;
        } catch (URISyntaxException e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }

    @Override
    public boolean isFullscreenModeSupported() {
        return false;
    }

    @Override
    public boolean isMSAASamplesSupported() {
        return true;
    }

    @Override
    public boolean isPbrModeSupported() {
        return false;
    }

    @Override
    public boolean isForegroundFpsSupported() {
        return true;
    }

    @Override
    public boolean isMonitorSupported() {
        return true;
    }

    @Override
    public boolean isVsyncSupported() {
        return true;
    }

    @Override
    public boolean isRestartSuported() {
        return true;
    }

    @Override
    public boolean isDebugModeSupported() {
        return true;
    }

}
