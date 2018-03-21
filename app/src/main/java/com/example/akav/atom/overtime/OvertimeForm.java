package com.example.akav.atom.overtime;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.akav.atom.MainActivity;
import com.example.akav.atom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Calendar;


public class OvertimeForm extends AppCompatActivity {
    Spinner shift;

    String shiftselect, userId;
    TextView start_time;
    TextView end_time, shiftvalue, roster;
    EditText descp;

    Button startbutt, endbutt, submit;
    String s1, e1, newe, news, newsm, newem, datestring;
    int s, e, dh, dm;
    String status, jsonstring, JSON_STRING;
    boolean formfillingallowed = true;
    boolean confirmed = false;
    String update = "f";

    JSONObject jo, j;
    JSONArray ja;
    String retrieveshift, retrievestrt, retrieveend, retrievedesc;


    Calendar date1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime_form);
        userId = getIntent().getExtras().getString("userID");
        datestring = getIntent().getExtras().getString("currdate");
        //Toast.makeText(OvertimeForm.this,"tags; "+getIntent().getExtras().getString("tag"), Toast.LENGTH_LONG).show();

        status = getIntent().getExtras().getString("tag");


        shift = (Spinner) findViewById(R.id.ShiftSpinner);

        start_time = (TextView) findViewById(R.id.starttime);
        end_time = (TextView) findViewById(R.id.endtime);
        roster = (TextView) findViewById(R.id.rostertext);
        shiftvalue = (TextView) findViewById(R.id.shifttext);

        startbutt = (Button) findViewById(R.id.startbutton);
        endbutt = (Button) findViewById(R.id.endbutton);
        submit = (Button) findViewById(R.id.submitbutt);
        descp = (EditText) findViewById(R.id.descid);

        if (status.equals("ot filled") || status.equals("ot notify")) {
            formfillingallowed = false;
            startbutt.setEnabled(false);
            endbutt.setEnabled(false);

            submit.setBackground(ContextCompat.getDrawable(this,R.drawable.ot_button));

            shift.setEnabled(false);
            //descp.setText(retrievedesc);
            descp.setEnabled(false);


            if (isOnline()) {
                // Start the AsyncTask
                String method = "display";
                OvertimeForm.Backgroundtask2 backgroundtask2 = new OvertimeForm.Backgroundtask2(this);
                backgroundtask2.execute(method, userId, datestring);
            } else {
                Toast.makeText(this, "NO Internet Connection, Try again", Toast.LENGTH_SHORT).show();
            }
        } else if (status.equals("ot verified")) {
            formfillingallowed = false;
            confirmed = true;
            startbutt.setEnabled(false);
            endbutt.setEnabled(false);
            submit.setEnabled(false);
            submit.setText("Already verified");
            err();

            shift.setEnabled(false);
            //descp.setText(retrievedesc);
            descp.setEnabled(false);


            if (isOnline()) {
                // Start the AsyncTask
                String method = "display";
                OvertimeForm.Backgroundtask2 backgroundtask2 = new OvertimeForm.Backgroundtask2(this);
                backgroundtask2.execute(method, userId, datestring);
            } else {
                Toast.makeText(this, "NO Internet Connection, Try again", Toast.LENGTH_SHORT).show();
            }

        }


        TextView actual = (TextView) findViewById(R.id.actuallabel);
        actual.setPaintFlags(actual.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);//underline subheading
        TextView rosterlabel = (TextView) findViewById(R.id.roasterlabel);
        rosterlabel.setPaintFlags(actual.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);//underline subheading
        TextView desclabel = (TextView) findViewById(R.id.desclabel);
        desclabel.setPaintFlags(actual.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);//underline subheading
        TextView selectsh = (TextView) findViewById(R.id.selectshift);
        selectsh.setPaintFlags(actual.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);//underline subheading


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
                        s1 = selectedHour + ":" + selectedMinute;
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
                        e1 = selectedHour + ":" + selectedMinute;
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
                if (!formfillingallowed && !confirmed) {
                    startbutt.setEnabled(true);
                    endbutt.setEnabled(true);
                    // submit.setEnabled(false);
                    submit.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.otsubmit_button));

                    shift.setEnabled(true);
                    descp.setEnabled(true);
                    formfillingallowed = true;
                    shiftvalue.setText("");
                    update = "t";

                } else if (!formfillingallowed && confirmed) {
                    err();
                } else {
                    // d=e-s;
                    // d-=8;
                    // text=(TextView)findViewById(R.id.textView);

                    news = s1.substring(0, s1.indexOf(':'));
                    newe = e1.substring(0, e1.indexOf(':'));
                    newsm = s1.substring(s1.indexOf(':') + 1);
                    newem = e1.substring(e1.indexOf(':') + 1);

                    int s = Integer.parseInt(news);
                    int e = Integer.parseInt(newe);
                    int sm = Integer.parseInt(newsm);
                    int em = Integer.parseInt(newem);
                    dh = e - s - 8;
                    if (dh < 0)
                        dh += 24;
                    dm = em - sm;
                    if (em < sm) {
                        dh--;
                        dm += 60;

                    }
                    //sample.setText(dm);
                    //msg();
                    open();

                }
            }
        });


    }

    public void err() {
        Toast.makeText(OvertimeForm.this, "Data for this date is already verified!!!", Toast.LENGTH_LONG).show();
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
        final TextView rosterdutyhours = (TextView) findViewById(R.id.rostertext);

        // Set the category String based on spinner selection
        shift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shiftselect = (String) parent.getItemAtPosition(position);
                if (shiftselect.equals("Morning"))
                    rosterdutyhours.setText("06:00 - 14:00");
                else if (shiftselect.equals("Evening"))
                    rosterdutyhours.setText("14:00 - 22:00");
                else if (shiftselect.equals("Night"))
                    rosterdutyhours.setText("22:00 - 06:00");

            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                shiftselect = "Select Shift";
            }
        });
    }

    //entry into ot table
    public void open() {
        String method = "fill";
        String datestr = datestring;
        String name = userId;
        String pfno = "101";
        String shift = shiftselect;//"morning";
        String actstart = news + ":" + newsm;
        String actend = newe + ":" + newem;
        String extra = dh + ":" + dm;
        String reason = descp.getText().toString();

        // Toast.makeText(QRverification.this, "You clicked yes button", Toast.LENGTH_LONG).show();

        if (isOnline()) {
            // Start the AsyncTask
            OvertimeForm.Backgroundtask backgroundtask = new OvertimeForm.Backgroundtask(this);
            backgroundtask.execute(method, pfno, name, shift, actstart, actend, extra, reason, datestr, update, datestring);
        } else {
            Toast.makeText(this, "NO Internet Connection, Try again", Toast.LENGTH_SHORT).show();
        }
    }


    class Backgroundtask extends AsyncTask<String, Void, String> {

        Context ctx;

        Backgroundtask(Context ctx) {
            this.ctx = ctx;

        }


        @Override
        protected String doInBackground(String... params) {
            String reg_url = "http://atomwrapp.dx.am/otfilling.php";
            String method = params[0];
            if (method.equals("fill")) {


                String pfno = params[1];
                String name = params[2];
                String shift = params[3];
                String actstart = params[4];
                String actend = params[5];
                String extra = params[6];
                String reas = params[7];
                String dat = params[8];
                String upflag = params[9];


                try {
                    URL url = new URL(reg_url);
                    HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
                    httpurlconnection.setRequestMethod("POST");

                    httpurlconnection.setDoOutput(true);
                    OutputStream OS = httpurlconnection.getOutputStream();
                    BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));

                    String newdata = URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(dat, "UTF-8") + "&" +
                            URLEncoder.encode("pfno", "UTF-8") + "=" + URLEncoder.encode(pfno, "UTF-8") + "&" +
                            URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                            URLEncoder.encode("shift", "UTF-8") + "=" + URLEncoder.encode(shift, "UTF-8") + "&" +
                            URLEncoder.encode("strt", "UTF-8") + "=" + URLEncoder.encode(actstart, "UTF-8") + "&" +
                            URLEncoder.encode("end", "UTF-8") + "=" + URLEncoder.encode(actend, "UTF-8") + "&" +
                            URLEncoder.encode("extra", "UTF-8") + "=" + URLEncoder.encode(extra, "UTF-8") + "&" +
                            URLEncoder.encode("reason", "UTF-8") + "=" + URLEncoder.encode(reas, "UTF-8") + "&" +
                            URLEncoder.encode("updateflag", "UTF-8") + "=" + URLEncoder.encode(upflag, "UTF-8") + "&" +
                            URLEncoder.encode("curdate", "UTF-8") + "=" + URLEncoder.encode(datestring, "UTF-8");

                    bufferedwriter.write(newdata);
                    // Toast.makeText(ctx, "data written", Toast.LENGTH_LONG).show();

                    bufferedwriter.flush();
                    bufferedwriter.close();
                    OS.close();
                    InputStream IS = httpurlconnection.getInputStream();
                    IS.close();


                    return "Successfully Filled OT Form";


                } catch (SocketTimeoutException s) {
                    s.printStackTrace();
                    return null;
                } catch (UnknownHostException u) {
                    u.printStackTrace();
                } catch (MalformedURLException e) {
                    Log.e(MainActivity.class.getName(), "Problem Building the URL", e);
                } catch (IOException e) {
                    Log.e(MainActivity.class.getName(), "Problem in Making HTTP request.", e);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            if (res == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "Error connecting to the Internet, Try again", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                v.setGravity(Gravity.CENTER);
                toast.show();
            } else {
                Toast.makeText(ctx, res, Toast.LENGTH_LONG).show();
            }

            // qr_result.setText(result);
        }
    }

    class Backgroundtask2 extends AsyncTask<String, Void, String> {

        Context ctx;

        Backgroundtask2(Context ctx) {
            this.ctx = ctx;

        }


        @Override
        protected String doInBackground(String... params) {
            String reg_url = "http://atomwrapp.dx.am/getot.php";
            String method = params[0];
            if (method.equals("display")) {

                String name = params[1];
                String dat = params[2];


                try {
                    URL url = new URL(reg_url);
                    HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
                    httpurlconnection.setRequestMethod("POST");

                    httpurlconnection.setDoOutput(true);
                    OutputStream OS = httpurlconnection.getOutputStream();
                    BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));

                    String newdata = URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(dat, "UTF-8") + "&" +
                            URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");


                    bufferedwriter.write(newdata);
                    // Toast.makeText(ctx, "data written", Toast.LENGTH_LONG).show();

                    bufferedwriter.flush();
                    bufferedwriter.close();
                    OS.close();


                    InputStream inputStream = httpurlconnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((JSON_STRING = bufferedReader.readLine()) != null) {

                        stringBuilder.append(JSON_STRING + "\n");
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpurlconnection.disconnect();

                    return stringBuilder.toString().trim();


                } catch (MalformedURLException e) {
                    Log.e(MainActivity.class.getName(), "Problem Building the URL", e);
                } catch (IOException e) {
                    Log.e(MainActivity.class.getName(), "Problem in Making HTTP request.", e);
                }
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String res) {
            // Toast.makeText(ctx, "ret "+res, Toast.LENGTH_LONG).show();
            // qr_result.setText(result);
            jsonstring = res;

            //json();
            // return  result;
            try {
                jo = new JSONObject(jsonstring);
                ja = jo.getJSONArray("otdataresponse");

                int i = 0;
                int count = 0;
                while (count < ja.length()) {
                    JSONObject j = ja.getJSONObject(count);
                    retrieveshift = j.getString("shift");
                    retrievestrt = j.getString("starttime");
                    retrieveend = j.getString("endtime");
                    retrievedesc = j.getString("descr");
                    count++;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            shiftvalue.setText(retrieveshift);
            start_time.setText(retrievestrt);
            end_time.setText(retrieveend);
            descp.setText(retrievedesc);
            if (retrieveshift.equals("Morning"))
                roster.setText("06:00 - 14:00");
            else if (retrieveshift.equals("Evening"))
                roster.setText("14:00 - 22:00");
            else if (retrieveshift.equals("Night"))
                roster.setText("22:00 - 06:00");


            // submit.setEnabled(false);

        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void msg() {

        //String disp=df.format(date);
        // Toast.makeText(this, disp, Toast.LENGTH_SHORT).show();
        // Toast.makeText(this, "Extra duty hours is "+dh +" : "+ dm +" hours.", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "Submitt", Toast.LENGTH_LONG).show();
    }

}
