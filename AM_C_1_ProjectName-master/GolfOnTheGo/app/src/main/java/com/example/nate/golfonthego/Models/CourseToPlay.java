package com.example.nate.golfonthego.Models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Constants.ConstantURL;
import VolleyAPI.VolleyBall;

/**
 * Created by tkranig on 12/3/17.
 * used to store the currently play course
 */

public class CourseToPlay {
    private static Course courseToPlay;

    public static Course getCourse(){
        if(courseToPlay == null){
            courseToPlay = new Course(1);
        }
        return courseToPlay;
    }

    public static void initServerCourse(int courseID, Context context){
        courseToPlay = new Course();
        courseToPlay.courseNumber = courseID;
        loadCourse(context);
        Toast.makeText(context, "Course Selected!", Toast.LENGTH_SHORT).show();
    }

    private static void loadCourse(Context context){
        VolleyBall.getResponseJsonArray(context, new VolleyBall.VolleyCallback<JSONArray>() {
            @Override
            public void doThings(JSONArray result) {
                ArrayList<Hole> holes = new ArrayList<>();

                try {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject obj = result.getJSONObject(i);
                        int holeNum = obj.getInt("holeNum");

                        Hole hole;
                        try{
                            hole = holes.get(holeNum);
                        }
                        catch(Exception e){
                            hole = new Hole(courseToPlay);
                            hole.setFairway(new ArrayList<LatLng>());
                            hole.setGreen(new ArrayList<LatLng>());
                            holes.add(hole);
                            hole = holes.get(holeNum);
                        }

                        String[] latlong = obj.getString("point").split(",");
                        Double latitude = Double.parseDouble(latlong[0]);
                        Double longitude = Double.parseDouble(latlong[1]);
                        LatLng latLng = new LatLng(latitude, longitude);

                        if (obj.getString("pointType").equals("tee")) {
                            hole.setTee(latLng);
                            System.out.println("TEEEEEEEEEE" + hole.getTee().toString());
                        } else if (obj.getString("pointType").equals("fairway")) {
                            hole.addLatLng(latLng, Hole.fairwayInt);
                            System.out.println("FAIRWAY" + hole.getFairway().toString());
                        } else if (obj.getString("pointType").equals("green")) {
                            hole.addLatLng(latLng, Hole.greenInt);
                            System.out.println("GREEN" + hole.getGreen().toString());
                        } else if (obj.getString("pointType").equals("hole")) {
                            hole.setFlagLocation(latLng);
                            System.out.println(hole.flagLocation.toString());
                        }
                    }
                }
                catch(JSONException e){
                    Log.i("json array error", e.toString());
                }
                courseToPlay.holes = holes;
                System.out.println(courseToPlay.holes.toString());
            }
        }, ConstantURL.URL_LOADSPECIFICCOURSE + "courseID=" + courseToPlay.courseNumber);
    }
}
