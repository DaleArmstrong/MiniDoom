package minidoom.game;

import minidoom.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public abstract class Window extends Canvas{
	protected JFrame jFrame;
	protected BufferStrategy bufferStrategy;
	protected Graphics2D graphics;

	/**
	 * Set up and initialize game window
	 */
	public Window() {
		jFrame = new JFrame();
		jFrame.setTitle("MiniDoom");
		jFrame.setSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		jFrame.setVisible(true);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setLocationRelativeTo(null);
		jFrame.setResizable(true);
		jFrame.setIgnoreRepaint(true);
		this.setIgnoreRepaint(true);
		this.setPreferredSize(new Dimension(jFrame.getWidth(), jFrame.getHeight()));
		this.setBackground(Color.BLACK);
		jFrame.add(this);
		jFrame.setExtendedState(Frame.MAXIMIZED_BOTH);

		this.createBufferStrategy(2);
		bufferStrategy = this.getBufferStrategy();
	}

	protected abstract void render(Graphics2D graphics);

	/**
	 * Render the frame. Concept to render and handle lost images taken from
	 * https://docs.oracle.com/javase/7/docs/api/java/awt/image/BufferStrategy.html
	 */
	protected void renderFrame() {
		do {
			do {
				graphics = (Graphics2D)bufferStrategy.getDrawGraphics();
				graphics.clearRect(0, 0, getWidth(), getHeight());

				render(graphics);
				graphics.dispose();
			} while (bufferStrategy.contentsRestored());
			bufferStrategy.show();
		} while (bufferStrategy.contentsLost());
	}
}
