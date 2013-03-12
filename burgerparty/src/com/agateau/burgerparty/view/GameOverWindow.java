package com.agateau.burgerparty.view;

import com.agateau.burgerparty.BurgerPartyGame;
import com.agateau.burgerparty.utils.UiUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class GameOverWindow extends Table {
	private BurgerPartyGame mGame;
	public GameOverWindow(BurgerPartyGame game, Skin skin) {
		mGame = game;

		setFillParent(true);

		Label label = new Label("Game Over", skin);

		TextButton tryAgainButton = new TextButton("Try Again", skin);
		tryAgainButton.addListener(new ChangeListener() {
			public void changed(ChangeListener.ChangeEvent Event, Actor actor) {
				mGame.start();
			}
		});

		TextButton menuButton = new TextButton("Menu", skin);
		menuButton.addListener(new ChangeListener() {
			public void changed(ChangeListener.ChangeEvent Event, Actor actor) {
				mGame.showMenu();
			}
		});

		add(label).padBottom(40);

		row();
		add(tryAgainButton).size(UiUtils.BUTTON_WIDTH, UiUtils.BUTTON_HEIGHT).padBottom(20);

		row();
		add(menuButton).size(UiUtils.BUTTON_WIDTH, UiUtils.BUTTON_HEIGHT);

		center();

		// Disable clicks behind us
		setTouchable(Touchable.enabled);
		addListener(new InputListener() {
			@Override
			public boolean handle(Event event) {
				event.cancel();
				return true;
			}
		});
	}

	@Override
	public void drawBackground(SpriteBatch batch, float parentAlpha) {
		batch.end();

		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		ShapeRenderer renderer = new ShapeRenderer();
		renderer.setProjectionMatrix(getStage().getCamera().combined);
		renderer.begin(ShapeType.FilledRectangle);
		renderer.setColor(0f, 0f, 0f, 0.8f);
		renderer.filledRect(0, 0, getWidth(), getHeight());
		renderer.end();

		Gdx.gl.glDisable(GL10.GL_BLEND);

		batch.begin();
	}
}
