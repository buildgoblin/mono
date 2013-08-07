package com.timtips.interfaces;

import java.util.Random;

import box2dLight.RayHandler;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.timtips.components.Renderable.TexType;
import com.timtips.components.SSound.SoundType;
import com.timtips.components.Timer;
import com.timtips.ressources.AchievementUtil;
import com.timtips.ressources.AssetManager;
import com.timtips.ressources.AudioManager;

public abstract class TimtipsBaseGame extends Game {

	public static final int LEVEL = 0;
	public static final int MAIN_MENU = 1;
	public static final int INTERMED = 2;
	public static final int HIGHSCORE = 9;
	public static final int PAUSE_SCREEN = 4;
	public static float scaleFact = 1;
	private Texture logo;
	private int cycle = 0;
	private final Timer logoTimer = new Timer(3f);

	public static int WIDTH;
	public static int HEIGHT;

	public int state = -1;
	public static final boolean logEnts = false;
	public static final int NUMBER_OF_LEVELS = 7;

	public boolean logTimings = false;

	public static int timeLogThreshold = 3;

	/* Physics related variables */
	public final float maxStepLength = 1 / 10f;

	public float accumulator = 0;

	public final float dt = 1 / 240f;
	public final float idealTimeStep = 1 / 240f;

	public int everyXthFrame = 1;
	public int velocityIterations = 5;
	public int positionIterations = 8;
	public Box2DDebugRenderer debugRenderer;
	public Matrix4 debugRenderMatrix;
	public Matrix4 combined = new Matrix4();
	public int desiredState = -1;
	// Variables for semi fixed calc
	public final float FIXED_TIMESTEP = 1 / 240f;
	public final int MAXIMUM_NUMBER_STEPS = 25;

	public final float MINIMUM_TIMESTEP = 1 / 600f;
	private boolean batchOn = false;
	public static boolean drawPhysDebug = false;
	public static float updateDeltaCorretion;

	public boolean musicOn = true;
	public boolean soundsOn = true;
	public boolean isWeb = false;
	protected Screen menuScreen;
	public Screen lvlScreen;
	public Screen doneScreen;
	public Screen highScreen;
	public int curLevel = 0;
	public float elapsed;
	public float baseZoom;
	protected TimTipsScreen intermedScreen;
	public TimTipsScreen pauseScreen;

	public TimtipsBaseGame() {

	}

	@Override
	public void create() {

		logo = new Texture(Gdx.files.internal("data/logo.png"));

		TimtipsBaseGame.WIDTH = Gdx.graphics.getWidth();
		TimtipsBaseGame.HEIGHT = Gdx.graphics.getHeight();
		TimtipsBaseGame.scaleFact = 1;

		System.out.println("Resolution: " + TimtipsBaseGame.WIDTH + " led to scaleFact: " + TimtipsBaseGame.scaleFact);
	}

	@Override
	public void dispose() {
		if (logo != null) {
			logo.dispose();
		}

		AssetManager.instance(this).dispose();
		AudioManager.instance(this).dispose();
		AchievementUtil.instance(this).reset();
		resetFactories();
	}

	protected void disposeOldScreen(int oldScreen) {
		switch (oldScreen) {
		case LEVEL:
			if (lvlScreen != null) {
				lvlScreen.dispose();
				lvlScreen = null;
			}
			break;
		case MAIN_MENU:
			if (menuScreen != null) {
				menuScreen.dispose();
				menuScreen = null;
			}
			break;

		case INTERMED:
			if (intermedScreen != null) {
				intermedScreen.dispose();
				intermedScreen = null;
			}
			break;

		default:
			break;
		}
	}

	public void endBatch() {
		if (batchOn) {
			getBatch().end();
			batchOn = false;
		}
	}

	public SpriteBatch getBatch() {
		return AssetManager.instance(this).getBatch();
	}

	public BitmapFont getFont() {
		return AssetManager.instance(this).getFont();
	}

	public Random getLvlRand() {
		return AssetManager.instance(this).getLvlRand();
	}

	public BitmapFont getMediumFont() {
		return AssetManager.instance(this).getMediumFont();
	}

	public OrthographicCamera getOrtho() {
		return AssetManager.instance(this).getOrtho();
	}

	public com.badlogic.gdx.physics.box2d.World getPhysWorld() {
		return ((TimTipsScreen) getScreen()).physWorld;
	}

	public Random getRand() {
		return AssetManager.instance(this).getRand();
	}

	public TextureRegion getTex(TexType type) {
		return AssetManager.instance(this).getTexture(type);
	}

	public com.artemis.World getWorld() {

		return ((TimTipsScreen) getScreen()).world;
	}

	public void goToLevelDoneScreen() {}

	public abstract void goToMainMenu();

	public void init() {
		loadSettings();

		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);

		AssetManager.instance(this).preload();
		AudioManager.instance(this).preload();

		desiredState = TimtipsBaseGame.MAIN_MENU;
	}

	public void initManagers(World world) {
		world.setManager(new GroupManager());
	}

	public void loadSettings() {
		Preferences p = Gdx.app.getPreferences("settings");
		soundsOn = p.getBoolean("soundsOn", true);
		musicOn = p.getBoolean("musicOn", true);
	}

	public void log(String where, String message) {
		Gdx.app.log(where, message);
	}

	public boolean OOS(float x, float dimX) {
		return x - dimX / 2f > getOrtho().position.x + TimtipsBaseGame.WIDTH / 2f * getOrtho().zoom
				|| x + dimX < getOrtho().position.x - TimtipsBaseGame.WIDTH / 2f * getOrtho().zoom;
	}

	public boolean OOS(float x, float y, float dimX, float dimY) {
		if (OOS(x, dimX) || OOSY(y, dimY)) {
			return true;
		}
		return false;
	}

	private boolean OOSY(float y, float dimY) {
		return y - dimY / 2f > getOrtho().position.y + TimtipsBaseGame.HEIGHT / 2f * getOrtho().zoom
				|| y + dimY < getOrtho().position.y - TimtipsBaseGame.HEIGHT / 2f * getOrtho().zoom;
	}

	public void pauseMusic() {
		if (musicOn) {
		}
	}

	public void playMusic() {
		if (musicOn) {

		}
	}

	@Override
	public void render() {
		elapsed += Gdx.graphics.getDeltaTime();
		if (cycle < 3) {
			if (cycle == 0) {
				Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
				AudioManager.instance(this).getMusic(SoundType.l1).setLooping(true);
				AudioManager.instance(this).getMusic(SoundType.l1).setVolume(0.65f);
				AudioManager.instance(this).getMusic(SoundType.l1).play();

				getBatch().begin();
				getBatch().draw(logo, TimtipsBaseGame.WIDTH / 2 - 128, TimtipsBaseGame.HEIGHT / 2 - 128);
				getBatch().flush();
				getBatch().end();
				cycle++;
				logoTimer.update(Gdx.graphics.getDeltaTime());
			} else if (cycle == 1) {
				Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

				getBatch().begin();
				getBatch().draw(logo, TimtipsBaseGame.WIDTH / 2 - 128, TimtipsBaseGame.HEIGHT / 2 - 128);
				getBatch().flush();
				getBatch().end();
				init();
				cycle++;
			} else if (cycle == 2) {
				Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

				getBatch().begin();
				getBatch().draw(logo, TimtipsBaseGame.WIDTH / 2 - 128, TimtipsBaseGame.HEIGHT / 2 - 128);
				getBatch().flush();
				getBatch().end();
				logoTimer.update(Gdx.graphics.getDeltaTime());
				if (logoTimer.isDone()) {
					cycle++;
				}
			}
		} else {
			if (state != desiredState) {
				switchState();
			} else

			if (getScreen() != null) {
				getScreen().render(Gdx.graphics.getDeltaTime());
			}
		}
	}

	protected void resetFactories() {
		AssetManager.reset();

	}

	public void startBatch() {
		if (!batchOn) {
			getBatch().begin();
			batchOn = true;
		}
	}

	public abstract void startLevel();

	private void switchState() {
		disposeOldScreen(state);

		switch (desiredState) {
		case LEVEL:
			startLevel();
			break;
		case MAIN_MENU:
			goToMainMenu();
			break;

		case INTERMED:
			goToIntermedScreen();
			break;
		case HIGHSCORE:
			goToHighscoreScreen();
			break;
		case PAUSE_SCREEN:
			setScreen(pauseScreen);
			break;
		default:
			break;
		}
	}

	protected abstract void goToIntermedScreen();

	protected abstract void goToHighscoreScreen();

	public void toggleMusic() {
		if (musicOn) {
			pauseMusic();
		}
		musicOn = !musicOn;
		Preferences p = Gdx.app.getPreferences("settings");
		p.putBoolean("musicOn", musicOn);
		p.flush();
		if (musicOn) {
			playMusic();
		}
	}

	public void toggleSound() {
		soundsOn = !soundsOn;
		Preferences p = Gdx.app.getPreferences("settings");
		p.putBoolean("soundsOn", soundsOn);
		p.flush();
	}

	public RayHandler getRay() {
		return ((TimTipsScreen) getScreen()).ray;
	}
}
