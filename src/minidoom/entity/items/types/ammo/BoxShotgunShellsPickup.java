package minidoom.entity.items.types.ammo;

import minidoom.entity.components.Sprite;
import minidoom.entity.items.AmmoPickup;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;
import minidoom.game.weapons.Weapon;

public class BoxShotgunShellsPickup extends AmmoPickup {
	private static String name = "BoxShotgunShells";

	public BoxShotgunShellsPickup(GameEngine engine, int entityID, float x, float y) {
		super(engine, entityID, new Sprite(name, x, y), true, Weapon.AmmoType.SHOTGUN,
				40, AnimationLoader.getAnimationSet(name));

		sound = SoundManager.getSound(name);
	}
}
