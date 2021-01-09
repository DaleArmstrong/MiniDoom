package minidoom.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Input keys is a default set of keys to load into the Control class.
 */
public class InputKeys {
	private static KeyPackage player1;

	static {
		player1 = new KeyPackage(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D,
				MouseEvent.BUTTON1, MouseEvent.BUTTON3, KeyEvent.VK_SPACE, KeyEvent.VK_E, KeyEvent.VK_R,
				KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_6, KeyEvent.VK_5);
	}

	public static KeyPackage getInputKeyPackage() { return player1; }
}
