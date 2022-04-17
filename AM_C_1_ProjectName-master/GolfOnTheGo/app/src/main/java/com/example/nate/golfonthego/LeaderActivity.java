package com.example.nate.golfonthego;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;

import Constants.ConstantURL;
import VolleyAPI.VolleyBall;

public class LeaderActivity extends AppCompatActivity {
    //TableLayout ll = (TableLayout) findViewById(R.id.Table);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader);
        try {
            initTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //fills table with first 10 userNames.
    public void initTable() throws SQLException{
        //initializes headers.
        String header = "Names:";
        displayLine(header, -1, -1);
        //loops to pull 10 userNames from the database
        String url = ConstantURL.URL_LEADER;

        VolleyBall.getResponseJsonArray(this, new VolleyBall.VolleyCallback<JSONArray>() {
            @Override
            public void doThings(JSONArray result) {
                for(int i = 0; i < result.length(); i++){
                    try {
                        JSONObject obj = result.getJSONObject(i);
                        //displayLine(obj.getString("userName"), 0);
                        displayLine(obj.getString("userName"), obj.getInt("score"), obj.getInt("courseID"));

                    }
                    catch (JSONException e) {

                    }
                }

            }
        }, url);



    }

    public void displayLine(String name, int score, int course){
        //Find Table
        TableLayout tl=(TableLayout)findViewById(R.id.Table);
        //String[] names = {"NAME:", "Stan", "Dave", "Matt", "Larry", "Joseph", "Danny", "Luke", "Zach", "John"};

            //create a new row
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            //add text for name
            TextView tvn = new TextView(this);
            tvn.setPadding(10,0,0,0);
            tvn.setText(name);
            //tvn.setText("asdf");

            //textview.getTextColors(R.color.)
            tvn.setTextColor(Color.BLACK);

            //add name into the row
            tr.addView(tvn);

            //buffer space
            TextView tvb = new TextView(this);
            tvb.setPadding(10,0,0,0);
            tvb.setText("  ");
            tr.addView(tvb);

            //add text for score
            TextView tvs = new TextView(this);
            if(name.equals("Names:")) tvs.setText("Score:");
            else{
                tvs.setText(Integer.toString(score));
            }

            //add score into the row
            tr.addView(tvs);


            TextView tvb2 = new TextView(this);
            tvb2.setPadding(10,0,0,0);
            tvb2.setText("  ");
            tr.addView(tvb2);

            TextView tvc = new TextView(this);
            if(name.equals("Names:")) tvc.setText("Course:");
            else{
                tvc.setText(Integer.toString(course));
            }
            //add course number
            tr.addView(tvc);


            //add row into table
            tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));


    }
}
