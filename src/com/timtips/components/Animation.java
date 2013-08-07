package com.timtips.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

public class Animation extends Component {
	public enum AnimationType {
		none, explode, fish, shark, sharkAttack, fishBlue, jellyfish, fish1_auge, fish1_bauch, fish1_body, fish1_ruecken, fish1_schwanz_hinten, fish1_schwanz_vorne, fish1_seitenflosse
	};

	public AnimationType type;
	public boolean pingpong = false;
	public boolean markedforRemoval = false;
	public boolean looping = true;
	public boolean flippedHor = false;
	public boolean flippedVer = false;
	public Color color = Color.WHITE;

	public Animation(AnimationType type) {
		this.type = type;
	}

	public Animation(AnimationType type, boolean pingpong) {
		this.type = type;
		this.pingpong = pingpong;
	}

	public Animation(AnimationType type, boolean pingpong, boolean looping) {
		this(type, pingpong);
		this.looping = looping;
	}

}
