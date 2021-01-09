package minidoom.game.weapons;

import minidoom.game.GameEngine;
import minidoom.game.managers.SoundManager;
import minidoom.game.weapons.ppackages.RocketProjectilePackage;

public class RocketLauncher extends Weapon {

	public RocketLauncher(GameEngine gameEngine) {
		super(gameEngine,
				AmmoType.ROCKET,
				WeaponType.ROCKETLAUNCHER,
				0.6f,
				0.6f, 1,
				1,
				1,
				0.5f,
				1);

		this.primaryPackage = new RocketProjectilePackage();

		this.secondaryPackage = new RocketProjectilePackage();

		this.primarySound = SoundManager.getSound("RocketLauncher");
		this.secondarySound = SoundManager.getSound("RocketLauncher");
	}

}
