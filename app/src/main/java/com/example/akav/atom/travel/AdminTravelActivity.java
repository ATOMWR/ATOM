package com.example.akav.atom.travel;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akav.atom.MainActivity;
import com.example.akav.atom.R;
import com.example.akav.atom.cycleDateObject;
import com.example.akav.atom.overtime.AdminOvertimeActivity;
import com.example.akav.atom.overtime.OvertimeFormListActivity;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdminTravelActivity extends AppCompatActivity {


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
        setContentView(R.layout.activity_admin_overtime);

        progressBarLayout = (RelativeLayout) findViewById(R.id.progress_bar_layout);
        cycleList = (RelativeLayout) findViewById(R.id.cycle_list_layout);

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

                /*SimpleDateFormat startDateFormat = new SimpleDateFormat("dd - MM - yyyy");
                SimpleDateFormat endDateFormat = new SimpleDateFormat("dd - MM - yyyy");

                Date startDate = null;
                Date endDate = null;
                try {
                    startDate = startDateFormat.parse(currentCycleStartDate);
                    endDate = endDateFormat.parse(currentCycleEndDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Get Timestamp from date string
                Long startDateTimestamp = startDate.getTime() / 1000;
                Long endDateTimestamp = endDate.getTime() / 1000;

                Intent currentCycleFormsList = new Intent(AdminTravelActivity.this, OvertimeFormListActivity.class);


                currentCycleFormsList.putExtra("startDate", startDateTimestamp.toString());
                currentCycleFormsList.putExtra("endDate", endDateTimestamp.toString());
                currentCycleFormsList.putExtra("isPreviousCycle", 0);

                startActivity(currentCycleFormsList);*/

                Toast.makeText(AdminTravelActivity.this, currentCycleStartDate + "  TO  " + currentCycleEndDate, Toast.LENGTH_SHORT).show();

            }
        });

        cycleDateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                cycleDateObject cycleDate = previousCycleDateList.get(position);

                String previousCycleStartDate = cycleDate.getStartDate();
                String previousCycleEndDate = cycleDate.getEndDate();

                SimpleDateFormat startDateFormat = new SimpleDateFormat("dd - MM - yyyy");
                SimpleDateFormat endDateFormat = new SimpleDateFormat("dd - MM - yyyy");

                /*Date startDate = null;
                Date endDate = null;
                try {
                    startDate = startDateFormat.parse(previousCycleStartDate);
                    endDate = endDateFormat.parse(previousCycleEndDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Get Timestamp from date string
                Long startDateTimestamp = startDate.getTime() / 1000;
                Long endDateTimestamp = endDate.getTime() / 1000;

                Intent previousCycleFormsList = new Intent(AdminTravelActivity.this, OvertimeFormListActivity.class);


                previousCycleFormsList.putExtra("startDate", startDateTimestamp.toString());
                previousCycleFormsList.putExtra("endDate", endDateTimestamp.toString());
                previousCycleFormsList.putExtra("isPreviousCycle", 1);

                startActivity(previousCycleFormsList);*/

                Toast.makeText(AdminTravelActivity.this, previousCycleStartDate + "  TO  " + previousCycleEndDate, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCycleDates(String url) {

        //Create URI
        Uri baseUri = Uri.parse(url);
        Uri.Builder uriBuilder = baseUri.buildUpon();

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
        } catch (IOException e) {
            Log.e(MainActivity.class.getName(), "Problem in Making HTTP request.", e);
        }

        String result = null;
        JSONObject root = null;
        try {
            root = new JSONObject(jsonResponse);

            // For Current Cycle Date
            long startDateTimeStamp = root.getLong("Start Date");
            long endDateTimeStamp = root.getLong("End Date");

            String currentStartDate = new SimpleDateFormat("dd - MM - yyyy").format(new Date(startDateTimeStamp * 1000));
            String currentEndDate = new SimpleDateFormat("dd - MM - yyyy").format(new Date(endDateTimeStamp * 1000));

            currentCycleStart.setText(currentStartDate);
            currentCycleEnd.setText(currentEndDate);

            result = "" + startDateTimeStamp + ", " + endDateTimeStamp;

        } catch (JSONException e) {
            Log.e(MainActivity.class.getName(), "Error in Parsing", e);
        }

        try {
            // For Previous Cycle Dates

            JSONArray previousCycleArray = root.getJSONArray("Previous Cycle Dates");

            for (int index = 0; index < previousCycleArray.length(); index++) {
                JSONObject cycleDates = previousCycleArray.getJSONObject(index);

                long startDate = cycleDates.getLong("Start Date");
                long endDate = cycleDates.getLong("End Date");

                previousCycleDateList.add(new cycleDateObject(startDate, endDate));
            }
        } catch (JSONException e) {
            Log.e(MainActivity.class.getName(), "Error in Parsing", e);
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