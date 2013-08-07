package com.timtips.ld26.systems;

import java.util.LinkedList;

import aurelienribon.tweenengine.Tween;
import box2dLight.Light;
import box2dLight.PointLight;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.timtips.components.EntityContacts;
import com.timtips.components.LDContact;
import com.timtips.components.Physic;
import com.timtips.components.Renderable;
import com.timtips.components.Renderable.TexType;
import com.timtips.components.SSound.SoundType;
import com.timtips.components.Timer;
import com.timtips.components.Transform;
import com.timtips.interfaces.TimtipsBaseGame;
import com.timtips.ld26.components.Acc;
import com.timtips.ld26.components.Evil;
import com.timtips.ld26.components.LightSource;
import com.timtips.ld26.components.Player;
import com.timtips.ld26.components.Target;
import com.timtips.ld26.components.Trigger;
import com.timtips.ld26.components.WColor;
import com.timtips.ld26.components.Wall;
import com.timtips.ld26.components.Wall.WallColor;
import com.timtips.ld26.ressources.ActorFactory;
import com.timtips.ld26.screens.LevelScreen;
import com.timtips.ressources.AssetManager;
import com.timtips.ressources.AudioManager;
import com.timtips.tween.ColorTweener;

public class PlayerSystem extends EntityProcessingSystem implements InputProcessor {

	private TimtipsBaseGame game;
	public static Entity player;
	private ComponentMapper<Physic> physMapper;
	private Vector2 impHelper = new Vector2();
	private ComponentMapper<Transform> transformMapper;
	private ComponentMapper<EntityContacts> contactMapper;
	private ComponentMapper<Wall> wallMapper;
	private Physic p;
	private Timer spaceTimer = new Timer(0.2f);
	private boolean keyDownList[] = new boolean[255];
	private LDContact lastContact = null;
	private float timeSinceLastContact = 1;
	private ComponentMapper<Evil> evilMapper;
	private ComponentMapper<Target> targetMapper;
	private ComponentMapper<Acc> accMapper;
	private ComponentMapper<Trigger> triggerMapper;
	private ComponentMapper<WColor> wcMapper;
	public Timer bumpReplayTimer = new Timer(0.05f);
	private Entity lastEnt;
	private ComponentMapper<Renderable> renderMapper;
	public Color playerColor = new Color(1, 1, 1, 1);

	public static float MAX_VEL_X = 40;
	public static float MAX_VEL_Y = 40;
	public static final float ACC_X = 20;
	public static final float ACC_Y = 20;

	private Transform lastTrans = new Transform(Integer.MAX_VALUE, 0);

	@SuppressWarnings("unchecked")
	public PlayerSystem(TimtipsBaseGame game) {
		super(Aspect.getAspectForAll(Player.class));
		this.game = game;
	}

	@Override
	protected void initialize() {
		super.initialize();

		physMapper = world.getMapper(Physic.class);
		transformMapper = world.getMapper(Transform.class);
		contactMapper = world.getMapper(EntityContacts.class);
		wallMapper = world.getMapper(Wall.class);
		evilMapper = world.getMapper(Evil.class);
		p = physMapper.get(PlayerSystem.player);
		targetMapper = world.getMapper(Target.class);
		accMapper = world.getMapper(Acc.class);
		triggerMapper = world.getMapper(Trigger.class);
		wcMapper = world.getMapper(WColor.class);
		renderMapper = world.getMapper(Renderable.class);

	}

	@Override
	protected void begin() {
		super.begin();
		bumpReplayTimer.update(world.getDelta());
		timeSinceLastContact += world.getDelta();
		spaceTimer.update(world.getDelta());

	}

	@Override
	protected void process(Entity e) {
		Transform t = transformMapper.get(e);

		LinkedList<LDContact> contacts = contactMapper.get(e).contactedEntities;
		// if (contacts.size() == 0 && !p.myBody.isAwake()) {
		// applyImp(impHelper.set(0, 0));
		// }
		if (lastContact != null) {
			float dst = transformMapper.get(e).getDistanceTo(lastContact.pos.x * Physic.phys2pixel, lastContact.pos.y * Physic.phys2pixel);
			if (dst > 24) {
				lastContact = null;
			}
		}

		for (LDContact c : contacts) {
			if (lastContact == null || c.other != lastEnt) {
				lastContact = c;
				lastEnt = c.other;
				if (wallMapper.has(c.other)) {
					AudioManager.instance(game).getSound(SoundType.jump).play();
					world.getSystem(MapSystem.class).transferSystem(WallColor.NONE);

				} else if (evilMapper.has(c.other)) {
					AudioManager.instance(game).getSound(SoundType.evil).play();
					((LevelScreen) game.getScreen()).decLights();
					Color clr = renderMapper.get(c.other).color;

					Tween.to(clr, ColorTweener.RGB, 0.2f).target(1, clr.g, clr.b).repeatYoyo(1, 0).start(LevelScreen.tweenManager);
					((LevelScreen) game.getScreen()).restart();

				} else if (targetMapper.has(c.other)) {
					AudioManager.instance(game).getSound(SoundType.target).play();
					Color clr = renderMapper.get(c.other).color;

					Tween.to(clr, ColorTweener.RGB, 0.5f).target(1, 1, 1).repeatYoyo(1, 0).start(LevelScreen.tweenManager);
					((LevelScreen) game.getScreen()).win();

				} else if (accMapper.has(c.other) && bumpReplayTimer.isDone()) {
					bumpReplayTimer.reset();
					Color clr = renderMapper.get(c.other).color;
					Tween.to(clr, ColorTweener.RGB, 0.1f).target(clr.r, clr.g + 0.1f, clr.b + 0.1f).repeatYoyo(1, 0).start(LevelScreen.tweenManager);

					AudioManager.instance(game).getSound(SoundType.acc).play();

					timeSinceLastContact = 0;

					lastContact.normal.set(0, accMapper.get(lastContact.other).speed);
					lastContact.normal.rotate(accMapper.get(lastContact.other).dir);
					impHelper.set(lastContact.normal);

					p.myBody.applyLinearImpulse(impHelper, p.myBody.getWorldCenter(), true);

				} else if (triggerMapper.has(c.other)) {
					world.getSystem(MapSystem.class).transferSystem(wcMapper.get(c.other).color);
					AudioManager.instance(game).getSound(SoundType.trigger).play(0.5f);
					Color clr = renderMapper.get(c.other).color;
					if (!LevelScreen.tweenManager.containsTarget(clr)) {
						Tween.to(clr, ColorTweener.RGB, 0.3f).target(1, 1, 1).repeatYoyo(1, 0).start(LevelScreen.tweenManager);
					}

				}

				int conts = 0;
				for (Light l : game.getRay().lightList) {
					if (l.getPosition().dst(c.pos) < 100 * Physic.pixel2phys) {
						// if (l.contains(c.pos.x, c.pos.y)) {
						conts++;
						// }
					}
				}

				if (conts <= 1) {
					Entity pl = ActorFactory.instance(game).getPL(50, new Color(0.6f, 0.6f, 0.6f, 0.3f), 300, c.pos.x * Physic.phys2pixel, c.pos.y * Physic.phys2pixel);
					PointLight light = pl.getComponent(LightSource.class).light;
					light.setXray(true);

				}

			}
		}

		handleInput();
		contacts.clear();
		adjustCam(t);

		game.getBatch().setColor(playerColor);
		game.getBatch().draw(AssetManager.instance(game).getTexture(TexType.player), t.x - t.dimX / 2f, t.y - t.dimY / 2f, t.dimX, t.dimY);
		game.getBatch().setColor(Color.WHITE);
	}

	public void handleInput() {
		if (keyDownList[Keys.D] || keyDownList[Keys.RIGHT]) {
			p.myBody.applyForceToCenter(PlayerSystem.ACC_X, 0, true);
		}
		if (keyDownList[Keys.A] || keyDownList[Keys.LEFT]) {
			p.myBody.applyForceToCenter(-PlayerSystem.ACC_X, 0, true);
		}
		if (keyDownList[Keys.W] || keyDownList[Keys.UP]) {
			p.myBody.applyForceToCenter(0, PlayerSystem.ACC_Y, true);
		}
		if (keyDownList[Keys.S] || keyDownList[Keys.DOWN]) {
			p.myBody.applyForceToCenter(0, -PlayerSystem.ACC_Y, true);
		}
		if (p.myBody.getLinearVelocity().x > PlayerSystem.MAX_VEL_X) {
			p.myBody.setLinearVelocity(PlayerSystem.MAX_VEL_X, p.myBody.getLinearVelocity().y);
		}
		if (p.myBody.getLinearVelocity().x < -PlayerSystem.MAX_VEL_X) {
			p.myBody.setLinearVelocity(-PlayerSystem.MAX_VEL_X, p.myBody.getLinearVelocity().y);
		}
		if (p.myBody.getLinearVelocity().y > PlayerSystem.MAX_VEL_Y) {
			p.myBody.setLinearVelocity(p.myBody.getLinearVelocity().x, PlayerSystem.MAX_VEL_Y);
		}
		if (p.myBody.getLinearVelocity().y < -PlayerSystem.MAX_VEL_Y) {
			p.myBody.setLinearVelocity(p.myBody.getLinearVelocity().x, -PlayerSystem.MAX_VEL_Y);
		}
	}

	public void adjustCam(Transform t) {
		if (!world.getSystem(MapSystem.class).fixedCam) {

			game.getOrtho().position.add((t.x - game.getOrtho().position.x) * 0.1f, (t.y - game.getOrtho().position.y) * 0.1f, 0);
			if (game.getOrtho().position.x < TimtipsBaseGame.WIDTH / 2f) {
				game.getOrtho().position.x = TimtipsBaseGame.WIDTH / 2f;
			}
			if (game.getOrtho().position.x > world.getSystem(MapSystem.class).width - TimtipsBaseGame.WIDTH / 2f) {
				game.getOrtho().position.x = world.getSystem(MapSystem.class).width - TimtipsBaseGame.WIDTH / 2f;
			}
			if (game.getOrtho().position.y < TimtipsBaseGame.HEIGHT / 2f + 160) {
				game.getOrtho().position.y = TimtipsBaseGame.HEIGHT / 2f + 160;
			}
			if (game.getOrtho().position.y > world.getSystem(MapSystem.class).height - TimtipsBaseGame.HEIGHT / 2f) {
				game.getOrtho().position.y = world.getSystem(MapSystem.class).height - TimtipsBaseGame.HEIGHT / 2f;
			}
		}
	}

	@Override
	public boolean keyDown(int keycode) {

		keyDownList[keycode] = true;
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.P) {
			TimtipsBaseGame.drawPhysDebug = !TimtipsBaseGame.drawPhysDebug;
		}
		if (keycode == Keys.ESCAPE) {
			game.setScreen(game.pauseScreen);
		}
		keyDownList[keycode] = false;
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
