package com.example.akav.atom;

import android.app.TimePickerDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class OvertimeForm extends AppCompatActivity {
Spinner shift;

    String shiftselect;
    TextView start_time;
    TextView end_time;
    Button startbutt,endbutt,submit;
    String s1,e1,newe,news;
    int s,e,d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime_form);

        shift=(Spinner)findViewById(R.id.ShiftSpinner);
        start_time=(TextView)findViewById(R.id.starttime);
        end_time=(TextView)findViewById(R.id.endtime);


        startbutt=(Button)findViewById(R.id.startbutton);
        endbutt=(Button)findViewById(R.id.endbutton);
        submit=(Button)findViewById(R.id.submitbutt);





        TextView actual=(TextView)findViewById(R.id.actuallabel);
        actual.setPaintFlags(actual.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);//underline subheading
        TextView rosterlabel=(TextView)findViewById(R.id.roasterlabel);
        rosterlabel.setPaintFlags(actual.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);//underline subheading
        TextView desclabel=(TextView)findViewById(R.id.desclabel);
        actual.setPaintFlags(actual.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);//underline subheading




        shiftSpinner();
        startbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime1 = Calendar.getInstance();
                int starthour = mcurrentTime1.get(Calendar.HOUR_OF_DAY);
                int startminute = mcurrentTime1.get(Calendar.MINUTE);
                //s=mcurrentTime1.getTime();

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(OvertimeForm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        s1= selectedHour + ":" + selectedMinute;
                        start_time.setText(selectedHour + ":" + selectedMinute);
                    }
                }, starthour, startminute, true);//Yes 24 hour time
               // s=starthour+startminute/60;
                mTimePicker.setTitle("Actual Duty Start Time");
                mTimePicker.show();


            }
        });
        endbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime2 = Calendar.getInstance();
                 int endhour = mcurrentTime2.get(Calendar.HOUR_OF_DAY);
                 int endminute = mcurrentTime2.get(Calendar.MINUTE);
               // e=mcurrentTime2.getTime();
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(OvertimeForm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        e1=selectedHour + ":" + selectedMinute;
                        end_time.setText(selectedHour + ":" + selectedMinute);
                    }
                }, endhour, endminute, true);//Yes 24 hour time
                //e=endhour+endminute/60;
                mTimePicker.setTitle("Actual Duty End Time");
                mTimePicker.show();


            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // d=e-s;
               // d-=8;
                 news=s1.substring(0,2);
                 newe=e1.substring(0,2);
                int s=Integer.parseInt(news);
                int e=Integer.parseInt(newe);
                d=e-s-8;
                msg();

            }
        });
    }







    private void shiftSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.shift_array, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        shift.setAdapter(genderSpinnerAdapter);
        final TextView rosterdutyhours=(TextView)findViewById(R.id.rostertext);

        // Set the category String based on spinner selection
        shift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shiftselect = (String) parent.getItemAtPosition(position);
                if(shiftselect.equals("Morning"))
                    rosterdutyhours.setText("06:00 - 14:00");
                else if(shiftselect.equals("Evening"))
                    rosterdutyhours.setText("14:00 - 22:00");
                else
                    rosterdutyhours.setText("22:00 - 06:00");

            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {shiftselect= "Select Shift";
            }
        });
    }
    private void msg(){
        Toast.makeText(this, "Extra duty hours is " + d + " hours.", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Submitted", Toast.LENGTH_LONG).show();
    }

}
