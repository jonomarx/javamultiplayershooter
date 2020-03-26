/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.level.entities;

import com.jonmarx.game.Game;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jon
 */
public abstract class Entity {
    protected double x;
    protected double y;
    protected BufferedImage b;
    protected boolean isSolid;
    protected Game game;
    
    public Entity(double x, double y, BufferedImage b, boolean isSolid) {
        this.x = x;
        this.y = y;
        this.b = b;
        this.isSolid = isSolid;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public boolean isSolid() {
        return isSolid;
    }
    
    public abstract void render(Graphics g);
    public synchronized void render(Graphics g, double xoff, double yoff) {
        x += xoff;
        y += yoff;
        render(g);
        x -= xoff;
        y -= yoff;
    }
    public abstract void update();
    public abstract void collide(Entity e);
    public abstract Shape getShape();
    public void init(Game g) {
        this.game = g;
    }
}
