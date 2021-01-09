package minidoom.game;

import minidoom.Game;
import minidoom.entity.Player;
import minidoom.entity.components.Sprite;
import minidoom.util.Constants;

import java.awt.*;

/**
 * Camera is used to follow a player around the map. It will stop once a player gets near
 * a boundary, so that the game does not render past the map.
 *
 * It will also be used to minimize the amount of draw calls, as the only things needed to be
 * drawn are those shown by the camera. Currently this is not implemented
 * in the tank game, because of the minimap requiring the draw calls for all items.
 */
public class Camera {
	private Game window;
	private Player player;
	private Sprite entity;
	private float x;
	private float y;
	private float differenceX;
	private float differenceY;
	private int cameraWidth;
	private int cameraHeight;
	private float halfWidth;
	private float halfHeight;
	private int mapWidth;
	private int mapHeight;
	private Rectangle bounds;

	public Camera(Game window, Player player, int cameraWidth, int cameraHeight, int mapWidth, int mapHeight) {
		this.window = window;
		this.player = player;
		this.entity = player.getSprite();
		this.cameraWidth = cameraWidth;
		this.cameraHeight = cameraHeight;
		this.halfWidth = cameraWidth / 2.0f;
		this.halfHeight = cameraHeight / 2.0f;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.bounds = new Rectangle();
		update();
	}

	public Rectangle getBounds() {
		bounds.setBounds(Math.round(x), Math.round(y), cameraWidth, cameraHeight);
		return bounds;
	}

	public void update() {
		float entityX = entity.getCenterX();
		float entityY = entity.getCenterY();
		if (entityX - halfWidth <= 0.0f) {
			x = 0.0f;
		} else if (entityX + halfWidth > mapWidth) {
			x = mapWidth - cameraWidth;
		} else {
			x = entityX - halfWidth;
		}

		if (entityY - halfHeight < 0.0f) {
			y = 0.0f;
		} else if (entityY + halfHeight > mapHeight) {
			y = mapHeight - cameraHeight;
		} else {
			y = entityY - halfHeight;
		}

		player.setRelativeToScreen(x, y, (float)window.getWidth() / cameraWidth, (float)window.getHeight() / cameraHeight);
	}

	public void setTarget (Sprite entity) {
		this.entity = entity;
	}
}
