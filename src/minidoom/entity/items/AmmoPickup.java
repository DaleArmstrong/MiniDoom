package minidoom.entity.items;

import minidoom.entity.Player;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationSet;
import minidoom.game.weapons.Weapon;

/**
 * Template for all ammo pickup items
 */
public class AmmoPickup extends Item {
	private Weapon.AmmoType ammoType;
	private int value;

	public AmmoPickup(GameEngine engine, int entityID, Sprite sprite, boolean visible,
	                  Weapon.AmmoType ammoType, int value, AnimationSet as) {
		super(engine, entityID, sprite, visible, as);
		this.ammoType = ammoType;
		this.value = value;
	}

	@Override
	public void onPickup(Player player) {
		if (player.hasMaxAmmo(ammoType))
			return;
		player.addAmmo(ammoType, value);
		kill();
	}

	@Override
	public void update(float dt) {
		if (animation.hasAnimation()) {
			animation.update(dt);
			sprite.setImage(animation.getCurrentImage());
		}
	}
}
