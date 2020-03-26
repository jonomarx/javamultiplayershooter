/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.level.tiles;

import com.jonmarx.game.Main;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Color;

/**
 *
 * @author Jon
 */
public class ColorTile extends Tile {
    Color c;
    public ColorTile(int x, int y, Color c) {
        super(x, y, null, false);
        this.c = c;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(c);
        g.fillRect(x * Main.SIZE, y * Main.SIZE, Main.SIZE, Main.SIZE);
    }

    @Override
    public void update() {
        
    }
    public Color getColor() {
        return c;
    }
}
