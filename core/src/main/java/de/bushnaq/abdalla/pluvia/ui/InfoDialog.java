package de.bushnaq.abdalla.pluvia.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.attributes.PointLightsAttribute;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.pluvia.desktop.Context;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.util.TimeStatistic;

/**
 * @author kunterbunt
 *
 */
public class InfoDialog {
	private static final String		VARIABLE_LABEL	= "variableLabel";
	private final Batch				batch;
	private final TimeStatistic		debugTimer;
	private final InputMultiplexer	inputMultiplexer;
	private int						labelIndex		= 0;
	private final List<LabelData>	labels			= new ArrayList<>();
	private RenderEngine3D<GameEngine> renderEngine;
	private float					screenHeight	= 0;
	private Stage					stage;
	private final StringBuilder		stringBuilder	= new StringBuilder();
	private final String			title			= "info";
	private Class<?>				type;
	private boolean					visible;
	private Window					window;

	public InfoDialog(final Batch batch, final InputMultiplexer inputMultiplexer) throws Exception {
		this.batch = batch;
		this.inputMultiplexer = inputMultiplexer;
		debugTimer = new TimeStatistic();
		createStage();
	}

	public void act(final float deltaTime) {
		stage.act(deltaTime);
	}

	private void checkSize(final int size) {
		if (labels.size() != size)
			System.err.printf("size mismatch, expected %d but was %d\n", size, labels.size());
	}

	private void clearUnmatchedSizeAndType(final int size, final Class<?> type) {
		if (labels.size() != size || this.type != type) {
			labels.clear();
			labelIndex = 0;
			disposeWindow();
			createStage();
			this.type = type;
		}
	}

	public void createStage() {
		if (stage == null) {
			stage = new Stage(new ScreenViewport(), batch);
			inputMultiplexer.addProcessor(stage);
		}
		window = new VisWindow(title);
		// window.setDebug( true );
		final TextButton closeButton = new VisTextButton("X");
		window.getTitleTable().add(closeButton).height(window.getPadTop());
		window.setMovable(true);
		closeButton.addListener(new ClickListener() {
			@Override
			public void clicked(final InputEvent event, final float x, final float y) {
				visible = false;
			}
		});
		stage.addActor(window);
		window.pack();
	}

	public void dispose() {
		inputMultiplexer.removeProcessor(stage);
		stage.dispose();
	}

	public void disposeWindow() {
		stage.clear();
	}

	public void draw() {
		stage.draw();
	}

	public Viewport getViewport() {
		return stage.getViewport();
	}

	public boolean isVisible() {
		return visible;
	}

	private void positionWindow() {
		window.setPosition(0, screenHeight - window.getHeight() - GameEngine.FONT_SIZE - 2);
	}

	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	private void update() {
		clearUnmatchedSizeAndType(6, Context.class);
		window.getTitleLabel().setText("Statistics");
		window.pack();
		positionWindow();
		labelIndex = 0;
	}

	private void update(final GLProfiler profiler) {
		if (profiler != null && profiler.isEnabled() && debugTimer.getTime() > 1000) {
			int size = 10;
			if (renderEngine != null)
				size = 14;
			clearUnmatchedSizeAndType(size, GLProfiler.class);
			updateNameAndValue("textureBindings", profiler.getTextureBindings(), VARIABLE_LABEL);
			updateNameAndValue("drawCalls", profiler.getDrawCalls(), VARIABLE_LABEL);

			updateNameAndValue("shaderSwitches", profiler.getShaderSwitches(), VARIABLE_LABEL);
			updateNameAndValue("vertexCount.min", profiler.getVertexCount().min, VARIABLE_LABEL);
			updateNameAndValue("vertexCount.average", profiler.getVertexCount().average, VARIABLE_LABEL);
			updateNameAndValue("vertexCount.max", profiler.getVertexCount().max, VARIABLE_LABEL);
			updateNameAndValue("calls", profiler.getCalls(), VARIABLE_LABEL);
			updateNameAndValue("Texture.getNumManagedTextures()", Texture.getNumManagedTextures(), VARIABLE_LABEL);
			updateNameAndValue("delta time", Gdx.graphics.getDeltaTime(), VARIABLE_LABEL);
			updateNameAndValue("fps", Gdx.graphics.getFramesPerSecond(), VARIABLE_LABEL);

			if (renderEngine != null) {
				{
					final int	count		= renderEngine.visibleStaticGameObjectCount;
					final int	totalCount	= renderEngine.staticGameObjects.size;
					stringBuilder.setLength(0);
					stringBuilder.append(count).append(" / ").append(totalCount);
					updateNameAndValue("Static Models", stringBuilder.toString(), VARIABLE_LABEL);
				}
				{
					stringBuilder.setLength(0);
					final int	count		= renderEngine.visibleDynamicGameObjectCount;
					final int	totalCount	= renderEngine.dynamicGameObjects.size;
					stringBuilder.append(count).append(" / ").append(totalCount);
					updateNameAndValue("Dynamic Models", stringBuilder.toString(), VARIABLE_LABEL);
				}
				// light
				PointLightsAttribute pointLightsAttribute = renderEngine.environment.get(PointLightsAttribute.class, PointLightsAttribute.Type);
				if (pointLightsAttribute != null) {
					{
						stringBuilder.setLength(0);
						final int	count		= renderEngine.visibleStaticLightCount;
						final int	totalCount	= pointLightsAttribute.lights.size;
						stringBuilder.append(count).append(" / ").append(totalCount);
						updateNameAndValue("Static PointLights", stringBuilder.toString(), VARIABLE_LABEL);
					}
					{
						stringBuilder.setLength(0);
						final int	count		= renderEngine.visibleDynamicLightCount;
						final int	totalCount	= pointLightsAttribute.lights.size;
						stringBuilder.append(count).append(" / ").append(totalCount);
						updateNameAndValue("Dynamic PointLights", stringBuilder.toString(), VARIABLE_LABEL);
					}
				}
			}

			updateWidgets("Profiler");
			checkSize(size);
		}
	}

	public void update(final Object selected, final RenderEngine3D<GameEngine> renderEngine) throws Exception {
		this.renderEngine = renderEngine;
		if (GLProfiler.class.isInstance(selected)) {
			update((GLProfiler) selected);
		} else {
			update();
		}
		if (debugTimer.getTime() > 1000) {
			debugTimer.restart();
		}
	}

	private LabelData updateName(final String name, final String style) {
		if (labelIndex >= labels.size()) {
			final LabelData data = new LabelData();
			data.nameLabel = new VisLabel(""/* , "captionLabel" */);
			data.valueLabel = new VisLabel("");
			data.value2Label = new VisLabel("");
			data.value3Label = new VisLabel("");
			data.value4Label = new VisLabel("");
			labels.add(data);
			window.add(data.nameLabel).right();
			window.add("");
			window.add(data.valueLabel).left();
			window.add(data.value2Label).left();
			window.add(data.value3Label).left();
			window.add(data.value4Label).left();
			window.row();
		}
		final LabelData data = labels.get(labelIndex++);
		data.nameLabel.setText(name);
//		data.valueLabel.setStyle(skin.get(style, LabelStyle.class));
		return data;
	}

//	private void updateNameAndValue(final String name, final boolean traded, final String style) {
//		final LabelData data = updateName(name, style);
//		data.valueLabel.setText(traded ? "true" : "false");
//	}

	private void updateNameAndValue(final String name, final float value, final String style) {
		final LabelData data = updateName(name, style);
		data.valueLabel.setText(String.valueOf(value));
	}

	private void updateNameAndValue(final String name, final String value, final String style) {
		final LabelData data = updateName(name, style);
		data.valueLabel.setText(value);
	}

	private void updateWidgets(final String title) {
		window.getTitleLabel().setText(title);
		final Array<Cell> cells = window.getCells();
		if (cells.size < labels.size() * 6 + 6) {
			window.add("").minWidth(100);
			window.add("").minWidth(10f);
			window.add("").minWidth(75);
			window.add("").minWidth(75);
			window.add("").minWidth(75);
			window.add("").minWidth(75);
			window.add("");
			window.row();
			window.pack();
			positionWindow();
		}
		labelIndex = 0;
	}
}
