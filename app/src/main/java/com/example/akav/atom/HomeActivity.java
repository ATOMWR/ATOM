package com.example.akav.atom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private Button fillOT;
    private Button fillTA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");

        Intent loginInfo = getIntent();
        final String userId = loginInfo.getStringExtra("userId");

        Toast.makeText(this, "Welcome " + userId + "!", Toast.LENGTH_SHORT).show();

        fillOT = (Button) findViewById(R.id.fill_ot);
        fillTA = (Button) findViewById(R.id.fill_ta);

        fillOT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoOT = new Intent(HomeActivity.this, OvertimeActivity.class);
                gotoOT.putExtra("userID",userId);
                startActivity(gotoOT);
            }
        });

        fillTA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoTA = new Intent(HomeActivity.this, TravelActivity.class);
                startActivity(gotoTA);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.edit_profile:
                editProfile();
                return true;

            case R.id.password_change:
                changePassword();
                return true;

            case R.id.log_out:
                logout();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        // Logout Logic

    }

    private void changePassword() {

        // Password change logic

    }

    private void editProfile() {

        // Edit Profile Logic

    }


}
