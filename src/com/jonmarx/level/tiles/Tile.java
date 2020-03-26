/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.level.tiles;

import com.jonmarx.game.Game;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jon
 */
public abstract class Tile {
    protected int x;
    protected int y;
    protected BufferedImage b;
    protected boolean isSolid;
    protected Game game;
    
    public Tile(int x, int y, BufferedImage b, boolean isSolid) {
        this.x = x;
        this.y = y;
        this.b = b;
        this.isSolid = isSolid;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public abstract void render(Graphics g);
    public synchronized void render(Graphics g, double xoff, double yoff) {
        int x2 = x;
        int y2 = y;
        x += xoff;
        y += yoff;
        render(g);
        x = x2;
        y = y2;
    }
    public abstract void update();
    public void init(Game g) {
        this.game = g;
    }
}
