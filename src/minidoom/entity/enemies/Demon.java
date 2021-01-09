package minidoom.entity.enemies;

import minidoom.entity.Enemy;
import minidoom.entity.Player;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;
import minidoom.game.weapons.ppackages.MeleeProjectilePackage;
import minidoom.game.weapons.ppackages.PlasmaProjectilePackage;

public class Demon extends Enemy {
	public Demon(GameEngine engine, int entityID, Team team, float x, float y, float rotation) {
		super(engine, entityID, "Demon", new Sprite(), team,
				200,
				0,
				100,
				AnimationLoader.getActorAnimationSet("Demon"),
				SoundManager.getSound("DemonDeath"),
				SoundManager.getSound("HellKnightHurt"),
				SoundManager.getSound("DemonAlert"),
				SoundManager.getSound("DemonAttack"),
				null,
				0.1f,
				0.7f,
				1,
				1.0f,
				125,
				700,
				null,
				new MeleeProjectilePackage());

		sprite.init("Demon", x, y, 38, 0, rotation, false);
		this.meleePackage.setDamageMin(15);
		this.meleePackage.setDamageMax(20);
		this.meleePackage.setWidth(160);
		this.meleePackage.setHeight(160);
		this.meleePackage.setDeathSound(SoundManager.getSound("ImpMelee"));
		this.meleePackage.setRipAndTear(true);
	}
}
