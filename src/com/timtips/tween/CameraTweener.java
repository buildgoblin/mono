package com.timtips.tween;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraTweener implements TweenAccessor<OrthographicCamera> {
	public static final int POS_X = 0;
	public static final int POS_Y = 1;
	public static final int POS_XY_REL = 2;

	public static final int ZOOM = 4;
	private float x = 0;
	private float y = 0;

	@Override
	public int getValues(OrthographicCamera target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case POS_X:
			returnValues[0] = target.position.x;
			return 1;
		case POS_Y:
			returnValues[0] = target.position.y;
			return 1;
		case POS_XY_REL:
			returnValues[0] = x;
			returnValues[1] = y;
			return 2;

		case ZOOM:
			returnValues[0] = target.zoom;

			return 1;

		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(OrthographicCamera target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case POS_X:
			target.position.x = newValues[0];
			break;
		case POS_Y:
			target.position.y = newValues[0];
			break;
		case POS_XY_REL:
			target.position.x += newValues[0];
			target.position.y += newValues[1];
			x = newValues[0];
			y = newValues[1];
			break;
		case ZOOM:
			target.zoom = newValues[0];
			break;
		default:
			assert false;
			break;
		}
	}
}
