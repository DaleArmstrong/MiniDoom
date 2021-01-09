package minidoom.entity.items;

import minidoom.entity.Player;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationSet;
import minidoom.game.weapons.Weapon;

/**
 * Template for all weapon pickup items
 */
public class WeaponPickup extends Item {
	private Weapon.WeaponType weaponType;
	private Weapon.AmmoType ammoType;
	private int value;

	public WeaponPickup(GameEngine engine, int entityID, Sprite sprite, boolean visible, Weapon.WeaponType weaponType,
	                    Weapon.AmmoType ammoType, int value, AnimationSet as) {
		super(engine, entityID, sprite, visible, as);
		this.weaponType = weaponType;
		this.ammoType = ammoType;
		this.value = value;
	}

	@Override
	public void onPickup(Player player) {
		if (player.hasWeapon(weaponType)) {
			if (!player.hasMaxAmmo(ammoType)) {
				player.addAmmo(ammoType, value);
				kill();
			}
		} else {
			Weapon newWeapon = engine.generateWeapon(weaponType);
			if (newWeapon != null) {
				player.addWeapon(weaponType, newWeapon);
				player.addAmmo(ammoType, value - newWeapon.getCurrentClip());
			} else {
				System.out.println("Could not generate weapon " + weaponType);
			}
			kill();
		}

	}

	@Override
	public void update(float dt) {
		if (animation.hasAnimation()) {
			animation.update(dt);
			sprite.setImage(animation.getCurrentImage());
		}
	}
}
