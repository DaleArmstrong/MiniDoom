package minidoom.entity;

import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.game.animations.Animation;
import minidoom.game.animations.AnimationSet;
import minidoom.util.Vector2f;

import java.awt.*;

/**
 * Key abstract class used to create all entities. Contains the sprite,
 * entity ID, and whether the entity is visible. Contains many wrapper functions
 * for the sprite class. All entities have a kill function to allow for removal
 * of the entity and execute any methods that happen when killed.
 */
public abstract class Entity {
	protected GameEngine engine;
	protected int entityID;
	protected Sprite sprite;
	protected boolean visible;
	protected Animation animation;

	public Entity(GameEngine engine, int entityID, Sprite sprite, boolean visible, AnimationSet as) {
		this.engine = engine;
		this.entityID = entityID;
		this.sprite = sprite;
		this.visible = visible;
		this.animation = new Animation(as, true);
	}

	public void setEntityID(int entityID) { this.entityID = entityID; }
	public void setSprite(Sprite sprite) { this.sprite = sprite; }
	public void setVisible(boolean visible) { this.visible = visible; }

	public void setRotation(float angle) { sprite.setRotation(angle); }
	public void setPosition(float x, float y) { sprite.setPosition(x, y); }
	public void setPosition(Vector2f position) { sprite.setPosition(position); }
	public void setSpriteRotates(boolean rotates) { sprite.setRotates(rotates); }
	public void setX(float x) { this.sprite.setX(x); }
	public void setY(float y) { this.sprite.setY(y); }

	public void move(float x, float y) { sprite.move(x, y); }
	public void move(Vector2f position) { sprite.move(position); }

	public int getEntityID() { return entityID; }
	public Sprite getSprite() { return sprite; }
	public float getRotation() { return sprite.getRotation(); }
	public Vector2f getPosition() { return sprite.getPosition(); }
	public boolean isVisible() { return visible; }
	public float getX() { return sprite.getX(); }
	public float getY() { return sprite.getY(); }
	public float getWidth() { return sprite.getWidth(); }
	public float getHeight() { return sprite.getHeight(); }

	public int hashCode() { return entityID; }

	public abstract void update(float dt);
	public abstract void kill();

	public void render(Graphics2D graphics) {
		if (visible)
			sprite.render(graphics);
	}

	public void renderShadow(Graphics2D graphics) {
		if (visible)
			sprite.renderShadow(graphics);
	}

	public String toString() {
		return "[id:" + entityID + ", x:" + getX() + ", y:" + getY() + ", angle:" + getRotation() + "]";
	}
}
