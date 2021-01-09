package minidoom.game.states;

import minidoom.Game;

import java.awt.*;

public abstract class State {
	protected Game window;
	protected StateManager stateManager;

	protected State(Game window, StateManager stateManager) {
		this.window = window;
		this.stateManager = stateManager;
	}

	public abstract void update(float dt);
	public abstract void render(Graphics2D graphics);
}
