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
public abstract class Packet {
    
    public static enum PacketTypes {
        INVALID(-1), LOGIN(00), DISCONNECT(01), INFO(02), SPAWN(03), MESSAGE(04);
        
        private int packetId;
        
        private PacketTypes(int packetId) {
            this.packetId = packetId;
        }
        
        public int getId() {
            return packetId;
        }
    }
    
    public byte packetId;
    
    public Packet(int packetId) {
        this.packetId = (byte) packetId;
    }
    
    public abstract void writeData(GameClient client);
    
    public abstract void writeData(GameServer server);
    
    public String readData(byte[] data) {
        String message = new String(data).trim();
        return message.substring(2);
    }
    
    public abstract byte[] getData();
    
    public static PacketTypes lookupPacket(String packetId) {
        try {
            return lookupPacket(Integer.parseInt(packetId));
        } catch(NumberFormatException e) {
            return PacketTypes.INVALID;
        }
    }
    
    public static PacketTypes lookupPacket(int id) {
        for(PacketTypes p : PacketTypes.values()) {
            if(p.getId() == id) {
                return p;
            }
        }
        return PacketTypes.INVALID;
    }
}
