package minidoom.entity;

import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.game.animations.AnimationSet;
import minidoom.util.Vector2f;

import java.awt.*;

/**
 * Physical entities have a speed, velocity, team, and can set whether
 * shootable, collidable, and whether effects can affect it.
 */
public abstract class PhysicalEntity extends Entity {
	public enum Team {
		PLAYER, NEUTRAL, WALL
	}

	protected boolean collidable;
	protected boolean shootable;
	protected boolean effectable;
	protected boolean blocksView;
	protected Team team;
	protected Vector2f velocity;
	protected int rotationSpeed;
	protected int speed;

	public PhysicalEntity(GameEngine engine, int entityID, Sprite sprite, boolean visible, boolean collidable, boolean shootable,
	                      boolean effectable, boolean blocksView, Team team, Vector2f velocity, int speed,
	                      int rotationSpeed, AnimationSet as) {
		super(engine, entityID, sprite, visible, as);
		this.collidable = collidable;
		this.shootable = shootable;
		this.effectable = effectable;
		this.team = team;
		this.velocity = velocity;
		this.speed = speed;
		this.rotationSpeed = rotationSpeed;
		this.blocksView = blocksView;
	}

	public void setCollidable(boolean collidable) {	this.collidable = collidable;}
	public void setSpeed(int speed) { this.speed = speed; }
	public void setShootable(boolean shootable) { this.shootable = shootable; }
	public void setTeam(Team team) { this.team = team; }
	public void setVelocity(Vector2f velocity) { this.velocity = velocity; }

	public Team getTeam() { return team; }
	public boolean isCollidable() { return collidable; }
	public boolean isShootable() { return shootable; }
	public boolean isEffectable() { return effectable; }
	public Vector2f getVelocity() { return velocity; }
	public int getSpeed() { return speed; }

	public abstract void onHit(int damage, String attackerName);

}
