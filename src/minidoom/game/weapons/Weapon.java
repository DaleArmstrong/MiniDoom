package minidoom.game.weapons;

import kuusisto.tinysound.Sound;
import minidoom.entity.Actor;
import minidoom.entity.PhysicalEntity;
import minidoom.game.GameEngine;
import minidoom.game.weapons.ppackages.ProjectilePackage;

import java.util.EnumMap;
import java.util.Map;

/**
 * Abstract class that keeps track of all the various stats of a weapon. Reload speed, weapon delays,
 * clip size, how much ammo per shot, ammo type, and holds the two projectile packages.
 */
public abstract class Weapon {
	public enum AmmoType {
		MELEE, BULLET, SHOTGUN, ROCKET, ENERGY
	}

	public enum WeaponType {
		MELEE, GUN, SHOTGUN, MACHINEGUN, PLASMAGUN, ROCKETLAUNCHER;

		private static WeaponType[] vals = values();
		public WeaponType next() {
			return vals[(this.ordinal()+1) % vals.length];
		}
		public WeaponType prev() {
			return vals[(this.ordinal()-1) < 0 ? vals.length - 1 : (this.ordinal()-1)];
		}
	}

	public static Map<WeaponType, String> weaponNames;
	static {
		weaponNames = new EnumMap<>(WeaponType.class);
		weaponNames.put(WeaponType.MELEE, "Melee");
		weaponNames.put(WeaponType.GUN, "Gun");
		weaponNames.put(WeaponType.SHOTGUN, "Shotgun");
		weaponNames.put(WeaponType.MACHINEGUN, "Machinegun");
		weaponNames.put(WeaponType.PLASMAGUN, "Plasma Gun");
		weaponNames.put(WeaponType.ROCKETLAUNCHER, "Rocket Launcher");
	}

	protected String name;
	protected GameEngine gameEngine;
	protected AmmoType ammoType;
	protected WeaponType weaponType;
	protected float primaryWeaponDelay;
	protected float secondaryWeaponDelay;
	protected int clipSize;
	protected int currentClip;
	protected int ammoPerFire;
	protected int ammoPerAltFire;
	protected float reloadSpeed;
	protected float delayTimer;
	protected ProjectilePackage primaryPackage;
	protected ProjectilePackage secondaryPackage;
	protected Sound primarySound;
	protected Sound secondarySound;

	public Weapon(GameEngine gameEngine, AmmoType ammoType, WeaponType weaponType, float primaryWeaponDelay,
	              float secondaryWeaponDelay, int clipSize, int ammoPerFire, int ammoPerAltFire, float reloadSpeed,
	              int currentClip) {
		this.gameEngine = gameEngine;
		this.ammoType = ammoType;
		this.weaponType = weaponType;
		this.primaryWeaponDelay = primaryWeaponDelay;
		this.secondaryWeaponDelay = secondaryWeaponDelay;
		this.clipSize = clipSize;
		this.currentClip = currentClip;
		this.ammoPerFire = ammoPerFire;
		this.ammoPerAltFire = ammoPerAltFire;
		this.reloadSpeed = reloadSpeed;
		this.delayTimer = 0;
	}

	public WeaponType getWeaponType() {
		return weaponType;
	}

	public float getReloadSpeed() {
		return reloadSpeed;
	}

	public int getCurrentClip() {
		return currentClip;
	}

	public int getClipSize() {
		return clipSize;
	}

	public void fire(Actor actor, float x, float y, float rotation) {
		if (delayTimer > 0 || currentClip < ammoPerFire)
			return;

		if (primarySound != null)
			primarySound.play();
		sendPackage(actor, x, y, rotation, ammoPerFire, primaryWeaponDelay, primaryPackage);
	}

	public void altFire(Actor actor, float x, float y, float rotation) {
		if (delayTimer > 0 || currentClip < ammoPerAltFire)
			return;

		if (secondarySound != null)
			secondarySound.play();
		sendPackage(actor, x, y, rotation, ammoPerAltFire, secondaryWeaponDelay, secondaryPackage);
	}

	protected void sendPackage(Actor actor, float x, float y, float rotation, int ammoUsed,
	                  float weaponDelay, ProjectilePackage pPackage) {

		currentClip -= ammoUsed;
		delayTimer = weaponDelay;
		pPackage.setShooter(actor);
		pPackage.setSpawnPointX(x);
		pPackage.setSpawnPointY(y);
		pPackage.setRotation(rotation);

		gameEngine.spawnProjectile(pPackage);
	}

	public int reloadNow(int ammoHeld) {
		if (ammoHeld < clipSize - currentClip) {
			currentClip = ammoHeld;
			return currentClip;
		} else {
			int loaded = clipSize - currentClip;
			currentClip = clipSize;
			return loaded;
		}
	}


	public void update(float dt) {
		if (delayTimer > 0)
			delayTimer -= dt;
	}

	public boolean isReady() {
		return delayTimer <= 0;
	}

	public AmmoType getAmmoType() {
		return ammoType;
	}

	public int getAmmoPerFire() { return ammoPerFire; }
	public int getAmmoPerAltFire() { return ammoPerAltFire; }
}
