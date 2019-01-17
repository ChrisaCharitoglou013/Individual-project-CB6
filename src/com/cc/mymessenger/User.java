/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.mymessenger;

/**
 *
 * @author Xrysa
 */
public class User {
    
    private int id;
    private String username;
    private String password;
    private String lname;
    private String fname;
    private int failedTries;
    private Status status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public int getFailedTries() {
        return failedTries;
    }

    public void setFailedTries(int failedTries) {
        this.failedTries = failedTries;
    }  
        
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }   
    
}
