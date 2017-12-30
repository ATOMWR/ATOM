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

    private RelativeLayout cycleListLayout;
    private RelativeLayout previousCycleListLayout;
    private LinearLayout previousCycle1;
    private LinearLayout previousCycle2;
    private LinearLayout previousCycle3;
    private RelativeLayout currentCycleLayout;
    private LinearLayout currentCycle;

    private RelativeLayout previousCycleGridLayout;

    private RelativeLayout currentCycleGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime);

        cycleListLayout = (RelativeLayout) findViewById(R.id.cycle_list_layout);
        previousCycle1 = (LinearLayout) findViewById(R.id.ot_previous_cycle_1);
        previousCycle2 = (LinearLayout) findViewById(R.id.ot_previous_cycle_2);
        previousCycle3 = (LinearLayout) findViewById(R.id.ot_previous_cycle_3);

        currentCycle = (LinearLayout) findViewById(R.id.ot_current_cycle);

        previousCycleGridLayout = (RelativeLayout) findViewById(R.id.previous_cycle_grid_view);
        currentCycleGridLayout = (RelativeLayout) findViewById(R.id.current_cycle_grid_view);

        previousCycleGridLayout.setVisibility(View.GONE);
        currentCycleGridLayout.setVisibility(View.GONE);

        previousCycle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cycleListLayout.setVisibility(View.GONE);
                currentCycleGridLayout.setVisibility(View.GONE);
                previousCycleGridLayout.setVisibility(View.VISIBLE);
            }
        });

        previousCycle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cycleListLayout.setVisibility(View.GONE);
                currentCycleGridLayout.setVisibility(View.GONE);
                previousCycleGridLayout.setVisibility(View.VISIBLE);
            }
        });

        previousCycle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cycleListLayout.setVisibility(View.GONE);
                currentCycleGridLayout.setVisibility(View.GONE);
                previousCycleGridLayout.setVisibility(View.VISIBLE);
            }
        });

        currentCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cycleListLayout.setVisibility(View.GONE);
                previousCycleGridLayout.setVisibility(View.GONE);
                currentCycleGridLayout.setVisibility(View.VISIBLE);
            }
        });

    }
}
