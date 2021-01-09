package minidoom.game.animations;

import minidoom.entity.components.Sprite;

import java.awt.*;

/**
 * Animation tied to a sprite with velocity and rotation
 */
public class AnimationSprite extends Animation {
	private Sprite sprite;
	protected float vx;
	protected float vy;
	protected float rotationSpeed;

	public AnimationSprite() {
		this.sprite = new Sprite();
	}

	public void init(AnimationSet as, float x, float y, float vx, float vy, float rotation, float rotationSpeed,
	                 boolean repeats) {
		super.init(as, repeats);
		this.vx = vx;
		this.vy = vy;
		this.rotationSpeed = rotationSpeed;
		this.dead = false;
		this.sprite.setRotation(rotation);
		this.sprite.init(x - as.get(0).getWidth() / 2.0f, y - as.get(0).getHeight() / 2.0f, as.get(0),
				0,0, rotation, true);
	}

	public float getX() {
		return sprite.getX();
	}

	public float getY() {
		return sprite.getY();
	}

	public float getWidth() {
		return sprite.getWidth();
	}

	public float getHeight() {
		return sprite.getHeight();
	}

	@Override
	public void update(float dt) {
		sprite.move(vx * dt, vy * dt);
		sprite.rotate(rotationSpeed * dt);

		currentTimer += dt;
		if (currentTimer >= as.getDelay()) {
			currentTimer -= as.getDelay();
			currentImage++;
			if (currentImage >= as.size()) {
				if (repeats) {
					currentImage = 0;
					sprite.updateImage(as.get(currentImage));
				} else
					dead = true;
			} else {
				sprite.updateImage(as.get(currentImage));
			}
		}
	}

	public void render(Graphics2D graphics) {
		if (!isDead()) {
			sprite.render(graphics);
		}
	}
}
