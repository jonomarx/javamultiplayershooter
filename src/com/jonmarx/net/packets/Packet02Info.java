/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.net.packets;

import com.jonmarx.net.GameClient;
import com.jonmarx.net.GameServer;

/**
 *
 * @author Jon
 */
public class Packet02Info extends Packet {
    String username;
    double x;
    double y;
    double rot;
    boolean isHeld;
    
    public Packet02Info(byte[] data) {
        super(02);
        String line = readData(data);
        String[] segs = line.split("\\|");
        username = segs[0];
        try {
            x = Double.parseDouble(segs[1]);
            y = Double.parseDouble(segs[2]);
            rot = Double.parseDouble(segs[3]);
        } catch(NumberFormatException e) {
            e.printStackTrace();
        }
    }
    
    public Packet02Info(String username, double x, double y, double rot) {
        super(02);
        this.username = username;
        this.x = x;
        this.y = y;
        this.rot = rot;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        return ("02" + username + "|" + x + "|" + y + "|" + rot).getBytes();
    }
    
    public String getUsername() {
        return username;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public double getRot() {
        return rot;
    }
}
