package minidoom.game.animations;

import minidoom.entity.Actor;
import minidoom.util.Constants;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ActorAnimationSet {
	private Map<Actor.ActorDirection, List<BufferedImage>> animations;
	private Map<Actor.ActorDirection, AnimationSet> rangedAttackAnimations;
	private Map<Actor.ActorDirection, AnimationSet> meleeAttackAnimations;
	private int numberOfMoving;
	private int idle;
	private int hurt;
	private int attack;
	private int attackImages;
	private boolean basicAttack;
	private boolean rangedAnimation;
	private boolean meleeAnimation;
	private List<AnimationSet> deathAnimations;
	private AnimationSet explosionAnimation;
	private AnimationSet dyingAnimation;
	private AnimationSet dyingDeadAnimation;

	public ActorAnimationSet() {
		this.animations = new EnumMap<>(Actor.ActorDirection.class);
		this.rangedAttackAnimations = null;
		this.meleeAttackAnimations = null;
		this.deathAnimations = new ArrayList<>();
		this.dyingAnimation = null;
		this.dyingDeadAnimation = null;
		this.explosionAnimation = null;
	}

	public void addAnimationDirection(Actor.ActorDirection direction, List<BufferedImage> animationList) {
		this.animations.put(direction, animationList);
	}

	public void init(int numberOfMoving, boolean basicAttack, boolean ranged, boolean melee, int idle) {
		this.numberOfMoving = numberOfMoving;
		this.idle = idle;
		this.rangedAnimation = ranged;
		this.meleeAnimation = melee;
		this.basicAttack = basicAttack;
		if (basicAttack)
			this.attackImages = 2;
		else
			this.attackImages = 0;

		this.attack = numberOfMoving + attackImages - 1;
		this.hurt = numberOfMoving + attackImages;
	}

	public void addDeathAnimation(AnimationSet death) {
		this.deathAnimations.add(death);
	}

	public void setExplosionAnimation(AnimationSet explosion) {
		this.explosionAnimation = explosion;
	}

	public void setDyingAnimation(AnimationSet dying) {
		this.dyingAnimation = dying;
	}

	public void setDyingDeadAnimation(AnimationSet dyingDead) {
		this.dyingDeadAnimation = dyingDead;
	}

	public void setRangedAttackAnimations(Map<Actor.ActorDirection, AnimationSet> rangedAnimations) {
		this.rangedAttackAnimations = rangedAnimations;
	}

	public void setMeleeAttackAnimations(Map<Actor.ActorDirection, AnimationSet> meleeAnimations) {
		this.meleeAttackAnimations = meleeAnimations;
	}

	public int getIdleFrameNumber() { return idle; }

	public BufferedImage getIdle(Actor.ActorDirection direction) { return animations.get(direction).get(idle); }
	public BufferedImage getHurt(Actor.ActorDirection direction) { return animations.get(direction).get(hurt); }

	public BufferedImage getAttack(Actor.ActorDirection direction) {
		if (rangedAnimation)
			return null;

		return animations.get(direction).get(attack);
	}

	public BufferedImage getAttackIdle(Actor.ActorDirection direction) {
		if (rangedAnimation)
			return null;

		return animations.get(direction).get(attack - 1);
	}

	public BufferedImage getMovement(Actor.ActorDirection direction, int frame) {
		return animations.get(direction).get(frame);
	}

	public int getNumberOfMoving() { return numberOfMoving; }

	public AnimationSet getRandomDeathAnimation() {
		return deathAnimations.get(Constants.rand(0, deathAnimations.size() - 1));
	}

	public AnimationSet getExplosionAnimation() { return explosionAnimation; }
	public AnimationSet getDyingAnimation() { return dyingAnimation; }
	public AnimationSet getDyingDeadAnimation() { return dyingDeadAnimation; }

	public boolean hasRangedAttackAnimation() { return rangedAnimation; }
	public boolean hasMeleeAttackAnimation() { return meleeAnimation; }
	public boolean hasExplosionAnimation() { return explosionAnimation != null; }
	public boolean hasDyingAnimation() { return dyingAnimation != null; }
	public boolean hasBasicAttack() { return basicAttack; }

	public AnimationSet getRangedAttackAnimation(Actor.ActorDirection direction) {
		return rangedAttackAnimations.get(direction);
	}

	public AnimationSet getMeleeAttackAnimation(Actor.ActorDirection direction) {
		return meleeAttackAnimations.get(direction);
	}
}
