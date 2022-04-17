package com.example.tyler.uitest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Button;
import android.widget.EditText;
import android.graphics.Color;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import android.content.res.Resources;
import android.util.TypedValue;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "userName";
    Connection conn = null;
    ResultSet rs;
    Statement stmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout layout = new RelativeLayout(this);
        layout.setBackgroundColor(Color.DKGRAY);

        //Add the username field
        EditText userName = new EditText(this);
        userName.setGravity(Gravity.CENTER);
        userName.setHint(R.string.userName);
        userName.setId(R.id.userName);

        //Add the password field
        EditText password = new EditText(this);
        password.setGravity(Gravity.CENTER);
        password.setHint(R.string.password);
        password.setTransformationMethod(new PasswordTransformationMethod());
        password.setId(R.id.password);

        //Add the login button
        Button login = new Button(this);
        login.setText(R.string.login);
        login.setBackgroundColor(Color.BLACK);
        login.setTextColor(Color.WHITE);
        login.setId(R.id.loginButton);
        login.setOnClickListener(LoginClickDo(login));

        RelativeLayout.LayoutParams buttonContainer = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams userNameContainer = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams passwordContainer = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        //give rules to containers
        buttonContainer.addRule(RelativeLayout.CENTER_HORIZONTAL);
        buttonContainer.addRule(RelativeLayout.CENTER_VERTICAL);

        userNameContainer.addRule(RelativeLayout.ABOVE, password.getId());
        userNameContainer.addRule(RelativeLayout.CENTER_HORIZONTAL);
        userNameContainer.setMargins(0, 0, 0, 50);

        passwordContainer.addRule(RelativeLayout.ABOVE, login.getId());
        passwordContainer.addRule(RelativeLayout.CENTER_HORIZONTAL);
        passwordContainer.setMargins(0, 0, 0, 50);

        //setting the same size for the inputs on multiple devices
        Resources r = getResources();
        int adamSandlerFilm = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200,
                    r.getDisplayMetrics()
                );

        userName.setWidth(adamSandlerFilm);
        password.setWidth(adamSandlerFilm);

        //add to layout
        layout.addView(login, buttonContainer);
        layout.addView(password, passwordContainer);
        layout.addView(userName, userNameContainer);

        try{
            conn = DriverManager.getConnection("mysql.cs.iastate.edu", "dbu309amc1", "XFsBvb1t");
        }
        catch(SQLException e){
            throw new IllegalStateException("Cannot connect the database!", e);
        }

        try{
            stmt = conn.createStatement();
            rs =  stmt.executeQuery("Select * from User");

            while(rs.next()){
                login.setText(rs.getString("userName"));
            }
        }
        catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignore

                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) { } // ignore

                stmt = null;
            }
        }

        setContentView(layout);
    }

    View.OnClickListener LoginClickDo(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v){
                EditText userName = (EditText)findViewById(R.id.userName);
                Intent intent = new Intent(MainActivity.this, MainScreen.class);
                String message = userName.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        };
    }
}
