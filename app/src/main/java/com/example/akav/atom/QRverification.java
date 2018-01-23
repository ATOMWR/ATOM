package com.example.akav.atom;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;


public class QRverification extends AppCompatActivity {
    String name, password, pfno, category, subcategory;
    String[] resultarray = new String[10];
    TextView qr_result;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrverification);
         submit=(Button)findViewById(R.id.submitbutton);

        final Activity activity = this;

        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan the generated QR Code");
        integrator.setCameraId(0);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();

      /*  submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbentry();
            }
        });*/

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         qr_result= (TextView) findViewById(R.id.qr_result);
        submit=(Button)findViewById(R.id.submitbutton);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "you cancelled scanning", Toast.LENGTH_LONG).show();
            } else {

                String fulldata = result.getContents().toString();


                // String substr=fulldata.substring()
                Scanner s = new Scanner(fulldata);
                int i = 0;
                while (s.hasNextLine()) {
                    resultarray[i] = s.nextLine();
                    i++;

                }
            }
                name = resultarray[2].substring(resultarray[2].indexOf(":") + 1);
                pfno = resultarray[3].substring(resultarray[3].indexOf(":") + 1);
                password = resultarray[5].substring(resultarray[5].indexOf(":") + 1);
                category = resultarray[4].substring(resultarray[4].indexOf(":") + 1);
                subcategory = resultarray[6].substring(resultarray[6].indexOf(":") + 1);


                qr_result.setText("name:"+name + "\n" + "pfno:" +pfno + "\n" + "password:"+password + "\n" + "category:"+category + "\n" + "subcategory:"+subcategory);


                // Intent returntoadmin=new Intent(QRverification.this,AdminHomeActivity.class);
                //startActivity(returntoadmin);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // open();
                    confirmationcall();
                }
            });
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
    public void open(){
        String method="register";
       // Toast.makeText(QRverification.this, "You clicked yes button", Toast.LENGTH_LONG).show();

        Backgroundtask backgroundtask=new Backgroundtask(this);
        backgroundtask.execute(method,name,password,pfno,category,subcategory);
    }
   public void confirmationcall(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to make decision");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                       // Toast.makeText(QRverification.this, "You clicked yes button", Toast.LENGTH_LONG).show();
                       open();
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }






    class Backgroundtask extends AsyncTask<String,Void,String> {

       Context ctx;

       Backgroundtask(Context ctx) {
            this.ctx = ctx;

        }



        @Override
        protected String doInBackground(String... params) {
            String reg_url = "http://192.168.1.6/logout.php";
            String method = params[0];
            if (method.equals("register")) {


                String name = params[1];
                String password = params[2];
                String  pfno= params[3];
                String category=params[4];
                String subcategory=params[5];



                try {
                    URL url = new URL(reg_url);
                    HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
                    httpurlconnection.setRequestMethod("POST");

                    httpurlconnection.setDoOutput(true);
                    OutputStream OS = httpurlconnection.getOutputStream();
                    BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));

                    String newdata = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                            URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" +
                            URLEncoder.encode("pfno", "UTF-8") + "=" + URLEncoder.encode(pfno, "UTF-8") +"&" +
                            URLEncoder.encode("cat", "UTF-8") + "=" + URLEncoder.encode(category, "UTF-8") + "&" +
                            URLEncoder.encode("subcat", "UTF-8") + "=" + URLEncoder.encode(subcategory, "UTF-8") ;

                    bufferedwriter.write(newdata);
                    // Toast.makeText(ctx, "data written", Toast.LENGTH_LONG).show();

                    bufferedwriter.flush();
                    bufferedwriter.close();
                    OS.close();
                    InputStream IS = httpurlconnection.getInputStream();
                    IS.close();



                    return "successful registration";


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




}
