package com.timtips.ressources;

public class Achievement {
	public enum AchievementType {
		LEVEL_1, LEVEL_3, LEVEL_6, LEVEL_9, LEVEL_12, LEVEL_15, LEVEL_18, LEVEL_21, LEVEL_24
	};

	public boolean done = false;
	public AchievementType type;
	public String title;
	public String msg;
	public Unlock unlock;

	public Achievement(AchievementType type, String title, String msg, Unlock unlock) {
		super();
		this.type = type;
		this.title = title;
		this.msg = msg;
		this.unlock = unlock;
	}
}
