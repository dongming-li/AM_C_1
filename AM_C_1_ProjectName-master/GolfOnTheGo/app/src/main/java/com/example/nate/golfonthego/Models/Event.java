package com.example.nate.golfonthego.Models;

/**
 * Created by tyler on 10/23/17.
 * Date and event name are  saved here for guild events
 */

public class Event {
    public String date;
    public String eventName;
    public String courseName;

    /**
     * Creates a new event based on the info given by the user
     * @param date The date that the event takes place
     * @param eventName The name of the event
     * @param courseName The name of the course that the event takes place on
     */
    public Event(String date, String eventName, String courseName) {
        this.date = date;
        this.eventName = eventName;
        this.courseName = courseName;
    }
}
