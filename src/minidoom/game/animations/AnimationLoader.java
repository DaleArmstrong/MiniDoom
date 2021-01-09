package minidoom.game.animations;

import minidoom.entity.Actor;
import minidoom.game.Hud;
import minidoom.game.generators.EntityGenerator;
import minidoom.game.managers.SpriteManager;
import minidoom.game.weapons.Weapon;
import minidoom.util.Constants;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static javax.imageio.ImageIO.read;

/**
 * Loads a txt file that contains a list of animations, their delay, and number of images per animation
 */
public class AnimationLoader {
	private static Map<String, AnimationSet> animationsMap;
	private static Map<String, ActorAnimationSet> actorSets;
	private static Map<Weapon.WeaponType, ActorAnimationSet> playerSets;
	private static Map<String, Map<Actor.ActorDirection, AnimationSet>> actorAnimations;

	static {
		animationsMap = new HashMap<>();
		actorSets = new HashMap<>();
		playerSets = new EnumMap<>(Weapon.WeaponType.class);
		actorAnimations = new HashMap<>();
		BufferedReader reader;
		String line;
		AnimationSet newAnimationSet;
		ActorAnimationSet newActorAnimationSet;

		try {
			reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(AnimationLoader.class
					.getClassLoader().getResourceAsStream("resourcelists/animations.txt"))));

			line = reader.readLine();
			while (line != null) {
				String[] values = line.split(",");
				List<BufferedImage> images = new ArrayList<>(Integer.parseInt(values[2]));
				for (int i = 0; i < Integer.parseInt(values[2]); i++) {
					images.add(readImage("animations/" + values[0] + "/" + values[0] + i));
				}
				newAnimationSet = new AnimationSet(images, Float.parseFloat(values[1]));
				animationsMap.put(values[0], newAnimationSet);
				line = reader.readLine();
			}
		} catch (IOException e) {
			System.out.println("Can't read animations" + e.getMessage());
		}

		try {
			reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(AnimationLoader.class
					.getClassLoader().getResourceAsStream("resourcelists/actoranimations.txt"))));

			line = reader.readLine();
			while (line != null) {
				String[] values = line.split(",");
				int imagesPerDirection = Integer.parseInt(values[3]);
				int totalImages = imagesPerDirection * 8;
				List<BufferedImage> images = null;
				Map<Actor.ActorDirection, AnimationSet> newActorAnimation = new EnumMap<>(Actor.ActorDirection.class);
				int j = 0;
				for (int i = 0; i < totalImages; i++) {
					if (i % imagesPerDirection == 0)
						images = new ArrayList<>(imagesPerDirection);

					images.add(readImage("animations/" + values[0] + values[1] + "/" + values[0] + values[1] + i));

					if (i % imagesPerDirection == (imagesPerDirection - 1)) {
						Actor.ActorDirection currentDirection = null;
						switch (j) {
							case 0: currentDirection = Actor.ActorDirection.LEFT;
								break;
							case 1: currentDirection = Actor.ActorDirection.UPLEFT;
								break;
							case 2: currentDirection = Actor.ActorDirection.UP;
								break;
							case 3: currentDirection = Actor.ActorDirection.UPRIGHT;
								break;
							case 4: currentDirection = Actor.ActorDirection.RIGHT;
								break;
							case 5: currentDirection = Actor.ActorDirection.DOWNRIGHT;
								break;
							case 6: currentDirection = Actor.ActorDirection.DOWN;
								break;
							case 7: currentDirection = Actor.ActorDirection.DOWNLEFT;
								break;
						}
						newAnimationSet = new AnimationSet(images, Float.parseFloat(values[2]));
						newActorAnimation.put(currentDirection, newAnimationSet);
						j++;
					}
				}
				actorAnimations.put((values[0] + values[1]), newActorAnimation);
				line = reader.readLine();
			}
		} catch (IOException e) {
			System.out.println("Can't read actor animations" + e.getMessage());
		}

		try {
			reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(AnimationLoader.class
					.getClassLoader().getResourceAsStream("resourcelists/actoranimationsset.txt"))));

			line = reader.readLine();
			List<BufferedImage> images = null;
			while (line != null) {
				String[] values = line.split(",");
				newActorAnimationSet = new ActorAnimationSet();
				int movingImages = Integer.parseInt(values[3]);
				boolean basicAttack = values[4].equals("true");
				boolean rangedAnimation = values[5].equals("true");
				boolean meleeAnimation = values[6].equals("true");
				newActorAnimationSet.init(movingImages, basicAttack, rangedAnimation, meleeAnimation, Integer.parseInt(values[7]));
				int imagesPerDirection = movingImages + (basicAttack ? 2 : 0) + 1;
				int totalImages = imagesPerDirection * 8;
				int j = 0;
				for (int i = 0; i < totalImages; i++) {
					if (i % imagesPerDirection == 0)
						images = new ArrayList<>(imagesPerDirection);

					images.add(readImage("actoranimations/" + values[2] + "/" + values[2] + i));

					if (i % imagesPerDirection == (imagesPerDirection - 1)) {
						Actor.ActorDirection currentDirection = null;
						switch (j) {
							case 0: currentDirection = Actor.ActorDirection.LEFT;
								break;
							case 1: currentDirection = Actor.ActorDirection.UPLEFT;
								break;
							case 2: currentDirection = Actor.ActorDirection.UP;
								break;
							case 3: currentDirection = Actor.ActorDirection.UPRIGHT;
								break;
							case 4: currentDirection = Actor.ActorDirection.RIGHT;
								break;
							case 5: currentDirection = Actor.ActorDirection.DOWNRIGHT;
								break;
							case 6: currentDirection = Actor.ActorDirection.DOWN;
								break;
							case 7: currentDirection = Actor.ActorDirection.DOWNLEFT;
								break;
						}
						newActorAnimationSet.addAnimationDirection(currentDirection, images);
						j++;
					}
				}
				j = Integer.parseInt(values[8]);
				for (int i = 1; i <= j; i++) {
					newActorAnimationSet.addDeathAnimation(getAnimationSet(values[9] + i));
				}

				if (values[10].equals("null"))
					newActorAnimationSet.setExplosionAnimation(null);
				else
					newActorAnimationSet.setExplosionAnimation(getAnimationSet(values[10]));

				if (!(values[11].equals("null"))) {
					newActorAnimationSet.setDyingAnimation(getAnimationSet(values[11]));
					newActorAnimationSet.setDyingDeadAnimation(getAnimationSet(values[12]));
				} else {
					newActorAnimationSet.setDyingAnimation(null);
					newActorAnimationSet.setDyingDeadAnimation(null);
				}

				if (rangedAnimation)
					newActorAnimationSet.setRangedAttackAnimations(actorAnimations.get(values[2] + "Ranged"));
				if (meleeAnimation)
					newActorAnimationSet.setMeleeAttackAnimations(actorAnimations.get(values[2] + "Melee"));

				if (values[0].equals("true")) {
					playerSets.put(Weapon.WeaponType.valueOf(values[1]), newActorAnimationSet);
				} else {
					actorSets.put(values[2], newActorAnimationSet);
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			System.out.println("Can't read Actor Animations" + e.getMessage());
		}
	}

	private static BufferedImage readImage(String fileName) {
		BufferedImage newImage = null;
		try {
			newImage = read(Objects.requireNonNull(AnimationLoader.class.getClassLoader()
					.getResource(fileName + ".png")));
		} catch (Exception e) {
			System.out.println("Missing animation image: " + fileName);
		}
		return newImage;
	}

	public static AnimationSet getAnimationSet(String fileName) {
		if (fileName == null)
			return null;

		return animationsMap.getOrDefault(fileName, null);
	}

	public static ActorAnimationSet getActorAnimationSet(String fileName) {
		if (fileName == null)
			return null;

		return actorSets.getOrDefault(fileName, null);
	}

	public static ActorAnimationSet getPlayerAnimationSet(Weapon.WeaponType type) {
		return playerSets.getOrDefault(type, null);
	}

	public static boolean hasAnimationSet(String fileName) {
		if (fileName == null) {
			return false;
		}

		return animationsMap.containsKey(fileName);
	}

	public static BufferedImage getRandomBloodSplatter() {
		return animationsMap.get("BloodSplatter").get(Constants.rand(0, 17));
	}
}
