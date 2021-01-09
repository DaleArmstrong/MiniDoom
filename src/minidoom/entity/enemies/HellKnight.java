package minidoom.entity.enemies;

import minidoom.entity.Enemy;
import minidoom.entity.Player;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;
import minidoom.game.weapons.ppackages.MeleeProjectilePackage;
import minidoom.game.weapons.ppackages.PlasmaProjectilePackage;

public class HellKnight extends Enemy {
	public HellKnight(GameEngine engine, int entityID, Team team, float x, float y, float rotation) {
		super(engine, entityID, "HellKnight", new Sprite(), team,
				200,
				0,
				250,
				AnimationLoader.getActorAnimationSet("HellKnight"),
				SoundManager.getSound("HellKnightDeath"),
				SoundManager.getSound("HellKnightHurt"),
				SoundManager.getSound("HellKnightAlert"),
				null,
				SoundManager.getSound("HellKnightRanged"),
				0.1f,
				0.5f,
				1,
				0.8f,
				125,
				600,
				new PlasmaProjectilePackage(),
				new MeleeProjectilePackage());

		sprite.init("HellKnight", x, y, 45, 0, rotation, false);
		this.meleePackage.setDamageMin(15);
		this.meleePackage.setDamageMax(20);
		this.meleePackage.setWidth(200);
		this.meleePackage.setHeight(200);
		this.meleePackage.setDeathSound(SoundManager.getSound("HellKnightMelee"));
		this.meleePackage.setRipAndTear(true);
		this.rangedPackage.setDamageMin(15);
		this.rangedPackage.setDamageMax(20);
		this.rangedPackage.setProjectileSpeed(800);
		this.rangedPackage.setProjectileRotates(true);
		this.rangedPackage.setDeathSound(SoundManager.getSound("ImpFireballDeath"));
		this.rangedPackage.setLivingAnimation(AnimationLoader.getAnimationSet("HellKnightFireball"));
		this.rangedPackage.setDeathAnimation(AnimationLoader.getAnimationSet("HellKnightFireballExplosion"));
	}
}
