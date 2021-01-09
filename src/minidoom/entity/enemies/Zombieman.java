package minidoom.entity.enemies;

import minidoom.entity.Enemy;
import minidoom.entity.Player;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;
import minidoom.game.weapons.ppackages.BulletProjectilePackage;


public class Zombieman extends Enemy {
	public Zombieman(GameEngine engine, int entityID, Team team, float x, float y, float rotation) {
		super(engine, entityID,"Zombieman", new Sprite(), team,
				150,
				0,
				50,
				AnimationLoader.getActorAnimationSet("Zombieman"),
				SoundManager.getSound("ZombiemanDeath"),
				SoundManager.getSound("ZombiemanHurt"),
				SoundManager.getSound("ZombiemanAlert"),
				null,
				SoundManager.getSound("Gun"),
				0.1f,
				0,
				3,
				1.5f,
				150,
				600,
				new BulletProjectilePackage(),
				null);

		sprite.init("Zombieman", x, y, 32, 0, rotation, false);
		this.rangedPackage.setDamageMin(3);
		this.rangedPackage.setDamageMax(6);

	}
}
