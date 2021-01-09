package minidoom.game.weapons.ppackages;

import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;

public class RocketProjectilePackage extends ProjectilePackage {
	public static String name = "RocketProjectile";

	public RocketProjectilePackage() {
		super(name,
				AnimationLoader.getAnimationSet("RocketExplosion"),
				AnimationLoader.getAnimationSet(name),
				1000,
				0.5f,
				false,
				1,
				20,
				80,
				true,
				500,
				true,
				false,
				0,
				0,
				80,
				SoundManager.getSound(name));
	}
}
