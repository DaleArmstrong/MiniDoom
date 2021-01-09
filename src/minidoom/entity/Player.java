package minidoom.entity;

import kuusisto.tinysound.Sound;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.managers.SoundManager;
import minidoom.game.weapons.*;
import minidoom.util.Constants;
import minidoom.util.Vector2f;

import java.util.EnumMap;
import java.util.Map;

public class Player extends Actor {
	private Map<Weapon.WeaponType, Weapon> heldWeapons;
	private Map<Weapon.AmmoType, Integer> heldAmmo;
	private Map<Weapon.AmmoType, Integer> maxAmmo;
	private Weapon currentWeapon;
	private float dodgeTime;
	private float weaponSwitchSpeed;
	private float dodgeDelay;
	private float dodgeTimer;
	private float weaponSwitchTimer;
	private float reloadTimer;
	private float dodgeDelayTimer;
	private float useTimer;
	private float killTimer;
	private float killStreak;
	private Sound reloadSound;

	private boolean moveUp;
	private boolean moveDown;
	private boolean moveLeft;
	private boolean moveRight;
	private boolean fire;
	private boolean altFire;
	private boolean dodge;
	private boolean use;
	private boolean swapNext;
	private boolean swapPrev;
	private boolean reload;
	private boolean swapMelee;
	private boolean swapGun;
	private boolean swapShotgun;
	private boolean swapMachinegun;
	private boolean swapRocketLauncher;
	private boolean swapPlasmagun;

	private int mouseX;
	private int mouseY;
	private float posRelativeX;
	private float posRelativeY;
	private float ratioX;
	private float ratioY;

	public Player(GameEngine engine, int entityID, Team team, float x, float y, float rotation) {
		super(engine, entityID, "Player", new Sprite(), true, true, true, true,
				team, new Vector2f(), 250, 260, 100, ActorState.IDLE, null,
				AnimationLoader.getPlayerAnimationSet(Weapon.WeaponType.GUN), SoundManager.getSound("PlayerDeath"),
				SoundManager.getSound("PlayerHurt"));

		sprite.init("Player", x, y, 0, 0, rotation,false);
		this.maxHealth = 200;
		this.armor = 0;
		this.armorProtectionLevel = 1.0f / 3.0f;
		this.maxArmor = 200;
		this.weaponSwitchSpeed = 0.5f;
		this.dodgeTime = 0.3f;
		this.dodgeDelay = 1.20f;
		this.dodgeDelayTimer = 0;
		this.dodgeTimer = 0;
		this.weaponSwitchTimer = 0;
		this.reloadTimer = 0;
		this.useTimer = 0;
		this.dyingTimer = 0;
		this.killTimer = 0;
		this.posRelativeY = 0;
		this.posRelativeX = 0;
		this.killStreak = 0;
		this.ratioX = 0;
		this.ratioY = 0;
		this.reloadSound = SoundManager.getSound("Reload");
		heldAmmo = new EnumMap<>(Weapon.AmmoType.class);
		heldAmmo.put(Weapon.AmmoType.MELEE, 999);
		heldAmmo.put(Weapon.AmmoType.BULLET, 100);
		heldAmmo.put(Weapon.AmmoType.SHOTGUN, 0);
		heldAmmo.put(Weapon.AmmoType.ENERGY, 0);
		heldAmmo.put(Weapon.AmmoType.ROCKET, 0);

		maxAmmo = new EnumMap<>(Weapon.AmmoType.class);
		maxAmmo.put(Weapon.AmmoType.MELEE, 999);
		maxAmmo.put(Weapon.AmmoType.BULLET, 300);
		maxAmmo.put(Weapon.AmmoType.SHOTGUN, 100);
		maxAmmo.put(Weapon.AmmoType.ENERGY, 300);
		maxAmmo.put(Weapon.AmmoType.ROCKET, 100);

		heldWeapons = new EnumMap<>(Weapon.WeaponType.class);
		heldWeapons.put(Weapon.WeaponType.MELEE, new Melee(engine));
		heldWeapons.put(Weapon.WeaponType.GUN, new Gun(engine));

		currentWeapon = heldWeapons.get(Weapon.WeaponType.GUN);
		this.sprite.updateShadowImage(actorAnimationSet.getIdle(ActorDirection.DOWN));
	}

	public void giveAll() {
		if (!heldWeapons.containsKey(Weapon.WeaponType.SHOTGUN)) {
			Weapon newWeapon = engine.generateWeapon(Weapon.WeaponType.SHOTGUN);
			if (newWeapon != null) {
				addWeapon(Weapon.WeaponType.SHOTGUN, newWeapon);
			} else {
				System.out.println("Could not generate shotgun for copy");
			}
		}

		if (!heldWeapons.containsKey(Weapon.WeaponType.MACHINEGUN)) {
			Weapon newWeapon = engine.generateWeapon(Weapon.WeaponType.MACHINEGUN);
			if (newWeapon != null) {
				addWeapon(Weapon.WeaponType.MACHINEGUN, newWeapon);
			} else {
				System.out.println("Could not generate machine gun for copy");
			}
		}

		if (!heldWeapons.containsKey(Weapon.WeaponType.PLASMAGUN)) {
			Weapon newWeapon = engine.generateWeapon(Weapon.WeaponType.PLASMAGUN);
			if (newWeapon != null) {
				addWeapon(Weapon.WeaponType.PLASMAGUN, newWeapon);
			} else {
				System.out.println("Could not generate plasma gun for copy");
			}
		}

		if (!heldWeapons.containsKey(Weapon.WeaponType.ROCKETLAUNCHER)) {
			Weapon newWeapon = engine.generateWeapon(Weapon.WeaponType.ROCKETLAUNCHER);
			if (newWeapon != null) {
				addWeapon(Weapon.WeaponType.ROCKETLAUNCHER, newWeapon);
			} else {
				System.out.println("Could not generate rocket launcher for copy");
			}
		}

		health = 200;
		armor = 200;
		armorProtectionLevel = 0.5f;
		heldAmmo.put(Weapon.AmmoType.BULLET, maxAmmo.get(Weapon.AmmoType.BULLET));
		heldAmmo.put(Weapon.AmmoType.SHOTGUN, maxAmmo.get(Weapon.AmmoType.SHOTGUN));
		heldAmmo.put(Weapon.AmmoType.ENERGY, maxAmmo.get(Weapon.AmmoType.ENERGY));
		heldAmmo.put(Weapon.AmmoType.ROCKET, maxAmmo.get(Weapon.AmmoType.ROCKET));

		currentWeapon = heldWeapons.get(Weapon.WeaponType.PLASMAGUN);
		actorAnimationSet = AnimationLoader.getPlayerAnimationSet(this.currentWeapon.getWeaponType());
		this.sprite.updateShadowImage(actorAnimationSet.getIdle(getDirection()));
	}

	@Override
	public void alert(boolean scream) {

	}

	/* Resets the player's timers, generally used for respawn */
	public void resetPlayerTimers() {
		dodgeTimer = 0;
		dodgeDelayTimer = 0;
		weaponSwitchTimer = 0;
		reloadTimer = 0;
		useTimer = 0;
		dyingTimer = 0;
		killTimer = 0;
		stunDelayTimer = 0;
		stunLengthTimer = 0;
		imageTimer = 0;
		attackingTimer = 0;
		timeToIdleTimer = 0;
		hurtSoundTimer = 0;
		currentFrame = actorAnimationSet.getIdleFrameNumber();
		fired = false;
		ripAndTearAvailable = false;
		ripTearTarget = false;
		animation.setDead(true);
		shootable = true;
	}

	/* copy this player from another player */
	public void copy(Player player) {
		this.health = player.health;
		this.armor = player.armor;
		this.armorProtectionLevel = player.armorProtectionLevel;
		this.maxAmmo.put(Weapon.AmmoType.BULLET, player.getMaxAmmo(Weapon.AmmoType.BULLET));
		this.maxAmmo.put(Weapon.AmmoType.SHOTGUN, player.getMaxAmmo(Weapon.AmmoType.SHOTGUN));
		this.maxAmmo.put(Weapon.AmmoType.ENERGY, player.getMaxAmmo(Weapon.AmmoType.ENERGY));
		this.maxAmmo.put(Weapon.AmmoType.ROCKET, player.getMaxAmmo(Weapon.AmmoType.ROCKET));
		this.heldAmmo.put(Weapon.AmmoType.BULLET, player.getHeldAmmo(Weapon.AmmoType.BULLET));
		this.heldAmmo.put(Weapon.AmmoType.SHOTGUN, player.getHeldAmmo(Weapon.AmmoType.SHOTGUN));
		this.heldAmmo.put(Weapon.AmmoType.ENERGY, player.getHeldAmmo(Weapon.AmmoType.ENERGY));
		this.heldAmmo.put(Weapon.AmmoType.ROCKET, player.getHeldAmmo(Weapon.AmmoType.ROCKET));

		if (player.hasWeapon(Weapon.WeaponType.SHOTGUN)) {
			Weapon newWeapon = engine.generateWeapon(Weapon.WeaponType.SHOTGUN);
			if (newWeapon != null) {
				addWeapon(Weapon.WeaponType.SHOTGUN, newWeapon);
			} else {
				System.out.println("Could not generate shotgun for copy");
			}
		}

		if (player.hasWeapon(Weapon.WeaponType.MACHINEGUN)) {
			Weapon newWeapon = engine.generateWeapon(Weapon.WeaponType.MACHINEGUN);
			if (newWeapon != null) {
				addWeapon(Weapon.WeaponType.MACHINEGUN, newWeapon);
			} else {
				System.out.println("Could not generate machine gun for copy");
			}
		}

		if (player.hasWeapon(Weapon.WeaponType.PLASMAGUN)) {
			Weapon newWeapon = engine.generateWeapon(Weapon.WeaponType.PLASMAGUN);
			if (newWeapon != null) {
				addWeapon(Weapon.WeaponType.PLASMAGUN, newWeapon);
			} else {
				System.out.println("Could not generate plasma gun for copy");
			}
		}

		if (player.hasWeapon(Weapon.WeaponType.ROCKETLAUNCHER)) {
			Weapon newWeapon = engine.generateWeapon(Weapon.WeaponType.ROCKETLAUNCHER);
			if (newWeapon != null) {
				addWeapon(Weapon.WeaponType.ROCKETLAUNCHER, newWeapon);
			} else {
				System.out.println("Could not generate rocket launcher for copy");
			}
		}

		this.currentWeapon = heldWeapons.get(player.getCurrentWeapon().getWeaponType());
		this.actorAnimationSet = AnimationLoader.getPlayerAnimationSet(this.currentWeapon.getWeaponType());
		this.sprite.updateShadowImage(actorAnimationSet.getIdle(ActorDirection.DOWN));
	}

	/* Spawns the player at this location */
	public void spawnPosition(float x, float y, float rotation) {
		this.sprite.setPosition(x, y);
		this.sprite.setRotation(rotation);
	}

	/* Check if player has max health when picking up a health item */
	public boolean hasMaxHealth(int maxHealthValue) {
		return health >= maxHealthValue || health >= maxHealth;
	}

	/* Adds health to the player up to the maximum that the health item may give */
	public void addHealth(int value, int maxHealthValue) {
		health += value;
		if (health > maxHealthValue)
			health = maxHealthValue;
		if (health > maxHealth)
			health = maxHealth;
	}

	/* Checks if player has the max armor when picking up an armor item */
	public boolean hasMaxArmor(int maxArmorPickup) {
		return armor >= maxArmorPickup || armor >= maxArmor;
	}

	/* Adds armor up to the max armor value of the armor item picked up */
	public void addArmor(int value, int maxArmorValue, float protectionLevel) {
		armor += value;
		if (armor > maxArmorValue)
			armor = maxArmorValue;
		if (armor > maxArmor)
			armor = maxArmor;
		if (protectionLevel > armorProtectionLevel)
			armorProtectionLevel = protectionLevel;
	}

	/* Checks if player has max ammo of the given type */
	public boolean hasMaxAmmo(Weapon.AmmoType ammoType) {
		return heldAmmo.get(ammoType) >= maxAmmo.get(ammoType);
	}

	/* Adds ammo of the given type up to the max ammo that the player can hold */
	public void addAmmo(Weapon.AmmoType ammoType, int value) {
		heldAmmo.put(ammoType, heldAmmo.get(ammoType) + value);
		if (heldAmmo.get(ammoType) > maxAmmo.get(ammoType))
			heldAmmo.put(ammoType, maxAmmo.get(ammoType));
		else if (heldAmmo.get(ammoType) < 0)
			heldAmmo.put(ammoType, 0);
	}

	/* Check if the player currently has this weapon */
	public boolean hasWeapon(Weapon.WeaponType weaponType) {
		return heldWeapons.containsKey(weaponType);
	}

	/* Adds the weapon to the players inventory */
	public void addWeapon(Weapon.WeaponType weaponType, Weapon weapon) {
		heldWeapons.put(weaponType, weapon);
	}

	@Override
	public void update(float dt) {
		if (actorState == ActorState.DEAD)
			return;

		if (actorState == ActorState.DYING) {
			dying(dt);
			return;
		}

		if (actorState == ActorState.RIPANDTEAR) {
			ripAndTear(dt);
			return;
		}

		/* update timers */
		if (dodgeTimer > 0) {
			dodgeTimer -= dt;
			if (dodgeTimer <= 0) {
				shootable = true;
				actorState = ActorState.IDLE;
			}
		}

		if (attackingTimer > 0) {
			attackingTimer -= dt;
			if (attackingTimer <= 0 && actorState == ActorState.ATTACKING)
				actorState = ActorState.IDLE;
		}

		if (timeToIdleTimer > 0) {
			timeToIdleTimer -= dt;
			if (timeToIdleTimer <= 0 && actorState == ActorState.MOVING)
				actorState = ActorState.IDLE;
		}

		if (hurtSoundTimer > 0)
			hurtSoundTimer -= dt;
		if (stunDelayTimer > 0)
			stunDelayTimer -= dt;
		if (stunLengthTimer > 0)
			stunLengthTimer -= dt;
		if (weaponSwitchTimer > 0)
			weaponSwitchTimer -= dt;
		if (dodgeDelayTimer > 0)
			dodgeDelayTimer -= dt;
		if (useTimer > 0)
			useTimer -= dt;
		if (killTimer > 0) {
			killTimer -= dt;
			if (killTimer <= 0.0)
				killStreak = 0;
		}

		if (reloadTimer > 0) {
			reloadTimer -= dt;
			if (reloadTimer <= 0) {
				int ammoLoaded = currentWeapon.reloadNow(heldAmmo.get(currentWeapon.getAmmoType()));
				if (ammoLoaded > 0)
					heldAmmo.put(currentWeapon.getAmmoType(), heldAmmo.get(currentWeapon.getAmmoType()) - ammoLoaded);
			}
		}

		/* Check if empty clip and start to reload */
		if (currentWeapon.getCurrentClip() == 0)
			reload();

		/* try to dodge */
		if (dodge && dodgeDelayTimer <= 0 && dodgeTimer <= 0) {
			if (sprite.rotates()) {
				if (moveDown)
					velocity.y = 1.85f;
				else
					velocity.y = -1.85f;
			}
			else {
				if (moveUp)
					velocity.y = -1.85f;
				if (moveDown)
					velocity.y = 1.85f;
				if (moveLeft)
					velocity.x = -1.85f;
				if (moveRight)
					velocity.x = 1.85f;

				if (Math.abs(velocity.x) > 0 && Math.abs(velocity.y) > 0) {
					velocity.x *= 0.7071f;
					velocity.y *= 0.7071f;
				}
			}

			dodgeTimer = dodgeTime;
			dodgeDelayTimer = dodgeDelay;
			timeToIdleTimer = 0;
			attackingTimer = 0;
			shootable = false;
			actorState = ActorState.DODGING;

			if (getRotation() > 0 && getRotation() < 180) {
				animation.init(AnimationLoader.getAnimationSet("PlayerRollDown"), false);
			} else {
				animation.init(AnimationLoader.getAnimationSet("PlayerRollUp"), false);
			}
		}

		/* move player */
		if (actorState != ActorState.DODGING) {
			setRotation((float) (Math.atan2(mouseY - (sprite.getCenterY() - posRelativeY) * ratioY,
					mouseX - (sprite.getCenterX() - posRelativeX) * ratioX) * Constants.TO_DEGREES));

			if (moveUp)
				velocity.y = -1;
			if (moveDown)
				velocity.y = 1;

			if (moveLeft)
				velocity.x = -1;
			if (moveRight)
				velocity.x = 1;

			if (Math.abs(velocity.x) > 0.0f && Math.abs(velocity.y) > 0.0f) {
				velocity.x *= 0.7071f;
				velocity.y *= 0.7071f;
			}

			if (Math.abs(velocity.y) > 0 || Math.abs(velocity.x) > 0) {
				if (actorState == ActorState.IDLE && timeToIdleTimer <= 0)
					imageTimer = 0.2f;
				actorState = ActorState.MOVING;
				timeToIdleTimer = 0.5f;
			} else
				actorState = ActorState.IDLE;
		}

		/* rotate player */
		if (sprite.rotates()) {
			if (actorState != ActorState.DODGING)
				sprite.rotate(velocity.x * rotationSpeed * dt);
			float speedChange = velocity.y * dt * speed * -1;

			sprite.move((float)Math.cos(Math.toRadians(sprite.getRotation())) * speedChange,
					(float)Math.sin(Math.toRadians(sprite.getRotation())) * speedChange);
		}
		else
			sprite.move(velocity.x * dt * speed, velocity.y * dt * speed);

		if (actorState != ActorState.DODGING) {
			velocity.x = 0;
			velocity.y = 0;

			if (reload)
				reload();

			/* fire weapons */
			if (playerIsReady() && currentWeapon.isReady()) {
				if (fire) {
					if (currentWeapon.getCurrentClip() >= currentWeapon.getAmmoPerFire() && currentWeapon.isReady()) {
						float spawnPointX = (float) (sprite.getCenterX() + Math.cos(Math.toRadians(getRotation())) * sprite.getHalfWidth());
						float spawnPointY = (float) (sprite.getCenterY() + Math.sin(Math.toRadians(getRotation())) * sprite.getHalfHeight());
						currentWeapon.fire(this, spawnPointX, spawnPointY, getRotation());
						fired = true;
						actorState = ActorState.ATTACKING;
						attackingTimer = 0.5f;
						engine.alertEnemies(sprite.getCenterX(), sprite.getCenterY(), false);
					} else if (heldAmmo.get(currentWeapon.getAmmoType()) > 0) {
						reload();
					} else if (currentWeapon.getCurrentClip() == 0)
						swapPrev();
				}

				if (altFire) {
					if (currentWeapon.getCurrentClip() >= currentWeapon.getAmmoPerAltFire() && currentWeapon.isReady()) {
						float spawnPointX = (float) (sprite.getCenterX() + (Math.cos(Math.toRadians(getRotation()))) * sprite.getHalfWidth());
						float spawnPointY = (float) (sprite.getCenterY() + (Math.sin(Math.toRadians(getRotation()))) * sprite.getHalfHeight());
						currentWeapon.altFire(this, spawnPointX, spawnPointY, getRotation());
						fired = true;
						actorState = ActorState.ATTACKING;
						attackingTimer = 0.5f;
						engine.alertEnemies(sprite.getCenterX(), sprite.getCenterY(), false);
					} else if (heldAmmo.get(currentWeapon.getAmmoType()) > 0) {
						reload();
					} else if (currentWeapon.getCurrentClip() == 0)
						swapPrev();
				}
			}

			if (use && useTimer <= 0) {
				useTimer = 1.0f;
			}

			if (swapNext) {
				swapNext();
				swapNext = false;
			}

			if (swapPrev) {
				swapPrev();
				swapPrev = false;
			}

			if (swapMelee) {
				swapWeapon(Weapon.WeaponType.MELEE);
				swapMelee = false;
			}

			if (swapGun) {
				swapWeapon(Weapon.WeaponType.GUN);
				swapGun = false;
			}

			if (swapShotgun) {
				swapWeapon(Weapon.WeaponType.SHOTGUN);
				swapShotgun = false;
			}

			if (swapMachinegun) {
				swapWeapon(Weapon.WeaponType.MACHINEGUN);
				swapMachinegun = false;
			}

			if (swapRocketLauncher) {
				swapWeapon(Weapon.WeaponType.ROCKETLAUNCHER);
				swapRocketLauncher = false;
			}

			if (swapPlasmagun) {
				swapWeapon(Weapon.WeaponType.PLASMAGUN);
				swapPlasmagun = false;
			}
		}

		heldWeapons.forEach((key, weapon) -> weapon.update(dt));
		updateImage(dt);
	}

	private void swapWeapon(Weapon.WeaponType weaponType) {
		if (weaponType == currentWeapon.getWeaponType())
			return;

		if (heldWeapons.containsKey(weaponType)) {
			weaponSwitchTimer = weaponSwitchSpeed;
			currentWeapon = heldWeapons.get(weaponType);
			swapAnimationSet();
		}
	}

	/* Swap to the next weapon available weapon that has ammo */
	private void swapNext() {
		Weapon.WeaponType switchWeapon = currentWeapon.getWeaponType();
		switchWeapon = switchWeapon.next();
		while (!heldWeapons.containsKey(switchWeapon)
				|| (heldWeapons.get(switchWeapon).getCurrentClip() == 0
				&& heldAmmo.get(heldWeapons.get(switchWeapon).getAmmoType()) == 0)) {
			switchWeapon = switchWeapon.next();
		}
		if (switchWeapon != currentWeapon.getWeaponType()) {
			weaponSwitchTimer = weaponSwitchSpeed;
			currentWeapon = heldWeapons.get(switchWeapon);
			swapAnimationSet();
		}
	}

	/* Swap to the previous weapon available that has ammo */
	private void swapPrev() {
		Weapon.WeaponType switchWeapon = currentWeapon.getWeaponType();
		switchWeapon = switchWeapon.prev();
		while (!heldWeapons.containsKey(switchWeapon)
				|| (heldWeapons.get(switchWeapon).getCurrentClip() == 0
				&& heldAmmo.get(heldWeapons.get(switchWeapon).getAmmoType()) == 0)) {
			switchWeapon = switchWeapon.prev();
		}
		if (switchWeapon != currentWeapon.getWeaponType()) {
			weaponSwitchTimer = weaponSwitchSpeed;
			currentWeapon = heldWeapons.get(switchWeapon);
			swapAnimationSet();
		}
	}

	private void swapAnimationSet() {
		actorAnimationSet = AnimationLoader.getPlayerAnimationSet(currentWeapon.getWeaponType());
	}

	/* Reload the currently equipped weapon */
	private void reload() {
		if (weaponSwitchTimer <= 0 && reloadTimer <= 0 && currentWeapon.isReady() &&
				currentWeapon.getCurrentClip() < currentWeapon.getClipSize() &&
				heldAmmo.get(currentWeapon.getAmmoType()) > 0) {
			reloadTimer = currentWeapon.getReloadSpeed();
			reloadSound.play();
		}
	}

	/* Check if the correct timers are ready to see if the player can perform an action */
	public boolean playerIsReady() {
		return reloadTimer <= 0 && dodgeTimer <= 0 && weaponSwitchTimer <= 0;
	}

	public int getHeldAmmo(Weapon.AmmoType ammoType) {
		return heldAmmo.get(ammoType);
	}

	public int getMaxAmmo(Weapon.AmmoType ammoType) {
		return maxAmmo.get(ammoType);
	}

	public Weapon getCurrentWeapon() {
		return currentWeapon;
	}

	public int getCurrentClip() {
		return currentWeapon.getCurrentClip();
	}

	public int getArmor() {
		return armor;
	}

	public void moveUpPressed() { moveUp = true; }
	public void moveDownPressed() { moveDown = true; }
	public void moveLeftPressed() { moveLeft = true; }
	public void moveRightPressed() { moveRight = true; }
	public void firePressed() { fire = true; }
	public void altFirePressed() { altFire = true; }
	public void dodgePressed() { dodge = true; }
	public void usePressed() { use = true; }
	public void reloadPressed() { reload = true; }
	public void swapMeleePressed() { swapMelee = true; }
	public void swapGunPressed() { swapGun = true; }
	public void swapShotgunPressed() { swapShotgun = true; }
	public void swapMachinegunPressed() { swapMachinegun = true; }
	public void swapRocketLauncherPressed() { swapRocketLauncher = true; }
	public void swapPlasmagunPressed() { swapPlasmagun = true; }

	public void moveUpReleased() { moveUp = false; }
	public void moveDownReleased() { moveDown = false; }
	public void moveLeftReleased() { moveLeft = false; }
	public void moveRightReleased() { moveRight = false; }
	public void fireReleased() { fire = false; }
	public void altFireReleased() { altFire = false; }
	public void dodgeReleased() { dodge = false; }
	public void useReleased() { use = false; }
	public void reloadReleased() { reload = false; }
	public void swapMeleeReleased() { swapMelee = true; }
	public void swapGunReleased() { swapGun = true; }
	public void swapShotgunReleased() { swapShotgun = true; }
	public void swapMachinegunReleased() { swapMachinegun = true; }
	public void swapRocketLauncherReleased() { swapRocketLauncher = true; }
	public void swapPlasmagunReleased() { swapPlasmagun = true; }

	public void swapNextPressed() {
		if (swapPrev)
			swapPrev = false;
		swapNext = true;
	}

	public void swapPrevPressed() {
		if (swapNext)
			swapNext = false;
		swapPrev = true;
	}

	public void setMouse(int mouseX, int mouseY) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}

	public void setRelativeToScreen(float x, float y, float ratioX, float ratioY) {
		this.posRelativeX = x;
		this.posRelativeY = y;
		this.ratioX = ratioX;
		this.ratioY = ratioY;
	}

	public boolean hurt() { return stunLengthTimer > 0.00f; }

	public boolean onKillStreak() { return killStreak > 3; }

	public void addKill() {
		killTimer = 5.0f;
		killStreak++;
	}
}
