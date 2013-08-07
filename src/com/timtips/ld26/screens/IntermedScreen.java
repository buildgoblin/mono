package com.timtips.ld26.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.timtips.components.SSound.SoundType;
import com.timtips.components.Timer;
import com.timtips.interfaces.TimTipsScreen;
import com.timtips.interfaces.TimtipsBaseGame;
import com.timtips.ld26.LD26;
import com.timtips.ressources.AssetManager;
import com.timtips.ressources.AudioManager;

public class IntermedScreen extends TimTipsScreen implements Screen {
	public static boolean overwrite = false;
	private TimtipsBaseGame game;

	public IntermedScreen(TimtipsBaseGame game) {
		this.game = game;
	}

	public Timer fadeInTimer = new Timer(2f);
	public Timer fadeOutTimer = new Timer(2f);

	public void init() {

	}

	@Override
	public void render(float delta) {
		super.render(delta);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Gdx.gl.glClearColor(66 / 255f, 69 / 255f, 84 / 255f, 1);
		Gdx.gl.glClearColor(0 / 255f, 0 / 255f, 0 / 255f, 1);

		game.startBatch();
		game.getBatch().setColor(1, 1, 1, !fadeInTimer.isDone() ? fadeInTimer.getPercentage() : 1 - fadeOutTimer.getPercentage());
		game.getMediumFont().setColor(1, 1, 1, !fadeInTimer.isDone() ? fadeInTimer.getPercentage() : 1 - fadeOutTimer.getPercentage());
		game.getBatch().setProjectionMatrix(AssetManager.instance(game).getGuiCam().combined);
		String str = ((LD26) game).titles[game.curLevel];
		game.getMediumFont().draw(game.getBatch(), str, TimtipsBaseGame.WIDTH / 2f - game.getMediumFont().getBounds(str).width / 2f, TimtipsBaseGame.HEIGHT - 300);

		game.endBatch();
		fadeInTimer.update(delta);
		if (fadeInTimer.isDone()) {
			fadeOutTimer.update(delta);
			if (fadeOutTimer.isDone()) {
				game.desiredState = TimtipsBaseGame.LEVEL;
			}
		}
	}

	public void stopMusic() {
		AudioManager.instance(game).getMusic(SoundType.ld26).stop();

	}
}
