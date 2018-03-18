package com.example.akav.atom;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.nio.charset.Charset;

//notification imports


public class MainActivity extends AppCompatActivity {

    private EditText mUserName;
    private EditText mPassword;
    private Button mLogin;
    private Button mRegister;
    // private String userId;
    private String password;
    private ProgressBar progressBar;

    private Boolean isValidUser;
    private Boolean isAdmin;


    String JSON_STRING, jsonstring, userId;
    TextView temp;
    JSONObject jo;
    JSONArray ja;

    private final String LOGIN_URL = "http://atomwrapp.dx.am/login.php";

    //notification contents

    NotificationCompat.Builder Notvar;
    public static final int unique_id = 12345;
    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //notification contents
        //n=new NotificationCompat.Builder(this);
      /*  Intent intent = new Intent(this, HomeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(this);

        b.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_logout)
                .setTicker("Hearty365")
                .setContentTitle("Default notification")
                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setContentIntent(contentIntent)
                .setContentInfo("Info");


        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
*/
        Intent notifyIntent = new Intent(this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (this, 8, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                60000, pendingIntent);

        mLogin = (Button) findViewById(R.id.login_button);
        mRegister = (Button) findViewById(R.id.goto_register_button);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.INVISIBLE);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateUser();
            }
        });


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoRegisterIntent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(gotoRegisterIntent);
            }
        });


        // Temperorary Buttons

        Button fillUser = (Button) findViewById(R.id.user_fill);
        Button fillAdmin = (Button) findViewById(R.id.admin_fill);

        fillUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserName = (EditText) findViewById(R.id.user_id);
                mPassword = (EditText) findViewById(R.id.password);
                mUserName.setText("user");
                mPassword.setText("userpw");
            }
        });

        fillAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserName = (EditText) findViewById(R.id.user_id);
                mPassword = (EditText) findViewById(R.id.password);
                mUserName.setText("admin");
                mPassword.setText("adminpw");
            }
        });
    }


    // To Authenticate User.
    private void validateUser() {

        getUserDetails();

        InputMethodManager i = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        i.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

        if (isOnline()) {
            progressBar.setVisibility(View.VISIBLE);

            // Start the AsyncTask
            MainAsyncTask task = new MainAsyncTask();
            task.execute(LOGIN_URL);
        } else {

            Toast.makeText(this, "NO Internet Connection, Try again", Toast.LENGTH_SHORT).show();
        }

    }

    private void identifyUser() {

        try {
            if (isValidUser) {

                progressBar.setVisibility(View.INVISIBLE);


                Intent gotoAdminHomeIntent = new Intent(MainActivity.this, AdminHomeActivity.class);
                Intent gotoUserHomeIntent = new Intent(MainActivity.this, HomeActivity.class);

                if (isAdmin) {
                    gotoAdminHomeIntent.putExtra("userId", userId);

                    startActivity(gotoAdminHomeIntent);
                } else {


                    gotoUserHomeIntent.putExtra("userId", userId);
                    startActivity(gotoUserHomeIntent);


                }

            } else {
                progressBar.setVisibility(View.INVISIBLE);
                clearUserDetails();
                error();
            }
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
    }

    // Show Error
    private void error() {
        Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_LONG).show();
    }

    /**
     * If returned JSON object contains false for 'validUser'
     * then return false,
     * else set isAdmin and isRegularUser based on
     * JSON Objects fields.
     * Make the Server script return JSON containing the booleans:
     * 1. isValidUser
     * 2. isAdmin
     * 3. isRegularUser;
     */


    private void getUserDetails() {

        mUserName = (EditText) findViewById(R.id.user_id);
        mPassword = (EditText) findViewById(R.id.password);

        userId = mUserName.getText().toString();
        password = mPassword.getText().toString();
    }

    private void clearUserDetails() {

        mUserName = (EditText) findViewById(R.id.user_id);
        mPassword = (EditText) findViewById(R.id.password);

        mUserName.setText("");
        mPassword.setText("");
    }

    private String userLogin(String url) {

        //Create URI
        Uri baseUri = Uri.parse(url);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("userId", userId);
        uriBuilder.appendQueryParameter("password", password);

        String insertUrl = uriBuilder.toString();
        URL finalInsertUrl = null;

        try {
            finalInsertUrl = new URL(insertUrl);
        } catch (MalformedURLException e) {
            Log.e(MainActivity.class.getName(), "Problem Building the URL", e);
            ;
        }

        //Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(finalInsertUrl);
            if (jsonResponse == null) {
                return null;
            }
        } catch (IOException e) {
            Log.e(MainActivity.class.getName(), "Problem in Making HTTP request.", e);
        }

        String result = null;

        try {
            JSONObject root = new JSONObject(jsonResponse);

            isValidUser = root.getBoolean("isValidUser");
            isAdmin = root.getBoolean("isAdmin");

            result = isValidUser.toString() + ", " + isAdmin.toString();

        } catch (JSONException e) {
            Log.e(MainActivity.class.getName(), "Error in Parsing", e);
            ;
        }
        return result;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(30000);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);

        } catch (SocketTimeoutException s) {
            s.printStackTrace();
            Log.e(MainActivity.class.getName(), "Error connecting to the Internet", s);
            return null;
        } catch (UnknownHostException u) {
            u.printStackTrace();
        } catch (IOException e) {
            Log.e(MainActivity.class.getName(), "Problem retrieving JSON.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert JSON to String.
     */
    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();

            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private class MainAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = userLogin(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // Toast.makeText(getApplicationContext(), "isValidUser, isAdmin = " + result, Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            if (result == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "Error connecting to the Internet, Try Again", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                v.setGravity(Gravity.CENTER);
                toast.show();
            } else {
                identifyUser();
            }
        }
    }


    class Backgroundtaskonload extends AsyncTask<String, Void, String> {

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
                int h = 0;
                int count = 0;
                while (count < ja.length()) {
                    JSONObject j = ja.getJSONObject(count);
                    h = j.getInt("count");

                    count++;
                }
                String q = String.valueOf(h);
                temp.setText(q);
                //globalcount=h;


                // qr_result.setText(result);
                //jsonstring = res;

                //json();
                // return  result;
            } catch (JSONException e) {
                e.printStackTrace();
            }

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


}
