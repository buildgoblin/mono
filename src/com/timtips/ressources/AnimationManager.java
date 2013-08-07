package com.timtips.ressources;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timtips.components.Animation.AnimationType;
import com.timtips.interfaces.TimtipsBaseGame;

public class AnimationManager {

	private static AnimationManager instance;

	public static AnimationManager instance(TimtipsBaseGame game) {
		if (AnimationManager.instance == null) {
			AnimationManager.instance = new AnimationManager(game);
		}
		return AnimationManager.instance;
	}

	public static void reset() {
		AnimationManager.instance = null;
	}

	private TimtipsBaseGame game;

	HashMap<AnimationType, Animation> anims = new HashMap<AnimationType, Animation>();

	private AnimationManager() {}

	private AnimationManager(TimtipsBaseGame game) {
		this.game = game;
	}

	public void dispose() {

	}

	public Animation getAnimation(AnimationType type) {

		if (anims.get(type) == null && type == AnimationType.fish) {

			TextureRegion[][] split = AssetManager.instance(game).getAtlas().findRegion(type.name()).split(100, 60);

			TextureRegion[] keyFrames = split[0];

			Animation anim = new Animation(1 / 6f, keyFrames);
			anim.setPlayMode(Animation.LOOP_PINGPONG);
			anims.put(type, anim);
		}
		if (anims.get(type) == null && type == AnimationType.jellyfish) {

			TextureRegion[][] split = AssetManager.instance(game).getAtlas().findRegion(type.name()).split(96, 96);

			TextureRegion[] keyFrames = split[0];

			Animation anim = new Animation(1 / 36f, keyFrames);

			anim.setPlayMode(Animation.LOOP_PINGPONG);
			anims.put(type, anim);
		}
		if (anims.get(type) == null && type == AnimationType.fishBlue) {

			TextureRegion[][] split = AssetManager.instance(game).getAtlas().findRegion(type.name()).split(50, 30);

			TextureRegion[] keyFrames = split[0];

			Animation anim = new Animation(1 / 6f, keyFrames);
			anim.setPlayMode(Animation.LOOP_PINGPONG);
			anims.put(type, anim);
		}
		if (anims.get(type) == null && type == AnimationType.sharkAttack) {

			TextureRegion[][] split = AssetManager.instance(game).getAtlas().findRegion(type.name()).split(50, 50);

			TextureRegion[] keyFrames = split[0];

			Animation anim = new Animation(1 / 6f, keyFrames);
			anim.setPlayMode(Animation.LOOP_PINGPONG);
			anims.put(type, anim);
		}

		if (anims.get(type) == null && type == AnimationType.explode) {
			TextureRegion[][] regions = AssetManager.instance(game).getAtlas().findRegion(type.name()).split(64, 64);
			TextureRegion[] reg = new TextureRegion[16];
			int index = 0;
			for (int x = 0; x < regions.length; x++) {
				for (int y = 0; y < regions.length; y++) {
					reg[index++] = regions[x][y];
				}
			}
			anims.put(type, new Animation(1 / 9f, reg));
		}

		return anims.get(type);
	}
}