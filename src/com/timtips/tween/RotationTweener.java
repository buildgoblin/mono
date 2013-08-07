package com.timtips.tween;

import aurelienribon.tweenengine.TweenAccessor;

import com.timtips.components.Rotation;

public class RotationTweener implements TweenAccessor<Rotation> {
	public static final int DEGREE = 0;

	@Override
	public int getValues(Rotation target, int tweenType, float[] returnValues) {
		switch (tweenType) {

		case DEGREE:
			returnValues[0] = target.getDegrees();
			return 1;

		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(Rotation target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case DEGREE:
			target.setDegrees(newValues[0]);
			break;
		default:
			assert false;
			break;
		}
	}
}
