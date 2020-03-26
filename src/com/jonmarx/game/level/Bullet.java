package com.jonmarx.game.level;

import com.jonmarx.game.Game;
import com.jonmarx.game.Main;
import com.jonmarx.level.entities.Entity;
import com.jonmarx.level.tiles.ColorTile;
import com.jonmarx.net.Transferable;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jon
 */
public class Bullet extends Entity implements Transferable {
    double speedx;
    double speedy;
    double sizex;
    double sizey;
    Tank owner;
    double damage;

    public Bullet(double x, double y, double speedx, double speedy, double sizex, double sizey, Tank owner, int life, int damage) {
        super(x, y, null, true);
        this.speedx = speedx;
        this.speedy = speedy;
        this.sizex = sizex;
        this.sizey = sizey;
        this.owner = owner;
        this.life = life;
        this.damage = damage;
    }
    
    public Bullet(byte[] data) {
        this(new String(data));
    }
    
    public Bullet(String data) {
        super(0, 0, null, true);
        parse(data);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        Ellipse2D rect = new Ellipse2D.Double(-Main.SIZE/2., -Main.SIZE/2., Main.SIZE * sizex, Main.SIZE * sizey);
        AffineTransform transform = new AffineTransform();
        transform.translate(x * Main.SIZE, y * Main.SIZE);
        transform.rotate(Math.atan2(speedy, speedx));
        Graphics2D g2 = (Graphics2D) g;
        g2.fill(transform.createTransformedShape(rect));
        //g.fillOval((int) Math.round(x * Main.SIZE), (int) Math.round(y * Main.SIZE), (int) Math.round(Main.SIZE * sizex), (int) Math.round(Main.SIZE * sizey));
    }
    int life;
    @Override
    public void update() {
        y += speedy;
        x += speedx;
        life--;
        if(life < 1) {
            game.getLevel().removeEntity(this);
        }
        if(game.getLevel().getTile((int)Math.floor(x)-1, (int)Math.floor(y)-1) == null) {
            return;
        }
        if(!(((ColorTile)game.getLevel().getTile((int)Math.floor(x)-1, (int)Math.floor(y)-1)).getColor().getGreen() >= 254)) {
            game.getLevel().removeEntity(this);
        }
    }

    @Override
    public void collide(Entity e) {
        if(e instanceof Tank && !(e instanceof ModelTank)) {
            if(((Tank) e) == owner) return;
            if(((TankMP) e).team == ((TankMP) owner).team && ((TankMP) owner).team != 0) return;
        }
        if(e instanceof Bullet) return;
        game.getLevel().removeEntity(this);
    }

    @Override
    public Shape getShape() {
        return new Rectangle2D.Double(x * Main.SIZE, y * Main.SIZE, Main.SIZE * sizex, Main.SIZE * sizey);
    }

    @Override
    public byte[] getData() {
        return ("Bullet|" + x + "|" + y + "|" + sizex + "|" + sizey + "|" + speedx + "|" + speedy + "|" + life + "|" + ((TankMP) owner).name + "|" + damage).getBytes();
    }
    public void parse(String data) {
        String[] parts = data.substring(2).trim().split("\\|");
        if(!parts[0].equals("Bullet")) {
            System.err.println("Invalid Parse for Bullet: " + data);
            return;
        }
        this.x = Double.parseDouble(parts[1]);
        this.y = Double.parseDouble(parts[2]);
        this.sizex = Double.parseDouble(parts[3]);
        this.sizey = Double.parseDouble(parts[4]);
        this.speedx = Double.parseDouble(parts[5]);
        this.speedy = Double.parseDouble(parts[6]);
        this.life = Integer.parseInt(parts[7]);
        this.damage = Double.parseDouble(parts[9]);
        for(TankMP player : Game.socketClient.connectedPlayers) {
            if(player instanceof TankMP) {
                if(((TankMP) player).name.equals(parts[8])) {
                    this.owner = player;
                    break;
                }
            }
        }
    }
}
