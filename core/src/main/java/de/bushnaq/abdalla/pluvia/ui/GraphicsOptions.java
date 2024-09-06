package de.bushnaq.abdalla.pluvia.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisSlider;

import de.bushnaq.abdalla.pluvia.engine.GameEngine;

/**
 * @author kunterbunt
 *
 */
public class GraphicsOptions {
	private VisCheckBox				debugModeCheckBox;
	private VisLabel				foregroundFpsLabel;
	private VisSlider				foregroundFpsSlider;
	private VisCheckBox				fullScreenModeCheckBox;
	private GameEngine				gameEngine;
	private VisSelectBox<Integer>	monitorSelectBox;
	private VisCheckBox				pbrCheckBox;
	private VisCheckBox				showFpsCheckBox;
	private VisCheckBox				showGraphsCheckBox;
	private Sizes					sizes;
	private VisCheckBox				vsyncCheckBox;

	public GraphicsOptions(Table table, GameEngine gameEngine) {
		this.gameEngine = gameEngine;
		sizes = VisUI.getSizes();
		table.row().pad(16);
		createShowFps(table);
		createShowGraphs(table);
		createPbr(table);
		createDebugMode(table);
		createVsync(table);
		createFullScreenMode(table);
		createForgroundFps(table);
		createMonitor(table);
	}

	private void createDebugMode(Table table) {
		if (gameEngine.context.isDebugModeSupported()) {
			{
				VisLabel label = new VisLabel("Debug Mode");
				label.setAlignment(Align.right);
				table.add(label).width(AbstractDialog.LABEL_WIDTH * sizes.scaleFactor);
			}
			{
				debugModeCheckBox = new VisCheckBox("", gameEngine.context.getDebugModeProperty());
				table.add(debugModeCheckBox).colspan(2).left();
			}
			table.row().pad(16);
		}
	}

	private void createForgroundFps(Table table) {
		if (gameEngine.context.isForegroundFpsSupported()) {
			{
				VisLabel label = new VisLabel("Foreground Frames Per Second");
				label.setAlignment(Align.right);
				table.add(label).width(AbstractDialog.LABEL_WIDTH * sizes.scaleFactor);
			}
			{
				foregroundFpsSlider = new VisSlider(25f, 1000f, 1f, false);
				foregroundFpsSlider.setValue(gameEngine.context.getForegroundFPSProperty());
				foregroundFpsSlider.addListener(new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						foregroundFpsLabel.setText((int) foregroundFpsSlider.getValue());
					}

				});
				table.add(foregroundFpsSlider).center().width(AbstractDialog.BUTTON_WIDTH * sizes.scaleFactor);
			}
			{
				foregroundFpsLabel = new VisLabel("" + gameEngine.context.getForegroundFPSProperty());
				foregroundFpsLabel.setAlignment(Align.right);
				table.add(foregroundFpsLabel).width(100).center();
			}
			table.row().pad(16);
		}
	}

	private void createFullScreenMode(Table table) {
		if (gameEngine.context.isFullscreenModeSupported()) {
			{
				VisLabel label = new VisLabel("Full Screen Mode");
				label.setAlignment(Align.right);
				table.add(label).width(AbstractDialog.LABEL_WIDTH * sizes.scaleFactor);
			}
			{
				fullScreenModeCheckBox = new VisCheckBox("", gameEngine.context.getFullscreenModeProperty());
				table.add(fullScreenModeCheckBox).colspan(2).left();
			}
			table.row().pad(16);
		}
	}

	private void createMonitor(Table table) {
		if (gameEngine.context.isMonitorSupported()) {
			{
				VisLabel label = new VisLabel("Monitor");
				label.setAlignment(Align.right);
				table.add(label).width(AbstractDialog.LABEL_WIDTH * sizes.scaleFactor);
			}
			{
				monitorSelectBox = new VisSelectBox<>();
				int			monitors	= gameEngine.context.getNumberOfMonitors();
				Integer[]	values		= new Integer[monitors];
				for (int i = 0; i < monitors; i++) {
					values[i] = i;
				}

				monitorSelectBox.setItems(values);
				monitorSelectBox.setSelected(gameEngine.context.getMonitorProperty());
				table.add(monitorSelectBox).colspan(2).left().width(AbstractDialog.BUTTON_WIDTH * sizes.scaleFactor);
			}
			table.row().pad(16);
		}
	}

	private void createPbr(Table table) {
		if (gameEngine.context.isPbrModeSupported()) {
			{
				VisLabel label = new VisLabel("Physical Based Rendering");
				label.setAlignment(Align.right);
				table.add(label).width(AbstractDialog.LABEL_WIDTH * sizes.scaleFactor);
			}
			{
				pbrCheckBox = new VisCheckBox("", gameEngine.context.getPbrModeProperty());
				table.add(pbrCheckBox).colspan(2).left();
			}
			table.row().pad(16);
		}
	}

	private void createShowFps(Table table) {
		{
			VisLabel label = new VisLabel("Show Frames Per Second");
			label.setAlignment(Align.right);
			table.add(label).width(AbstractDialog.LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			showFpsCheckBox = new VisCheckBox("", gameEngine.context.getShowFpsProperty());
			table.add(showFpsCheckBox).colspan(2).left();
		}
		table.row().pad(16);
	}

	private void createShowGraphs(Table table) {
		{
			VisLabel label = new VisLabel("Show CPU/GPU Time Graphs");
			label.setAlignment(Align.right);
			table.add(label).width(AbstractDialog.LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			showGraphsCheckBox = new VisCheckBox("", gameEngine.context.getShowGraphsProperty());
			table.add(showGraphsCheckBox).colspan(2).left();
		}
		table.row().pad(16);
	}

	private void createVsync(Table table) {
		if (gameEngine.context.isVsyncSupported()) {
			{
				VisLabel label = new VisLabel("Vertical Synchronization");
				label.setAlignment(Align.right);
				table.add(label).width(AbstractDialog.LABEL_WIDTH * sizes.scaleFactor);
			}
			{
				vsyncCheckBox = new VisCheckBox("", gameEngine.context.getVsyncProperty());
				table.add(vsyncCheckBox).colspan(2).left();
			}
			table.row().pad(16);
		}
	}

	public void save() {
		if (gameEngine.context.isPbrModeSupported())
			gameEngine.context.setPbr(pbrCheckBox.isChecked());
		gameEngine.context.setShowFps(showFpsCheckBox.isChecked());
		if (gameEngine.context.isVsyncSupported())
			gameEngine.context.setVsync(vsyncCheckBox.isChecked());
		if (gameEngine.context.isFullscreenModeSupported())
			gameEngine.context.setFullScreenMode(fullScreenModeCheckBox.isChecked());
		if (gameEngine.context.isDebugModeSupported())
			gameEngine.context.setDebugMode(debugModeCheckBox.isChecked());
		if (gameEngine.context.isForegroundFpsSupported())
			gameEngine.context.setForegroundFps((int) foregroundFpsSlider.getValue());
		if (gameEngine.context.isMonitorSupported())
			gameEngine.context.setMonitor(monitorSelectBox.getSelected());
		gameEngine.context.setShowGraphs(showGraphsCheckBox.isChecked());
	}

}
