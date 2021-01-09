package minidoom.entity.items.types.armor;

import minidoom.entity.components.Sprite;
import minidoom.entity.items.ArmorPickup;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;

public class BlueArmorPickup extends ArmorPickup {
	private static String name = "BlueArmor";

	public BlueArmorPickup(GameEngine engine, int entityID, float x, float y) {
		super(engine, entityID, new Sprite(name, x, y), true, 150,
				150, 3, AnimationLoader.getAnimationSet(name));

		sound = SoundManager.getSound("Armor");
	}
}
