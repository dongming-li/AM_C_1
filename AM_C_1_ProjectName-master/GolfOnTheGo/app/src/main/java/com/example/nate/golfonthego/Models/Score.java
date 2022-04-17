package com.example.nate.golfonthego.Models;

import java.util.ArrayList;

/**
 * Created by tyler on 10/22/2017.
 * Used to store scores, may change in the future when we figure out how we want this to act
 */

public class Score {
    public String courseName;
    private ArrayList<Integer> numberScore;
    private ArrayList<String> userNames;

    /**
     * Creates a new Score object
     * @param courseName The coursename that the score was made on
     */
    public Score(String courseName){
        numberScore = new ArrayList<>();
        userNames = new ArrayList<>();
        this.courseName = courseName;
    }

    /**
     * Adds a new score this this object
     * @param userName The user that set the score
     * @param score The score that the user scored
     */
    public void AddScore(String userName, int score){
        userNames.add(userName);
        numberScore.add(score);
    }

    /**
     * Gets the score at the given position
     * @param position The position you want the score of, sorted from least to most
     * @return The score at the position
     */
    public int getScore(int position){
        return numberScore.get(position);
    }

    /**
     * Gets the score that a user set inside this score, returns int.Min_value if not found
     * @param userName The given username who set the score
     * @return if the user is found returns the score, else returns int.Min_value
     */
    public int getScore(String userName){
        int index = userNames.indexOf(userName);
        if(index > 0){
            return getScore(index);
        }

        return Integer.MIN_VALUE;
    }

    /**
     * Returns the user at the given position
     * @param position The position that the user scored in
     * @return String for the user's username
     */
    public String getUser(int position){
        return userNames.get(position);
    }
}
