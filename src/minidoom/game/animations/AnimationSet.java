package minidoom.game.animations;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * An animation set holes the images of an animation and the delay between images
 */
public class AnimationSet {
	private List<BufferedImage> images;
	private float delay;

	public AnimationSet(List<BufferedImage> images, float delay) {
		this.images = images;
		this.delay = delay;
	}

	public BufferedImage get(int index) {
		return images.get(index);
	}

	public float getDelay() {
		return delay;
	}

	public int size() {
		return images.size();
	}

}
