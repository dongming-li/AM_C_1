package com.example.nate.golfonthego.guildBehind;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nate.golfonthego.*;
import com.example.nate.golfonthego.Models.Guild;
import com.example.nate.golfonthego.Models.User;
import com.example.nate.golfonthego.guildBehind.guildAdapters.guildMemberAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Constants.ConstantURL;
import VolleyAPI.VolleyBall;

/**
 * Created by tyler on 10/16/2017.
 * Fragment used for the guild members screen
 */

public class fragGuildMembers extends Fragment{
    ArrayList<User> users;
    ArrayAdapter<User> memberAdapter;
    ListView memberListView;
    SwipeRefreshLayout memberRefresher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guildlist_members_fragment, container, false);

        //get the users in the guild then set the Adapter to interpret them
        users = new ArrayList<>();

        memberAdapter = new guildMemberAdapter(this.getContext(), users);
        memberListView = view.findViewById(R.id.list_Members);
        memberListView.setAdapter(memberAdapter);

        if(users.isEmpty()) {
            loadData();
        }

        memberRefresher = view.findViewById(R.id.membersRefresher);

        memberRefresher.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("member refresh", "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        users.clear();
                        loadData();
                    }
                }
        );

        memberListView.setOnItemClickListener(memberClick());

        return view;
    }

    /**
     * A click listener that handles what the list should do in the event that a user wants to add a new user
     * @return An on click listener that sends the user to the new guild member activity
     */
    AdapterView.OnItemClickListener memberClick(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(position == 0 && guildInfoScreen.currentGuild.isLeader() == 1){
                    //start intent to go to new member page
                    Intent intent = new Intent(getContext(), newGuildMember.class);
                    intent.putExtra("GuildID", guildInfoScreen.currentGuild.get_id());
                    startActivity(intent);
                }
            }
        };
    }

    /**
     * This is called in multiple places in order to load guild members from the database
     */
    private void loadData(){
        VolleyBall.getResponseJsonArray(this.getContext(), new VolleyBall.VolleyCallback<JSONArray>() {
            @Override
            public void doThings(JSONArray result) {
                Log.i("CURRENT USER IS LEADER", guildInfoScreen.currentGuild.isLeader() + "");
                if(guildInfoScreen.currentGuild.isLeader() == 1){
                    users.add(new User("+", "Add New User", -1));
                }
                for(int i = 0; i < result.length(); i++){
                    try {
                        JSONObject obj = result.getJSONObject(i);

                        User user = new User(obj.getString("userName"), obj.getString("password"), obj.getInt("userID"));
                        users.add(user);
                        Log.i("json array error", "added things");
                    }
                    catch (JSONException e) {
                        Log.i("json array error", e.toString());
                    }
                }

                memberAdapter.notifyDataSetChanged();
                memberRefresher.setRefreshing(false);
                Toast.makeText(getContext(), "Data Refreshed", Toast.LENGTH_SHORT).show();

            }
        }, ConstantURL.URL_GUILDMEMBERS + "guildName=" + "\"" + guildInfoScreen.currentGuild.get_name() +"\"");
        //Log.i("guildMembersURL", ConstantURL.URL_GUILDMEMBERS + "guildName=" + "\"" + guildInfoScreen.currentGuild.get_name() +"\"");
    }
}
