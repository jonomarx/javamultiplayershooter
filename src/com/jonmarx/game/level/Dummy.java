package com.jonmarx.game.level;

import com.jonmarx.game.Main;
import com.jonmarx.level.entities.Entity;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jon
 */
public class Dummy extends Entity {

    public Dummy(double x, double y) {
        super(x, y, null, true);
    }

    @Override
    public void render(Graphics g) {
        if(redFrames > 0) {
            g.setColor(Color.red);
            redFrames--;
        } else {
            g.setColor(Color.BLACK);
        }
        g.fillOval((int) Math.round(x * Main.SIZE), (int) Math.round(y * Main.SIZE), Main.SIZE * 3, Main.SIZE * 3);

    }

    @Override
    public void update() {
        
    }
    int redFrames = 0;
    @Override
    public void collide(Entity e) {
        if(e instanceof Bullet) redFrames = 20;
    }

    @Override
    public Shape getShape() {
        return new Rectangle2D.Double(x * Main.SIZE, y * Main.SIZE, Main.SIZE * 2.5, Main.SIZE * 2.5);
    }
    
}
