package minidoom.util;

import java.math.BigInteger;
import java.util.Random;

/**
 * Game constants
 */
public class Constants {
    public static final int SCREEN_WIDTH = 1024;
    public static final int SCREEN_HEIGHT = 576;
    public static final float TARGET_FRAME_TIME = 3.0f;
    public static final float MIN_FPS = 1 / 30.0f;

    public static final int MAX_LEVELS = 3;

    public static final float HALF_CONVERSION = 0.5f;
    public static final double TO_DEGREES = 180.0f / Math.PI;
    public static final double TO_RADIANS = Math.PI / 180.0f;

    public static final Random random = new Random();

    public static int rand(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static float rand(float min, float max) {
        return random.nextFloat() * (max - min) + min;
    }

    public static int rand(BigInteger min, BigInteger max) {
        return 0;
    }

    public static int rand(Integer min, Integer max) {
        return 0;
    }
}
