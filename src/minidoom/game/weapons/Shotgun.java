package minidoom.game.weapons;

import minidoom.game.GameEngine;
import minidoom.game.managers.SoundManager;
import minidoom.game.weapons.ppackages.BulletProjectilePackage;

public class Shotgun extends Weapon {

	public Shotgun(GameEngine gameEngine) {
		super(gameEngine,
				AmmoType.SHOTGUN,
				WeaponType.SHOTGUN,
				0.8f,
				1.0f,
				8,
				1,
				2,
				0.8f,
				8);

		this.primaryPackage = new BulletProjectilePackage();

		this.primaryPackage.setProjectileRange(400);
		this.primaryPackage.setUniformSpread(true);
		this.primaryPackage.setWeaponSpread(3);
		this.primaryPackage.setNumberOfProjectiles(4);

		this.secondaryPackage = new BulletProjectilePackage();

		this.secondaryPackage.setProjectileRange(400);
		this.secondaryPackage.setUniformSpread(true);
		this.secondaryPackage.setWeaponSpread(9);
		this.secondaryPackage.setNumberOfProjectiles(8);

		this.primarySound = SoundManager.getSound("Shotgun");
		this.secondarySound = SoundManager.getSound("Shotgun");
	}
}
