package minidoom.entity.items.types.weapons;

import minidoom.entity.components.Sprite;
import minidoom.entity.items.WeaponPickup;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;
import minidoom.game.weapons.Weapon;

public class PlasmaGunPickup extends WeaponPickup {
	private static String name = "PlasmaGun";

	public PlasmaGunPickup(GameEngine engine, int entityID, float x, float y) {
		super(engine, entityID, new Sprite(name, x, y), true,
				Weapon.WeaponType.PLASMAGUN, Weapon.AmmoType.ENERGY,
				40, AnimationLoader.getAnimationSet(name));

		sound = SoundManager.getSound("WeaponPickup");
	}
}
