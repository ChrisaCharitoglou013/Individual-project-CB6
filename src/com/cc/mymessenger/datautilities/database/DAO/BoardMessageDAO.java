/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.mymessenger.datautilities.database.DAO;

import com.cc.mymessenger.BoardMessage;
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
public class BoardMessageDAO {

    String table = "board_messages";
    Database db = new Database();
    LinkedHashMap<String, Object> messageMap = new LinkedHashMap<>();

    public LinkedHashMap mapFiller(BoardMessage message) {
        messageMap.put("id", message.getId());
        messageMap.put("data", message.getData());
        messageMap.put("sender", message.getSender().getId());
        return messageMap;
    }

    public int insertMessage(BoardMessage message) {
        int rowsInserted;
        messageMap = mapFiller(message);
        rowsInserted = db.insert(table, messageMap);
        return rowsInserted;
    }

    public int updateMessage(BoardMessage message) {
        int rowsUpdated;
        messageMap.put("id", message.getId());
        messageMap.put("data", message.getData());
        rowsUpdated = db.update(table, messageMap);
        return rowsUpdated;
    }

    public int deleteMessage(BoardMessage message) {
        int messageId, result;
        messageId = message.getId();
        result = db.deleteById(table, messageId);
        return result;
    }

    public BoardMessage selectMessage(BoardMessage message) {
        boolean ifExists = false;
        String senderID, data, date;
        ArrayList<String> parameters = new ArrayList<>();
        String query = "SELECT * FROM `messenger`.`board_messages` "
                + "WHERE `sender` =? AND `data` = ?;";
        senderID = String.valueOf(message.getSender().getId());
        data = message.getData();
        parameters.add(senderID);
        parameters.add(data);
        ResultSet result = db.preparedSelect(query, parameters);

        try {
            ifExists = result.first();
        } catch (SQLException ex) {
            Logger.getLogger(BoardMessageDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ifExists) {
            date = DAOutilities.resultGetString(result, "date");
            message.setDate(date);
        }
        return message;
    }
    // View own board messages
    public ArrayList<BoardMessage> selectOwnBoardMessages(int senderId) {
        BoardMessage message = new BoardMessage();
        ArrayList<BoardMessage> ownBoardMessages = new ArrayList<>();
        String query = "SELECT `board_messages`.`id` AS `id`,\n"
                + "`data`, `date`,\n"
                + "`username` AS `sender_username`,`sender`\n"
                + "FROM `messenger`.`board_messages` \n"
                + "INNER JOIN `messenger`.`users` \n"
                + "ON `board_messages`.`sender`=`users`.`id`\n"
                +  "WHERE `sender` ='" + senderId + "' ORDER BY `date`;";
        ResultSet result = db.Select(query);
        try {
            result.beforeFirst();
        } catch (SQLException ex) {
            Logger.getLogger(RoleActionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            while (result.next()) {
                message = resultSetRowToMessage(result);
                ownBoardMessages.add(message);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleActionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ownBoardMessages;
    }
    
    // View all board messages
    public ArrayList<BoardMessage> selectAllBoardMessages() {
        BoardMessage boardMessage = new BoardMessage();
        ArrayList<BoardMessage> allBoardMessages = new ArrayList<>();
        String query = "SELECT `board_messages`.`id` AS `id`,\n"
                + "`data`, `date`,\n"
                + "`username` AS `sender_username`,`sender`\n"
                + "FROM `messenger`.`board_messages` \n"
                + "INNER JOIN `messenger`.`users` \n"
                + "ON `board_messages`.`sender`=`users`.`id`\n"
                + "ORDER BY `date`;";
        ResultSet result = db.Select(query);
        try {
            result.beforeFirst();
        } catch (SQLException ex) {
            Logger.getLogger(RoleActionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            while (result.next()) {
                boardMessage = resultSetRowToMessage(result);
                allBoardMessages.add(boardMessage);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleActionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allBoardMessages;
    }

    private BoardMessage resultSetRowToMessage(ResultSet result) {
        BoardMessage message = new BoardMessage();
        User sender = new User();
        User receiver = new User();
        int id = 0, senderID = 0, receiverID = 0;
        String data = null, date = null, senderUsername, receiverUsername;

        id = DAOutilities.resultGetInt(result, "id");
        message.setId(id);

        senderID = DAOutilities.resultGetInt(result, "sender");
        sender.setId(senderID);
        senderUsername = DAOutilities.resultGetString(result, "sender_username");
        sender.setUsername(senderUsername);
        message.setSender(sender);

        data = DAOutilities.resultGetString(result, "data");
        message.setData(data);

        date = DAOutilities.resultGetString(result, "date");
        message.setDate(date);

        return message;
    }

}
