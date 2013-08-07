package com.timtips.components;

import com.artemis.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Physic extends Component {
	public static final short CATEGORY_PLAYER = 0x0001; // 0000000000000001 in
	public static final short CATEGORY_ENVIRONMENT = 0x0004; // 0000000000000100

	public static final short MASK_NONE = 0x0000;

	public Body myBody;

	public FixtureDef fixDef = new FixtureDef();

	// public static float pixel2phys = 0.0666666666666666666666f;
	// public static float phys2pixel = 15;

	public static float pixel2phys = 0.0166666666666666f;
	public static float phys2pixel = 60;

	public boolean independentTransform = false;

}
