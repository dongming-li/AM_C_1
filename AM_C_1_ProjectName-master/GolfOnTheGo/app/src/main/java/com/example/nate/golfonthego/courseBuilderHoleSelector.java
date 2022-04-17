package com.example.nate.golfonthego;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.nate.golfonthego.Controllers.Adapters.holeListAdapter;
import com.example.nate.golfonthego.Models.Hole;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Constants.ConstantURL;
import VolleyAPI.VolleyBall;

public class courseBuilderHoleSelector extends AppCompatActivity {
    private ArrayAdapter<Hole> holeAdapter;
    private ListView holeListView;
    private Button btnAddHole;
    private Button btnSaveCourse;
    private SwipeRefreshLayout refreshHoles;
    public static final String tag_current_hole = "currentHole";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course_builder_hole_selector);
        holeAdapter = new holeListAdapter(this, courseBuildCourseSelector.courseBuilder.getHoles());

        holeListView = findViewById(R.id.lstBuiltHoles);
        btnAddHole = findViewById(R.id.btnAddHole);
        btnSaveCourse = findViewById(R.id.btnSaveCourse);
        refreshHoles = findViewById(R.id.holesRefresher);

        refreshHoles.setRefreshing(true);
        loadCourse(this);

        setupButtons();
    }

    private void setupButtons(){
        holeListView.setAdapter(holeAdapter);
        holeListView.setOnItemClickListener(holeClick());
        btnAddHole.setOnClickListener(newHoleClick());
        btnSaveCourse.setOnClickListener(saveCourseClick());

        refreshHoles.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("holes refresh", "onRefresh called from SwipeRefreshLayout");
                        loadCourse(getBaseContext());
                    }
                });
    }


    private View.OnClickListener newHoleClick(){
        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Hole newHole = new Hole(courseBuildCourseSelector.courseBuilder.getCourse());
                courseBuildCourseSelector.courseBuilder.AddHole(newHole);

                //update the courses list
                holeAdapter.notifyDataSetChanged();

                Intent intent = new Intent(courseBuilderHoleSelector.this, CourseBuilder.class);
                intent.putExtra(tag_current_hole, courseBuildCourseSelector.courseBuilder.getHoles().size() - 1);
                startActivity(intent);
            }
        };
    }

    private AdapterView.OnItemClickListener holeClick() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(courseBuilderHoleSelector.this, CourseBuilder.class);
                intent.putExtra(tag_current_hole, position);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener saveCourseClick(){
        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                courseBuildCourseSelector.courseBuilder.saveCourse(getBaseContext());
                refreshHoles.setRefreshing(true);
                loadCourse(getBaseContext());
            }
        };
    }

    private void loadCourse(Context context){
        VolleyBall.getResponseJsonArray(context, new VolleyBall.VolleyCallback<JSONArray>() {
            @Override
            public void doThings(JSONArray result) {
                ArrayList<Hole> addedHoles = new ArrayList<>();

                try {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject obj = result.getJSONObject(i);
                        int holeNum = obj.getInt("holeNum");

                        Hole hole;
                        try{
                            hole = addedHoles.get(holeNum);
                        }
                        catch(Exception e){
                            hole = new Hole(courseBuildCourseSelector.courseBuilder.getCourse());
                            hole.setFairway(new ArrayList<LatLng>());
                            hole.setGreen(new ArrayList<LatLng>());
                            addedHoles.add(hole);
                            hole = addedHoles.get(holeNum);
                        }

                        String[] latlong = obj.getString("point").split(",");
                        Double latitude = Double.parseDouble(latlong[0]);
                        Double longitude = Double.parseDouble(latlong[1]);
                        LatLng latLng = new LatLng(latitude, longitude);

                        if (obj.getString("pointType").equals("tee")) {
                            hole.setTee(latLng);
                        } else if (obj.getString("pointType").equals("fairway")) {
                            hole.addLatLng(latLng, Hole.fairwayInt);
                        } else if (obj.getString("pointType").equals("green")) {
                            hole.addLatLng(latLng, Hole.greenInt);
                        } else if (obj.getString("pointType").equals("hole")) {
                            hole.setFlagLocation(latLng);
                        }
                    }
                }
                catch(JSONException e){
                    Log.i("json array error", e.toString());
                }

                courseBuildCourseSelector.courseBuilder.AddAllHoles(addedHoles);
                System.out.println("added all holes");

                holeAdapter.notifyDataSetChanged();
                refreshHoles.setRefreshing(false);
            }
        }, ConstantURL.URL_LOADSPECIFICCOURSE + "courseID=" + courseBuildCourseSelector.courseBuilder.getCourse().courseNumber);

    }
}
