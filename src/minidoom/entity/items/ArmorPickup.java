package minidoom.entity.items;

import minidoom.entity.Player;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationSet;

/**
 * Template for all armor pickup items
 */
public class ArmorPickup extends Item {
	private int value;
	private int maxArmorValue;
	private float protectionLevel;

	public ArmorPickup(GameEngine engine, int entityID, Sprite sprite, boolean visible,
	                   int value, int maxArmorValue, int protectionLevel, AnimationSet as) {
		super(engine, entityID, sprite, visible, as);
		this.value = value;
		this.maxArmorValue = maxArmorValue;
		this.protectionLevel = 1.0f / protectionLevel;
	}

	@Override
	public void onPickup(Player player) {
		if (player.hasMaxArmor(maxArmorValue))
			return;
		player.addArmor(value, maxArmorValue, protectionLevel);
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
