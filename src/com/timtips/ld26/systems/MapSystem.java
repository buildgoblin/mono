package com.timtips.ld26.systems;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.timtips.components.Physic;
import com.timtips.interfaces.TimtipsBaseGame;
import com.timtips.ld26.LD26;
import com.timtips.ld26.components.Rotateable;
import com.timtips.ld26.components.WColor;
import com.timtips.ld26.components.Wall.WallColor;
import com.timtips.ld26.ressources.ActorFactory;
import com.timtips.ld26.ressources.EntityTweener;
import com.timtips.ld26.screens.LevelScreen;

public class MapSystem extends EntityProcessingSystem {
	public static Vector2 playerStart = new Vector2();
	private TimtipsBaseGame game;
	private float curRot = 0;
	private Vector2 playerKeep = new Vector2();
	private ComponentMapper<Physic> physMapper;
	private ComponentMapper<Rotateable> rotateableMapper;
	private ComponentMapper<WColor> wcolorMapper;
	public float width;
	int height;
	public boolean fixedCam;
	public static boolean inTransition = false;

	public MapSystem(TimtipsBaseGame game) {
		super(Aspect.getAspectForAll(Rotateable.class));

		this.game = game;

	}

	@Override
	protected void initialize() {
		super.initialize();
		TiledMap map = new TmxMapLoader().load("data/maps/" + ((LD26) game).titles[game.curLevel] + ".tmx");
		width = (Integer) map.getProperties().get("width") * 32 + 32;
		height = (Integer) map.getProperties().get("height") * 32 + 32;
		PlayerSystem.MAX_VEL_X = Float.valueOf((String) map.getProperties().get("MAX_VEL_X"));
		PlayerSystem.MAX_VEL_Y = Float.valueOf((String) map.getProperties().get("MAX_VEL_Y"));
		fixedCam = Boolean.valueOf((String) map.getProperties().get("fixedCam"));
		Object maxBrightness = map.getProperties().get("maxBrightness");
		if (maxBrightness != null) {
			float maxValue = Float.valueOf((String) maxBrightness);
			LevelScreen.maxLight = maxValue;
			((LevelScreen) game.getScreen()).ambientLightColor.set(maxValue, maxValue, maxValue, 1);
			((LevelScreen) game.getScreen()).decLights();

		} else {
			LevelScreen.maxLight = 0.4f;
		}
		if (fixedCam) {
			game.getOrtho().position.set(width / 2f, height / 2f, 0);
		}
		System.out.println("width: " + width);
		MapLayers layers = map.getLayers();
		for (int i = 0; i < layers.getCount(); i++) {
			System.out.println("layer: " + layers.get(i).getName());
			if (layers.get(i).getName().equals("good")) {
				for (MapObject o : layers.get(i).getObjects()) {
					parseWall(o);
				}
			}
			if (layers.get(i).getName().equals("acc")) {
				for (MapObject o : layers.get(i).getObjects()) {
					parseAcc(o);
				}
			}
			if (layers.get(i).getName().equals("trigger")) {
				for (MapObject o : layers.get(i).getObjects()) {
					parseTrigger(o);
				}
			}
			if (layers.get(i).getName().equals("evil")) {
				for (MapObject o : layers.get(i).getObjects()) {
					parseEvil(o);
				}
			}
			if (layers.get(i).getName().equals("teleporter")) {
				for (MapObject o : layers.get(i).getObjects()) {
					parseTeleporter(o);
				}
			}
			if (layers.get(i).getName().equals("art")) {
				for (MapObject o : layers.get(i).getObjects()) {
					parseArt(o);
				}
			}
			if (layers.get(i).getName().equals("gravity")) {
				for (MapObject o : layers.get(i).getObjects()) {
					parseGravity(o);
				}
			}
			if (layers.get(i).getName().equals("texts")) {
				for (MapObject o : layers.get(i).getObjects()) {
					parseTextnode(o);
				}
			}
			if (layers.get(i).getName().equals("obj")) {
				for (MapObject o : layers.get(i).getObjects()) {
					if (o.getName().equals("playerstart")) {
						parsePlayerStart(o);
					}
					if (o.getName().equals("target")) {
						parseTargetArea(o);
					}

				}
			}
			if (layers.get(i).getName().equals("lights")) {
				for (MapObject o : layers.get(i).getObjects()) {
					parseLight(o);

				}
			}
		}
		ActorFactory.instance(game).getWall(-32, -32, 32, height, false, 0, 0, WallColor.NONE);
		ActorFactory.instance(game).getWall(-32, -32, width, 32, false, 0, 0, WallColor.NONE);
		ActorFactory.instance(game).getWall(width - 32, -32, 32, height + 32, false, 0, 0, WallColor.NONE);
		ActorFactory.instance(game).getWall(-32, height - 32, width, 32, false, 0, 0, WallColor.NONE);
		// ActorFactory.instance(game).getWall(0, 0, 32, height, false).removeComponent(Rotateable.class).changedInWorld();
		// ActorFactory.instance(game).getWall(0, 0, width, 32, false).removeComponent(Rotateable.class).changedInWorld();
		// ActorFactory.instance(game).getWall(width, 0, 32, height, false).removeComponent(Rotateable.class).changedInWorld();
		// ActorFactory.instance(game).getWall(0, height, width, 32, false).removeComponent(Rotateable.class).changedInWorld();
		physMapper = world.getMapper(Physic.class);
		rotateableMapper = world.getMapper(Rotateable.class);
		wcolorMapper = world.getMapper(WColor.class);
	}

	private void parseTextnode(MapObject o) {
		RectangleMapObject pol = (RectangleMapObject) o;
		Rectangle rec = pol.getRectangle();
		ActorFactory.instance(game).getTextNode(rec.x, rec.y, rec.width, rec.height, (String) pol.getProperties().get("text"));
	}

	private void parseGravity(MapObject o) {
		RectangleMapObject pol = (RectangleMapObject) o;
		Rectangle rec = pol.getRectangle();
		ActorFactory.instance(game).getGravity(rec.x, rec.y, rec.width, rec.height, Float.valueOf((String) pol.getProperties().get("x")),
				Float.valueOf((String) pol.getProperties().get("y")));
	}

	private void parseTeleporter(MapObject o) {
		RectangleMapObject pol = (RectangleMapObject) o;
		Rectangle rec = pol.getRectangle();
		ActorFactory.instance(game).getTeleporter(rec.x, rec.y, rec.width, rec.height, Color.valueOf((String) pol.getProperties().get("color")), pol.getName(),
				(String) pol.getProperties().get("target"));
	}

	private void parseLight(MapObject o) {
		RectangleMapObject pol = (RectangleMapObject) o;
		Rectangle rec = pol.getRectangle();
		ActorFactory.instance(game).getPL(40, new Color(0.5f, 0.5f, 0.5f, 0.5f), 1000, rec.x, rec.y);
	}

	private void parseArt(MapObject o) {
		RectangleMapObject pol = (RectangleMapObject) o;
		Rectangle rec = pol.getRectangle();

		Color c = Color.valueOf((String) o.getProperties().get("color"));

		ActorFactory.instance(game).getArt(rec.x, rec.y, rec.width, rec.height, c);
	}

	public void parseTargetArea(MapObject o) {
		float x = o.getProperties().get("x", Integer.class);
		float y = o.getProperties().get("y", Integer.class);
		ActorFactory.instance(game).getTarget(x, y);
	}

	public void parsePlayerStart(MapObject o) {
		float x = o.getProperties().get("x", Integer.class);
		float y = o.getProperties().get("y", Integer.class);
		PlayerSystem.player = ActorFactory.instance(game).getPlayer(x, y);
		MapSystem.playerStart.set(x, y);
	}

	public void parseEvil(MapObject o) {
		RectangleMapObject pol = (RectangleMapObject) o;
		Rectangle rec = pol.getRectangle();
		String am = (String) pol.getProperties().get("turnsfor");
		float amount = 0;
		if (am != null) {
			amount = Float.valueOf(am);
		}
		String d = (String) pol.getProperties().get("duration");
		float duration = 0;
		if (d != null) {
			duration = Float.valueOf(d);
		}
		String co = (String) pol.getProperties().get("color");
		WallColor c = WallColor.NONE;
		if (co != null) {
			c = WallColor.valueOf(co);
		}
		ActorFactory.instance(game).getEvil(rec.x, rec.y, rec.width, rec.height, pol.getProperties().containsKey("rotateable"), amount, duration, c);

	}

	public void parseTrigger(MapObject o) {
		RectangleMapObject pol = (RectangleMapObject) o;
		Rectangle rec = pol.getRectangle();
		String string = (String) pol.getProperties().get("color");
		WallColor color = string != null ? WallColor.valueOf(string) : WallColor.NONE;
		ActorFactory.instance(game).getTrigger(rec.x, rec.y, rec.width, rec.height, color);
	}

	public void parseAcc(MapObject o) {
		RectangleMapObject pol = (RectangleMapObject) o;
		Rectangle rec = pol.getRectangle();
		boolean rotateable = pol.getProperties().containsKey("rotateable");
		String string = (String) pol.getProperties().get("turnsfor");
		Integer amount = string != null ? Integer.valueOf(string) : 90;
		string = (String) pol.getProperties().get("duration");
		Integer duration = string != null ? Integer.valueOf(string) : 1;
		string = (String) pol.getProperties().get("speed");
		Integer speed = string != null ? Integer.valueOf(string) : 1;
		string = (String) pol.getProperties().get("dir");
		Integer dir = string != null ? Integer.valueOf(string) : 0;

		ActorFactory.instance(game).getAcc(rec.x, rec.y, rec.width, rec.height, rotateable, amount, duration, speed, dir);
	}

	public void parseWall(MapObject o) {
		if (o instanceof RectangleMapObject) {
			RectangleMapObject pol = (RectangleMapObject) o;
			Rectangle rec = pol.getRectangle();
			boolean rotateable = pol.getProperties().containsKey("rotateable");
			String string = (String) pol.getProperties().get("turnsfor");
			Integer amount = string != null ? Integer.valueOf(string) : 90;
			string = (String) pol.getProperties().get("duration");
			Integer duration = string != null ? Integer.valueOf(string) : 1;
			string = (String) pol.getProperties().get("color");
			WallColor color = string != null ? WallColor.valueOf(string) : WallColor.NONE;
			ActorFactory.instance(game).getWall(rec.x, rec.y, rec.width, rec.height, rotateable, amount, duration, color);
		}
		if (o instanceof PolygonMapObject) {
			PolygonMapObject pol = (PolygonMapObject) o;

			boolean rotateable = pol.getProperties().containsKey("rotateable");
			String string = (String) pol.getProperties().get("turnsfor");
			Integer amount = string != null ? Integer.valueOf(string) : 90;
			string = (String) pol.getProperties().get("duration");
			Integer duration = string != null ? Integer.valueOf(string) : 1;
			string = (String) pol.getProperties().get("color");
			WallColor color = string != null ? WallColor.valueOf(string) : WallColor.NONE;
			ActorFactory.instance(game).getWallPoly(pol.getPolygon(), rotateable, amount, duration, color);
		}
	}

	@Override
	protected void process(Entity e) {}

	public void transferSystem(WallColor color) {
		// physMapper.get(PlayerSystem.player).myBody.getFixtureList().get(0).setSensor(true);
		for (int j = 0; j < getActives().size(); j++) {
			if (!LevelScreen.tweenManager.containsTarget(getActives().get(j))) {
				WColor wc = wcolorMapper.getSafe(getActives().get(j));
				if (wc != null) {
					if (wc.color != color) {
						continue;
					}
				}
				Rotateable rotateable = rotateableMapper.get(getActives().get(j));
				Tween.to(getActives().get(j), EntityTweener.ROTATABLE, rotateable.duration).targetRelative(rotateable.amount).setCallback(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						if (type == TweenCallback.COMPLETE) {
							physMapper.get(PlayerSystem.player).myBody.getFixtureList().get(0).setSensor(false);
						}
					}
				}).start(LevelScreen.tweenManager);
			}
		}
	}
}
