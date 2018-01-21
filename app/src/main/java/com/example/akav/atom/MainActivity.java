package com.example.akav.atom;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private EditText mUserName;
    private EditText mPassword;
    private Button mLogin;
    private Button mRegister;
    private Button temp;    //Temporary button for inserting.
    private String userId;
    private String password;
    private ProgressBar progressBar;

    private Boolean isAdmin;
    private Boolean isRegularUser;
    private Boolean flag;   //Set true when add data is clicked, false when login is clicked

    private final String INSERT_DATA_URL = "https://railwayproject.000webhostapp.com/insertData.php";
    private final String GET_DATA_URL = "https://railwayproject.000webhostapp.com/getData.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogin = (Button) findViewById(R.id.login_button);
        mRegister = (Button) findViewById(R.id.goto_register_button);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                flag = false;

                authenticateUser();
            }
        });


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoRegisterIntent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(gotoRegisterIntent);
            }
        });

        // Temporary Insert Button
        temp = (Button) findViewById(R.id.temp_add);
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                flag = true;

                // Start the AsyncTask
                MainAsyncTask task = new MainAsyncTask();
                task.execute(INSERT_DATA_URL);
            }
        });

    }

    // To Authenticate User.
    private void authenticateUser() {

        getUserDetails();

        // Remove Comments once the getData.php on Server is written.
        // Start the AsyncTask
        // MainAsyncTask task = new MainAsyncTask();
        // task.execute(GET_DATA_URL);

        if (isValidUser()) {

            progressBar.setVisibility(View.INVISIBLE);

            Intent gotoUserHomeIntent = new Intent(MainActivity.this, HomeActivity.class);
            Intent gotoAdminHomeIntent = new Intent(MainActivity.this, AdminHomeActivity.class);

            if(isAdmin){
                gotoAdminHomeIntent.putExtra("userId", userId);
                startActivity(gotoAdminHomeIntent);
            }
            else{
                gotoUserHomeIntent.putExtra("userName", userId);
                startActivity(gotoUserHomeIntent);
            }

        } else {
            progressBar.setVisibility(View.INVISIBLE);
            error();
        }
    }

    // To Validate Enterd Credentials.
    private void error(){
        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
    }

    private boolean isValidUser() {

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

        if(isAdmin(userId, password)){
            isAdmin = true;
            isRegularUser = false;
            return true;
        }
        else if(isRegularUser(userId, password)){
            isRegularUser = true;
            isAdmin = false;
            return true;
        }
        return false;
    }

    private boolean isRegularUser(String userId, String password) {
        // Validation Code
        if(userId.equals("u")&&password.equals("p")){
            return true;
        }
        return false;
    }

    private boolean isAdmin(String userId, String password) {
        // Validation Code
        if(userId.equals("a")&&password.equals("p")){
            return true;
        }
        return false;
    }

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

    private String addData(String url){

        getUserDetails();

        if(userId == "" || password == ""){
            return "false";
        }

        if(userId == null || password == null){
            return "false";
        }

        //Create URI
        Uri baseUri = Uri.parse(url);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("userName", userId);
        uriBuilder.appendQueryParameter("password", password);

        String insertUrl = uriBuilder.toString();
        URL finalInsertUrl = null;

        try {
            finalInsertUrl = new URL(insertUrl);
        } catch (MalformedURLException e) {
            Log.e(MainActivity.class.getName(), "Problem Building the URL", e);;
        }

        //Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(finalInsertUrl);
        }
        catch (IOException e){
            Log.e(MainActivity.class.getName(), "Problem in Making HTTP request.", e);
        }

        String result = null;

        try {
            JSONObject root = new JSONObject(jsonResponse);
            result = root.getString("insertResult");

        } catch (JSONException e) {
            Log.e(MainActivity.class.getName(), "Error in Parsing", e);;
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
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);

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
    private String readFromStream(InputStream inputStream) throws  IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();

            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private class MainAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = addData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Added: " + result, Toast.LENGTH_SHORT).show();
            clearUserDetails();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
