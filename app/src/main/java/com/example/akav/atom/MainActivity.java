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
    private String userId;
    private String password;

    private Boolean isAdmin;
    private Boolean isRegularUser;

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

                getUserDetails();

                // Set up Progress/Loading Bar

                if (isValidUser()) {
                    Intent gotoUserHomeIntent = new Intent(MainActivity.this, HomeActivity.class);
                    Intent gotoAdminHomeIntent = new Intent(MainActivity.this, AdminHomeActivity.class);

                    if(isAdmin){
                        gotoAdminHomeIntent.putExtra("userId", userId);
                        startActivity(gotoAdminHomeIntent);
                    }
                    else{
                        gotoUserHomeIntent.putExtra("userName", userId);
                        startActivity(gotoUserHomeIntent);
                    }

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

        if(isAdmin(userId, password)){
            isAdmin = true;
            isRegularUser = false;
            return true;
        }
        else if(isRegularUser(userId, password)){
            isRegularUser = true;
            isAdmin = false;
            return true;
        }
        return false;
    }

    private boolean isRegularUser(String userId, String password) {
        // Validation Code
        if(userId.equals("u")&&password.equals("p")){
            return true;
        }

        return false;
    }

    private boolean isAdmin(String userId, String password) {
        // Validation Code
        if(userId.equals("a")&&password.equals("p")){
            return true;
        }

        return false;
    }

    private void getUserDetails() {

        mUserName = (EditText) findViewById(R.id.user_id);
        mPassword = (EditText) findViewById(R.id.password);

        userId = mUserName.getText().toString();
        password = mPassword.getText().toString();
    }
}
