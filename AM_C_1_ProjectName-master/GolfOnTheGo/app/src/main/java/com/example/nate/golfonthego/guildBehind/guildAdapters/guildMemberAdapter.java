package com.example.nate.golfonthego.guildBehind.guildAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nate.golfonthego.Models.User;
import com.example.nate.golfonthego.R;

import java.util.ArrayList;

/**
 * Created by tyler on 10/21/2017.
 * Used to generate the list view for the guild info members fragment
 */

public class guildMemberAdapter extends ArrayAdapter<User> {
    public guildMemberAdapter(Context context, ArrayList<User> users) {
        super(context, R.layout.custom_row_guildmember, users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater guildInflater = LayoutInflater.from(getContext());
        View customView = guildInflater.inflate(R.layout.custom_row_guildmember, parent, false);

        User user = getItem(position);
        TextView name = customView.findViewById(R.id.list_memberName);
        TextView password = customView.findViewById(R.id.list_memberPassword);

        assert user != null;
        name.setText(user.getName());
        password.setText(user.getPassword());

        return customView;
    }
}
