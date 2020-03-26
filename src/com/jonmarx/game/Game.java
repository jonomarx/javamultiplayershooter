package com.jonmarx.game;

import static com.jonmarx.game.Main.spawnLocations;
import com.jonmarx.game.level.ModelTank;
import com.jonmarx.game.level.Tank;
import com.jonmarx.game.level.TankMP;
import com.jonmarx.game.level.sprays.ARSpray;
import com.jonmarx.game.level.sprays.HMGSpray;
import com.jonmarx.game.level.sprays.LMGSpray;
import com.jonmarx.game.level.sprays.SMGSpray;
import com.jonmarx.game.level.sprays.ShotgunSpray;
import com.jonmarx.game.level.sprays.SniperSpray;
import com.jonmarx.game.mods.Core;
import com.jonmarx.game.mods.ModLoader;
import com.jonmarx.level.Level;
import com.jonmarx.level.entities.Entity;
import com.jonmarx.level.tiles.ColorTile;
import com.jonmarx.level.tiles.Tile;
import com.jonmarx.net.GameClient;
import com.jonmarx.net.GameServer;
import com.jonmarx.net.packets.Packet00Login;
import com.jonmarx.net.packets.Packet01Disconnect;
import com.jonmarx.net.packets.Packet04Message;
import com.perlin.ImprovedNoise;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Jon
 */
public class Game extends JPanel {
    private double xLoc = 0;
    private double yLoc = 0;
    private final Level level;
    public static String username;
    public static GameClient socketClient;
    public static Tank player;
    public static Thread hook;
    public static double xp = 0;
    private ModLoader modLoader;
    
    public Game(Level l) throws UnknownHostException, InterruptedException {
        this.level = l;
        if(JOptionPane.showConfirmDialog(this, "Startup the server?") == JOptionPane.YES_OPTION) {
            String[] options = new String[] {"FFA", "TDM"};
            int response = JOptionPane.showOptionDialog(this, "Chose a gamemode", "Choose",
            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
            null, options, options[0]);
            if(response != 0 && response != 1) {
                System.err.println("Invalid option");
                System.exit(2);
            }
            GameServer server = new GameServer(this, response);
            try {
                long seed = Long.parseLong(JOptionPane.showInputDialog(this, "Pick a seed (-1 for random seed)"));
                if(seed != -1) {
                    server.setSeed(seed); 
                } else {
                    server.setSeed((int) Math.floor(Math.random() * 200000));
                }
            } catch(NumberFormatException e) {
                
            }
            server.start();
        }
        
        socketClient = new GameClient(this, JOptionPane.showInputDialog(this, "What's the host?"));
        
        Packet04Message packet = new Packet04Message(socketClient.poll(new Packet04Message("person", "seed")));
        tileSetup(Long.parseLong(packet.getMessage().split("\\|")[1]));
        
        username = JOptionPane.showInputDialog(this, "Pick a name");
        Packet00Login loginPacket = new Packet00Login(username, 0);
        loginPacket.writeData(socketClient);
        socketClient.start();
        ModelTank.respawn(null);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Core.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        for(TankMP play : socketClient.connectedPlayers) {
            if(play.name.equals(username)) {
                player = play;
                Main.player = player;
            }
        }
        hook = new Thread(new Runnable() {
            public void run() {
                Packet01Disconnect disconnectPacket = new Packet01Disconnect(username);
                disconnectPacket.writeData(socketClient);
            }
        });
        Runtime.getRuntime().addShutdownHook(hook);
        
        for(Tile[] ti : this.getLevel().getTiles()) {
            for(Tile t : ti) {
                t.init(this);
            }
        }
        for(Entity e: this.getLevel().getEntities()) {
            e.init(this);
        }
        String[] options = new String[] {"AR", "SMG", "LMG", "HMG", "Sniper", "Shotgun"};
        int response = JOptionPane.showOptionDialog(this, "Chose a gun", "Choose",
        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
        null, options, options[0]);
        switch(response) {
            case 0:
                player.spray = new ARSpray(this);
                break;
            case 1:
                player.spray = new SMGSpray(this);
                break;
            case 2:
                player.spray = new LMGSpray(this);
                break;
            case 3:
                player.spray = new HMGSpray(this);
                break;
            case 4:
                player.spray = new SniperSpray(this);
                break;
            case 5:
                player.spray = new ShotgunSpray(this);
                break;
        }
    }
    
    public void setScroll(double x, double y) {
        xLoc = x;
        yLoc = y;
    }
    
    public double getXScroll() {
        return xLoc;
    }
    
    public double getYScroll() {
        return yLoc;
    }
    
    
    @Override
    public void paint(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
        for(Tile[] ti : this.getLevel().getTiles()) {
            for(Tile t : ti) {
                if(t == null) continue;
                t.render(g, getXScroll(), getYScroll());
            }
        }
        for(Entity e: this.getLevel().getEntities()) {
            if(e == null) continue;
            e.render(g, getXScroll(), getYScroll());
        }
    }
    public void update() {
        for(Tile[] ti : this.getLevel().getTiles()) {
            for(Tile t : ti) {
                if(t == null) continue;
                t.update();
            }
        }
        Entity[] entities = this.getLevel().getEntities().clone();
        for(Entity e: entities) {
            if(e == null) continue;
            e.update();
            for(Entity ei : entities) {
                if(e.getShape().getBounds().intersects(ei.getShape().getBounds())) {
                    if(e == ei) continue;
                    e.collide(ei);
                    ei.collide(e); 
                }
            } 
        }
    }
    
    public Level getLevel() {
        return level;
    }
    
    public void drawTank(Graphics g, double x, double y, double rotation) {
        // taken from com.jonmarx.game.level.Tank
        Rectangle2D rect = new Rectangle2D.Double(-Main.SIZE/2., -Main.SIZE/2., Main.SIZE * 2, Main.SIZE / 2);
        AffineTransform transform = new AffineTransform();
        transform.translate(x * Main.SIZE, y * Main.SIZE);
        transform.rotate(Math.toRadians(45));
        Shape rotatedRect = transform.createTransformedShape(rect);
        g.setColor(Color.BLACK);
        Area area = new Area(rotatedRect);
        area.add(new Area(new Ellipse2D.Double(x * Main.SIZE, y * Main.SIZE, Main.SIZE * 2, Main.SIZE * 2)));
        transform = new AffineTransform();
        transform.rotate(rotation + Math.toRadians(-50), x * Main.SIZE + Main.SIZE, y * Main.SIZE + Main.SIZE);
        Area area2 = new Area(transform.createTransformedShape(area));
        
        ((Graphics2D) g).fill(area2);
    }
    private void tileSetup(long seed) {
        Tile[][] tiles = new Tile[63][63];
        LinkedList<Point2D> points = new LinkedList<>();
        if(seed != 0) {
            for(double i = 0; i < 63; i++) {
                for(double j = 0; j < 63; j++) {
                    double noise = ImprovedNoise.noise((i + seed)/20, (j)/20, 0);
                    Color c = null;
                    if(noise > 0.3) {
                        c = new Color(150, 75, 0);
                    } else if(noise > 0.25) {
                        c = new Color(250, 125, 0);
                    } else if(noise > 0.2) {
                        c = new Color(254, 254, 254);
                    } else {
                        c = new Color(0, 255, 0);
                    }
                    tiles[(int)i][(int)j] = new ColorTile((int)i, (int)j, c);
                    if(c.getGreen() == 255) {
                        points.add(new Point2D.Double(i, j));
                    }
                }
            }
        } else {
            for(int i = 0; i < 63; i++) {
                for (int j = 0; j < 63; j++) {
                    Color c = new Color(0, 255, 0);
                    tiles[(int)i][(int)j] = new ColorTile((int)i, (int)j, c);
                    points.add(new Point2D.Double(i, j));
                }
            }
            for(int i = 0; i < 10; i++) {
                for(int j = 0; j < 10; j++) {
                    Color c = new Color(255, 255, 0);
                    tiles[i + 25][j + 25] = new ColorTile(i + 25, j + 25, c);
                    points.remove(new Point2D.Double(i, j));
                }
            }
        }
        spawnLocations = points.toArray(new Point2D[points.size()]);
        this.getLevel().setTiles(tiles);
    }
}
