package com.example.nate.golfonthego;

import android.content.Context;
import android.graphics.Point;
import android.location.Location;
import android.os.SystemClock;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nate.golfonthego.Models.Course;
import com.example.nate.golfonthego.Models.GolfBag;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Observable;
import java.util.Observer;


/**
 * Created by Leo on 10/31/2017.
 */

public class Gameplay extends Observable implements Observer{

    private GoogleMap courseMap;
    private Marker ballMark;
    private Context courseCon;
    private Swinger playerSwing;
    private static Gameplay game;
    private Course course;
    private int holeNum;
    private GolfBag bag;

    public boolean startedSwing = false;

    private static int StrokeCount = 0;

    public boolean gamePlayInProgress;
    public boolean gameHasBeenStarted;

    public int increaseStrokeCount(){ return this.StrokeCount++; }
    public int getStrokeCount(){ return this.StrokeCount; }

    private Gameplay(){ gameHasBeenStarted = false; }

    public void setParameters(GoogleMap gm, Marker m, Context c, int courseNumber, int holeNumber, Course currCourse){
        courseMap = gm;
        ballMark = m;
        courseCon = c;
        course = currCourse;
        playerSwing = Swinger.getSwinger();
        playerSwing.addObserver(this);
        this.addObserver(currCourse.getBall());
        holeNum = holeNumber;
        bag = GolfBag.getBag();
        startedSwing = false;
    }

    public static Gameplay getGameplay(){
        if (game == null)
            return game = new Gameplay();
        return game;
    }

    public void executeSwing(Button b, FragmentManager fg){
        b.setVisibility(View.INVISIBLE);
        b.setVisibility(View.GONE);

        startedSwing = true;
        swingFragment swingFrag = new swingFragment();
        FragmentTransaction swingFragTransaction = fg.beginTransaction();
        swingFragTransaction.attach(swingFrag);
        swingFragTransaction.add(R.id.swingFrame, swingFrag);
        //swingFragTransaction.addToBackStack(null);
        swingFragTransaction.commit();
    }

    public void moveBall(Context con){
        //test to see if swinger singleton works
        if(playerSwing != null){
            CharSequence text =
                    "\nPower:      " + playerSwing.power +
                    "\nError:      " + playerSwing.error;
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(con, text, duration);
            toast.show();
            LatLng ballFinalPosition = calculateSwing(courseMap, playerSwing.score);
            animateMarker(ballMark, ballFinalPosition, false);

            setChanged();
            notifyObservers(ballFinalPosition);
            courseCon = con;
            playerSwing.first = true;
            startedSwing = true;
            course.incrementScore(holeNum);
            this.gamePlayInProgress = false;
        }
    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = courseMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 5000;

        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {

                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    @Override
    public void update(Observable o, Object n){
        moveBall(courseCon);
    }

    private LatLng calculateSwing(GoogleMap currMap, float shotLength){

        switch(bag.club){
            case 0:
                break;
            case 1:
                shotLength *= 0.8;
                break;
            case 2:
                shotLength *= 0.6;
                break;
            case 3:
                shotLength *= 0.4;
                break;
            case 4:
                shotLength *= 0.2;
                break;
        }

        double swingLat = 0;
        double ballLat = ballMark.getPosition().latitude;
        double swingLng = 0;
        double ballLng = ballMark.getPosition().longitude;
        LatLng playerLatLng = new LatLng(ballLat, ballLng);
        Location ballLoc = new Location("ball");
        Location holeLoc = new Location("hole");
        holeLoc.setLatitude(course.getHoleLocation(holeNum).latitude);
        holeLoc.setLongitude(course.getHoleLocation(holeNum).longitude);

        ballLoc.setLatitude(ballLat);
        ballLoc.setLongitude(ballLng);

        float tempBearing = courseMap.getCameraPosition().bearing;
        System.out.println("Bearing: " + tempBearing);
        tempBearing += playerSwing.error/8;
        tempBearing *=  Math.PI / 180;

        //between 0 and 90
        if(tempBearing <= Math.PI / 2){
            swingLat = shotLength * Math.cos(tempBearing);
            swingLng = shotLength * Math.sin(tempBearing);
        }
        //between 90 and 180
        else if(tempBearing <= Math.PI && tempBearing > Math.PI / 2){
            swingLat = -shotLength * Math.sin(tempBearing - Math.PI / 2);
            swingLng = shotLength * Math.cos(tempBearing - Math.PI / 2);
        }
        //between 180 and 270
        else if(tempBearing <= Math.PI + Math.PI / 2 && tempBearing > Math.PI){
            swingLat = -shotLength * Math.cos(tempBearing - Math.PI);
            swingLng = -shotLength * Math.sin(tempBearing - Math.PI);
        }
        //between 270 and 360
        else if(tempBearing <= 2 * Math.PI && tempBearing > Math.PI + Math.PI / 2){
            swingLat = - shotLength * Math.sin(tempBearing - Math.PI + Math.PI / 2);
            swingLng = shotLength * Math.cos(tempBearing - Math.PI + Math.PI / 2);
        }

        return new LatLng(ballLat + swingLat,  ballLng + swingLng);
    }
}
