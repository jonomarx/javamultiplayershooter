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
public class Packet00Login extends Packet {

    private String username;
    private int team;
    
    public Packet00Login(byte[] data) {
        super(00);
        this.username = readData(data).split("\\|")[0];
        this.team = Integer.parseInt(readData(data).split("\\|")[1]);
    }
    
    public Packet00Login(String username, int team) {
        super(00);
        this.username = username;
        this.team = team;
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
        return ("00" + this.username + "|" + team).getBytes();
    }
    
    public String getUsername() {
        return username;
    }
    
    public int getTeam() {
        return team;
    }
}
