/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.mymessenger.objectvalidators;

import com.cc.mymessenger.User;
import com.cc.mymessenger.datautilities.database.DAO.UserDAO;

/**
 *
 * @author Xrysa
 */
public class UserValidator {

    public boolean validateUsername(String username) {
        boolean isValid = false;
        if (username.isEmpty() || username == null) {
            System.out.println("\nUsername can't be empty!");
        } else {
            isValid = true;
        }
        return isValid;
    }

    public boolean validateNewUsername(String username) {
        boolean isValid = false, alreadyInUse = false;
        User user = new User();
        UserDAO userDAO = new UserDAO();
        user.setUsername(username);
        alreadyInUse = userDAO.ifExists(user);

        if (username.isEmpty() || username == null) {
            System.out.println("\nUsername can't be empty!");
        } else if (alreadyInUse) {
            System.out.println("\nThis username is already being used!"
                    + "\nPlease choose another username.");
        } else if (username.length() < 5 || username.length() > 15) {
            System.out.println("\nUsername must be between 5 - 15 characters."
                    + "\nPlease choose another username.");
        } else {
            isValid = true;
        }
        return isValid;
    }

    public boolean validatePassword(String password) {
        boolean isValid = false;
        if (password.isEmpty() || password == null) {
            System.out.println("\nPassword can't be empty!");
        } else if (password.length() < 5 || password.length() > 15) {
            System.out.println("\nPassword must be between 5 - 15 characters."
                    + "\nPlease choose another username.");
        } else {
            isValid = true;
        }
        return isValid;
    }

    public boolean validateLastName(String lname) {
        boolean isValid = false;
        if (lname.isEmpty() || lname == null) {
            System.out.println("\nLast name can't be empty!");
        }else if (lname.length() > 25) {
            System.out.println("\nLast name can't be larger than 25 characters.");
        } else {
            isValid = true;
        }
        return isValid;
    }

    public boolean validateFirstName(String fname) {
        boolean isValid = false;
        if (fname.isEmpty() || fname == null) {
            System.out.println("\nFirst name can't be empty!");
        }else if (fname.length() > 15) {
            System.out.println("\nFirst name can't be larger than 15 characters.");
        } else {
            isValid = true;
        }
        return isValid;
    }

}
