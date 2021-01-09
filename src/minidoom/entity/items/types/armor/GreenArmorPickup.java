package minidoom.entity.items.types.armor;

import minidoom.entity.components.Sprite;
import minidoom.entity.items.ArmorPickup;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;

public class GreenArmorPickup extends ArmorPickup {
	private static String name = "GreenArmor";

	public GreenArmorPickup(GameEngine engine, int entityID, float x, float y) {
		super(engine, entityID, new Sprite(name, x, y), true, 100,
				100, 3, AnimationLoader.getAnimationSet(name));

		sound = SoundManager.getSound("Armor");
	}
}
