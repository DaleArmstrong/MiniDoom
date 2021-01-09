package minidoom.entity.enemies;

import kuusisto.tinysound.Sound;
import minidoom.entity.Enemy;
import minidoom.entity.Player;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.game.animations.ActorAnimationSet;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;
import minidoom.game.weapons.ppackages.BulletProjectilePackage;
import minidoom.game.weapons.ppackages.ProjectilePackage;

public class Shotgunguy extends Enemy {
	public Shotgunguy(GameEngine engine, int entityID, Team team, float x, float y, float rotation) {
		super(engine, entityID, "Shotgunguy", new Sprite(), team,
				150,
				0,
				50,
				AnimationLoader.getActorAnimationSet("Shotgunguy"),
				SoundManager.getSound("ShotgunguyDeath"),
				SoundManager.getSound("ShotgunguyHurt"),
				SoundManager.getSound("ShotgunguyAlert"),
				null,
				SoundManager.getSound("Shotgun"),
				0.1f,
				0,
				1,
				1.5f,
				150,
				500,
				new BulletProjectilePackage(),
				null);

		sprite.init("Shotgunguy", x, y, 32, 0, rotation, false);

		this.rangedPackage.setProjectileRange(400);
		this.rangedPackage.setUniformSpread(true);
		this.rangedPackage.setWeaponSpread(3);
		this.rangedPackage.setNumberOfProjectiles(4);
		this.rangedPackage.setDamageMin(3);
		this.rangedPackage.setDamageMax(6);
	}
}
