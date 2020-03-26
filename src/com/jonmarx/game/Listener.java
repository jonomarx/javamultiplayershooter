/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

/**
 *
 * @author Jon
 */
public class Listener implements KeyListener{
    
    private volatile HashMap<Integer, Boolean> keys = new HashMap<>();
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys.put(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys.put(e.getKeyCode(), false);
    }
    
    public boolean isKeyPressed(int keycode) {
        if(keys.get(keycode) == null) return false;
        return keys.get(keycode);
    }
}
