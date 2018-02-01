package com.example.akav.atom;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminHomeActivity extends AppCompatActivity {

    private Button checkOvertime;
    private Button checkTravelAllowance;
    private Button verification;

    private ProgressBar progressBar;

    String jsonResponse;

    private final String GET_OT_FORMS_URL = "http://atomwrapp.dx.am/getOtForms.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Intent loginInfo = getIntent();
        String userId = loginInfo.getStringExtra("userId");

        Toast.makeText(this, "Welcome Admin, " + userId, Toast.LENGTH_SHORT).show();

        checkOvertime = (Button) findViewById(R.id.admin_ot);
        checkTravelAllowance = (Button) findViewById((R.id.admin_ta));
        verification=(Button)findViewById(R.id.verify_user);

        progressBar = (ProgressBar) findViewById(R.id.admin_progress_bar);

        checkOvertime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                // Start the AsyncTask
                MainAsyncTask task = new MainAsyncTask();
                task.execute(GET_OT_FORMS_URL);
            }
        });

        checkTravelAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do nothing for now.
            }
        });

        verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goto_qrcode_verification = new Intent(AdminHomeActivity.this,QRverification.class);
                startActivity(goto_qrcode_verification);
            }
        });
    }

    private String getOtFroms(String url){

        //Create URI
        Uri baseUri = Uri.parse(url);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        String insertUrl = uriBuilder.toString();
        URL finalInsertUrl = null;

        try {
            finalInsertUrl = new URL(insertUrl);
        } catch (MalformedURLException e) {
            Log.e(MainActivity.class.getName(), "Problem Building the URL", e);;
        }

        //Perform HTTP request to the URL and receive a JSON response back
        jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(finalInsertUrl);
        }
        catch (IOException e){
            Log.e(MainActivity.class.getName(), "Problem in Making HTTP request.", e);
        }

        return jsonResponse;
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
            urlConnection.setReadTimeout(15000);
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
    private String readFromStream(InputStream inputStream) throws IOException {
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
            String result = getOtFroms(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);

            Intent goto_otforms_list = new Intent(AdminHomeActivity.this, OvertimeFormListActivity.class);
            goto_otforms_list.putExtra("OtFormList", result);
            startActivity(goto_otforms_list);
        }
    }
}