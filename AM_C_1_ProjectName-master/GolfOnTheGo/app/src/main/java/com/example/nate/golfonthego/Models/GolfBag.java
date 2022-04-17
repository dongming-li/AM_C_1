package com.example.nate.golfonthego.Models;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.nate.golfonthego.CourseActivity;

import java.util.Observable;

/**
 * Created by Leo on 11/30/2017.
 */

public class GolfBag extends Observable {
    public int club = 0;
    public String clubName = "";

    private static GolfBag bag;

    private GolfBag(int club){
        this.club = club;
        this.clubName = "Driver";
    }

    public static GolfBag getBag(){
        if(bag == null)
            bag = new GolfBag(0);
        return bag;
    }

    public int ShowBag(Context context) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose an Club");

// add a list
        String[] clubs = {"Driver", "Long Iron", "Short Iron", "Wedge", "Putter"};
        builder.setItems(clubs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        club = 0;
                        clubName = "Driver";
                        break;// driver
                    case 1:
                        club = 1;
                        clubName = "Long Iron";
                        break;// long iron
                    case 2:
                        club = 2;
                        clubName = "Short Iron";
                        break;// short iron
                    case 3:
                        club = 3;
                        clubName = "Wedge";
                        break;// wedge
                    case 4:
                        club = 4;
                        clubName = "Putter";
                        break;// putter
                }
                setChanged();
                notifyObservers();
                //update text
            }
        });
// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        return 0;
    }
}
