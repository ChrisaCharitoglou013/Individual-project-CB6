/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.mymessenger.datautilities.database.DAO;

import com.cc.mymessenger.Action;
import com.cc.mymessenger.Role;
import com.cc.mymessenger.RoleAction;
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
public class RoleActionDAO {

    private Database db = new Database();

    public ArrayList<RoleAction> findActionsByUserID(int userID) {
        RoleAction roleAction;
        ArrayList<RoleAction> roleActions = new ArrayList<>();
        ResultSet result;

        String query = "SELECT `role_actions`.`id` AS `id`\n"
                + ",`role`,`action`,`type` \n"
                + "FROM `messenger`.`user_roles` \n"
                + "INNER JOIN `messenger`.`users`\n"
                + "ON `user_roles`.`user_id`=`users`.`id`\n"
                + "INNER JOIN `messenger`.`roles`\n"
                + "ON `user_roles`.`role_id`=`roles`.`id`\n"
                + "INNER JOIN `messenger`.`role_actions`\n"
                + "ON `role_actions`.`role_id`=`roles`.`id`\n"
                + "INNER JOIN `messenger`.`actions`\n"
                + "ON `role_actions`.`action_id`=`actions`.`id`\n"
                + "WHERE `user_roles`.`user_id` ='" + userID + "' "
                + "ORDER BY `role_actions`.`id`;";
        result = db.Select(query);

        try {
            result.beforeFirst();
        } catch (SQLException ex) {
            Logger.getLogger(RoleActionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            while (result.next()) {
                roleAction = resultRowToRoleActionObject(result);
                roleActions.add(roleAction);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleActionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return roleActions;
    }

    private RoleAction resultRowToRoleActionObject(ResultSet result) {
        RoleAction roleAction = new RoleAction();
        int id = 0;
        Role role = new Role();
        Action action = new Action();
        String type = null, roleName = null, actionName = null;

        id = DAOutilities.resultGetInt(result, "id");
        roleAction.setId(id);

        roleName = DAOutilities.resultGetString(result, "role");
        role.setRoleName(roleName);
        roleAction.setRole(role);

        actionName = DAOutilities.resultGetString(result, "action");
        action.setActionName(actionName);
        roleAction.setAction(action);

        type = DAOutilities.resultGetString(result, "type");
        roleAction.setType(type);

        return roleAction;
    }
}
