package com.timtips.components;

import com.artemis.Component;
import com.artemis.utils.Utils;
import com.badlogic.gdx.math.Vector2;

public class Transform extends Component {

	public float x = 0;
	public float y = 0;
	public float dimX = 0;

	public float dimY = 0;

	public Transform() {}

	public Transform(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Transform(float x, float y, float dimX, float dimY) {
		this(x, y);
		this.dimX = dimX;
		this.dimY = dimY;
	}

	public Transform(Transform transform) {
		x = transform.x;
		y = transform.y;
		dimX = transform.dimX;
		dimY = transform.dimY;

	}

	public Transform(Vector2 pos, float dimX, float dimY) {
		x = pos.x;
		y = pos.y;
		this.dimX = dimX;
		this.dimY = dimY;
	}

	public void add(float x2, float y2) {
		x += x2;
		y += y2;

	}

	public void addX(float x) {
		this.x += x;
	}

	public void addY(float y) {
		this.y += y;
	}

	public boolean contains(float x2, float y2) {
		if (x2 > x - dimX / 2 && x2 < x + dimX / 2 && y2 > y - dimY / 2 && y2 < y + dimY / 2) {
			return true;
		}
		return false;
	}

	public boolean contains(Transform top) {
		return contains(top.x, top.y);
	}

	public void convert2Phys() {
		x = Physic.pixel2phys * x;
		y = Physic.pixel2phys * y;
	}

	public void convert2Pixel() {
		x = Physic.phys2pixel * x;
		y = Physic.phys2pixel * y;
		dimX = Physic.phys2pixel * dimX;
		dimY = Physic.phys2pixel * dimY;
	}

	public float getCenterDistanceTo(Transform targetT) {
		return Utils.distance(targetT.getCenteredX(), targetT.getCenteredY(), getCenteredX(), getCenteredY());

	}

	public float getCenteredX() {
		return x - dimX / 2f;
	}

	public float getCenteredY() {
		return y - dimY / 2f;
	}

	public float getDistanceTo(float f, float y2) {
		return Utils.distance(x, y, f, y2);
	}

	public float getDistanceTo(Transform t) {
		return Utils.distance(t.getX(), t.getY(), x, y);
	}

	public float getDistanceTo(Vector2 target) {
		return Utils.distance(target.x, target.y, x, y);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public boolean intersects(Transform t2) {
		/*
		 * // Unten Links if (includes(t2.x - dimX / 2, t2.y - t2.dimY / 2)) { return true; } // Oben Links if (includes(t2.x - t2.dimX / 2, t2.y +
		 * t2.dimY / 2)) { return true; } // Oben Rechts if (includes(t2.x + t2.dimX / 2, t2.y + t2.dimY / 2)) { return true; } // Unten Rechts if
		 * (includes(t2.x + t2.dimX / 2, t2.y - t2.dimY / 2)) { return true; }
		 */
		// return false;

		if (Math.abs(x - t2.x) > dimX / 2 + t2.dimX / 2) {
			return false;
		}
		if (Math.abs(y - t2.y) > dimY / 2 + t2.dimY / 2) {
			return false;
		}
		return true;
	}

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void set(float x, float y, float dimX, float dimY) {
		this.x = x;
		this.y = y;
		this.dimX = dimX;
		this.dimY = dimY;
	}

	public void set(Transform transform) {
		x = transform.x;
		y = transform.y;
		dimX = transform.dimX;
		dimY = transform.dimY;

	}

	public void setDim(float x, float y) {
		dimX = x;
		dimY = y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Transform [x=" + x + ", y=" + y + ", dimX=" + dimX + ", dimY=" + dimY + "]";
	}

	public Transform sub(Transform t) {
		x -= t.x;
		y -= t.y;
		return this;
	}

}
