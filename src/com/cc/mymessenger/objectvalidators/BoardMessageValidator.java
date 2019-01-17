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
public class BoardMessageValidator {

    public boolean validateData(String text) {
        boolean isValid = false;
        if (text.isEmpty()) {
            System.out.println("\nMessage can't be empty!");
        } else if (text.length() > 250) {
            System.out.println("\nMessage can't be larger than 250 characters!");
        } else {
            isValid = true;
        }
        return isValid;
    }
    
}
