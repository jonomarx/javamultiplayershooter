/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game.level;

import com.jonmarx.game.Game;
import com.jonmarx.game.Listener;
import com.jonmarx.game.Main;
import com.jonmarx.game.Mouse;
import com.jonmarx.level.entities.Entity;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.net.InetAddress;

/**
 *
 * @author Jon
 */
public class TankMP extends Tank implements Comparable<TankMP> {
    public String name;
    public InetAddress ipAddress;
    public int port;
    public int team;
    
    public TankMP(Listener l, Mouse mouse, double x, double y, String name, InetAddress address, int port) {
        super(x, y, l, mouse);
        this.name = name;
        this.ipAddress = address;
        this.port = port;
    }
    
    public TankMP(double x, double y, String name, InetAddress address, int port, int team) {
        super(x, y, null, null);
        this.name = name;
        this.ipAddress = address;
        this.port = port;
        this.team = team;
    }
    
    @Override 
    public void render(Graphics g) {
        Color c;
        switch(team) {
            case 0:
                c = Color.BLACK;
                break;
            case 1:
                c = Color.BLUE;
                break;
            case 2:
                c = Color.RED;
                break;
            default:
                c = Color.BLACK;
                break;
        }
        
        Rectangle2D rect = new Rectangle2D.Double(-Main.SIZE/2., -Main.SIZE/2., Main.SIZE * 2, Main.SIZE / 2);
        AffineTransform transform = new AffineTransform();
        transform.translate(x * Main.SIZE - 8, y * Main.SIZE + 8);
        transform.rotate(Math.toRadians(45));
        Shape rotatedRect = transform.createTransformedShape(rect);
        g.setColor(c);
        Area area = new Area(rotatedRect);
        area.add(new Area(new Ellipse2D.Double(x * Main.SIZE, y * Main.SIZE, Main.SIZE * 2, Main.SIZE * 2)));
        transform = new AffineTransform();
        transform.rotate(rot + Math.toRadians(-50), x * Main.SIZE + Main.SIZE, y * Main.SIZE + Main.SIZE);
        Area area2 = new Area(transform.createTransformedShape(area));
        
        ((Graphics2D) g).fill(area2);
        
        g.setColor(Color.BLACK);
        
        String text = "health: " + ((int) Math.floor(hits));
        String text2 = "";
        if(spray != null && spray.mag < 1) {
            text2 = "reloading: " + (100 - Math.floor((100 * spray.reload / spray.magreload))) + "%";
        } else if(spray != null && spray.mag > 0) {
            text2 += spray.mag + "/" + spray.magsize;
        }
        if(!Game.username.equals(name)) {
            text = "";
            text2 = "";
        }
        
        g.setFont(new Font("TimesRoman", Font.BOLD, 18));
        g.drawString(name + " kills: " + kills, (int) x * Main.SIZE, (int) (y-3) * Main.SIZE);
        g.drawString(text, (int) x * Main.SIZE, (int) (y-2) * Main.SIZE);
        g.drawString(text2, (int) x * Main.SIZE, (int) (y-1) * Main.SIZE);
    }
    
    @Override
    public void update() {
        super.update();
        if(Game.player == this) game.setScroll(-x + (224 / Main.SIZE), -y + (224 / Main.SIZE));
    }
    
    @Override
    public void collide(Entity e) {
        if(e instanceof Bullet && ((Bullet) e).owner != this) {
            if(((TankMP)((Bullet) e).owner).team != team) {
                hits -= ((Bullet) e).damage;
                healphase = 0;
            } else if (((TankMP) ((Bullet) e).owner).team == 0) {
                hits -= ((Bullet) e).damage;
                healphase = 0;
            }
        }
        if(hits < 1 && e instanceof Bullet) {
            hits = 100;
            if(this == Game.player) ModelTank.respawn(((Bullet) e).owner);
        }
    }

    @Override
    public int compareTo(TankMP o) {
        return ((Integer) o.kills).compareTo(kills);
    }
}
