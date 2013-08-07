package com.timtips.ld26.components;

import com.artemis.Component;

public class Rotateable extends Component {

	public int duration = 1;

	public Rotateable(float amount, int duration) {
		this.amount = amount;
		this.duration = duration;
	}

	public float amount = 90;

}
