package com.timtips.ld26.ressources;

import aurelienribon.tweenengine.TweenAccessor;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.timtips.components.Physic;
import com.timtips.interfaces.TimtipsBaseGame;

public class EntityTweener implements TweenAccessor<Entity> {
	public final static int ROTATABLE = 0;
	private ComponentMapper<Physic> physMapper;

	public EntityTweener(TimtipsBaseGame game) {
		physMapper = game.getWorld().getMapper(Physic.class);
	}

	@Override
	public int getValues(Entity target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case ROTATABLE:
			returnValues[0] = (float) Math.toDegrees(physMapper.get(target).myBody.getAngle());
			return 1;

		default:
			break;
		}
		return 0;
	}

	@Override
	public void setValues(Entity target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case ROTATABLE:
			physMapper.get(target).myBody.setTransform(physMapper.get(target).myBody.getPosition().x, physMapper.get(target).myBody.getPosition().y,
					(float) Math.toRadians(newValues[0]));
			break;

		default:
			break;
		}

	}
}
