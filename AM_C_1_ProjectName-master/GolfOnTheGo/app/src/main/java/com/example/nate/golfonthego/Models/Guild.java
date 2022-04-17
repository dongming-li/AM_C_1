package com.example.nate.golfonthego.Models;

/**
 * Created by tyler on 10/15/17.
 * Class for the guilds objects
 */

public class Guild {
    private String _name;
    private int _id;
    private int currentUserIsLeader;

    /**
     * returns the name of the guild
     * @return (String) Name of the guild
     */
    public String get_name() {
        return _name;
    }

    /**
     * Sets the name of the guild to a new name
     * @param _name The new name that the user wants to set
     */
    public void set_name(String _name) {
        this._name = _name;
    }

    /**
     * Get the id of the guild
     * @return The id of the guild stored in the database
     */
    public int get_id() {
        return _id;
    }

    /**
     * Sets the id of the guild to a new id
     * @param _id The new id that the user wants to set
     */
    public void set_id(int _id) {
        this._id = _id;
    }

    /**
     * checks if the current user that is logged in is a leader of this guild
     * @return returns whether or not the current user logged in is a guild leader for this guild
     */
    public int isLeader(){
        return currentUserIsLeader;
    }

    /**
     * Creates a new guild based on parameters
     * @param _name The name of the guild
     * @param _id the id of the new guild
     * @param currentUserIsLeader whether or not the current user is a leader of the guild
     */
    public Guild(String _name, int _id, int currentUserIsLeader) {
        this._name = _name;
        this._id = _id;
        this.currentUserIsLeader = currentUserIsLeader;
    }
}
