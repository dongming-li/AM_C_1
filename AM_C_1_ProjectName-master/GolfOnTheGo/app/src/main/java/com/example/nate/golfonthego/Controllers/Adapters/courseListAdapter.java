package com.example.nate.golfonthego.Controllers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.nate.golfonthego.MainActivity;
import com.example.nate.golfonthego.Models.Course;
import com.example.nate.golfonthego.Models.CourseToPlay;
import com.example.nate.golfonthego.R;

import java.util.ArrayList;

/**
 * Created by tyler on 11/21/2017.
 * Used to create a list of courses
 */

public class courseListAdapter extends ArrayAdapter<Course>{

    public courseListAdapter(Context context, ArrayList<Course> courses) {
        super(context, R.layout.custom_row_course, courses);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater guildInflater = LayoutInflater.from(getContext());
        View customView = guildInflater.inflate(R.layout.custom_row_course, parent, false);

        Course course = getItem(position);
        TextView txtCourseID = customView.findViewById(R.id.txtCourseID);
        Button btnCourseSelect = customView.findViewById(R.id.btnCourseSelect);

        assert course != null;
        txtCourseID.setText(course.courseNumber + "");
        btnCourseSelect.setOnClickListener(selectCourseClick(course.courseNumber));

        return customView;
    }

    private View.OnClickListener selectCourseClick(final int position){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseToPlay.initServerCourse(position, getContext());
            }
        };
    }




}
