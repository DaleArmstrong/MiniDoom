package minidoom.game.states;

import minidoom.Game;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;
import minidoom.util.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Used to pause and unpause the game during gameplay
 */
public class PauseMenuState extends State {
	private GameEngine engine;
	private boolean giveAll;
	private float pauseDelay;
	private boolean readyToContinue;
	private Sprite pausedOverlay;

	public PauseMenuState(Game window, StateManager stateManager, GameEngine engine) {
		super(window, stateManager);
		this.pauseDelay = 0.5f;
		this.readyToContinue = false;
		this.pausedOverlay = new Sprite("Paused");
		this.giveAll = false;
		this.engine = engine;
	}

	@Override
	public void update(float dt) {
		if (!readyToContinue) {
			if (pauseDelay <= 0.0f)
				readyToContinue = true;
			else
				pauseDelay -= dt;
		}
		pausedOverlay.setPosition(window.getWidth() / 2.0f - pausedOverlay.getWidth() / 2.0f,
				window.getHeight() / 2.0f - pausedOverlay.getHeight() / 2.0f);
	}

	@Override
	public void render(Graphics2D graphics) {
		stateManager.renderPreviousState(graphics);
		if (!giveAll && window.isKeyPressed(KeyEvent.VK_F5))
			giveAll = true;

		if (window.isKeyPressed(KeyEvent.VK_F9)) {
			if (engine.getLevel() < Constants.MAX_LEVELS)
				stateManager.changeLevel(engine.getLevel() + 1, null);
		}

		if (readyToContinue && window.isKeyPressed(KeyEvent.VK_ESCAPE)) {
			if (giveAll)
				engine.giveAll();
			stateManager.popState();
		} else {
			pausedOverlay.render(graphics);
		}
	}
}
