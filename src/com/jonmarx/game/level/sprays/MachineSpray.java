package com.jonmarx.game.level.sprays;

import com.jonmarx.game.Game;
import com.jonmarx.game.level.Bullet;
import com.jonmarx.game.level.Tank;

public class MachineSpray extends SprayType {
    public MachineSpray(Game g) {
        super(g);
    }
    int cooldown = 5;
    int count = cooldown;

    @Override
    public Bullet[] fireBullet(double x, double y, double radians, double speed, Tank t) {
        count--;
        if(count < 1) {
            double num = Math.random() * 60 - 30;
            radians += Math.toRadians(num);
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