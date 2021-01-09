package minidoom.game.weapons;

import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;
import minidoom.game.weapons.ppackages.PlasmaProjectilePackage;

public class PlasmaGun extends Weapon {
	public PlasmaGun(GameEngine gameEngine) {
		super(gameEngine,
				AmmoType.ENERGY,
				WeaponType.PLASMAGUN,
				0.1f,
				0.6f,
				60,
				1,
				20,
				1.5f,
				60);

		this.primaryPackage = new PlasmaProjectilePackage();

		this.secondaryPackage = new PlasmaProjectilePackage();
		this.secondaryPackage.setLivingAnimation(AnimationLoader.getAnimationSet("LargePlasmaProjectile"));
		this.secondaryPackage.setDamageMin(50);
		this.secondaryPackage.setDamageMax(200);
		this.secondaryPackage.setSelfDamageExplosion(false);
		this.secondaryPackage.setExplosive(true);
		this.secondaryPackage.setExplosiveRange(150);

		this.primarySound = SoundManager.getSound("PlasmaGun");
		this.secondarySound = SoundManager.getSound("LargePlasmaProjectile");
	}
}
