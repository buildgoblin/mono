package com.timtips.ld26.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.timtips.components.Transform;
import com.timtips.interfaces.TimtipsBaseGame;
import com.timtips.ld26.ressources.TextNode;

public class TextNodeSystem extends EntityProcessingSystem {
	private TimtipsBaseGame game;
	private ComponentMapper<Transform> transformMapper;
	private ComponentMapper<TextNode> textMapper;

	public TextNodeSystem(TimtipsBaseGame game) {
		super(Aspect.getAspectForAll(TextNode.class));
		this.game = game;
	}

	@Override
	protected void initialize() {
		super.initialize();
		textMapper = world.getMapper(TextNode.class);
		transformMapper = world.getMapper(Transform.class);
	}

	@Override
	protected void process(Entity e) {
		String msg = textMapper.get(e).text;
		Transform t = transformMapper.get(e);
		game.getMediumFont().setColor(Color.WHITE);
		game.getMediumFont().setScale(game.getOrtho().zoom);
		game.getMediumFont().drawWrapped(game.getBatch(), "\"" + msg + "\"", t.x - t.dimX / 2, t.y, t.dimX);
	}
}
