package de.bushnaq.abdalla.pluvia.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;

import de.bushnaq.abdalla.pluvia.engine.GameEngine;

/**
 * @author kunterbunt
 *
 */
public class AudioOptions {
	private VisCheckBox	ambientAudioCheckBox;
	private VisLabel	ambientAudioVolumenLabel;
	private VisSlider	ambientAudioVolumenSlider;
	private VisLabel	audioVolumenLabel;
	private VisSlider	audioVolumenSlider;
	private GameEngine	gameEngine;
	private Sizes		sizes;

	public AudioOptions(Table table, GameEngine gameEngine) {
//		table.setDebug(true);
		this.gameEngine = gameEngine;
		sizes = VisUI.getSizes();
		table.row().pad(16);
		createAmbientAudio(table);
		createAmbientVolumen(table);
		createAudioVolumen(table);
	}

	private void createAmbientAudio(Table table) {
		{
			VisLabel label = new VisLabel("Ambient Audio");
			label.setAlignment(Align.right);
			table.add(label).width(AbstractDialog.LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			ambientAudioCheckBox = new VisCheckBox("", gameEngine.context.getAmbientAudioProperty());
			table.add(ambientAudioCheckBox).colspan(2).left();
		}
		table.row().pad(16);
	}

	private void createAmbientVolumen(Table table) {
		{
			VisLabel label = new VisLabel("Ambient Audio Volumen");
			label.setAlignment(Align.right);
			table.add(label).width(AbstractDialog.LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			ambientAudioVolumenSlider = new VisSlider(1f, 100f, 1f, false);
			ambientAudioVolumenSlider.setValue(gameEngine.context.getAmbientAudioVolumenProperty());
			ambientAudioVolumenSlider.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					ambientAudioVolumenLabel.setText((int) ambientAudioVolumenSlider.getValue());
				}

			});
			table.add(ambientAudioVolumenSlider).center().width(AbstractDialog.BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			ambientAudioVolumenLabel = new VisLabel("" + gameEngine.context.getAmbientAudioVolumenProperty());
			ambientAudioVolumenLabel.setAlignment(Align.right);
			table.add(ambientAudioVolumenLabel).width(100).center();
		}
		table.row().pad(16);
	}

	private void createAudioVolumen(Table table) {
		{
			VisLabel label = new VisLabel("Audio Volumen");
			label.setAlignment(Align.right);
			table.add(label).width(AbstractDialog.LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			audioVolumenSlider = new VisSlider(1f, 100f, 1f, false);
			audioVolumenSlider.setValue(gameEngine.context.getAudioVolumenProperty());
			audioVolumenSlider.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					audioVolumenLabel.setText((int) audioVolumenSlider.getValue());
				}

			});
			table.add(audioVolumenSlider).center().width(AbstractDialog.BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			audioVolumenLabel = new VisLabel("" + gameEngine.context.getAudioVolumenProperty());
			audioVolumenLabel.setAlignment(Align.right);
			table.add(audioVolumenLabel).width(100).center();
		}
		table.row().pad(16);
	}

	public void save() {
		gameEngine.context.setAmbientAudio(ambientAudioCheckBox.isChecked());
		gameEngine.context.setAmbientAudioVolumen((int) ambientAudioVolumenSlider.getValue());
		gameEngine.context.setAudioVolumen((int) audioVolumenSlider.getValue());
	}
}
