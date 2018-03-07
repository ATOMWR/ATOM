package com.example.akav.atom;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akav.atom.overtime.OvertimeActivity;
import com.example.akav.atom.travel.TravelActivity;

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
import java.net.URL;
import java.net.URLEncoder;

public class HomeActivity extends AppCompatActivity {

    private Button fillOT;
    private Button fillTA;
    String JSON_STRING,jsonstring,userId;
    TextView temp;
    JSONObject jo;
    JSONArray ja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("home");

        Intent loginInfo = getIntent();
        userId = loginInfo.getStringExtra("userId");

        Toast.makeText(this, "Welcome " + userId + "!", Toast.LENGTH_SHORT).show();

        String method = "display";
        Backgroundtaskonload backgroundtask2 = new Backgroundtaskonload(this);
        backgroundtask2.execute(method, userId);

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
                gotoTA.putExtra("userID",userId);
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
        Intent gotoNotification = new Intent(HomeActivity.this, Notification.class);
        gotoNotification.putExtra("userID",userId);
        startActivity(gotoNotification);

    }

    private void changePassword() {

        // Password change logic

    }

    private void editProfile() {

        // Edit Profile Logic

    }
    class Backgroundtaskafterclick extends AsyncTask<String,Void,String> {

        Context ctx;

        Backgroundtaskafterclick(Context ctx) {
            this.ctx = ctx;

        }



        @Override
        protected String doInBackground(String... params) {
            String reg_url = "http://atomwrapp.dx.am/otfilling.php";
            String method = params[0];
            if (method.equals("fill")) {




                String  pfno= params[1];
                String name = params[2];
                String shift=params[3];
                String actstart=params[4];
                String actend=params[5];
                String extra=params[6];
                String reas=params[7];
                String dat=params[8];
                String upflag=params[9];



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
                            URLEncoder.encode("shift", "UTF-8") + "=" + URLEncoder.encode(shift, "UTF-8") +"&" +
                            URLEncoder.encode("strt", "UTF-8") + "=" + URLEncoder.encode(actstart, "UTF-8") + "&" +
                            URLEncoder.encode("end", "UTF-8") + "=" + URLEncoder.encode(actend, "UTF-8") + "&" +
                            URLEncoder.encode("extra", "UTF-8") + "=" + URLEncoder.encode(extra, "UTF-8") + "&" +
                            URLEncoder.encode("reason", "UTF-8") + "=" + URLEncoder.encode(reas, "UTF-8") + "&" +
                            URLEncoder.encode("updateflag", "UTF-8") + "=" + URLEncoder.encode(upflag, "UTF-8") ;


                    bufferedwriter.write(newdata);
                    // Toast.makeText(ctx, "data written", Toast.LENGTH_LONG).show();

                    bufferedwriter.flush();
                    bufferedwriter.close();
                    OS.close();
                    InputStream IS = httpurlconnection.getInputStream();
                    IS.close();



                    return "successfull filled";


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
            Toast.makeText(ctx, res, Toast.LENGTH_LONG).show();
            // qr_result.setText(result);
        }
    }
    class Backgroundtaskonload extends AsyncTask<String,Void,String> {

        Context ctx;

        Backgroundtaskonload(Context ctx) {
            this.ctx = ctx;

        }


        @Override
        protected String doInBackground(String... params) {
            String reg_url = "http://atomwrapp.dx.am/notificationload.php";
            String method = params[0];
            if (method.equals("display")) {

                String name = params[1];
                // String dat=params[2];


                try {
                    URL url = new URL(reg_url);
                    HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
                    httpurlconnection.setRequestMethod("POST");

                    httpurlconnection.setDoOutput(true);
                    OutputStream OS = httpurlconnection.getOutputStream();
                    BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));

                    String newdata = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");


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
            Toast.makeText(ctx, "ret " + res, Toast.LENGTH_LONG).show();
            jsonstring = res;
            temp = (TextView) findViewById(R.id.tv);
           // temp.setText(res);
            try {
                jo = new JSONObject(jsonstring);
                ja = jo.getJSONArray("countresponse");

                int i = 0;
                int h=0;
                int count = 0;
                while (count < ja.length()) {
                    JSONObject j = ja.getJSONObject(count);
                    h=j.getInt("count");

                    count++;
                }
                String q= String.valueOf(h);
                temp.setText(q);


                // qr_result.setText(result);
                //jsonstring = res;

                //json();
                // return  result;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
