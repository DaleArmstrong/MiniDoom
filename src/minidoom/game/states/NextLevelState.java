package minidoom.game.states;

import minidoom.Game;
import minidoom.entity.Player;
import minidoom.entity.components.Sprite;
import minidoom.game.GameEngine;

import java.awt.*;
import java.awt.event.KeyEvent;

public class NextLevelState extends State {
	Player player;
	GameEngine engine;
	Sprite winOverlay;

	public NextLevelState(Game window, StateManager stateManager, Player player, GameEngine engine) {
		super(window, stateManager);
		this.player = player;
		this.engine = engine;
		winOverlay = new Sprite("NextLevel");
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
			stateManager.changeLevel(engine.getLevel() + 1, player);
		}
	}
}
