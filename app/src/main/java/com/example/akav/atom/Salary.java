package com.example.akav.atom;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

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

public class Salary extends AppCompatActivity {
    String userId,JSON_STRING,jsonstring,datestring;
    JSONObject jo;
    JSONArray ja;
    String[] start,end;
    int[] amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary);
        userId=getIntent().getExtras().getString("userID");
        String method="dates";
        Backgroundtask backgroundtask2 = new Backgroundtask(this);
        backgroundtask2.execute(method, userId);

    }

    public void gridviewcall(){
        final GridView gridView=(GridView)findViewById(R.id.gridview);
        gridView.setAdapter(new SalaryAdapter(this,start,end,amt));

    }
    class Backgroundtask extends AsyncTask<String,Void,String> {

        Context ctx;

        Backgroundtask(Context ctx) {
            this.ctx = ctx;

        }



        @Override
        protected String doInBackground(String... params) {
            String reg_url = "http://atomwrapp.dx.am/salary.php";
            String method = params[0];
            if (method.equals("dates")) {

                String name = params[1];

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

                }
                catch (SocketTimeoutException s){
                    s.printStackTrace();
                    Toast.makeText(Salary.this, "Error connecting to the Internet, Please try again", Toast.LENGTH_SHORT).show();
                }
                catch(UnknownHostException u){
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
            Toast.makeText(ctx, res, Toast.LENGTH_LONG).show();
            // qr_result.setText(result);
            jsonstring = res;
            //json();
            // return  result;
            try {
                jo = new JSONObject(jsonstring);
                ja=jo.getJSONArray("salaryresponse");

                int i=0;
                int count=0;
                if(ja.length()!=0) {

                    start=new String[ja.length()];
                    end=new String[ja.length()];
                    amt=new int[ja.length()];
                    while (count < ja.length()) {
                        JSONObject j = ja.getJSONObject(count);
                        start[i] = j.getString("startdate");
                        end[i]=j.getString("enddate");
                        amt[i]=j.getInt("amount");

                        // inter_verification_status[i] = j.getInt("inter_verification");
                        i++;
                        count++;

                    }
                }else{
                    start=new String[1];
                    start[i] = "nodate";

                }


                // ress = jo.getInt("data");
                //  msg();
                // loadinglayout.setVisibility(View.GONE);
                // actuallayout.setVisibility(View.VISIBLE);
                gridviewcall();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
