package minidoom.game.states;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;
import minidoom.Game;
import minidoom.entity.Player;
import minidoom.game.GameEngine;
import minidoom.game.input.Control;
import minidoom.game.managers.SoundManager;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.List;

public class StateManager {
	public enum StateType {
		TitleScreen,
		MainMenu,
		Game,
		PauseMenu,
		GameOver
	}

	private List<State> states;
	private Game window;
	private Map<StateType, String> stateTypes;
	private Set<Control> controls;
	public static Music music;

	public StateManager(Game window) {
		stateTypes = new EnumMap<>(StateType.class);
		stateTypes.put(StateType.TitleScreen, "TitleScreenState");
		stateTypes.put(StateType.MainMenu, "MainMenuState");
		stateTypes.put(StateType.PauseMenu, "PauseMenuState");

		this.window = window;
		states = new Stack<>();
		controls = new HashSet<>();
		loadTitleScreen();
	}

	public void addControl(Control control) {
		controls.add(control);
		window.addKeyListener(control);
		window.addMouseListener(control);
		window.addMouseWheelListener(control);
		window.addMouseMotionListener(control);
	}

	public void update(float dt) {
		states.get(states.size() - 1).update(dt);
	}

	public void render(Graphics2D graphics) {
		states.get(states.size() - 1).render(graphics);
	}

	public void renderPreviousState(Graphics2D graphics) {
		states.get(states.size() - 2).render(graphics);
	}

	public void setGameOver(Player player, GameEngine engine) {
		try {
			Class<?> stateClass = Class.forName("minidoom.game.states.GameOverState");
			Constructor<?> constructor = stateClass.getDeclaredConstructor(window.getClass(), this.getClass(),
					Player.class, GameEngine.class);
			State newState = (State) constructor.newInstance(window, this, player, engine);
			states.add(newState);
		} catch (Exception e) {
			System.out.println("Couldn't load Game Over: " + e.getMessage());
		}
	}

	public void setNextLevelOverlay(Player player, GameEngine engine) {
		try {
			Class<?> stateClass = Class.forName("minidoom.game.states.NextLevelState");
			Constructor<?> constructor = stateClass.getDeclaredConstructor(window.getClass(), this.getClass(),
					Player.class, GameEngine.class);
			State newState = (State) constructor.newInstance(window, this, player, engine);
			states.add(newState);
		} catch (Exception e) {
			System.out.println("Couldn't load Next Level Overlay: " + e.getMessage());
		}
	}

	public void setWinOverlay(GameEngine engine) {
		SoundManager.playIntro();
		try {
			Class<?> stateClass = Class.forName("minidoom.game.states.YouWinState");
			Constructor<?> constructor = stateClass.getDeclaredConstructor(window.getClass(), this.getClass(),
					engine.getClass());
			State newState = (State) constructor.newInstance(window, this, engine);
			states.add(newState);
		} catch (Exception e) {
			System.out.println("Couldn't load You Win State: " + e.getMessage());
		}
	}

	public void addState(StateType state) {
		try {
			Class<?> stateClass = Class.forName("minidoom.game.states." + stateTypes.get(state));
			Constructor<?> constructor = stateClass.getDeclaredConstructor(window.getClass(), this.getClass());
			State newState = (State) constructor.newInstance(window, this);
			states.add(newState);
		} catch (Exception e) {
			System.out.println("Could not load state: " + state.toString() + " : " + e.getMessage());
		}
	}

	public void pauseGame(GameEngine engine) {
		try {
			Class<?> stateClass = Class.forName("minidoom.game.states.PauseMenuState");
			Constructor<?> constructor = stateClass.getDeclaredConstructor(window.getClass(), this.getClass(),
					engine.getClass());
			State newState = (State) constructor.newInstance(window, this, engine);
			states.add(newState);
		} catch (Exception e) {
			System.out.println("Could not Pause game: " + e.getMessage());
		}
	}

	public void changeLevel(int level, Player savedPlayer) {
		State newState = null;
		try {
			Class<?> stateClass = Class.forName("minidoom.game.GameEngine");
			newState = (State) stateClass.getDeclaredConstructor(window.getClass(), this.getClass(),
					int.class, Player.class)
					.newInstance(window, this, level, savedPlayer);
		} catch (Exception e) {
			System.out.println("Could not change level: " +  level + " : " + e.getMessage());
		}

		if (newState != null) {
			states.clear();
			SoundManager.playLevelMusic(level);
			states.add(newState);
		}
	}

	public void startNewGame() {
		clear();
		SoundManager.playLevelMusic(1);
		startNewGame(1);
	}

	public void loadTitleScreen() {
		clear();
		SoundManager.playIntro();
		addState(StateType.TitleScreen);
	}

	public void startNewGame(int level) {
		State newState = null;
		try {
			Class<?> stateClass = Class.forName("minidoom.game.GameEngine");
			newState = (State) stateClass.getDeclaredConstructor(window.getClass(), this.getClass(),
					int.class).newInstance(window, this, level);
		} catch (Exception e) {
			System.out.println("Could not start a new game: " + e.getMessage());
		}

		if (newState != null) {
			states.clear();
			states.add(newState);
		}
	}

	public void popState() {
		if (!states.isEmpty())
			states.remove(states.size() - 1);
	}

	public void clear() {
		for (Control control : controls) {
			window.removeKeyListener(control);
			window.removeMouseListener(control);
			window.removeMouseWheelListener(control);
			window.removeMouseMotionListener(control);
		}
		controls.clear();
		states.clear();
		System.gc();
	}
}
