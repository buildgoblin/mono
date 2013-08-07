package com.timtips.ld26.components;

import com.artemis.Component;

public class Wall extends Component {
	private WallColor color;

	public Wall(WallColor color) {
		this.color = color;
	}

	public enum WallColor {
		NONE, BLACK, ORANGE, BLUE
	};
}
