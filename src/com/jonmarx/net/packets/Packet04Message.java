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
public class Packet04Message extends Packet {
    String name;
    String message;
    public Packet04Message(byte[] data) {
        super(04);
        String[] lines = readData(data).trim().split("\\|", 2);
        this.name = lines[0];
        this.message = lines[1];
    }
    
    public Packet04Message(String name, String message) {
        super(04);
        this.name = name;
        this.message = message;
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
        return ("04" + name + "|" + message).getBytes();
    }
    
    public String getName() {
        return name;
    }
    
    public String getMessage() {
        return message;
    }
    
}
