/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.mymessenger.datautilities.database.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xrysa
 */
public class DAOutilities {

    static int resultGetInt(ResultSet result, String columnLabel) {
        int num = 0;
        try {
            num = result.getInt(columnLabel);
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return num;
    }

    static String resultGetString(ResultSet result, String columnLabel) {
        String info = null;
        try {
            info = result.getString(columnLabel);
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return info;
    }

}
