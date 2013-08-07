package com.timtips.components;

import com.artemis.Component;

public class Paralax extends Component {
	public enum ParaType {
		CONTIN, SINGLE
	};

	public ParaType type;
	public boolean triggered = false;
	public float amount;
	public int layer;

	public Paralax(ParaType type, float amount, boolean preTriggered, int layer) {
		super();
		this.type = type;
		this.amount = amount;
		triggered = preTriggered;
		this.layer = layer;
	}
}
