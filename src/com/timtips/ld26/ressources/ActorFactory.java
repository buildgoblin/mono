package com.timtips.ld26.ressources;

import java.util.HashMap;

import box2dLight.PointLight;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.timtips.components.EntityContacts;
import com.timtips.components.Physic;
import com.timtips.components.Renderable;
import com.timtips.components.Renderable.TexType;
import com.timtips.components.Rotation;
import com.timtips.components.Transform;
import com.timtips.interfaces.TimtipsBaseGame;
import com.timtips.ld26.components.Acc;
import com.timtips.ld26.components.Evil;
import com.timtips.ld26.components.Gravity;
import com.timtips.ld26.components.LightSource;
import com.timtips.ld26.components.Player;
import com.timtips.ld26.components.Rotateable;
import com.timtips.ld26.components.Target;
import com.timtips.ld26.components.Trigger;
import com.timtips.ld26.components.WColor;
import com.timtips.ld26.components.Wall;
import com.timtips.ld26.components.Wall.WallColor;

public class ActorFactory {
	private static ActorFactory instance;
	private TimtipsBaseGame game;
	private BodyDef bodyDef;
	private Color evilColor = new Color(140 / 255f, 22 / 255f, 44 / 255f, 255 / 255f);
	// private Color evilColor = Color.BLACK;
	private Color goodColor = new Color(255 / 255f, 237 / 255f, 209 / 255f, 255 / 255f);
	private HashMap<WallColor, Color> wallColors = new HashMap<WallColor, Color>();

	private ActorFactory(TimtipsBaseGame game) {
		this.game = game;
		bodyDef = new BodyDef();
		wallColors.put(WallColor.NONE, goodColor);
		wallColors.put(WallColor.BLACK, new Color(66 / 255f, 69 / 255f, 84 / 255f, 1));
		wallColors.put(WallColor.ORANGE, new Color(242 / 255f, 127 / 255f, 61 / 255f, 1));
		wallColors.put(WallColor.BLUE, new Color(100 / 255f, 30 / 255f, 150 / 255f, 1));
	}

	public static ActorFactory instance(TimtipsBaseGame game) {
		if (ActorFactory.instance == null) {
			ActorFactory.instance = new ActorFactory(game);
		}
		return ActorFactory.instance;
	}

	public Entity getWall(float x, float y, float width, float height, boolean rotatable, float amount, int duration, WallColor color) {
		x += width / 2f;
		y += height / 2f;
		Entity e = game.getWorld().createEntity();
		Transform t = new Transform(x, y, width, height);
		e.addComponent(t);
		e.addComponent(new Rotation(0));

		e.addComponent(new Renderable(TexType.bg, wallColors.get(color)));
		e.addComponent(new Wall(color));
		if (rotatable) {
			e.addComponent(new Rotateable(amount, duration));
		}

		Physic p = new Physic();
		p.fixDef.shape = new PolygonShape();
		p.fixDef.friction = 1;
		p.fixDef.restitution = 0;
		((PolygonShape) p.fixDef.shape).setAsBox(width / 2f * Physic.pixel2phys, height / 2f * Physic.pixel2phys);
		e.addComponent(p);
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(x * Physic.pixel2phys, y * Physic.pixel2phys);
		p.myBody = game.getPhysWorld().createBody(bodyDef);
		p.myBody.createFixture(p.fixDef);
		p.myBody.setUserData(e);
		e.addComponent(new WColor(color));
		e.addToWorld();
		System.out.println("Creating wall: " + t);

		return e;
	}

	public Entity getEvil(float x, float y, float width, float height, boolean rotatable, float amount, float duration, WallColor c) {
		x += width / 2f;
		y += height / 2f;
		Entity e = game.getWorld().createEntity();
		e.addComponent(new Transform(x, y, width, height));
		e.addComponent(new Rotation(0));
		e.addComponent(new Renderable(TexType.bg, evilColor));
		e.addComponent(new Evil());
		if (rotatable) {
			e.addComponent(new Rotateable(amount, (int) duration));
		}

		Physic p = new Physic();
		p.fixDef.shape = new PolygonShape();
		((PolygonShape) p.fixDef.shape).setAsBox(width / 2f * Physic.pixel2phys, height / 2f * Physic.pixel2phys);
		e.addComponent(p);
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(x * Physic.pixel2phys, y * Physic.pixel2phys);
		p.myBody = game.getPhysWorld().createBody(bodyDef);
		p.myBody.createFixture(p.fixDef);
		p.myBody.setUserData(e);
		e.addComponent(new WColor(c));
		e.addToWorld();

		return e;
	}

	public Entity getPlayer(Float x, Float y) {
		Entity e2 = game.getWorld().createEntity();
		e2.addComponent(new Transform(x, y, 96, 96));
		e2.addComponent(new Rotation(0));
		e2.addComponent(new Renderable(TexType.playerStart));
		e2.addToWorld();
		System.out.println("Initing player at : " + x + "," + y);
		Entity e = game.getWorld().createEntity();
		e.addComponent(new Transform(x, y, 48, 48));
		e.addComponent(new Rotation(0));
		// e.addComponent(new Renderable(TexType.player));
		e.addComponent(new Player());

		Physic p = new Physic();
		p.fixDef.shape = new CircleShape();
		p.fixDef.shape.setRadius(Physic.pixel2phys * 20);
		p.fixDef.restitution = 0.5f;
		bodyDef.linearDamping = 1f;
		bodyDef.bullet = true;

		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x * Physic.pixel2phys, y * Physic.pixel2phys);
		p.myBody = game.getPhysWorld().createBody(bodyDef);
		p.myBody.createFixture(p.fixDef);
		p.myBody.setUserData(e);
		e.addComponent(p);
		e.addComponent(new EntityContacts());
		LightSource l = new LightSource();
		PointLight pl = new PointLight(game.getRay(), 40, new Color(1f, 1f, 1f, 0.5f), 100 * Physic.pixel2phys, 0, 0);
		l.light = pl;
		e.addComponent(l);
		pl.attachToBody(p.myBody, 0, 0);
		e.addToWorld();

		return e;

	}

	public Entity getPL(int rays, Color c, float dst, float posX, float posY) {
		Entity e = game.getWorld().createEntity();
		LightSource ls = new LightSource();
		e.addComponent(ls);
		ls.light = new PointLight(game.getRay(), rays, c, dst * Physic.pixel2phys, posX * Physic.pixel2phys, posY * Physic.pixel2phys);
		ls.light.setXray(false);
		ls.light.setStaticLight(true);
		ls.light.setSoft(true);
		ls.light.setSoftnessLenght(1.5f);
		return e;
	}

	public Entity getTarget(float x, float y) {
		System.out.println("Initing target at : " + x + "," + y);
		Entity e = game.getWorld().createEntity();
		e.addComponent(new Transform(x, y, 48, 48));
		e.addComponent(new Rotation(45));
		e.addComponent(new Renderable(TexType.bg, Color.PINK));
		e.addComponent(new Rotateable(0, 0));

		Physic p = new Physic();
		p.fixDef.shape = new CircleShape();
		p.fixDef.shape.setRadius(Physic.pixel2phys * 32);
		p.fixDef.restitution = 0.3f;
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(x * Physic.pixel2phys, y * Physic.pixel2phys);
		p.myBody = game.getPhysWorld().createBody(bodyDef);
		p.myBody.createFixture(p.fixDef);
		p.myBody.setUserData(e);
		e.addComponent(p);
		e.addComponent(new Target());

		LightSource l = new LightSource();
		PointLight pl = new PointLight(game.getRay(), 40, new Color(0.6f, 0f, 1f, 0.2f), 200 * Physic.pixel2phys, 0, 0);
		l.light = pl;
		e.addComponent(l);
		pl.attachToBody(p.myBody, 0, 0);
		e.addToWorld();

		return e;
	}

	public void getAcc(float x, float y, float width, float height, boolean rotateable, Integer amount, Integer duration, Integer speed, Integer dir) {
		Entity wall = getWall(x, y, width, height, rotateable, amount, duration, WallColor.NONE);
		wall.removeComponent(Wall.class);
		wall.addComponent(new Acc(speed, dir));
		wall.getComponent(Renderable.class).color = new Color(52 / 255f, 88 / 255f, 112 / 255f, 1);
		wall.changedInWorld();
		System.out.println("loading acc");
	}

	public void getTrigger(float x, float y, float width, float height, WallColor color) {
		Entity wall = getWall(x, y, width, height, false, 0, 0, color);
		wall.removeComponent(Wall.class);
		wall.addComponent(new Trigger(color));
		wall.getComponent(Renderable.class).color = wallColors.get(color);
		wall.changedInWorld();
		System.out.println("loading trigger");
	}

	public Entity getWallPoly(Polygon polygon, boolean rotateable, Integer amount, Integer duration, WallColor color) {
		Pixmap pm = new Pixmap(100, 100, Format.RGBA8888);

		Entity e = game.getWorld().createEntity();
		Rectangle bounds = polygon.getBoundingRectangle();
		Transform t = new Transform(polygon.getX(), polygon.getY() - bounds.height / 2f, bounds.width, bounds.height);
		e.addComponent(t);
		e.addComponent(new Rotation(0));

		e.addComponent(new Renderable(TexType.triangle, wallColors.get(color)));
		e.addComponent(new Wall(color));
		if (rotateable) {
			e.addComponent(new Rotateable(amount, duration));
		}

		Physic p = new Physic();
		p.independentTransform = true;
		p.fixDef.shape = new PolygonShape();
		p.fixDef.friction = 1;
		p.fixDef.restitution = 0;
		float[] vertices = polygon.getVertices();
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] *= Physic.pixel2phys;
		}
		((PolygonShape) p.fixDef.shape).set(vertices);
		e.addComponent(p);
		bodyDef.type = BodyType.StaticBody;

		bodyDef.position.set(polygon.getX() * Physic.pixel2phys, polygon.getY() * Physic.pixel2phys);
		p.myBody = game.getPhysWorld().createBody(bodyDef);
		p.myBody.createFixture(p.fixDef);
		p.myBody.setUserData(e);

		e.addComponent(new WColor(color));
		e.addToWorld();
		System.out.println("Creating wall: " + t);

		return e;
	}

	public void getArt(float x, float y, float width, float height, Color color) {
		x += width / 2f;
		y += height / 2f;
		Entity e = game.getWorld().createEntity();
		e.addComponent(new Transform(x, y, width, height));
		e.addComponent(new Rotation(0));
		System.out.println(color);

		e.addComponent(new Renderable(TexType.bg, Color.valueOf(color.toString())));
		e.addToWorld();
	}

	public void getTeleporter(float x, float y, float width, float height, Color c, String name, String target) {
		x += width / 2f;
		y += height / 2f;
		Entity e = game.getWorld().createEntity();
		e.addComponent(new Transform(x, y, width, height));
		e.addComponent(new Rotation(0));

		e.addComponent(new Renderable(TexType.bg, c));
		e.addComponent(new Teleporter(name, target));
		e.addToWorld();
	}

	public void getGravity(float x, float y, float width, float height, Float gx, Float gy) {
		x += width / 2f;
		y += height / 2f;
		Entity e = game.getWorld().createEntity();
		e.addComponent(new Transform(x, y, width, height));
		e.addComponent(new Rotation(0));
		e.addComponent(new Gravity(gx, gy));
		e.addToWorld();
	}

	public void getTextNode(float x, float y, float width, float height, String text) {
		System.out.println("Adding textnode with text: " + text);
		x += width / 2f;
		y += height;
		Entity e = game.getWorld().createEntity();
		e.addComponent(new Transform(x, y, width, height));
		e.addComponent(new Rotation(0));
		text = text.replace("BREAK", "\n");
		e.addComponent(new TextNode(text));
		e.addToWorld();
	}

}
