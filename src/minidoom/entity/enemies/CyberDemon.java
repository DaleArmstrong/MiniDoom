package minidoom.entity.enemies;

import minidoom.entity.Enemy;
import minidoom.entity.Player;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;
import minidoom.game.weapons.ppackages.BulletProjectilePackage;
import minidoom.game.weapons.ppackages.RocketProjectilePackage;

public class CyberDemon extends Enemy {
	public CyberDemon(GameEngine engine, int entityID, Team team, float x, float y, float rotation) {
		super(engine, entityID, "CyberDemon", new Sprite(), team,
				100,
				0,
				1200,
				AnimationLoader.getActorAnimationSet("CyberDemon"),
				SoundManager.getSound("CyberDemonDeath"),
				SoundManager.getSound("HellKnightHurt"),
				SoundManager.getSound("CyberDemonAlert"),
				null,
				SoundManager.getSound("RocketLauncher"),
				0.15f,
				0,
				4,
				2f,
				150,
				600,
				new RocketProjectilePackage(),
				null);

		sprite.init("CyberDemon", x, y, 70, 0, rotation, false);
		this.rangedPackage.setSelfDamageExplosion(false);
		this.effectable = false;
	}
}
