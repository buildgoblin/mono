package com.timtips.ressources;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.timtips.interfaces.TimtipsBaseGame;
import com.timtips.ressources.Achievement.AchievementType;

public class AchievementUtil {
	private static final int START_BUILDINGS = 3;
	private static final int START_AIRPLANES = 1;
	private static final int START_POWERUPS = 1;
	private static final int START_DO_NOT_HIT = 1;
	private static AchievementUtil instance;

	public static AchievementUtil instance(TimtipsBaseGame game) {
		if (AchievementUtil.instance == null) {
			AchievementUtil.instance = new AchievementUtil(game);
		}
		return AchievementUtil.instance;
	}

	protected TimtipsBaseGame game;

	public LinkedHashMap<AchievementType, Achievement> achievements = new LinkedHashMap<AchievementType, Achievement>();
	public ArrayList<Achievement> justUnlocked = new ArrayList<Achievement>();
	private Preferences ap;

	protected AchievementUtil(TimtipsBaseGame game) {
		this.game = game;
		loadFromFile();
		ap = Gdx.app.getPreferences("achievements");

	}

	public int getAchievementUnlockID(AchievementType type) {
		return ap.getInteger(type.toString() + "id", -1);

	}

	private void loadFromFile() {
		// try {

		// FileHandle f = Gdx.files.internal("data/lvl/achievements.txt");
		// Properties p = new Properties();
		// p.load(f.read());
		// ap = Gdx.app.getPreferences("achievements");
		// for (int i = 0; i < p.keySet().size(); i++) {
		// String dat[] = p.getProperty("" + i).split("\\|");
		// AchievementType type = AchievementType.valueOf(dat[0]);
		// String title = dat[1];
		// String msg = dat[2];
		// UnlockType unT = UnlockType.valueOf(dat[3]);
		// Achievement achievement = new Achievement(type, title, msg, new Unlock(unT));
		// achievement.done = ap.contains(dat[0] + "done");
		// if (achievement.done) {
		// achievement.unlock.id = getAchievementUnlockID(type);
		// }
		// achievements.put(type, achievement);
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

	}

	public void notify(AchievementType type) {
		if (!achievements.get(type).done) {
			unlockAchievement(type);
		}
	}

	public void reset() {
		AchievementUtil.instance = null;
	}

	private void unlockAchievement(AchievementType type) {
		Achievement achievement = achievements.get(type);
		achievement.done = true;
		writeAchievementDone(achievement);
		justUnlocked.add(achievement);
		// ActorFactory.instance(game).getToast(achievement.title, achievement.msg, type);
		if (achievement.unlock != null) {
			switch (achievement.unlock.type) {

			default:
				break;
			}
			writeAchievementUnlockID(achievement);
		}
	}

	private void writeAchievementDone(Achievement achievement) {
		ap = Gdx.app.getPreferences("achievements");
		ap.putBoolean(achievement.type.toString() + "done", true);
		ap.flush();
	}

	private void writeAchievementUnlockID(Achievement achievement) {
		ap = Gdx.app.getPreferences("achievements");
		ap.putInteger(achievement.type.toString() + "id", achievement.unlock.id);
		ap.flush();
	}
}
