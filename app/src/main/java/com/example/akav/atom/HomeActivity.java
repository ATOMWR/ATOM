package com.example.akav.atom;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");

        Intent loginInfo = getIntent();
        Uri userIdUri = loginInfo.getData();

        Toast.makeText(this, "Welcome " + userIdUri + "!", Toast.LENGTH_SHORT).show();
    }
}
