/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.mymessenger.datautilities.database.DAO;

import com.cc.mymessenger.datautilities.database.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xrysa
 */
public class UserRoleDAO {

    private Database db = new Database();

    public void setUserRole(int userID, int roleID) {
        String query;
        query = "INSERT INTO `messenger`.`user_roles` (`user_id`, `role_id`) VALUES ('"
                + userID + "', '" + roleID + "');";
        db.insertOrUpdate(query);
    }

    public void changeUserRole(int userID, int roleID) {
        String query;
        query = "UPDATE `messenger`.`user_roles` SET `role_id`='" + roleID + "' WHERE `user_id`='"
                + userID + "';";
        db.insertOrUpdate(query);
    }

    // to fix: this should return a ArrayList<UserRole> 
    public ArrayList<String> findUserRole(int userID) {
        ResultSet result;
        int roleID = 0;
        String query, userRole;
        ArrayList<String> userRoles = new ArrayList<>();
        query = "SELECT `roles`.`id` AS `role_id`,`role`\n"
                + "FROM `messenger`.`user_roles` \n"
                + "INNER JOIN `messenger`.`roles`\n"
                + "ON `user_roles`.`role_id`=`roles`.`id`\n"
                + "WHERE `user_roles`.`user_id` ='" + userID + "' ORDER BY `role_id`;";
        result = db.Select(query);
        try {
            result.beforeFirst();
        } catch (SQLException ex) {
            Logger.getLogger(UserRoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            while (result.next()) {
                userRole = result.getString("role");
                userRoles.add(userRole);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserRoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userRoles;
    }
    
}
