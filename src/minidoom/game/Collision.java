package minidoom.game;

import minidoom.entity.Entity;
import minidoom.entity.components.Sprite;
import minidoom.util.Vector2f;

/**
 * This class is used to check collision between various types of shapes. Each function can be
 * told to handle the collision or only return whether a collision has occured. Will also
 * check the map bounds for entities.
 */
public class Collision {
	private int mapWidth;
	private int mapHeight;

	public Collision(int mapWidth, int mapHeight) {
		this.mapHeight = mapHeight;
		this.mapWidth = mapWidth;
	}

	public void setMapDimensions(int mapWidth, int mapHeight) {
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
	}

	public boolean checkProjectileMapBounds(Entity entity) {
		Sprite sprite = entity.getSprite();
		return sprite.getCenterX() < -50 || sprite.getCenterY() < -50
				|| sprite.getCenterX() > mapWidth + 50 || sprite.getCenterY() > mapHeight + 50;
	}

	/* Checks whether the entity is past the map bounds */
	public boolean checkMapBounds(Entity entity, boolean handleCollisions, boolean shadowBounds) {
		Sprite sprite = entity.getSprite();
		boolean collision = false;
		if (sprite.getX() < 0) {
			if (handleCollisions) {
				collision = true;
				sprite.setX(0);
			} else
				return true;
		} else if (sprite.getX() + sprite.getWidth() > mapWidth) {
			if (handleCollisions) {
				collision = true;
				sprite.setX(mapWidth - sprite.getWidth());
			} else
				return true;
		}

		if (shadowBounds) {
			if (sprite.getY() + sprite.getHeight() - sprite.getHalfWidth() < 0) {
				if (handleCollisions) {
					collision = true;
					sprite.setY(-sprite.getHeight() + sprite.getHalfWidth());
				} else
					return true;
			} else if (sprite.getY() + sprite.getHeight() + sprite.getHalfWidth() > mapHeight) {
				if (handleCollisions) {
					collision = true;
					sprite.setY(mapHeight - sprite.getHalfWidth() - sprite.getHeight());
				} else
					return true;
			}
		} else {
			if (sprite.getY() < 0) {
				if (handleCollisions) {
					collision = true;
					sprite.setY(0);
				} else
					return true;
			} else if (sprite.getY() + sprite.getHeight() > mapHeight) {
				if (handleCollisions) {
					collision = true;
					sprite.setY(mapHeight - sprite.getHeight());
				} else
					return true;
			}
		}
		return collision;
	}

	/* Checks whether two circles are colliding */
	public boolean checkCircleBounds(Entity entity1, Entity entity2, boolean handleCollision) {
		Sprite sprite1 = entity1.getSprite();
		Sprite sprite2 = entity2.getSprite();

		float distX = sprite1.getCenterX() - sprite2.getCenterX();
		float distY = (sprite1.getY() + sprite1.getHeight()) - (sprite2.getY() + sprite2.getHeight());
		float radiusSum = sprite1.getHalfWidth() + sprite2.getHalfWidth();
		float distSqr = distX * distX + distY * distY;

		if (radiusSum * radiusSum >= distSqr) {
			if (handleCollision) {
				double distance = Math.sqrt(distSqr);
				double unitVectorX = distX / distance;
				double unitVectorY = distY / distance;
				sprite1.setX((float) (sprite2.getCenterX() - sprite1.getHalfWidth() + ((radiusSum + 0.1) * unitVectorX)));
				sprite1.setY((float) (sprite2.getY() + sprite2.getHeight() - sprite1.getHeight() + ((radiusSum + 0.1) * unitVectorY)));
			}
			return true;
		}
		return false;
	}

	/* Checks whether two boxes are colliding */
	public boolean checkBoxBounds(Entity entity1, Entity entity2, boolean handleCollision) {
		Sprite sprite1 = entity1.getSprite();
		Sprite sprite2 = entity2.getSprite();

		if (sprite1.rotates() && !handleCollision) {
			return checkRotatedBoxes(sprite1, sprite2);
		}

		if (sprite1.getX() > sprite2.getX() + sprite2.getWidth()
				|| sprite1.getX() + sprite1.getWidth() < sprite2.getX()
				|| sprite1.getY() + sprite1.getHeight() < sprite2.getY()
				|| sprite1.getY() > sprite2.getY() + sprite2.getHeight()) {
			return false;
		} else {
			if (handleCollision) {
				if (sprite1.getX() <= sprite2.getX() + sprite2.getWidth()
						&& sprite1.getPreviousX() > sprite2.getX() + sprite2.getWidth()) {
					sprite1.setX(sprite2.getX() + sprite2.getWidth() + 0.1f);
				} else if (sprite1.getX() + sprite1.getWidth() >= sprite2.getX()
						&& sprite1.getPreviousX() + sprite1.getWidth() < sprite2.getX()) {
					sprite1.setX(sprite2.getX() - sprite1.getWidth() - 0.1f);
				} else if (sprite1.getY() <= sprite2.getY() + sprite2.getHeight()
						&& sprite1.getPreviousY() > sprite2.getY() + sprite2.getHeight()) {
					sprite1.setY(sprite2.getY() + sprite2.getHeight() + 0.1f);
				} else {
					sprite1.setY(sprite2.getY() - sprite1.getHeight() - 0.1f);
				}
			}
			return true;
		}
	}


	/**
	 * http://www.dyn4j.org/2010/01/sat/#sat-algo
	 * Separating Axis Theorem
	 * Get the points of the shape, project it onto axes, find out if there is overlap on *all* axes. If
	 * there is one axis that does not overlap, then there is a separating axis and that means the two shapes do not
	 * intersect.
	 * Collision Resolution does not work. Collision check is OK!
	 * @param sprite1 entity1 sprite
	 * @param sprite2 entity2 sprite
	 * @return whether there was a collision
	 */
	private boolean checkRotatedBoxes(Sprite sprite1, Sprite sprite2) {
		Vector2f[] points1 = sprite1.getRotatedPoints();
		Vector2f[] points2 = sprite2.getRotatedPoints();

		Vector2f[] axes1 = getAxes(points1);
		Vector2f[] axes2 = getAxes(points2);
		Vector2f project1;
		Vector2f project2;

		for (Vector2f axis : axes1) {
			project1 = project(axis, points1);
			project2 = project(axis, points2);
			if (noOverlap(project1, project2)) {
				return false;
			}
		}
		for (Vector2f axis : axes2) {
			project1 = project(axis, points1);
			project2 = project(axis, points2);
			if (noOverlap(project1, project2)) {
				return false;
			}
		}
		return true;
	}

	/* Axes used in the Separating Axis Theorem */
	private Vector2f[] getAxes(Vector2f[] points) {
		Vector2f[] axes = new Vector2f[points.length];
		for (int i = 0; i < points.length; i++) {
			axes[i] = points[i].sub(points[i + 1 == points.length ? 0 : i + 1]);
			axes[i].norm();
		}
		return axes;
	}

	/* Projections used in the Separating Axis Theorem */
	private Vector2f project(Vector2f axis, Vector2f[] points) {
		float min = axis.dot(points[0]);
		float max = min;
		for (int i = 1; i < points.length; i++) {
			float p = axis.dot(points[i]);
			if (p < min)
				min = p;
			else if (p > max)
				max = p;
		}
		return new Vector2f(min, max);
	}

	private boolean noOverlap(Vector2f project1, Vector2f project2) {
		return project1.y < project2.x || project2.y < project1.x;
	}

	/* Check the collision between a circle and a Box */
	public boolean checkCircleBox(float x, float y, float radius, Entity entity2) {
		Sprite sprite2 = entity2.getSprite();
		float distX = Math.abs(x - sprite2.getCenterX());
		float distY = Math.abs(y - sprite2.getCenterY());

		if (distX > (sprite2.getHalfWidth() + radius) || distY > (sprite2.getHalfHeight() + radius))
			return false;

		if (distX <= sprite2.getHalfWidth() || distY <= sprite2.getHalfHeight())
			return true;

		float dx = distX - sprite2.getHalfWidth();
		float dy = distY - sprite2.getHalfHeight();
		return (dx * dx + dy * dy <= radius * radius);
	}

	/* Check the collision between the shadow of an entity and a box, mainly walls */
	public boolean checkShadowBox(Entity entity1, Entity entity2, boolean handleCollision) {
		Sprite sprite1 = entity1.getSprite();
		Sprite sprite2 = entity2.getSprite();
		if (sprite1.getX() > sprite2.getX() + sprite2.getWidth()
				|| sprite1.getX() + sprite1.getWidth() < sprite2.getX()
				|| sprite1.getY() + sprite1.getHeight() + sprite1.getHalfWidth() < sprite2.getY()
				|| sprite1.getY() + sprite1.getHeight() - sprite1.getHalfWidth() > sprite2.getY() + sprite2.getHeight()) {
			return false;
		} else {
			if(handleCollision)
				handleShadowBox(sprite1, sprite2);
			return true;
		}


		/*  //Correct detection of circle-box collision, however, I can not get the
			//collision resolution to work correctly. Switching to a rectangle-rectangle collision.
		float distX = Math.abs(sprite1.getCenterX() - sprite2.getCenterX());
		float distY = Math.abs(sprite1.getY() + sprite1.getHeight() - sprite2.getCenterY());

		if (distX > (sprite2.getHalfWidth() + sprite1.getHalfWidth())
				|| distY > (sprite2.getHalfHeight() + sprite1.getHalfWidth())) {
			return false;
		}

		if (distX <= sprite2.getHalfWidth() || distY <= sprite2.getHalfHeight()) {
			if (handleCollision)
				handleShadowBox(sprite1, sprite2);
			return true;
		}

		float dx = distX - sprite2.getHalfWidth();
		float dy = distY - sprite2.getHalfHeight();
		if (dx * dx + dy * dy <= sprite1.getHalfWidth() * sprite1.getHalfWidth()) {
			if (handleCollision)
				handleShadowBox(sprite1, sprite2);
			return true;
		}

		return false;
		*/
	}

	/* used to handle collision between shadows and boxes */
	private void handleShadowBox(Sprite sprite1, Sprite sprite2) {
		if (sprite1.getX() <= sprite2.getX() + sprite2.getWidth()
				&& sprite1.getPreviousX() > sprite2.getX() + sprite2.getWidth()) {
			sprite1.setX(sprite2.getX() + sprite2.getWidth() + 0.1f);
		} else if (sprite1.getX() + sprite1.getWidth() >= sprite2.getX()
				&& sprite1.getPreviousX() + sprite1.getWidth() < sprite2.getX()) {
			sprite1.setX(sprite2.getX() - sprite1.getWidth() - 0.1f);
		} else if (sprite1.getY() + sprite1.getHeight() - sprite1.getHalfWidth() <= sprite2.getY() + sprite2.getHeight()
				&& sprite1.getPreviousY() + sprite1.getHeight() - sprite1.getHalfWidth() > sprite2.getY() + sprite2.getHeight()) {
			sprite1.setY(sprite2.getY() + sprite2.getHeight() - sprite1.getHeight() + sprite1.getHalfWidth() + 0.1f);
		} else {
			sprite1.setY(sprite2.getY() - sprite1.getHeight() - sprite1.getHalfWidth() - 0.1f);
		}
	}

}
