package com.timtips.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.timtips.components.DeleteMarker;
import com.timtips.components.Particle;
import com.timtips.components.Physic;
import com.timtips.components.Rotation;
import com.timtips.components.Transform;
import com.timtips.interfaces.TimtipsBaseGame;

public class PhysicsSystem extends EntityProcessingSystem {

	private final TimtipsBaseGame game;

	private ComponentMapper<Transform> transformMapper;

	private ComponentMapper<Physic> physicMapper;

	private ComponentMapper<Rotation> rotMapper;

	private ComponentMapper<DeleteMarker> deleteMapper;

	private ComponentMapper<Particle> particleMapper;

	private long begin = 0;

	private long end = 0;

	private Physic p;

	private Transform t;

	private Rotation r;

	private float degrees;
	private final Vector2 position = new Vector2();

	@SuppressWarnings("unchecked")
	public PhysicsSystem(TimtipsBaseGame game) {
		super(Aspect.getAspectForAll(Physic.class));
		this.game = game;
	}

	@Override
	protected void begin() {
		begin = System.currentTimeMillis();
		// Logger.getLogger(this.getClass()).debug(("PhysicsSystem",
		// "num Physics: " + getActives().size());
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
		transformMapper = world.getMapper(Transform.class);
		physicMapper = world.getMapper(Physic.class);
		rotMapper = world.getMapper(Rotation.class);
		deleteMapper = world.getMapper(DeleteMarker.class);
		particleMapper = world.getMapper(Particle.class);

	}

	@Override
	public void process(Entity e) {

		p = physicMapper.get(e);

		// if (tileMapper.has(e)) {
		// tileMapper.get(e).sides.clear();
		// }

		if (!deleteMapper.has(e) && !p.independentTransform) {

			t = transformMapper.get(e);
			r = rotMapper.get(e);
			degrees = (float) Math.toDegrees(p.myBody.getAngle());
			if (degrees != 0) {
				r.setDegrees(degrees);
			}
			t.set(p.myBody.getPosition().x * Physic.phys2pixel, p.myBody.getPosition().y * Physic.phys2pixel);

		}

	}

	@Override
	protected void removed(Entity e) {

		super.removed(e);

	}

}
