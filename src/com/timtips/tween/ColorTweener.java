package com.timtips.tween;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.Color;

public class ColorTweener implements TweenAccessor<Color> {
	public static final int RGBA = 0;
	public static final int RGB = 1;

	@Override
	public int getValues(Color target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case RGBA:
			returnValues[0] = target.r;
			returnValues[1] = target.g;
			returnValues[2] = target.b;
			returnValues[3] = target.a;
			return 4;
		case RGB:
			returnValues[0] = target.r;
			returnValues[1] = target.g;
			returnValues[2] = target.b;

			return 3;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(Color target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case RGBA:
			target.r = newValues[0];
			target.g = newValues[1];
			target.b = newValues[2];
			target.a = newValues[3];
			break;
		case RGB:
			target.r = newValues[0];
			target.g = newValues[1];
			target.b = newValues[2];

			break;
		default:
			assert false;
			break;
		}
	}
}
