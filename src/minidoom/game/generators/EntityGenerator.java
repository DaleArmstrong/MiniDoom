package minidoom.game.generators;

import minidoom.entity.Actor;
import minidoom.entity.Enemy;
import minidoom.entity.PhysicalEntity;
import minidoom.entity.Player;
import minidoom.entity.items.Item;
import minidoom.game.GameEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The entity generator is used by the maploader and possibly spawners
 * to generate items, walls, and actors. Items information is stored in
 * resourcelists/items.txt so that the maploader only needs to send the itemIDs
 * to the entityGenerator to create the correct item.
 */
public class EntityGenerator {
	GameEngine gameEngine;
	Map<Integer, String> actors;
	Map<Integer, String> walls;
	Map<Integer, String> items;

	public EntityGenerator(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
		actors = new HashMap<>();
		walls = new HashMap<>();
		items = new HashMap<>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(EntityGenerator.class
					.getClassLoader().getResourceAsStream("resourcelists/items.txt"))));

			String itemLine = reader.readLine();
			while (itemLine != null) {
				String[] values = itemLine.split(",");
				items.put(Integer.parseInt(values[0]), values[1]);
				itemLine = reader.readLine();
			}
		} catch (IOException e) {
			System.out.println("Can't load items" + e.getMessage());
		}

		try {
			reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(EntityGenerator.class
					.getClassLoader().getResourceAsStream("resourcelists/enemies.txt"))));

			String actorLine = reader.readLine();
			while (actorLine != null) {
				String[] values = actorLine.split(",");
				actors.put(Integer.parseInt(values[0]), values[1]);
				actorLine = reader.readLine();
			}
		} catch (IOException e) {
			System.out.println("Can't load actors " + e.getMessage());
		}

		walls.put(1, "DestroyableWall");
		walls.put(0, "InvincibleWall");
	}

	public Enemy generateEnemy(int actorID, int entityID, PhysicalEntity.Team team, float x, float y, float rotation) {
		Enemy newEnemy = null;
		try {
			newEnemy = (Enemy)(Class.forName("minidoom.entity.enemies." + actors.get(actorID)))
					.getDeclaredConstructor(gameEngine.getClass(), int.class, PhysicalEntity.Team.class,
							float.class, float.class, float.class)
					.newInstance(gameEngine, entityID, team, x, y, rotation);
		} catch (Exception e) {
			System.out.println("Could not generate Actor: " + actorID + " " + e.getMessage());
		}
		return newEnemy;
	}

	public PhysicalEntity generateWall(int wallType, String tileName, int entityID, float x, float y) {
		PhysicalEntity newWall = null;
		try {
			newWall = (PhysicalEntity)(Class.forName("minidoom.entity.walls." + walls.get(wallType)))
					.getDeclaredConstructor(gameEngine.getClass(), tileName.getClass(), int.class, float.class, float.class)
					.newInstance(gameEngine, tileName, entityID, x, y);
		} catch (Exception e) {
			System.out.println("Could not generate Wall: " + e.getMessage());
		}
		return newWall;
	}

	public Item generateItem(int itemID, int entityID, float x, float y) {
		Item newItem = null;
		try {
			newItem = (Item)(Class.forName("minidoom.entity.items.types." + items.get(itemID) + "Pickup"))
					.getDeclaredConstructor(gameEngine.getClass(), int.class, float.class, float.class)
					.newInstance(gameEngine, entityID, x, y);
		} catch (Exception e) {
			System.out.println("Could not generate Item: " + e.getMessage());
		}
		return newItem;
	}
}
