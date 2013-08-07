package com.timtips.components;

import com.artemis.Component;

public class SSound extends Component {
	public enum MusicType {
		MAIN_SCORE
	}

	public enum SoundType {
		silence, jump, evil, target, acc, trigger, teleport, ld26, selected, hover, l1
	}

	public float maxDistHearable = 2000;

	public SoundType type;

	public long id = -1;

	public boolean looping;
	public boolean stopOnDeath = true;
	public float volume = 1;

	public SSound(int hearDist, SoundType type, boolean looping, float volume) {
		this.type = type;
		this.looping = looping;
		this.volume = 1;
		maxDistHearable = hearDist;
	}

	public SSound(SoundType type, boolean looping) {
		this.type = type;
		this.looping = looping;

	}

	public SSound(SoundType type, boolean stopOnDeath, float volume) {
		super();
		this.type = type;
		this.stopOnDeath = stopOnDeath;
		this.volume = volume;
	}

}
