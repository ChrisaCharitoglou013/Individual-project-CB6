/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.mymessenger.datautilities.database.DAO;

import com.cc.mymessenger.Message;
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
public class MessageDAO {

    String table = "messages";
    Database db = new Database();
    LinkedHashMap<String, Object> messageMap = new LinkedHashMap<>();

    public LinkedHashMap mapFiller(Message message) {
        messageMap.put("id", message.getId());
        messageMap.put("data", message.getData());
        messageMap.put("sender", message.getSender().getId());
        messageMap.put("receiver", message.getReceiver().getId());
        return messageMap;
    }

    public int insertMessage(Message message) {
        int rowsInserted;
        messageMap = mapFiller(message);
        rowsInserted = db.insert(table, messageMap);
        return rowsInserted;
    }

    public int updateMessage(Message message) {
        int rowsUpdated;
        messageMap.put("id", message.getId());
        messageMap.put("data", message.getData());
        rowsUpdated = db.update(table, messageMap);
        return rowsUpdated;
    }

    public int deleteMessage(Message message) {
        int messageId, result;
        messageId = message.getId();
        result = db.deleteById(table, messageId);
        return result;
    }

    public Message selectMessage(Message message) {
        boolean ifExists = false;
        String senderID, receiverID, data, date;
        ArrayList<String> parameters = new ArrayList<>();
        String query = "SELECT * FROM `messenger`.`messages` "
                + "WHERE `sender` =? AND `receiver` = ? AND `data` = ?;";
        senderID = String.valueOf(message.getSender().getId());
        receiverID = String.valueOf(message.getReceiver().getId());
        data = message.getData();
        parameters.add(senderID);
        parameters.add(receiverID);
        parameters.add(data);
        ResultSet result = db.preparedSelect(query, parameters);

        try {
            ifExists = result.first();
        } catch (SQLException ex) {
            Logger.getLogger(MessageDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ifExists) {
            date = DAOutilities.resultGetString(result, "date");
            message.setDate(date);
        }
        return message;
    }

    public ArrayList<Message> selectMessagesByReceiverId(int receiverId) {
        Message message = new Message();
        ArrayList<Message> receivedMessages = new ArrayList<>();
        String query = "SELECT `messages`.`id` AS `id`,\n"
                + "`data`, `date`,\n"
                + "`senderusername`.`username` AS `sender_username`,`sender`,\n"
                + "`receiverusername`.`username` AS `receiver_username`,`receiver`\n"
                + "FROM `messenger`.`messages` \n"
                + "INNER JOIN `messenger`.`users` `senderusername`\n"
                + "ON `messages`.`sender`=`senderusername`.`id`\n"
                + "INNER JOIN `messenger`.`users` `receiverusername`\n"
                + "ON `messages`.`receiver`=`receiverusername`.`id`"
                + "WHERE `receiver` ='" + receiverId + "' ORDER BY `id`;";
        ResultSet result = db.Select(query);
        try {
            result.beforeFirst();
        } catch (SQLException ex) {
            Logger.getLogger(RoleActionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            while (result.next()) {
                message = resultSetRowToMessage(result);
                receivedMessages.add(message);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleActionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return receivedMessages;
    }

    private Message resultSetRowToMessage(ResultSet result) {
        Message message = new Message();
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

        receiverID = DAOutilities.resultGetInt(result, "receiver");
        receiver.setId(receiverID);
        receiverUsername = DAOutilities.resultGetString(result, "receiver_username");
        receiver.setUsername(receiverUsername);
        message.setReceiver(receiver);

        data = DAOutilities.resultGetString(result, "data");
        message.setData(data);

        date = DAOutilities.resultGetString(result, "date");
        message.setDate(date);

        return message;
    }

}
