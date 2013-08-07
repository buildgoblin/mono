package com.timtips.ld26;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "MONO";
		cfg.useGL20 = true;
		cfg.width = 670;
		cfg.height = 670;
		cfg.vSyncEnabled = false;

		new LwjglApplication(new LD26(), cfg);
	}
}
