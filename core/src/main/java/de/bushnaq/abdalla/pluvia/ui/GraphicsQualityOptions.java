package de.bushnaq.abdalla.pluvia.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisSlider;

import de.bushnaq.abdalla.engine.IApplicationProperties;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;

/**
 * @author kunterbunt
 *
 */
public class GraphicsQualityOptions {
	private GameEngine				gameEngine;
	private VisLabel				graphicsQualityLabel;
	private VisSlider				graphicsQualitySlider;
	private VisLabel				maxPointLightsLabel;
	private VisSlider				maxPointLightsSlider;
	private VisLabel				maxSceneObjectsLabel;
	private VisSlider				maxSceneObjectsSlider;
	private VisLabel				msaaSamplesLabel;
	private VisSlider				msaaSamplesSlider;
	private VisLabel				shadowMapSizeLabel;
	private VisSelectBox<Integer>	shadowMapSizeSelectBox;
	private Sizes					sizes;

	public GraphicsQualityOptions(Table table, GameEngine gameEngine) {
		this.gameEngine = gameEngine;
		sizes = VisUI.getSizes();
		table.row().pad(16);
		createGraphcsQuality(table);
		createMaxPointLights(table);
		createMaxSceneObjects(table);
		createMsaaSamples(table);
		createShadowMapSize(table);
	}

	private void createGraphcsQuality(Table table) {
		{
			VisLabel label = new VisLabel("Graphics Quality");
			label.setAlignment(Align.right);
			table.add(label).width(AbstractDialog.LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			graphicsQualitySlider = new VisSlider(0f, IApplicationProperties.MAX_GRAPHICS_QUALITY, 1f, false);
			graphicsQualitySlider.setValue(gameEngine.context.getGraphicsQuality());
			graphicsQualitySlider.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					int value = (int) graphicsQualitySlider.getValue();
					if (value <= IApplicationProperties.MAX_GRAPHICS_QUALITY) {
						graphicsQualityLabel.setText(value);
						if (value < IApplicationProperties.MAX_GRAPHICS_QUALITY) {
							setMaxPointLights(gameEngine.context.predefinedMaxPointLights[value]);
							setMaxSceneObjects(gameEngine.context.predefinedMaxSceneObjects[value]);
							setShadowMapSize(gameEngine.context.predefinedShadowMapSize[value]);
							if (gameEngine.context.isMSAASamplesSupported())
								setMsaaSamples(gameEngine.context.predefinedMssaSamples[value]);
							maxPointLightsSlider.setDisabled(true);
							maxSceneObjectsSlider.setDisabled(true);
							shadowMapSizeSelectBox.setDisabled(true);
							if (gameEngine.context.isMSAASamplesSupported())
								msaaSamplesSlider.setDisabled(true);
						} else {
							maxPointLightsSlider.setDisabled(false);
							maxSceneObjectsSlider.setDisabled(false);
							shadowMapSizeSelectBox.setDisabled(false);
							if (gameEngine.context.isMSAASamplesSupported())
								msaaSamplesSlider.setDisabled(false);
						}
					} else {
					}
				}

			});
			table.add(graphicsQualitySlider).center().width(AbstractDialog.BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			graphicsQualityLabel = new VisLabel("" + gameEngine.context.getGraphicsQuality());
			graphicsQualityLabel.setAlignment(Align.right);
			table.add(graphicsQualityLabel).width(100).center();
		}
		table.row().pad(16);
	}

	private void createMaxPointLights(Table table) {
		{
			VisLabel label = new VisLabel("Max Point Lights");
			label.setAlignment(Align.right);
			table.add(label).width(AbstractDialog.LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			maxPointLightsSlider = new VisSlider(0f, 500f, 1f, false);
			if (!isCustomMode())
				maxPointLightsSlider.setDisabled(true);
			maxPointLightsSlider.setValue(gameEngine.context.getMaxPointLights());
			maxPointLightsSlider.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					maxPointLightsLabel.setText((int) maxPointLightsSlider.getValue());
				}

			});
			table.add(maxPointLightsSlider).center().width(AbstractDialog.BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			maxPointLightsLabel = new VisLabel("" + gameEngine.context.getMaxPointLights());
			maxPointLightsLabel.setAlignment(Align.right);
			table.add(maxPointLightsLabel).width(100).center();
		}
		table.row().pad(16);
	}

	private void createMaxSceneObjects(Table table) {
		{
			VisLabel label = new VisLabel("Max Scene Objects");
			label.setAlignment(Align.right);
			table.add(label).width(AbstractDialog.LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			maxSceneObjectsSlider = new VisSlider(0f, 500f, 1f, false);
			if (!isCustomMode())
				maxSceneObjectsSlider.setDisabled(true);
			maxSceneObjectsSlider.setValue(gameEngine.context.getMaxSceneObjects());
			maxSceneObjectsSlider.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					maxSceneObjectsLabel.setText((int) maxSceneObjectsSlider.getValue());
				}

			});
			table.add(maxSceneObjectsSlider).center().width(AbstractDialog.BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			maxSceneObjectsLabel = new VisLabel("" + gameEngine.context.getMaxSceneObjects());
			maxSceneObjectsLabel.setAlignment(Align.right);
			table.add(maxSceneObjectsLabel).width(100).center();
		}
		table.row().pad(16);
	}

	private void createMsaaSamples(Table table) {
		if (gameEngine.context.isMSAASamplesSupported()) {
			{
				VisLabel label = new VisLabel("Multisample Anti-Aliasing Samples");
				label.setAlignment(Align.right);
				table.add(label).width(AbstractDialog.LABEL_WIDTH * sizes.scaleFactor);
			}
			{
				msaaSamplesSlider = new VisSlider(0f, 16f, 1f, false);
				if (!isCustomMode())
					msaaSamplesSlider.setDisabled(true);
				msaaSamplesSlider.setValue(gameEngine.context.getMSAASamples());
				msaaSamplesSlider.addListener(new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						msaaSamplesLabel.setText((int) msaaSamplesSlider.getValue());
					}

				});
				table.add(msaaSamplesSlider).center().width(AbstractDialog.BUTTON_WIDTH * sizes.scaleFactor);
			}
			{
				msaaSamplesLabel = new VisLabel("" + gameEngine.context.getMSAASamples());
				msaaSamplesLabel.setAlignment(Align.right);
				table.add(msaaSamplesLabel).width(100).center();
			}
			table.row().pad(16);
		}
	}

	private void createShadowMapSize(Table table) {
		{
			VisLabel label = new VisLabel("Shadow Map Size");
			label.setAlignment(Align.right);
			table.add(label).width(AbstractDialog.LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			shadowMapSizeSelectBox = new VisSelectBox<>();
			shadowMapSizeSelectBox.setItems(1024, 2048, 4096, 8192);
			shadowMapSizeSelectBox.setSelected(gameEngine.context.getShadowMapSizeProperty());
			if (!isCustomMode()) {
				shadowMapSizeSelectBox.setDisabled(true);
			}
			shadowMapSizeSelectBox.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					shadowMapSizeLabel.setText(shadowMapSizeSelectBox.getSelected());
				}
			});
			table.add(shadowMapSizeSelectBox).center().width(AbstractDialog.BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			shadowMapSizeLabel = new VisLabel("" + gameEngine.context.getShadowMapSizeProperty());
			shadowMapSizeLabel.setAlignment(Align.right);
			table.add(shadowMapSizeLabel).width(100).center();
		}
		table.row().pad(16);
	}

	private boolean isCustomMode() {
		return (int) graphicsQualitySlider.getValue() == IApplicationProperties.MAX_GRAPHICS_QUALITY;
	}

	public void save() {
		gameEngine.context.setGraphicsQuality((int) graphicsQualitySlider.getValue());
		if (isCustomMode()) {
			gameEngine.context.setMaxPointLights((int) maxPointLightsSlider.getValue());
			gameEngine.context.setMaxSceneObjects((int) maxSceneObjectsSlider.getValue());
			gameEngine.context.setShadowMapSize(shadowMapSizeSelectBox.getSelected());
			if (gameEngine.context.isMSAASamplesSupported())
				gameEngine.context.setMsaaSamples((int) msaaSamplesSlider.getValue());
		} else {
		}
	}

	void setGraphicsQuality(int graphicsQuality) {
		graphicsQualityLabel.setText("" + graphicsQuality);
		graphicsQualitySlider.setValue(graphicsQuality);
	}

	private void setMaxPointLights(int maxPointLights) {
		maxPointLightsLabel.setText("" + maxPointLights);
		maxPointLightsSlider.setValue(maxPointLights);
	}

	private void setMaxSceneObjects(int maxSceneObjects) {
		maxSceneObjectsLabel.setText("" + maxSceneObjects);
		maxSceneObjectsSlider.setValue(maxSceneObjects);
	}

	private void setMsaaSamples(int value) {
		msaaSamplesLabel.setText("" + value);
		msaaSamplesSlider.setValue(value);
	}

	private void setShadowMapSize(int value) {
		shadowMapSizeLabel.setText("" + value);
		shadowMapSizeSelectBox.setSelected(value);
	}

}
