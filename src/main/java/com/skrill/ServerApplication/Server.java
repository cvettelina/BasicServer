/*
 * $Id$
 *
 * Copyright 2013 Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */

package com.skrill.ServerApplication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private final static int PORT = 5554;


    public static void main(String[] args) {
        new Server().startServer();
    }

    public void startServer() {
        ServerSocket serverSocket = null;
        TCPCommunication communication = null;
        Socket client = null;
        boolean listening = true;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + PORT);
            System.exit(-1);
        }
        while (listening) {
            try {
                client = serverSocket.accept();
                communication = new TCPCommunication(client);
                RequestHandler a = new RequestHandler(communication);
                a.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}