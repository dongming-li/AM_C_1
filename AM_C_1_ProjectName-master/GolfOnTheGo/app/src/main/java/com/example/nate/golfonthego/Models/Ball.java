package com.example.nate.golfonthego.Models;

import android.location.Location;

import com.example.nate.golfonthego.Gameplay;
import com.google.android.gms.maps.model.LatLng;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Leo on 11/24/2017.
 */

public class Ball implements Observer {

    private Location currentBallLocation;
    private int courseID;
    private LatLng ballLatLng;
    public boolean hasTeedOff;

    public Ball(){
        courseID = 0;
        ballLatLng = new LatLng(0,0);
        hasTeedOff = false;
        currentBallLocation = new Location("ballLocation");
    }

    public Ball(int currentCourseID, LatLng currentBallLatLng){
        courseID = currentCourseID;
        ballLatLng = currentBallLatLng;
        hasTeedOff = false;
        currentBallLocation = new Location("ballLocation");
    }

    public LatLng getBallLatLng(){return ballLatLng;}
    public Location getCurrentBallLocation(){return currentBallLocation;}

    public void setCurrentBallLocation(LatLng newPosition){
        currentBallLocation.setLatitude(newPosition.latitude);
        currentBallLocation.setLongitude(newPosition.longitude);
    }

    //whenever ball is moved, updateBall will be called.
    @Override
    public void update(Observable o, Object n){
        ballLatLng = (LatLng) n;
        currentBallLocation.setLatitude(ballLatLng.latitude);
        currentBallLocation.setLongitude(ballLatLng.longitude);
        hasTeedOff = true;
    }
}
