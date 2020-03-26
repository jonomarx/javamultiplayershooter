/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game.mods;

import com.jonmarx.game.Game;
import java.awt.Graphics;

/**
 *
 * @author Jon
 */
public abstract class Mod {
    public abstract void update();
    public abstract void render(Graphics g);
    
    public abstract void load(Game g);
    public abstract void unload();
}
