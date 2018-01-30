package com.example.akav.atom;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OvertimeActivity extends AppCompatActivity {

    private LinearLayout previousCycle1;
    private LinearLayout previousCycle2;
    private LinearLayout previousCycle3;
    private LinearLayout currentCycle;

    private TextView prevCycle1Start;
    private TextView prevCycle1End;

    private TextView prevCycle2Start;
    private TextView prevCycle2End;

    private TextView prevCycle3Start;
    private TextView prevCycle3End;

    private TextView currentCycleStart;
    private TextView currentCycleEnd;

    private RelativeLayout progressBarLayout;
    private RelativeLayout cycleList;

    public static final String GET_DATE_URL = "http://atomwrapp.dx.am/getOtCycleDates.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime);

        progressBarLayout = (RelativeLayout) findViewById(R.id.progress_bar_layout);
        cycleList = (RelativeLayout) findViewById(R.id.cycle_list_layout);

        // Start the AsyncTask
        MainAsyncTask task = new MainAsyncTask();
        task.execute(GET_DATE_URL);

        previousCycle1 = (LinearLayout) findViewById(R.id.ot_previous_cycle_1);
        previousCycle2 = (LinearLayout) findViewById(R.id.ot_previous_cycle_2);
        previousCycle3 = (LinearLayout) findViewById(R.id.ot_previous_cycle_3);

        prevCycle1Start = (TextView) findViewById(R.id.ot_cycle_1_start);
        prevCycle1End = (TextView) findViewById(R.id.ot_cycle_1_end);

        prevCycle2Start = (TextView) findViewById(R.id.ot_cycle_2_start);
        prevCycle2End = (TextView) findViewById(R.id.ot_cycle_2_end);

        prevCycle3Start = (TextView) findViewById(R.id.ot_cycle_3_start);
        prevCycle3End = (TextView) findViewById(R.id.ot_cycle_3_end);

        currentCycleStart = (TextView) findViewById(R.id.ot_current_cycle_start);
        currentCycleEnd = (TextView) findViewById(R.id.ot_current_cycle_end);

        currentCycle = (LinearLayout) findViewById(R.id.ot_current_cycle);

        previousCycle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prevCycle1StartDate = prevCycle1Start.getText().toString();
                String prevCycle1EndDate = prevCycle1End.getText().toString();

                Intent prevCycle1ToGrid = new Intent(OvertimeActivity.this, OvertimeGridActivity.class);

                prevCycle1ToGrid.putExtra("startDate", prevCycle1StartDate);
                prevCycle1ToGrid.putExtra("endDate", prevCycle1EndDate);

                startActivity(prevCycle1ToGrid);
            }
        });

        previousCycle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prevCycle2StartDate = prevCycle2Start.getText().toString();
                String prevCycle2EndDate = prevCycle2End.getText().toString();

                Intent prevCycle2ToGrid = new Intent(OvertimeActivity.this, OvertimeGridActivity.class);

                prevCycle2ToGrid.putExtra("startDate", prevCycle2StartDate);
                prevCycle2ToGrid.putExtra("endDate", prevCycle2EndDate);

                startActivity(prevCycle2ToGrid);
            }
        });

        previousCycle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prevCycle3StartDate = prevCycle3Start.getText().toString();
                String prevCycle3EndDate = prevCycle3End.getText().toString();

                Intent prevCycle3ToGrid = new Intent(OvertimeActivity.this, OvertimeGridActivity.class);

                prevCycle3ToGrid.putExtra("startDate", prevCycle3StartDate);
                prevCycle3ToGrid.putExtra("endDate", prevCycle3EndDate);

                startActivity(prevCycle3ToGrid);
            }
        });

        currentCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentCycleStartDate = currentCycleStart.getText().toString();
                String currentCycleEndDate = currentCycleEnd.getText().toString();

                Intent currentCycleToGrid = new Intent(OvertimeActivity.this, OvertimeGridActivity.class);

                currentCycleToGrid.putExtra("startDate", currentCycleStartDate);
                currentCycleToGrid.putExtra("endDate", currentCycleEndDate);

                startActivity(currentCycleToGrid);
            }
        });

    }

    private String getCurrentCycleDates(String url){

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

            long startDateTimeStamp = root.getLong("Start Date");
            long endDateTimeStamp = root.getLong("End Date");

            String currentStartDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date(startDateTimeStamp * 1000));
            String currentEndDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date(endDateTimeStamp * 1000));

            currentCycleStart.setText(currentStartDate);
            currentCycleEnd.setText(currentEndDate);

            result = "" + startDateTimeStamp + ", " + endDateTimeStamp;

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
            String result = getCurrentCycleDates(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "isValidUser, isAdmin = " + result, Toast.LENGTH_SHORT).show();
            progressBarLayout.setVisibility(View.GONE);
            cycleList.setVisibility(View.VISIBLE);
        }
    }
}
