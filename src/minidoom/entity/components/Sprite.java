package minidoom.entity.components;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import minidoom.game.managers.*;
import minidoom.util.Constants;
import minidoom.util.Vector2f;


/**
 * Sprite class that contains the image of the entities. It also holds
 * the transform information, x, y, rotation, width, height. Automatically
 * will request from the SpriteManager a sprite of a given name.
 */
public class Sprite {
	private BufferedImage image = null;
	AffineTransform transform;
	private float x;
	private float previousX;
	private float y;
	private float previousY;
	private int width;
	private int height;
	private float halfWidth;
	private float halfHeight;
	private float rotation;
	private float offsetX;
	private float offsetY;
	private float inAirOffset;
	private boolean rotates;
	static Color shadowColor = new Color(1,1,1, 45);

	public Sprite() {
	}

	public Sprite(BufferedImage image, float x, float y, int width, int height,
	              float rotation, boolean rotates) {
		init(x, y, image, width, height, rotation, rotates);
	}

	public Sprite(String name, float x, float y, int width,
	              int height, float rotation, boolean rotates){
		init(name, x, y, width, height, rotation, rotates);
	}

	public Sprite(String name) {
		init(name, 0, 0, 0, 0, 0, false);
	}

	public Sprite(String name, float x, float y) {
		init(name, x, y, 0, 0, 0, false);
	}

	public Sprite(String name, float x, float y, int rotation) {
		init(name, x, y, 0, 0, rotation, false);
	}

	public Sprite(BufferedImage image, float x, float y) {
		init(x, y, image, 0, 0, 0, false);
	}

	public void init(float x, float y, BufferedImage image, int width, int height,
	                 float rotation, boolean rotates) {
		this.image = image;
		transform = new AffineTransform();
		this.x = x;
		this.previousX = x;
		this.y = y;
		this.previousY = y;

		if (width < 0.1f) {
			if (image != null)
				this.width = image.getWidth(null);
			else
				this.width = 0;
		} else
			this.width = width;

		if (height < 0.1f) {
			if (image != null)
				this.height = image.getHeight(null);
			else
				this.height = 0;
		} else
			this.height = height;

		this.rotation = rotation;
		this.offsetX = 0;
		this.offsetY = 0;
		this.inAirOffset = 0;
		this.rotates = rotates;
		calculateHalfDimensions();
	}

	public void init(String name, float x, float y, int width,
	                 int height, float rotation, boolean rotates) {
		init(x, y, SpriteManager.getSprite(name), width, height, rotation, rotates);
	}

	public void calculateHalfDimensions() {
		halfHeight = height * Constants.HALF_CONVERSION;
		halfWidth = width * Constants.HALF_CONVERSION;
	}

	public void clear() {
		image = null;
		x = 0;
		y = 0;
		width = 0;
		height = 0;
		halfWidth = 0;
		halfHeight = 0;
		rotation = 0;
		offsetX = 0;
		offsetY = 0;
		inAirOffset = 0;
		rotates = false;
	}

	public void move(float x, float y) {
		this.previousX = this.x;
		this.previousY = this.y;
		this.x += x;
		this.y += y;
	}

	public void move(Vector2f movement) {
		this.previousX = this.x;
		this.previousY = this.y;
		this.x += movement.x;
		this.y += movement.y;
	}

	public void rotate(float angle) {
		rotation += angle;
		if (rotation < 0)
			rotation += 360;
		else if (rotation >= 360)
			rotation %= 360;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setPosition(Vector2f position) {
		this.x = position.x;
		this.y = position.y;
	}

	public void updatePosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void updateShadowImage(BufferedImage image) {
		this.offsetX = (image.getWidth() - width) * Constants.HALF_CONVERSION;
		this.offsetY = (image.getHeight() - height);
		this.image = image;
	}

	public float getOffsetX() { return offsetX; }
	public float getOffsetY() { return offsetY; }

	public void updateImage(BufferedImage image) {
		this.offsetX = (image.getWidth() - width) * Constants.HALF_CONVERSION;
		this.offsetY = (image.getHeight() - height) * Constants.HALF_CONVERSION;
		this.image = image;
	}

	public void updateImage(BufferedImage image, int offsetX, int offsetY) {
		updateImage(image);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public void setHeight(int height) {
		this.height = height;
		calculateHalfDimensions();
	}

	public void setWidth(int width) {
		this.width = width;
		calculateHalfDimensions();
	}

	public void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
		calculateHalfDimensions();
	}

	public void setImage(BufferedImage image) { this.image = image; }
	public void setInAirOffset(float offset) { this.inAirOffset = offset; }
	public void setRotates(boolean rotates) { this.rotates = rotates; }

	public void setRotation(float rotation) {
		if (rotation < 0)
			rotation += 360;
		else if (rotation >= 360)
			rotation %= 360;

		this.rotation = rotation;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getCenterX() { return x + halfWidth; }
	public float getCenterY() { return y + halfHeight; }

	/* Used to find the shadow circle generated by the entity */
	public Shape getShadowCircle() {
		Ellipse2D.Float ellipse = new Ellipse2D.Float(0,0, width, width);
		transform.setToIdentity();
		transform.translate(x, y + height - halfWidth);

		return transform.createTransformedShape(ellipse);
	}

	/* returns a transformed rectangle used in hit box calculations */
	public Shape getHitBoxBounds() {
		Rectangle2D rect = new Rectangle2D.Float(x, y, width, height);
		transform.setToIdentity();

		if (rotates)
			transform.rotate(Math.toRadians(rotation), x + halfWidth, y + halfHeight);

		return transform.createTransformedShape(rect);
	}

	public void render(Graphics2D graphics) {
		if (image == null)
			return;

		transform.setToIdentity();
		transform.translate(x - offsetX, y - offsetY - inAirOffset);
		if (rotates)
			transform.rotate(Math.toRadians(rotation), image.getWidth() * Constants.HALF_CONVERSION,
					image.getHeight() * Constants.HALF_CONVERSION);

		graphics.drawImage(image, transform, null);
	}

	public void renderShadow(Graphics2D graphics) {
		if (image == null)
			return;

		graphics.setColor(shadowColor);
		graphics.fillOval((int)x + 2, (int)(y + height - halfWidth), width - 4, width);
	}

	/* Rotated points utilized in the Spatial Axis Theorem */
	public Vector2f[] getRotatedPoints() {
		Vector2f[] points = new Vector2f[4];
		float angle = (float) Math.toRadians(rotation);
		float cX = getCenterX();
		float cY = getCenterY();
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		points[0] = new Vector2f(cos * (x - cX) - sin * (y - cY) + cX, sin * (x - cX) + cos * (y - cY) + cY);
		points[1] = new Vector2f(cos * (x + width - cX) - sin * (y - cY) + cX,
				sin * (x + width - cX) + cos * (y - cY) + cY);
		points[2] = new Vector2f(cos * (x + width - cX) - sin * (y + height - cY) + cX,
				sin * (x + width - cX) + cos * (y + height - cY) + cY);
		points[3] = new Vector2f(cos * (x - cX) - sin * (y + height - cY) + cX,
				sin * (x - cX) + cos * (y + height - cY) + cY);

		return points;
	}

	public boolean rotates() { return rotates; }
	public float getHalfHeight() { return halfHeight; }
	public float getHalfWidth() { return halfWidth; }
	public int getHeight() { return height; }
	public BufferedImage getImage() { return image; }
	public Vector2f getPosition() { return new Vector2f(x, y); }
	public float getRotation() { return rotation; }
	public int getWidth() { return width; }
	public float getX() { return x; }
	public float getY() { return y; }
	public float getPreviousX() { return previousX; }
	public float getPreviousY() { return previousY; }

	public String toString() {
		return "[x:" + x + ",y:" + y + ",width:" + width + ",height:" + height + "]";
	}
}
