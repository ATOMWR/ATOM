package com.example.akav.atom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AdminNotification extends AppCompatActivity {
//TODO:display cycle in which changes is to be done i.e. isVerified=2 in cyclelist table
//TODO:on cycle list click,display users whose correction is needed by hq i.e. hq_verification =2 in ot_table and ta_table
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notification);
    }
}
