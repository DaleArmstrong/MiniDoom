package minidoom.entity.enemies;

import minidoom.entity.Enemy;
import minidoom.entity.Player;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;
import minidoom.game.weapons.ppackages.BulletProjectilePackage;
import minidoom.game.weapons.ppackages.PlasmaProjectilePackage;

public class Arachnotron extends Enemy {
	public Arachnotron(GameEngine engine, int entityID, Team team, float x, float y, float rotation) {
		super(engine, entityID, "Arachnotron", new Sprite(), team,
				120,
				0,
				150,
				AnimationLoader.getActorAnimationSet("Arachnotron"),
				SoundManager.getSound("ArachnotronDeath"),
				SoundManager.getSound("HellKnightHurt"),
				SoundManager.getSound("ArachnotronAlert"),
				null,
				SoundManager.getSound("PlasmaGun"),
				0.1f,
				0,
				12,
				2.5f,
				150,
				600,
				new PlasmaProjectilePackage(),
				null);

		sprite.init("Arachnotron", x, y, 75, 0, rotation, false);
		this.rangedPackage.setDamageMin(5);
		this.rangedPackage.setDamageMax(17);
		this.effectable = false;
	}
}
