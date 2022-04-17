package com.example.nate.golfonthego;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;
import Constants.ConstantURL;
import VolleyAPI.VolleyBall;

public class LoginScreen extends AppCompatActivity {

    public EditText userName;
    public EditText password;
    public Button login;
    public Button register;
    public static final String EXTRA_MESSAGE = "userName";
    public static final String EXTRA_USERID = "userID";
    public static final String EXTRA_ISADMIN = "isAdmin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        userName = (EditText)findViewById(R.id.userName);
        password = (EditText)findViewById(R.id.password);

        login = (Button)findViewById(R.id.login);
        register = (Button)findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginRequest();
            }
        });
        register.setOnClickListener(RegisterClickDo());

        Button howtoplay = (Button)findViewById(R.id.howtoplay);
        howtoplay.setOnClickListener(howtoplayClick());

    }

    private void loginRequest(){
        String user = userName.getText().toString();
        String pass = password.getText().toString();
        String url = ConstantURL.URL_LOGIN + "userName=\"" + user + "\"&password=\"" + pass + "\"";

        if (user.equals("") || pass.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a UserName and Password", Toast.LENGTH_LONG).show();
            return;
        }

        /*
        We use VolleyBall to make calls to the server and get data
        back from the database or the server itself
         */
        VolleyBall.getResponseJson(this, new VolleyBall.VolleyCallback<JSONObject>() {
            @Override
            public void doThings(JSONObject result) {
                try{
                    //cast the object coming back to JSON
                    System.out.println(result.toString());

                    //if the string result
                    if(result.getInt("result") == 1){
                        Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                        intent.putExtra(EXTRA_MESSAGE, userName.getText().toString());
                        Log.i("userID is", "" + result.getInt("userID"));
                        intent.putExtra(EXTRA_USERID, result.getInt("userID"));
                        intent.putExtra(EXTRA_ISADMIN, result.getInt("isAdmin"));
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrect Username or password", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e){
                    System.out.println(e.toString());
                }
            }
        }, url);
    }

    View.OnClickListener RegisterClickDo() {
        return new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(LoginScreen.this, Register.class);
                startActivity(intent);
            }
        };
    }

    View.OnClickListener howtoplayClick(){
        return new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, HowToPlay.class);
                startActivity(intent);
            }
        };
    }
}
