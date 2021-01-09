package minidoom.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Keyboard is a keylistener that is for global key control. While
 * Control is specific to a player.
 */
public class Keyboard implements KeyListener {
	private static int MAX_KEYS = 250;
	private static boolean key[] = new boolean[MAX_KEYS];

	public boolean isKeyPressed(int keyCode) {
		return key[keyCode];
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() >= MAX_KEYS)
			return;

		key[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() >= MAX_KEYS)
			return;

		key[e.getKeyCode()] = false;
	}
}
