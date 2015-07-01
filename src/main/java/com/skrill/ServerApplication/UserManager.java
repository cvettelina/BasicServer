/*
 * $Id$
 *
 * Copyright 2013 Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */

package com.skrill.ServerApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * class UserManager for managing user's data
 * keeps information for registered and logged users
 * keeps messages
 *
 */
public class UserManager {
    private static List<User> registeredUsers = new ArrayList<User>();
    private static Map<String, User> users = new HashMap<String, User>();
    private static List<String> messages = new ArrayList<String>();

    /**
     * Checking if the user is logged
     *
     * @param user
     * @return
     */
    public static boolean checkUserExists(User user) {
        if (users.containsValue(user)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Chenking if user's password is correct
     *
     * @param user
     * @param pass
     * @return
     */
    public static boolean checkUserData(User user, String pass) {
        int index = registeredUsers.indexOf(user);
        if (registeredUsers.get(index).getPassword().equals(pass) == false) {
            return false;
        }
        return true;
    }

    /**
     * getting all unread messages for the user with the given session id
     *
     * @param sessionId
     * @return
     */
    public static String getUnreadMessages(String sessionId) {
        if ((users.get(sessionId).getLastReadMsg() == messages.size()) || messages.size() == 0) {
            return "You don't have unread messages";
        } else {
            StringBuilder unreadMessages = new StringBuilder();
            for (int i = users.get(sessionId).getLastReadMsg(); i < messages.size(); i++) {
                unreadMessages.append("<p>" + messages.get(i) + "</p>");
            }
            users.get(sessionId).setLastReadMsg(messages.size());
            return unreadMessages.toString();
        }

    }

    public static void addMessage(String message) {
        messages.add(message);
    }

    public static void addUser(User user, String sessionId) {
        users.put(sessionId, getUser(user));

    }

    public static User getUserByID(String UID) {
        return users.get(UID);
    }

    public static void logOut(String id) {
        users.remove(id);
    }

    /**
     * checking if user is registered
     *
     * @param user
     * @return
     */
    public static boolean isUserRegistered(User user) {
        if (registeredUsers.contains(user)) {
            return true;
        } else {
            return false;
        }
    }

    private static User getUser(User user) {
        int index = registeredUsers.indexOf(user);
        return registeredUsers.get(index);
    }

    /**
     * registering user
     *
     * @param user
     */
    public static void registerUser(User user) {
        registeredUsers.add(user);
    }
}
