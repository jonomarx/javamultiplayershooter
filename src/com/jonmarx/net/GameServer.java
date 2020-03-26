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
import com.jonmarx.net.packets.Packet.PacketTypes;
import com.jonmarx.net.packets.Packet00Login;
import com.jonmarx.net.packets.Packet01Disconnect;
import com.jonmarx.net.packets.Packet02Info;
import com.jonmarx.net.packets.Packet03Spawn;
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

/**
 *
 * @author Jon
 */
public class GameServer extends Thread {
    private DatagramSocket socket;
    private Game game;
    long seed = System.currentTimeMillis();
    private List<TankMP> connectedPlayers = new ArrayList<>();
    public int gamemode;
    // 0 = ffa
    // 1 == teams
    
    public void setSeed(long seed) {
        this.seed = seed;
    }
    
    public GameServer(Game game, int gamemode) {
        this.game = game;
        try {
            this.socket = new DatagramSocket(1331);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.gamemode = gamemode;
    }
    public GameServer(Game game, long seed, int gamemode) {
        this(game, gamemode);
        this.seed = seed;
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
            parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }
    
    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDataToAllClients(byte[] data) {
        for(TankMP p : connectedPlayers) {
            sendData(data, p.ipAddress, p.port);
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        switch(type) {
            case INVALID:
                break;
            case LOGIN:
                Packet00Login packet = new Packet00Login(data);
                
                int team = 0;
                
                if(gamemode == 1) {
                    int redPlayers = 0;
                    int bluePlayers = 0;
                    for(TankMP player : connectedPlayers) {
                        if(player.team == 1) {
                            bluePlayers++;
                        } else if(player.team == 2) {
                            redPlayers++;
                        }
                    }
                    if(redPlayers > bluePlayers) {
                        team = 1;
                    } else if(redPlayers < bluePlayers) {
                        team = 2;
                    } else if(redPlayers == bluePlayers) {
                        team = 1;
                    }
                } else if(gamemode == 0) {
                    team = 0;
                }
                
                System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet.getUsername() + " has connected!");
                TankMP player = null;
                if(false) {
                    player = new TankMP(Main.LISTENER, Main.MOUSE, 15, 15, packet.getUsername(), address, port);
                } else {
                    player = new TankMP(15, 15, packet.getUsername(), address, port, team);
                }
                boolean exit = false;
                for(TankMP cPlayer : connectedPlayers) {
                    if(cPlayer.name.equals(packet.getUsername())) {
                        sendData("-1".getBytes(), address, port);
                        exit = true;
                    }
                }
                if(exit) break;
                
                if(player != null) {
                    player.init(game);
                    this.connectedPlayers.add(player);
                }
                for(TankMP conPlay : connectedPlayers) {
                    Packet00Login login = new Packet00Login(conPlay.name, conPlay.team);
                    sendDataToAllClients(login.getData());
                    
                    Packet04Message m = new Packet04Message(conPlay.name, "kill|"+conPlay.kills);
                    sendData(m.getData(), address, port);
                }
                sendLeaderboards();
                break;
            case DISCONNECT:
                Packet01Disconnect dpacket = new Packet01Disconnect(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] " + dpacket.getUsername() + " has disconnected!");
                for(TankMP dplayer : this.connectedPlayers.toArray(new TankMP[0])) {
                    if(dplayer.name.equals(dpacket.getUsername())) {
                        connectedPlayers.remove(dplayer);
                        game.getLevel().removeEntity(dplayer);
                    }
                }
                dpacket.writeData(this);
                sendLeaderboards();
                break;
            case INFO:
                Packet02Info ipacket = new Packet02Info(data);
                ipacket.writeData(this);
                break;
            case SPAWN:
                Bullet b = new Bullet(data);
                Packet03Spawn spacket = new Packet03Spawn(b);
                spacket.writeData(this);
                break;
            case MESSAGE:
                Packet04Message mpacket = new Packet04Message(data);
                mpacket.writeData(this);
                if(mpacket.getMessage().split("\\|").length == 2) {
                    for(TankMP t : connectedPlayers) {
                        if(mpacket.getName().equals(t.name)) {
                            t.kills = Integer.parseInt(mpacket.getMessage().split("\\|")[1]);
                        }
                    }
                } else if(mpacket.getMessage().equals("kill")) {
                    for(TankMP t : connectedPlayers) {
                        if(mpacket.getName().equals(t.name)) {
                            t.kills++;
                        }
                    }
                } else if (mpacket.getMessage().equals("seed")) {
                    Packet04Message m = new Packet04Message("person", "seed|" + seed);
                    this.sendData(m.getData(), address, port);
                }
                sendLeaderboards();
                break;
        }
    }
    
    private void sendLeaderboards() {
        TankMP[] players = connectedPlayers.toArray(new TankMP[connectedPlayers.size()]);
        Arrays.sort(players);
        String[] leaders = new String[5];
        if(gamemode == 0) {
            for(int i = 0; i < 5; i++) {
                try {
                    leaders[i] = players[i].name + " kills: " + players[i].kills; 
                } catch(ArrayIndexOutOfBoundsException e) {
                    leaders[i] = "No one here :(";
                }
            }
        } else if(gamemode == 1) {
            int redkills = 0;
            int bluekills = 0;
            for(TankMP p : connectedPlayers) {
                if(p.team == 1) {
                    bluekills += p.kills;
                } else if(p.team == 2) {
                    redkills += p.kills;
                }
            }
            if(bluekills > redkills) {
                leaders[0] = "Blue kills: " + bluekills;
                leaders[1] = "Red kills: " + redkills;
            } else {
                leaders[0] = "Red kills: " + redkills;
                leaders[1] = "Blue kills: " + bluekills;
            }
            for(int i = 0; i < 3; i++) {
                try {
                    leaders[i + 2] = (players[i].team == 1 ? "(Blue)" : "(Red)") + " " + players[i].name + " kills: " + players[i].kills; 
                } catch(ArrayIndexOutOfBoundsException e) {
                    leaders[i + 2] = "No one here :(";
                }
            }
        }
        String board = "";
        for(String str : leaders) {
            board += str;
            board += "|";
        }
        board = board.substring(0, board.length() - 1);
        Packet04Message message = new Packet04Message("leaderboard", board);
        message.writeData(this);
    }
}

