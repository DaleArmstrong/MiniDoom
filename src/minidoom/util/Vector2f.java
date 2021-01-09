package minidoom.util;

/**
 * A vector class with various vector manipulating functions
 */
public class Vector2f {
	public float x;
	public float y;

	public Vector2f() {
		this.x = 0.0f;
		this.y = 0.0f;
	}

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setVector(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/* subtracts one vector from another */
	public Vector2f sub(Vector2f vec2) {
		return new Vector2f(this.x - vec2.x, this.y - vec2.y);
	}

	/* returns the norm of the vector */
	public Vector2f norm() {
		float temp = this.x;
		this.x = this.y;
		this.y = temp * -1;
		return this;
	}

	/* returns the dot product */
	public float dot(Vector2f vec2) {
		return this.x * vec2.x + this.y * vec2.y;
	}

	/* returns the unit vector */
	public Vector2f unit() {
		float magnitude = magnitude();
		x = x / magnitude;
		y = y / magnitude;
		return this;
	}

	/* returns the magnitude of the vector */
	public float magnitude() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public String toString() {
		return "x:" + x + ",y:" + y;
	}
}