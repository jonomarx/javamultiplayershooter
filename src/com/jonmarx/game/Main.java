package com.jonmarx.game;

import com.jonmarx.game.level.Dummy;
import com.jonmarx.game.level.ModelTank;
import com.jonmarx.game.level.Tank;
import com.jonmarx.game.level.TankMP;
import com.jonmarx.game.level.sprays.ARSpray;
import com.jonmarx.game.level.sprays.SMGSpray;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import com.jonmarx.level.Level;
import com.jonmarx.level.entities.Entity;
import com.jonmarx.level.tiles.ColorTile;
import com.jonmarx.level.tiles.Tile;
import com.jonmarx.net.GameClient;
import com.jonmarx.net.GameServer;
import com.perlin.ImprovedNoise;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.JFrame;

/**
 *
 * @author Jon
 */
public class Main {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    public static volatile int SIZE = 14;
    public static final Listener LISTENER = new Listener();
    public static final Mouse MOUSE = new Mouse();
    public static Tank player;
    public static Point2D[] spawnLocations;
    private static String[] leaderBoard = new String[] {"", "", "", "", ""};
    
    public static void main(String[] args) throws IOException, InterruptedException {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.addKeyListener(LISTENER);
        frame.addMouseListener(MOUSE);
        frame.addMouseMotionListener(MOUSE);
        
        ArrayList<Entity> ens= new ArrayList<>();
        ens.add(new ModelTank(0, 0, Main.LISTENER, Main.MOUSE));
        
        Game game = new Game(new Level(new Tile[0][0], ens));
        frame.setContentPane(game);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);
        
        long makeupTime = 0;
        long currentTime;
        
        while(true) {
            currentTime = System.currentTimeMillis();
            render(game);
            update(game);
            
            try {
                Thread.sleep(20 - makeupTime);
            } catch (InterruptedException ex) {System.exit(0);}
            makeupTime = System.currentTimeMillis() - currentTime - (20 - makeupTime);
            if(makeupTime > 20) makeupTime = 20;
        }
    }
    static void render(Game g) {
        BufferedImage b = new BufferedImage(Main.HEIGHT, Main.WIDTH, BufferedImage.TYPE_INT_RGB);
        Graphics2D gr = (Graphics2D) g.getGraphics();
        Graphics g2 = b.createGraphics();
        g2.setColor(new Color(238, 238, 238));
        g2.fillRect(0, 0, Main.HEIGHT, Main.WIDTH);
        
        g.paint(b.getGraphics());
        g2.setColor(Color.GRAY);
        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        int spot = 30;
        for(String str : leaderBoard) {
            g2.drawString(str, Main.WIDTH - 200, spot);
            spot += 30;
        }
        
        gr.drawImage(b, 0, 0, null);
        Toolkit.getDefaultToolkit().sync();
    }
    static void update(Game g) {
        g.update();
    }
    
    public static synchronized void setLeaderBoard(String[] board) {
        leaderBoard = board;
    }
    
    public static String[] getLeaderBoard() {
        return leaderBoard;
    }
    
    public void changeSize(int size) {
        Main.SIZE = size;
    }
}
