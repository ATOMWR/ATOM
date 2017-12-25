package com.example.akav.atom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText mUserName;
    private EditText mPassword;
    private Button mLogin;
    private Button mRegister;
    private Button showpw;
    private String userId;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRegister = (Button) findViewById(R.id.goto_register_button);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoRegisterIntent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(gotoRegisterIntent);
            }
        });

        authenticateUser();

    }

    // To Authenticate User.
    private void authenticateUser() {

        mLogin = (Button) findViewById(R.id.login_button);


        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Set up Progress/Loading Bar

                if (isValidUser()) {
                    Intent gotoHomeIntent = new Intent(MainActivity.this, HomeActivity.class);

                    //Only for testing purpose.
                    mUserName = (EditText) findViewById(R.id.user_id);
                    userId = mUserName.getText().toString();
                    Uri userNameUri = Uri.parse(userId);
                    gotoHomeIntent.setData(userNameUri);

                    startActivity(gotoHomeIntent);
                } else {
                    error();
                    // Display Error
                    // Stop Progress/Loading Bar
                }
            }
        });
    }

    // To Validate Enterd Credentials.
    private void error(){
        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
    }

    private boolean isValidUser() {

        mUserName = (EditText) findViewById(R.id.user_id);
        mPassword = (EditText) findViewById(R.id.password);

        userId = mUserName.getText().toString();
        password = mPassword.getText().toString();

        // Validation Code
        if(userId.equals("username")&&password.equals("password"))
            return true;
        else
            return false;
    }
}
