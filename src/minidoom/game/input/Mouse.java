package minidoom.game.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Mouse is a keylistener to keep track of the movement of the mousewheel
 * and any Mouse click events.
 */
public class Mouse implements MouseWheelListener, MouseListener {
	private static int MAX_MOUSE_KEYS = 10;
	private static boolean mouseKey[] = new boolean[MAX_MOUSE_KEYS];
	private static int mouseWheel = 0;

	public boolean isMouseKeyPressed(int mouseKeyCode) {
		return mouseKey[mouseKeyCode];
	}

	public int getMouseWheel() {
		int consumeMouseWheel = mouseWheel;
		mouseWheel = 0;
		return consumeMouseWheel;
	}

	public void resetMouseWheel() {
		mouseWheel = 0;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
				if (e.getButton() >= MAX_MOUSE_KEYS)
			return;
		mouseKey[e.getButton()] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() >= MAX_MOUSE_KEYS)
			return;
		mouseKey[e.getButton()] = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0) {
			mouseWheel = 1;
		} else {
			mouseWheel = -1;
		}
	}
}
