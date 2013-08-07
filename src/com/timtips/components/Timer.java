package com.timtips.components;

import com.artemis.Component;

public class Timer extends Component {

	public enum TimerType {
		SELF_PARTICLE, NONE, REMOVE_NO_CHECKS, FIRE
	}

	float duration = 0;

	float cur = 0;;

	public TimerType type = TimerType.NONE;

	public Timer(float duration) {
		this.duration = duration;
		cur = 0;
	}

	public Timer(float duration, TimerType type) {
		this(duration);
		this.type = type;
	}

	public float getDuration() {
		return duration;
	}

	public float getPercentage() {
		return Math.min(1f, cur / duration);
	}

	public float getTime() {
		return cur;
	}

	public float getTimeRemaining() {
		return duration - cur;
	}

	public boolean isDone() {
		if (cur >= duration) {
			return true;
		}
		return false;
	}

	public void reset() {
		cur = 0;
	}

	public void setDone() {
		cur = duration;
	}

	public Timer setDuration(float duration) {
		this.duration = duration;
		return this;
	}

	@Override
	public String toString() {
		return "Timer [duration=" + duration + ", cur=" + cur + ", type=" + type + "]";
	}

	public void update(float delta) {
		cur += delta;
	}

}
