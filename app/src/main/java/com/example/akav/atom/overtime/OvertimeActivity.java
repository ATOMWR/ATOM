package com.example.akav.atom.overtime;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akav.atom.MainActivity;
import com.example.akav.atom.cycleDateObject;
import com.example.akav.atom.R;
import com.example.akav.atom.previousCycleDateListAdapter;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OvertimeActivity extends AppCompatActivity {

    private LinearLayout currentCycle;

    private TextView currentCycleStart;
    private TextView currentCycleEnd;

    private RelativeLayout progressBarLayout;
    private RelativeLayout cycleList;

    private ArrayList<cycleDateObject> previousCycleDateList;

    public static final String GET_DATE_URL = "http://atomwrapp.dx.am/getOtCycleDates.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime);

        progressBarLayout = (RelativeLayout) findViewById(R.id.progress_bar_layout);
        cycleList = (RelativeLayout) findViewById(R.id.cycle_list_layout);
        final String userId = getIntent().getExtras().getString("userID");

        previousCycleDateList = new ArrayList<>();

        // Start the AsyncTask
        MainAsyncTask task = new MainAsyncTask();
        task.execute(GET_DATE_URL);

        ListView cycleDateListView = (ListView) findViewById(R.id.prev_cycle_date_list_view);

        previousCycleDateListAdapter dateListAdapter = new previousCycleDateListAdapter(this, previousCycleDateList);

        cycleDateListView.setAdapter(dateListAdapter);

        currentCycleStart = (TextView) findViewById(R.id.ot_current_cycle_start);
        currentCycleEnd = (TextView) findViewById(R.id.ot_current_cycle_end);

        currentCycle = (LinearLayout) findViewById(R.id.ot_current_cycle);

        currentCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentCycleStartDate = currentCycleStart.getText().toString();
                String currentCycleEndDate = currentCycleEnd.getText().toString();

                String sdc=currentCycleStartDate.substring(10,14)+"-"+currentCycleStartDate.substring(5,7)+"-"+currentCycleStartDate.substring(0,2);

                String edc=currentCycleEndDate.substring(10,14)+"-"+currentCycleEndDate.substring(5,7)+"-"+currentCycleEndDate.substring(0,2);

                Intent currentCycleToGrid = new Intent(OvertimeActivity.this, OvertimeGridActivity.class);


                currentCycleToGrid.putExtra("startDate", sdc);
                currentCycleToGrid.putExtra("endDate", edc);
                currentCycleToGrid.putExtra("userID", userId);
                currentCycleToGrid.putExtra("fromcyclelist", "canfill");


                startActivity(currentCycleToGrid);
            }
        });

        cycleDateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                cycleDateObject cycleDate = previousCycleDateList.get(position);

                String startDate = cycleDate.getStartDate();
                String endDate = cycleDate.getEndDate();

                String sd=startDate.substring(10,14)+"-"+startDate.substring(5,7)+"-"+startDate.substring(0,2);

                String ed=endDate.substring(10,14)+"-"+endDate.substring(5,7)+"-"+endDate.substring(0,2);

                Intent prevCycleToGrid = new Intent(OvertimeActivity.this, OvertimeGridActivity.class);

                prevCycleToGrid.putExtra("startDate", sd);
                prevCycleToGrid.putExtra("endDate", ed);
                prevCycleToGrid.putExtra("userID", userId);
                prevCycleToGrid.putExtra("fromcyclelist", "cannotfill");


                startActivity(prevCycleToGrid);

                Toast.makeText(OvertimeActivity.this, startDate + "  TO  " + endDate, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCycleDates(String url){

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

            // For Current Cycle Date
            long startDateTimeStamp = root.getLong("Start Date");
            long endDateTimeStamp = root.getLong("End Date");

            String currentStartDate = new SimpleDateFormat("dd - MM - yyyy").format(new Date(startDateTimeStamp * 1000));
            String currentEndDate = new SimpleDateFormat("dd - MM - yyyy").format(new Date(endDateTimeStamp * 1000));

            currentCycleStart.setText(currentStartDate);
            currentCycleEnd.setText(currentEndDate);

            // For Previous Cycle Dates

            JSONArray previousCycleArray = root.getJSONArray("Previous Cycle Dates");

            for (int index = 0; index < previousCycleArray.length(); index++){
                JSONObject cycleDates = previousCycleArray.getJSONObject(index);

                Long startDate = cycleDates.getLong("Start Date");
                Long endDate = cycleDates.getLong("End Date");

                previousCycleDateList.add(new cycleDateObject(startDate, endDate));
            }

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
            urlConnection.setReadTimeout(30000);
            urlConnection.setConnectTimeout(30000);
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
            String result = getCycleDates(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(), "isValidUser, isAdmin = " + result, Toast.LENGTH_SHORT).show();
            progressBarLayout.setVisibility(View.GONE);
            cycleList.setVisibility(View.VISIBLE);
        }
    }
}
