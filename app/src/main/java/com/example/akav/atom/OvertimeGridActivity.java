package com.example.akav.atom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OvertimeGridActivity extends AppCompatActivity {

    private TextView selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime_grid);

        selectedDate = (TextView) findViewById(R.id.selected_date);

        Intent intent = getIntent();

        String startDate = intent.getStringExtra("startDate");
        String endDate = intent.getStringExtra("endDate");

        selectedDate.setText(startDate + " TO " + endDate);

    }
}
