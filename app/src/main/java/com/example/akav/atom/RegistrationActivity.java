package com.example.akav.atom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Random;

public class RegistrationActivity extends AppCompatActivity {

    private EditText fullNameEditText;
    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private EditText uniquepfno;
    EditText input;

    private Spinner categorySpinner;

    private Button gotoLogin;
    private Button registerButton,enterotp;

    private String fullName;
    String JSON_STRING;
    private String emailAddress;
    private String ottext;
    private EditText password;
    private String category;
    String otptext;

    private String name,email,mobile,pwd,pfno,cat,subcat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        fullNameEditText = (EditText) findViewById(R.id.full_name);
        emailEditText = (EditText) findViewById(R.id.email_address);
        phoneNumberEditText = (EditText) findViewById(R.id.phone_number);
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        registerButton = (Button) findViewById(R.id.qrcodebutton);
        enterotp=(Button)findViewById(R.id.enterotp);
        uniquepfno = (EditText) findViewById(R.id.pf_number);
        password=(EditText)findViewById(R.id.password);

        name=fullNameEditText.getText().toString();
        email=emailEditText.getText().toString();
        mobile=phoneNumberEditText.getText().toString();
        pwd=password.getText().toString();
        pfno=uniquepfno.getText().toString();
        //TODO:no category in db,entry during formfilling





        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /*Intent gotoHomeIntent = new Intent(RegistrationActivity.this, HomeActivity.class);

                //Only for Testing
                fullName = fullNameEditText.getText().toString();
                Uri nameUri = Uri.parse(fullName);
                gotoHomeIntent.setData(nameUri);
                startActivity(gotoHomeIntent);*/

                msg();
            }
        });
        enterotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alert();
            }
        });


        setupSpinner();

    }
    private void msg(){
        fullNameEditText = (EditText) findViewById(R.id.full_name);
        emailEditText = (EditText) findViewById(R.id.email_address);
        phoneNumberEditText = (EditText) findViewById(R.id.phone_number);
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        registerButton = (Button) findViewById(R.id.qrcodebutton);
        enterotp=(Button)findViewById(R.id.enterotp);
        uniquepfno = (EditText) findViewById(R.id.pf_number);
        password=(EditText)findViewById(R.id.password);

        name=fullNameEditText.getText().toString();
        email=emailEditText.getText().toString();
        mobile=phoneNumberEditText.getText().toString();
        pwd=password.getText().toString();
        pfno=uniquepfno.getText().toString();
        String method="otp";
        otptext=randomstring();
        Backgroundtask backgroundtask = new Backgroundtask(this);
        backgroundtask.execute(method,name,email,mobile,pwd,pfno,otptext);
       // Toast.makeText(this, "OTP sent to your email id", Toast.LENGTH_SHORT).show();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the Category.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        categorySpinner.setAdapter(genderSpinnerAdapter);

        // Set the category String based on spinner selection
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String) parent.getItemAtPosition(position);
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = "Select Category";
            }
        });
    }

    public void alert() {

            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
            LayoutInflater inflater = RegistrationActivity.this.getLayoutInflater();

            SharedPreferences sharedPref = RegistrationActivity.this.getPreferences(Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPref.edit();

            builder.setView(inflater.inflate(R.layout.qrcode, null))
                    .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Dialog dialg=(Dialog)dialog;
                            EditText et = (EditText) dialg.findViewById(R.id.otptb);
                             ottext = et.getText().toString();

                            savemessage();
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
builder.show();

       /* final AlertDialog.Builder alertadd = new AlertDialog.Builder(
                RegistrationActivity.this);
        alertadd.setTitle("OTP verification");
          input = new EditText(RegistrationActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertadd.setView(input);



      //  LayoutInflater factory = LayoutInflater.from(RegistrationActivity.this);
      //  final View view = factory.inflate(R.layout.qrcode, null);

        // ImageView image= (ImageView) view.findViewById(R.id.imageView);
        // image.setImageResource(R.drawable.logo);


        VCard abhay = new VCard(fullNameEditText.getText().toString())
                .setEmail(password.getText().toString())
                .setTitle( uniquepfno.getText().toString())
                .setPhoneNumber(category)
                .setAddress(category);

      /*  Bitmap myBitmap = QRCode.from(abhay).bitmap();
        final ImageView image = (ImageView) view.findViewById(imageView);
        image.setImageBitmap(myBitmap);


        alertadd.setView(view);
        alertadd.setNeutralButton("SAVE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

              /*  String method="permreg";
               Backgroundtaskpermreg backgroundtask1 = new Backgroundtaskpermreg(getApplicationContext());

              // backgroundtask1.execute(method,totp);
               savemessage();
                finish();

            }

        });
        alertadd.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });


        alertadd.show();
*/
    }

    private void savemessage() {

        Toast.makeText(this, ottext, Toast.LENGTH_SHORT).show();
        String method="permreg";
        Backgroundtaskpermreg backgroundtask1 = new Backgroundtaskpermreg(this);

        backgroundtask1.execute(method,ottext);
    }
    private String randomstring(){

            String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
            StringBuilder salt = new StringBuilder();
            Random rnd = new Random();
            while (salt.length() < 9) { // length of the random string.
                int index = (int) (rnd.nextFloat() * SALTCHARS.length());
                salt.append(SALTCHARS.charAt(index));
            }
            String saltStr = salt.toString();
            return saltStr;


    }
    class Backgroundtask extends AsyncTask<String, Void, String> {

        Context ctx;

        Backgroundtask(Context ctx) {
            this.ctx = ctx;

        }


        @Override
        protected String doInBackground(String... params) {
            String reg_url = "http://atomwrapp.dx.am/tempregistration.php";
            String method = params[0];
            if (method.equals("otp")) {


                String pwd = params[4];
                String name = params[1];
             String email=params[2];
             String mobile=params[3];
             String pfno=params[5];
             String otptxt=params[6];


                try {
                    URL url = new URL(reg_url);
                    HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
                    httpurlconnection.setRequestMethod("POST");

                    httpurlconnection.setDoOutput(true);
                    OutputStream OS = httpurlconnection.getOutputStream();
                    BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));

                    String newdata = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                            URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                            URLEncoder.encode("mobile", "UTF-8") + "=" + URLEncoder.encode(mobile, "UTF-8") + "&" +
                            URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pwd, "UTF-8") + "&" +
                            URLEncoder.encode("pfno", "UTF-8") + "=" + URLEncoder.encode(pfno, "UTF-8") + "&" +

                            URLEncoder.encode("otpstring", "UTF-8") + "=" + URLEncoder.encode(otptxt, "UTF-8") ;



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
    class Backgroundtaskpermreg extends AsyncTask<String,Void,String> {

        Context ctx;

        Backgroundtaskpermreg(Context ctx) {
            this.ctx = ctx;

        }


        @Override
        protected String doInBackground(String... params) {
            String reg_url = "http://atomwrapp.dx.am/permregistration.php";
            String method = params[0];
            if (method.equals("permreg")) {

                String otp = params[1];
                // String dat=params[2];


                try {
                    URL url = new URL(reg_url);
                    HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
                    httpurlconnection.setRequestMethod("POST");

                    httpurlconnection.setDoOutput(true);
                    OutputStream OS = httpurlconnection.getOutputStream();
                    BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));

                    String newdata = URLEncoder.encode("otp", "UTF-8") + "=" + URLEncoder.encode(otp, "UTF-8");


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



                // qr_result.setText(result);
                //jsonstring = res;

                //json();
                // return  result;
            }

        }



}



