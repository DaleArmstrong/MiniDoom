package minidoom.game.states;

import minidoom.Game;
import minidoom.entity.Player;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;

import java.awt.*;
import java.awt.event.KeyEvent;

public class YouWinState extends State {
	GameEngine engine;
	Sprite winOverlay;

	public YouWinState(Game window, StateManager stateManager, GameEngine engine) {
		super(window, stateManager);
		this.engine = engine;
		winOverlay = new Sprite("YouWin");
	}

	@Override
	public void update(float dt) {
		engine.update(dt);
		winOverlay.setPosition(window.getWidth() / 2.0f - winOverlay.getWidth() / 2.0f,
				window.getHeight() / 3.5f - winOverlay.getHeight() / 2.0f);
	}

	@Override
	public void render(Graphics2D graphics) {
		engine.render(graphics);
		winOverlay.render(graphics);

		if (window.isKeyPressed(KeyEvent.VK_ESCAPE)) {
			stateManager.loadTitleScreen();
		}
	}
}
