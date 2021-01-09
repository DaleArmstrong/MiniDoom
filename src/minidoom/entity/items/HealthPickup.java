package minidoom.entity.items;

import minidoom.entity.Player;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationSet;

/**
 * Template for all health pickup items
 */
public class HealthPickup extends Item {
	private int value;
	private int maxHealthValue;

	public HealthPickup(GameEngine engine, int entityID, Sprite sprite, boolean visible,
	                    int value, int maxHealthValue, AnimationSet as) {
		super(engine, entityID, sprite, visible, as);
		this.value = value;
		this.maxHealthValue = maxHealthValue;
	}

	@Override
	public void onPickup(Player player) {
		if (player.hasMaxHealth(maxHealthValue))
			return;
		player.addHealth(value, maxHealthValue);
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
