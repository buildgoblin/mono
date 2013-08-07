package com.timtips.components;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;

public class LDContact {
	public Vector2 normal;
	public Entity other;
	public float dir = 1;
	public Vector2 pos;

	public LDContact(Vector2 normal, Entity other, float f, Vector2 pos) {
		super();
		this.normal = normal;
		this.other = other;
		dir = f;
		this.pos = pos;
	}
}
