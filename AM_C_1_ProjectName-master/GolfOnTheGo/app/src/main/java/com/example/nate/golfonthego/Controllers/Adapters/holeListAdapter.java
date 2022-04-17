package com.example.nate.golfonthego.Controllers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nate.golfonthego.Models.Hole;
import com.example.nate.golfonthego.R;

import java.util.ArrayList;

/**
 * Created by resh_tkranig on 11/22/2017.
 */

public class holeListAdapter extends ArrayAdapter<Hole> {

    public holeListAdapter(Context context, ArrayList<Hole> holes) {
        super(context, R.layout.custom_row_guild, holes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater guildInflater = LayoutInflater.from(getContext());
        View customView = guildInflater.inflate(R.layout.custom_row_guild, parent, false);

        Hole hole = getItem(position);
        TextView guildText = customView.findViewById(R.id.txtGuildName);

        assert hole != null;
        guildText.setText("Hole: " + position);

        return customView;
    }
}
