package minidoom.entity.items.types.ammo;

import minidoom.entity.components.Sprite;
import minidoom.entity.items.AmmoPickup;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;
import minidoom.game.weapons.Weapon;

public class RocketSinglePickup extends AmmoPickup {
	private static String name = "RocketSingle";

	public RocketSinglePickup(GameEngine engine, int entityID, float x, float y) {
		super(engine, entityID, new Sprite(name, x, y), true, Weapon.AmmoType.ROCKET,
				2, AnimationLoader.getAnimationSet(name));

		sound = SoundManager.getSound("RocketPack");
	}
}
