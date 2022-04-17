package com.example.tyler.uitest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        View theView = findViewById(R.id.UserName);
        View root = theView.getRootView();
        root.setBackgroundColor(Color.BLACK);

        Intent sender = getIntent();
        String message = sender.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView userMessage = (TextView)findViewById(R.id.UserName);
        userMessage.setText("Hello " + message + "!");
        userMessage.setTextColor(Color.WHITE);
    }
}
