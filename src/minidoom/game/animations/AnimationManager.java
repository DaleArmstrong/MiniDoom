package minidoom.game.animations;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to create animations and remove animations when they expire
 */
public class AnimationManager {
	private List<AnimationSprite> animations;
	private List<AnimationSprite> removals;
	private List<AnimationSprite> pool;

	public AnimationManager() {
		this(100);
	}

	public AnimationManager(int poolSize) {
		this.animations = new ArrayList<>();
		this.pool = new ArrayList<>();
		this.removals = new ArrayList<>();

		for (int i = 0; i < poolSize; i++) {
			pool.add(new AnimationSprite());
		}
	}

	public void createParticle(AnimationSet template, float x, float y, float vx, float vy, float rotation, float rotationSpeed) {
		if (!pool.isEmpty()) {
			AnimationSprite animation = pool.get(pool.size() - 1);
			animation.init(template, x, y, vx, vy, rotation, rotationSpeed, false);
			animations.add(animation);
			pool.remove(pool.size() - 1);
		}
	}

	public void update(float dt) {
		for (AnimationSprite animation : animations) {
			animation.update(dt);
			if (animation.isDead())
				removals.add(animation);
		}

		animations.removeAll(removals);
		pool.addAll(removals);
		removals.clear();
	}

	public void render(Graphics2D graphics) {
		for (AnimationSprite animation : animations) {
			animation.render(graphics);
		}
	}
}
