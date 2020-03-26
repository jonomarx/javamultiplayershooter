package com.jonmarx.game.level.sprays;

import com.jonmarx.game.Game;
import com.jonmarx.game.level.Bullet;
import com.jonmarx.game.level.Tank;

/**
 *
 * @author Jon
 */
public abstract class SprayType {
    Game game;
    public int magsize;
    public int mag;
    public int magreload;
    public int reload;
    
    public SprayType(Game g) {
        this.game = g;
    }
    public abstract Bullet[] fireBullet(double x, double y, double radians, double speed, Tank t); 
    public abstract void reload();
}
