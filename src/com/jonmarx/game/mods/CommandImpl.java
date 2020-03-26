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
abstract class CommandImpl {
    public abstract void execute(HashMap<String, Object> params);
}
