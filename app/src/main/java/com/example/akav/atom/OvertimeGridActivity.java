package com.example.akav.atom;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OvertimeGridActivity extends AppCompatActivity {

    private TextView selectedDate;
    String st,JSON_STRING,jsonstring,userID;
    String startDate,endDate;
    String[] s=new String[14];

    JSONObject jo;
    int ress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime_grid);
        final String uid = getIntent().getExtras().getString("userID");

        userID=uid;

        selectedDate = (TextView) findViewById(R.id.selected_date);

        Intent intent = getIntent();

         startDate = intent.getStringExtra("startDate");
        endDate = intent.getStringExtra("endDate");



        selectedDate.setText(startDate + " TO " + endDate);




        Calendar c=Calendar.getInstance();
        Calendar d=Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        try {
            c.setTime(sdf.parse(startDate));
            d.setTime(sdf.parse(endDate));
            int i=0;
            String temp;

            while(!c.equals(d)){

                temp=sdf.format(c.getTime());
                s[i]=temp.substring(0,10);
                i++;
                c.add(Calendar.DATE,1);

            }
            temp=sdf.format(c.getTime());
            s[i]=temp.substring(0,10);


        } catch (ParseException e) {
            e.printStackTrace();
        }












        Backgroundtask backgroundtask = new Backgroundtask();
        backgroundtask.execute();


    }
        public void gridviewcall(){
        /*String[] s=new String[14];
        for(int i=0;i<s.length;i++){
            s[i]=" "+(i+1)+" ";
        }*/
        GridView gridView=(GridView)findViewById(R.id.gridview);
        gridView.setAdapter(new TextViewAdapter(this, s,ress));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                st=parent.getItemAtPosition(position).toString();
                msg();
               // Intent gotoOTFormIntent = new Intent(OvertimeGridActivity.this, OvertimeForm.class);
               // gotoOTFormIntent.putExtra("userID",userID);
               // startActivity(gotoOTFormIntent);
            }
        });





        //gridview logic


    }
    private void msg(){
        Toast.makeText(OvertimeGridActivity.this, st, Toast.LENGTH_SHORT).show();

    }





    class Backgroundtask extends AsyncTask<Void, Void, String> {
        String json_url;

        @Override
        protected void onPreExecute() {
            json_url = "http://atomwrapp.dx.am/temp.php";
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_STRING = bufferedReader.readLine()) != null) {

                    stringBuilder.append(JSON_STRING + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(OvertimeGridActivity.this, result, Toast.LENGTH_SHORT).show();
            jsonstring = result;
            //json();
            // return  result;
            try {
                jo = new JSONObject(jsonstring);
                ress = jo.getInt("data");
                msg();
                gridviewcall();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }




}
