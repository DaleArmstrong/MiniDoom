package minidoom.entity;

import kuusisto.tinysound.Sound;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.game.animations.ActorAnimationSet;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.animations.AnimationSet;
import minidoom.game.managers.SoundManager;
import minidoom.util.Constants;
import minidoom.util.Vector2f;

import java.awt.*;

/**
 * Abstract class that is utilized for all Players and Enemies. Contains health,
 * actor states, and keeps track of a hurt animation effect.
 */
public abstract class Actor extends PhysicalEntity {
	public enum ActorState {
		IDLE, MOVING, ATTACKING, DODGING, DYING, RIPANDTEAR, DEAD
	}

	public enum ActorDirection {
		LEFT, UPLEFT, UP, UPRIGHT, RIGHT, DOWNRIGHT, DOWN, DOWNLEFT
	}

	protected String name;
	protected int health;
	protected int maxHealth;
	protected ActorState actorState;
	protected float stunDelay;
	protected float stunDelayTimer;
	protected float stunLength;
	protected float stunLengthTimer;
	protected float dyingTimer;
	protected float imageTimer;
	protected float attackingTimer;
	protected float timeToIdleTimer;
	protected int currentFrame;
	protected ActorAnimationSet actorAnimationSet;
	protected boolean ripTearTarget;
	protected boolean ripAndTearAvailable;
	protected boolean fired;
	protected Actor attacker;
	protected boolean finishedRipAndTear;
	protected Sound deathSound;
	protected Sound hurtSound;
	protected float hurtSoundDelay;
	protected float hurtSoundTimer;
	protected int tempHealth;
	protected int armor;
	protected float armorProtectionLevel;
	protected int maxArmor;

	public Actor(GameEngine engine, int entityID, String name, Sprite sprite, boolean visible, boolean collidable,
	             boolean shootable, boolean effectable, Team team, Vector2f velocity, int speed, int rotationSpeed,
	             int health, ActorState actorState, AnimationSet as, ActorAnimationSet aas, Sound deathSound,
	             Sound hurtSound) {
		super(engine, entityID, sprite, visible, collidable, shootable, effectable, false, team,
				velocity, speed, rotationSpeed, as);
		this.name = name;
		this.health = health;
		this.maxHealth = health;
		this.maxArmor = 200;
		this.armor = 0;
		this.armorProtectionLevel = 0;
		this.actorState = actorState;
		this.stunDelay = 0.75f;
		this.stunLength = 0.15f;
		this.hurtSoundDelay = 1.2f;
		this.hurtSoundTimer = 0;
		this.actorAnimationSet = aas;
		this.imageTimer = 0;
		this.currentFrame = aas.getIdleFrameNumber();
		this.stunDelayTimer = 0;
		this.stunLengthTimer = 0;
		this.currentFrame = 0;
		this.ripTearTarget = false;
		this.ripAndTearAvailable = false;
		this.fired = false;
		this.attacker = null;
		this.finishedRipAndTear = false;
		this.attackingTimer = 0;
		this.timeToIdleTimer = 0;
		this.deathSound = deathSound;
		this.hurtSound = hurtSound;
		this.tempHealth = 0;
		this.finishedRipAndTear = false;
	}

	public ActorState getActorState() {
		return actorState;
	}

	public void setActorState(ActorState actorState) {
		this.actorState = actorState;
	}

	public void setHealth(int health) { this.health = health; }
	public int getHealth() { return health; }
	public String getName() { return name; }
	public int getMaxHealth() { return maxHealth; }
	public void setRipAndTearAvailable(boolean ready) { ripAndTearAvailable = ready; }

	public boolean ripTearReady() {
		return (!(actorState == ActorState.DYING || actorState == ActorState.DEAD
				|| actorState == ActorState.RIPANDTEAR) && ripAndTearAvailable);
	}

	public void setRipAndTear(boolean toggle) {
		if (toggle) {
			this.actorState = ActorState.RIPANDTEAR;
			this.visible = false;
			this.shootable = false;
			this.effectable = false;
			this.ripTearTarget = false;
		} else {
			this.visible = true;
			this.shootable = true;
			this.effectable = true;
			this.actorState = ActorState.IDLE;
			this.sprite.updateImage(actorAnimationSet.getIdle(getDirection()));
			this.finishedRipAndTear = true;
		}
	}

	public void activateRipAndTear(Actor attacker) {
		this.attacker = attacker;
		attacker.setRipAndTear(true);
		this.setActorState(ActorState.RIPANDTEAR);
		this.ripTearTarget = true;
		this.animation.init(AnimationLoader.getAnimationSet(attacker.getName() + name + "RT"), false);
		this.sprite.updateShadowImage(animation.getCurrentImage());
		this.hurtSound.play();
		this.hurtSoundTimer = this.hurtSoundDelay;
	}

	protected void ripAndTear(float dt) {
		if (ripTearTarget) {
			animation.update(dt);
			if (hurtSoundTimer > 0) {
				hurtSoundTimer -= dt;
				if (hurtSoundTimer <= 0) {
					hurtSound.play();
					hurtSoundTimer = 1.0f;
				}
			}
			if (animation.isDead()) {
				attacker.setRipAndTear(false);
				deathSound.play();
				kill();
			} else {
				sprite.updateShadowImage(animation.getCurrentImage());
			}
		} else {
			if (attackingTimer > 0)
				attackingTimer -= dt;
			if (timeToIdleTimer > 0)
				timeToIdleTimer -= dt;
			if (stunDelayTimer > 0)
				stunDelayTimer -= dt;
			if (stunLengthTimer > 0)
				stunLengthTimer -= dt;
		}
	}

	public ActorDirection getDirection() {
		float positiveAngle = sprite.getRotation() > 0 ? sprite.getRotation() : sprite.getRotation() + 360;
		if (positiveAngle <= 22.5 || positiveAngle >= 337.5)
			return ActorDirection.RIGHT;
		else if (positiveAngle < 67.5)
			return ActorDirection.DOWNRIGHT;
		else if (positiveAngle < 112.5)
			return ActorDirection.DOWN;
		else if (positiveAngle < 157.5)
			return ActorDirection.DOWNLEFT;
		else if (positiveAngle < 202.5)
			return ActorDirection.LEFT;
		else if (positiveAngle < 247.5)
			return ActorDirection.UPLEFT;
		else if (positiveAngle < 292.5)
			return ActorDirection.UP;
		else
			return ActorDirection.UPRIGHT;
	}

	@Override
	public int hashCode() {
		return entityID;
	}

	protected void updateImage(float dt) {
		if (actorState == ActorState.DODGING) {
			animation.update(dt);
			if (animation.isDead()) {
				actorState = ActorState.MOVING;
				sprite.updateShadowImage(actorAnimationSet.getIdle(getDirection()));
				imageTimer = 0;
			} else {
				sprite.updateShadowImage(animation.getCurrentImage());
			}
			return;
		}

		if (stunLengthTimer > 0) {
			sprite.updateShadowImage(actorAnimationSet.getHurt(getDirection()));
			imageTimer = 0.1f;
			return;
		}

		if (fired) {
			sprite.updateShadowImage(actorAnimationSet.getAttack(getDirection()));
			fired = false;
			imageTimer = 0.15f;
			return;
		}

		if (imageTimer > 0)
			imageTimer -= dt;

		if (imageTimer <= 0) {
			if (attackingTimer > 0.01) {
				sprite.updateShadowImage(actorAnimationSet.getAttackIdle(getDirection()));
			} else if (actorState == ActorState.IDLE) {
				if (timeToIdleTimer <= 0) {
					currentFrame = actorAnimationSet.getIdleFrameNumber();
					sprite.updateShadowImage(actorAnimationSet.getIdle(getDirection()));
				}
			}
			else if (actorState == ActorState.MOVING) {
				currentFrame++;
				if (currentFrame >= actorAnimationSet.getNumberOfMoving())
					currentFrame = 0;
				sprite.updateShadowImage(actorAnimationSet.getMovement(getDirection(), currentFrame));
				imageTimer = 0.12f;
			}
		}
	}

	@Override
	public void renderShadow(Graphics2D graphics) {
		if (visible && actorState != ActorState.DEAD && actorState != ActorState.DYING) {
			sprite.renderShadow(graphics);
		}
	}

	protected void dying(float dt) {
		if (hurtSoundTimer > 0.0001) {
			hurtSoundTimer -= dt;
			if (hurtSoundTimer <= 0) {
				hurtSound.play();
				hurtSoundTimer = 0.7f;
			}
		}

		if (dyingTimer > 0.0001) {
			dyingTimer -= dt;
			if (dyingTimer <= 0 || tempHealth <= 0) {
				deathSound.play();
				animation.init(actorAnimationSet.getDyingDeadAnimation(), false);
			}
		}

		animation.update(dt);
		sprite.updateShadowImage(animation.getCurrentImage());
		if (animation.isDead()) {
			kill();
		}
	}

	@Override
	public void kill() {
		if (!name.equals("Player"))
			engine.addPlayerKill();
		else
			engine.gameOver();

		actorState = ActorState.DEAD;
		collidable = false;
		shootable = false;
		effectable = false;
	}

	/* Damage, armor, and health calculations when hit */
	@Override
	public void onHit(int damage, String attackerName) {
		if (actorState == ActorState.RIPANDTEAR || actorState == ActorState.DEAD)
			return;


		if (actorState == ActorState.DYING) {
			if (tempHealth <= 0.00001)
				return;
			tempHealth -= damage;
			if (tempHealth <= 0) {
				animation.init(actorAnimationSet.getDyingDeadAnimation(), false);
				dyingTimer = 0;
			}
			if (hurtSoundTimer <= 0) {
				hurtSound.play();
				hurtSoundTimer = hurtSoundDelay;
			}
			return;
		}

		if (armor > 0) {
			int splitDamage = (int)(damage * armorProtectionLevel);
			armor -= splitDamage;
			damage -= splitDamage;
			if (armor <= 0) {
				damage += Math.abs(armor);
				armor = 0;
				armorProtectionLevel = 1.0f / 3.0f;
			}
		}

		health -= damage;
		engine.bloodSplatter((int)sprite.getCenterX(), (int)sprite.getCenterY());

		if (attackerName != null && health < 15 && armor <= 0 &&
				AnimationLoader.hasAnimationSet(attackerName + name + "RT")) {
			ripAndTearAvailable = true;
		} else if (health <= 0) {
			if (health < -40 && actorAnimationSet.hasExplosionAnimation()) {
				SoundManager.getSound("ExplosionDeath").play();
				animation.init(actorAnimationSet.getExplosionAnimation(), false);
			} else {
				int randomDeath = Constants.rand(1, 6);
				if (randomDeath == 6 && actorAnimationSet.hasExplosionAnimation()) {
					SoundManager.getSound("ExplosionDeath").play();
					animation.init(actorAnimationSet.getExplosionAnimation(), false);
				} else if (randomDeath == 5 && actorAnimationSet.hasDyingAnimation()) {
					animation.init(actorAnimationSet.getDyingAnimation(), true);
					dyingTimer = 5.0f;
					tempHealth = 20;
					hurtSound.play();
					hurtSoundTimer = 0.7f;
				} else {
					deathSound.play();
					animation.init(actorAnimationSet.getRandomDeathAnimation(), false);
				}
				sprite.updateShadowImage(animation.getCurrentImage());
			}
			if (tempHealth <= 0) {
				shootable = false;
				collidable = false;
			}
			actorState = ActorState.DYING;
			health = 0;
			return;
		}

		if (stunDelayTimer <= 0 && stunLengthTimer <= 0) {
			stunDelayTimer = stunDelay;
			stunLengthTimer = stunLength;
		}

		if (hurtSoundTimer <= 0) {
			hurtSound.play();
			hurtSoundTimer = hurtSoundDelay;
		}
	}

	public abstract void alert(boolean scream);
}

