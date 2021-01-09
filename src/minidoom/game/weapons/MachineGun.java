package minidoom.game.weapons;

import minidoom.game.GameEngine;
import minidoom.game.managers.SoundManager;
import minidoom.game.weapons.ppackages.BulletProjectilePackage;

public class MachineGun extends Weapon {

	public MachineGun(GameEngine gameEngine) {
		super(gameEngine,
				AmmoType.BULLET,
				WeaponType.MACHINEGUN,
				0.114f,
				0.09f,
				100,
				1,
				1,
				1.5f,
				50);

		this.primaryPackage = new BulletProjectilePackage();

		this.secondaryPackage = new BulletProjectilePackage();
		this.secondaryPackage.setUniformSpread(false);
		this.secondaryPackage.setWeaponSpread(10);

		this.primarySound = SoundManager.getSound("MachineGun");
		this.secondarySound = SoundManager.getSound("MachineGun");
	}
}
