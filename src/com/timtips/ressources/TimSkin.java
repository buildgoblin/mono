package com.timtips.ressources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class TimSkin extends Skin {

	private TextureAtlas atlas;
	NinePatchDrawable highscore_bg_patch;

	public TimSkin(String string) {
		super();
		atlas = new TextureAtlas(string);
		addRegions(atlas);
		add("fireMedium", new BitmapFont(Gdx.files.internal("data/fonts/fireMedium.fnt"), atlas.findRegion("fireMedium"), false));
		LabelStyle highscoreLabelStyle = new LabelStyle();
		highscore_bg_patch = new NinePatchDrawable(new NinePatch(getRegion("background_highscore"), 10, 10, 10, 10));
		highscoreLabelStyle.background = highscore_bg_patch;
		highscoreLabelStyle.font = getFont("fireMedium");
		highscoreLabelStyle.fontColor = Color.WHITE;
		add("highscore", highscoreLabelStyle);
	}

	@Override
	public void dispose() {
		super.dispose();
		atlas.dispose();
	}
}
