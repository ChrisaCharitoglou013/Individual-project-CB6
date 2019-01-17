/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.mymessenger.datautilities.database.DAO;

import com.cc.mymessenger.Role;
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
public class RoleDAO {

    private Database db = new Database();

    public ArrayList<Role> selectAllRoles() {
        Role role = new Role();
        ArrayList<Role> availableRoles = new ArrayList<>();
        String query = "SELECT * FROM `messenger`.`roles` ORDER BY `id`;";
        ResultSet result = db.Select(query);
        try {
            result.beforeFirst();
        } catch (SQLException ex) {
            Logger.getLogger(RoleActionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            while (result.next()) {
                role = resultSetRowToRole(result);
                availableRoles.add(role);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleActionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return availableRoles;
    }

    private Role resultSetRowToRole(ResultSet result) {
        int id;
        String roleName, roleDescription;
        Role role = new Role();

        id = DAOutilities.resultGetInt(result, "id");
        role.setId(id);
        roleName = DAOutilities.resultGetString(result, "role");
        role.setRoleName(roleName);
        roleDescription = DAOutilities.resultGetString(result, "role_description");
        role.setRoleDescription(roleDescription);

        return role;
    }

}
