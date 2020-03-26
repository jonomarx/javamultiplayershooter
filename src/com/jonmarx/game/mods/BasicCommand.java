/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game.mods;

import java.util.HashMap;

/**
 *
 * @author Jon
 */
public class BasicCommand extends Command {
    private boolean disabled = false;
    private CommandImpl impl;
    private HashMap<String, Object> map;
    private String name;
    
    public BasicCommand(CommandImpl impl, HashMap<String, Object> map, String name) {
        this.impl = impl;
        this.map = map;
        this.name = name;
    }

    @Override
    public void execute() {
        if(disabled) return;
        impl.execute(map);
    }

    @Override
    public void disable() {
        disabled = true;
    }
    
    @Override
    public String getName() {
        return name;
    }
}
