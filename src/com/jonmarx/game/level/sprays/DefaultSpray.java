package com.jonmarx.game.level.sprays;

import com.jonmarx.game.Game;
import com.jonmarx.game.level.Bullet;
import com.jonmarx.game.level.Tank;

public class DefaultSpray extends SprayType {
    int cooldown = 15;
    int count = cooldown;

    public DefaultSpray(Game g) {
        super(g);
    }

    @Override
    public Bullet[] fireBullet(double x, double y, double radians, double speed, Tank t) {
        count--;
        if(count < 1) {
            Bullet b = new Bullet(x, y, Math.sin(-radians) * speed, Math.cos(radians) * speed, 1, 1, t, 200, 0);
            //b.init(game);
            //game.getLevel().addEntity(b);
            count = cooldown;
            return new Bullet[] {b};
        }
        return null;
    }
    
    public void reload() {}
}