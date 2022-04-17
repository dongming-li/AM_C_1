package com.example.nate.golfonthego.guildBehind.guildAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nate.golfonthego.Models.Score;
import com.example.nate.golfonthego.R;

/**
 * Created by tyler on 10/22/2017.
 * Used to display scores in the guild info scores page
 */

public class guildScoresAdapter extends ArrayAdapter<Score>{
    public guildScoresAdapter(Context context, Score[] scores) {
        super(context, R.layout.custom_row_guildscore, scores);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater guildInflater = LayoutInflater.from(getContext());
        View customView = guildInflater.inflate(R.layout.custom_row_guildscore, parent, false);

        //first load in the score
        Score score = getItem(position);

        //second find all the views in the table inside the fragment
        TextView courseName = customView.findViewById(R.id.txtGuildScoreCourse);
        TextView score1User = customView.findViewById(R.id.txtGuildScoreUser1);
        TextView score1 = customView.findViewById(R.id.txtGuildScore1);
        TextView score2User = customView.findViewById(R.id.txtGuildScoreUser2);
        TextView score2 = customView.findViewById(R.id.txtGuildScore2);
        TextView score3User = customView.findViewById(R.id.txtGuildScoreUser3);
        TextView score3 = customView.findViewById(R.id.txtGuildScore3);

        //make sure the score exists
        assert score != null;

        //set the text of each box to the correlating score
        courseName.setText(score.courseName);

        score1User.setText(score.getUser(0));
        score1.setText("" + score.getScore(0));

        score2User.setText(score.getUser(1));
        score2.setText("" + score.getScore(1));

        score3User.setText(score.getUser(2));
        score3.setText("" + score.getScore(2));

        return customView;
    }
}
