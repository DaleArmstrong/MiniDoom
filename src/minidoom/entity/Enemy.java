package minidoom.entity;

import kuusisto.tinysound.Sound;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.game.animations.ActorAnimationSet;
import minidoom.game.weapons.ppackages.ProjectilePackage;
import minidoom.util.Constants;
import minidoom.util.Vector2f;

public abstract class Enemy extends Actor {
	public enum EnemyState {
		WAITING, SEARCHING, CHASING, MELEE, RANGED
	}

	protected EnemyState enemyState;
	protected Actor target;
	protected ProjectilePackage rangedPackage;
	protected ProjectilePackage meleePackage;
	protected boolean attackAnimation;
	protected Sound rangedSound;
	protected Sound meleeSound;
	protected Sound alertSound;
	protected float rangedAttackDelay;
	protected float meleeAttackDelay;
	protected float attackDelayTimer;
	protected float timeBetweenAttacks;
	protected int attacksPerAttack;
	protected int attacksToGo;
	protected float meleeRange;
	protected float rangedRange;
	protected float directionChangeTimer;
	protected static int maxSight = 1500 * 1500;

	public Enemy(GameEngine engine, int entityID, String name, Sprite sprite, Team team, int speed,
	             int rotationSpeed, int health, ActorAnimationSet aas, Sound deathSound, Sound hurtSound,
	             Sound alertSound, Sound meleeSound, Sound rangedSound, float rangedAttackDelay, float meleeAttackDelay,
	             int attacksPerAttack, float timeBetweenAttacks, float meleeRange, float rangedRange,
	             ProjectilePackage rangedPackage, ProjectilePackage meleePackage) {
		super(engine, entityID, name, sprite, true, true, true, true, team,
				new Vector2f(), speed, rotationSpeed, health, ActorState.IDLE, null, aas, deathSound, hurtSound);

		this.attackAnimation = false;
		this.enemyState = EnemyState.WAITING;
		this.rangedAttackDelay = rangedAttackDelay;
		this.meleeAttackDelay = meleeAttackDelay;
		this.attackDelayTimer = 0;
		this.attacksToGo = 0;
		this.directionChangeTimer = 0;
		this.attacksPerAttack = attacksPerAttack;
		this.alertSound = alertSound;
		this.rangedSound = rangedSound;
		this.meleeSound = meleeSound;
		this.rangedPackage = rangedPackage;
		this.meleePackage = meleePackage;
		this.meleeRange = meleeRange * meleeRange;
		this.rangedRange = rangedRange * rangedRange;
		this.timeBetweenAttacks = timeBetweenAttacks;
	}

	public void alert(boolean scream) {
		if (enemyState != EnemyState.WAITING && enemyState != EnemyState.SEARCHING)
			return;

		updateVelocity();
		enemyState = EnemyState.CHASING;
		actorState = ActorState.MOVING;
		if (!scream) {
			alertSound.play();
			engine.alertEnemies(sprite.getCenterX(), sprite.getCenterY(), true);
		}
	}

	protected float angleToTarget() {
		float angle;
		if (target != null && target.getActorState() != ActorState.DEAD)
			angle =  (float)(Math.atan2(target.getSprite().getCenterY() - sprite.getCenterY(),
					target.getSprite().getCenterX() - sprite.getCenterX()) * Constants.TO_DEGREES);
		else
			angle = Constants.rand(0, 360);

		return angle > 0 ? angle : angle + 360;
	}

	protected void faceTarget() {
		setRotation(angleToTarget());
	}

	public void updateTarget(Actor actor) {
		target = actor;
	}

	protected void updateAnimation(float dt) {
		animation.update(dt);
		sprite.updateShadowImage(animation.getCurrentImage());
		if (animation.isDead()) {
			faceTarget();
			if (enemyState == EnemyState.MELEE) {
				attackAnimation = false;
				actorState = ActorState.MOVING;
				if (meleeSound != null)
					meleeSound.play();
				sendPackage(meleePackage);
				enemyState = EnemyState.CHASING;
				attackDelayTimer = meleeAttackDelay;
			} else {
				if (rangedSound != null)
					rangedSound.play();
				sendPackage(rangedPackage);
				attacksToGo--;
				if (attacksToGo > 0) {
					animation.resetAnimation();
				} else {
					attackAnimation = false;
					enemyState = EnemyState.CHASING;
					actorState = ActorState.MOVING;
					attackDelayTimer = getRandomAttackDelay();
				}
			}
		}
	}

	protected float getRandomAttackDelay() {
		return timeBetweenAttacks + Constants.rand(-0.5f, 0.5f);
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

		if (directionChangeTimer > 0)
			directionChangeTimer -= dt;
		if (hurtSoundTimer > 0)
			hurtSoundTimer -= dt;
		if (stunDelayTimer > 0)
			stunDelayTimer -= dt;
		if (stunLengthTimer > 0)
			stunLengthTimer -= dt;

		if (stunLengthTimer > 0) {
			updateImage(dt);
			return;
		}

		if (actorState == ActorState.ATTACKING && attackAnimation) {
			updateAnimation(dt);
			if (actorState == ActorState.ATTACKING)
				return;
		}

		if (attackDelayTimer > 0)
			attackDelayTimer -= dt;

		if (attackingTimer > 0) {
			attackingTimer -= dt;
			if (attackingTimer <= 0 && actorState == ActorState.ATTACKING) {
				actorState = ActorState.MOVING;
				enemyState = EnemyState.CHASING;
				attackAnimation = false;
			}
		}

		updateAI(dt);
		if (actorState == ActorState.MOVING) {
			sprite.move(velocity.x * dt * speed, velocity.y * dt * speed);
			if (Math.abs(sprite.getPreviousX() - sprite.getX()) < 0.0
					|| Math.abs(sprite.getPreviousY() - sprite.getY()) < 0.001) {
				sprite.setRotation(Constants.rand(0.0f, 360f));
				updateVelocity();
			}
		}
		updateImage(dt);
	}

	protected void sendPackage(ProjectilePackage pPackage) {
		float spawnPointX = (float) (sprite.getCenterX() + Math.cos(Math.toRadians(getRotation())) * sprite.getHalfWidth());
		float spawnPointY = (float) (sprite.getCenterY() + Math.sin(Math.toRadians(getRotation())) * sprite.getHalfHeight());
		pPackage.setShooter(this);
		pPackage.setSpawnPointX(spawnPointX);
		pPackage.setSpawnPointY(spawnPointY);
		pPackage.setRotation(sprite.getRotation());
		engine.spawnProjectile(pPackage);
	}

	protected boolean facingTarget(float targetAngle) {
		return (180 - Math.abs(Math.abs(getRotation() - targetAngle) - 180)) < 80;
	}

	protected void updateAI(float dt) {
		if (attacksToGo > 0) {
			if (attackDelayTimer <= 0) {
				if (rangedSound != null)
					rangedSound.play();
				faceTarget();
				fired = true;
				sendPackage(rangedPackage);
				attackingTimer = 0.5f;
				attacksToGo--;
				if (attacksToGo > 0)
					attackDelayTimer = rangedAttackDelay;
				else {
					attackDelayTimer = getRandomAttackDelay();
				}
			}
			return;
		}

		int distance = distanceToTarget();

		if (meleePackage != null && target != null && distance <= meleeRange && attackDelayTimer <= 0
				&& actorState != ActorState.ATTACKING && actorState != ActorState.IDLE
				&& target.getActorState() != ActorState.DEAD && target.isVisible()) {
			faceTarget();
			animation.init(actorAnimationSet.getMeleeAttackAnimation(getDirection()), false);
			sprite.updateShadowImage(animation.getCurrentImage());
			attackAnimation = true;
			actorState = ActorState.ATTACKING;
			enemyState = EnemyState.MELEE;
			return;
		}

		if (rangedPackage != null && target != null && distance <= rangedRange && attackDelayTimer <= 0
				&& actorState != ActorState.ATTACKING && actorState != ActorState.IDLE
				&& target.getActorState() != ActorState.DEAD && target.isVisible()) {
			actorState = ActorState.ATTACKING;
			enemyState = EnemyState.RANGED;
			faceTarget();
			if (actorAnimationSet.hasBasicAttack()) {
				attacksToGo = attacksPerAttack - 1;
				sendPackage(rangedPackage);
				fired = true;
				attackingTimer = 0.5f;
				if (attacksToGo > 0)
					attackDelayTimer = rangedAttackDelay;
				else
					attackDelayTimer = getRandomAttackDelay();
			} else {
				attackAnimation = true;
				animation.init(actorAnimationSet.getRangedAttackAnimation(getDirection()), false);
				sprite.updateShadowImage(animation.getCurrentImage());
				attacksToGo = attacksPerAttack;
			}
			return;
		}

		float targetAngle = angleToTarget();

		if (enemyState == EnemyState.WAITING) {
			if (facingTarget(targetAngle) && distance < maxSight) {
				enemyState = EnemyState.SEARCHING;
				actorState = ActorState.MOVING;
			}
			else
				return;
		}

		if (distance > maxSight)
			enemyState = EnemyState.SEARCHING;
		else if (enemyState == EnemyState.SEARCHING && facingTarget(targetAngle))
			enemyState = EnemyState.CHASING;

		if (directionChangeTimer <= 0) {
			directionChangeTimer = 2.0f;
			if (enemyState == EnemyState.SEARCHING) {
				sprite.setRotation(Constants.rand(0.0f, 360f));
				updateVelocity();
			} else if (enemyState == EnemyState.CHASING) {

				if (rangedPackage != null) {
					if (Constants.rand(1, 3) == 3)
						sprite.setRotation(Constants.rand(0.0f, 360f));
					else {
						float angleOffset = Constants.rand(-55.0f, 55.0f);
						sprite.setRotation(angleOffset + targetAngle);
					}
				} else {
					if (Constants.rand(1, 7) == 7)
						sprite.setRotation(Constants.rand(0.0f, 360f));
					else {
						float angleOffset = Constants.rand(-35.0f, 35.0f);
						sprite.setRotation(angleOffset + targetAngle);
					}
				}
				updateVelocity();
			}
			return;
		}

		if (distance < meleeRange && meleePackage == null) {
			float angleOffset = Constants.rand(-35.0f, 35.0f);
			sprite.setRotation(angleOffset + (-1 * targetAngle));
			updateVelocity();
			directionChangeTimer = 2.0f;
			return;
		}
	}

	protected void updateVelocity() {
		double radians = sprite.getRotation() * Constants.TO_RADIANS;
		velocity.x = (float) (Math.cos(radians));
		velocity.y = (float) (Math.sin(radians));
	}

	protected int distanceToTarget() {
		if (target == null)
			return Integer.MAX_VALUE;

		int dx = (int)(Math.abs(sprite.getCenterX() - target.getSprite().getCenterX()));
		int dy = (int)(Math.abs(sprite.getCenterY() - target.getSprite().getCenterY()));
		return dx * dx + dy * dy;
	}

}
