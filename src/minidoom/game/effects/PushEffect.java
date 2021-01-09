package minidoom.game.effects;

import minidoom.entity.Entity;
import minidoom.game.GameEngine;
import minidoom.util.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * Push effect used mainly for explosions. Holds the list of entities and their
 * push direction. Every update, pushes the entity in that direction.
 */
public class PushEffect {
	public class PushContainer {
		protected Entity entity;
		protected Vector2f velocity;
		protected float maxTime;
		protected float currentTime;

		PushContainer(Entity entity, Vector2f velocity, float maxTime) {
			this.entity = entity;
			this.velocity = velocity;
			this.maxTime = maxTime;
			currentTime = 0;
		}
	}

	private GameEngine engine;
	private List<PushContainer> containers;
	private List<PushContainer> removals;

	public PushEffect(GameEngine engine) {
		this.engine = engine;
		this.containers = new ArrayList<>();
		this.removals = new ArrayList<>();
	}

	/**
	 * Adds an entity to be pushed
	 * @param entity the entity to be pushed
	 * @param pointX the x direction to be pushed from
	 * @param pointY the y direction to be pushed from
	 * @param speed the speed to be pushed
	 * @param maxTime the amount of time to be pushed
	 */
	public void addEntity(Entity entity, float pointX, float pointY, float speed, float maxTime) {
		double angle = Math.atan2(entity.getSprite().getY() - pointY, entity.getSprite().getX() - pointX);
		Vector2f newVector = new Vector2f((float)Math.cos(angle) * speed, (float)Math.sin(angle) * speed);
		containers.add(new PushContainer(entity, newVector, maxTime));
	}

	public void remove(Entity entity) {
		for (PushContainer container : containers) {
			if (container.entity.getEntityID() == entity.getEntityID())
				removals.add(container);
		}
		containers.removeAll(removals);
		removals.clear();
	}

	public void update(float dt) {
		for (PushContainer container : containers) {
			container.currentTime += dt;
			if (container.currentTime < container.maxTime) {
				container.entity.move(container.velocity.x * dt, container.velocity.y * dt);
				engine.checkMovedEntityCollision(container.entity);
			}
			else
				removals.add(container);
		}

		containers.removeAll(removals);
		removals.clear();
	}
}
