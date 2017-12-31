package com.example.akav.atom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class OvertimeActivity extends AppCompatActivity {

    private LinearLayout previousCycle1;
    private LinearLayout previousCycle2;
    private LinearLayout previousCycle3;
    private LinearLayout currentCycle;

    private TextView prevCycle1Start;
    private TextView prevCycle1End;

    private TextView prevCycle2Start;
    private TextView prevCycle2End;

    private TextView prevCycle3Start;
    private TextView prevCycle3End;

    private TextView currentCycleStart;
    private TextView currentCycleEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime);

        previousCycle1 = (LinearLayout) findViewById(R.id.ot_previous_cycle_1);
        previousCycle2 = (LinearLayout) findViewById(R.id.ot_previous_cycle_2);
        previousCycle3 = (LinearLayout) findViewById(R.id.ot_previous_cycle_3);

        prevCycle1Start = (TextView) findViewById(R.id.ot_cycle_1_start);
        prevCycle1End = (TextView) findViewById(R.id.ot_cycle_1_end);

        prevCycle2Start = (TextView) findViewById(R.id.ot_cycle_2_start);
        prevCycle2End = (TextView) findViewById(R.id.ot_cycle_2_end);

        prevCycle3Start = (TextView) findViewById(R.id.ot_cycle_3_start);
        prevCycle3End = (TextView) findViewById(R.id.ot_cycle_3_end);

        currentCycleStart = (TextView) findViewById(R.id.ot_current_cycle_start);
        currentCycleEnd = (TextView) findViewById(R.id.ot_current_cycle_end);

        currentCycle = (LinearLayout) findViewById(R.id.ot_current_cycle);

        previousCycle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prevCycle1StartDate = prevCycle1Start.getText().toString();
                String prevCycle1EndDate = prevCycle1End.getText().toString();

                Intent prevCycle1ToGrid = new Intent(OvertimeActivity.this, OvertimeGridActivity.class);

                prevCycle1ToGrid.putExtra("startDate", prevCycle1StartDate);
                prevCycle1ToGrid.putExtra("endDate", prevCycle1EndDate);

                startActivity(prevCycle1ToGrid);
            }
        });

        previousCycle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prevCycle2StartDate = prevCycle2Start.getText().toString();
                String prevCycle2EndDate = prevCycle2End.getText().toString();

                Intent prevCycle2ToGrid = new Intent(OvertimeActivity.this, OvertimeGridActivity.class);

                prevCycle2ToGrid.putExtra("startDate", prevCycle2StartDate);
                prevCycle2ToGrid.putExtra("endDate", prevCycle2EndDate);

                startActivity(prevCycle2ToGrid);
            }
        });

        previousCycle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prevCycle3StartDate = prevCycle3Start.getText().toString();
                String prevCycle3EndDate = prevCycle3End.getText().toString();

                Intent prevCycle3ToGrid = new Intent(OvertimeActivity.this, OvertimeGridActivity.class);

                prevCycle3ToGrid.putExtra("startDate", prevCycle3StartDate);
                prevCycle3ToGrid.putExtra("endDate", prevCycle3EndDate);

                startActivity(prevCycle3ToGrid);
            }
        });

        currentCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentCycleStartDate = currentCycleStart.getText().toString();
                String currentCycleEndDate = currentCycleEnd.getText().toString();

                Intent currentCycleToGrid = new Intent(OvertimeActivity.this, OvertimeGridActivity.class);

                currentCycleToGrid.putExtra("startDate", currentCycleStartDate);
                currentCycleToGrid.putExtra("endDate", currentCycleEndDate);

                startActivity(currentCycleToGrid);
            }
        });

    }
}
