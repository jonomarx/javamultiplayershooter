/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game.mods;

/**
 *
 * @author Jon
 */
abstract class Command {
    public abstract void execute();
    public abstract void disable();
    public abstract String getName();
}
