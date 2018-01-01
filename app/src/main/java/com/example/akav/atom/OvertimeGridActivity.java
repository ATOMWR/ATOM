package com.example.akav.atom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class OvertimeGridActivity extends AppCompatActivity {

    private TextView selectedDate;
    String st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime_grid);

        selectedDate = (TextView) findViewById(R.id.selected_date);

        Intent intent = getIntent();

        String startDate = intent.getStringExtra("startDate");
        String endDate = intent.getStringExtra("endDate");


        selectedDate.setText(startDate + " TO " + endDate);

        //gridview logic
        String[] s=new String[28];
        for(int i=0;i<s.length;i++){
            s[i]=" "+(i+1)+" ";
        }
        GridView gridView=(GridView)findViewById(R.id.gridview);
        gridView.setAdapter(new TextViewAdapter(this, s));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                 st=parent.getItemAtPosition(position).toString();
                msg();
            }
        });



    }
    private void msg(){
        Toast.makeText(this, st+"date is selected", Toast.LENGTH_SHORT).show();
    }
}
