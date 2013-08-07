package com.timtips.components;

import com.artemis.Component;

public class Particle extends Component {
	public enum ParticleType {
		WATER, FIRE, STEAM, WATER_HIT_FIRE, WATER_NO_PHYS, SPLASH, TRANSIT_FLAME, DROP, BUBBLE, FOOD, BLOOD, DUST
	};

	public ParticleType type;

	public boolean active = true;

	public Particle(ParticleType type) {
		this.type = type;

	}
}
