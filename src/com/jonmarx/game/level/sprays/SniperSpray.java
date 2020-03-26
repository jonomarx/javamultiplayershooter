/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game.level.sprays;

import com.jonmarx.game.Game;
import com.jonmarx.game.level.Bullet;
import com.jonmarx.game.level.Tank;

/**
 *
 * @author Jon
 */
public class SniperSpray extends SprayType {
    int cooldown;
    int count;
    
    public SniperSpray(Game g) {
        super(g);
        cooldown = 100;
        count = cooldown;
        magsize = 3;
        magreload = 150;
        reload = magreload;
        mag = magsize;
    }

    @Override
    public Bullet[] fireBullet(double x, double y, double radians, double speed, Tank t) {
        speed *= 1.2; // snipeer bullets are faster
        if(mag < 1) {
            if(reload < 1) {
                mag = magsize;
                reload = magreload;
            } else {
                reload();
                return null;
            }
        }
        count--;
        if(count < 1) {
            double num = Math.random() * 0.5 - 0.25;
            radians += Math.toRadians(num);
            Bullet b = new Bullet(x, y, Math.sin(-radians) * speed, Math.cos(radians) * speed, 0.75, 0.25, t, 50, 80);
            //b.init(game);
            //game.getLevel().addEntity(b);
            count = cooldown;
            mag--;
            return new Bullet[] {b};
        }
        return null;
    }
    public void reload() {
        if(reload >= 1 && mag < 1) {
            reload--;
        } else if(reload < 1 && mag < 1){
            mag = magsize;
            reload = magreload;
        } else if(count > 1 && count > 1) {
            count--;
        }
    }
}
