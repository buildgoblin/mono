package com.timtips.ld26.components;

import com.artemis.Component;
import com.timtips.ld26.components.Wall.WallColor;

public class Trigger extends Component {

	private WallColor color;

	public Trigger(WallColor color) {
		this.color = color;
	}

}
