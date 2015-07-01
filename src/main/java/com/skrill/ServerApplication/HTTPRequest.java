/*
 * $Id$
 *
 * Copyright 2013 Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */

package com.skrill.ServerApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that will handle the received data on the socket.
 * Contains the body and the needed information in the header.
 *
 *
 *
 *
 */
public class HTTPRequest {
    private final String request;
    private String body;
    private String uri;
    private String requestType;
    private String id;
    private final Map<String, String> bodyInfo;

    /**
     *
     * @param request
     *            represents the client request in String
     */
    public HTTPRequest(String request) {
        this.request = request;
        bodyInfo = new HashMap<String, String>();
        this.initialize();
    }

    public String getRequest() {
        return this.request;
    }

    /**
     * Initialize the body and the needed info in the header
     */
    private void initialize() {
        String[] splitedData = this.request.split("/");
        String[] splitedBody;
        System.out.println(uri + " - uri 1");
        this.uri = splitedData[1].split(" ")[0];
        System.out.println(uri + " - uri 2");
        this.requestType = this.request.split(" ")[0];
        String bodyRequest = null;
        String[] headAndBody = this.request.split("\r\n\r\n");
        String[] splitedId = headAndBody[0].split("Cookie: ");
        if (splitedId.length > 1) {
            this.id = splitedId[1].split("\r\n")[0];
        }
        if (headAndBody.length > 1) {
            bodyRequest = headAndBody[1];
            this.body = bodyRequest;
        }
        if ("POST".equals(requestType)) {
            splitedBody = body.split("&");
            for (int i = 0; i < splitedBody.length; i++) {
                String[] splitedParameters = splitedBody[i].split("=");
                if (splitedParameters.length > 1) {
                    this.bodyInfo.put(splitedParameters[0], splitedParameters[1]);
                }
            }
        }
    }

    public String getBody() {
        return this.body;
    }

    public String getUri() {
        return uri;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getId() {
        return this.id;
    }

    public String getBodyInfo(String key) {
        return this.bodyInfo.get(key);
    }
}
