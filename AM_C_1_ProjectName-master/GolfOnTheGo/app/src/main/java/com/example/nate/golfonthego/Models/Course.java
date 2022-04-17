package com.example.nate.golfonthego.Models;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Constants.ConstantURL;
import VolleyAPI.RequestQueueSingleton;
import VolleyAPI.VolleyBall;

/**
 * Created by nate on 10/9/17.
 * Used to store courses in android
 */
public class Course {

    public static ArrayList<Course> allCourses;
    public ArrayList<Hole> holes;
    private Ball currentBall;
    private int totalScore;
    public int courseNumber;

    /**
     * Constructor for courses.
     * @param courseNumber The number of the course to be construced.
     */
    public Course(int courseNumber) {
        this.courseNumber = courseNumber;
        holes = new ArrayList<>();
        CourseInit(courseNumber);
        totalScore = 0;
        //sets up the ball for the course
        currentBall = new Ball(courseNumber, getTee(1));
    }

    /**
     * Constructor for network courses
     */
    public Course(){
        holes = new ArrayList<>();
        currentBall = new Ball();
    }

    /**
     * return the fairway for a hole
     * @param hole hole number
     * @return List of LatLng of points on the fairway.
     */
    public ArrayList<LatLng> getFairway(int hole) {
        return holes.get(hole - 1).getFairway();
    }

    /**
     * Return the green for a hole.
     * @param hole
     * @return List of LatLng of points on the green
     */
    public ArrayList<LatLng> getGreen(int hole) {
        return holes.get(hole - 1).getGreen();
    }

    /**
     * Return tee location
     * @param hole hole number
     * @return tee LatLng
     */
    public LatLng getTee(int hole) {
        try {
            return holes.get(hole - 1).getTee();
        } catch (Exception e) { return null; }
    }

    /**
     * Return hole location
     * @param hole hole number
     * @return hole LatLng
     */
    public LatLng getHoleLocation(int hole) { return holes.get(hole - 1).getFlagLocationAsLatLng(); }

    /**
     * Increase the score for a player on a hole by 1.
     * @param hole hole number
     */
    public void incrementScore(int hole){holes.get(hole-1).holeScore ++;}

    /**
     * Get the total score for a player so far
     * @return the score of the player for all holes played
     */
    public int getTotalScore(){ return this.totalScore; }

    /**
     * Sets the total score automatically
     * @return
     */
    public void updateTotalScore(int hole){ this.totalScore += holes.get(hole-1).holeScore; }

    /**
     * Set the score to 0 for the next hole
     * @param hole hole number.
     */
    public void resetScore(int hole){
        holes.get(hole-1).holeScore = 0;
    }

    /**
     *Resets totalScore to 0
     */
    public void resetTotalScore(){this.totalScore = 0;}

    /**
     * return players current score for a hole
     * @param hole hole number
     * @return players score on the hole
     */
    public int getScore(int hole){return holes.get(hole-1).holeScore;}

    /**
     * Get ball object
     * @return current ball
     */
    public Ball getBall() {return currentBall;}

    /**
     * Initializes courses, soon to be deprecated.
     * @param courseNumber course number
     */
    private void CourseInit (int courseNumber) {
        if(courseNumber == 1){
            this.courseNumber = 1;
            ArrayList<LatLng> hole1 = new ArrayList<LatLng>();
            hole1.add(new LatLng(42.026855, -93.647630));
            hole1.add(new LatLng(42.026499, -93.647619));
            hole1.add(new LatLng(42.026224, -93.647684));
            hole1.add(new LatLng(42.026377, -93.646026));
            hole1.add(new LatLng(42.026356, -93.645405));
            hole1.add(new LatLng(42.026778, -93.645426));
            hole1.add(new LatLng(42.026814, -93.646231));
            hole1.add(new LatLng(42.026655, -93.646950));

            LatLng hole1Tee = new LatLng(42.026486, -93.647377);

            ArrayList<LatLng> hole1Green = new ArrayList<LatLng>();
            hole1Green.add(new LatLng(42.026633, -93.645787));
            hole1Green.add(new LatLng(42.026406, -93.645795));
            hole1Green.add(new LatLng(42.026370, -93.645495));
            hole1Green.add(new LatLng(42.026677, -93.645500));

            Hole holeToAdd = new Hole(this);
            holeToAdd.setFairway(hole1);
            holeToAdd.setGreen(hole1Green);
            holeToAdd.setTee(hole1Tee);
            holeToAdd.setFlagLocation(new LatLng(42.06400, -93.645600));

            holes.add(holeToAdd);
            // end course 1
        }
        if(courseNumber == 2){
            this.courseNumber = 2;
            ArrayList<LatLng> hole1 = new ArrayList<LatLng>();
            hole1.add(new LatLng(42.021608, -93.677601));
            hole1.add(new LatLng(42.021606, -93.677515));
            hole1.add(new LatLng(42.021841, -93.677520));
            hole1.add(new LatLng(42.021841, -93.677676));

            LatLng hole1Tee = new LatLng(42.021650, -93.677566);

            ArrayList<LatLng> hole1Green = new ArrayList<LatLng>();
            hole1Green.add(new LatLng(42.021787, -93.677604));
            hole1Green.add(new LatLng(42.021789, -93.677534));
            hole1Green.add(new LatLng(42.021787, -93.677604));
            hole1Green.add(new LatLng(42.021788, -93.677534));

            Hole holeToAdd = new Hole(this);
            holeToAdd.setFairway(hole1);
            holeToAdd.setGreen(hole1Green);
            holeToAdd.setTee(hole1Tee);
            holeToAdd.setFlagLocation(new LatLng(42.021788, -93.677534));

            holes.add(holeToAdd);
            // end course 2 test course at nates apartment
        }
        if(courseNumber == 3) {
            this.courseNumber = 3;
            ArrayList<LatLng> hole1 = new ArrayList<LatLng>();
            hole1.add(new LatLng(42.028258, -93.649823));
            hole1.add(new LatLng(42.028198, -93.649820));
            hole1.add(new LatLng(42.028156, -93.650238));
            hole1.add(new LatLng(42.028266, -93.650694));
            hole1.add(new LatLng(42.028381, -93.650789));
            hole1.add(new LatLng(42.028523, -93.650673));
            hole1.add(new LatLng(42.028409, -93.650418));
            hole1.add(new LatLng(42.028262, -93.650139));

            LatLng hole1Tee = new LatLng(42.028236, -93.649875);

            ArrayList<LatLng> hole1Green = new ArrayList<LatLng>();
            hole1Green.add(new LatLng(42.028373, -93.650725));
            hole1Green.add(new LatLng(42.028423, -93.650663));
            hole1Green.add(new LatLng(42.028379, -93.650599));
            hole1Green.add(new LatLng(42.028329, -93.650696));

            Hole holeToAdd = new Hole(this);
            holeToAdd.setFairway(hole1);
            holeToAdd.setGreen(hole1Green);
            holeToAdd.setTee(hole1Tee);
            holeToAdd.setFlagLocation(new LatLng(42.028367, -93.650666));

            holes.add(holeToAdd);
            // end course 3 test course at attanassoff for demos

        }
    }

    /**
     * Gets just the course id's from the database and not the holes
     * @param context The context that the method was called from
     */
    //called as soon as the main screen is loaded in order to load all courses from the database
    public static void loadCourses(Context context){
        allCourses = new ArrayList<>();
        String url = ConstantURL.URL_LOADCOURSES;

        VolleyBall.getResponseJsonArray(context, new VolleyBall.VolleyCallback<JSONArray>() {
            @Override
            public void doThings(JSONArray result) {
                for(int i = 0; i < result.length(); i++){
                    try {
                        JSONObject obj = result.getJSONObject(i);
                        Course c = new Course();
                        c.courseNumber = obj.getInt("courseID");
                        allCourses.add(c);
                        Log.d("json array error", c.toString());
                    }
                    catch (JSONException e) {
                        Log.i("json array error", e.toString());
                    }
                }
            }
        }, url);
    }

    /**
     * Called the getRequestForSave method after iterating through each hole and saving them
     * @param context The context that the method was called from
     */
    public void saveCourse(Context context){
        for(int i = 0; i < holes.size(); i++){
            RequestQueueSingleton.getInstance(context).addToRequestQueue(getRequestForSave(i, holes.get(i)));
        }
    }

    /**
     * Used to save the course and all its holes to the database
     * @param holeNumber what ever hole is currently being saved
     * @return The string request that us used to save the course to the database
     */
    private StringRequest getRequestForSave(final int holeNumber, final Hole hole){
        String url = ConstantURL.URL_SAVECOURSE;

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("Save data", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("errorSave",error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                ArrayList<LatLng> fairway = hole.getFairway();
                ArrayList<LatLng> green = hole.getGreen();

                ArrayList<String> fairwayStrings = new ArrayList<>();
                ArrayList<String> greenStrings = new ArrayList<>();

                for(int i = 0; i < fairway.size(); i++){
                    LatLng ltlng = fairway.get(i);
                    fairwayStrings.add(ltlng.latitude + "," + ltlng.longitude);
                }

                for(int i = 0; i < green.size(); i++){
                    LatLng ltlng = green.get(i);
                    greenStrings.add(ltlng.latitude + "," + ltlng.longitude);
                }

                JSONArray fairwayJS = new JSONArray(fairwayStrings);
                JSONArray greenJS = new JSONArray(greenStrings);

                LatLng tee = hole.getTee();
                String teeString = tee.latitude + "," + tee.longitude;

                params.put("tee", teeString);
                params.put("fairway", fairwayJS.toString());
                params.put("green", greenJS.toString());
                params.put("courseID", courseNumber + "");
                params.put("holeNumber",holeNumber + "");
                Location flag = hole.flagLocation;
                params.put("holeLocation", flag.getLatitude()+ "," + flag.getLongitude());
                Log.d("jason", params.toString());
                return params;
            }
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded;  charset=utf-8");
                return params;
            }
        };

        return jsonObjReq;
    }
}

