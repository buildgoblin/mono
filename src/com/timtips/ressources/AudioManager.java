package com.timtips.ressources;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.timtips.components.SSound;
import com.timtips.components.SSound.SoundType;
import com.timtips.interfaces.TimtipsBaseGame;

public class AudioManager {

	private static AudioManager instance;

	public static AudioManager instance(TimtipsBaseGame game) {
		if (AudioManager.instance == null) {
			AudioManager.instance = new AudioManager(game);
		}
		return AudioManager.instance;
	}

	private final HashMap<SoundType, Sound> sounds = new HashMap<SoundType, Sound>();
	private final HashMap<SoundType, Music> music = new HashMap<SoundType, Music>();
	private final TimtipsBaseGame game;

	private AudioManager(TimtipsBaseGame game) {
		this.game = game;
	}

	public void dispose() {

		for (Sound s : sounds.values()) {
			s.dispose();
		}
		for (Music s : music.values()) {
			s.dispose();
		}
		AudioManager.instance = null;

	}

	public Music getMusic(SoundType type) {
		if (music.get(type) == null) {
			music.put(type, Gdx.audio.newMusic(Gdx.files.internal("data/music/" + type.name() + ".mp3")));
		}
		return music.get(type);
	}

	public Sound getSound(SoundType type) {
		if (!game.soundsOn) {
			type = SoundType.silence;
		}
		if (sounds.get(type) == null) {

			sounds.put(type, Gdx.audio.newSound(Gdx.files.internal("data/sounds/" + type.name() + ".mp3")));

		}
		return sounds.get(type);

	}

	public void preload() {
		for (SoundType s : SSound.SoundType.values()) {

		}
	}

}
