package com.example.akav.atom.overtime;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OvertimeGridActivity extends AppCompatActivity {

    private TextView selectedDate;
    String datestring,JSON_STRING,jsonstring,userID;
    String startDate,endDate;
    String[] s=new String[14];
    String[] stringdatearray;
    int[] inter_verification_status;
    String t;
    String fromcurrorprevcycle;


    JSONObject jo;
    JSONArray ja;
    int ress;

    RelativeLayout loadinglayout,actuallayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime_grid);
        final String uid = getIntent().getExtras().getString("userID");
        fromcurrorprevcycle=getIntent().getExtras().getString("fromcyclelist");
        loadinglayout=(RelativeLayout)findViewById(R.id.load_layout);
        actuallayout=(RelativeLayout)findViewById(R.id.actual_layout);

        userID=uid;

        selectedDate = (TextView) findViewById(R.id.selected_date);

        Intent intent = getIntent();

         startDate = intent.getStringExtra("startDate");
        endDate = intent.getStringExtra("endDate");



        selectedDate.setText(startDate + " TO " + endDate);




        Calendar c=Calendar.getInstance();
        Calendar d=Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
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
        final GridView gridView=(GridView)findViewById(R.id.gridview);
        gridView.setAdapter(new TextViewAdapter(this, s,stringdatearray,inter_verification_status));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                datestring=parent.getItemAtPosition(position).toString();
                t=view.getTag().toString();
                if(fromcurrorprevcycle.equals("cannotfill")&&!t.equals("ot notify")){
                    cannotfill();
                }

               else {
                    Intent gotoOTFormIntent = new Intent(OvertimeGridActivity.this, OvertimeForm.class);
                    gotoOTFormIntent.putExtra("userID", userID);
                    gotoOTFormIntent.putExtra("currdate", datestring);
                    gotoOTFormIntent.putExtra("tag", t);

                    //msg();

                    startActivity(gotoOTFormIntent);
                }
            }
        });





        //gridview logic


    }
    private void msg(){
        Toast.makeText(OvertimeGridActivity.this, "tag is:"+t, Toast.LENGTH_SHORT).show();

    }
    private void cannotfill(){
        Toast.makeText(OvertimeGridActivity.this, "Sorry,you cannot fill form as it is under verification.", Toast.LENGTH_SHORT).show();
    }





    class Backgroundtask extends AsyncTask<Void, Void, String> {
        String json_url;

        @Override
        protected void onPreExecute() {
            json_url = "http://atomwrapp.dx.am/colorcoding.php";
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                URL url = new URL(json_url);
                HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
                httpurlconnection.setRequestMethod("POST");

                httpurlconnection.setDoOutput(true);
                OutputStream OS = httpurlconnection.getOutputStream();
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));

                String newdata = URLEncoder.encode("stdate", "UTF-8") + "=" + URLEncoder.encode(startDate, "UTF-8") + "&" +
                        URLEncoder.encode("eddate", "UTF-8") + "=" + URLEncoder.encode(endDate, "UTF-8")+ "&" +
                        URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(userID, "UTF-8") ;


                bufferedwriter.write(newdata);
                // Toast.makeText(ctx, "data written", Toast.LENGTH_LONG).show();

                bufferedwriter.flush();
                bufferedwriter.close();
                //httpurlconnection.disconnect();
                OS.close();

                //HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
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
           // Toast.makeText(OvertimeGridActivity.this, "json string =>"+result, Toast.LENGTH_SHORT).show();
            jsonstring = result;
            //json();
            // return  result;
            try {
                jo = new JSONObject(jsonstring);
                ja=jo.getJSONArray("dateresponse");

                int i=0;
                int count=0;
                if(ja.length()!=0) {
                    stringdatearray=new String[ja.length()];
                    inter_verification_status=new int[ja.length()];
                    while (count < ja.length()) {
                        JSONObject j = ja.getJSONObject(count);
                        stringdatearray[i] = j.getString("date");
                        inter_verification_status[i] = j.getInt("inter_verification");
                        i++;
                        count++;

                    }
                }else{
                    stringdatearray=new String[1];
                    inter_verification_status=new int[1];
                    stringdatearray[i] = "nodate";
                    inter_verification_status[i] =999;
                }


               // ress = jo.getInt("data");
              //  msg();
                loadinglayout.setVisibility(View.GONE);
                actuallayout.setVisibility(View.VISIBLE);
                gridviewcall();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }




}
