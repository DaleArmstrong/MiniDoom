package minidoom.game.states;

import minidoom.Game;
import minidoom.entity.components.Sprite;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.animations.AnimationSet;
import minidoom.game.animations.AnimationSprite;
import minidoom.game.managers.SoundManager;
import minidoom.game.managers.SpriteManager;
import minidoom.util.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * TitleScreen that is shown when the game is first run. Will display
 * logo with a "press esc key" to switch to the game.
 */
public class TitleScreenState extends State {
	private BufferedImage screenImage;
	private Graphics2D screenGraphics;
	private BufferedImage logo;
	private float logoX;
	private float logoY;
	private BufferedImage pressKey;
	private float pressKeyX;
	private float pressKeyY;
	private float logoTimer;
	private boolean doneAnimating;
	private boolean pressKeyMoved;
	private boolean escReleased;
	private boolean escPressed;

	public TitleScreenState(Game window, StateManager stateManager) {
		super(window, stateManager);
		this.screenImage = new BufferedImage(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
		this.screenGraphics = screenImage.createGraphics();
		this.doneAnimating = false;
		this.pressKeyMoved = false;
		this.escReleased = false;
		this.logo = SpriteManager.getSprite("DoomLogo");
		this.pressKey = SpriteManager.getSprite("DoomStart");
		this.logoX = Constants.SCREEN_WIDTH / 2.0f - logo.getWidth() / 2.0f;
		this.logoY = 0;
		this.logoTimer = 0;
		SoundManager.playIntro();
	}

	@Override
	public void update(float dt) {
		if (!doneAnimating) {
			if (logoTimer > 0.8f && window.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				logoY += (3.0f - logoTimer) * 50;
				logoTimer = 3.0f;
				escPressed = true;
				doneAnimating = true;
			} else if (logoTimer < 3) {
				logoTimer += dt;
				logoY += 30 * dt;
			} else {
				escReleased = true;
				doneAnimating = true;
			}
		}

		if (!pressKeyMoved && doneAnimating) {
			pressKeyX = logoX + (logo.getWidth() - pressKey.getWidth()) / 2.0f;
			pressKeyY = logoY + logo.getHeight();
			pressKeyMoved = true;
		}
	}

	@Override
	public void render(Graphics2D graphics) {
		screenGraphics.clearRect(0, 0, screenImage.getWidth(), screenImage.getHeight());
		screenGraphics.drawImage(logo, null, (int)logoX, (int)logoY);
		if (doneAnimating) {
			screenGraphics.drawImage(pressKey, null, (int)pressKeyX, (int)pressKeyY);
			if (escPressed && !window.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				escPressed = false;
				escReleased = true;
			}
			if (window.isKeyPressed(KeyEvent.VK_ESCAPE) && escReleased) {
				stateManager.startNewGame();
				graphics.setColor(Color.BLACK);
				graphics.clearRect(0, 0, window.getWidth(), window.getHeight());
				return;
			}
		}
		graphics.drawImage(screenImage, 0, 0, window.getWidth(), window.getHeight(),
				0, 0, screenImage.getWidth(), screenImage.getHeight(), null);
	}
}
