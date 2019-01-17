/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.mymessenger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xrysa
 */
public class FileAccess {

    BufferedWriter writer = null;

    public void messageToFile(Message message) {
        String sender, receiver, data, date;

        sender = message.getSender().getUsername();
        receiver = message.getReceiver().getUsername();
        date = message.getDate();
        data = message.getData();

        try {
            writer = new BufferedWriter(new FileWriter("messages.txt", true));
        } catch (IOException ex) {
            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            writer.newLine();
        } catch (IOException ex) {
            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            writer.append("Sender: " + sender + ", receiver: " + receiver + ", date: " + date);
        } catch (IOException ex) {
            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            writer.newLine();
        } catch (IOException ex) {
            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            writer.append("Data: " + data);
        } catch (IOException ex) {
            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            writer.newLine();
        } catch (IOException ex) {
            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            writer.append("---------------------------------------------------");
        } catch (IOException ex) {
            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    public void BoardMessageToFile(BoardMessage message) {
        String sender, data, date;

        sender = message.getSender().getUsername();
        date = message.getDate();
        data = message.getData();

        try {
            writer = new BufferedWriter(new FileWriter("BoardMessages.txt", true));
        } catch (IOException ex) {
            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            writer.newLine();
        } catch (IOException ex) {
            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            writer.append("Sender: " + sender + ", date: " + date);
        } catch (IOException ex) {
            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            writer.newLine();
        } catch (IOException ex) {
            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            writer.append("Data: " + data);
        } catch (IOException ex) {
            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            writer.newLine();
        } catch (IOException ex) {
            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            writer.append("---------------------------------------------------");
        } catch (IOException ex) {
            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

}
