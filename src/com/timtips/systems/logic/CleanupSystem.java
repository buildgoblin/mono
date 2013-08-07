package com.timtips.systems.logic;

import java.util.ArrayList;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.timtips.components.CacheMarker;
import com.timtips.components.DeleteMarker;
import com.timtips.components.Particle;
import com.timtips.components.PhysRemovalMarker;
import com.timtips.components.Physic;
import com.timtips.components.SSound;
import com.timtips.interfaces.TimtipsBaseGame;

public class CleanupSystem extends EntityProcessingSystem {

	private final TimtipsBaseGame game;

	private ComponentMapper<Physic> physicMapper;

	private ComponentMapper<DeleteMarker> deleteMapper;

	private ComponentMapper<PhysRemovalMarker> removalMapper;

	private ComponentMapper<CacheMarker> cacheMapper;

	private long begin = 0;

	private long end = 0;

	private ArrayList<Fixture> fixtureList;

	private ComponentMapper<Particle> partMapper;

	private Particle particle;

	private ComponentMapper<SSound> soundMapper;

	@SuppressWarnings("unchecked")
	public CleanupSystem(TimtipsBaseGame game) {
		super(Aspect.getAspectForOne(DeleteMarker.class, PhysRemovalMarker.class, CacheMarker.class));
		this.game = game;

	}

	@Override
	protected void begin() {
		begin = System.currentTimeMillis();
		if (TimtipsBaseGame.logEnts) {
			Gdx.app.log(getClass().getName(), "Ents: " + getActives().size());
		}
		super.begin();

	}

	public void destroyPhysics(Entity e) {
		if (physicMapper.has(e)) {
			if (physicMapper.get(e).myBody != null) {
				fixtureList = physicMapper.get(e).myBody.getFixtureList();
				for (int i = 0; i < fixtureList.size(); i++) {
					physicMapper.get(e).myBody.destroyFixture(fixtureList.get(i));
				}
				game.getPhysWorld().destroyBody(physicMapper.get(e).myBody);
			}
			e.removeComponent(Physic.class);
			e.changedInWorld();
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
		physicMapper = world.getMapper(Physic.class);
		deleteMapper = world.getMapper(DeleteMarker.class);
		removalMapper = world.getMapper(PhysRemovalMarker.class);
		cacheMapper = world.getMapper(CacheMarker.class);
		partMapper = world.getMapper(Particle.class);

		soundMapper = world.getMapper(SSound.class);
	}

	@Override
	protected void process(Entity e) {
		if (cacheMapper.has(e)) {
			if (partMapper.has(e)) {
				particle = partMapper.get(e);

			}

			if (physicMapper.has(e)) {
				destroyPhysics(e);
			}

			if (removalMapper.has(e)) {

				e.removeComponent(Physic.class);
				e.removeComponent(PhysRemovalMarker.class);

				e.changedInWorld();

			}

			if (deleteMapper.has(e)) {

				e.deleteFromWorld();
				e.disable();
				e.changedInWorld();
			}
		}
	}
}
