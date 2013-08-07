package com.timtips.ld26;

import com.timtips.components.SSound.SoundType;
import com.timtips.interfaces.TimtipsBaseGame;
import com.timtips.ld26.screens.IntermedScreen;
import com.timtips.ld26.screens.LevelScreen;
import com.timtips.ld26.screens.MenuScreen;
import com.timtips.ld26.screens.PauseScreen;

public class LD26 extends TimtipsBaseGame {

	public SoundType curMusic = SoundType.silence;
	public String[] titles = { "Start", "Knock Knock", "U-Turn", "Teleport", "Sideways", "Sprint", "Light", "The End" };

	@Override
	public void create() {
		super.create();
		pauseScreen = new PauseScreen(this);

	}

	@Override
	public void goToMainMenu() {
		menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
		((MenuScreen) menuScreen).init();

		state = TimtipsBaseGame.MAIN_MENU;

	}

	@Override
	public void startLevel() {

		lvlScreen = new LevelScreen(this);

		setScreen(lvlScreen);
		((LevelScreen) lvlScreen).init();

		state = TimtipsBaseGame.LEVEL;
	}

	@Override
	protected void goToHighscoreScreen() {}

	@Override
	protected void goToIntermedScreen() {
		intermedScreen = new IntermedScreen(this);

		setScreen(intermedScreen);
		((IntermedScreen) intermedScreen).init();

		state = TimtipsBaseGame.INTERMED;
	}
}
