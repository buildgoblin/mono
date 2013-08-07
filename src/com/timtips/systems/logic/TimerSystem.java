package com.timtips.systems.logic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.timtips.components.CacheMarker;
import com.timtips.components.DeleteMarker;
import com.timtips.components.Particle;
import com.timtips.components.Particle.ParticleType;
import com.timtips.components.Physic;
import com.timtips.components.Timer;
import com.timtips.components.Timer.TimerType;
import com.timtips.interfaces.TimtipsBaseGame;

public class TimerSystem extends EntityProcessingSystem {

	private final TimtipsBaseGame game;

	private ComponentMapper<Timer> timerMapper;

	private ComponentMapper<Physic> physMapper;

	private Timer t;

	private long begin = 0;

	private long end = 0;

	private ComponentMapper<Particle> particleMapper;

	private Particle particle;

	@SuppressWarnings("unchecked")
	public TimerSystem(TimtipsBaseGame game) {
		super(Aspect.getAspectForAll(Timer.class).exclude(DeleteMarker.class));
		this.game = game;
	}

	@Override
	protected void begin() {
		begin = System.currentTimeMillis();
		super.begin();
		if (TimtipsBaseGame.logEnts) {
			Gdx.app.log(getClass().getName(), "Ents: " + getActives().size());
		}
	}

	@Override
	protected void end() {
		super.end();
		end = System.currentTimeMillis();
		if (game.logTimings && end - begin > TimtipsBaseGame.timeLogThreshold) {
			Gdx.app.log(this.getClass().getName(), "Processing Time: " + (end - begin));
		}
	}

	@Override
	protected void initialize() {
		super.initialize();
		timerMapper = world.getMapper(Timer.class);
		particleMapper = world.getMapper(Particle.class);
		physMapper = world.getMapper(Physic.class);
	}

	@Override
	protected void process(Entity e) {

		t = timerMapper.getSafe(e);
		if (t == null) {
			return;
		}
		if (t.isDone()) {
			if (t.type == TimerType.SELF_PARTICLE) {

				particle = particleMapper.get(e);
				if (particle.type == ParticleType.FIRE || particle.type == ParticleType.WATER || particle.type == ParticleType.SPLASH
						|| particle.type == ParticleType.DROP) {
					e.addComponent(new CacheMarker());
					e.changedInWorld();
				} else {
					e.addComponent(new DeleteMarker());
					e.changedInWorld();
				}
			} else

			if (t.type == TimerType.REMOVE_NO_CHECKS) {
				if (physMapper.has(e)) {
					e.addComponent(new DeleteMarker());
					e.changedInWorld();
				} else {
					e.deleteFromWorld();
				}
			} else {
				t.reset();
			}

		} else {
			t.update(world.getDelta());
		}

	}
}
