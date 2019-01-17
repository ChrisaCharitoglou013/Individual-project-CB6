/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.mymessenger;

import com.cc.mymessenger.datautilities.database.DAO.UserDAO;
import com.cc.mymessenger.datautilities.database.Database;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xrysa
 */
public class Login {

    private static BufferedReader in;
    private static User loggedInUser;
    private static int loggedInUserId;

    public static int getLoggedInUserId() {
        return loggedInUserId;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    private User enterUsername(User user) {
        Login.in = new BufferedReader(new InputStreamReader(System.in));
        UserDAO userDAO = new UserDAO();
        boolean checkIfExists = false;
        String username = null;

        do {
            System.out.println("\nUsername: ");
            try {
                username = Login.in.readLine();
            } catch (IOException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            user.setUsername(username);
            checkIfExists = userDAO.ifExists(user);

            if (checkIfExists) {
                user = userDAO.selectUserByUsername(user);
            } else {
                System.out.println("\nInvalid username!\nPlease enter a valid username.");
            }

        } while (!checkIfExists);

        return user;
    }

    /**
     *
     */
    public void login() {
        Login.in = MyMessenger.in;
        User user = new User();
        UserDAO userDAO = new UserDAO();
        boolean checkPassword;
        int failedTries = 0;
        String password = null;

        user = enterUsername(user);

        failedTries = user.getFailedTries();

        while ((loggedInUserId == 0) && (user.getStatus() == Status.OK) && (failedTries < 3)) {
            System.out.println("\nPassword: ");
            try {
                password = Login.in.readLine();
            } catch (IOException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            checkPassword = user.getPassword().equals(password);

            if (checkPassword) {
                loggedInUserId = user.getId();
                loggedInUser = user;
                user.setFailedTries(0);
                user.setStatus(Status.OK);
                userDAO.changeStatus(user);
            } else {
                System.out.println("\nInvalid password!");
                failedTries++;
                user.setFailedTries(failedTries);
            }
        }
        if ((failedTries == 3 || (user.getStatus() == Status.LOCKED)) && (user.getId() != 1)) {
            System.out.println("\nAccount locked!");
            user.setStatus(Status.LOCKED);
            userDAO.changeStatus(user);
        }
    }

    public void logout() {
        if (loggedInUserId != 0) {
            loggedInUserId = 0;
            System.out.println("\nYou have been logged out successfully!");
        }
    }

    public static void connectAndTest() {
        boolean isConnectionOK;
        Database db = new Database();
        isConnectionOK = db.connectAndTest();
        if (isConnectionOK) {
            System.out.println("\nWelcome!\n");
        } else {
            System.out.println("\nUnable to connect to database."
                    + "\nPlease contact system administrator.");
        }
    }

}
