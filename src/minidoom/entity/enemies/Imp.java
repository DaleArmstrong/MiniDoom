package minidoom.entity.enemies;

import minidoom.entity.Enemy;
import minidoom.entity.Player;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;
import minidoom.game.weapons.ppackages.BulletProjectilePackage;
import minidoom.game.weapons.ppackages.MeleeProjectilePackage;
import minidoom.game.weapons.ppackages.PlasmaProjectilePackage;

public class Imp extends Enemy {
	public Imp(GameEngine engine, int entityID, Team team, float x, float y, float rotation) {
		super(engine, entityID, "Imp", new Sprite(), team,
				200,
				0,
				50,
				AnimationLoader.getActorAnimationSet("Imp"),
				SoundManager.getSound("ImpDeath"),
				SoundManager.getSound("ImpHurt"),
				SoundManager.getSound("ImpAlert"),
				null,
				SoundManager.getSound("ImpRanged"),
				0.1f,
				0.7f,
				1,
				1.0f,
				100,
				700,
				new PlasmaProjectilePackage(),
				new MeleeProjectilePackage());
		sprite.init("Imp", x, y, 35, 0, rotation, false);
		this.meleePackage.setDamageMin(5);
		this.meleePackage.setDamageMax(15);
		this.meleePackage.setDeathSound(SoundManager.getSound("ImpMelee"));
		this.meleePackage.setRipAndTear(true);
		this.rangedPackage.setDamageMin(10);
		this.rangedPackage.setDamageMax(20);
		this.rangedPackage.setDeathSound(SoundManager.getSound("ImpFireballDeath"));
		this.rangedPackage.setLivingAnimation(AnimationLoader.getAnimationSet("ImpFireball"));
		this.rangedPackage.setDeathAnimation(AnimationLoader.getAnimationSet("ImpFireballExplosion"));
	}
}
