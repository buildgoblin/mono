package com.timtips.ld26.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import box2dLight.RayHandler;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.timtips.components.Physic;
import com.timtips.components.Renderable.TexType;
import com.timtips.components.Timer;
import com.timtips.components.Transform;
import com.timtips.interfaces.TimTipsScreen;
import com.timtips.interfaces.TimtipsBaseGame;
import com.timtips.ld26.MyContactListener;
import com.timtips.ld26.ressources.EntityTweener;
import com.timtips.ld26.systems.BackgroundSystem;
import com.timtips.ld26.systems.GravitySystem;
import com.timtips.ld26.systems.MapSystem;
import com.timtips.ld26.systems.PlayerSystem;
import com.timtips.ld26.systems.TeleporterSystem;
import com.timtips.ld26.systems.TextNodeSystem;
import com.timtips.ressources.AssetManager;
import com.timtips.systems.logic.CleanupSystem;
import com.timtips.systems.logic.TimerSystem;
import com.timtips.systems.render.GuiSystem;
import com.timtips.systems.render.PhysicsSystem;
import com.timtips.systems.render.RenderSystem;
import com.timtips.tween.ColorTweener;
import com.timtips.tween.TransformTweener;

public class LevelScreen extends TimTipsScreen {

	public static float maxLight = 0.4f;
	private TimtipsBaseGame game;
	public static TweenManager tweenManager = new TweenManager();
	private MyContactListener cl;
	public Timer fadeInTimer = new Timer(2);
	public Timer fadeOutTimer = new Timer(2);

	public LevelScreen(TimtipsBaseGame game) {
		this.game = game;

	}

	public Color ambientLightColor = new Color(0.4f, 0.4f, 0.4f, 1f);
	public boolean levelDone = false;
	private float curStand = -1;

	@Override
	public void dispose() {
		physWorld.dispose();
		LevelScreen.tweenManager = new TweenManager();
	}

	@Override
	public void hide() {}

	public void init() {
		physWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), false);
		physWorld.setAutoClearForces(false);
		cl = new MyContactListener(game);
		physWorld.setContactListener(cl);
		game.debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);
		game.debugRenderMatrix = new Matrix4();
		game.debugRenderMatrix.scale(Physic.phys2pixel, Physic.phys2pixel, 1);

		ray = new RayHandler(game.getPhysWorld());
		RayHandler.useDiffuseLight(true);
		ray.setAmbientLight(ambientLightColor);
		Tween.setCombinedAttributesLimit(4);
		world = new World();
		world.setSystem(new PhysicsSystem(game));

		world.setSystem(new BackgroundSystem(game), false);
		world.setSystem(new TextNodeSystem(game), false);
		world.setSystem(new GravitySystem(game));
		world.setSystem(new MapSystem(game));
		world.setSystem(new TeleporterSystem(game));
		world.setSystem(new RenderSystem(game), true);
		Gdx.input.setInputProcessor(world.setSystem(new PlayerSystem(game)));
		world.setSystem(new CleanupSystem(game));
		world.setSystem(new TimerSystem(game));
		world.setSystem(new GuiSystem(game), true);
		world.initialize();
		Tween.registerAccessor(Transform.class, new TransformTweener());
		Tween.registerAccessor(Entity.class, new EntityTweener(game));
		Tween.registerAccessor(Color.class, new ColorTweener());

		game.getOrtho().zoom = 2.5f;
		// game.getOrtho().position.set(TimtipsBaseGame.WIDTH / 2f, TimtipsBaseGame.HEIGHT / 2f, 0);
		// ActorFactory.instance(game).getGround();
		// ActorFactory.instance(game).getWall(0, 500, 50, 1000);
		// ActorFactory.instance(game).getWall(1000, 500, 50, 1000);
		// ActorFactory.instance(game).getWall(500, 1000, 1000, 50);
		// ActorFactory.instance(game).getWall(500, 500, 200, 200);
		cl.init();

	}

	@Override
	public void pause() {}

	public void restart() {
		Physic p = PlayerSystem.player.getComponent(Physic.class);
		p.myBody.setTransform(MapSystem.playerStart.x * Physic.pixel2phys, MapSystem.playerStart.y * Physic.pixel2phys, 0);
		p.myBody.setLinearVelocity(0, 0);
	}

	@Override
	public void render(float delta) {

		// System.out.println("Phys: " + (System.currentTimeMillis() - begin));
		if (!fadeInTimer.isDone()) {
			float intVal = fadeInTimer.getPercentage();
			intVal = Interpolation.exp10In.apply(intVal);
			ambientLightColor.set(LevelScreen.maxLight * intVal, LevelScreen.maxLight * intVal, LevelScreen.maxLight * intVal, 1);
			game.getRay().setAmbientLight(ambientLightColor);
			fadeInTimer.update(delta);
		}
		if (!fadeOutTimer.isDone() && levelDone) {
			float intVal = fadeOutTimer.getPercentage();
			if (curStand == -1f) {
				curStand = ambientLightColor.r;
			}
			intVal = 1 - Interpolation.exp10In.apply(intVal);
			ambientLightColor.set(curStand * intVal, curStand * intVal, curStand * intVal, 1);
			game.getRay().setAmbientLight(ambientLightColor);
			fadeOutTimer.update(delta);
		}
		if (fadeOutTimer.isDone() && levelDone) {
			if (game.curLevel > TimtipsBaseGame.NUMBER_OF_LEVELS) {
				game.desiredState = TimtipsBaseGame.MAIN_MENU;
			} else {
				game.desiredState = TimtipsBaseGame.INTERMED;
			}
		}
		world.setDelta(fadeInTimer.isDone() && !levelDone ? delta : 0);
		updatePhysics(fadeInTimer.isDone() && !levelDone ? delta : 0);
		LevelScreen.tweenManager.update(delta);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		// Gdx.gl.glClearColor(66 / 255f, 69 / 255f, 84 / 255f, 1);
		Gdx.gl.glClearColor(11 / 255f, 12 / 255f, 7 / 255f, 1);
		/*
		 * Render Game World
		 */
		game.getBatch().setProjectionMatrix(game.getOrtho().combined);
		game.getOrtho().update();

		game.startBatch();
		world.getSystem(TextNodeSystem.class).process();
		world.getSystem(RenderSystem.class).process();
		// world.getSystem(BackgroundSystem.class).process();

		// game.getBatch().draw(AssetManager.instance(game).getTexture(TexType.bg), 0, 0, TimtipsBaseGame.WIDTH, TimtipsBaseGame.HEIGHT);
		game.getWorld().process();

		game.getBatch().setProjectionMatrix(AssetManager.instance(game).getGuiCam().combined);

		world.getSystem(GuiSystem.class).process();
		game.endBatch();
		game.debugRenderMatrix.set(game.getOrtho().combined);
		game.debugRenderMatrix.scale(Physic.phys2pixel, Physic.phys2pixel, 1);
		ray.setCombinedMatrix(game.debugRenderMatrix);
		if (TimtipsBaseGame.drawPhysDebug) {
			game.debugRenderer.render(game.getPhysWorld(), game.debugRenderMatrix);
		}
		ray.updateAndRender();
		if (fadeInTimer.getPercentage() < 0.33f) {
			game.startBatch();
			game.getBatch().setColor(0, 0, 0, 1 - fadeInTimer.getPercentage() * 3);
			game.getBatch().draw(AssetManager.instance(game).getTexture(TexType.bg), -3000, -3000, 5000, 5000);
			game.endBatch();
		}
		if (fadeOutTimer.getPercentage() > 0.66f) {
			game.startBatch();
			game.getBatch().setColor(0, 0, 0, fadeOutTimer.getPercentage() * 0.33f);
			game.getBatch().draw(AssetManager.instance(game).getTexture(TexType.bg), -3000, -3000, 5000, 5000);
			game.endBatch();
		}

	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void resume() {}

	@Override
	public void show() {}

	private float updatePhysics(float frameTime) {
		if (frameTime > game.maxStepLength) {
			frameTime = game.maxStepLength;
		}
		game.accumulator += frameTime;

		while (game.accumulator >= game.dt) {
			game.getPhysWorld().step(game.dt, game.velocityIterations, game.positionIterations);
			game.accumulator -= game.dt;
		}
		game.getPhysWorld().clearForces();

		TimtipsBaseGame.updateDeltaCorretion = frameTime / game.idealTimeStep;

		// int stepsPerformed = 0;
		// float processed = 0;
		// while (frameTime > 0.0 && stepsPerformed < game.MAXIMUM_NUMBER_STEPS) {
		// float deltaTime = Math.min(frameTime, game.FIXED_TIMESTEP);
		// frameTime -= deltaTime;
		// if (frameTime < game.MINIMUM_TIMESTEP) {
		// deltaTime += frameTime;
		// frameTime = 0.0f;
		// }
		// game.getPhysWorld().step(deltaTime, game.velocityIterations, game.positionIterations);
		// processed += deltaTime;
		// stepsPerformed++;
		// }
		// game.getPhysWorld().clearForces();
		// if (game.logTimings && System.currentTimeMillis() - begin > TimtipsBaseGame.timeLogThreshold) {
		// System.out.println("Phys Update: " + (System.curr entTimeMillis() - begin));
		// }
		return frameTime;
	}

	public void decLights() {
		LevelScreen.maxLight -= 0.02f;
		ambientLightColor.set(LevelScreen.maxLight, LevelScreen.maxLight, LevelScreen.maxLight, LevelScreen.maxLight);
		game.getRay().setAmbientLight(ambientLightColor);
	}

	public void incLights() {
		LevelScreen.maxLight = 0.4f;
		ambientLightColor.set(LevelScreen.maxLight, LevelScreen.maxLight, LevelScreen.maxLight, LevelScreen.maxLight);
		game.getRay().setAmbientLight(ambientLightColor);
	}

	public void win() {

		game.curLevel++;
		levelDone = true;
	}
}
