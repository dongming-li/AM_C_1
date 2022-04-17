package com.example.nate.golfonthego.Models;

/**
 * Created by tyler on 10/21/2017.
 * A User object for storing info of users in the Golfverse
 */

public class User {

    private String name;
    private String password;
    private int userID;
    private int isAdmin;

    /**
     * @param name The username for the user
     * @param password The password for the user
     * Creates a new user object and returns
     */
    public User(String name, String password, int userID){
        this.name = name;
        this.password = password;
        this.userID = userID;
    }


    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setAdmin(int isAdmin){ this.isAdmin = isAdmin; }

    public boolean isAdmin(){
        return this.isAdmin == 1;
    }
}
