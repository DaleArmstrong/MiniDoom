package minidoom.game.managers;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import kuusisto.tinysound.TinySound;

public class SoundManager {
	private static Music music;
	private static Map<String, Sound> sounds;

	static {
		sounds = new HashMap<>();
		TinySound.init();
		TinySound.setGlobalVolume(0.01);
		music = TinySound.loadMusic("music/intro.wav");

		BufferedReader reader;
		String line;
		try {
			reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(SoundManager.class
					.getClassLoader().getResourceAsStream("resourcelists/sounds.txt"))));

			line = reader.readLine();
			while (line != null) {
				sounds.put(line, TinySound.loadSound("sounds/"+ line + ".wav"));
				line = reader.readLine();
			}
		} catch (IOException e) {
			System.out.println("Can't read sounds list" + e.getMessage());
		}
	}

	public static Sound getSound(String name) {
		return sounds.get(name);
	}

	public static void playLevelMusic(int level) {
		if (music.playing())
			music.stop();
		music.unload();
		music = TinySound.loadMusic("music/level" + level + ".mid");
		music.play(true, 0.5);
	}

	public static void playIntro() {
		if (music.playing())
			music.stop();
		music.unload();
		music = TinySound.loadMusic("music/intro.wav");
		music.play(true, 1);
	}
}
