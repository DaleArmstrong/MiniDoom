package minidoom.game;

import minidoom.entity.Entity;
import minidoom.entity.components.Sprite;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Used for broad phase collision checking to minimize the amount of collision checks with
 * entities on the map. It will also be used along with the camera to minimize the amount
 * of entities to draw to the screen. This allows for the game to hold a lot more entities without
 * slowdown.
 * @param <T> the type of Entity this spatial hash will hold
 */
public class SpatialHash<T extends Entity> {
    private final int mapWidth;
    private final int mapHeight;
    private final int cellSize;             /* Size of each cell */
    private final double conversion;        /* Conversion constant */
    private final int buckets;              /* Number of Buckets in Spatial Hash */
    private final int cellWidth;            /* The width of a cell */
    private List<Set<T>> spatialHash;       /* SpatialHash of Entities */
    private boolean shadowCircle;           /* Flag to determine type of spatial hash check */
    private Set<Integer> cellList;
    private Set<T> entityList;

    public SpatialHash(int mapWidth, int mapHeight, int cellSize, boolean shadowCircle) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.cellSize = cellSize;
        this.shadowCircle = shadowCircle;
        this.cellList = new HashSet<>(7);
        this.entityList = new HashSet<>();

        this.cellWidth = (mapWidth + cellSize - 1) / cellSize;
        this.buckets = cellWidth * ((mapHeight + cellSize - 1) / cellSize);
        this.spatialHash = new ArrayList<>(buckets);
        for (int i = 0; i < buckets; i++)
            spatialHash.add(new HashSet<>());

        this.conversion = 1.0 / cellSize;
    }

    /* The hash returns the cell the position sits in */
    private int hashPosition(float x, float y) {
        if (x < 0)
            x = 0;
        else if (x >= mapWidth)
            x = mapWidth - 1;
        if (y < 0)
            y = 0;
        else if (y >= mapHeight)
            y = mapHeight - 1;
        return (int)(Math.floor(x * conversion) + Math.floor(y * conversion) * cellWidth);
    }

    /* Used to add an arraylist to the spatial hash */
    public void addEntityArray(List<? extends T> entities) {
        for (T entity : entities)
            addEntity(entity);
    }

    /* add the entity to the spatial hash */
    public void addEntity(T entity) {
        getEntityCells(entity);

        for (Integer cell : cellList)
            if (cell < buckets && cell >= 0)
                spatialHash.get(cell).add(entity);
    }

    /* Get the cells that the entity spans */
    public Set<Integer> getEntityCells(Entity entity) {
        Sprite sprite = entity.getSprite();
        cellList.clear();
        if (shadowCircle) {
            cellList.add(hashPosition(sprite.getX(), sprite.getY() + sprite.getHeight() - sprite.getHalfWidth()));
            cellList.add(hashPosition(sprite.getX() + sprite.getWidth(), sprite.getY() + sprite.getHeight() - sprite.getHalfWidth()));
            cellList.add(hashPosition(sprite.getX() + sprite.getWidth(), sprite.getY() + sprite.getHeight() + sprite.getHalfWidth()));
            cellList.add(hashPosition(sprite.getX(), sprite.getY() + sprite.getHeight() + sprite.getHalfWidth()));
        } else {
            cellList.add(hashPosition(sprite.getX(), sprite.getY()));
            cellList.add(hashPosition(sprite.getX(), sprite.getY() + sprite.getHeight()));
            cellList.add(hashPosition(sprite.getX() + sprite.getWidth(), sprite.getY()));
            cellList.add(hashPosition(sprite.getX() + sprite.getWidth(), sprite.getY() + sprite.getHeight()));
        }
        return cellList;
    }

    /* clears the spatial hash, ready to be refilled */
    public void clear() {
        for (Set<T> set : spatialHash)
            set.clear();
    }

    /* removes the specific entity from the spatial hash */
    public void remove(T entity) {
        getEntityCells(entity);
        for (Integer cell : cellList)
            if (cell >= 0 && cell < buckets)
            spatialHash.get(cell).remove(entity);
    }

    /* removes all of the entities in the provided list from the spatial hash */
    public void removeAll(List<? extends T> entities) {
        for (T entity : entities)
            remove(entity);
    }

    /* Returns a set of all entities in the given specific cells */
    public Set<T> getEntitiesInCells(Set<Integer> cells) {
        entityList.clear();
        for (Integer cell : cells)
            if (cell < buckets && cell >= 0)
                entityList.addAll(spatialHash.get(cell));

        return entityList;
    }

    /* Returns the entities near a specific entity */
    public Set<T> getEntitiesNear(Entity entity) {
        entityList.clear();
        getEntityCells(entity);
        for (Integer cell : cellList)
            if (cell >= 0 && cell < buckets)
            entityList.addAll(spatialHash.get(cell));

        return entityList;
    }

    /* returns all the entities a specific distance from a point, inclusive */
    public Set<T> getEntitiesByDistance(float x, float y, float distance) {
        int min = hashPosition(x - distance, y - distance);
        int max = hashPosition(x + distance, y + distance);
        return getCells(min, max);
    }

    /* returns the enties that fall within the rectangle bounds */
    public Set<T> getEntityByRange(Rectangle bounds, float offset) {
        int min = hashPosition(bounds.x - offset, bounds.y - offset);
        int max = hashPosition(bounds.x + bounds.width + offset, bounds.y + bounds.height + offset);
        return getCells(min, max);
    }

    /* Used to find all the entities within the min cell to max cell */
    private Set<T> getCells(int min, int max) {
        entityList.clear();
        int minX = (min == buckets ? cellWidth : min % cellWidth);
        int maxX = (max == buckets ? cellWidth : max % cellWidth);
        int minY = min / cellWidth;
        int maxY = max / cellWidth;

        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                entityList.addAll(spatialHash.get(i + j * cellWidth));
            }
        }
        return entityList;
    }
}
