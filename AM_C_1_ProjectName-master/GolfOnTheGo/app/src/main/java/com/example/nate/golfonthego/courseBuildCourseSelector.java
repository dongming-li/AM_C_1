package com.example.nate.golfonthego;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.nate.golfonthego.Controllers.Adapters.courseListAdapter;
import com.example.nate.golfonthego.Controllers.courseBuilderController;
import com.example.nate.golfonthego.Models.Course;

import java.util.ArrayList;

public class courseBuildCourseSelector extends AppCompatActivity {
    private ArrayAdapter<Course> courseAdapter;
    private ListView courseListView;
    private Button btnAddCourse;
    public static courseBuilderController courseBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_build_course_selector);

        //set the data for the courses list
        courseAdapter = new courseListAdapter(this, Course.allCourses);
        courseListView = findViewById(R.id.lstBuiltCourses);

        //set button click listener
        btnAddCourse = findViewById(R.id.btnAddCourse);

        setupButtons();
    }

    private void setupButtons(){
        courseListView.setAdapter(courseAdapter);
        if(MainActivity.mainUser.isAdmin()){
            btnAddCourse.setOnClickListener(newCourseClick());
            courseListView.setOnItemClickListener(courseClick());
        }

        if(!MainActivity.mainUser.isAdmin()){
            btnAddCourse.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener newCourseClick(){
        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Course newCourse = new Course();

                //id for the new course is going to be the next index available
                Course.allCourses.add(newCourse);
                newCourse.courseNumber = Course.allCourses.size()-1;

                courseBuilder = courseBuilderController.getInstance(newCourse);
                Intent intent = new Intent(courseBuildCourseSelector.this, courseBuilderHoleSelector.class);
                startActivity(intent);

                //update the courses list
                courseAdapter.notifyDataSetChanged();
            }
        };
    }

    public AdapterView.OnItemClickListener courseClick() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                courseBuilder = courseBuilderController.getInstance((Course)adapterView.getItemAtPosition(position));
                Intent intent = new Intent(courseBuildCourseSelector.this, courseBuilderHoleSelector.class);
                startActivity(intent);
            }
        };
    }
}
