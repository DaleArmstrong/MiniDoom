package minidoom;

import minidoom.game.states.StateManager;
import minidoom.game.Window;
import minidoom.game.input.Keyboard;
import minidoom.game.input.Mouse;
import minidoom.util.Constants;
import minidoom.util.Time;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Main game window that keeps track of the FPS, and limits the FPS to 144.
 */
public class Game extends Window implements Runnable {
	private boolean isRunning;
	private Time time;
	private float elapsedTime;
	private float dt;
	private int fpsCounter = 0;
	private float fpsTime = 0;
	private StateManager stateManager;
	private Keyboard keyboard;
	private Mouse mouse;

	protected void init() {
		isRunning = true;
		time = new Time();
		dt = 0;
		elapsedTime = 0;
		stateManager = new StateManager(this);
		keyboard = new Keyboard();
		mouse = new Mouse();
		this.addKeyListener(keyboard);
		this.addMouseListener(mouse);
		this.addMouseWheelListener(mouse);
		requestFocusInWindow(true);
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				new ImageIcon(Objects.requireNonNull(Game.class.getClassLoader()
						.getResource("sprites/reticle.png")))
						.getImage(), new Point(0,0),"reticle"));

	}

	public static void main(String[] args) {
		Thread thread = new Thread(new Game());
		thread.start();
	}

	@Override
	protected void render(Graphics2D graphics) {
		stateManager.render(graphics);
	}

	@Override
	public void run() {
		this.init();

		while (isRunning) {
			dt = time.resetTimeInSeconds();

			fpsTime += dt;
			fpsCounter++;
			if(fpsTime > 1) {
				fpsTime -= 1;
				jFrame.setTitle("MiniDoom - FPS: " + fpsCounter);
				fpsCounter = 0;
			}

			if (dt > Constants.MIN_FPS)
				dt = Constants.MIN_FPS;

			stateManager.update(dt);
			renderFrame();

			elapsedTime = time.getElapsedTimeInMilliseconds();
			if (elapsedTime < Constants.TARGET_FRAME_TIME) {
				try {
					Thread.sleep((int) (Constants.TARGET_FRAME_TIME - elapsedTime));
				} catch (Exception e) {
					System.out.println("Frame limiter error");
				}
			}
		}
		System.exit(0);
	}

	public void stopRunning() {
		isRunning = false;
	}

	public boolean isMousePressed(int mouseCode) {
		return mouse.isMouseKeyPressed(mouseCode);
	}

	public int getMouseWheel() {
		return mouse.getMouseWheel();
	}

	public boolean isKeyPressed(int keyCode) {
		return keyboard.isKeyPressed(keyCode);
	}
}
