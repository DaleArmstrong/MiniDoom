package minidoom.game.weapons;

import minidoom.entity.PhysicalEntity;
import minidoom.game.GameEngine;
import minidoom.game.managers.SoundManager;
import minidoom.game.weapons.ppackages.BulletProjectilePackage;

public class Gun extends Weapon {
	public Gun(GameEngine gameEngine) {
		super(gameEngine,
				AmmoType.BULLET,
				WeaponType.GUN,
				0.30f,
				0.055f,
				15,
				1,
				1,
				1.2f,
				15);

		this.primaryPackage = new BulletProjectilePackage();

		this.secondaryPackage = new BulletProjectilePackage();

		this.secondaryPackage.setUniformSpread(false);
		this.secondaryPackage.setWeaponSpread(15);

		this.primarySound = SoundManager.getSound("Gun");
		this.secondarySound = SoundManager.getSound("Gun");
	}
}
