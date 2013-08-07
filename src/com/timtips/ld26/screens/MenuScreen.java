package com.timtips.ld26.screens;

import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.timtips.components.Renderable.TexType;
import com.timtips.components.SSound.SoundType;
import com.timtips.components.Timer;
import com.timtips.components.Transform;
import com.timtips.interfaces.TimTipsScreen;
import com.timtips.interfaces.TimtipsBaseGame;
import com.timtips.ld26.LD26;
import com.timtips.ressources.AssetManager;
import com.timtips.ressources.AudioManager;
import com.timtips.tween.TransformTweener;

public class MenuScreen extends TimTipsScreen implements Screen, InputProcessor {
	private TimtipsBaseGame game;
	public Timer fadeInTimer = new Timer(1f);
	public Timer fadeOutTimer = new Timer(3f);
	private boolean pressed;
	Transform ts[] = null;
	private String[] titles = null;
	private Transform selectedT = null;
	private Transform fts[] = null;
	private Transform curHover;
	private Tween[] tweens = null;
	private float oldSize;

	public MenuScreen(TimtipsBaseGame game) {
		this.game = game;
		titles = ((LD26) game).titles;
	}

	public void init() {
		Tween.registerAccessor(Transform.class, new TransformTweener());
		((LD26) game).curMusic = SoundType.ld26;
		ts = new Transform[((LD26) game).titles.length];
		fts = new Transform[((LD26) game).titles.length];
		tweens = new Tween[((LD26) game).titles.length];
		int x = 0;
		int y = 0;
		for (String str : ((LD26) game).titles) {
			if (x % 4 == 0) {
				y++;

			}
			ts[x] = new Transform(TimtipsBaseGame.WIDTH / 5 * (x % 4 + 1), TimtipsBaseGame.HEIGHT - 100 - y * 130 - 48, 96, 96);
			fts[x] = new Transform(TimtipsBaseGame.WIDTH / 5 * (x % 4 + 1), TimtipsBaseGame.HEIGHT - 100 - y * 130 - 48, 96, 96);
			tweens[x] = Tween.to(ts[x], TransformTweener.DIM_XY, 4).target(120, 120).repeatYoyo(-1, 0).start(LevelScreen.tweenManager);
			x++;

		}
		game.getOrtho().position.set(AssetManager.instance(game).getGuiCam().position);
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Gdx.gl.glClearColor(66 / 255f, 69 / 255f, 84 / 255f, 1);
		// Gdx.gl.glClearColor(70 / 255f, 140 / 255f, 174 / 255f, 1);
		// Gdx.gl.glClearColor(54 / 255f, 39 / 255f, 60 / 255f, 1);
		Gdx.gl.glClearColor(92 / 255f, 83 / 255f, 101 / 255f, 1);

		LevelScreen.tweenManager.update(delta);

		float intVal = fadeOutTimer.getPercentage();
		intVal = Interpolation.exp10In.apply(intVal);
		fadeInTimer.update(delta);

		game.getBatch().setColor(1, 1, 1, !fadeInTimer.isDone() ? fadeInTimer.getPercentage() : 1 - intVal);
		game.getMediumFont().setColor(1, 1, 1, !fadeInTimer.isDone() ? fadeInTimer.getPercentage() : 1 - intVal);
		game.startBatch();
		game.getBatch().setProjectionMatrix(AssetManager.instance(game).getGuiCam().combined);
		int index = 0;
		game.getBatch().draw(AssetManager.instance(game).getTexture(TexType.logo), TimtipsBaseGame.WIDTH / 2f - 128, TimtipsBaseGame.HEIGHT - 230);
		for (Transform t : ts) {
			if (pressed && t != selectedT) {
				index++;
				continue;
			}
			String refinedString = titles[index].replace(" ", "");
			refinedString = refinedString.replace("-", "");
			game.getBatch().draw(AssetManager.instance(game).getTexture(TexType.valueOf(refinedString)), t.x - t.dimX / 2f, t.y - t.dimY / 2f, t.dimX, t.dimY);
			if (!pressed) {
				Transform ft = fts[index];
				game.getMediumFont().draw(game.getBatch(), titles[index], ft.x - game.getMediumFont().getBounds(refinedString).width / 2f, ft.y - ft.dimY / 2f);
			}
			index++;
		}

		String str = "If you find a level too difficult try another";
		game.getMediumFont().draw(game.getBatch(), str, TimtipsBaseGame.WIDTH / 2f - game.getMediumFont().getBounds(str).width / 2f, fts[ts.length - 1].y - 150);
		str = "Press ESC during the game to get back here";
		game.getMediumFont().draw(game.getBatch(), str, TimtipsBaseGame.WIDTH / 2f - game.getMediumFont().getBounds(str).width / 2f, fts[ts.length - 1].y - 200);
		game.endBatch();
		if (fadeInTimer.isDone() && pressed) {
			fadeOutTimer.update(delta);
			game.startBatch();
			game.getBatch().setColor(0, 0, 0, intVal);
			game.getBatch().draw(AssetManager.instance(game).getTexture(TexType.bg), -3000, -3000, 6000, 6000);
			game.endBatch();
			if (fadeOutTimer.isDone()) {
				game.desiredState = TimtipsBaseGame.INTERMED;

			}
		}
	}

	public void stopMusic() {
		// AudioManager.instance(game).getMusic(SoundType.ld26).stop();

	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.ESCAPE) {
			Gdx.app.exit();
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (pressed) {
			return true;
		}
		int index = 0;
		System.out.println(screenX + "," + (TimtipsBaseGame.HEIGHT - screenY));
		for (Transform t : ts) {
			System.out.println(t);
			// if (t.getDistanceTo(screenX, TimtipsBaseGame.HEIGHT - screenY) < 80) {
			if (t.contains(screenX, TimtipsBaseGame.HEIGHT - screenY)) {
				game.curLevel = index;
				AudioManager.instance(game).getSound(SoundType.selected).play();
				LevelScreen.tweenManager.killTarget(t);
				Tween.to(t, TransformTweener.DIM_XY, 1).target(200, 200).start(LevelScreen.tweenManager);
				Tween.to(t, TransformTweener.POS_XY, 1).target(game.getOrtho().position.x, game.getOrtho().position.y).start(LevelScreen.tweenManager);

				pressed = true;
				selectedT = t;
				return true;
			}
			index++;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		int index = 0;
		if (pressed) {
			return true;
		}

		for (Transform t : ts) {
			// if (t.getDistanceTo(screenX, TimtipsBaseGame.HEIGHT - screenY) < 80) {
			if (t.contains(screenX, TimtipsBaseGame.HEIGHT - screenY)) {
				if (curHover != t) {

					AudioManager.instance(game).getSound(SoundType.hover).play();
					if (curHover != null) {
						curHover.dimX = oldSize;
						curHover.dimY = oldSize;
						tweens[indexOf(curHover)].resume();
					}
					curHover = t;

					oldSize = t.dimX;
					t.dimX = 150;
					t.dimY = 150;
					tweens[index].pause();
					System.out.println("pausing");

				}
			} else if (t == curHover) {
				curHover = null;
				t.dimX = oldSize;
				t.dimY = oldSize;
				tweens[index].resume();
				System.out.println("resuming");

			}
			index++;
		}

		return false;
	}

	public int indexOf(Transform t) {
		int x = 0;
		for (Transform tss : ts) {
			if (tss == t) {
				return x;
			}
			x++;
		}
		return -1;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
