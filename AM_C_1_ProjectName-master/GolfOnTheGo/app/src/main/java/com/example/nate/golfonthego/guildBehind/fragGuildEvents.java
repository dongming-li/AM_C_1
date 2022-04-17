package com.example.nate.golfonthego.guildBehind;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.nate.golfonthego.Models.Event;
import com.example.nate.golfonthego.Models.User;
import com.example.nate.golfonthego.R;
import com.example.nate.golfonthego.guildBehind.guildAdapters.guildEventsAdapter;
import com.example.nate.golfonthego.guildInfoScreen;

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

public class fragGuildEvents extends Fragment{
    ArrayList<Event> events;
    ArrayAdapter<Event> eventArrayAdapter;
    ListView eventListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guildlist_events_fragment, container, false);

        //get the users in the guild then set the Adapter to interpret them
        events = new ArrayList<>();
        eventArrayAdapter = new guildEventsAdapter(this.getContext(), events);
        eventListView = view.findViewById(R.id.list_guild_events);
        eventListView.setAdapter(eventArrayAdapter);

        loadData();


        return view;
    }

    private void loadData(){
        VolleyBall.getResponseJsonArray(this.getContext(), new VolleyBall.VolleyCallback<JSONArray>() {
            @Override
            public void doThings(JSONArray result) {
                for(int i = 0; i < result.length(); i++){
                    try {
                        JSONObject obj = result.getJSONObject(i);

                        Event event = new Event(obj.getString("EventName"), obj.getString("Date"), "");
                        events.add(event);
                        Log.i("json array error", "added things");
                    }
                    catch (JSONException e) {
                        Log.i("json array error", e.toString());
                    }

                    eventArrayAdapter.notifyDataSetChanged();
                }

            }
        }, ConstantURL.URL_GUILDEVENTS + "guildName=" + "\"" + guildInfoScreen.currentGuild.get_name() +"\"");
        Log.i("guildMembersURL", ConstantURL.URL_GUILDEVENTS + "guildName=" + "\"" + guildInfoScreen.currentGuild.get_name() +"\"");
    }
}
