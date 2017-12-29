package com.example.akav.atom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class OvertimeActivity extends AppCompatActivity {

    private DatePicker datePickerOT;

    private Button goBack;
    private Button selectDate;

    private TextView dateTextView;

    private RelativeLayout datePickerLayout;
    private RelativeLayout fillOtLayout;

    private Integer dayOfMonth;
    private Integer monthNumber;
    private Integer year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime);

        datePickerOT = (DatePicker) findViewById(R.id.date_Picker_OT);
        goBack = (Button) findViewById(R.id.go_back_from_ot);
        selectDate = (Button) findViewById(R.id.select_date_from_ot);
        datePickerLayout = (RelativeLayout) findViewById(R.id.date_picker_layout);
        fillOtLayout = (RelativeLayout) findViewById(R.id.ot_form_layout);
        dateTextView = (TextView) findViewById(R.id.date_text_view);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goto_home = new Intent(OvertimeActivity.this, HomeActivity.class);
                startActivity(goto_home);
                finish();
            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dayOfMonth = datePickerOT.getDayOfMonth();

                // Add 1 because month number starts off with 0;
                monthNumber = datePickerOT.getMonth() + 1;

                year = datePickerOT.getYear();

                datePickerLayout.setVisibility(View.GONE);

                // Convert into convenient form later.
                // this is only for Testing Purpose.
                dateTextView.setText("Selected Date : " + dayOfMonth + "/" + monthNumber + "/" + year);

                fillOtLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}
