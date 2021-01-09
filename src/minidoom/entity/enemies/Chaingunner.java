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

public class Chaingunner extends Enemy {
	public Chaingunner(GameEngine engine, int entityID, Team team, float x, float y, float rotation) {
		super(engine, entityID, "Chaingunner", new Sprite(), team,
				100,
				0,
				80,
				AnimationLoader.getActorAnimationSet("Chaingunner"),
				SoundManager.getSound("ChaingunnerDeath"),
				SoundManager.getSound("ChaingunnerHurt"),
				SoundManager.getSound("ChaingunnerAlert"),
				null,
				SoundManager.getSound("MachineGun"),
				0.1f,
				0,
				10,
				3.5f,
				200,
				600,
				new BulletProjectilePackage(),
				null);

		sprite.init("Chaingunner", x, y, 40, 0, rotation, false);
		this.rangedPackage.setDamageMin(3);
		this.rangedPackage.setDamageMax(6);
	}
}
