package minidoom.entity.enemies;

import minidoom.entity.Enemy;
import minidoom.entity.Player;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;
import minidoom.game.weapons.ppackages.BulletProjectilePackage;
import minidoom.game.weapons.ppackages.PlasmaProjectilePackage;

public class Mancubus extends Enemy {
	public Mancubus(GameEngine engine, int entityID, Team team, float x, float y, float rotation) {
		super(engine, entityID, "Mancubus", new Sprite(), team,
				60,
				0,
				250,
				AnimationLoader.getActorAnimationSet("Mancubus"),
				SoundManager.getSound("MancubusDeath"),
				SoundManager.getSound("MancubusHurt"),
				SoundManager.getSound("MancubusAlert"),
				null,
				SoundManager.getSound("MancubusAttack"),
				0.3f,
				0,
				7,
				3.5f,
				150,
				600,
				new PlasmaProjectilePackage(),
				null);

		sprite.init("Mancubus", x, y, 75, 0, rotation, false);
		this.rangedPackage.setDamageMin(10);
		this.rangedPackage.setDamageMax(15);
		this.rangedPackage.setLivingAnimation(AnimationLoader.getAnimationSet("MancubusFireball"));
		this.rangedPackage.setDeathAnimation(AnimationLoader.getAnimationSet("MancubusFireballExplosion"));
		this.rangedPackage.setProjectileRotates(true);
		this.effectable = false;
	}
}
