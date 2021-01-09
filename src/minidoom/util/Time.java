package minidoom.util;

/**
 * Time utility class to keep track of elapsed time. Time in seconds and
 * milliseconds, will return the value as float. Nano seconds will be returned as a long.
 */
public class Time {
	private long timeStart;
	private static final double secondsConversion = 1.0E-9;
	private static final double millisecondsConversion = 1.0E-6;

	public Time() {
		this.timeStart = System.nanoTime();
	}

	/**
	 * Copy constructor - copies the given object's starting time from the
	 * last time resetTime() was called
	 * @param clone the object to copy
	 */
	public Time(Time clone) {
		this.timeStart = clone.timeStart;
	}

	/**
	 * Return the elapsed time from the last time resetTime()
	 * was called.
	 * @return time elapsed in nano seconds as a long
	 */
	public long getElapsedTime() {
		return System.nanoTime() - timeStart;
	}

	/**
	 * Return the elapsed time from the last time resetTime()
	 * was called.
	 * @return time elapsed in seconds as a float
	 */
	public float getElapsedTimeInSeconds() {
		return (float)(getElapsedTime() * secondsConversion);
	}
	/**
	 * Return the elapsed time from the last time resetTime()
	 * was called.
	 * @return time elapsed in milliseconds as a float
	 */
	public float getElapsedTimeInMilliseconds() {
		return (float)(getElapsedTime() * millisecondsConversion);
	}

	/**
	 * Return the elapsed time from the last time resetTime()
	 * was called. Set timeStart to current Time. Used to keep
	 * updating delta time between frames.
	 * @return time elapsed in nano seconds as a long
	 */
	public long resetTime() {
		long previousTime = timeStart;
		timeStart = System.nanoTime();
		return timeStart - previousTime;
	}

	/**
	 * Return the elapsed time from the last time resetTime()
	 * was called. Set timeStart to current Time. Used to keep
	 * updating delta time between frames.
	 * @return time elapsed in seconds as a float
	 */
	public float resetTimeInSeconds() {
		return (float)(resetTime() * secondsConversion);
	}

	/**
	 * Return the elapsed time from the last time resetTime()
	 * was called. Set timeStart to current Time. Used to keep
	 * updating delta time between frames.
	 * @return time elapsed in milliseconds as a float
	 */
	public float resetTimeInMilliseconds() {
		return (float)(resetTime() * millisecondsConversion);
	}
}
