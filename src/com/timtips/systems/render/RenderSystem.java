package com.timtips.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.timtips.components.DeleteMarker;
import com.timtips.components.Paralax;
import com.timtips.components.Particle;
import com.timtips.components.Renderable;
import com.timtips.components.Renderable.TransitionType;
import com.timtips.components.Rotation;
import com.timtips.components.Timer;
import com.timtips.components.Transform;
import com.timtips.interfaces.TimtipsBaseGame;
import com.timtips.ressources.AssetManager;

public class RenderSystem extends EntityProcessingSystem {
	TimtipsBaseGame game;
	private ComponentMapper<Renderable> renderMapper;
	private ComponentMapper<Transform> transformMapper;
	private ComponentMapper<Rotation> rotationMapper;

	private float rotation;

	private Transform t;
	private Rotation rot;
	public static float maxDistX = Float.MAX_VALUE;
	public static float maxDistY = Float.MAX_VALUE;
	private Renderable r;

	private long begin = 0;

	private long end = 0;
	private final Vector2 pos = new Vector2();
	private ComponentMapper<Timer> timeMapper;

	@SuppressWarnings("unchecked")
	public RenderSystem(TimtipsBaseGame game) {
		super(Aspect.getAspectForAll(Transform.class, Renderable.class).exclude(Particle.class, DeleteMarker.class, Paralax.class));
		this.game = game;
		updateRenderDistance();
	}

	@Override
	protected void begin() {
		super.begin();
		if (TimtipsBaseGame.logEnts) {
			Gdx.app.log(getClass().getName(), "Ents: " + getActives().size());
		}
		begin = System.currentTimeMillis();
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
		renderMapper = world.getMapper(Renderable.class);
		rotationMapper = world.getMapper(Rotation.class);
		timeMapper = world.getMapper(Timer.class);
	}

	@Override
	protected void process(Entity e) {
		t = transformMapper.get(e);

		r = renderMapper.getSafe(e);
		if (r == null) {
			return;
		}
		// if (!r.gui) {
		// if (game.OOS(t.x, t.y, t.dimX, t.dimY)) {
		// return;
		// }
		// }
		if (r.transType != TransitionType.NONE) {
			if (r.transType == TransitionType.FADE_OUT) {

				r.alpha = 1 - Interpolation.fade.apply(0, 1, timeMapper.get(e).getPercentage());

			}
			if (r.transType == TransitionType.FADE_IN_OUT) {
				if (timeMapper.get(e).getPercentage() < 0.3f) {

					r.alpha = Interpolation.circle.apply(0, 1, timeMapper.get(e).getPercentage() / 0.3f);

				} else if (timeMapper.get(e).getPercentage() > 0.3f) {
					r.transType = TransitionType.FADE_OUT;
				} else {
					r.alpha = 1;

				}

			}
		}

		if (r.color != null) {
			rot = rotationMapper.getSafe(e);

			if (rot != null) {
				rotation = rot.getDegrees();

			} else {
				rotation = 0;
			}
			if (r.gui) {
				game.getBatch().setProjectionMatrix(AssetManager.instance(game).getGuiCam().combined);
			}
			game.getBatch().setColor(r.color.r, r.color.g, r.color.b, r.alpha);

			game.getBatch().draw(AssetManager.instance(game).getTexture(r.type), t.x - t.dimX / 2f, t.y - t.dimY / 2f, t.dimX / 2f, t.dimY / 2f, t.dimX, t.dimY,
					r.flippedHor ? -1 : 1, 1, rotation);
			game.getBatch().setColor(Color.WHITE);
			if (r.gui) {
				game.getBatch().setProjectionMatrix(AssetManager.instance(game).getOrtho().combined);
			}

		}

	}

	public void updateRenderDistance() {
		RenderSystem.maxDistX = TimtipsBaseGame.WIDTH;
		RenderSystem.maxDistY = TimtipsBaseGame.HEIGHT * 0.8f;
	}
}
