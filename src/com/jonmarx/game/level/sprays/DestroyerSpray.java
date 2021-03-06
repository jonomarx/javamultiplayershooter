package com.jonmarx.game.level.sprays;

import com.jonmarx.game.Game;
import com.jonmarx.game.level.Bullet;
import com.jonmarx.game.level.Tank;

public class DestroyerSpray extends SprayType{
    public DestroyerSpray(Game g) {
        super(g);
    }
    int cooldown = 50;
    int count = cooldown;

    @Override
    public Bullet[] fireBullet(double x, double y, double radians, double speed, Tank t) {
        count--;
        if(count < 1) {
            double num = 0;
            radians += Math.toRadians(num);
            Bullet b = new Bullet(x, y, Math.sin(-radians) * speed, Math.cos(radians) * speed, 3, 3, t, 200, 0);
            //b.init(game);
            //game.getLevel().addEntity(b);
            count = cooldown;
            return new Bullet[] {b};
        }
        return null;
    }
    
    public void reload() {}
}