package com.timtips.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.timtips.components.Animation;
import com.timtips.components.Rotation;
import com.timtips.components.Timer;
import com.timtips.components.Transform;
import com.timtips.interfaces.TimtipsBaseGame;
import com.timtips.ressources.AnimationManager;

public class AnimationSystem extends EntityProcessingSystem {

	ComponentMapper<Transform> transformMapper;

	private ComponentMapper<Timer> timeMapper;

	private ComponentMapper<Animation> animMapper;

	private ComponentMapper<Rotation> rotationMapper;
	private final TimtipsBaseGame game;

	private long begin = 0;
	private long end = 0;
	private float rot;
	private Transform t;
	private Timer timer;
	private Rotation rotation;
	private float frameTime;
	private Animation anim;

	@SuppressWarnings("unchecked")
	public AnimationSystem(TimtipsBaseGame game) {
		super(Aspect.getAspectForAll(Animation.class));
		this.game = game;
	}

	@Override
	protected void begin() {
		begin = System.currentTimeMillis();
		super.begin();
	}

	@Override
	protected void end() {
		super.end();
		end = System.currentTimeMillis();

	}

	@Override
	protected void initialize() {
		super.initialize();
		transformMapper = world.getMapper(Transform.class);
		timeMapper = world.getMapper(Timer.class);
		animMapper = world.getMapper(Animation.class);
		rotationMapper = world.getMapper(Rotation.class);
	}

	@Override
	protected void process(Entity e) {
		anim = animMapper.getSafe(e);
		if (anim == null) {

		} else {
			t = transformMapper.get(e);
			timer = timeMapper.get(e);
			rotation = rotationMapper.getSafe(e);
			if (rotation != null) {
				rot = rotation.getDegrees();
			} else {
				rot = 0;
			}
			frameTime = timer.getPercentage();
			if (anim.pingpong) {

				AnimationManager.instance(game).getAnimation(anim.type).setPlayMode(com.badlogic.gdx.graphics.g2d.Animation.LOOP_PINGPONG);

			}
			game.getBatch().setColor(anim.color);
			// System.out.println("playing anim: " + timer.getPercentage() + ""
			// + AnimationManager.instance(game).getAnimation(anim.type).getKeyFrame(frameTime, anim.looping));
			game.getBatch().draw(AnimationManager.instance(game).getAnimation(anim.type).getKeyFrame(frameTime), t.x - t.dimX / 2, t.y - t.dimY / 2, t.dimX / 2,
					t.dimY / 2, t.dimX, t.dimY, anim.flippedHor ? -1 : 1, anim.flippedVer ? -1 : 1, rot);
			game.getBatch().setColor(Color.WHITE);
		}
	}

}
