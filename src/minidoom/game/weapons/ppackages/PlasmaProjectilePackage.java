package minidoom.game.weapons.ppackages;

import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;

public class PlasmaProjectilePackage extends ProjectilePackage {
	public static String name = "PlasmaProjectile";

	public PlasmaProjectilePackage() {
		super(name,
				AnimationLoader.getAnimationSet("PlasmaParticle"),
				AnimationLoader.getAnimationSet(name),
				1000,
				2,
				false,
				1,
				5,
				30,
				false,
				500,
				false,
				false,
				0,
				0,
				0,
				SoundManager.getSound(name));
	}
}
