package minidoom.game.animations;

import java.awt.image.BufferedImage;

/**
 * An animation holds an animation set along with a timer to control which image the
 * animation is currently on.
 */
public class Animation {
	protected AnimationSet as;
	protected float currentTimer;
	protected int currentImage;
	protected boolean repeats;
	protected boolean dead;

	public Animation() {
		this(null, true);
	}

	public Animation(AnimationSet as, boolean repeats) {
		this.as = as;
		this.currentTimer = 0;
		this.currentImage = 0;
		this.repeats = repeats;
		if (as == null)
			this.dead = true;
		else
			this.dead = false;
	}

	public void init(AnimationSet as, boolean repeats) {
		this.as = as;
		this.currentTimer = 0;
		this.currentImage = 0;
		this.repeats = repeats;
		if (as == null)
			this.dead = true;
		else
			this.dead = false;
	}

	public void setAnimationSet(AnimationSet as) {
		this.as = as;
	}

	public void resetTimer() {
		currentTimer = 0;
	}

	public void resetCurrentImage() {
		currentImage = 0;
	}

	public void resetAnimation() {
		currentTimer = 0;
		currentImage = 0;
		this.dead = false;
	}

	public boolean hasAnimation() {
		return as != null;
	}

	public BufferedImage getCurrentImage() {
		if (as == null)
			return null;

		return as.get(currentImage);
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public void update(float dt) {
		if (as == null || dead)
			return;

		currentTimer += dt;
		if (currentTimer >= as.getDelay()) {
			currentTimer -= as.getDelay();
			currentImage++;
			if (currentImage >= as.size()) {
				if (repeats)
					currentImage = 0;
				else {
					currentImage--;
					dead = true;
				}
			}
		}
	}
}

