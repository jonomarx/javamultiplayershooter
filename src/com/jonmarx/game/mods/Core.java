/*
 * To change game license header, choose License Headers in Project Properties.
 * To change game template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game.mods;

import com.jonmarx.game.Game;
import static com.jonmarx.game.Game.hook;
import static com.jonmarx.game.Game.player;
import static com.jonmarx.game.Game.socketClient;
import static com.jonmarx.game.Game.username;
import com.jonmarx.game.Main;
import static com.jonmarx.game.Main.spawnLocations;
import com.jonmarx.game.level.ModelTank;
import com.jonmarx.game.level.TankMP;
import com.jonmarx.game.level.sprays.ARSpray;
import com.jonmarx.game.level.sprays.HMGSpray;
import com.jonmarx.game.level.sprays.LMGSpray;
import com.jonmarx.game.level.sprays.SMGSpray;
import com.jonmarx.game.level.sprays.ShotgunSpray;
import com.jonmarx.game.level.sprays.SniperSpray;
import com.jonmarx.level.entities.Entity;
import com.jonmarx.level.tiles.ColorTile;
import com.jonmarx.level.tiles.Tile;
import com.jonmarx.net.GameClient;
import com.jonmarx.net.GameServer;
import com.jonmarx.net.packets.Packet00Login;
import com.jonmarx.net.packets.Packet01Disconnect;
import com.jonmarx.net.packets.Packet04Message;
import com.perlin.ImprovedNoise;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Jon
 */
public class Core extends Mod {
    private Game game;
    @Override
    public void update() {
        
    }

    @Override
    public void render(Graphics gr) {
        
    }

    @Override
    public void load(Game g) {
        this.game = g;
        
    }

    @Override
    public void unload() {
        // nothing to unload
    }
    
    
}
