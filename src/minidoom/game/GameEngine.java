package minidoom.game;

import minidoom.Game;
import minidoom.entity.*;
import minidoom.entity.components.Sprite;
import minidoom.entity.items.Item;
import minidoom.entity.projectile.Projectile;
import minidoom.game.animations.AnimationLoader;
import minidoom.game.animations.AnimationSet;
import minidoom.game.animations.AnimationManager;
import minidoom.game.effects.PushEffect;
import minidoom.game.generators.*;
import minidoom.game.input.Control;
import minidoom.game.input.InputKeys;
import minidoom.game.managers.GameMap;
import minidoom.game.managers.MapLoader;
import minidoom.game.states.State;
import minidoom.game.states.StateManager;
import minidoom.game.weapons.ppackages.ProjectilePackage;
import minidoom.game.weapons.Weapon;
import minidoom.util.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Game engine holds all the data for various entities, maps, cameras, controls, etc...
 * and uses this to construct the game.
 */
public class GameEngine extends State {
	private boolean gameOver;
	private int level;
	private int numberOfEntities;
	private int numberOfEnemies;
	private float pauseDelay;
	private boolean alertedThisCycle;
	private GameMap map;
	private Graphics2D mapGraphics;
	private Collision collision;
	private WeaponGenerator weaponGenerator;
	private EntityGenerator entityGenerator;
	private BufferedImage worldImage;
	private Graphics2D worldContext;
	private PushEffect pushEffect;
	private AnimationManager particleManager;

	private Hud hud;
	private Player player;
	private Player savedPlayer;
	private Camera camera;
	private List<Item> items;
	private List<Item> itemRemovals;
	private List<PhysicalEntity> walls;
	private List<PhysicalEntity> wallRemovals;
	private List<Enemy> enemies;
	private List<Enemy> enemyRemovals;
	private List<Projectile> projectiles;
	private List<Projectile> projectilePool;
	private List<Projectile> projectileRemovals;

	private List<Sprite> topLayer;

	private SpatialHash<Actor> actorHash;
	private SpatialHash<Actor> shadowHash;
	private SpatialHash<PhysicalEntity> wallHash;
	private SpatialHash<Item> itemHash;

	public GameEngine(Game window, StateManager stateManager, int level) {
		this(window, stateManager, level, null);
	}

	public GameEngine(Game window, StateManager stateManager, int level, Player savedPlayer) {
		super(window, stateManager);

		this.weaponGenerator = new WeaponGenerator(this);
		this.entityGenerator = new EntityGenerator(this);

		this.level = level;
		this.numberOfEntities = 0;
		this.numberOfEnemies = 0;
		this.savedPlayer = savedPlayer;
		this.particleManager = new AnimationManager();
		this.alertedThisCycle = false;
		this.gameOver = false;
		this.pauseDelay = 1.0f;
		this.items = new ArrayList<>();
		this.walls = new ArrayList<>();
		this.enemies = new ArrayList<>();
		this.projectiles = new ArrayList<>();
		this.projectilePool = new ArrayList<>();
		this.projectileRemovals = new ArrayList<>();
		this.enemyRemovals = new ArrayList<>();
		this.itemRemovals = new ArrayList<>();
		this.wallRemovals = new ArrayList<>();
		this.topLayer = new ArrayList<>();
		this.pushEffect = new PushEffect(this);
		this.map = new GameMap();
		this.player = null;

		for (int i = 0; i < 30; i++) {
			this.projectilePool.add(new Projectile(this));
		}

		MapLoader mapLoader = new MapLoader(this);
		mapLoader.loadMap(level, map);
		mapGraphics = map.getGraphics();

		for (Enemy enemy : enemies)
			enemy.updateTarget(player);

		worldImage = new BufferedImage(map.getMapWidth(), map.getMapHeight(), BufferedImage.TYPE_INT_RGB);
		worldImage.getGraphics().drawImage(map.getMapLayer(), 0, 0, null);

		int cellSize = 100;
		actorHash = new SpatialHash<>(map.getMapWidth(), map.getMapHeight(), cellSize, false);
		actorHash.addEntity(player);
		actorHash.addEntityArray(enemies);

		shadowHash = new SpatialHash<>(map.getMapWidth(), map.getMapHeight(), cellSize, true);
		shadowHash.addEntity(player);
		shadowHash.addEntityArray(enemies);

		wallHash = new SpatialHash<>(map.getMapWidth(), map.getMapHeight(), cellSize, false);
		wallHash.addEntityArray(walls);

		itemHash = new SpatialHash<>(map.getMapWidth(), map.getMapHeight(), cellSize, false);
		itemHash.addEntityArray(items);

		collision = new Collision(map.getMapWidth(), map.getMapHeight());
	}

	public int getLevel() { return level; }

	/**
	 * Spawns a player at the given location on the specific Team
	 * @param team the team the player is on
	 * @param x the x location
	 * @param y the y location
	 * @param rotation the rotation of the player when spawned
	 */
	public void spawnPlayer(PhysicalEntity.Team team, float x, float y, float rotation) {
		/* Do more players need to be made? */
		if (player != null)
			return;

		player = new Player(this, numberOfEntities, team, x, y, rotation);
		numberOfEntities++;

		/* Assign controls to the player */
		Control control = new Control(player, InputKeys.getInputKeyPackage());
		stateManager.addControl(control);

		camera = new Camera(window, player, Constants.SCREEN_WIDTH,
				Constants.SCREEN_HEIGHT, map.getMapWidth(), map.getMapHeight());

		hud = new Hud(player);

		if (savedPlayer == null) {
			savedPlayer = new Player(this, 0, PhysicalEntity.Team.PLAYER, 0, 0, 0);
			savedPlayer.copy(player);
		} else {
			player.copy(savedPlayer);
		}

	}

	/* Spawns the actor at the given location and adds to the actors list */
	public void spawnEnemy(PhysicalEntity.Team team, int actorID, float x, float y, float rotation) {
		Enemy newEnemy = entityGenerator.generateEnemy(actorID, numberOfEntities, team, x, y, rotation);
		numberOfEntities++;
		numberOfEnemies++;
		enemies.add(newEnemy);
	}

	/* Spawns the wall at the given location and adds to the wall list */
	public void spawnWall(int wallType, String tileName, float x, float y) {
		PhysicalEntity newWall = entityGenerator.generateWall(wallType, tileName, numberOfEntities, x, y);
		numberOfEntities++;
		walls.add(newWall);
	}

	/* Spawns the item at the given location and adds to the item list */
	public void spawnItem(int itemID, float x, float y) {
		Item newItem = entityGenerator.generateItem(itemID, numberOfEntities, x, y);
		numberOfEntities++;
		items.add(newItem);
	}

	/* Spawns the decoration at the given location and adds to the toplayer list */
	public void addDecoration(String spriteName, float x, float y) {
		Sprite newSprite = new Sprite(spriteName, x, y);
		topLayer.add(newSprite);
	}

	/* adds the wall to be removed at a later point in time */
	public void addWallRemoval(PhysicalEntity wall) {
		wallRemovals.add(wall);
	}

	/* adds the item to be removed at a later point in time */
	public void addItemRemoval(Item item) {
		itemRemovals.add(item);
	}

	/* adds the actor to be removed at a later point in time */
	public void addEnemyRemoval(Enemy enemy) {
		enemyRemovals.add(enemy);
	}

	/* adds the projectile to be removed at a later point in time */
	public void addProjectileRemoval(Projectile projectile) {
		projectileRemovals.add(projectile);
	}

	/* generates the given weapon */
	public Weapon generateWeapon(Weapon.WeaponType weaponType) {
		return weaponGenerator.generateWeapon(weaponType);
	}

	/* Checks whether a moved entity by an effect is colliding and to handle the collision */
	public void checkMovedEntityCollision(Entity entity) {
		Set<Integer> cells = shadowHash.getEntityCells(entity);
		collision.checkMapBounds(entity, true, true);

		Set<Actor> actorsToCheck = shadowHash.getEntitiesInCells(cells);
		for (Actor actor : actorsToCheck) {
			if(entity.getEntityID() != actor.getEntityID())
				collision.checkCircleBounds(entity, actor, true);
		}

		Set<PhysicalEntity> wallsToCheck = wallHash.getEntitiesInCells(cells);
		for (PhysicalEntity phyEntity : wallsToCheck) {
			collision.checkShadowBox(entity, phyEntity, true);
		}
	}

	public void clearEffects(Entity entity) {
		pushEffect.remove(entity);
	}

	public void giveAll() {
		player.giveAll();
	}

	/* spawns an explosion and assigns a push effect to actors affected by the explosion */
	public void spawnExplosion(float x, float y, int damage, float explosiveRange, PhysicalEntity entity) {
		Set<Actor> actorsToCheck = actorHash.getEntitiesByDistance(x, y, explosiveRange);
		for (Actor actor : actorsToCheck) {
			if (entity != null && actor.getEntityID() == entity.getEntityID())
				continue;

			if (actor.isShootable() && collision.checkCircleBox(x, y, explosiveRange, actor)) {
				if (actor.isEffectable())
					pushEffect.addEntity(actor, x, y, 400, 0.3f);
				actor.onHit(damage, null);
			}
		}

		Set<PhysicalEntity> wallsToCheck = wallHash.getEntitiesByDistance(x, y, explosiveRange);
		for (PhysicalEntity wall : wallsToCheck) {
			if (collision.checkCircleBox(x, y, explosiveRange, wall)) {
				wall.onHit(damage, null);
			}
		}
	}

	/* spawns a particle at the location with an animation */
	public void spawnParticle(AnimationSet as, float x, float y, float vx, float vy, float rotation, float rotationSpeed) {
		particleManager.createParticle(as, x, y, vx, vy, rotation, rotationSpeed);
	}

	public void alertEnemies(float x, float y, boolean scream) {
		if (alertedThisCycle)
			return;
		alertedThisCycle = true;
		Set<Actor> actors = actorHash.getEntitiesByDistance(x, y, 200);
		for (Actor actor : actors) {
			actor.alert(scream);
		}

	}

	public void addPlayerKill() {
		player.addKill();
		numberOfEnemies--;
		if (numberOfEnemies <= 0) {
			gameOver = true;
			if (level < Constants.MAX_LEVELS) {
				stateManager.setNextLevelOverlay(player, this);
			} else {
				stateManager.setWinOverlay(this);
			}
		}
	}

	public void gameOver() {
		gameOver = true;
		stateManager.setGameOver(savedPlayer, this);
	}

	/* spawns the given projectile from a projectile package */
	public void spawnProjectile(ProjectilePackage pPackage) {
		float minAngle = pPackage.getRotation() - pPackage.getWeaponSpread();
		float maxAngle = pPackage.getRotation() + pPackage.getWeaponSpread();
		float currentAngle = minAngle;
		float anglePerBullet = pPackage.getWeaponSpread() * 2 / pPackage.getNumberOfProjectiles();

		Projectile projectile;
		for (int i = 0; i < pPackage.getNumberOfProjectiles(); i++) {
			if (!projectilePool.isEmpty()) {
				projectile = projectilePool.get(projectilePool.size() - 1);
				projectilePool.remove(projectilePool.size() - 1);
			} else {
				projectile = new Projectile(this);
			}

			if (pPackage.uniformSpread()) {
				projectile.init(pPackage, currentAngle);
				currentAngle += anglePerBullet;
			} else
				projectile.init(pPackage, Constants.rand(minAngle, maxAngle));
			projectiles.add(projectile);
		}
	}

	public void bloodSplatter(int x, int y) {
		int spawnX = x + Constants.rand(-35, 35);
		int spawnY = y + Constants.rand(-35, 35);
		mapGraphics.drawImage(AnimationLoader.getRandomBloodSplatter(), null, spawnX, spawnY);
	}

	/* Spatial hash is cleared and updated every update */
	public void updateSpatialHash() {
		actorHash.clear();
		actorHash.addEntity(player);
		actorHash.addEntityArray(enemies);
		shadowHash.clear();
		shadowHash.addEntity(player);
		shadowHash.addEntityArray(enemies);
	}

	/* Remove all removals from the corresponding lists */
	public void updateRemovals() {
		items.removeAll(itemRemovals);
		itemHash.removeAll(itemRemovals);
		itemRemovals.clear();
		walls.removeAll(wallRemovals);
		wallHash.removeAll(wallRemovals);
		wallRemovals.clear();
		enemies.removeAll(enemyRemovals);
		enemyRemovals.clear();
		projectiles.removeAll(projectileRemovals);
		projectilePool.addAll(projectileRemovals);
		projectileRemovals.clear();
	}

	/* update players and check collision */
	public void updatePlayer(float dt) {
		Set<Integer> cells;
		player.update(dt);
		if (!player.isCollidable())
			return;
		collision.checkMapBounds(player, true, true);
		cells = shadowHash.getEntityCells(player);
		Set<Actor> actorsToCheck = shadowHash.getEntitiesInCells(cells);
		for (Actor entity : actorsToCheck) {
			if(player.getEntityID() != entity.getEntityID() && entity.isCollidable())
				collision.checkCircleBounds(player, entity, true);
		}

		Set<PhysicalEntity> wallsToCheck = wallHash.getEntitiesInCells(cells);
		for (PhysicalEntity entity : wallsToCheck) {
			if (entity.isCollidable())
				collision.checkShadowBox(player, entity, true);
		}

		Set<Item> entitiesToCheck = itemHash.getEntitiesInCells(cells);
		for (Item entity : entitiesToCheck) {
			if (collision.checkBoxBounds(player, entity, false)) {
				entity.onPickup(player);
			}
		}
	}

	/* update actors and check collision */
	public void updateActors(float dt) {
		Set<Integer> cells;
		for (Actor actor : enemies) {
			actor.update(dt);
			if (!actor.isCollidable())
				continue;
			collision.checkMapBounds(actor, true, true);
			cells = shadowHash.getEntityCells(actor);
			Set<Actor> actorsToCheck = shadowHash.getEntitiesInCells(cells);
			for (Actor entity : actorsToCheck) {
				if(actor.getEntityID() != entity.getEntityID() && entity.isCollidable())
					collision.checkCircleBounds(actor, entity, false);
			}

			Set<PhysicalEntity> wallsToCheck = wallHash.getEntitiesInCells(cells);
			for (PhysicalEntity entity : wallsToCheck) {
				if (entity.isCollidable())
					collision.checkShadowBox(actor, entity, true);
			}
		}
	}

	/* update projectiles, check collisions, and assign damage to entities */
	public void updateProjectiles(float dt) {
		Set<Integer> cells;
		for (Projectile projectile : projectiles) {
			projectile.update(dt);
			if (projectile.destroyed()) {
				addProjectileRemoval(projectile);
				continue;
			}

			if (collision.checkProjectileMapBounds(projectile)) {
				projectileRemovals.add(projectile);
			} else {
				cells = actorHash.getEntityCells(projectile);
				Set<Actor> actorsToCheck = actorHash.getEntitiesInCells(cells);
				for (Actor entity : actorsToCheck) {
					if (projectile.getTeam() != entity.getTeam() && projectile.getEntityID() != entity.getEntityID()
						&& entity.isShootable()) {
						if (collision.checkBoxBounds(projectile, entity, false)) {
							entity.alert(false);
							if (!projectile.isExplosive()) {
								if (projectile.isRipAndTear()) {
									entity.onHit(projectile.getDamage(), projectile.getShooter().getName());

									if (entity.ripTearReady())
										entity.activateRipAndTear(projectile.getShooter());
									else
										entity.setRipAndTearAvailable(false);
								} else {
									entity.onHit(projectile.getDamage(), null);
								}
							}
							projectile.kill();
							break;
						}
					}
				}
				if (!projectile.destroyed()) {
					Set<PhysicalEntity> wallsToCheck = wallHash.getEntitiesInCells(cells);
					for (PhysicalEntity entity : wallsToCheck) {
						if (entity.isShootable()) {
							if (collision.checkBoxBounds(projectile, entity, false)) {
								if (!projectile.isExplosive())
									entity.onHit(projectile.getDamage(), null);
								projectile.kill();
								break;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void update(float dt) {
		alertedThisCycle = false;
		particleManager.update(dt);
		pushEffect.update(dt);
		updatePlayer(dt);
		updateActors(dt);
		updateProjectiles(dt);
		hud.update(dt);

		for (Entity item : items)
			item.update(dt);

		updateRemovals();
		updateSpatialHash();

		camera.update();

		if (pauseDelay >= 0.0f)
			pauseDelay -= dt;
	}

	/**
	 * Draws all entities to an image. Utilizing subimages to draw the players location on each
	 * half of the screen. The image is also drawn to the middle of the screen for a minimap.
	 * @param graphics the window to draw to
	 */
	@Override
	public void render(Graphics2D graphics) {
		Rectangle rect = camera.getBounds();

		worldContext = worldImage.createGraphics();
		worldContext.drawImage(map.getMapLayer(), rect.x, rect.y, rect.x + rect.width, rect.y + rect.height,
				rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, null);

		List<Actor> actorsToRender = new ArrayList<>(actorHash.getEntityByRange(rect, 40));
		actorsToRender.sort((actor1, actor2) -> Float.compare(actor1.getY(), actor2.getY()));

		for (Entity entity : actorsToRender)
			entity.renderShadow(worldContext);

		Set<PhysicalEntity> wallsToRender = wallHash.getEntityByRange(rect, 40);
		for (Entity entity : wallsToRender)
			entity.render(worldContext);

		Set<Item> itemsToRender = itemHash.getEntityByRange(rect, 40);
		for(Entity entity : itemsToRender)
			entity.render(worldContext);

		for (Entity entity : actorsToRender)
			entity.render(worldContext);

		for (Entity entity : projectiles)
			entity.render(worldContext);

		for (Sprite sprite : topLayer)
			sprite.render(worldContext);

		particleManager.render(worldContext);

		/* renders information about the players, weapon, ammo, health, armor */
		hud.render(worldContext, rect.x + 2, rect.y + rect.height - 5, rect.x + rect.width - 2);

		worldContext.dispose();

		graphics.drawImage(worldImage, 0, 0, window.getWidth(), window.getHeight(), rect.x, rect.y,
				rect.x + rect.width, rect.y + rect.height, null);

		if (pauseDelay < 0 && !gameOver && window.isKeyPressed(KeyEvent.VK_ESCAPE)) {
			pauseDelay = 0.7f;
			stateManager.pauseGame(this);
		}
	}
}
