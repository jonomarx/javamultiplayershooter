/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game.server;


import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jon
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(1234);
        LinkedList<SocketHandler> players = new LinkedList<>();
        
        Thread th = new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        String msg = "";
                        for(SocketHandler soh : players) {
                            msg += soh.getX() + "," + soh.getY() + "," + soh.getId() + "|";
                        }
                        for(SocketHandler soh : players) {
                            Socket socket = soh.getSocket();
                            if(socket.isClosed())  {
                                players.remove(soh);
                                continue;
                            }
                            PrintStream out = new PrintStream(socket.getOutputStream());
                            out.println(msg);
                        }
                    } catch(Exception e) {
                        System.out.println("FAILURE:" + e.getMessage());
                    }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        th.start();
        while(true) {
            Socket sock = server.accept();
            SocketHandler sh = new SocketHandler(sock, System.currentTimeMillis());
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try(InputStream in = sock.getInputStream(); PrintStream out = new PrintStream(sock.getOutputStream())) {
                        Scanner scan = new Scanner(in);
                        out.println(sh.getId());
                        players.add(sh);
                        while(scan.hasNextLine()) {
                            String[] line = scan.nextLine().trim().split("\\,");
                            sh.setX(new Integer(line[0]));
                            sh.setY(new Integer(line[1]));
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        players.remove(sh);
                    } 
                }
            });
            t.start();
        }
    }
}

class SocketHandler {
    Socket sock;
    int x = 0;
    int y = 0;
    long id;
    public SocketHandler(Socket s, long id) {
        sock = s;
        this.id = id;
    }
    public Socket getSocket() {
        return sock;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public long getId() {
        return id;
    }
}