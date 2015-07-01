/*
 * $Id$
 *
 * Copyright 2013 Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */

package com.skrill.ServerApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 *
 * TCPCommunication class handles input and output stream
 *
 */
public class TCPCommunication {
    private Socket socket;

    public TCPCommunication(Socket socket) {
        this.socket = socket;
    }

    /**
     * Sending response to the client
     *
     * @param response
     */
    public void sendResponse(String response) {
        OutputStreamWriter outToClient = null;
        try {
            outToClient = new OutputStreamWriter(socket.getOutputStream());
            outToClient.write(response);
            outToClient.flush();
            outToClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * getting the whole client's request
     *
     * @return
     */
    public String getRequest() {
        BufferedReader input = null;
        String[] content = null;
        String[] splittedHeader = null;
        StringBuilder clientRequest = new StringBuilder();
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String singleLine = null;
            do {
                singleLine = input.readLine();
                clientRequest.append(singleLine + "\r\n");
            } while ((singleLine.isEmpty() == false));
            splittedHeader = clientRequest.toString().split("Content-Length: ");
            if (splittedHeader.length > 1) {
                content = splittedHeader[1].split("\r\n");
                int contentLength = Integer.parseInt(content[0]);
                char[] requestInBytes = new char[contentLength];
                int bytesRead = 0;
                bytesRead = input.read(requestInBytes, 0, contentLength);
                String body = new String(requestInBytes, 0, bytesRead);
                clientRequest.append(body);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("User is trying to mess it up, but he can't");
        } catch (NullPointerException e) {
            System.out.println("User is trying to mess it up, but he can't");
        }
        System.out.println(clientRequest.toString());
        return clientRequest.toString();
    }
}
