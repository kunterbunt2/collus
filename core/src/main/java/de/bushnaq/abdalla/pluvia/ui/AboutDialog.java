package de.bushnaq.abdalla.pluvia.ui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;

import de.bushnaq.abdalla.pluvia.engine.GameEngine;

/**
 * @author kunterbunt
 *
 */
public class AboutDialog extends AbstractDialog {

	public AboutDialog(GameEngine gameEngine, final Batch batch, final InputMultiplexer inputMultiplexer) throws Exception {
		super(gameEngine, batch, inputMultiplexer);
		createStage("", true);
	}

//	@Override
//	protected void close() {
//		pop();
////		AboutDialog.this.setVisible(false);
////		getGameEngine().getMainDialog().setVisible(true);
//	}

//	@Override
//	protected void enterAction() {
//		close();
////		close();
//	}

	@Override
	protected void create() {
		Sizes sizes = VisUI.getSizes();
		{
			getTable().row();
			VisLabel label = new VisLabel("About Pluvia");
			label.setColor(LIGHT_BLUE_COLOR);
			label.setAlignment(Align.center);
			getTable().add(label).width(DIALOG_WIDTH * sizes.scaleFactor).pad(0, 16, 16, 16).center();
		}
		{
			getTable().row();
			VisLabel textArea = new VisLabel("Pluvia\nCopyright 2001 - 2022\nA & J Bushnaq");
			textArea.setAlignment(Align.left);
			getTable().add(textArea).expand().fillX();
//			width(DIALOG_WIDTH * sizes.scaleFactor).pad(16).center();
		}
		{
			getTable().row();
			VisTextButton button = new VisTextButton("OK", "blue");
			addHoverEffect(button);
			button.addListener(new ClickListener() {
				@Override
				public void clicked(final InputEvent event, final float x, final float y) {
					close();
				}
			});
			getTable().add(button).center().width(BUTTON_WIDTH * sizes.scaleFactor).pad(16);
		}
	}

}
