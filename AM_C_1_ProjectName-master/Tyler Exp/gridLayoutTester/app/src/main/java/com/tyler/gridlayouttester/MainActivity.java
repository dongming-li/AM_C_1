package com.tyler.gridlayouttester;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements  fragTopSection.TopSectionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //gets called by the button press in the top fragment
    @Override
    public void createMeme(String top, String bot) {
        fragBotSection bottomPictureFragment = (fragBotSection) getSupportFragmentManager().findFragmentById(R.id.fragment2);
        bottomPictureFragment.setMemeText(top,bot);
    }
}
