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
 * Class that will handle the response from the HTTP server
 *
 */
public class HTTPResponse {

    private final Map<Integer, String> statusResponses = new HashMap<Integer, String>();
    private final String SERVER_DETAILS = "Server: Java HTTPServer\r\n";;
    private String contentLengthLine;
    private final String CONTENT_TYPE_LINE = "Content-Type: text/html\r\n";

    public HTTPResponse() {
        statusResponses.put(200, "HTTP/1.1 200 OK\r\n");
        statusResponses.put(201, "HTTP/1.1 201 CREATED\r\n");
        statusResponses.put(302, "HTTP/1.1 302 Found\r\n");
        statusResponses.put(301, "HTTP/1.1 301 Moved Permanently\r\n");
        statusResponses.put(400, "HTTP/1.1 400 Bad Request\r\n");
        statusResponses.put(404, "HTTP/1.1 404 Not Found\r\n");
        statusResponses.put(500, "HTTP/1.1 500 Internal Error\r\n");

    }

    /**
     *
     * @param status
     *            represents the status code in http response
     * @param response
     * @returns the full response with the header
     */

    public String buildResponse(Integer statusCode, String response, String... info) {
        String status = statusResponses.get(statusCode);
        StringBuilder buildResponse = new StringBuilder();
        contentLengthLine = "Content-Length: " + response.length() + "\r\n";
        buildResponse.append(status);
        buildResponse.append(SERVER_DETAILS);
        if (info.length == 1) {
            buildResponse.append("Location: " + info[0] + "\r\n");
        } else if (info.length == 2) {
            buildResponse.append("Location: " + info[1] + "\r\n");
            buildResponse.append("Set-Cookie: " + info[0] + "\r\n");
        }
        buildResponse.append("Link: /styles.css ;rel=stylesheet");
        buildResponse.append(CONTENT_TYPE_LINE);

        buildResponse.append(contentLengthLine);
        buildResponse.append("Connection: close\r\n");
        buildResponse.append("\r\n");
        buildResponse.append(response);
        System.out.println(buildResponse.toString());
        return buildResponse.toString();
    }

}
