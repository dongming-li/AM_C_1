package com.example.nate.golfonthego.guildBehind.guildAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nate.golfonthego.Models.Event;
import com.example.nate.golfonthego.R;

import java.util.ArrayList;

/**
 * Created by tyler on 10/23/17.
 * Used to create a list and set the guild events inside it
 */

public class guildEventsAdapter extends ArrayAdapter<Event> {
    public guildEventsAdapter(Context context, ArrayList<Event> users) {
        super(context, R.layout.custom_row_guild_event, users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater guildInflater = LayoutInflater.from(getContext());
        View customView = guildInflater.inflate(R.layout.custom_row_guild_event, parent, false);

        Event event = getItem(position);
        TextView eventName = customView.findViewById(R.id.list_eventcoursename);
        TextView date = customView.findViewById(R.id.list_eventdate);

        assert event != null;
        eventName.setText(event.courseName + " - " + event.eventName);
        date.setText(event.date);

        return customView;
    }
}
