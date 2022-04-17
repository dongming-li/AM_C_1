package com.example.nate.golfonthego;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import Constants.ConstantURL;
import VolleyAPI.VolleyBall;

public class NewGuild extends AppCompatActivity {
    Button createNewGuild;
    EditText newGuildName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_guild);

        setupUi();
    }

    private void setupUi(){
        createNewGuild = findViewById(R.id.btnCreateNewGuild);
        newGuildName = findViewById(R.id.txtGuildNameInput);

        createNewGuild.setOnClickListener(newGuildClick());
    }

    View.OnClickListener newGuildClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VolleyBall.getResponseJson(getBaseContext(), new VolleyBall.VolleyCallback<JSONObject>() {
                    @Override
                    public void doThings(JSONObject result) {
                        System.out.println(result.toString());
                        try {
                            if(result.getInt("result") == 1){
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "GuildName already taken", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            System.out.println(e.toString());
                        }
                    }
                }, ConstantURL.URL_GUILDCREATENEW + "guildName=\"" + newGuildName.getText().toString() + "\"&userID=" + MainActivity.mainUser.getUserID());
                Log.i("addmemberurl",ConstantURL.URL_GUILDCREATENEW + "guildName=\"" + newGuildName.getText().toString() + "\"&userID=" + MainActivity.mainUser.getUserID());
            }
        };
    }
}
