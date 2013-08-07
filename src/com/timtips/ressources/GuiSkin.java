package com.timtips.ressources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.timtips.interfaces.TimtipsBaseGame;

public class GuiSkin {
	private final TextureAtlas guiAtlas;
	public Skin skin;

	TimtipsBaseGame game;
	public NinePatch menuPatch;

	public GuiSkin(TimtipsBaseGame game) {
		this.game = game;

		skin = new Skin();

		guiAtlas = new TextureAtlas(Gdx.files.internal("data/gui.txt"));
		skin.addRegions(guiAtlas);
		skin.add("font", game.getFont(), BitmapFont.class);
		skin.add("mediumFont", game.getMediumFont(), BitmapFont.class);

		// LabelStyle labelStyle = new LabelStyle();
		// labelStyle.background = skin.getDrawable("textfield");
		// labelStyle.font = skin.getFont("font");
		// labelStyle.fontColor = Color.WHITE;
		// skin.add("default", labelStyle);

		TextButtonStyle lockedStyle = new TextButtonStyle();

		lockedStyle.up = skin.getDrawable("lockedSelection");
		lockedStyle.down = skin.getDrawable("lockedSelectionDown");

		lockedStyle.font = skin.getFont("font");

		skin.add("lockedStyle", lockedStyle);

		menuPatch = new NinePatch(skin.getRegion("menuBorder"), 14, 14, 14, 14);

		TextButtonStyle selectStyle = new TextButtonStyle();
		selectStyle.checked = skin.getDrawable("selection");
		selectStyle.up = new NinePatchDrawable(menuPatch);
		selectStyle.down = skin.getDrawable("selectionDown");
		selectStyle.over = skin.getDrawable("selectionOver");
		selectStyle.font = skin.getFont("font");
		skin.add("selection", selectStyle);

		TextButtonStyle ac = new TextButtonStyle();

		ac.font = skin.getFont("mediumFont");
		NinePatch ninePatch = new NinePatch(skin.getRegion("achievementToast"), 5, 5, 5, 5);
		ac.up = new NinePatchDrawable(ninePatch);
		skin.add("achievement", ac);

		LabelStyle lsac = new LabelStyle();
		lsac.font = skin.getFont("mediumFont");

		skin.add("achievement", lsac);

		TextButtonStyle point = new TextButtonStyle();
		point.checked = skin.getDrawable("pointPlate");
		point.up = skin.getDrawable("pointPlate");
		point.down = skin.getDrawable("pointPlate");
		point.over = skin.getDrawable("pointPlate");
		point.font = skin.getFont("mediumFont");

		skin.add("pointButtonStyle", point);

	}
}
