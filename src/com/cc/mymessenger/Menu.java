/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.mymessenger;

import com.cc.mymessenger.datautilities.database.DAO.BoardMessageDAO;
import com.cc.mymessenger.datautilities.database.DAO.UserDAO;
import com.cc.mymessenger.datautilities.database.DAO.RoleActionDAO;
import com.cc.mymessenger.datautilities.database.DAO.MessageDAO;
import com.cc.mymessenger.datautilities.database.DAO.UserRoleDAO;
import static com.cc.mymessenger.menuutilities.ConsoleUtils.clearConsole;
import com.cc.mymessenger.menuutilities.MenuConstruction;
import com.cc.mymessenger.menuutilities.MenuItem;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author Xrysa
 */
public class Menu {

    private static int loggedInUserId;
    private ArrayList<RoleAction> permissions;
    private LinkedHashMap<String, String> menuParameters = new LinkedHashMap<>();

    public Menu(int loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
    }

    public void createMenuParameters(ArrayList<RoleAction> permissions) {
        String actionName, type;
        for (int i = 0; i < permissions.size(); i++) {
            actionName = permissions.get(i).getAction().getActionName();
            type = permissions.get(i).getType();
            switch (type) {
                case "USERS":
                    switch (actionName) {
                        case "create":
                            menuParameters.put("createUser", "Create new user");
                            break;
                        case "read":
                            menuParameters.put("viewUsers", "View users");
                            break;
                        case "update":
                            menuParameters.put("editUser", "Edit user");
                            break;
                        case "delete":
                            menuParameters.put("deleteUser", "Delete user");
                            break;
                    }
                    break;
                case "MESSAGES":
                    switch (actionName) {
                        case "create":
                            menuParameters.put("createMessage", "Create new Personal message");
                            break;
                        case "read":
                            menuParameters.put("viewMessages", "View Personal messages");
                            break;
                        case "update":
                            menuParameters.put("editMessage", "Edit Personal message");
                            break;
                        case "delete":
                            menuParameters.put("deleteMessage", "Delete Personal message");
                            break;
                    }
                    break;
                case "BOARD":
                    switch (actionName) {
                        case "create":
                            menuParameters.put("createBoardMessage", "Create new Board message");
                            break;
                        case "read":
                            menuParameters.put("viewAllBoardMessages", "View Board messages");
                            break;
                        case "update":
                            menuParameters.put("editBoardMessage", "Edit Board message");
                            break;
                        case "delete":
                            menuParameters.put("deleteBoardMessage", "Delete Board message");
                            break;
                    }
                    break;
            }
        }
    } 

    private void findPermissions(int userID) {
        RoleActionDAO roleActionDAO = new RoleActionDAO();
        permissions = roleActionDAO.findActionsByUserID(userID);
    }

    public void chooseMenu() {
        findPermissions(loggedInUserId);
        createMenuParameters(permissions);
        mainMenu();
    }

    private void mainMenu() {
        clearConsole();
        MenuConstruction menu = new MenuConstruction();
        menu.setTitle("*** Main Menu ***");
        menu.addItem(new MenuItem("View Profile", this, "viewProfile"));
        menu.addItem(new MenuItem("Change password", this, "changeOwnPassword"));
        for (String key : menuParameters.keySet()) {
            menu.addItem(new MenuItem(menuParameters.get(key), this, key));
        }
        menu.execute();
    }

    public void viewProfile() {
        UserDAO userDAO = new UserDAO();
        
        System.out.println("--- My profile ---\n");
        User user = new User();
        user.setId(loggedInUserId);
        user = userDAO.selectUserById(user);
        System.out.println("Username: " + user.getUsername() + "\nLast name: "
                + user.getLname() + "\nFirst name: " + user.getFname());
        System.out.println("\n------------------\n");
    }

    public void changeOwnPassword() {
        UserInteraction userInteraction = new UserInteraction();
        userInteraction.changeOwnPassword();
    }

    public void createUser() {
        UserInteraction userInteraction = new UserInteraction();
        UserDAO userDAO = new UserDAO();
        int rowsInserted;
        User user = userInteraction.createUser();
        rowsInserted = userDAO.insertUser(user);
        
        if (rowsInserted == 1) {
            System.out.println("\nUser created successfully!\n");
            user = userDAO.selectUserByUsername(user);
            userInteraction.setUserRole(user.getId());
        } else {
            System.out.println("\nThe user could not be created.\n");
        }
    }

    public LinkedHashMap<Integer, User> viewUsers() {
        UserRoleDAO userRoleDAO = new UserRoleDAO();
        UserDAO userDAO = new UserDAO();
        String title;
        int num = 0;
        ArrayList<User> allUsers = new ArrayList<>();
        LinkedHashMap<Integer, User> numberedUserMap = new LinkedHashMap<>();
        ArrayList<String> userRoles;

        allUsers = userDAO.selectAllUsers();
        System.out.println("\n--- All users ---\n");
        title = String.format("   %-15s|%-25s|%-15s|%-7s|%-13s|%-10s|", "Username", "Last name", "First name", "Role", "Failed tries", "Status");
        System.out.println(title);
        for (User aUser : allUsers) {
            if (aUser == allUsers.get(0)) {
                continue;
            }
            userRoles = userRoleDAO.findUserRole(aUser.getId());
            num++;
            numberedUserMap.put(num, aUser);
            System.out.println(num + displayOneUser(aUser, userRoles));
        }
        return numberedUserMap;
    }

    public void editUser() {
        UserInteraction userInteraction = new UserInteraction();
        LinkedHashMap<Integer, User> numberedUserMap;
        numberedUserMap = viewUsers();
        userInteraction.editUser(numberedUserMap);
    }

    public void deleteUser() {
        UserInteraction userInteraction = new UserInteraction();
        LinkedHashMap<Integer, User> numberedUserMap;
        numberedUserMap = viewUsers();
        userInteraction.deleteUser(numberedUserMap);
    }

    public void createMessage() {
        MessageDAO messageDAO = new MessageDAO();
        UserInteraction userInteraction = new UserInteraction();
        FileAccess writeToFile = new FileAccess();
        int rowsInserted;
        
        Message message = userInteraction.createMessage();
        rowsInserted = messageDAO.insertMessage(message);
        if (rowsInserted == 1) {
            message.setDate(messageDAO.selectMessage(message).getDate());
            writeToFile.messageToFile(message);
            System.out.println("\nMessage sent...\n");
        } else {
            System.out.println("\nThe message could not be sent...\n");
        }
    }

    public void editMessage() {       
        UserInteraction userInteraction = new UserInteraction();
        LinkedHashMap<Integer, Message> numberedReceivedMessagesMap;
        numberedReceivedMessagesMap = viewMessages();
        userInteraction.editMessage(numberedReceivedMessagesMap);
    }

    public void deleteMessage() {
        UserInteraction userInteraction = new UserInteraction();
        LinkedHashMap<Integer, Message> numberedReceivedMessagesMap;
        numberedReceivedMessagesMap = viewMessages();
        userInteraction.deleteMessage(numberedReceivedMessagesMap);
    }

    public LinkedHashMap<Integer, Message> viewMessages() {
        MessageDAO messageDAO = new MessageDAO();
        ArrayList<Message> receivedMessages ;
        LinkedHashMap<Integer, Message> numberedreceivedMessagesMap = new LinkedHashMap<>();
        int num = 0;

        receivedMessages = messageDAO.selectMessagesByReceiverId(loggedInUserId);
        System.out.println("\n--- Received Messages ---\n");
        if (receivedMessages.isEmpty()) {
            System.out.println("\nNo messages at the moment!");
        } else {
            for (Message aMessage : receivedMessages) {
                num++;
                numberedreceivedMessagesMap.put(num, aMessage);
                System.out.println(num + displayOneMessage(aMessage));
            }
        }
        return numberedreceivedMessagesMap;
    }

    public String displayOneMessage(Message message) {
        String displayMessage, data, date, senderUsername, receiverUsername;

        senderUsername = message.getSender().getUsername();
        receiverUsername = message.getReceiver().getUsername();
        date = message.getDate();
        data = message.getData();
        displayMessage = String.format(". From: %-15s To:%-15s Date: %-25s %n%s", senderUsername, receiverUsername, date, data);

        return displayMessage;
    }

    public String displayOneUser(User user, ArrayList<String> userRole) {
        int id, failedTries;
        String username, lname, fname, status, role, displayUser;
        
        username = user.getUsername();
        lname = user.getLname();
        fname = user.getFname();
        failedTries = user.getFailedTries();
        status = user.getStatus().toString();
        if (userRole.isEmpty()) {
            displayUser = String.format(". %-15s|%-25s|%-15s|%-7s|%6s%1d%6s|%-10s|", username, lname, fname, "N/A", "", failedTries, "", status);
        } else {
            role = userRole.get(userRole.size() - 1);
            displayUser = String.format(". %-15s|%-25s|%-15s|%-7s|%6s%1d%6s|%-10s|", username, lname, fname, role, "", failedTries, "", status);
        }
        return displayUser;
    }
    
    public void createBoardMessage() {
        BoardMessageDAO boardMessageDAO = new BoardMessageDAO();
        UserInteraction userInteraction = new UserInteraction();
        FileAccess writeToFile = new FileAccess();
        int rowsInserted;
        
        BoardMessage boardMessage = userInteraction.createBoardMessage();
        rowsInserted = boardMessageDAO.insertMessage(boardMessage);
        if (rowsInserted == 1) {
            boardMessage.setDate(boardMessageDAO.selectMessage(boardMessage).getDate());
            writeToFile.BoardMessageToFile(boardMessage);
            System.out.println("\nMessage written...\n");
        } else {
            System.out.println("\nThe message could not be written...\n");
        }
    }

    public void editBoardMessage() {
        UserInteraction userInteraction = new UserInteraction();
        LinkedHashMap<Integer, BoardMessage> numberedOwnBoardMessagesMap;
        numberedOwnBoardMessagesMap = viewOwnBoardMessages();
        userInteraction.editBoardMessage(numberedOwnBoardMessagesMap);
    }

    public void deleteBoardMessage() {
        UserInteraction userInteraction = new UserInteraction();
        LinkedHashMap<Integer, BoardMessage> numberedAllBoardMessagesMap;
        numberedAllBoardMessagesMap = viewAllBoardMessages();
        userInteraction.deleteBoardMessage(numberedAllBoardMessagesMap);
    }

    public LinkedHashMap<Integer, BoardMessage> viewAllBoardMessages() {
        BoardMessageDAO boardMessageDAO = new BoardMessageDAO();
        int num = 0;
        ArrayList<BoardMessage> allBoardMessages ;
        LinkedHashMap<Integer, BoardMessage> numberedAllBoardMessagesMap = new LinkedHashMap<>();

        allBoardMessages = boardMessageDAO.selectAllBoardMessages();
        System.out.println("\n--- Board Messages ---\n");
        if (allBoardMessages.isEmpty()) {
            System.out.println("\nBoard Empty!");
        } else {
            for (BoardMessage aMessage : allBoardMessages) {
                num++;
                numberedAllBoardMessagesMap.put(num, aMessage);
                System.out.println(num + displayOneBoardMessage(aMessage));
            }
        }
        return numberedAllBoardMessagesMap;
    }
    
    public LinkedHashMap<Integer, BoardMessage> viewOwnBoardMessages() {
        BoardMessageDAO boardMessageDAO = new BoardMessageDAO();
        int num = 0;
        ArrayList<BoardMessage> ownBoardMessages ;
        LinkedHashMap<Integer, BoardMessage> numberedOwnBoardMessagesMap = new LinkedHashMap<>();

        ownBoardMessages = boardMessageDAO.selectOwnBoardMessages(loggedInUserId);
        System.out.println("\n--- Own posted messages ---\n");
        if (ownBoardMessages.isEmpty()) {
            System.out.println("\nYou have not posted anything yet!");
        } else {
            for (BoardMessage aMessage : ownBoardMessages) {
                num++;
                numberedOwnBoardMessagesMap.put(num, aMessage);
                System.out.println(num + displayOneBoardMessage(aMessage));
            }
        }
        return numberedOwnBoardMessagesMap;
    }
    
    public String displayOneBoardMessage(BoardMessage message) {
        String displayMessage, data, date, senderUsername;

        senderUsername = message.getSender().getUsername();
        date = message.getDate();
        data = message.getData();
        displayMessage = String.format(". From: %-15s Date: %-25s %n%s", senderUsername, date, data);

        return displayMessage;
    }
}
