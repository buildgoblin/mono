package com.timtips.ressources;

import java.util.LinkedList;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.timtips.components.CacheMarker;
import com.timtips.interfaces.TimtipsBaseGame;
import com.timtips.systems.logic.CleanupSystem;

public class ObjectCache {
	private final TimtipsBaseGame game;

	private final LinkedList<Entity> objects = new LinkedList<Entity>();

	private boolean enabled = true;

	private final ComponentMapper<CacheMarker> cacheMapper;

	public ObjectCache(TimtipsBaseGame game) {
		this.game = game;
		cacheMapper = game.getWorld().getMapper(CacheMarker.class);
	}

	public boolean available() {
		return enabled && objects.size() > 0;
	}

	public void cache(Entity e) {
		if (cacheMapper.has(e)) {
			e.removeComponent(CacheMarker.class);
		}
		game.getWorld().getSystem(CleanupSystem.class).destroyPhysics(e);
		e.changedInWorld();
		e.disable();
		objects.add(e);

	}

	public void disable() {
		enabled = false;
	}

	public void enable() {
		enabled = true;
	}

	public Entity get() {
		Entity ret = objects.removeFirst();
		ret.enable();
		return ret;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public int size() {
		return objects.size();
	}

}
