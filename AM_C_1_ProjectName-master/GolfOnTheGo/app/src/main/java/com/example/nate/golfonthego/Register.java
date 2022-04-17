package com.example.nate.golfonthego;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;
import Constants.ConstantURL;
import VolleyAPI.RequestQueueSingleton;
import VolleyAPI.VolleyBall;

public class Register extends AppCompatActivity {

    public EditText userName;
    public EditText password;
    public EditText confirm;
    public Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = (EditText)findViewById(R.id.regUserName);
        password = (EditText)findViewById(R.id.regPass);
        confirm = (EditText)findViewById(R.id.regPassConf);
        register = (Button)findViewById(R.id.regRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerRequest();
            }
        });
    }

    private void registerRequest(){
        String user = userName.getText().toString();
        String pass = password.getText().toString();
        String conf = confirm.getText().toString();

        if (user.equals("") || pass.equals("") || conf.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a Username and Confirm your password", Toast.LENGTH_LONG).show();
            return;
        } else if (!pass.equals(conf)){
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }

        String url = ConstantURL.URL_REGISTER + "userName=\"" + user + "\"&password=\"" + pass + "\"";

        VolleyBall.getResponseJson(this, new VolleyBall.VolleyCallback() {
            @Override
            public void doThings(Object result) {
                try{
                    JSONObject response = (JSONObject)result;
                    System.out.println(response.toString());
                    if(response.getInt("result") == 1){
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Username already taken", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e){
                    System.out.println(e.toString());
                }
            }
        }, url);
    }
}
