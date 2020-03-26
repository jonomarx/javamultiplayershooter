/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.net;

import com.jonmarx.game.Game;
import com.jonmarx.game.Main;
import com.jonmarx.game.level.Bullet;
import com.jonmarx.game.level.TankMP;
import com.jonmarx.net.packets.Packet;
import com.jonmarx.net.packets.Packet00Login;
import com.jonmarx.net.packets.Packet01Disconnect;
import com.jonmarx.net.packets.Packet02Info;
import com.jonmarx.net.packets.Packet04Message;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jon
 */
public class GameClient extends Thread {
    private InetAddress ipAddress;
    private DatagramSocket socket;
    private Game game;
    public List<TankMP> connectedPlayers = new ArrayList<>();
    
    public GameClient(Game game, String ipAddress) {
        this.game = game;
        try {
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ipAddress);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    
    public byte[] poll(Packet p) {
        p.writeData(this);
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        try {
            socket.receive(packet);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return packet.getData();
    }
    
    public void run() {
        while(true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String message = new String(packet.getData());
            parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }
    
    public void sendData(byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        switch(type) {
            default:
            case INVALID:
                System.err.println("Name already taken, reload program");
                System.exit(1);
                Runtime.getRuntime().removeShutdownHook(Game.hook);
                break;
            case LOGIN:
                Packet00Login packet = new Packet00Login(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet.getUsername() + " has connected!");
                
                TankMP player = new TankMP(15, 15, packet.getUsername(), address, port, packet.getTeam());
                boolean exit = false;
                for(TankMP lplayer : connectedPlayers) {
                    if(lplayer.name.equals(packet.getUsername())) {
                        exit = true;
                        break;
                    }
                }
                if(exit) break;
                if(player != null) {
                    player.init(game);
                    this.connectedPlayers.add(player);
                    game.getLevel().addEntity(player);
                }
                break;
            case DISCONNECT:
                Packet01Disconnect dpacket = new Packet01Disconnect(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] " + dpacket.getUsername() + " has disconnected!");
                player = null;
                TankMP[] playerscopy = new TankMP[connectedPlayers.size()]; 
                this.connectedPlayers.toArray(playerscopy);
                for(TankMP dplayer : playerscopy) {
                    if(dplayer.name.equals(dpacket.getUsername())) {
                        connectedPlayers.remove(dplayer);
                        game.getLevel().removeEntity(dplayer);
                    }
                }
                break;
            case INFO:
                Packet02Info ipacket = new Packet02Info(data);
                for(TankMP iplayer : connectedPlayers) {
                    if(ipacket.getUsername().equals(iplayer.name)) {
                        iplayer.setX(ipacket.getX());
                        iplayer.setY(ipacket.getY());
                        iplayer.rot = ipacket.getRot();
                    }
                }
                break;
            case SPAWN:
                Bullet b = new Bullet(data);
                b.init(game);
                game.getLevel().addEntity(b);
                break;
            case MESSAGE:
                Packet04Message mpacket = new Packet04Message(data);
                if(mpacket.getMessage().split("\\|").length == 2 && mpacket.getMessage().startsWith("kill")) {
                    for(TankMP t : connectedPlayers) {
                        if(mpacket.getName().equals(t.name)) {
                            t.kills = Integer.parseInt(mpacket.getMessage().split("\\|")[1]);
                        }
                    }
                } else if(mpacket.getMessage().equals("kill")) {
                    if(mpacket.getName().equals(Game.username)) {
                        Game.xp += 100;
                    }
                    for(TankMP t : connectedPlayers) {
                        if(mpacket.getName().equals(t.name)) {
                            t.kills++;
                        }
                    }
                } else if(mpacket.getMessage().split("\\|").length == 5) {
                    String[] lines = mpacket.getMessage().split("\\|");
                    String[] board = new String[] {lines[0], lines[1], lines[2], lines[3], lines[4]};
                    Main.setLeaderBoard(board);
                }
                break;
        }
    }
}
