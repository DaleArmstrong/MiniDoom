package minidoom.entity.walls;

import minidoom.entity.PhysicalEntity;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.util.Vector2f;

/**
 * Invincible wall that accepts a tileName for different image displays
 */
public class InvincibleWall extends PhysicalEntity {


	public InvincibleWall(GameEngine engine, String tileName, int entityID, float x, float y) {
		super(engine, entityID, new Sprite(tileName, x, y), false, true, true, false,
				true, Team.WALL, new Vector2f(), 0, 0, null);
	}

	@Override
	public void onHit(int damage, String name) {

	}

	@Override
	public void update(float dt) {

	}

	@Override
	public void kill() {

	}
}
