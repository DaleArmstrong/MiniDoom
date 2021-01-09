package minidoom.entity.enemies;

import minidoom.entity.Enemy;
import minidoom.entity.Player;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;
import minidoom.game.weapons.ppackages.MeleeProjectilePackage;
import minidoom.game.weapons.ppackages.PlasmaProjectilePackage;

public class Baron extends Enemy {
	public Baron(GameEngine engine, int entityID, Team team, float x, float y, float rotation) {
		super(engine, entityID, "Baron", new Sprite(), team,
				200,
				0,
				800,
				AnimationLoader.getActorAnimationSet("Baron"),
				SoundManager.getSound("BaronDeath"),
				SoundManager.getSound("HellKnightHurt"),
				SoundManager.getSound("BaronAlert"),
				null,
				SoundManager.getSound("HellKnightRanged"),
				0.1f,
				0.4f,
				1,
				0.6f,
				125,
				600,
				new PlasmaProjectilePackage(),
				new MeleeProjectilePackage());

		sprite.init("Baron", x, y, 45, 0, rotation, false);
		this.meleePackage.setDamageMin(20);
		this.meleePackage.setDamageMax(25);
		this.meleePackage.setWidth(200);
		this.meleePackage.setHeight(200);
		this.meleePackage.setDeathSound(SoundManager.getSound("BaronMelee"));
		this.meleePackage.setRipAndTear(true);
		this.rangedPackage.setDamageMin(25);
		this.rangedPackage.setDamageMax(35);
		this.rangedPackage.setProjectileSpeed(800);
		this.rangedPackage.setProjectileRotates(true);
		this.rangedPackage.setDeathSound(SoundManager.getSound("ImpFireballDeath"));
		this.rangedPackage.setLivingAnimation(AnimationLoader.getAnimationSet("HellKnightFireball"));
		this.rangedPackage.setDeathAnimation(AnimationLoader.getAnimationSet("HellKnightFireballExplosion"));
	}
}
