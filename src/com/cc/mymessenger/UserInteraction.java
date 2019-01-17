/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.mymessenger;

import com.cc.mymessenger.datautilities.database.DAO.BoardMessageDAO;
import com.cc.mymessenger.objectvalidators.MessageValidator;
import com.cc.mymessenger.objectvalidators.UserValidator;
import com.cc.mymessenger.datautilities.database.DAO.UserDAO;
import com.cc.mymessenger.datautilities.database.DAO.RoleDAO;
import com.cc.mymessenger.datautilities.database.DAO.MessageDAO;
import com.cc.mymessenger.datautilities.database.DAO.UserRoleDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xrysa
 */
public class UserInteraction {

    private static BufferedReader in = MyMessenger.in;

    public String takeUserInput() {
        String input = null;
        try {
            input = UserInteraction.in.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return input;
    }

    public void changeOwnPassword() {
        User currentUser = Login.getLoggedInUser();
        UserDAO userDAO = new UserDAO();
        boolean agree, checkPassword;
        String password, verification;
        int result;

        System.out.println("\nDo you want to change your password?");
        agree = chooseYesOrNo();
        if (agree) {
            System.out.println("\nEnter your current password: ");
            password = takeUserInput();
            checkPassword = currentUser.getPassword().equals(password);
            if (checkPassword) {
                password = acquirePassword();
                System.out.println("Verify new password...");
                verification = acquirePassword();
                if (password.equals(verification)) {
                    currentUser.setPassword(password);
                    result = userDAO.updateUser(currentUser);
                    if (result == 1) {
                        System.out.println("\nPassword changed!\n");
                    } else {
                        System.out.println("\nPassword coul not be changed.\n");
                    }
                }
            } else {
                System.out.println("\nThe password you entered is not correct."
                        + "\nUnable to proceed...");
            }
            //currentUser.setPassword(password);
        }
    }

    public String acquireUsername() {
        UserValidator userValidator = new UserValidator();
        boolean isValid;
        String username;

        do {
            System.out.println("\nEnter a new username:");
            username = takeUserInput();
            isValid = userValidator.validateNewUsername(username);
        } while (!isValid);
        return username;
    }

    public String acquirePassword() {
        UserValidator userValidator = new UserValidator();
        boolean isValid;
        String password;

        do {
            System.out.println("\nEnter the new password:");
            password = takeUserInput();
            isValid = userValidator.validatePassword(password);
        } while (!isValid);
        return password;
    }

    public String acquireLastName() {
        UserValidator userValidator = new UserValidator();
        boolean isValid;
        String lname;

        do {
            System.out.println("\nEnter last name:");
            lname = takeUserInput();
            isValid = userValidator.validateLastName(lname);
        } while (!isValid);
        return lname;
    }

    public String acquireFirstName() {
        UserValidator userValidator = new UserValidator();
        boolean isValid;
        String fname;
        do {
            System.out.println("\nEnter first name:");
            fname = takeUserInput();
            isValid = userValidator.validateFirstName(fname);
        } while (!isValid);
        return fname;
    }

    public User createUser() {
        User newUser = new User();
        String username = null, password = null, fname = null, lname = null;

        System.out.println("\nCreating new user:");

        username = acquireUsername();
        newUser.setUsername(username);

        password = acquirePassword();
        newUser.setPassword(password);

        lname = acquireLastName();
        newUser.setLname(lname);

        fname = acquireFirstName();
        newUser.setFname(fname);

        newUser.setFailedTries(0);
        newUser.setStatus(Status.OK);

        return newUser;
    }

    public boolean isRoleAssigned(int userID) {
        boolean isRoleAssigned;
        UserRoleDAO userRoleDAO = new UserRoleDAO();
        //RoleDAO roleDAO = new RoleDAO();
        ArrayList<String> userRoles = new ArrayList<>();
        //ArrayList<Role> allAvailableRoles = new ArrayList<>();

        // Check if the selected user already has a role
        userRoles = userRoleDAO.findUserRole(userID);
        if (userRoles.isEmpty()) {
            isRoleAssigned = false;
        } else {
            isRoleAssigned = true;
        }
        return isRoleAssigned;
    }

    public void setUserRole(int userID) {
        boolean agree;
        int roleID;
        UserRoleDAO userRoleDAO = new UserRoleDAO();
        System.out.println("Assign a role to the user?");
        agree = chooseYesOrNo();
        if (agree) {
            roleID = chooseRoleId();
            userRoleDAO.setUserRole(userID, roleID);
        }
    }

    private boolean isTheSameRole(String currentRole, int chosenRoleID) {
        String chosenRole;
        boolean isTheSameRole;
        RoleDAO roleDAO = new RoleDAO();
        ArrayList<Role> availableRoles = roleDAO.selectAllRoles();
        chosenRole = availableRoles.get(chosenRoleID - 1).getRoleName();
        isTheSameRole = chosenRole.equals(currentRole);
        return isTheSameRole;
    }

    public void changeUserRole(int userID) {
        boolean isRoleAssigned, agree, isTheSame;
        int roleID;
        String currentRole;
        UserRoleDAO userRoleDAO = new UserRoleDAO();
        ArrayList<String> userRoles;
        isRoleAssigned = isRoleAssigned(userID);
        if (isRoleAssigned) {
            userRoles = userRoleDAO.findUserRole(userID);
            currentRole = userRoles.get(userRoles.size() - 1);
            System.out.println("\nCurrent role is: "
                    + currentRole
                    + "\nChange User's role?");
            agree = chooseYesOrNo();
            if (agree) {
                roleID = chooseRoleId();
                isTheSame = isTheSameRole(currentRole, roleID);
                if (isTheSame) {
                    System.out.println("\nChosen role is the same as the current one."
                            + "\nProceeding without change...");
                } else {
                    userRoleDAO.changeUserRole(userID, roleID);
                    System.out.println("\nUser edited successfully!\n");
                }
            }
        } else {
            System.out.println("\nNo role assigned for this user.");
            setUserRole(userID);
        }
    }

    private int chooseRoleId() {
        int numOfChoices, choice, roleID;
        RoleDAO roleDAO = new RoleDAO();
        ArrayList<Role> availableRoles = roleDAO.selectAllRoles();
        System.out.println("\nAvailable roles are:");
        numOfChoices = (availableRoles.size() - 1);
        for (int i = 1; i <= numOfChoices; i++) {
            System.out.println(i + ". " + availableRoles.get(i).getRoleName());
        }
        do {
            choice = takeNumberInput();
        } while (choice < 1 && choice > numOfChoices);
        roleID = availableRoles.get(choice).getId();
        return roleID;
    }

    public void editUser(LinkedHashMap<Integer, User> numberedUserMap) {
        UserDAO userDAO = new UserDAO();
        String username, password, fname, lname, status;
        int choice = 0, result;
        boolean isValid, agree;
        User chosenUser;

        if (!numberedUserMap.isEmpty()) {
            System.out.println("\nChoose the user you want to edit:");
            do {
                choice = takeNumberInput();
                isValid = numberedUserMap.containsKey(choice);
                if (!isValid) {
                    System.out.println("\n" + choice + " is not a valid choice.");
                }
            } while (!isValid);

            chosenUser = numberedUserMap.get(choice);

            // ask for new values
            System.out.println("\nCurrent username is: " + chosenUser.getUsername());
            System.out.println("Do you want to change username?");
            agree = chooseYesOrNo();
            if (agree) {
                username = acquireUsername();
                chosenUser.setUsername(username);
            }

            System.out.println("\nDo you want to change user's password?");
            agree = chooseYesOrNo();
            if (agree) {
                password = acquirePassword();
                chosenUser.setPassword(password);
            }

            System.out.println("\nUser's current last name is: " + chosenUser.getLname());
            System.out.println("Do you want to change last name?");
            agree = chooseYesOrNo();
            if (agree) {
                lname = acquireLastName();
                chosenUser.setLname(lname);
            }

            System.out.println("\nUser's current first name is: " + chosenUser.getFname());
            System.out.println("Do you want to change first name?");
            agree = chooseYesOrNo();
            if (agree) {
                fname = acquireFirstName();
                chosenUser.setFname(fname);
            }

            status = chosenUser.getStatus().toString();
            System.out.println("\nUser's current status is: " + status);
            System.out.println("Do you want to change user's status?");
            agree = chooseYesOrNo();
            if (agree) {
                switch (status) {
                    case ("OK"):
                        chosenUser.setStatus(Status.LOCKED);
                        break;
                    case ("LOCKED"):
                        chosenUser.setFailedTries(0);
                        chosenUser.setStatus(Status.OK);
                        break;
                }
            }

            changeUserRole(chosenUser.getId());
            //-----------------------
            result = userDAO.updateUser(chosenUser);
            if (result == 1) {
                System.out.println("\nUser edited successfully!\n");
            } else {
                System.out.println("\nThe user could not be edited.\n");
            }
        } else {
            System.out.println("User list is empty!");
        }

    }

    public void deleteUser(LinkedHashMap<Integer, User> numberedUserMap) {
        UserDAO userDAO = new UserDAO();
        int choice = 0, result;
        boolean isValid;
        User chosenUser;

        if (!numberedUserMap.isEmpty()) {
            System.out.println("\nChoose the user you want to delete:");
            do {
                choice = takeNumberInput();
                isValid = numberedUserMap.containsKey(choice);
                if (!isValid) {
                    System.out.println("\n" + choice + " is not a valid choice.");
                }
            } while (!isValid);

            chosenUser = numberedUserMap.get(choice);
            result = userDAO.deleteUser(chosenUser);
            if (result == 1) {
                System.out.println("\nUser deleted successfully!\n");
            } else {
                System.out.println("\nThe user could not be deleted.\n");
            }
        }
    }

    public String acquireMessageData() {
        MessageValidator messageValidator = new MessageValidator();
        boolean isValid;
        String text;

        do {
            System.out.println("\nType the message:");
            text = takeUserInput();
            isValid = messageValidator.validateData(text);
        } while (!isValid);
        return text;
    }

    public String acquireReceiver() {
        MessageValidator messageValidator = new MessageValidator();
        boolean isValid;
        String receiverUsername;
        do {
            System.out.println("\nType the recipient username:");
            receiverUsername = takeUserInput();
            isValid = messageValidator.validateReceiver(receiverUsername);
        } while (!isValid);
        return receiverUsername;
    }

    public Message createMessage() {
        UserDAO userDAO = new UserDAO();
        Message message = new Message();
        User receiver = new User();
        User sender;
        String text, receiverUsername;
        int receiverID;

        System.out.println("\nCreating new message...");

        text = acquireMessageData();

        receiverUsername = acquireReceiver();
        receiver.setUsername(receiverUsername);

        receiverID = userDAO.selectUserByUsername(receiver).getId();
        receiver.setId(receiverID);

        sender = Login.getLoggedInUser();

        message.setData(text);
        message.setReceiver(receiver);
        message.setSender(sender);

        return message;
    }

    public void editMessage(LinkedHashMap<Integer, Message> numberedReceivedMessagesMap) {
        MessageDAO messageDAO = new MessageDAO();
        String data;
        int choice = 0, result;
        boolean isValid;
        Message chosenMessage;

        if (!numberedReceivedMessagesMap.isEmpty()) {
            System.out.println("\nChoose the message you want to edit:");
            do {
                choice = takeNumberInput();
                isValid = numberedReceivedMessagesMap.containsKey(choice);
                if (!isValid) {
                    System.out.println("\n" + choice + " is not a valid choice.");
                }
            } while (!isValid);

            chosenMessage = numberedReceivedMessagesMap.get(choice);

            // ask for new values
            data = acquireMessageData();
            chosenMessage.setData(data);
            //-----------------------
            result = messageDAO.updateMessage(chosenMessage);
            if (result == 1) {
                System.out.println("\nMessage edited successfully!\n");
            } else {
                System.out.println("\nThe message could not be edited.\n");
            }
        } else {
            System.out.println("You don't have any messages to update");
        }
    }

    public void deleteMessage(LinkedHashMap<Integer, Message> numberedReceivedMessagesMap) {
        MessageDAO messageDAO = new MessageDAO();
        int choice = 0, result;
        boolean isValid;
        Message chosenMessage;

        if (!numberedReceivedMessagesMap.isEmpty()) {
            System.out.println("\nChoose the message you want to delete:");
            do {
                choice = takeNumberInput();
                isValid = numberedReceivedMessagesMap.containsKey(choice);
                if (!isValid) {
                    System.out.println("\n" + choice + " is not a valid choice.");
                }
            } while (!isValid);

            chosenMessage = numberedReceivedMessagesMap.get(choice);
            result = messageDAO.deleteMessage(chosenMessage);
            if (result == 1) {
                System.out.println("\nMessage deleted successfully!\n");
            } else {
                System.out.println("\nThe message could not be deleted.\n");
            }
        } else {
            System.out.println("You don't have any messages to delete.");
        }
    }

    public int takeNumberInput() {
        String userInput;
        boolean isValid;
        int number;
        do {
            userInput = takeUserInput();
            isValid = isNumber(userInput);
            if (!isValid) {
                System.out.println("\n" + userInput + " is not a valid choice."
                        + "Please make a valid choice");
            }
        } while (!isValid);
        number = Integer.parseInt(userInput);
        return number;
    }

    public boolean isNumber(String userInput) {
        boolean isNumber = false;
        if (userInput != null && !userInput.isEmpty()) {
            for (char c : userInput.toCharArray()) {
                isNumber = Character.isDigit(c);
                if (!isNumber) {
                    break;
                }
            }
        }
        return isNumber;
    }

    private boolean chooseYesOrNo() {
        boolean agree = false;
        System.out.print("Confirm Operation (y/n)... ");
        String choice = takeUserInput().toLowerCase();
        while (!(choice.equals("yes") || choice.equals("y")
                || choice.equals("no") || choice.equals("n"))) {

            System.out.println("That's not a valid choice!\n"
                    + "Please enter 'yes' or 'no' : ");
            choice = takeUserInput().toLowerCase();
        }
        if (choice.equals("y") || choice.equals("yes")) {
            agree = true;
        } else if (choice.equals("n") || choice.equals("no")) {
            agree = false;
        }
        return agree;
    }

    public BoardMessage createBoardMessage() {
        User sender;
        BoardMessage boardMessage = new BoardMessage();
        String text;

        System.out.println("\nCreating new message...");

        text = acquireMessageData();

        sender = Login.getLoggedInUser();

        boardMessage.setData(text);
        boardMessage.setSender(sender);

        return boardMessage;
    }

    public void editBoardMessage(LinkedHashMap<Integer, BoardMessage> numberedOwnBoardMessagesMap) {
        BoardMessageDAO boardMessageDAO = new BoardMessageDAO();
        String data;
        int choice = 0, result;
        boolean isValid;
        BoardMessage chosenMessage;
        FileAccess writeToFile = new FileAccess();

        if (!numberedOwnBoardMessagesMap.isEmpty()) {
            System.out.println("\nChoose the message you want to edit:");
            do {
                choice = takeNumberInput();
                isValid = numberedOwnBoardMessagesMap.containsKey(choice);
                if (!isValid) {
                    System.out.println("\n" + choice + " is not a valid choice.");
                }
            } while (!isValid);

            chosenMessage = numberedOwnBoardMessagesMap.get(choice);

            // ask for new values
            data = acquireMessageData();
            chosenMessage.setData(data);
            //-----------------------
            result = boardMessageDAO.updateMessage(chosenMessage);
            if (result == 1) {
                writeToFile.BoardMessageToFile(chosenMessage);
                System.out.println("\nMessage edited successfully!\n");
            } else {
                System.out.println("\nThe message could not be edited.\n");
            }
        } else {
            System.out.println("You don't have any messages to edit.");
        }
    }

    public void deleteBoardMessage(LinkedHashMap<Integer, BoardMessage> numberedAllBoardMessagesMap) {
        BoardMessageDAO boardMessageDAO = new BoardMessageDAO();
        int choice = 0, result;
        boolean isValid;
        BoardMessage chosenMessage;

        if (!numberedAllBoardMessagesMap.isEmpty()) {
            System.out.println("\nChoose the message you want to delete:");
            do {
                choice = takeNumberInput();
                isValid = numberedAllBoardMessagesMap.containsKey(choice);
                if (!isValid) {
                    System.out.println("\n" + choice + " is not a valid choice.");
                }
            } while (!isValid);

            chosenMessage = numberedAllBoardMessagesMap.get(choice);
            result = boardMessageDAO.deleteMessage(chosenMessage);
            if (result == 1) {
                System.out.println("\nMessage deleted successfully!\n");
            } else {
                System.out.println("\nThe message could not be deleted.\n");
            }
        } else {
            System.out.println("There are no messages to delete!");
        }
    }

}
