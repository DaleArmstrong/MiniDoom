package minidoom.game.managers;

import minidoom.entity.PhysicalEntity;
import minidoom.game.GameEngine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import static javax.imageio.ImageIO.read;

public class MapLoader {
	private GameEngine engine;
	private BufferedReader reader;
	private BufferedImage tileset;
	private BufferedImage newLayer;
	private Graphics2D graphics;
	private int mapWidth;
	private int mapHeight;
	private int tileSize;
	private int level;
	private int tilesetTileWidth;
	List<Integer> tiles;

	/**
	 * Loads the given map from various pieces. Calls
	 * the engine functions to spawn entities at specific locations.
	 * @param engine the game engine used to spawn entities
	 */
	public MapLoader(GameEngine engine) {
		this.engine = engine;
		this.reader = null;
		this.tileset = null;
		this.newLayer = null;
		this.graphics = null;
	}

	/**
	 * Loads the specific level and stores the information in the given map
	 * @param level the level to load
	 * @param map the map to store information in
	 */
	public void loadMap(int level, GameMap map) {
		this.level = level;
		try {
			tileset = read(Objects.requireNonNull(MapLoader.class.getClassLoader()
					.getResource("maps/level" + level + "/tileset.png")));
			reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(SpriteManager.class
					.getClassLoader().getResourceAsStream("maps/level" + level + "/Map.csv"))));
		} catch (Exception e) {
			System.out.println("Can't load level " + level + " : " + e.getMessage());
		}

		/* Store map information */
		try {
			String layerLine = reader.readLine();
			String[] values = layerLine.split(",");
			tileSize = Integer.parseInt(values[0]);
			map.setTilesize(tileSize);
			mapWidth = Integer.parseInt(values[1]);
			map.setMapTilesWide(mapWidth);
			map.setMapWidth(mapWidth * tileSize);
			mapHeight = Integer.parseInt(values[2]);
			map.setMapTilesHigh(mapHeight);
			map.setMapHeight(mapHeight * tileSize);
			tilesetTileWidth = tileset.getWidth() / tileSize;
		} catch (Exception e) {
			System.out.println("Could not load Map " + level + " : " + e.getMessage());
		}

		this.newLayer = new BufferedImage(mapWidth * tileSize, mapHeight * tileSize, BufferedImage.TYPE_INT_RGB);
		this.graphics = (Graphics2D) newLayer.getGraphics();

		try {
			reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(SpriteManager.class
					.getClassLoader().getResourceAsStream("maps/level" + level + "/level" + level + "_MapLayer.csv"))));
			readMapLayer();
		} catch (Exception e) {
			System.out.println("Can't load Map Layer: " + e.getMessage());
		}

		try {
			reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(SpriteManager.class
					.getClassLoader().getResourceAsStream("maps/level" + level + "/level" + level + "_MapLayer2.csv"))));
			readMapLayer();
		} catch (Exception e) {
			System.out.println("Can't load Map Layer 2: " + e.getMessage());
		}

		map.setMapLayer(newLayer);

		try {
			reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(SpriteManager.class
					.getClassLoader().getResourceAsStream("maps/level" + level + "/level" + level + "_Unbreakable.csv"))));
			readUnbreakable();
		} catch (Exception e) {
			System.out.println("Can't read unbreakable: " + e.getMessage());
		}

		try {
			reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(SpriteManager.class
					.getClassLoader().getResourceAsStream("maps/level" + level + "/level" + level + "_Breakable.csv"))));
			readBreakable();
		} catch (Exception e) {
			System.out.println("Can't read breakable: " + e.getMessage());
		}

		try {
			reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(SpriteManager.class
					.getClassLoader().getResourceAsStream("maps/level" + level + "/level" + level + "_Items.csv"))));
			readItems();
		} catch (Exception e) {
			System.out.println("Can't read items: " + e.getMessage());
		}

		try {
			reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(SpriteManager.class
					.getClassLoader().getResourceAsStream("maps/level" + level + "/level" + level + "_Spawns.csv"))));
			readSpawns();
		} catch (Exception e) {
			System.out.println("Can't read spawns: " + e.getMessage());
		}

		try {
			reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(SpriteManager.class
					.getClassLoader().getResourceAsStream("maps/level" + level + "/level" + level + "_Overlay.csv"))));
			readOverlay();
		} catch (Exception e) {
			System.out.println("Can't read overlay: " + e.getMessage());
		}
		graphics.dispose();
	}

	/**
	 * Used to parse the csv files containing information for each layer
	 */
	private void parseMapLayers() {
		tiles = new ArrayList<>(mapWidth * mapHeight);
		try {
			String layerLine = reader.readLine();
			while (layerLine != null) {
				String[] values = layerLine.split(",");
				for (String string : values) {
					tiles.add(Integer.parseInt(string));
				}
				layerLine = reader.readLine();
			}
		} catch (Exception e) {
			System.out.println("Could not parse: " + e.getMessage());
		}
	}

	/**
	 * Reads the unbreakable layer and calls the game engine to create
	 * unbreakable walls.
	 */
	private void readUnbreakable() {
		parseMapLayers();

		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				int tileValue = tiles.get(i + j * mapWidth);
				if (tileValue != -1) {
					int tilePositionX = tileValue % tilesetTileWidth;
					int tilePositionY = tileValue / tilesetTileWidth;
					if (!SpriteManager.hasImage("Tile" + tileValue)) {
						BufferedImage tileImage = tileset.getSubimage(tilePositionX * tileSize, tilePositionY * tileSize,
								tileSize, tileSize);
						SpriteManager.addImage("Tile" + tileValue, tileImage);
					}
					engine.spawnWall(0, "Tile" + tileValue, i * tileSize, j * tileSize);
				}
			}
		}
	}

	/**
	 * Reads the breakable layer and calls the game engine to create
	 * breakable walls.
	 */
	private void readBreakable() {
		parseMapLayers();

		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				int tileValue = tiles.get(i + j * mapWidth);
				if (tileValue != -1) {
					int tilePositionX = tileValue % tilesetTileWidth;
					int tilePositionY = tileValue / tilesetTileWidth;
					if (!SpriteManager.hasImage("Tile" + tileValue)) {
						BufferedImage tileImage = tileset.getSubimage(tilePositionX * tileSize, tilePositionY * tileSize,
								tileSize, tileSize);
						SpriteManager.addImage("Tile" + tileValue, tileImage);
					}
					engine.spawnWall(1, "Tile" + tileValue, i * tileSize, j * tileSize);
				}
			}
		}
	}

	/**
	 * Reads the items layer and calls the game engine to create
	 * items.
	 */
	private void readItems() {
		parseMapLayers();

		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				int tileValue = tiles.get(i + j * mapWidth);
				if (tileValue != -1) {
					engine.spawnItem(tileValue, i * tileSize, j * tileSize);
				}
			}
		}
	}

	/**
	 * Reads the spawns layer and calls the game engine to create
	 * actors and players.
	 */
	private void readSpawns() {
		parseMapLayers();
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				int tileValue = tiles.get(i + j * mapWidth);
				if (tileValue != -1) {
					if (tileValue == 0)
						engine.spawnPlayer(PhysicalEntity.Team.PLAYER, i * tileSize, j * tileSize, 0);
					else {
						int enemyRotation = (tileValue % 4) * 90;
						engine.spawnEnemy(PhysicalEntity.Team.NEUTRAL, tileValue, i * tileSize, j * tileSize,
								enemyRotation);
					}
				}
			}
		}
	}

	/**
	 * Overlay layer is used for images that can not be interacted with. It is only for decoration
	 * such as an archway, the top of a pillar, or statue
	 */
	private void readOverlay() {
		parseMapLayers();

		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				int tileValue = tiles.get(i + j * mapWidth);
				if (tileValue != -1) {
					int tilePositionX = tileValue % tilesetTileWidth;
					int tilePositionY = tileValue / tilesetTileWidth;
					if (!SpriteManager.hasImage("Tile" + tileValue)) {
						BufferedImage tileImage = tileset.getSubimage(tilePositionX * tileSize, tilePositionY * tileSize,
								tileSize, tileSize);
						SpriteManager.addImage("Tile" + tileValue, tileImage);
					}
					engine.addDecoration("Tile" + tileValue, i * tileSize, j * tileSize);
				}
			}
		}
	}

	/**
	 * Reads the base map layer, and constructs a BufferedImage from all the various tiles
	 */
	private void readMapLayer() {
		parseMapLayers();

		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				int tileValue = tiles.get(i + j * mapWidth);
				if (tileValue != -1) {
					int tilePositionX = tileValue % tilesetTileWidth;
					int tilePositionY = tileValue / tilesetTileWidth;
					graphics.drawImage(tileset.getSubimage(tilePositionX * tileSize, tilePositionY * tileSize,
							tileSize, tileSize),i * tileSize, j * tileSize, null);
				}
			}
		}
	}
}
