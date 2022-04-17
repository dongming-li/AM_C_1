package com.example.nate.golfonthego;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.nate.golfonthego.Models.Guild;
import com.example.nate.golfonthego.guildBehind.*;
import com.example.nate.golfonthego.guildBehind.guildAdapters.GuildInfoPageAdapter;

public class guildInfoScreen extends AppCompatActivity {

    private GuildInfoPageAdapter _guildInfoPageAdapter;

    private ViewPager _guildViewPager;

    public static Guild currentGuild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guild_info_screen);

        //here we set the title of toolbar to the guild name that was put inside the intent
        Toolbar infoToolbar = (Toolbar) findViewById(R.id.guildInfoToolbar);
        setSupportActionBar(infoToolbar);
        Intent i = getIntent();
        getSupportActionBar().setTitle(i.getStringExtra(guildListMain.tag_guild_name));
        currentGuild = new Guild(i.getStringExtra(guildListMain.tag_guild_name), i.getIntExtra(guildListMain.tag_guild_id, 0),i.getIntExtra(guildListMain.tag_guild_leader, 0));

        _guildInfoPageAdapter = new GuildInfoPageAdapter(getSupportFragmentManager());

        //set up the view pager with the sections adapter
        _guildViewPager = (ViewPager) findViewById(R.id.guildInfoContainer);
        setupViewPager(_guildViewPager);

        //setup the tab layout object
        TabLayout guildTabs = (TabLayout) findViewById(R.id.guildInfoTabs);
        guildTabs.setupWithViewPager(_guildViewPager);

        _guildViewPager.setOffscreenPageLimit(3);

    }

    private void setupViewPager(ViewPager viewPager){
        GuildInfoPageAdapter pageAdapter = new GuildInfoPageAdapter(getSupportFragmentManager());
        pageAdapter.addFragment(new fragGuildMembers(), "Members");
        pageAdapter.addFragment(new fragGuildScores(), "Scores");
        pageAdapter.addFragment(new fragGuildEvents(), "Events");
        viewPager.setAdapter(pageAdapter);
    }

}
