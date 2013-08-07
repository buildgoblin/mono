package com.timtips.ld26.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.timtips.interfaces.TimTipsScreen;
import com.timtips.interfaces.TimtipsBaseGame;
import com.timtips.ressources.AssetManager;

public class PauseScreen extends TimTipsScreen implements Screen {
	public static boolean overwrite = false;
	private TimtipsBaseGame game;

	public PauseScreen(TimtipsBaseGame game) {
		this.game = game;
	}

	public void init() {

	}

	@Override
	public void render(float delta) {
		super.render(delta);

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		// Gdx.gl.glClearColor(66 / 255f, 69 / 255f, 84 / 255f, 1);
		Gdx.gl.glClearColor(0 / 255f, 0 / 255f, 0 / 255f, 1);
		game.getMediumFont().setColor(Color.WHITE);
		game.startBatch();
		game.getBatch().setProjectionMatrix(AssetManager.instance(game).getGuiCam().combined);
		String str = "Return to Level Selection";

		game.getMediumFont().draw(game.getBatch(), str, TimtipsBaseGame.WIDTH / 2f - game.getMediumFont().getBounds(str).width / 2f, TimtipsBaseGame.HEIGHT / 4);

		str = "Resume";

		game.getMediumFont().draw(game.getBatch(), str, TimtipsBaseGame.WIDTH / 2f - game.getMediumFont().getBounds(str).width / 2f,
				TimtipsBaseGame.HEIGHT - TimtipsBaseGame.HEIGHT / 4);

		game.endBatch();
		if (Gdx.input.isTouched()) {
			if (Gdx.input.getY() < TimtipsBaseGame.HEIGHT / 2f) {
				game.desiredState = TimtipsBaseGame.LEVEL;
				game.state = TimtipsBaseGame.LEVEL;
				game.setScreen(game.lvlScreen);
			} else {
				game.desiredState = TimtipsBaseGame.MAIN_MENU;
			}
		}

	}

}
