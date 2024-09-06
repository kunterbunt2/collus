package de.bushnaq.abdalla.pluvia.ui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;

import de.bushnaq.abdalla.pluvia.engine.GameEngine;

/**
 * @author kunterbunt
 *
 */
public class OptionsDialog extends AbstractDialog {

	private class TestTab extends Tab {
		private Table	content;
		private String	title;

		public TestTab(String title) {
			super(false, false);
			this.title = title;
			content = new VisTable();
		}

		@Override
		public Table getContentTable() {
			return content;
		}

		@Override
		public String getTabTitle() {
			return title;
		}
	}

	private AudioOptions			audioOptions;
	final VisTable					container		= new VisTable();
	private float					dialogHeight	= 0;
	private GraphicsOptions			graphicsOptions;
	private GraphicsQualityOptions	graphicsQualityOptions;
	private VisTable				mainTable		= new VisTable();
	private Sizes					sizes;

	private TabbedPane				tabbedPane		= new TabbedPane();

	private Cell<?>					tableSpacerCell;					// used to measure height adn set height of total dialog

	public OptionsDialog(GameEngine gameEngine, final Batch batch, final InputMultiplexer inputMultiplexer) throws Exception {
		super(gameEngine, batch, inputMultiplexer);
		createStage("", true);
	}

	/**
	 * in the tabbed panel, every panel has a different height we need to find the maximum and then set the spacer cell to that size
	 */
	private void calculateBestTableHight() {
		dialogHeight = 0;
		for (Tab tab : tabbedPane.getTabs()) {
			tabbedPane.switchTab(tab);
			dialogHeight = Math.max(dialogHeight, getTable().getHeight());
		}
		tableSpacerCell.height(dialogHeight);
		tabbedPane.switchTab(0);
	}

	@Override
	public void create() {
		sizes = VisUI.getSizes();
		getTable().add(mainTable).top();
		tableSpacerCell = getTable().add();

		createCaption(mainTable);
		createTabButtons(mainTable);
		{
			Table table = tabbedPane.getTabs().get(0).getContentTable();
			graphicsQualityOptions = new GraphicsQualityOptions(table, getGameEngine());
		}
		{
			Table table = tabbedPane.getTabs().get(1).getContentTable();
			graphicsOptions = new GraphicsOptions(table, getGameEngine());
		}
		{
			Table table = tabbedPane.getTabs().get(2).getContentTable();
			audioOptions = new AudioOptions(table, getGameEngine());
		}
		createButtons(mainTable);

		calculateBestTableHight();
	}

	private void createButtons(VisTable table) {
		VisTable t = new VisTable();
		table.add(t);
		{
			VisTextButton button = new VisTextButton("OK");
			addHoverEffect(button);
			button.addListener(new ClickListener() {

				@Override
				public void clicked(final InputEvent event, final float x, final float y) {
					if (getGameEngine().context.isRestartSuported())
						getGameEngine().context.restart = true;
					graphicsQualityOptions.save();
					graphicsOptions.save();
					audioOptions.save();
					getGameEngine().context.write();
					if (!getGameEngine().context.isRestartSuported())
						getGameEngine().scheduleContextUpdate();
					close();
				}
			});
			t.add(button).center().width(BUTTON_WIDTH * sizes.scaleFactor).pad(16);
		}
		{
			VisTextButton button = new VisTextButton("Cancel", "blue");
			addHoverEffect(button);
			button.addListener(new ClickListener() {

				@Override
				public void clicked(final InputEvent event, final float x, final float y) {
					close();
				}
			});
			t.add(button).center().width(BUTTON_WIDTH * sizes.scaleFactor).pad(16);
		}
		getTable().pack();
	}

	private void createCaption(VisTable table) {
		table.row();
		VisLabel label = new VisLabel("Options");
		label.setColor(LIGHT_BLUE_COLOR);
		label.setAlignment(Align.center);
		table.add(label).width(DIALOG_WIDTH * sizes.scaleFactor).pad(0, 16, 16, 16).center();
		table.row().pad(16);
	}

	private void createTabButtons(VisTable table) {
		table.add(tabbedPane.getTable()).colspan(1).expandX().fillX();
		tabbedPane.addListener(new TabbedPaneAdapter() {
			@Override
			public void switchedTab(Tab tab) {
				container.clearChildren();
				container.add(tab.getContentTable()).expand().fill();
			}
		});
		tabbedPane.add(new TestTab("Graphics Quality"));
		tabbedPane.add(new TestTab("Graphics"));
		tabbedPane.add(new TestTab("Audio"));
		tabbedPane.switchTab(1);
		table.row().pad(16);
		table.add(container);
		table.row().pad(16);
	}

	@Override
	public void setVisible(final boolean visible) {
		super.setVisible(visible);
		if (visible) {
			graphicsQualityOptions.setGraphicsQuality(getGameEngine().context.getGraphicsQuality());
		}
	}

}
