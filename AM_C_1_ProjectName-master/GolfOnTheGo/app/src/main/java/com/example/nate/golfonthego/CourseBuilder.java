package com.example.nate.golfonthego;

import android.content.Context;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.nate.golfonthego.Controllers.courseBuilderController;
import com.example.nate.golfonthego.Models.Hole;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;

public class CourseBuilder extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Button btnAdd;
    private RadioGroup rdoGroup;
    private ArrayList<LatLng> stagedAdds;
    private int currentHoleNum;
    private Hole currentHole;
    private ArrayList<Marker> markerList;
    private Polygon fairway = null;
    private Polygon green = null;
    private Marker Tee;
    private Marker flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_builder);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.courseBuilderMap);
        mapFragment.getMapAsync(this);

        //set the buttons
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(btnAddClick());
        rdoGroup = findViewById(R.id.rdoGroupEditorSelector);
        rdoGroup.setOnCheckedChangeListener(rdoChange());

        //setup array list to add the locations to that the user wants
        stagedAdds = new ArrayList<>();

        //get the current hole being edited
        currentHoleNum = (int) getIntent().getExtras().get(courseBuilderHoleSelector.tag_current_hole);
        currentHole = courseBuildCourseSelector.courseBuilder.getHole(currentHoleNum);

        //setup markerlist
        markerList = new ArrayList<>();
    }

    //Things for button control
    private View.OnClickListener btnAddClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stagedAdds.size() > 0) {
                    courseBuildCourseSelector.courseBuilder.addLatLng(stagedAdds, currentHoleNum, rdoGroup.getCheckedRadioButtonId());
                    currentHole = courseBuildCourseSelector.courseBuilder.getHole(currentHoleNum);

                    drawHole(rdoGroup.getCheckedRadioButtonId(), stagedAdds);
                    stagedAdds.clear();
                    clearMarkerList();
                    Toast.makeText(CourseBuilder.this, "Added to Hole", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private RadioGroup.OnCheckedChangeListener rdoChange(){
        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                ArrayList<LatLng> points = new ArrayList<>();
                if(i == R.id.rdoTee){
                    points.add(currentHole.getTee());
                }else if (i == R.id.rdoFairway){
                    points = currentHole.getFairway();
                }else if (i == R.id.rdoGreen){
                    points = currentHole.getGreen();
                }else if(i == R.id.rdoFlag){
                    if(currentHole.getFlagLocationAsLatLng()!=null){
                        points.add(currentHole.getFlagLocationAsLatLng());
                    }
                }

                stagedAdds.clear();
                clearMarkerList();

                Log.i("points", points.toString());

                drawHole(R.id.rdoFairway, currentHole.getFairway());
                drawHole(R.id.rdoGreen, currentHole.getGreen());

                drawHole(i, points);

                LatLng start;

                if(currentHole.getTee() != null){
                    start = currentHole.getTee();
                }
                else{
                    start = new LatLng(42.026486, -93.647377);
                }

                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 16.0f));
            }
        };
    }

    //Google Maps things
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //set the start to the tee if it exists
        LatLng start;
        if(currentHole.getTee() != null){
            start = currentHole.getTee();
        }
        else{
            start = new LatLng(42.026486, -93.647377);
        }

        if(currentHole.flagLocation != null){
            flag = mMap.addMarker(new MarkerOptions().position(currentHole.getFlagLocationAsLatLng()));
        }

        Tee = mMap.addMarker(new MarkerOptions().position(start));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 17.0f));
        mMap.setOnMapClickListener(mapClickListener());

        drawHole(R.id.rdoFairway, currentHole.getFairway());
        drawHole(R.id.rdoGreen, currentHole.getGreen());

        setMapStyle();
    }

    private GoogleMap.OnMapClickListener mapClickListener() {
        return new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(rdoGroup.getCheckedRadioButtonId() == R.id.rdoTee){
                    stagedAdds.clear();
                    if(Tee != null){
                        Tee.remove();
                    }
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                    Tee =  mMap.addMarker(markerOptions);
                }
                else if(rdoGroup.getCheckedRadioButtonId() == R.id.rdoFlag){
                    stagedAdds.clear();
                    if(flag != null){
                        flag.remove();
                    }
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                    flag =  mMap.addMarker(markerOptions);
                }
                else if(stagedAdds.size() == 0){
                    clearMarkerList();
                    clearPolygons(rdoGroup.getCheckedRadioButtonId());
                }

                if(rdoGroup.getCheckedRadioButtonId() != R.id.rdoTee && rdoGroup.getCheckedRadioButtonId() != R.id.rdoFlag){
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                    Marker m = mMap.addMarker(markerOptions);
                    markerList.add(m);
                }

                stagedAdds.add(latLng);
            }
        };
    }

    private void setMapStyle(){
        boolean mapStyleSuccess = mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.map_style)));
        if(!mapStyleSuccess) {
            Context context = getApplicationContext();
            CharSequence text = "Style Input Failed";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    private void drawHole(int selectedID, ArrayList<LatLng> list){
        if(selectedID == R.id.rdoFairway && list.size() > 0){
            PolygonOptions fairwayOpt = new PolygonOptions()
                    .addAll(list).fillColor(Color.GREEN).strokeJointType(2).strokeWidth((float)10).strokeColor(Color.GREEN);
            Polygon fairway = mMap.addPolygon(fairwayOpt);
            fairway.setZIndex(0);
            if(this.fairway != null){
                clearPolygons(selectedID);
            }
            this.fairway = fairway;

        }else if(selectedID == R.id.rdoGreen && list.size() > 0){
            PolygonOptions greenOpt = new PolygonOptions()
                    .addAll(list).fillColor(Color.GREEN);
            Polygon green = mMap.addPolygon(greenOpt);
            green.setZIndex(1);
            if(this.green != null){
                clearPolygons(selectedID);
            }
            this.green = green;

        }
    }

    private void clearMarkerList(){
        for(Marker m:markerList){
            m.remove();
        }
    }

    private void clearPolygons(int selectedID){
        if(selectedID == R.id.rdoFairway && fairway != null){
            this.fairway.remove();
        }else if(selectedID == R.id.rdoGreen && green != null){
            this.green.remove();
        }
    }
}
