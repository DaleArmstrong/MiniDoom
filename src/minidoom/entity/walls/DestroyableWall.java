package minidoom.entity.walls;

import minidoom.entity.PhysicalEntity;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.util.Vector2f;

/**
 * Destroyable wall that accepts a tileName for different image displays
 */
public class DestroyableWall extends PhysicalEntity {
	private int health;

	public DestroyableWall(GameEngine engine, String tileName, int entityID, float x, float y) {
		super(engine, entityID, new Sprite(tileName, x, y), true, true, true, false,
				false, Team.WALL,	new Vector2f(), 0, 0, null);

		this.health = 20;
	}

	@Override
	public void onHit(int damage, String name) {
		health -= damage;
		if (health <= 0) {
			health = 0;
			kill();
		}
	}


	@Override
	public void update(float dt) {

	}

	@Override
	public void kill() {
		engine.addWallRemoval(this);
	}

}
