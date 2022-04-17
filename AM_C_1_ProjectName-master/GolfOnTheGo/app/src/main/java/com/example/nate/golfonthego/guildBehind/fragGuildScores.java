package com.example.nate.golfonthego.guildBehind;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.nate.golfonthego.Models.Score;
import com.example.nate.golfonthego.R;
import com.example.nate.golfonthego.guildBehind.guildAdapters.guildScoresAdapter;

/**
 * Created by tyler on 10/16/2017.
 * Fragment used for the guild members screen
 */

public class fragGuildScores extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guildlist_scores_fragment, container, false);

        //get the user scores for a guild and the courses the scores were played on
        Score[] scores = {new Score("ElectricBoogaloo"), new Score("ArnoldPalmer'sCourse"), new Score("Back9forFront9")};

        //this is temporary code used to make fake values
        scores[0].AddScore("nate", -10);
        scores[0].AddScore("tyler", -9);
        scores[0].AddScore("xXxGolfSniper", -8);
        scores[1].AddScore("literalhacker", 20);
        scores[1].AddScore("shaq", 25);
        scores[1].AddScore("thatguy", 30);
        scores[2].AddScore("russianhacker", -200);
        scores[2].AddScore("otherhacker", 10);
        scores[2].AddScore("tom", 20);

        //set the adapter for the list view using the scores object we made
        ListAdapter scoreAdapter = new guildScoresAdapter(this.getContext(), scores);
        ListView scoreListView = view.findViewById(R.id.list_guild_scores);
        scoreListView.setAdapter(scoreAdapter);

        return view;
    }
}
