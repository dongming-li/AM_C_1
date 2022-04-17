package com.example.nate.golfonthego.Controllers;

import android.content.Context;

import com.example.nate.golfonthego.Models.Course;
import com.example.nate.golfonthego.Models.Hole;
import com.example.nate.golfonthego.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by tkranig on 11/21/2017.
 * Used to handle the currently in progress building course
 */

public class courseBuilderController {
    private Course course = null;
    private static courseBuilderController builder = null;
    public static final String Tee = "tee";
    public static final String Fairway = "fairway";
    public static final String Green = "green";

    public static courseBuilderController getInstance(Course course){
        if(builder == null){
            builder = new courseBuilderController();
        }

        //set the course to start on
        builder.setCourse(course);

        return builder;
    }

    private void setCourse(Course course){
        this.course = course;
    }

    private courseBuilderController(){}

    public ArrayList<Hole> getHoles(){
        return course.holes;
    }
    public Hole getHole(int hole){
        return course.holes.get(hole);
    }

    public void addLatLng(ArrayList<LatLng> latLngs, int currentHole, int whichPart){
        Hole hole = getHole(currentHole);

        switch (whichPart){
            case R.id.rdoTee:
                hole.setTee(latLngs.get(0));
                break;
            case R.id.rdoFairway:
                hole.setFairway(latLngs);
                break;
            case R.id.rdoGreen:
                hole.setGreen(latLngs);
                break;
            case R.id.rdoFlag:
                hole.setFlagLocation(latLngs.get(0));
                break;
            default:
                break;
        }

        course.holes.set(currentHole, hole);
    }

    public Course getCourse(){
        return this.course;
    }

    public void AddHole(Hole hole){
        course.holes.add(hole);
    }
    public void AddAllHoles(ArrayList<Hole> holeList){
        course.holes.clear();
        course.holes.addAll(holeList);
    }

    public void saveCourse(Context context){
        course.saveCourse(context);
    }
}