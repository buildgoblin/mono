package com.timtips.systems.render;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.timtips.components.Timer;
import com.timtips.interfaces.TimtipsBaseGame;

public class GuiSystem extends EntityProcessingSystem {

	private final TimtipsBaseGame game;

	private long begin = 0;

	private long end = 0;

	private final Timer fadeInTimer = new Timer(0.5f);

	public GuiSystem(TimtipsBaseGame game) {
		super(Aspect.getEmpty());

		this.game = game;
	};

	@Override
	protected void begin() {
		fadeInTimer.update(world.getDelta());
		begin = System.currentTimeMillis();
		// game.getMediumFont().setScale(0.5f);
		// game.getMediumFont().draw(game.getBatch(), "FPS " + Gdx.graphics.getFramesPerSecond(), 10, 20);
		game.getMediumFont().setScale(1);
	}

	@Override
	protected void end() {

		super.end();
		end = System.currentTimeMillis();
		if (game.logTimings && end - begin > TimtipsBaseGame.timeLogThreshold) {
			Gdx.app.log(this.getClass().getName(), "Processing Time: " + (end - begin));
		}
	}

	@Override
	protected void initialize() {
		fadeInTimer.reset();
		// TODO Auto-generated method stub
		super.initialize();

	}

	@Override
	protected void process(Entity e) {

	}
}
