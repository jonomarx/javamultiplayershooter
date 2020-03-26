package com.jonmarx.level;

import com.jonmarx.level.entities.Entity;
import com.jonmarx.level.tiles.Tile;
import java.util.List;
import java.util.LinkedList;

/**
 *
 * @author Jon
 */
public class Level {
    private Tile[][] tiles;
    private LinkedList<Entity> entities;
    
    public Level(Tile[][] tiles, List<Entity> entities) {
        this.tiles = tiles;
        this.entities = new LinkedList<>(entities);
    }
    
    public Tile[][] getTiles() {
        return tiles;
    }
    
    public Tile getTile(int x, int y) {
        if(x > tiles.length-1 || x < 0 || y > tiles.length-1 || y < 0) {
            return null;
        }
        return tiles[x][y];
    }
    
    public Entity[] getEntities() {
        return entities.toArray(new Entity[0]);
    }
    
    public void addEntity(Entity e) {
        entities.add(e);
    }
    
    public void removeEntity(Entity e) {
        entities.remove(e);
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }
}
