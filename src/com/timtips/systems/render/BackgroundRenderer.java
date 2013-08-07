package com.timtips.systems.render;

import com.badlogic.gdx.utils.Disposable;
import com.timtips.interfaces.TimtipsBaseGame;

public class BackgroundRenderer implements Disposable {

	private final TimtipsBaseGame game;

	public BackgroundRenderer(TimtipsBaseGame game) {

		this.game = game;

		// Init layer 1

	}

	// public void autoScroll() {
	// amount.add(0, Gdx.graphics.getDeltaTime() * -500);
	//
	// s1.setU(amount.x / (800 * parallaxAmountL1));
	// s1.setU2(s1.getU() + 800 / ParalaxManager.l1TWidth);
	// s1.setV(amount.y / (480 * parallaxAmountL1));
	// s1.setV2(s1.getV() + 480 / ParalaxManager.l1TWidth);
	// // s2.setU(amount.x / (TileSystem.tileWidth * TileSystem.tilesX * parallaxAmountL2));
	// // s2.setU2(s2.getU() + TileSystem.tilesX * TileSystem.tileWidth / ParalaxManager.L2TSize);
	// // s2.setV(amount.y / (TileSystem.tileHeight * TileSystem.tilesY * parallaxAmountL2));
	// // s2.setV2(s2.getV() + TileSystem.tilesY * TileSystem.tileHeight / ParalaxManager.L2TSize);
	// // // render LAyer 1
	// s1.draw(game.getBatch());
	// // s2.draw(game.getBatch());
	// }

	@Override
	public void dispose() {

	}

	public void render() {

		// render LAyer 1

		// s1.draw(game.getBatch());

	}

	public void renderStatic() {

		// s1.setPosition(game.getOrtho().position.x - TimtipsBaseGame.WIDTH / 2f, game.getOrtho().position.y - TimtipsBaseGame.HEIGHT / 2);

		// s1.draw(game.getBatch());
	}

}
