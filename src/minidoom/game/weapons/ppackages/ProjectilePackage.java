package minidoom.game.weapons.ppackages;

import kuusisto.tinysound.Sound;
import minidoom.entity.Actor;
import minidoom.entity.PhysicalEntity;
import minidoom.game.animations.AnimationSet;

/**
 * A projectile package is held by each weapon. This contains the various information about
 * each projectile and is sent to the game engine to request a projectile to be spawned with these
 * stats. This allows for possible weapon upgrades, where the projectile package is changed.
 * Example: Pistol gets explosive upgrade, now bullets explode.
 */
public abstract class ProjectilePackage {
	private String name;
	private Actor shooter;
	private AnimationSet deathAnimation;
	private AnimationSet livingAnimation;
	private float projectileRange;
	private float weaponSpread;
	private boolean uniformSpread;
	private int numberOfProjectiles;
	private Sound deathSound;
	private boolean ripAndTear;
	private int entityID;

	private int damageMin;
	private int damageMax;
	private boolean explosive;
	private float explosiveRange;
	private int projectileSpeed;
	private boolean projectileRotates;
	private boolean particleRotates;
	private float spawnPointX;
	private float spawnPointY;
	private float rotation;
	private boolean selfDamageExplosion;

	private int width;
	private int height;

	public ProjectilePackage(String name, AnimationSet deathAnimation, AnimationSet livingAnimation, float projectileRange,
	                         float weaponSpread, boolean uniformSpread, int numberOfProjectiles, int damageMin,
	                         int damageMax, boolean explosive, int projectileSpeed, boolean projectileRotates,
	                         boolean particleRotates, int width, int height, float explosiveRange, Sound deathSound) {
		this.name = name;
		this.entityID = Integer.MAX_VALUE;
		this.deathAnimation = deathAnimation;
		this.livingAnimation = livingAnimation;
		this.projectileRange = projectileRange;
		this.weaponSpread = weaponSpread;
		this.uniformSpread = uniformSpread;
		this.numberOfProjectiles = numberOfProjectiles;
		this.damageMin = damageMin;
		this.damageMax = damageMax;
		this.explosive = explosive;
		this.projectileSpeed = projectileSpeed;
		this.projectileRotates = projectileRotates;
		this.particleRotates = particleRotates;
		this.explosiveRange = explosiveRange;
		this.selfDamageExplosion = true;
		this.deathSound = deathSound;
		this.ripAndTear = false;
	}

	public void setShooter(Actor shooter) { this.shooter = shooter; }
	public void setRipAndTear(boolean ripAndTear) { this.ripAndTear = ripAndTear; }
	public void setExplosiveRange(float range) { this.explosiveRange = range; }
	public void setProjectileRange(float projectileRange) { this.projectileRange = projectileRange; }
	public void setUniformSpread(boolean uniformSpread) { this.uniformSpread = uniformSpread; }
	public void setDamageMax(int damageMax) { this.damageMax = damageMax; }
	public void setDamageMin(int damageMin) { this.damageMin = damageMin; }
	public void setExplosive(boolean explosive) { this.explosive = explosive; }
	public void setName(String name) { this.name = name; }
	public void setNumberOfProjectiles(int numberOfProjectiles) { this.numberOfProjectiles = numberOfProjectiles; }
	public void setProjectileRotates(boolean projectileRotates) { this.projectileRotates = projectileRotates; }
	public void setProjectileSpeed(int projectileSpeed) { this.projectileSpeed = projectileSpeed; }
	public void setRotation(float rotation) { this.rotation = rotation; }
	public void setSpawnPointX(float spawnPointX) { this.spawnPointX = spawnPointX; }
	public void setSpawnPointY(float spawnPointY) { this.spawnPointY = spawnPointY; }
	public void setWeaponSpread(float weaponSpread) { this.weaponSpread = weaponSpread; }
	public void setWidth(int width) { this.width = width; }
	public void setHeight(int height) { this.height = height; }
	public void setLivingAnimation(AnimationSet as) { this.livingAnimation = as; }
	public void setEntityID(int entityID) { this.entityID = entityID; }
	public void setDeathSound(Sound deathSound) { this.deathSound = deathSound; }
	public void setDeathAnimation(AnimationSet as) { this.deathAnimation = as; }
	public void setSelfDamageExplosion(boolean selfDamageExplosion) { this.selfDamageExplosion = selfDamageExplosion;}

	public Actor getShooter() { return shooter; }
	public boolean getRipAndTear() { return ripAndTear; }
	public int getDamageMax() { return damageMax; }
	public int getDamageMin() { return damageMin; }
	public String getName() { return name; }
	public int getNumberOfProjectiles() { return numberOfProjectiles; }
	public int getProjectileSpeed() { return projectileSpeed; }
	public float getProjectileRange() { return projectileRange; }
	public float getRotation() { return rotation; }
	public float getSpawnPointX() { return spawnPointX; }
	public float getSpawnPointY() { return spawnPointY; }
	public float getWeaponSpread() { return weaponSpread; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public float getExplosiveRange() { return explosiveRange; }
	public AnimationSet getDeathAnimation() { return deathAnimation; }
	public AnimationSet getLivingAnimation() { return livingAnimation; }
	public Sound getDeathSound() { return deathSound; }
	public int getEntityID() { return entityID; }
	public boolean getSelfDamageExplosion() { return selfDamageExplosion; }

	public boolean explosive() { return explosive; }
	public boolean rotates() { return projectileRotates; }
	public boolean particleRotates() { return particleRotates; }
	public boolean uniformSpread() { return uniformSpread; }
}
