/*
 * $Id$
 *
 * Copyright 2013 Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */

package com.skrill.ServerApplication;

/**
 *
 *
 * class User designed to
 * keep client's data:
 *
 * @param username
 *            keeps the name of the client
 * @param password
 *            keep the password of the client
 *
 */
public class User {
    private String username;
    private String password;
    private int lastReadMsg;

    public User(String username, String password, int lastReadMsg) {
        super();
        this.username = username;
        this.password = password;
        this.lastReadMsg = lastReadMsg;
    }

    /**
     *
     * @param username
     * @param password
     */
    public User(String username, String password) {
        super();
        this.username = username;
        this.password = password;
        this.lastReadMsg = 0;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLastReadMsg() {
        return lastReadMsg;
    }

    public void setLastReadMsg(int lastReadMsg) {
        this.lastReadMsg = lastReadMsg;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (o instanceof User) {
            if (this.username.equals(((User) o).getUsername())) {
                return true;
            }
        }
        return false;
    }
}
