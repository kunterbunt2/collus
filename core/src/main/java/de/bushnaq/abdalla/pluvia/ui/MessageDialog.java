package de.bushnaq.abdalla.pluvia.ui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;

import de.bushnaq.abdalla.pluvia.engine.GameEngine;

/**
 * @author kunterbunt
 *
 */
public class MessageDialog extends AbstractDialog {
	VisLabel	textArea;
	VisLabel	titleLabel;

	public MessageDialog(GameEngine gameEngine, final Batch batch, final InputMultiplexer inputMultiplexer) throws Exception {
		super(gameEngine, batch, inputMultiplexer);
		modal = true;
		createStage("", true);
	}

	@Override
	protected void close() {
//		pop();
		setVisible(false);
		getGameEngine().context.setEnableTime(true);
	}

	@Override
	protected void create() {
		Sizes sizes = VisUI.getSizes();
		{
			getTable().row();
			titleLabel = new VisLabel("About Pluvia");
			titleLabel.setColor(LIGHT_BLUE_COLOR);
			titleLabel.setAlignment(Align.center);
			getTable().add(titleLabel).width(DIALOG_WIDTH * sizes.scaleFactor).pad(0, 16, 16, 16).center();
		}
		{
			getTable().row();
			textArea = new VisLabel("Pluvia\nCopyright 2001 - 2022\nA & J Bushnaq");
			textArea.setAlignment(Align.left);
			textArea.setWrap(true);
			getTable().add(textArea).expand().fillX().width(DIALOG_WIDTH * 2 * sizes.scaleFactor);
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

	@Override
	protected VisDialog createWindow(String title) {
		super.createWindow(title);
		getDialog().setColor(0.0f, 0.0f, 0.0f, 0.9f);
		getDialog().setBackground("window");
		return getDialog();
	}

//	@Override
//	protected void enterAction() {
//		close();
////		close();
//	}

	public void showModal(String title, String msg) {
		titleLabel.setText(title);
		textArea.setText(msg);
		packAndPosition();
		getGameEngine().context.setEnableTime(false);
		super.setVisible(true);
	}

}
