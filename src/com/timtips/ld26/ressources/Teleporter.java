package com.timtips.ld26.ressources;

import com.artemis.Component;

public class Teleporter extends Component {
	public String name;
	public String target;
	public boolean triggered = false;

	public Teleporter(String name, String target) {
		super();
		this.name = name;
		this.target = target;
	}

	@Override
	public String toString() {
		return "Teleporter [name=" + name + ", target=" + target + ", triggered=" + triggered + "]";
	}

}
