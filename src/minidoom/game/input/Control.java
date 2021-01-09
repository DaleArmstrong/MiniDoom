package minidoom.game.input;

import minidoom.entity.Player;

import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Control is a keylister that is assigned to a specific
 * player. It is passed a keyPackage that contains the keys to
 * assign to the controls.
 */
public class Control implements KeyListener, MouseListener, MouseWheelListener, MouseMotionListener {
	Player player;
	Map<Integer, Runnable> keyPressed;
	Map<Integer, Runnable> keyReleased;
	Map<Integer, Runnable> mousePressed;
	Map<Integer, Runnable> mouseReleased;

	public Control(Player player, KeyPackage keyPackage) {
		keyPressed = new HashMap<>();
		keyReleased = new HashMap<>();
		mousePressed = new HashMap<>();
		mouseReleased = new HashMap<>();
		this.player = player;

		keyPressed.put(keyPackage.moveUp, player::moveUpPressed);
		keyPressed.put(keyPackage.moveDown, player::moveDownPressed);
		keyPressed.put(keyPackage.moveLeft, player::moveLeftPressed);
		keyPressed.put(keyPackage.moveRight, player::moveRightPressed);
		keyPressed.put(keyPackage.dodge, player::dodgePressed);
		keyPressed.put(keyPackage.use, player::usePressed);
		keyPressed.put(keyPackage.reload, player::reloadPressed);
		keyPressed.put(keyPackage.swapMelee, player::swapMeleePressed);
		keyPressed.put(keyPackage.swapGun, player::swapGunPressed);
		keyPressed.put(keyPackage.swapShotgun, player::swapShotgunPressed);
		keyPressed.put(keyPackage.swapMachinegun, player::swapMachinegunPressed);
		keyPressed.put(keyPackage.swapRocketLauncher, player::swapRocketLauncherPressed);
		keyPressed.put(keyPackage.swapPlasmagun, player::swapPlasmagunPressed);

		keyReleased.put(keyPackage.moveUp, player::moveUpReleased);
		keyReleased.put(keyPackage.moveDown, player::moveDownReleased);
		keyReleased.put(keyPackage.moveLeft, player::moveLeftReleased);
		keyReleased.put(keyPackage.moveRight, player::moveRightReleased);
		keyReleased.put(keyPackage.dodge, player::dodgeReleased);
		keyReleased.put(keyPackage.use, player::useReleased);
		keyReleased.put(keyPackage.reload, player::reloadReleased);
		keyReleased.put(keyPackage.swapMelee, player::swapMeleeReleased);
		keyReleased.put(keyPackage.swapGun, player::swapGunReleased);
		keyReleased.put(keyPackage.swapShotgun, player::swapShotgunReleased);
		keyReleased.put(keyPackage.swapMachinegun, player::swapMachinegunReleased);
		keyReleased.put(keyPackage.swapRocketLauncher, player::swapRocketLauncherReleased);
		keyReleased.put(keyPackage.swapPlasmagun, player::swapPlasmagunReleased);

		mousePressed.put(keyPackage.fire, player::firePressed);
		mousePressed.put(keyPackage.altFire, player::altFirePressed);

		mouseReleased.put(keyPackage.fire, player::fireReleased);
		mouseReleased.put(keyPackage.altFire, player::altFireReleased);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (keyPressed.containsKey(e.getKeyCode()))
			keyPressed.get(e.getKeyCode()).run();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (keyReleased.containsKey(e.getKeyCode()))
			keyReleased.get(e.getKeyCode()).run();
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		player.setMouse(e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		player.setMouse(e.getX(), e.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (mousePressed.containsKey(e.getButton()))
			mousePressed.get(e.getButton()).run();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (mouseReleased.containsKey(e.getButton()))
			mouseReleased.get(e.getButton()).run();
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
			player.swapNextPressed();
		} else {
			player.swapPrevPressed();
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
