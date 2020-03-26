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
public class Packet01Disconnect extends Packet {
    
    private String username;
    
    public Packet01Disconnect(byte[] data) {
        super(1);
        this.username = readData(data);
    }
    
    public Packet01Disconnect(String username) {
        super(1);
        this.username = username;
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
        return ("01" + username).getBytes();
    }
    
    public String getUsername() {
        return username;
    }
    
}
