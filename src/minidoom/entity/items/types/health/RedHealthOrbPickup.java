package minidoom.entity.items.types.health;

import minidoom.entity.components.Sprite;
import minidoom.entity.items.HealthPickup;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;

public class RedHealthOrbPickup extends HealthPickup {
	private static String name = "RedHealthOrb";

	public RedHealthOrbPickup(GameEngine engine, int entityID, float x, float y) {
		super(engine, entityID, new Sprite(name, x, y), true, 100,
				200, AnimationLoader.getAnimationSet(name));

		sound = SoundManager.getSound(name);
	}
}
