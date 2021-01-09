package minidoom.game.managers;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Contains information about the current game map
 * and the base layer of the map.
 */
public class GameMap {
	private BufferedImage mapLayer;
	private Graphics2D graphics;
	private int tileSize;
	private int mapWidth;
	private int mapHeight;
	private int mapTilesWide;
	private int mapTilesHigh;

	public GameMap() {

	}

	public Graphics2D getGraphics() {
		return graphics;
	}

	public void setMapLayer(BufferedImage mapLayer) {
		this.mapLayer = mapLayer;
		this.graphics = mapLayer.createGraphics();
	}

	public void setMapHeight(int mapHeight) {
		this.mapHeight = mapHeight;
	}

	public void setMapTilesHigh(int mapTilesHigh) {
		this.mapTilesHigh = mapTilesHigh;
	}

	public void setMapTilesWide(int mapTilesWide) {
		this.mapTilesWide = mapTilesWide;
	}

	public void setMapWidth(int mapWidth) {
		this.mapWidth = mapWidth;
	}

	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}

	public void setTilesize(int tileSize) {
		this.tileSize = tileSize;
	}

	public BufferedImage getMapLayer() {
		return mapLayer;
	}

	public int getMapHeight() {
		return mapHeight;
	}

	public int getMapTilesHigh() {
		return mapTilesHigh;
	}

	public int getMapTilesWide() {
		return mapTilesWide;
	}

	public int getMapWidth() {
		return mapWidth;
	}

	public int getTileSize() {
		return tileSize;
	}

	public int getTilesize() {
		return tileSize;
	}
}
