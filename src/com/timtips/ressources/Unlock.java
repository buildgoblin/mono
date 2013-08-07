package com.timtips.ressources;

import com.timtips.components.Renderable.TexType;

public class Unlock {
	public enum UnlockType {
		BUILDING, BAD_BUILDING, AIRPLANE, POWERUP
	};

	public UnlockType type;
	public int id;
	private TexType tex;

	public Unlock(UnlockType type) {
		super();
		this.type = type;
	}

	public TexType getTexType() {
		if (tex == null) {
			switch (type) {
			case AIRPLANE:
				tex = TexType.valueOf("airplane" + id);
				break;
			case BAD_BUILDING:
				tex = TexType.valueOf("doNotHit" + id);
				break;
			case BUILDING:
				tex = TexType.valueOf("city" + id);
				break;
			case POWERUP:
			default:
				break;

			}

		}
		return tex;
	}

}
