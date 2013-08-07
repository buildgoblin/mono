package com.timtips.components;

import com.artemis.Component;

public class Rotation extends Component {
	public enum RotationStyle {
		CENTERED
	}

	public float degrees = 0;

	public Rotation(float degrees) {
		this.degrees = degrees;
	}

	public float getDegrees() {
		return degrees;
	}

	public void setDegrees(float degrees) {
		this.degrees = degrees;

		while (degrees > 360) {
			degrees -= 360;
		}

		while (degrees < 0) {
			degrees += 360;
		}

	}

	public float getRad() {
		return (float) Math.toRadians(degrees);
	}
}
