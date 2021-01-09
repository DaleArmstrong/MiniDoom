package minidoom.game;

import minidoom.entity.Player;
import minidoom.game.managers.SpriteManager;
import minidoom.game.weapons.Weapon;
import minidoom.util.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;
import java.util.List;

import static javax.imageio.ImageIO.read;

/**
 * Used to display information about the player. This includes their current weapon,
 * health, armor, and ammo.
 */
public class Hud {
	private enum FontType {
		SMALL_WHITE,
		SMALL_DARK,
		MEDIUM_WHITE,
		MEDIUM_DARK
	}

	private enum FontDirection {
		LEFT, RIGHT
	}

	private static Map<FontType, Map<Character, BufferedImage>> numberMap;
	private static Map<Character, BufferedImage> smallWhiteNumbers;
	private static Map<Character, BufferedImage> smallDarkNumbers;
	private static Map<Character, BufferedImage> mediumWhiteNumbers;
	private static Map<Character, BufferedImage> mediumDarkNumbers;
	private static Map<String, BufferedImage> hudWords;
	private static List<List<BufferedImage>> hudFaces;
	private static Map<Weapon.WeaponType, BufferedImage> miniWeapons;
	private static BufferedImage deadface;
	private static Color hudBGColor = new Color(1,1,1, 150);
	private Player player;
	private int healthValue;
	private int faceSelection;
	private BufferedImage currentFace;
	private float faceTimer;

	static {
		numberMap = new EnumMap<>(FontType.class);
		smallWhiteNumbers = new HashMap<>();
		smallDarkNumbers = new HashMap<>();
		mediumWhiteNumbers = new HashMap<>();
		mediumDarkNumbers = new HashMap<>();
		hudWords = new HashMap<>();
		hudFaces = new ArrayList<>();
		miniWeapons = new EnumMap<>(Weapon.WeaponType.class);
		BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Hud.class
				.getClassLoader().getResourceAsStream("hud/hud.txt"))));

		String line = null;
		try {
			line = reader.readLine();

			while (line != null) {
				String[] values = line.split("\t");
				addCharacter(values[0].charAt(0), values[1]);
				line = reader.readLine();
			}
		} catch (IOException e) {
			System.out.println("Can't read number characters " + e.getMessage());
		}

		try {
			reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Hud.class
					.getClassLoader().getResourceAsStream("hud/words.txt"))));

			line = reader.readLine();
			while (line != null) {
				addWord(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			System.out.println("Can't read word list " + e.getMessage());
		}
		numberMap.put(FontType.SMALL_WHITE, smallWhiteNumbers);
		numberMap.put(FontType.SMALL_DARK, smallDarkNumbers);
		numberMap.put(FontType.MEDIUM_WHITE, mediumWhiteNumbers);
		numberMap.put(FontType.MEDIUM_DARK, mediumDarkNumbers);

		List<BufferedImage> newList;
		BufferedImage currentImage;
		for (int i = 0; i < 5; i++) {
			newList = new ArrayList<>();
			for (int j = i * 5; j < (i + 1) * 5; j++) {
				try {
					currentImage = read(Objects.requireNonNull(Hud.class.getClassLoader()
							.getResource("hud/doomface/face" + j + ".png")));
				} catch (Exception e) {
					currentImage = SpriteManager.getSprite("missing");
					System.out.println("Could not load hud face: " + j);
				}
				newList.add(currentImage);
			}
			hudFaces.add(newList);
		}

		try {
			deadface = read(Objects.requireNonNull(Hud.class.getClassLoader()
					.getResource("hud/doomface/faceDead.png")));
		} catch (Exception e) {
			deadface = SpriteManager.getSprite("missing");
			System.out.println("Could not load deadFace");
		}

		try {
			reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Hud.class
					.getClassLoader().getResourceAsStream("hud/weapons.txt"))));

			line = reader.readLine();
			while (line != null) {
				String[] values = line.split("\t");
				addWeapon(values[0], values[1]);
				line = reader.readLine();
			}
		} catch (IOException e) {
			System.out.println("Can't read mini weapons list " + e.getMessage());
		}


	}


	public Hud(Player player) {
		this.player = player;
		this.faceTimer = 4.0f;
		this.faceSelection = 1;
		this.healthValue = 0;
	}

	private static void addWeapon(String type, String weaponName) {
		BufferedImage miniWeapon = null;
		try {
			miniWeapon = read(Objects.requireNonNull(Hud.class.getClassLoader()
					.getResource("hud/miniweapons/" + weaponName + ".png")));
		} catch (Exception e) {
			miniWeapon = SpriteManager.getSprite("missing");
			System.out.println("Could not load miniweapon: " + weaponName);
		}

		miniWeapons.put(Weapon.WeaponType.valueOf(type), miniWeapon);
	}

	/**
	 * Adds the character to the hashmaps
	 * @param character the character to be assigned to the image
	 * @param fileName the name of the image
	 */
	private static void addCharacter(Character character, String fileName) {
		BufferedImage smallWhiteNumber = null;
		BufferedImage smallDarkNumber = null;
		BufferedImage mediumWhiteNumber= null;
		BufferedImage mediumDarkNumber = null;
		try {
			smallWhiteNumber = read(Objects.requireNonNull(Hud.class.getClassLoader()
					.getResource("hud/smallwhitenumbers/" + fileName + ".png")));
			smallDarkNumber = read(Objects.requireNonNull(Hud.class.getClassLoader()
					.getResource("hud/smalldarknumbers/" + fileName + ".png")));
			mediumWhiteNumber = read(Objects.requireNonNull(Hud.class.getClassLoader()
					.getResource("hud/mediumwhitenumbers/" + fileName + ".png")));
			mediumDarkNumber = read(Objects.requireNonNull(Hud.class.getClassLoader()
					.getResource("hud/mediumdarknumbers/" + fileName + ".png")));
		} catch (Exception e) {
			smallWhiteNumber = SpriteManager.getSprite("missing");
			smallDarkNumber = SpriteManager.getSprite("missing");
			mediumWhiteNumber = SpriteManager.getSprite("missing");
			mediumDarkNumber = SpriteManager.getSprite("missing");
			System.out.println("Missing font: " + character);
		}

		smallWhiteNumbers.put(character, smallWhiteNumber);
		smallDarkNumbers.put(character, smallDarkNumber);
		mediumWhiteNumbers.put(character, mediumWhiteNumber);
		mediumDarkNumbers.put(character, mediumDarkNumber);
	}

	private static void addWord(String word) {
		BufferedImage newWord;
		try {
			newWord = read(Objects.requireNonNull(Hud.class.getClassLoader()
					.getResource("hud/words/" + word + ".png")));
		} catch (Exception e) {
			newWord = SpriteManager.getSprite("missing");
			System.out.println("Can't load word: " + word);
		}

		hudWords.put(word, newWord);
	}

	private void printNumber(Graphics2D graphics, FontType font, FontDirection fd, String string, int x, int y) {
		CharacterIterator charItr;
		BufferedImage currentImage = null;
		Map<Character, BufferedImage> currentMap = numberMap.get(font);
		charItr = new StringCharacterIterator(string);
		if (fd == FontDirection.LEFT) {
			for (char c = charItr.first(); c != CharacterIterator.DONE; c = charItr.next()) {
				currentImage = currentMap.get(c);
				graphics.drawImage(currentImage, null, x, y);
				x += currentImage.getWidth();
			}
		} else {
			for (char c = charItr.last(); c != CharacterIterator.DONE; c = charItr.previous()) {
				currentImage = currentMap.get(c);
				x -= currentImage.getWidth();
				graphics.drawImage(currentImage, null, x, y);
			}
		}
	}

	public void update(float dt) {
		healthValue = player.getHealth();
		if (healthValue <= 0) {
			currentFace = deadface;
			return;
		}

		if (healthValue > 85)
			healthValue = 0;
		else if (healthValue > 70)
			healthValue = 1;
		else if (healthValue > 50)
			healthValue = 2;
		else if (healthValue > 30)
			healthValue = 3;
		else
			healthValue = 4;

		if (player.onKillStreak()) {
			currentFace = hudFaces.get(healthValue).get(4);
			faceTimer = 1.0f;
		} else if (player.hurt()) {
			currentFace = hudFaces.get(healthValue).get(3);
			faceTimer = 1.0f;
		} else {
			faceTimer -= dt;
			if (faceTimer <= 0) {
				faceSelection = Constants.rand(0, 2);
				faceTimer = Constants.rand(0.5f, 0.75f);
			}
			currentFace = hudFaces.get(healthValue).get(faceSelection);
		}
	}

	public void render(Graphics2D graphics, int x, int y, int rx) {
		/* Left Side - Health, Armor, Face */
		BufferedImage currentImage;
		graphics.setColor(hudBGColor);
		graphics.fillRect(x - 1, y - 27, 219, 29);
		int tempX = x;
		int tempY = y;
		int sameDistanceX;

		currentImage = hudWords.get("Armor");
		tempY -= currentImage.getHeight();
		graphics.drawImage(currentImage, tempX, tempY, null);
		tempX += 79;
		graphics.setColor(Color.BLACK);
		graphics.fillRect(tempX, tempY, 104, 12);
		graphics.setColor(Color.BLUE);
		if (player.getArmor() >= 100) {
			graphics.fillRect(tempX + 2, tempY + 2, 100, 8);
			graphics.setColor(Color.GREEN);
			graphics.fillRect(tempX + 2, tempY + 2, player.getArmor() - 100, 8);
		} else {
			graphics.fillRect(tempX + 2, tempY + 2, player.getArmor(), 8);
		}
		tempX += 138;
		printNumber(graphics, FontType.MEDIUM_WHITE, FontDirection.RIGHT, player.getArmor() + "", tempX, tempY);
		tempX = x;
		currentImage = hudWords.get("Health");
		tempY -= currentImage.getHeight() + 1;
		graphics.drawImage(currentImage, null, tempX, tempY);
		tempX += 79;
		graphics.setColor(Color.BLACK);
		graphics.fillRect(tempX, tempY, 104, 12);
		graphics.setColor(Color.RED);
		if (player.getHealth() >= 100) {
			graphics.fillRect(tempX + 2, tempY + 2, 100, 8);
			graphics.setColor(Color.YELLOW);
			graphics.fillRect(tempX + 2, tempY + 2, player.getHealth() - 100, 8);
		} else {
			graphics.fillRect(tempX + 2, tempY + 2, player.getHealth(), 8);
		}
		tempX += 138;
		printNumber(graphics, FontType.MEDIUM_WHITE, FontDirection.RIGHT, player.getHealth() + "", tempX, tempY);

		tempX = x;
		tempY -= 3;
		graphics.setColor(hudBGColor);
		graphics.fillRect(tempX - 1, tempY - 42, 35, 42);
		tempX += 4;
		tempY -= 37;
		graphics.drawImage(currentFace, null, tempX, tempY);


		/* Right Side, Weapons, Ammo */

		Weapon.AmmoType currentAmmoType = player.getCurrentWeapon().getAmmoType();
		graphics.setColor(hudBGColor);
		graphics.fillRect(rx - 146, y - 53, 146, 55);
		graphics.fillRect(rx - 146, y - 68, 146, 14);
		graphics.fillRect(rx - 191, y - 27, 44, 29);
		graphics.fillRect(rx - 240, y - 18, 47, 20);
		tempY = y - 12;
		tempX = rx - 1;

		if (currentAmmoType == Weapon.AmmoType.ROCKET) {
			printNumber(graphics, FontType.MEDIUM_WHITE, FontDirection.RIGHT,
					player.getHeldAmmo(Weapon.AmmoType.ROCKET) + "", tempX, tempY);
		} else {
			printNumber(graphics, FontType.MEDIUM_DARK, FontDirection.RIGHT,
					player.getHeldAmmo(Weapon.AmmoType.ROCKET) + "", tempX, tempY);
		}
		tempX -= 85;
		graphics.setColor(Color.BLACK);
		graphics.fillRect(tempX, tempY, 50, 12);
		graphics.setColor(Color.GREEN);
		graphics.fillRect(tempX + 1, tempY + 2,
				(int)(player.getHeldAmmo(Weapon.AmmoType.ROCKET) / (float) (player.getMaxAmmo(Weapon.AmmoType.ROCKET)) * 50),
				8);
		tempX -= 59;
		if (currentAmmoType == Weapon.AmmoType.ROCKET)
			graphics.drawImage(hudWords.get("Rock"), null, tempX, tempY);
		else
			graphics.drawImage(hudWords.get("Rockd"), null, tempX, tempY);

		tempY -= 13;
		tempX = rx - 1;
		if (currentAmmoType == Weapon.AmmoType.ENERGY) {
			printNumber(graphics, FontType.MEDIUM_WHITE, FontDirection.RIGHT,
					player.getHeldAmmo(Weapon.AmmoType.ENERGY) + "", tempX, tempY);
		} else {
			printNumber(graphics, FontType.MEDIUM_DARK, FontDirection.RIGHT,
					player.getHeldAmmo(Weapon.AmmoType.ENERGY) + "", tempX, tempY);
		}
		tempX -= 85;
		graphics.setColor(Color.BLACK);
		graphics.fillRect(tempX, tempY, 50, 12);
		graphics.setColor(Color.GREEN);
		graphics.fillRect(tempX + 1, tempY + 2,
				(int)(player.getHeldAmmo(Weapon.AmmoType.ENERGY) / (float) (player.getMaxAmmo(Weapon.AmmoType.ENERGY)) * 50),
				8);
		tempX -= 59;
		if (currentAmmoType == Weapon.AmmoType.ENERGY)
			graphics.drawImage(hudWords.get("Cell"), null, tempX, tempY);
		else
			graphics.drawImage(hudWords.get("Celld"), null, tempX, tempY);

		tempY -= 13;
		tempX = rx - 1;
		if (currentAmmoType == Weapon.AmmoType.SHOTGUN) {
			printNumber(graphics, FontType.MEDIUM_WHITE, FontDirection.RIGHT,
					player.getHeldAmmo(Weapon.AmmoType.SHOTGUN) + "", tempX, tempY);
		} else {
			printNumber(graphics, FontType.MEDIUM_DARK, FontDirection.RIGHT,
					player.getHeldAmmo(Weapon.AmmoType.SHOTGUN) + "", tempX, tempY);
		}
		tempX -= 85;
		graphics.setColor(Color.BLACK);
		graphics.fillRect(tempX, tempY, 50, 12);
		graphics.setColor(Color.GREEN);
		graphics.fillRect(tempX + 1, tempY + 2,
				(int)(player.getHeldAmmo(Weapon.AmmoType.SHOTGUN) / (float) (player.getMaxAmmo(Weapon.AmmoType.SHOTGUN)) * 50),
				8);
		tempX -= 59;
		if (currentAmmoType == Weapon.AmmoType.SHOTGUN)
			graphics.drawImage(hudWords.get("Shel"), null, tempX, tempY);
		else
			graphics.drawImage(hudWords.get("Sheld"), null, tempX, tempY);

		tempY -= 13;
		tempX = rx - 1;
		if (currentAmmoType == Weapon.AmmoType.BULLET) {
			printNumber(graphics, FontType.MEDIUM_WHITE, FontDirection.RIGHT,
					player.getHeldAmmo(Weapon.AmmoType.BULLET) + "", tempX, tempY);
		} else {
			printNumber(graphics, FontType.MEDIUM_DARK, FontDirection.RIGHT,
					player.getHeldAmmo(Weapon.AmmoType.BULLET) + "", tempX, tempY);
		}
		tempX -= 85;
		graphics.setColor(Color.BLACK);
		graphics.fillRect(tempX, tempY, 50, 12);
		graphics.setColor(Color.GREEN);
		graphics.fillRect(tempX + 1, tempY + 2,
				(int)(player.getHeldAmmo(Weapon.AmmoType.BULLET) / (float) (player.getMaxAmmo(Weapon.AmmoType.BULLET)) * 50),
				8);
		tempX -= 59;
		if (currentAmmoType == Weapon.AmmoType.BULLET)
			graphics.drawImage(hudWords.get("Bull"), null, tempX, tempY);
		else
			graphics.drawImage(hudWords.get("Bulld"), null, tempX, tempY);

		tempX = rx - 1;
		tempY = y - 67;
		if (player.hasWeapon(Weapon.WeaponType.PLASMAGUN))
			printNumber(graphics, FontType.MEDIUM_WHITE, FontDirection.RIGHT, "6", tempX, tempY);
		else
			printNumber(graphics, FontType.MEDIUM_DARK, FontDirection.RIGHT, "6", tempX, tempY);
		tempX -= 15;
		if (player.hasWeapon(Weapon.WeaponType.ROCKETLAUNCHER))
			printNumber(graphics, FontType.MEDIUM_WHITE, FontDirection.RIGHT, "5", tempX, tempY);
		else
			printNumber(graphics, FontType.MEDIUM_DARK, FontDirection.RIGHT, "5", tempX, tempY);
		tempX -= 15;
		if (player.hasWeapon(Weapon.WeaponType.MACHINEGUN))
			printNumber(graphics, FontType.MEDIUM_WHITE, FontDirection.RIGHT, "4", tempX, tempY);
		else
			printNumber(graphics, FontType.MEDIUM_DARK, FontDirection.RIGHT, "4", tempX, tempY);
		tempX -= 15;
		if (player.hasWeapon(Weapon.WeaponType.SHOTGUN))
			printNumber(graphics, FontType.MEDIUM_WHITE, FontDirection.RIGHT, "3", tempX, tempY);
		else
			printNumber(graphics, FontType.MEDIUM_DARK, FontDirection.RIGHT, "3", tempX, tempY);
		tempX -= 15;
		if (player.hasWeapon(Weapon.WeaponType.GUN))
			printNumber(graphics, FontType.MEDIUM_WHITE, FontDirection.RIGHT, "2", tempX, tempY);
		else
			printNumber(graphics, FontType.MEDIUM_DARK, FontDirection.RIGHT, "2", tempX, tempY);
		tempX -= 15;
		if (player.hasWeapon(Weapon.WeaponType.MELEE))
			printNumber(graphics, FontType.MEDIUM_WHITE, FontDirection.RIGHT, "1", tempX, tempY);
		else
			printNumber(graphics, FontType.MEDIUM_DARK, FontDirection.RIGHT, "1", tempX, tempY);
		tempX = rx - 145;
		graphics.drawImage(hudWords.get("Arms"), null, tempX, tempY);

		tempX = rx - 190;
		tempY = y - 12;
		graphics.drawImage(hudWords.get("Clip"), null, tempX, tempY);
		tempX += 37;
		tempY -= 13;
		printNumber(graphics, FontType.MEDIUM_WHITE, FontDirection.RIGHT,
				player.getCurrentClip() + "", tempX, tempY);

		BufferedImage currentWeapon = miniWeapons.get(player.getCurrentWeapon().getWeaponType());
		graphics.drawImage(currentWeapon, null, rx - 239 - (currentWeapon.getWidth() - 45) / 2,
				y - 17 - (currentWeapon.getHeight() - 16) / 2);
	}
}
