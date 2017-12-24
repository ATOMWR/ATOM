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
    private String userId;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authenticateUser();
    }

    // To Authenticate User.
    private void authenticateUser() {

        mLogin = (Button) findViewById(R.id.login_button);

        if (isValidUser()) {
            mLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent goHome = new Intent(MainActivity.this, HomeActivity.class);

                    //Only for testing purpose.
                    mUserName = (EditText) findViewById(R.id.user_id);
                    userId = mUserName.getText().toString();
                    Uri userNameUri = Uri.parse(userId);
                    goHome.setData(userNameUri);

                    startActivity(goHome);
                }
            });
        }
    }

    // To Validate Enterd Credentials.
    private boolean isValidUser() {

        mUserName = (EditText) findViewById(R.id.user_id);
        mPassword = (EditText) findViewById(R.id.password);

        userId = mUserName.getText().toString();
        password = mPassword.getText().toString();

        // Validation Code

        return true;
    }
}
