package com.example.akav.atom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AdminHomeActivity extends AppCompatActivity {

    private Button checkOvertime;
    private Button checkTravelAllowance;
    private Button verification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Intent loginInfo = getIntent();
        String userId = loginInfo.getStringExtra("userId");

        Toast.makeText(this, "Welcome Admin, " + userId, Toast.LENGTH_SHORT).show();

        checkOvertime = (Button) findViewById(R.id.admin_ot);
        checkTravelAllowance = (Button) findViewById((R.id.admin_ta));
        verification=(Button)findViewById(R.id.verify_user);

        checkOvertime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do nothing for now
            }
        });

        checkTravelAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do nothing for now.
            }
        });
        verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goto_qrcode_verification=new Intent(AdminHomeActivity.this,QRverification.class);
                startActivity(goto_qrcode_verification);
            }
        });
    }
}
