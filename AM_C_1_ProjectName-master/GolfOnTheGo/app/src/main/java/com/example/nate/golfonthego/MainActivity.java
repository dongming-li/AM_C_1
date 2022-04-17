package com.example.nate.golfonthego;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nate.golfonthego.Models.Course;
import com.example.nate.golfonthego.Models.User;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    final Context context = this;
    float currVolume = 0.90f;
    private Handler handler = new Handler();
    MediaPlayer player;
    public static User mainUser;
    private ImageView backgroundImage;
    private int currImage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Random rand = new Random(System.currentTimeMillis());
        handler.postDelayed(runnable, 0);
        backgroundImage = findViewById(R.id.backgroundImageView);
        //Plays music - change music eventualy or comment to perma-off



        Intent intent = getIntent();
        mainUser = new User(intent.getStringExtra(LoginScreen.EXTRA_MESSAGE), "", intent.getIntExtra(LoginScreen.EXTRA_USERID, -1));
        mainUser.setAdmin(intent.getIntExtra(LoginScreen.EXTRA_ISADMIN, 0));

        //Plays music - change music eventualy or comment to off

        playMusic();

        Course.loadCourses(this);
    }
    /**
     * Initiates the background music on loading of MainActivity.
     * */
    public void playMusic(){
        player = MediaPlayer.create(this, R.raw.piano_background);
        player.setLooping(true);
        player.start();
        player.setVolume(0.40f, 0.40f);

        return;
    }

    /**
     * btnSettings_onClick loads the SettingsActivity over the MainActivity.
     *
     * */
    public void btnSettings_onClick(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    /**
     * guildBtn_onClick loads the guildListMain over the MainActivity.
     *
     * */
    public void guildBtn_onClick(View view){
        Intent intent = new Intent(this, guildListMain.class);
        startActivity(intent);
    }
    /**
     * coursesBtn_onClick loads the courseBuildCourseSelector over the MainActivity.
     *
     * */
    public void coursesBtn_onClick(View view){
        Intent intent = new Intent(this, courseBuildCourseSelector.class);
        startActivity(intent);
    }
    /**
     * leaderBoardBtn_onClick loads the LeaderActivity over the MainActivity.
     *
     * */
    public void leaderBoardBtn_onClick(View view){
        Intent intent = new Intent(this, LeaderActivity.class);
        startActivity(intent);
    }
    /**
     * play_onClick loads the CoursesActivity over the MainActivity.
     *
     * */
    public void play_onClick(View view){
        Intent intent = new Intent(this, CourseActivity.class);
        startActivity(intent);
    }
    /**
     * Mutes the background music.
     *
     * */
    public void volumeOff(View view) {
        //MediaPlayer mediaPlayer = null;
        player.setVolume(0.0f, 0.0f);
        currVolume = 0.0f;
    }
    /**
     * Sets the background music to low volume.
     *
     * */
    public void volumeLow(View view){
        //MediaPlayer mediaPlayer = null;
        player.setVolume(0.09f, 0.09f);
        currVolume = 0.09f;
    }
    /**
     * Sets the background music to medium volume.
     *
     * */
    public void volumeMed(View view) {
        //MediaPlayer mediaPlayer = null;
        player.setVolume(0.40f, 0.40f);
        currVolume = 0.40f;
    }
    /**
     * Sets the background music to high volume.
     *
     * */
    public void volumeHigh(View view) {
        //MediaPlayer mediaPlayer = null;
        player.setVolume(0.90f, 0.90f);
        currVolume = 0.90f;
    }
    /**
     * Randomly loads a new background on the main activity
     * @param r This is a Random object used to generate random numbers.
     *
     * */
    public void altBackground(Random r){
        final LinearLayout layout = (LinearLayout) findViewById(R.id.linearlayout);

        int nextRand = r.nextInt()%5+1;
        if(nextRand == currImage){
            nextRand ++;
        }

        currImage = nextRand;
        switch (nextRand){ //skip background 1 for now
            case 1:
                        backgroundImage.setImageResource(R.drawable.golf_background1);
                break;
            case 2:
                        backgroundImage.setImageResource(R.drawable.golf_background2);
                break;
            case 3:
                        backgroundImage.setImageResource(R.drawable.golf_background3);
                break;
            case 4:
                        backgroundImage.setImageResource(R.drawable.golf_background4);
                break;
            case 5:
                        backgroundImage.setImageResource(R.drawable.golf_background5);
                break;
        }
    }

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            Random rand = new Random(System.currentTimeMillis());
            altBackground(rand);
            handler.postDelayed(this, 5000);
        }
    };
}
