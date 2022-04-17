package com.example.nate.golfonthego;

import java.util.Observable;

public class Swinger extends Observable{

    /*Backswing is the variable keeping track of the stages of the swing.
        *   0: the swing has not started yet.
        *   1: the user has indicated somehow they want to swing. (e.g. button)
        *   2: the user has started the swing by swinging back past the backSwingVal threshold.
        *   3: the user has started to swing forward, so the acc values are being tracked and calculated.
        *   0: when the user has stopped swinging. the done() method will return true after first swing now.*/

    //this is a singleton class, here is the instance variable
    private static Swinger swinger;

    int swingTrack;
    boolean first = true;
    float x, y, z;
    private float Xmax, Xmin, Xavg, Ymax, Ymin, Yavg, Zmax, Zmin, Zavg;
    float power, overswing, error, score;
    boolean swingLefty;

    /* scoreArray is a 4 index array where:
        *   0 : power
        *   1 : overswing
        *   2 : error
        *   3 : scoreArray
    */
    float[] scoreArray = new float[4];


    //three parameeters:
    //x, y, and z are acceleration values
    //swingTemp is the backswing value, or int value tracking swing progress.

    private Swinger(boolean orient){
        x = 0;
        y = 0;
        z = 0;
        swingTrack = 0;
        first = true;
        swingLefty = orient;
    }

    public static Swinger getSwinger(){
        if (swinger == null)
            return swinger = new Swinger(false);
        return swinger;
    }

    public void swapOrient(){
        swingLefty = !swingLefty;
    }

    public synchronized void swang() {

        if(swingTrack == 0){
            return;
        }

        if(!swingLefty) {
            //start the swing logic on a backswing
            if (swingTrack == 1 && (x + z < -5 || x < -5 || z < -5)) {
                swingTrack = 2;
                return;
            }
            if (swingTrack == 2 && ((x > 0 && z > 0) || x > 10)) {
                swingTrack = 3;
                return;
            }

            //after the swing starts...
            //calculating max/min/avg
            if (swingTrack == 3) {
                Xavg = (Xavg + x) / 2;
                Yavg = (Yavg + y) / 2;
                Zavg = (Zavg + z) / 2;
                if (x > Xmax) {
                    Xmax = x;
                }
                if (x < Xmin) {
                    Xmin = x;
                }
                if (y > Ymax) {
                    Ymax = y;
                }
                if (y < Ymin) {
                    Ymin = y;
                }
                if (z > Zmax) {
                    Zmax = z;
                }
                if (z < Zmin) {
                    Zmin = z;
                }

            }
        }
        if(swingLefty){
            if (swingTrack == 1 && (x + z > 5 || x > 5 || z > 5)){
                swingTrack = 2;
                return;
            }
            if (swingTrack == 2 && ((x < 0 && z < 0) || x < 10)){
                swingTrack = 3;
                return;
            }

            //after the swing starts...
            //calculating max/min/avg
            if (swingTrack == 3) {
                Xavg = (Xavg + x) / 2;
                Yavg = (Yavg + y) / 2;
                Zavg = (Zavg + z) / 2;
                if (x > Xmax) {
                    Xmax = x;
                }
                if (x < Xmin) {
                    Xmin = x;
                }
                if (y > Ymax) {
                    Ymax = y;
                }
                if (y < Ymin) {
                    Ymin = y;
                }
                if (z > Zmax) {
                    Zmax = z;
                }
                if (z < Zmin) {
                    Zmin = z;
                }

            }
        }
        //end swing
        if (swingTrack == 3 && x >= 5 && z >= 5) {

            if(swingLefty){
                Xmax = -Xmax;
                Ymax = -Ymax;
                Zmax = -Zmax;
                Xavg = -Xavg;
                Yavg = -Yavg;
                Zavg = -Zavg;
            }
            //calculating power, overswing, error, and score.
            power = Xmax + Zmax;
            overswing = ((Xmax + Zmax) - (Xavg + Zavg)) - 60;

            //error is based solely on Y acceleration
            error = (Yavg - 5);
            overswing = overswing < 0 ? 0 : overswing;

            //20 power is 100 yds
            //30 power is 200 yds
            //40 power is 300 yds
            float yards = 10 * (power + overswing) - 100;

            // multiply yards by 3 to convert yards to feet
            // divide feet by 3280.4 to go from feet to kilometers
            // 90 / 10000 is the conversion from kilometers to lat/lng
            // divide by 10
            //score is now in "lat/lng" units
            score = (yards) * (3) / (3280.4f) * (9);
            score = score / 10000;

            first = false;
            swingTrack = 0;

            scoreArray[0] = power;
            scoreArray[1] = overswing;
            scoreArray[2] = error;
            scoreArray[3] = score;
        }

    }

    public void clearSwing(){
        swingTrack = 0;
        first = true;
        x = 0; y = 0; z = 0;
        Xmax = 0; Xmin = 0; Xavg = 0; Ymax = 0; Ymin = 0; Yavg = 0; Zmax = 0; Zmin = 0; Zavg = 0;
        power = 0; overswing = 0; error = 0; score = 0;
    }

    public synchronized void done(){
        setChanged();
        this.notifyObservers();
    }

}
