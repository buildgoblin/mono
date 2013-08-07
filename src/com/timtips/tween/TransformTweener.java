package com.timtips.tween;

import aurelienribon.tweenengine.TweenAccessor;

import com.timtips.components.Transform;

public class TransformTweener implements TweenAccessor<Transform> {
	public static final int POS_X = 0;
	public static final int POS_Y = 1;
	public static final int POS_XY = 2;
	public static final int DIM_X = 3;
	public static final int DIM_Y = 4;
	public static final int DIM_XY = 5;
	public static final int POS_Y_REL = 6;

	private float posYtmp = 0;

	@Override
	public int getValues(Transform target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case POS_X:
			returnValues[0] = target.x;
			return 1;
		case POS_Y:
			returnValues[0] = target.y;
			return 1;
		case POS_XY:
			returnValues[0] = target.x;
			returnValues[1] = target.y;
			return 2;
		case DIM_X:
			returnValues[0] = target.dimX;
			return 1;
		case DIM_Y:
			returnValues[0] = target.dimY;
			return 1;
		case DIM_XY:
			returnValues[0] = target.dimX;
			returnValues[1] = target.dimY;
			return 2;
		case POS_Y_REL:
			returnValues[0] = posYtmp;
			return 1;

		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(Transform target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case POS_X:
			target.x = newValues[0];
			break;
		case POS_Y:
			target.y = newValues[0];
			break;
		case POS_XY:
			target.x = newValues[0];
			target.y = newValues[1];
			break;
		case DIM_X:
			target.dimX = newValues[0];
			break;
		case DIM_Y:
			target.dimY = newValues[0];
			break;
		case DIM_XY:
			target.dimX = newValues[0];
			target.dimY = newValues[1];
			break;
		case POS_Y_REL:
			posYtmp = newValues[0];
			target.y += posYtmp;
			break;
		default:
			assert false;
			break;
		}
	}
}
