/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.net.packets;

import com.jonmarx.net.GameClient;
import com.jonmarx.net.GameServer;
import com.jonmarx.net.Transferable;

/**
 *
 * @author Jon
 */
public class Packet03Spawn extends Packet {
    Transferable object;
    public Packet03Spawn(Transferable object) {
        super(03);
        this.object = object;
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
        return ("03" + new String(object.getData())).getBytes();
    }
    
}
