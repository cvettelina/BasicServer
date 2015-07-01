/*
 * $Id$
 *
 * Copyright 2013 Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.ServerApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.rmi.server.UID;

/**
 * Handles HTTP requests and HTTP responses.
 *
 *
 *
 */
public class RequestHandler extends Thread {
    HTTPResponse response;
    HTTPRequest clientRequest;
    TCPCommunication communication;
    FileHTMLReader reader;

    public RequestHandler(TCPCommunication communication) {
        this.communication = communication;
        this.response = new HTTPResponse();
        this.clientRequest = new HTTPRequest(communication.getRequest());
        this.reader = new FileHTMLReader();
    }

    /**
     * for each request the class creates a new thread to handle the request
     */
    @Override
    public void run() {
        if ("POST".equals(clientRequest.getRequestType())) {
            post(clientRequest);
        } else if ("GET".equals(clientRequest.getRequestType())) {
            get(clientRequest);
        } else {
            communication.sendResponse(response.buildResponse(500, "Internal error\r\n", "/error.html"));
        }
    }

    /**
     * Handles the GET method
     *
     * @param request
     *            contains the full request in String
     *
     */
    private void get(HTTPRequest request) {
        String uri = request.getUri();
        if ("login.html".equals(uri)) {
            communication.sendResponse(response.buildResponse(200, reader.getHTML("login.html")));
        } else if ("menu.html".equals(uri)) {
            communication.sendResponse(response.buildResponse(200, reader.getHTML("menu.html")));
        } else if ("send.html".equals(uri)) {
            communication.sendResponse(response.buildResponse(200, reader.getHTML("send.html")));
        } else if ("register.html".equals(uri)) {
            communication.sendResponse(response.buildResponse(200, reader.getHTML("register.html")));
        } else if ("error.html".equals(uri)) {
            communication.sendResponse(response.buildResponse(200, reader.getHTML("error.html")));
        } else if ("styles.css".equals(uri)) {
            communication.sendResponse(response.buildResponse(200, reader.getHTML("styles.css")));
        } else {
            communication.sendResponse(response.buildResponse(200, reader.getHTML("index.html")));
        }

    }

    /**
     * Handles the POST method
     *
     * @param request
     *            contains the full request in string
     */
    private void post(HTTPRequest request) {
        String body = request.getBody();
        String uri = request.getUri();
        String uid = request.getId();
        UID id = new UID();
        if (body == null) {
            communication.sendResponse(response.buildResponse(404, "There is no body\r\n", "/error.html"));
            return;
        }
        if ("index.html".equals(uri)) {
            if ("register".equals(request.getBodyInfo("button"))) {
                communication.sendResponse(response.buildResponse(302, "Enter register form\r\n", "/register.html"));
                return;
            } else {
                communication.sendResponse(response.buildResponse(302, "Please log in\r\n", "/login.html"));
                return;
            }
        }
        if (body.matches("^username=[a-zA-Z][a-zA-Z0-9]+&password=[a-zA-Z0-9]+$")) {
            User user = new User(request.getBodyInfo("username"), request.getBodyInfo("password"));
            if ("register.html".equals(uri)) {
                if (UserManager.isUserRegistered(user)) {
                    communication.sendResponse(response.buildResponse(400, "The user " + user.getUsername() + " is already registered\r\n"));
                } else {
                    UserManager.registerUser(user);
                    communication
                            .sendResponse(response.buildResponse(302, "Your registeration is successfull. Please log in\r\n", "/login.html"));
                }
            } else if ("login.html".equals(uri)) {
                if (UserManager.isUserRegistered(user)) {
                    if (UserManager.checkUserExists(user)) {
                        communication.sendResponse(response.buildResponse(302, "The user " + user.getUsername() + " is already logged\r\n",
                                "/error.html"));
                    } else if (UserManager.checkUserData(user, user.getPassword())) {
                        UserManager.addUser(user, id.toString());
                        communication.sendResponse(response.buildResponse(302, "set cookie", id.toString(), "/menu.html"));
                    } else {
                        communication.sendResponse(response.buildResponse(404, "Wrong password\r\n"));
                    }
                } else {
                    communication.sendResponse(response.buildResponse(404, "User is not registered\r\n"));
                }
            }
        } else if (body.matches("^button=[a-zA-Z]{4}$")) {
            String button = request.getBodyInfo("button");
            if ("read".equals(button)) {
                String messages = UserManager.getUnreadMessages(uid);
                communication.sendResponse(response.buildResponse(200, reader.getHTML("msg.html", messages)));
            } else if ("send".equals(button)) {
                communication.sendResponse(response.buildResponse(302, "send", "/send.html"));
            } else if ("exit".equals(button)) {
                UserManager.logOut(uid);
                communication.sendResponse(response.buildResponse(302, "You are logged out\r\n", "/login.html"));
            }
        } else if (body.matches("^message=.+$")) {
            User sender = UserManager.getUserByID(uid);
            if (sender == null) {
                communication.sendResponse(response.buildResponse(404, "Please log in\r\n"));
            } else {
                String msg = null;
                try {
                    msg = URLDecoder.decode(request.getBodyInfo("message"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                UserManager.addMessage("<span>" + sender.getUsername() + ": </span> " + msg);
                communication.sendResponse(response.buildResponse(200, "Your message was sent successfully\r\n"));
            }
        } else {
            communication.sendResponse(response.buildResponse(200, "wrong input\r\n", "/error.html"));
        }

    }
}
