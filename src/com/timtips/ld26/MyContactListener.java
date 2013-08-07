package com.timtips.ld26;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.timtips.components.EntityContacts;
import com.timtips.components.LDContact;
import com.timtips.components.Physic;
import com.timtips.components.Transform;
import com.timtips.interfaces.TimtipsBaseGame;
import com.timtips.ld26.components.Player;
import com.timtips.ld26.components.Wall;
import com.timtips.ld26.systems.PlayerSystem;

public class MyContactListener implements ContactListener {

	private Entity eA;
	private Entity eB;
	private TimtipsBaseGame game;
	private Object physMapper;
	private Object transformMapper;
	private ComponentMapper<Player> playerMapper;
	private ComponentMapper<Wall> wallMapper;
	private ComponentMapper<EntityContacts> contactMapper;

	public MyContactListener(TimtipsBaseGame game) {
		this.game = game;

	}

	public void init() {
		transformMapper = game.getWorld().getMapper(Transform.class);
		physMapper = game.getWorld().getMapper(Physic.class);
		playerMapper = game.getWorld().getMapper(Player.class);
		wallMapper = game.getWorld().getMapper(Wall.class);
		contactMapper = game.getWorld().getMapper(EntityContacts.class);
	}

	@Override
	public void beginContact(Contact contact) {}

	@Override
	public void endContact(Contact contact) {}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		eA = (Entity) contact.getFixtureA().getBody().getUserData();
		eB = (Entity) contact.getFixtureB().getBody().getUserData();
		if (eA == PlayerSystem.player) {
			contactMapper.get(eA).contactedEntities.add(new LDContact(contact.getWorldManifold().getNormal(), eB, -contact.getFixtureA().getBody().getLinearVelocity()
					.len(), contact.getWorldManifold().getPoints()[0]));
		} else if (eB == PlayerSystem.player) {

			contactMapper.get(eB).contactedEntities.add(new LDContact(contact.getWorldManifold().getNormal(), eA, contact.getFixtureB().getBody().getLinearVelocity()
					.len(), contact.getWorldManifold().getPoints()[0]));

		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
}
