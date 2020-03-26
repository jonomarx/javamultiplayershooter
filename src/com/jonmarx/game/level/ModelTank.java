/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game.level;

import com.jonmarx.game.Game;
import com.jonmarx.game.Listener;
import com.jonmarx.game.Main;
import static com.jonmarx.game.Main.spawnLocations;
import com.jonmarx.game.Mouse;
import com.jonmarx.level.tiles.ColorTile;
import com.jonmarx.net.packets.Packet02Info;
import com.jonmarx.net.packets.Packet03Spawn;
import com.jonmarx.net.packets.Packet04Message;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

/**
 *
 * @author Jon
 */
public class ModelTank extends Tank {
    
    public ModelTank(double x, double y, Listener listener, Mouse mouse) {
        super(x, y, listener, mouse);
    }
    
    @Override
    public void render(Graphics g) {
        
    }
    
    @Override
    public void update() {
        double rot = Math.atan2(Main.HEIGHT/2 - mouse.getY(), Main.WIDTH/2 - mouse.getX());
        Color green = new Color(0, 255, 0);
        
        double ex = 0;
        double ey = 0;
        if((listener.isKeyPressed(KeyEvent.VK_W) || listener.isKeyPressed(KeyEvent.VK_UP)) && Game.player.getY() > 1) {
            if(((ColorTile)game.getLevel().getTile((int)Math.round(Game.player.getX()), (int)Math.round(Game.player.getY() - 0.25))).getColor().getGreen() == 255) {
                ey -= 0.25;
            }
        } if((listener.isKeyPressed(KeyEvent.VK_A) || listener.isKeyPressed(KeyEvent.VK_LEFT)) && Game.player.getX() > 1) {
            if(((ColorTile)game.getLevel().getTile((int)Math.round(Game.player.getX() - 0.25), (int)Math.round(Game.player.getY()))).getColor().getGreen() == 255) {
                ex -= 0.25;
            }
        } if((listener.isKeyPressed(KeyEvent.VK_S) || listener.isKeyPressed(KeyEvent.VK_DOWN)) && Game.player.getY() < 61) {
            if(((ColorTile)game.getLevel().getTile((int)Math.round(Game.player.getX()), (int)Math.round(Game.player.getY() + 0.25))).getColor().getGreen() == 255) {
                ey += 0.25;
            }
        } if((listener.isKeyPressed(KeyEvent.VK_D) || listener.isKeyPressed(KeyEvent.VK_RIGHT)) && Game.player.getX() < 61) {
            if(((ColorTile)game.getLevel().getTile((int)Math.round(Game.player.getX()), (int)Math.round(Game.player.getY() + 0.25))).getColor().getGreen() == 255) {
                ex += 0.25;
            }
        }
        if(Main.MOUSE.isClicked()) {
            double radians = rot + Math.toRadians(-275);
            double xx = Math.sin(-radians) * 2;
            double yy = Math.cos(-radians) * 2;
            double finx = Game.player.getX() + 1 + xx;
            double finy = Game.player.getY() + 1 + yy;
            Bullet[] bu = Game.player.spray.fireBullet(finx, finy, radians, speed, Game.player);
            if(bu == null) bu = new Bullet[0];
            for(Bullet b : bu) {
                if(b==null) continue;
                Packet03Spawn packet2 = new Packet03Spawn(b);
                packet2.writeData(Game.socketClient);
            }
        } else {
            Game.player.spray.reload();
        }
        if(listener.isKeyPressed(KeyEvent.VK_R) && (Game.player.spray.reload == Game.player.spray.magreload)) {
            Game.player.spray.mag = 0;
            Game.player.spray.reload = Game.player.spray.magreload;
        }
        Packet02Info packet = new Packet02Info(Game.username, Game.player.getX() + ex, Game.player.getY() + ey, rot);
        packet.writeData(Game.socketClient);
        ColorTile tile = (ColorTile) game.getLevel().getTile((int)Game.player.getX(), (int)Game.player.getY());
        Color xptile = new Color(255, 255, 0);
        if(tile != null && tile.getColor().equals(xptile)) {
            Game.xp += 0.1;
        }
    }
    public static void respawn(Tank killer) {
        Point2D spawnPoint = spawnLocations[(int) Math.round(Math.random() * (spawnLocations.length - 1))];
        Packet02Info packet = new Packet02Info(Game.username, spawnPoint.getX(), spawnPoint.getY(), 0);
        packet.writeData(Game.socketClient);
        if(killer != null) {
            String name = ((TankMP) killer).name;
            Packet04Message message = new Packet04Message(name, "kill");
            message.writeData(Game.socketClient);
            Packet04Message m = new Packet04Message(Game.username, "kill|0");
            m.writeData(Game.socketClient);
        }
        Game.xp = 0;
    }
}
