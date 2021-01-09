package minidoom.game.weapons.ppackages;

import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;

public class BulletProjectilePackage extends ProjectilePackage {
	public static String name = "BulletProjectile";

	public BulletProjectilePackage() {
		super(name,
				AnimationLoader.getAnimationSet("BulletParticle"),
				AnimationLoader.getAnimationSet(name),
				1000,
				1,
				false,
				1,
				5,
				15,
				false,
				1000,
				false,
				false,
				0,
				0,
				0,
				SoundManager.getSound(name));
	}
}
