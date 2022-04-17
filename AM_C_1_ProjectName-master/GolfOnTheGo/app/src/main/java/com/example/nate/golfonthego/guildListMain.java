package com.example.nate.golfonthego;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nate.golfonthego.Models.Guild;
import com.example.nate.golfonthego.guildBehind.guildAdapters.guildListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Constants.ConstantURL;
import VolleyAPI.VolleyBall;

public class guildListMain extends AppCompatActivity {
    public static String tag_guild_name = "guildName";
    public static String tag_guild_id = "guildID";
    public static String tag_guild_leader = "guildLeader";
    ArrayList<Guild> guilds;
    ArrayAdapter<Guild> guildAdapter;
    ListView guildListView;
    SwipeRefreshLayout guildsListRefresher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Clear out the title bar at the top of the page
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guild_list_main);

        //Generate some fake guilds to use for testing, in the future this will be called from the network
        guilds = new ArrayList<>();

        //Create a new adapter using the guilds we made above
        guildAdapter = new guildListAdapter(this, guilds);
        guildListView = findViewById(R.id.listGuilds);

        //set the adapter to the guild adapter we made
        guildListView.setAdapter(guildAdapter);

        //load data from the server
        loadData();

        //When a user clicks a guild we send them to the guild info screen for that guild
        guildListView.setOnItemClickListener(guildClick());

        guildsListRefresher = findViewById(R.id.guildListRefresher);

        guildsListRefresher.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("member refresh", "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        guilds.clear();
                        loadData();
                    }
                }
        );
    }

    private AdapterView.OnItemClickListener guildClick(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(position == 0){
                    //start intent to go to new guild page
                    Intent intent = new Intent(guildListMain.this, NewGuild.class);
                    startActivity(intent);
                }
                else{
                    Guild guild = (Guild)adapterView.getItemAtPosition(position);
                    Intent intent = new Intent(guildListMain.this, guildInfoScreen.class);
                    System.out.println(guild.get_name());

                    //making sure that the guild name gets included
                    intent.putExtra(tag_guild_name, guild.get_name());
                    intent.putExtra(tag_guild_id, guild.get_id());
                    intent.putExtra(tag_guild_leader, guild.isLeader());
                    startActivity(intent);
                }
            }
        };
    }

    private void loadData(){
        VolleyBall.getResponseJsonArray(this, new VolleyBall.VolleyCallback<JSONArray>() {
            @Override
            public void doThings(JSONArray result) {
                guilds.add(new Guild("Add New Guild", -1, 0));
                for(int i = 0; i < result.length(); i++){
                    try {
                        JSONObject obj = result.getJSONObject(i);

                        Guild guild = new Guild(obj.getString("guildName"), obj.getInt("guildID"), obj.getInt("leader"));
                        guilds.add(guild);
                        Log.i("json array error", "added things");
                    }
                    catch (JSONException e) {
                        Log.i("json array error", e.toString());
                    }
                }
                guildsListRefresher.setRefreshing(false);
                guildAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Data Refreshed", Toast.LENGTH_SHORT).show();
            }
        }, ConstantURL.URL_GUILDLIST + "userName=" +
                "\"" + MainActivity.mainUser.getName() +"\"");
        Log.i("volley do things url",ConstantURL.URL_GUILDLIST + "userName=\"" + MainActivity.mainUser.getName() +"\"");
    }
}
