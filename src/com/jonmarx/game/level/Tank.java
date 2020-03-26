package com.jonmarx.game.level;

import com.jonmarx.game.level.sprays.SprayType;
import com.jonmarx.game.level.sprays.MachineSpray;
import com.jonmarx.game.Game;
import com.jonmarx.game.Listener;
import com.jonmarx.game.Main;
import com.jonmarx.game.Mouse;
import com.jonmarx.game.level.sprays.DefaultSpray;
import com.jonmarx.game.level.sprays.DestroyerSpray;
import com.jonmarx.game.level.sprays.ARSpray;
import com.jonmarx.game.level.sprays.SMGSpray;
import com.jonmarx.level.entities.Entity;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tank extends Entity {
    Listener listener;
    Mouse mouse;
    public double rot;
    public boolean isHeld = false;
    public double hits = 100; // actually health
    public int kills = 0;
    public double healspeed = 0.02;
    public double healphase = 0;
    
    public Tank(double x, double y, Listener listener, Mouse mouse) {
        super(x, y, null, true);
        this.listener = listener;
        this.mouse = mouse;
    }
    
    @Override
    public void render(Graphics g) {
        Rectangle2D rect = new Rectangle2D.Double(-Main.SIZE/2., -Main.SIZE/2., Main.SIZE * 2, Main.SIZE / 2);
        AffineTransform transform = new AffineTransform();
        transform.translate(x * Main.SIZE - (Main.SIZE / 2), y * Main.SIZE + (Main.SIZE / 2));
        transform.rotate(Math.toRadians(45));
        Shape rotatedRect = transform.createTransformedShape(rect);
        g.setColor(Color.BLACK);
        Area area = new Area(rotatedRect);
        area.add(new Area(new Ellipse2D.Double(x * Main.SIZE, y * Main.SIZE, Main.SIZE * 2, Main.SIZE * 2)));
        transform = new AffineTransform();
        transform.rotate(rot + Math.toRadians(-50), x * Main.SIZE + Main.SIZE, y * Main.SIZE + Main.SIZE);
        Area area2 = new Area(transform.createTransformedShape(area));
        
        ((Graphics2D) g).fill(area2);
    }
    int count = 15;
    double speed = 1.2;//global speed multiplier
    @Override
    public void update() {
        hits += healspeed * Math.pow(2, healphase);
        if(hits >= 100) {
            hits = 100;
            healphase = 0;
        } else {
            healphase += 0.004;
        }
        if(mouse != null) {
            isHeld = mouse.isClicked();
            rot = calculateAngle(15, 15, mouse.getX(), mouse.getY());
        }
        if(isHeld) {
            if(true) {
                double radians = rot + Math.toRadians(-275);
                double xx = Math.sin(-radians) * speed;
                double yy = Math.cos(radians) * speed;
                double finx = x + 2.75 * xx;
                double finy = y + 2.75 * yy;
                finx += 0.5;
                finy += 0.5;
                Bullet[] b = spray.fireBullet(finx, finy, radians, speed, this);
                if(b == null) b = new Bullet[0];
                for(Bullet bul : b) {
                    bul.init(game);
                    game.getLevel().addEntity(bul);
                }
                count = 15;
            } else {
                count--;
            }
        }
        if(listener == null) return;
        if((listener.isKeyPressed(KeyEvent.VK_W) || listener.isKeyPressed(KeyEvent.VK_UP)) && getY() != 1) {
            setX(getX());
            setY(getY() - 0.25);
        } else if((listener.isKeyPressed(KeyEvent.VK_A) || listener.isKeyPressed(KeyEvent.VK_LEFT)) && getX() != 1) {
            setX(getX() - 0.25);
            setY(getY());
        } else if((listener.isKeyPressed(KeyEvent.VK_S) || listener.isKeyPressed(KeyEvent.VK_DOWN)) && getY() != 61) {
            setX(getX());
            setY(getY() + 0.25);
        } else if((listener.isKeyPressed(KeyEvent.VK_D) || listener.isKeyPressed(KeyEvent.VK_RIGHT)) && getX() != 61) {
            setX(getX() + 0.25);
            setY(getY());
        }
    }
    public SprayType spray;
    @Override
    public void collide(Entity e) {
        if(e instanceof Bullet && ((Bullet) e).owner != this) {
            hits -= ((Bullet) e).damage;
            healphase = 0;
        }
        if(hits < 1 && e instanceof Bullet) {
            hits = 100;
            if(this == Game.player) ModelTank.respawn(((Bullet) e).owner);
        }
    }

    @Override
    public Shape getShape() {
        return new Rectangle2D.Double(x * Main.SIZE, y * Main.SIZE, Main.SIZE * 2, Main.SIZE * 2);
    }
    
    @Override
    public void init(Game g) {
        super.init(g);
        spray = new MachineSpray(game);
        spray = new ARSpray(game);
    }
    private double calculateAngle(double midx, double midy, double x2, double y2) {
        return Math.atan2(midy - y2 / Main.SIZE, midx - x2 / Main.SIZE);
    }
}
