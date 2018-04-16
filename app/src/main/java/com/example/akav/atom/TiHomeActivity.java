package com.example.akav.atom;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class TiHomeActivity extends AppCompatActivity {


    private LinearLayout currentCycle;

    private TextView currentCycleStart;
    private TextView currentCycleEnd;

    private RelativeLayout progressBarLayout;
    private RelativeLayout cycleList;

    private ArrayList<CycleDateObject> errorCycleDateList;

    public static final String GET_DATE_URL = "http://atomwrapp.dx.am/getTiCycleDates.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ti_home);


        Intent loginInfo = getIntent();
        String userId = loginInfo.getStringExtra("userId");

        Toast.makeText(this, "Welcome TI, " + userId + "!", Toast.LENGTH_SHORT).show();


        progressBarLayout = (RelativeLayout) findViewById(R.id.progress_bar_layout);
        cycleList = (RelativeLayout) findViewById(R.id.cycle_list_layout);

        errorCycleDateList = new ArrayList<>();

        if (isOnline()) {
            // Start the AsyncTask
            MainAsyncTask task = new MainAsyncTask();
            task.execute(GET_DATE_URL);
        } else {
            Toast.makeText(this, "NO Internet Connection, Try again", Toast.LENGTH_SHORT).show();
        }


        ListView cycleDateListView = (ListView) findViewById(R.id.prev_cycle_date_list_view);

        ErrorCycleDateListAdapter dateListAdapter = new ErrorCycleDateListAdapter(this, errorCycleDateList);

        cycleDateListView.setAdapter(dateListAdapter);


        /*currentCycle.setOnClickListener(new View.OnClickListener() {
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
                currentCycleFormsList.putExtra("iserrorCycle", 0);
                currentCycleFormsList.putExtra("strt", s);
                currentCycleFormsList.putExtra("enddt", e);


                startActivity(currentCycleFormsList);
            }
        });*/

        /*cycleDateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                CycleDateObject cycleDate = errorCycleDateList.get(position);

                String errorCycleStartDate = cycleDate.getStartDate();
                String errorCycleEndDate = cycleDate.getEndDate();

                SimpleDateFormat startDateFormat = new SimpleDateFormat("dd - MM - yyyy");
                SimpleDateFormat endDateFormat = new SimpleDateFormat("dd - MM - yyyy");

                Date startDate = null;
                Date endDate = null;
                try {
                    startDate = startDateFormat.parse(errorCycleStartDate);
                    endDate = endDateFormat.parse(errorCycleEndDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                // Get Timestamp from date string
                Long startDateTimestamp = startDate.getTime() / 1000;
                Long endDateTimestamp = endDate.getTime() / 1000;
                String s = errorCycleStartDate.substring(10, 14) + "-" + errorCycleStartDate.substring(5, 7) + "-" + errorCycleStartDate.substring(0, 2);
                String e = errorCycleEndDate.substring(10, 14) + "-" + errorCycleEndDate.substring(5, 7) + "-" + errorCycleEndDate.substring(0, 2);


                Intent errorCycleFormsList = new Intent(AdminNotification.this, OvertimeFormListActivity.class);


                errorCycleFormsList.putExtra("startDate", startDateTimestamp.toString());
                errorCycleFormsList.putExtra("endDate", endDateTimestamp.toString());
                errorCycleFormsList.putExtra("isPreviousCycle", 2);
                errorCycleFormsList.putExtra("strt", s);
                errorCycleFormsList.putExtra("enddt", e);

                startActivity(errorCycleFormsList);
            }
        });*/
    }

    private String getCycleDates(String url) {

        //Create URI
        Uri baseUri = Uri.parse(url);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("flag", "2");

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

        String result = jsonResponse;

        try {
            // For error Cycle Dates
            JSONObject root = new JSONObject(jsonResponse);

            Integer flag = root.getInt("flag");

            JSONArray errorCycleArray = root.getJSONArray("Error Cycle Dates");

            for (int index = 0; index < errorCycleArray.length(); index++) {
                JSONObject cycleDates = errorCycleArray.getJSONObject(index);

                long startDate = cycleDates.getLong("Start Date");
                long endDate = cycleDates.getLong("End Date");

                errorCycleDateList.add(new CycleDateObject(startDate, endDate, flag));
            }
        } catch (JSONException e) {
            Log.e(MainActivity.class.getName(), "Error in Parsing", e);
        }
        Log.i(AdminNotification.class.getName(), "Response" + result);
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
            Log.e(AdminNotification.class.getName(), "Error connecting to the internet", s);
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
        Log.i(AdminNotification.class.getName(), "Response" + jsonResponse);
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
