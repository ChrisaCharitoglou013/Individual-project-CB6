/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.mymessenger.datautilities.database.DAO;

import com.cc.mymessenger.Status;
import com.cc.mymessenger.User;
import com.cc.mymessenger.datautilities.database.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xrysa
 */
public class UserDAO {

    private String table = "users";
    private Database db = new Database();
    private LinkedHashMap<String, Object> userMap = new LinkedHashMap<>();

    public LinkedHashMap mapFiller(User user) {
        userMap.put("id", user.getId());
        userMap.put("username", user.getUsername());
        userMap.put("password", user.getPassword());
        userMap.put("lname", user.getLname());
        userMap.put("fname", user.getFname());
        userMap.put("failed_tries", user.getFailedTries());
        userMap.put("status", user.getStatus());
        return userMap;
    }

    public int insertUser(User user) {
        int rowsInserted;
        userMap = mapFiller(user);
        rowsInserted = db.insert(table, userMap);
        return rowsInserted;
    }

    public int updateUser(User user) {
        int rowsUpdated;
        userMap = mapFiller(user);
        rowsUpdated = db.update(table, userMap);
        return rowsUpdated;
    }

    public int deleteUser(User user) {
        int userId, result;
        userId = user.getId();
        result = db.deleteById(table, userId);
        return result;
    }

    public User selectUserByUsername(User user) {
        ResultSet result;
        boolean ifExists = false;
        String username = user.getUsername();
        String query = "SELECT * FROM `messenger`.`users` WHERE `username` ='" + username + "';";
        result = db.Select(query);
        try {
            ifExists = result.first();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ifExists) {
            user = resultSetRowToUser(result);
        }
        return user;
    }

    public User selectUserById(User user) {
        int id = user.getId();
        ResultSet result = db.SelectAllById(table, id);
        try {
            result.first();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        user = resultSetRowToUser(result);
        return user;
    }

    public ArrayList<User> selectAllUsers() {
        User user;
        ArrayList<User> allUsers = new ArrayList<>();
        String query = "SELECT * FROM `messenger`.`users` ORDER BY `id`;";
        ResultSet result = db.Select(query);
        try {
            result.beforeFirst();
        } catch (SQLException ex) {
            Logger.getLogger(RoleActionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            while (result.next()) {
                user = resultSetRowToUser(result);
                allUsers.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleActionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allUsers;
    }

    public int selectUserLogin(User user) {
        ResultSet result;
        String username = user.getUsername();
        String password = user.getPassword();
        String query = "SELECT * FROM `messenger`.`users` WHERE `username` ='" + username + "' AND `password` = '" + password + "';";
        result = db.Select(query);

        try {
            result.first();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        try {
            if (result.isFirst()) {
                int id = 0;
                try {
                    id = result.getInt("id");
                } catch (SQLException ex) {
                    Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                }
                return id;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return 0;
    }

    public void changeStatus(User user) {
        userMap.put("id", user.getId());
        userMap.put("failed_tries", user.getFailedTries());
        userMap.put("status", user.getStatus());
        db.update(table, userMap);
    }

    private User resultSetRowToUser(ResultSet result) {
        User user = new User();
        int id = 0, failedTries = 0;
        String username = null, password = null, lname = null, fname = null, checkStatus = null;

        id = DAOutilities.resultGetInt(result, "id");
        user.setId(id);

        username = DAOutilities.resultGetString(result, "username");
        user.setUsername(username);

        password = DAOutilities.resultGetString(result, "password");
        user.setPassword(password);

        lname = DAOutilities.resultGetString(result, "lname");
        user.setLname(lname);

        fname = DAOutilities.resultGetString(result, "fname");
        user.setFname(fname);

        failedTries = DAOutilities.resultGetInt(result, "failed_tries");
        user.setFailedTries(failedTries);

        checkStatus = DAOutilities.resultGetString(result, "status");

        switch (checkStatus) {
            case "OK":
                user.setStatus(Status.OK);
                break;
            case "LOCKED":
                user.setStatus(Status.LOCKED);
                break;
        }
        return user;
    }

    public boolean ifExists(User user) {
        ResultSet result;
        boolean ifExists = false;
        String username = user.getUsername();
        String query = "SELECT * FROM `messenger`.`users` WHERE `username` ='" + username + "';";
        result = db.Select(query);
        try {
            ifExists = result.first();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ifExists;
    }

}
