package com.timtips.ld26.systems;

import aurelienribon.tweenengine.Tween;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.timtips.components.Physic;
import com.timtips.components.Renderable;
import com.timtips.components.SSound.SoundType;
import com.timtips.components.Transform;
import com.timtips.interfaces.TimtipsBaseGame;
import com.timtips.ld26.ressources.Teleporter;
import com.timtips.ld26.screens.LevelScreen;
import com.timtips.ressources.AudioManager;
import com.timtips.tween.ColorTweener;

public class TeleporterSystem extends EntityProcessingSystem {
	private TimtipsBaseGame game;
	private ComponentMapper<Teleporter> teleMapper;
	private ComponentMapper<Physic> physMapper;
	private ComponentMapper<Transform> transformMapper;
	private ComponentMapper<Renderable> renderMapper;

	public TeleporterSystem(TimtipsBaseGame game) {
		super(Aspect.getAspectForAll(Teleporter.class));
		this.game = game;
	}

	@Override
	protected void initialize() {
		super.initialize();
		teleMapper = world.getMapper(Teleporter.class);
		physMapper = world.getMapper(Physic.class);
		transformMapper = world.getMapper(Transform.class);
		renderMapper = world.getMapper(Renderable.class);
	}

	public void trigger(Entity e) {
		for (int i = 0; i < getActives().size(); i++) {
			Entity a = getActives().get(i);
			if (teleMapper.get(a).name.equals(teleMapper.get(e).target)) {
				System.out.println("Found target teleporter: " + teleMapper.get(a));

				teleMapper.get(e).triggered = true;
				teleMapper.get(a).triggered = true;
				Color clr = renderMapper.get(e).color;
				if (!LevelScreen.tweenManager.containsTarget(clr)) {
					Tween.to(clr, ColorTweener.RGB, 0.1f).target(1, 1, 1).repeatYoyo(1, 0).start(LevelScreen.tweenManager);
				}
				clr = renderMapper.get(a).color;
				if (!LevelScreen.tweenManager.containsTarget(clr)) {
					Tween.to(clr, ColorTweener.RGB, 0.1f).target(1, 1, 1).repeatYoyo(1, 0).start(LevelScreen.tweenManager);
				}

				clr = world.getSystem(PlayerSystem.class).playerColor;
				if (!LevelScreen.tweenManager.containsTarget(clr)) {
					Tween.to(clr, ColorTweener.RGB, 0.5f).target(renderMapper.get(e).color.r, renderMapper.get(e).color.g, renderMapper.get(e).color.b).repeatYoyo(1, 0)
							.start(LevelScreen.tweenManager);
				}
				teleportPlayer(a);
				return;
			}
		}
	}

	private void teleportPlayer(Entity a) {

		physMapper.get(PlayerSystem.player).myBody.setTransform(transformMapper.get(a).x * Physic.pixel2phys, transformMapper.get(a).y * Physic.pixel2phys, 0);
		// physMapper.get(PlayerSystem.player).myBody.setLinearVelocity(0, 0);
		transformMapper.get(PlayerSystem.player).x = transformMapper.get(a).x;
		transformMapper.get(PlayerSystem.player).y = transformMapper.get(a).y;
		AudioManager.instance(game).getSound(SoundType.teleport).play(0.4f);

	}

	@Override
	protected void process(Entity e) {
		if (transformMapper.get(e).contains(transformMapper.get(PlayerSystem.player)) && !teleMapper.get(e).triggered) {
			trigger(e);
		}
		if (teleMapper.get(e).triggered && !transformMapper.get(e).contains(transformMapper.get(PlayerSystem.player))) {
			teleMapper.get(e).triggered = false;
		}
	}
}
