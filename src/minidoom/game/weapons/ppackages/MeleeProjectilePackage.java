package minidoom.game.weapons.ppackages;

import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;

public class MeleeProjectilePackage extends ProjectilePackage {
	public static String name = null;

	public MeleeProjectilePackage() {
		super(name,
				AnimationLoader.getAnimationSet("MeleeParticle"),
				AnimationLoader.getAnimationSet(name),
				10,
				0.0f,
				false,
				1,
				15,
				20,
				false,
				150,
				true,
				false,
				200,
				200,
				0,
				SoundManager.getSound("MeleeHit"));
	}
}
