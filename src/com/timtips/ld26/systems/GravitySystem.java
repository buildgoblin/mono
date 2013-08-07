package com.timtips.ld26.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.timtips.components.Physic;
import com.timtips.components.Transform;
import com.timtips.interfaces.TimtipsBaseGame;
import com.timtips.ld26.components.Gravity;

public class GravitySystem extends EntityProcessingSystem {
	private TimtipsBaseGame game;
	private ComponentMapper<Physic> physMapper;
	private ComponentMapper<Transform> transformMapper;
	private ComponentMapper<Gravity> gravMapper;

	public GravitySystem(TimtipsBaseGame game) {
		super(Aspect.getAspectForAll(Gravity.class));
		this.game = game;
	}

	@Override
	protected void initialize() {
		super.initialize();
		gravMapper = world.getMapper(Gravity.class);
		physMapper = world.getMapper(Physic.class);
		transformMapper = world.getMapper(Transform.class);
	}

	@Override
	protected void process(Entity e) {
		if (transformMapper.get(e).contains(transformMapper.get(PlayerSystem.player))) {
			Gravity grav = gravMapper.get(e);
			physMapper.get(PlayerSystem.player).myBody.applyForceToCenter(grav.x, grav.y, true);
		}
	}
}
