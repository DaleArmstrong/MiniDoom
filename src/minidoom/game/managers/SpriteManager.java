package minidoom.game.managers;

import minidoom.game.generators.EntityGenerator;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static javax.imageio.ImageIO.read;

/**
 * Sprite manager loads and stores the images used for all the sprites. If a request is
 * made and the image is not available, it will check and load the image from the sprites
 * folder.
 */
public class SpriteManager {
	private static Map<String, BufferedImage> spriteMap = new HashMap<>();

	static {
		addSprite("missing");

		BufferedReader reader;
		String line;
		try {
			reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(EntityGenerator.class
					.getClassLoader().getResourceAsStream("resourcelists/sprites.txt"))));

			line = reader.readLine();
			while (line != null) {
				addSprite(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			System.out.println("Can't read sprites list" + e.getMessage());
		}
	}

	public static void addSprite(String fileName) {
		BufferedImage newSprite;
		try {
			newSprite = read(Objects.requireNonNull(SpriteManager.class.getClassLoader()
					.getResource("sprites/" + fileName + ".png")));
		} catch (Exception e) {
			newSprite = spriteMap.get("missing");
			System.out.println("Missing sprite: " + fileName);
		}

		spriteMap.put(fileName, newSprite);
	}

	public static BufferedImage getSprite(String fileName) {
		if (fileName == null)
			return null;

		if (!spriteMap.containsKey(fileName))
			addSprite(fileName);

		return spriteMap.getOrDefault(fileName, null);
	}

	public static void addImage(String fileName, BufferedImage image) {
		if (!spriteMap.containsKey(fileName))
			spriteMap.put(fileName, image);
	}

	public static boolean hasImage(String fileName) {
		return spriteMap.containsKey(fileName + ".png");
	}
}
