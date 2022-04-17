package com.example.nate.golfonthego;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nate.golfonthego.Models.Guild;
import com.example.nate.golfonthego.Models.User;
import com.example.nate.golfonthego.guildBehind.guildAdapters.guildMemberAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Constants.ConstantURL;
import VolleyAPI.VolleyBall;

public class newGuildMember extends AppCompatActivity {
    ListView searchedMembers;
    Button search;
    EditText userInput;
    int _guildID;
    ArrayList<User> users;
    ArrayAdapter<User> memberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_guild_member);
        _guildID = getIntent().getExtras().getInt("GuildID");

        setupUi();
    }

    private void setupUi(){
        searchedMembers = findViewById(R.id.searchedMembers);
        search = findViewById(R.id.btnMemberSearch);
        userInput = findViewById(R.id.newGuildMemberSearchText);
        users = new ArrayList<>();

        memberAdapter = new guildMemberAdapter(this, users);
        searchedMembers.setAdapter(memberAdapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call the server
                loadData();
            }
        });

        searchedMembers.setOnItemClickListener(userClick());
    }

    AdapterView.OnItemClickListener userClick(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            User user = (User)adapterView.getItemAtPosition(position);
            VolleyBall.getResponseJson(getBaseContext(), new VolleyBall.VolleyCallback() {
                @Override
                public void doThings(Object result) {
                    finish();
                }
            }, ConstantURL.URL_ADDGUILDMEMBER + "guildID=" + _guildID + "&userID=" + user.getUserID());
            Log.i("addmemberurl",ConstantURL.URL_ADDGUILDMEMBER + "guildID=" + _guildID + "&userID=" + user.getUserID());
            }
        };
    }

    private void loadData(){
        VolleyBall.getResponseJsonArray(this, new VolleyBall.VolleyCallback<JSONArray>() {
            @Override
            public void doThings(JSONArray result) {
                for(int i = 0; i < result.length(); i++){
                    try {
                        JSONObject obj = result.getJSONObject(i);

                        User user = new User(obj.getString("userName"), "" + obj.getInt("userId"), obj.getInt("userId"));
                        users.add(user);
                        Log.i("json array error", "added things");
                    }
                    catch (JSONException e) {
                        Log.i("json array error", e.toString());
                    }
                }

                memberAdapter.notifyDataSetChanged();

            }
        }, ConstantURL.URL_MEMBERSEARCH + "userName=" + userInput.getText().toString() + "&guildID=" + guildInfoScreen.currentGuild.get_id());
        Log.i("member search url", ConstantURL.URL_MEMBERSEARCH + "userName=" + userInput.getText().toString());
    }
}
