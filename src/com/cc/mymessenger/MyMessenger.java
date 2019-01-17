/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.mymessenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xrysa
 */
public class MyMessenger {

    public static BufferedReader in;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        MyMessenger.in = new BufferedReader(new InputStreamReader(System.in));
        String exitCommand = "Start";

        Login.connectAndTest();
        while (!exitCommand.toLowerCase().equals("q")) {
            Login newSession = new Login();
            newSession.login();
            if(Login.getLoggedInUserId() > 0){
                Menu menu = new Menu(Login.getLoggedInUserId());
                menu.chooseMenu();
            }            
            newSession.logout();
            System.out.println("\nPress q to Exit the program or Enter to login.");
            try {
                exitCommand = MyMessenger.in.readLine();
            } catch (IOException ex) {
                Logger.getLogger(MyMessenger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
