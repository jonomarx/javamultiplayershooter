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
public class ShotgunSpray extends SprayType {
    int cooldown;
    int count;
    int bulletnum;
    int bulletcount;
    int shotreload;
    int shotload;
    
    public ShotgunSpray(Game g) {
        super(g);
        cooldown = 30;
        count = cooldown;
        magsize = 3;
        magreload = 100;
        reload = magreload;
        mag = magsize;
    }

    @Override
    public Bullet[] fireBullet(double x, double y, double radians, double speed, Tank t) {
        speed *= 1.5; // shotgun bullets are faster
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
            int bullets = 5;
            double spray = 30;
            Bullet[] bL = new Bullet[bullets];
            for(int i = 0; i < bullets; i++) {
                double r = radians;
                r += Math.toRadians((spray / bullets) * i - (spray / 2));
                Bullet b = new Bullet(x, y, Math.sin(-r) * speed, Math.cos(r) * speed, 0.75, 0.25, t, 10, 40);
                bL[i] = b;
            }
            count = cooldown;
            mag--;
            return bL;
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
        if(shotreload > 0) {
            shotreload--;
        }
    }
}
