/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game.mods;

import com.jonmarx.game.Game;
import java.awt.Graphics;
import java.util.LinkedList;

/**
 *
 * @author Jon
 */
public class ModLoader {
    private final LinkedList<Mod> loadedMods = new LinkedList<>();
    private final Game game;
    
    public ModLoader(Game game) {
        this.game = game;
    }
    
    public void loadMods(Mod[] mods) {
        for(Mod mod : mods) {
            loadMod(mod);
        }
    }
    
    public void loadMod(Mod mod) {
        loadedMods.add(mod);
        mod.load(game);
    }
    
    public void update() {
        for(Mod mod : loadedMods) {
            mod.update();
        }
    }
    
    public void render(Graphics g) {
        for(Mod mod : loadedMods) {
            mod.render(g);
        }
    }
}
