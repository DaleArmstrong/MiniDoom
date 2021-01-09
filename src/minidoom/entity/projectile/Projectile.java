package minidoom.entity.projectile;

import kuusisto.tinysound.Sound;
import minidoom.entity.Actor;
import minidoom.entity.PhysicalEntity;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationSet;
import minidoom.game.weapons.ppackages.ProjectilePackage;
import minidoom.util.Constants;
import minidoom.util.Vector2f;

/**
 * Template for projectile entities. Receives a projectile package from a weapon
 * and creates the requested projectile.
 */
public class Projectile extends PhysicalEntity {
	private Actor shooter;
	private AnimationSet deathAnimation;
	private int damageMin;
	private int damageMax;
	private float projectileRange;
	private boolean explosive;
	private float explosiveRange;
	private float distanceTraveled;
	private boolean destroyed;
	private boolean particleRotates;
	private Sound deathSound;
	private boolean ripAndTear;
	private boolean selfDamageExplosion;

	public Projectile(GameEngine engine) {
		super(engine, 0, new Sprite(), true, false, false, false, false,
				Team.NEUTRAL, new Vector2f(), 0, 0, null);
	}

	public void init(ProjectilePackage pPackage, float angle) {
		if (pPackage.getName() == null)
			this.sprite.init(null, pPackage.getSpawnPointX(), pPackage.getSpawnPointY(), pPackage.getWidth(),
					pPackage.getHeight(), angle, pPackage.rotates());
		else
			this.sprite.init(pPackage.getName() + "", pPackage.getSpawnPointX(), pPackage.getSpawnPointY(),
					pPackage.getWidth(), pPackage.getHeight(), angle, pPackage.rotates());

		this.deathAnimation = pPackage.getDeathAnimation();
		this.sprite.setX(sprite.getX() - sprite.getHalfWidth());
		this.sprite.setY(sprite.getY() - sprite.getHalfHeight());

		this.speed = pPackage.getProjectileSpeed();
		this.velocity.x = (float)(pPackage.getProjectileSpeed() * Math.cos(Math.toRadians(angle)));
		this.velocity.y = (float)(pPackage.getProjectileSpeed() * Math.sin(Math.toRadians(angle)));
		this.projectileRange = pPackage.getProjectileRange();
		this.damageMin = pPackage.getDamageMin();
		this.damageMax = pPackage.getDamageMax();
		this.explosive = pPackage.explosive();
		this.explosiveRange = pPackage.getExplosiveRange();
		this.distanceTraveled = 0;
		this.destroyed = false;
		this.particleRotates = pPackage.particleRotates();
		this.animation.init(pPackage.getLivingAnimation(), true);
		this.deathSound = pPackage.getDeathSound();
		this.ripAndTear = pPackage.getRipAndTear();
		this.selfDamageExplosion = pPackage.getSelfDamageExplosion();
		if (pPackage.getShooter() == null) {
			this.shooter = null;
			this.team = Team.NEUTRAL;
			this.entityID = pPackage.getEntityID();
		} else {
			this.shooter = pPackage.getShooter();
			this.team = pPackage.getShooter().getTeam();
			this.entityID = pPackage.getShooter().getEntityID();
		}
	}

	@Override
	public void onHit(int damage, String name) {

	}

	@Override
	public void update(float dt) {
		if (animation.hasAnimation()) {
			animation.update(dt);
			if (sprite.getImage() != animation.getCurrentImage())
				sprite.updateImage(animation.getCurrentImage());
		}

		float dx = velocity.x * dt;
		float dy = velocity.y * dt;
		distanceTraveled += speed * dt;
		if (distanceTraveled >= projectileRange) {
			destroyed = true;
		}
		else
			sprite.move(dx, dy);
	}

	@Override
	public void kill() {
		destroyed = true;
		float spawnX = (float) (sprite.getCenterX() + Math.cos(Math.toRadians(getRotation())) * sprite.getWidth());
		float spawnY = (float) (sprite.getCenterY() + Math.sin(Math.toRadians(getRotation())) * sprite.getHeight());
		if (isExplosive()) {
			if (selfDamageExplosion)
				engine.spawnExplosion(spawnX, spawnY, getDamage(), explosiveRange, null);
			else
				engine.spawnExplosion(spawnX, spawnY, getDamage(), explosiveRange, this);
		}
		if (deathAnimation != null) {
			if (particleRotates)
				engine.spawnParticle(deathAnimation, spawnX, spawnY, 0, 0, sprite.getRotation(), 0);
			else
				engine.spawnParticle(deathAnimation, spawnX, spawnY, 0, 0, 0, 0);
		}
		engine.addProjectileRemoval(this);
		if (deathSound != null)
			deathSound.play();
	}

	public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }
	public boolean destroyed() { return destroyed; }
	public boolean isExplosive() { return explosive; }
	public int getDamage() { return Constants.rand(damageMin, damageMax); }
	public boolean isRipAndTear() { return ripAndTear; }
	public Actor getShooter() { return shooter; }
}
