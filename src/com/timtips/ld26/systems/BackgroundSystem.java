package com.timtips.ld26.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.timtips.components.Transform;
import com.timtips.interfaces.TimtipsBaseGame;

public class BackgroundSystem extends EntityProcessingSystem {

	private TimtipsBaseGame game;

	private ComponentMapper<Transform> transformMapper;

	private Transform t;

	@SuppressWarnings("unchecked")
	public BackgroundSystem(TimtipsBaseGame game) {
		super(Aspect.getEmpty());
		this.game = game;
	}

	@Override
	protected void initialize() {
		super.initialize();

		// Entity e = world.createEntity();
		// t = new Transform();
		// e.addComponent(t);
		// e.addComponent(new Rotation(0));
		// e.addComponent(new Renderable(TexType.wallpaper));
		// e.addToWorld();
	}

	@Override
	protected void begin() {
		super.begin();
		// t.set(game.getOrtho().position.x, game.getOrtho().position.y, TimtipsBaseGame.WIDTH * game.getOrtho().zoom, TimtipsBaseGame.HEIGHT *
		// game.getOrtho().zoom);
	}

	@Override
	protected void process(Entity e) {

	}
}
