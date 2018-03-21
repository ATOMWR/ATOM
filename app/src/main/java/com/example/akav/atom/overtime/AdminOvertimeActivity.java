package com.example.akav.atom.overtime;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akav.atom.MainActivity;
import com.example.akav.atom.CycleDateObject;
import com.example.akav.atom.R;
import com.example.akav.atom.PreviousCycleDateListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdminOvertimeActivity extends AppCompatActivity {

    private LinearLayout currentCycle;

    private TextView currentCycleStart;
    private TextView currentCycleEnd;

    private RelativeLayout progressBarLayout;
    private RelativeLayout cycleList;

    private ArrayList<CycleDateObject> previousCycleDateList;

    public static final String GET_DATE_URL = "http://atomwrapp.dx.am/getOtCycleDates.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_overtime);

        progressBarLayout = (RelativeLayout) findViewById(R.id.progress_bar_layout);
        cycleList = (RelativeLayout) findViewById(R.id.cycle_list_layout);

        previousCycleDateList = new ArrayList<>();

        if (isOnline()) {
            // Start the AsyncTask
            MainAsyncTask task = new MainAsyncTask();
            task.execute(GET_DATE_URL);
        } else {
            Toast.makeText(this, "NO Internet Connection, Try again", Toast.LENGTH_SHORT).show();
        }


        ListView cycleDateListView = (ListView) findViewById(R.id.prev_cycle_date_list_view);

        PreviousCycleDateListAdapter dateListAdapter = new PreviousCycleDateListAdapter(this, previousCycleDateList);

        cycleDateListView.setAdapter(dateListAdapter);

        currentCycleStart = (TextView) findViewById(R.id.ot_current_cycle_start);
        currentCycleEnd = (TextView) findViewById(R.id.ot_current_cycle_end);

        currentCycle = (LinearLayout) findViewById(R.id.ot_current_cycle);

        currentCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentCycleStartDate = currentCycleStart.getText().toString();
                String currentCycleEndDate = currentCycleEnd.getText().toString();

                SimpleDateFormat startDateFormat = new SimpleDateFormat("dd - MM - yyyy");
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
                String s = currentCycleStartDate.substring(10, 14) + "-" + currentCycleStartDate.substring(5, 7) + "-" + currentCycleStartDate.substring(0, 2);
                String e = currentCycleEndDate.substring(10, 14) + "-" + currentCycleEndDate.substring(5, 7) + "-" + currentCycleEndDate.substring(0, 2);


                Intent currentCycleFormsList = new Intent(AdminOvertimeActivity.this, OvertimeFormListActivity.class);


                currentCycleFormsList.putExtra("startDate", startDateTimestamp.toString());
                currentCycleFormsList.putExtra("endDate", endDateTimestamp.toString());
                currentCycleFormsList.putExtra("isPreviousCycle", 0);
                currentCycleFormsList.putExtra("strt", s);
                currentCycleFormsList.putExtra("enddt", e);


                startActivity(currentCycleFormsList);
            }
        });

        cycleDateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CycleDateObject cycleDate = previousCycleDateList.get(position);

                String previousCycleStartDate = cycleDate.getStartDate();
                String previousCycleEndDate = cycleDate.getEndDate();

                SimpleDateFormat startDateFormat = new SimpleDateFormat("dd - MM - yyyy");
                SimpleDateFormat endDateFormat = new SimpleDateFormat("dd - MM - yyyy");

                Date startDate = null;
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
                String s = previousCycleStartDate.substring(10, 14) + "-" + previousCycleStartDate.substring(5, 7) + "-" + previousCycleStartDate.substring(0, 2);
                String e = previousCycleEndDate.substring(10, 14) + "-" + previousCycleEndDate.substring(5, 7) + "-" + previousCycleEndDate.substring(0, 2);


                Intent previousCycleFormsList = new Intent(AdminOvertimeActivity.this, OvertimeFormListActivity.class);


                previousCycleFormsList.putExtra("startDate", startDateTimestamp.toString());
                previousCycleFormsList.putExtra("endDate", endDateTimestamp.toString());
                previousCycleFormsList.putExtra("isPreviousCycle", 1);
                previousCycleFormsList.putExtra("strt", s);
                previousCycleFormsList.putExtra("enddt", e);

                startActivity(previousCycleFormsList);
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
            if (jsonResponse == null) {
                return null;
            }
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

                previousCycleDateList.add(new CycleDateObject(startDate, endDate));
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

        } catch (SocketTimeoutException s) {
            s.printStackTrace();
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
            String result = getCycleDates(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            progressBarLayout.setVisibility(View.GONE);

            if (result == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "Error connecting to the Internet, Try again", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                v.setGravity(Gravity.CENTER);
                toast.show();
            } else {
                cycleList.setVisibility(View.VISIBLE);
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
