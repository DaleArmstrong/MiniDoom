package minidoom.entity.items.types.health;

import minidoom.entity.components.Sprite;
import minidoom.entity.items.HealthPickup;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;

public class MedipackPickup extends HealthPickup {
	private static String name = "Medipack";

	public MedipackPickup(GameEngine engine, int entityID, float x, float y) {
		super(engine, entityID, new Sprite(name, x, y), true, 25,
				100, AnimationLoader.getAnimationSet(name));

		sound = SoundManager.getSound(name);
	}
}
