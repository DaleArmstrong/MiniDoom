package minidoom.entity.items.types.weapons;

import minidoom.entity.components.Sprite;
import minidoom.entity.items.WeaponPickup;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;
import minidoom.game.weapons.Weapon;

public class MachineGunPickup extends WeaponPickup {
	private static String name = "MachineGun";

	public MachineGunPickup(GameEngine engine, int entityID, float x, float y) {
		super(engine, entityID, new Sprite(name, x, y), true,
				Weapon.WeaponType.MACHINEGUN, Weapon.AmmoType.BULLET,
				50, AnimationLoader.getAnimationSet(name));

		sound = SoundManager.getSound("WeaponPickup");
	}
}
