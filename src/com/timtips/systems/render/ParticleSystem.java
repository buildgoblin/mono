package com.timtips.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.timtips.components.DeleteMarker;
import com.timtips.components.Particle;
import com.timtips.components.Physic;
import com.timtips.components.Renderable;
import com.timtips.components.Renderable.TransitionType;
import com.timtips.components.Rotation;
import com.timtips.components.Timer;
import com.timtips.components.Transform;
import com.timtips.interfaces.TimtipsBaseGame;
import com.timtips.ressources.AssetManager;
import com.timtips.ressources.Blood;

public class ParticleSystem extends EntityProcessingSystem {

	private ComponentMapper<Physic> physMapper;

	private ComponentMapper<Transform> transformMapper;
	private ComponentMapper<Renderable> renderMapper;
	private ComponentMapper<Rotation> rotationMapper;
	private ComponentMapper<Timer> timeMapper;

	private final TimtipsBaseGame game;

	private long begin = 0;

	private long end = 0;

	private Transform t;

	private Renderable r;

	private Rotation rot;

	private float rotation;

	private ComponentMapper<Particle> particleMapper;

	private Physic p;

	private Timer timer;

	private Particle particle;

	private ComponentMapper<Blood> bloodMapper;

	@SuppressWarnings("unchecked")
	public ParticleSystem(TimtipsBaseGame game) {
		super(Aspect.getAspectForAll(Particle.class).exclude(DeleteMarker.class));
		this.game = game;
	}

	@Override
	protected void initialize() {
		super.initialize();
		physMapper = world.getMapper(Physic.class);
		transformMapper = world.getMapper(Transform.class);
		renderMapper = world.getMapper(Renderable.class);
		rotationMapper = world.getMapper(Rotation.class);
		timeMapper = world.getMapper(Timer.class);
		particleMapper = world.getMapper(Particle.class);
		bloodMapper = world.getMapper(Blood.class);
	}

	@Override
	protected void process(Entity e) {
		begin = System.currentTimeMillis();
		t = transformMapper.getSafe(e);
		if (t == null) {
			return;
		}
		if (game.OOS(t.x, t.dimX)) {
			return;
		}
		timer = timeMapper.getSafe(e);

		r = renderMapper.getSafe(e);

		if (r.transType != TransitionType.NONE) {
			if (r.transType == TransitionType.FADE_OUT) {

				r.alpha = 1 - Interpolation.fade.apply(0, 1, timer.getPercentage());

			}
			if (r.transType == TransitionType.FADE_IN_OUT) {
				if (timer.getPercentage() < 0.2f) {

					r.alpha = timer.getPercentage() * 5;

				} else if (timer.getPercentage() > 0.8f) {
					r.alpha = 5 - timer.getPercentage() * 5;
				} else {
					r.alpha = 1;
				}
			}
		}
		particle = particleMapper.getSafe(e);
		if (particle == null) {
			return;
		}

		if (r != null && r.color != null) {
			rot = rotationMapper.getSafe(e);

			if (rot != null) {
				rotation = rot.getDegrees();
			} else {
				rotation = 0;
			}

			game.getBatch().setColor(r.color.r, r.color.g, r.color.b, r.alpha);
			if (physMapper.has(e)) {
				p = physMapper.get(e);

				rotation = (float) Math.toDegrees(p.myBody.getAngle());
			}

			game.getBatch().draw(AssetManager.instance(game).getTexture(r.type), t.x - t.dimX / 2f, t.y - t.dimY / 2f, t.dimX / 2f, t.dimY / 2f, t.dimX, t.dimY, 1, 1,
					rotation);
			game.getBatch().setColor(Color.WHITE);

		}

	}

	@Override
	protected void removed(Entity e) {
		super.removed(e);

	}
}
