package minidoom.game.weapons;

import minidoom.entity.Actor;
import minidoom.entity.PhysicalEntity;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.animations.AnimationSet;
import minidoom.game.managers.SoundManager;
import minidoom.game.weapons.ppackages.MeleeProjectilePackage;

public class Melee extends Weapon {

	public Melee(GameEngine gameEngine) {
		super(gameEngine,
				AmmoType.MELEE,
				WeaponType.MELEE,
				0.35f,
				0.35f,
				999,
				0,
				0,
				0,
				999);

		this.primaryPackage = new MeleeProjectilePackage();
		this.primaryPackage.setDamageMin(15);
		this.primaryPackage.setDamageMax(25);

		this.secondaryPackage = new MeleeProjectilePackage();
		this.secondaryPackage.setRipAndTear(true);
		this.secondaryPackage.setDamageMin(15);
		this.secondaryPackage.setDamageMax(25);

		this.primarySound = SoundManager.getSound("MeleeAttack");
		this.secondarySound = SoundManager.getSound("MeleeAttack");
	}
}
