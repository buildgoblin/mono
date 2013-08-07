package com.timtips.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

public class Renderable extends Component {
	public enum TexType {
		bgtile, player, bg, triangle, wallpaper, KnockKnock, UTurn, Sideways, Teleport, Start, Sprint, Light, logo, playerStart, TheEnd
	}

	public enum TransitionType {
		NONE, FADE_OUT, FADE_IN, FADE_IN_OUT
	};

	public TransitionType transType = TransitionType.NONE;
	public TexType type;
	float scale = 1;
	public float alpha = 1;
	public Color color = Color.WHITE;
	public boolean flippedHor = false;
	public boolean gui = false;

	public Renderable(TexType tex) {
		type = tex;
	}

	public Renderable(TexType type, boolean flippedHor) {
		super();
		this.type = type;
		this.flippedHor = flippedHor;
	}

	public Renderable(TexType tex, Color color) {
		this(tex);
		this.color = color;
	}

	public Renderable(TexType type, Color color, float alpha) {
		this.type = type;
		this.color = color;
		this.alpha = alpha;
	}

	public Renderable(TexType tex, Color c, TransitionType fadeOut) {
		this(tex, c);
		transType = fadeOut;
	}

	public Renderable(TexType tex, float scale) {
		type = tex;
		this.scale = scale;

	}

	public Renderable(TexType type, TransitionType transType) {
		this.type = type;
		this.transType = transType;
	}

	public float getScale() {
		return scale;
	}

	public TexType getType() {
		return type;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public void setType(TexType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Renderable [type=" + type + ", scale=" + scale + ", color=" + color + "]";
	}

}
