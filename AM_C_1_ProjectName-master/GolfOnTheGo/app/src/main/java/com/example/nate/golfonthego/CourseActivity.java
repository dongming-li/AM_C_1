package com.example.nate.golfonthego;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.nate.golfonthego.Models.Course;
import com.example.nate.golfonthego.Models.CourseToPlay;
import com.example.nate.golfonthego.Models.GolfBag;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.Observer;

import Constants.ConstantURL;
import VolleyAPI.VolleyBall;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.nate.golfonthego.R.id.map;

/**
 * CourseActivity is the main game screen of the app. A google map takes up the screen, and a
 * user walks around, to be with in proximity of their ball to trigger screens to play the game.
 */
public class CourseActivity extends FragmentActivity implements OnMapReadyCallback,
        ConnectionCallbacks, OnConnectionFailedListener, swingFragment.OnFragmentInteractionListener{

    // buttons for the activity
    private Button swingButton;
    // where gameplay logic resides
    private Gameplay SwingGame = Gameplay.getGameplay();
    // main google map object
    private GoogleMap mMap;
    // api for getting location
    private FusedLocationProviderApi fusedLocationProvider = LocationServices.FusedLocationApi;
    //api for google api's lol
    private GoogleApiClient googleApiClient;
    // for getting locations
    private LocationCallback locationCallback = new LocationCallback();
    // the actual location variable
    Location currentLocation;
    // current location marker
    Marker livePlayerMarker;
    Marker ballMarker;

    public TextView clubTextView;
    public TextView holeNumTextView;
    public TextView scoreTextView;
    private int duplicateBall = 0;
    public Course currentCourse;
    public int currentHole;
    public Marker tempTeeMarker;

    // golf bag with club
    private GolfBag bag;

    /*final Course currentCourse,
    final int currentHole, final Marker tempTeeMarker*/

    // request for location
    LocationRequest locationRequest;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    // main on create of the activity

    /**
     * onCreate of the application, builds location permissions, and connections to the google
     * api.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // create the location request
        this.checkLocationPermission();
        this.createLocationRequest();
        // create the api connection
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        //set the map view
        setContentView(R.layout.activity_course);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        holeNumTextView = findViewById(R.id.holeNumText);
        holeNumTextView.setVisibility(View.INVISIBLE);

        scoreTextView = findViewById(R.id.scoreText);
        scoreTextView.setVisibility(View.INVISIBLE);

        clubTextView = findViewById(R.id.clubText);
        clubTextView.setVisibility(View.INVISIBLE);

        bag = GolfBag.getBag();
    }

    /**
     * fragment interaction required for fragments
     * @param uri
     */
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * google api client call start
     */
    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    /**
     * google api client calls stop
     */
    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // set the google map object and attempt to add the map style
        mMap = googleMap;
        setMapStyle();

        //pick a course to load in, eventually will be extended to be based on savedIntsanceState
        currentCourse = CourseToPlay.getCourse();
        currentHole = 1;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCourse.getTee(1), (float)19.0));
        // this is the instantiation of the player marker, updates position when permissed.
        livePlayerMarker = mMap.addMarker(
                new MarkerOptions().position(currentCourse.getTee(currentHole)).title("You are here"));
        tempTeeMarker = mMap.addMarker(
                new MarkerOptions().position(currentCourse.getTee(currentHole)).title("Move Here to Play"));
        // tee marker on the map
        Bitmap startBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.course_start);
        BitmapDescriptor startBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(startBitmap);
        tempTeeMarker.setIcon(startBitmapDescriptor);

        initializeButtons();
        SwingGame.gamePlayInProgress = false;
        locationBasedContent();
        drawHole(currentCourse, currentHole);
        System.out.println("onmapready completed");
    }

    /**
     * set the stylr of the google map to the custom style
     */
    public void setMapStyle(){
        boolean mapStyleSuccess = mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.map_style)));
        if(!mapStyleSuccess) {
            Context context = getApplicationContext();
            CharSequence text = "Style Input Failed";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    /**
     * initialize all the buttons on the screen
     */
    public void initializeButtons(){
        // swing button top left of screen
        swingButton = (Button)findViewById(R.id.swingButton);
        swingButton.setVisibility(View.GONE);
        swingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                FragmentManager tempfrag = getSupportFragmentManager();
                if(SwingGame.gamePlayInProgress)
                    SwingGame.executeSwing(swingButton, tempfrag);
            }
        });
    }

    /**
     * Game content that is run while the activity is getting location updates.
     */
    public void locationBasedContent(){
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    // set the pin that represents the users location
                    livePlayerMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                    // set a marker where the tee is for the given course

                    setScoreText();

                    Location teeLocation = new Location("tmp");
                    if(!currentCourse.getBall().hasTeedOff) {
                        teeLocation.setLatitude(tempTeeMarker.getPosition().latitude);
                        teeLocation.setLongitude(tempTeeMarker.getPosition().longitude);
                    }

                    currentLocation = location;
                    LinearLayout ll = (LinearLayout)findViewById(R.id.swingLayout);

                    // if the distance  between the player and the first tee is less than 20 meters
                    // any more precise may cause issues due to the inconsistency of
                    // fine location gps -nate
                    if(currentCourse.getBall().getCurrentBallLocation().distanceTo(currentCourse.holes.get(currentHole - 1).flagLocation) < 10){
                        // check to see if there is a next hole
                        SwingGame.gamePlayInProgress = false;
                        swingButton.setVisibility(View.INVISIBLE);
                        swingButton.setVisibility(View.GONE);

                        if(currentCourse.getTee(currentHole + 1) == null)
                            endGame();
                        else
                            endHole();
                    }
                    else if(location.distanceTo(teeLocation) < 2000 && !SwingGame.gamePlayInProgress && !currentCourse.getBall().hasTeedOff){
                        if(duplicateBall > 0){
                            ballMarker.remove();
                            duplicateBall --;
                        }

                        Bitmap ballMap = BitmapFactory.decodeResource(getResources(), R.mipmap.ball_marker_foreground);
                        BitmapDescriptor ballBitmap = BitmapDescriptorFactory.fromBitmap(ballMap);
                        ballMarker = mMap.addMarker(
                                new MarkerOptions().position(currentCourse.getTee(currentHole)).title("Move Here to Play"));
                        ballMarker.setIcon(ballBitmap);
                        ballMarker.setAnchor(0.5f,0.5f);
                        tempTeeMarker.remove();

                        // the following needs to be changed to be more modular
                        SwingGame.setParameters(mMap, ballMarker, getApplicationContext(),currentCourse.courseNumber,currentHole, currentCourse);
                        SwingGame.gamePlayInProgress = true;
                        //button appears
                        swingButton.setVisibility(View.VISIBLE);
                        swingButton.setText("Swing");
                        duplicateBall ++;
                    }
                    else if(location.distanceTo(currentCourse.getBall().getCurrentBallLocation()) < 2000 && !SwingGame.gamePlayInProgress){
                        // the following needs to be changed to be more modular
                        SwingGame.setParameters(mMap, ballMarker, getApplicationContext(),currentCourse.courseNumber,currentHole, currentCourse);
                        SwingGame.gamePlayInProgress = true;
                        //button appears
                        swingButton.setVisibility(View.VISIBLE);
                        swingButton.setText("Swing");
                    }
                    else{
                        if(!SwingGame.startedSwing)
                        SwingGame.gamePlayInProgress = false;
                        swingButton.setVisibility(View.INVISIBLE);
                        swingButton.setVisibility(View.GONE);
                    }
                }
            };
        };
    }

    /**
     * When a hole is finished, this method is called.
     */
    public void endHole() {
        //before the hole ends
        currentCourse.updateTotalScore(currentHole);
        currentCourse.resetScore(currentHole);
        Context context = getApplicationContext();
        CharSequence text = "Mah Dude! you \n"
                + "finished the hole with a score of: " + currentCourse.getScore(currentHole);
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        ballMarker.remove();

        //hole is now ended
        currentHole++;
        currentCourse.getBall().hasTeedOff = false;
        tempTeeMarker = mMap.addMarker(
                new MarkerOptions().position(currentCourse.getTee(currentHole)).title("Start Here"));
        Bitmap startBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.course_start);
        BitmapDescriptor startBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(startBitmap);
        tempTeeMarker.setIcon(startBitmapDescriptor);
        drawHole(currentCourse, currentHole);
        CameraUpdate cameraLoc = CameraUpdateFactory.newLatLng(
                currentCourse.getTee(currentHole));
        mMap.animateCamera(cameraLoc);
    }

    /**
     * When a game is finished, all holes are finished, this method is called.
     */
    public void endGame() {
        currentCourse.updateTotalScore(currentHole);
        currentCourse.resetScore(currentHole);
        Context context = getApplicationContext();
        CharSequence text = "Mah Dude! you \n"
                + "finished the course with a score of: " + currentCourse.getTotalScore();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        currentCourse.getBall().hasTeedOff = false;
        currentCourse.getBall().setCurrentBallLocation(currentCourse.getTee(1));
        ballMarker.remove();
        sendScore(currentCourse.getTotalScore(), currentCourse.courseNumber, MainActivity.mainUser.getUserID());
        currentCourse.resetTotalScore();
        finish();
    }

    /**
     * Draws the current whole that the user is playing on the map
     * @param currentCourse the course being played
     * @param currentHole the current hole on the course being played
     */
    public void drawHole(Course currentCourse, int currentHole){
        PolygonOptions hole1 = new PolygonOptions().addAll(
                currentCourse.getFairway(currentHole))
                .fillColor(getResources().getColor(R.color.colorFairwayDrawColor)).strokeJointType(2)
                .strokeWidth((float)10).strokeColor(getResources().getColor(R.color.colorFairwayDrawColor));
        Polygon holePolygon1 = mMap.addPolygon(hole1);
        holePolygon1.setZIndex(0);
        PolygonOptions green1 = new PolygonOptions().addAll(currentCourse.getGreen(currentHole))
                .fillColor(getResources().getColor(R.color.colorGreenDrawColor));
        Polygon greenPolygon1 = mMap.addPolygon(green1);
        greenPolygon1.setZIndex(1);
        Marker hole = mMap.addMarker(new MarkerOptions().position(currentCourse.getHoleLocation(currentHole)));
        Bitmap startBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.fore_flag_location);
        BitmapDescriptor startBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(startBitmap);
        hole.setIcon(startBitmapDescriptor);
        hole.setAnchor(0.5f,0.5f);
    }

    /**
     * Updates the heads up display on the top right of the screen with the hole number the current
     * number of strokes, and the current club selected.
     */
    public void setScoreText(){
        scoreTextView.setText("" + currentCourse.getScore(currentHole));
        scoreTextView.setVisibility(View.VISIBLE);

        holeNumTextView.setText("Hole: " + currentHole);
        holeNumTextView.setVisibility(View.VISIBLE);

        clubTextView.setText("Club: " + bag.clubName);
        clubTextView.setVisibility(View.VISIBLE);
    }

    /**
     * Google api connection suspended callback
     * @param i
     */
    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * google api on connection failed callback
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Context context = getApplicationContext();
        CharSequence text = "Connection Failed";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * Callback when the google api connection succeeds.
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        try {
            fusedLocationProvider.requestLocationUpdates(googleApiClient, locationRequest,
                    locationCallback,
                    null /* Looper */);
        } catch(SecurityException e) { e.printStackTrace(); }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    /**
     * Ask for persistent location permissions.
     * @return true if the permission is granted.
     */
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(CourseActivity.this,
                                        new String[]{ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Restart the api connection because onmapready
                        //throws an error
                        googleApiClient.disconnect();
                        googleApiClient.connect();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    private void sendScore(int score,int courseID, int userID){
        VolleyBall.getResponseJson(getBaseContext(), new VolleyBall.VolleyCallback() {
            @Override
            public void doThings(Object result) {

            }
        }, ConstantURL.URL_SCOREINPUT + "score=" + score + "&courseID=" + courseID + "&userID= " + userID);
    }

}
