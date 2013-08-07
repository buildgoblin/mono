package com.timtips.ressources;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.timtips.components.Animation.AnimationType;
import com.timtips.components.Renderable;
import com.timtips.components.Renderable.TexType;
import com.timtips.interfaces.TimtipsBaseGame;

public class AssetManager {

	private static AssetManager instance;

	public static AssetManager instance(TimtipsBaseGame game) {
		if (AssetManager.instance == null) {
			AssetManager.instance = new AssetManager(game);
		}
		return AssetManager.instance;
	}

	public static void reset() {
		if (AssetManager.instance != null) {
			if (AssetManager.instance.font != null) {
				AssetManager.instance.font.dispose();
				AssetManager.instance.font = null;
			}

			if (AssetManager.instance.mediumFont != null) {
				AssetManager.instance.mediumFont.dispose();
				AssetManager.instance.mediumFont = null;
			}
		}
	}

	SpriteBatch batch;

	private OrthographicCamera ortho;
	private OrthographicCamera guiCam;
	Random rand;
	TextureAtlas atlas;

	// public TextureAtlas para;
	public final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";;

	HashMap<TexType, TextureRegion> texRegs = new HashMap<TexType, TextureRegion>();

	HashMap<AnimationType, Animation> anims = new HashMap<AnimationType, Animation>();

	private BitmapFont font;

	private BitmapFont mediumFont;
	private BitmapFont bonusFont;
	private Random lvlRand;

	private TimtipsBaseGame game;

	private TextureAtlas paraAtlas;

	private TextureRegion textureRegion;

	private Skin skin;

	private AssetManager() {

	}

	private AssetManager(TimtipsBaseGame game) {
		this.game = game;

	}

	public void dispose() {

		if (font != null) {
			getFont().dispose();
		}

		if (atlas != null) {
			atlas.dispose();
		}
		if (batch != null) {
			batch.dispose();
			// para.dispose();
		}
		if (paraAtlas != null) {
			paraAtlas.dispose();
		}
		if (skin != null) {
			skin.dispose();
		}
		AssetManager.instance = null;
	}

	public Animation getAnimation(AnimationType type) {
		if (anims.get(type) == null) {

		}
		return null;
	}

	public TextureAtlas getAtlas() {
		return atlas;
	}

	public SpriteBatch getBatch() {
		if (batch == null) {
			batch = new SpriteBatch();
			Gdx.app.log(this.getClass().getName(), "trying to get batch before preload called");

		}
		return batch;
	}

	public BitmapFont getFont() {
		if (font == null) {
			//
			// FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
			// font = generator.generateFont(20);
			// generator.dispose();
			if (game.logTimings) {
				System.out.println("Initing font");
			}
			font = new BitmapFont(Gdx.files.internal("data/fireLarge.fnt"), atlas.findRegion("fireLarge"), false);
			return font;
		}

		return font;
	}

	public OrthographicCamera getGuiCam() {
		return guiCam;
	}

	public Random getLvlRand() {
		if (lvlRand == null) {
			if (game.logTimings) {
				System.out.println("Initing lvlRand");
			}
			lvlRand = new Random();
		}
		return lvlRand;
	}

	public BitmapFont getMediumFont() {
		if (mediumFont == null) {
			if (game.logTimings) {
				System.out.println("Initing medFont");
			}
			//
			// FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
			// font = generator.generateFont(20);
			// generator.dispose();

			mediumFont = new BitmapFont(Gdx.files.internal("data/fonts/ld26.fnt"), atlas.findRegion("ld26"), false);
			return mediumFont;
		}

		return mediumFont;
	}

	public OrthographicCamera getOrtho() {
		return ortho;
	}

	public Random getRand() {
		if (rand == null) {
			rand = new Random();
			if (game.logTimings) {
				System.out.println("Initing Rand");
			}
		}
		return rand;
	}

	public TextureRegion getTexture(TexType type) {
		textureRegion = texRegs.get(type);
		if (textureRegion == null) {
			texRegs.put(type, atlas.findRegion(type.name()));
			textureRegion = texRegs.get(type);
		}
		if (textureRegion == null) {
			Gdx.app.log(getClass().getName(), "Couldn't find texture: " + type);
		}
		return textureRegion;
	}

	public void preload() {
		if (game.logTimings) {
			System.out.println("................");
			System.out.println("Preloading");
			System.out.println("................");
		}
		atlas = new TextureAtlas("data/ld26.txt");
		// paraAtlas = new TextureAtlas("data/para.txt");

		// Texture next = atlas.getTextures().iterator().next();
		// next.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		if (batch == null) {
			batch = new SpriteBatch();
		}

		ortho = new OrthographicCamera(TimtipsBaseGame.WIDTH, TimtipsBaseGame.HEIGHT);
		guiCam = new OrthographicCamera(TimtipsBaseGame.WIDTH, TimtipsBaseGame.HEIGHT);
		guiCam.position.set(TimtipsBaseGame.WIDTH / 2, TimtipsBaseGame.HEIGHT / 2, 0);
		ortho.position.set(TimtipsBaseGame.WIDTH / 2, TimtipsBaseGame.HEIGHT / 2, 0);
		game.getOrtho().zoom = TimtipsBaseGame.scaleFact < 1 ? 1 + (1 - TimtipsBaseGame.scaleFact) : 1;
		game.baseZoom = game.getOrtho().zoom;
		if (Gdx.graphics.isGL20Available()) {
			final GL20 gl = Gdx.graphics.getGL20();
			gl.glViewport(0, 0, TimtipsBaseGame.WIDTH, TimtipsBaseGame.HEIGHT);
		} else {
			final GL10 gl = Gdx.graphics.getGL10();
			gl.glViewport(0, 0, TimtipsBaseGame.WIDTH, TimtipsBaseGame.HEIGHT);
		}
		getGuiCam().update();

		rand = new Random(System.currentTimeMillis());
		for (TexType t : Renderable.TexType.values()) {
			getTexture(t);
		}

	}

	public void setGuiCam(OrthographicCamera guiCam) {
		this.guiCam = guiCam;
	}

	public Skin getSkin() {
		if (skin == null) {
			skin = new TimSkin("data/guiSkin.txt");
		}
		return skin;
	}

}
