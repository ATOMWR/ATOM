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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akav.atom.MainActivity;
import com.example.akav.atom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class OvertimeSummaryListActivity extends AppCompatActivity {

    private Button sendToHq;
    private Button detailedReport;
    private Button summarisedReport;

    private String startDateTimestamp;
    private String endDateTimeStamp;
    private String jsonResponse;
    private String sd, ed;

    private JSONObject jsonToSend;

    private LinearLayout updateFormProgressLayout;
    private RelativeLayout formListLayout;

    private TextView loadingFormTextView;

    private Integer isPreviousCycle;

    ListView listView;

    OvertimeFormListAdapter formListAdapter;
    OvertimeSummaryListAdapter summaryListAdapter;

    private ArrayList<OvertimeFormObject> formList;
    private ArrayList<OvertimeSummaryObject> summaryList;

    private final String GET_OT_FORMS_URL = "http://atomwrapp.dx.am/getOtSummary.php";
    private final String SEND_TO_HQ = "http://atomwrapp.dx.am/sendToHq.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime_summary_list);

        Intent intent = getIntent();
        startDateTimestamp = intent.getStringExtra("startDate");
        endDateTimeStamp = intent.getStringExtra("endDate");
        isPreviousCycle = intent.getIntExtra("isPreviousCycle", 0);
        sd = getIntent().getExtras().getString("strt");
        ed = getIntent().getExtras().getString("enddt");


        if (isOnline()) {
            // Start the AsyncTask
            MainAsyncTask2 task2 = new MainAsyncTask2();
            task2.execute(GET_OT_FORMS_URL);
        } else {
            Toast.makeText(this, "NO Internet Connection, Try again", Toast.LENGTH_SHORT).show();
        }

        updateFormProgressLayout = (LinearLayout) findViewById(R.id.update_form_list_progress_layout);
        formListLayout = (RelativeLayout) findViewById(R.id.form_list_layout);
        loadingFormTextView = (TextView) findViewById(R.id.loading_forms_text);

        sendToHq = (Button) findViewById(R.id.send_to_hq);
        detailedReport = (Button) findViewById(R.id.detailed_list);
        summarisedReport = (Button) findViewById(R.id.summary_list);

        listView = (ListView) findViewById(R.id.list);

        sendToHq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO : Add code to pull report.
                // msg();

                jsonToSend = ArrayListToJson(summaryList);

                loadingFormTextView.setText("Sending to Headquarters.");
                formListLayout.setVisibility(View.GONE);
                updateFormProgressLayout.setVisibility(View.VISIBLE);


                if (isOnline()) {
                    // Start the AsyncTask
                    MainAsyncTask task2 = new MainAsyncTask();
                    task2.execute(SEND_TO_HQ);
                } else {
                    Toast.makeText(OvertimeSummaryListActivity.this, "NO Internet Connection, Try again", Toast.LENGTH_SHORT).show();
                }

            }
        });

        detailedReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                detailedReport.setVisibility(View.GONE);
                sendToHq.setVisibility(View.GONE);
                summarisedReport.setVisibility(View.VISIBLE);

                listView.setAdapter(formListAdapter);

            }
        });

        summarisedReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                summarisedReport.setVisibility(View.GONE);
                detailedReport.setVisibility(View.VISIBLE);
                sendToHq.setVisibility(View.VISIBLE);

                listView.setAdapter(summaryListAdapter);
            }
        });
    }

    private JSONObject ArrayListToJson(ArrayList<OvertimeSummaryObject> formList) {

        JSONObject formsListJsonObject = new JSONObject();
        JSONArray formsArray = new JSONArray();

        for (int index = 0; index < formList.size(); index++) {

            JSONObject singleForm = new JSONObject();
            try {
                singleForm.put("name", formList.get(index).getName());
                singleForm.put("startDate", sd);
                singleForm.put("endDate", ed);
                singleForm.put("interVerification", formList.get(index).getInterVerification());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            formsArray.put(singleForm);
        }

        try {
            formsListJsonObject.put("forms", formsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return formsListJsonObject;
    }

    private ArrayList<OvertimeFormObject> parseJson1(String formListJson) {

        ArrayList<OvertimeFormObject> formList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(formListJson);
            JSONArray forms = root.getJSONArray("forms");

            for (int index = 0; index < forms.length(); index++) {

                JSONObject currentForm = forms.getJSONObject(index);

                String date = currentForm.getString("date1");
                String platformNumber = currentForm.getString("pfno");
                String name = currentForm.getString("name");
                String shift = currentForm.getString("DutyType");
                String actualStart = currentForm.getString("actualstart");
                String actualEnd = currentForm.getString("actualend");
                String extraHours = currentForm.getString("extrahours");
                String reason = currentForm.getString("reason");
                Integer interverification = currentForm.getInt("inter_verification");
                Integer finalVerification = currentForm.getInt("final_verification");

                formList.add(new OvertimeFormObject(date, platformNumber, name, shift, actualStart,
                        actualEnd, extraHours, reason, interverification, finalVerification));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return formList;
    }

    private ArrayList<OvertimeSummaryObject> parseJson2(String formListJson) {

        ArrayList<OvertimeSummaryObject> summaryList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(formListJson);
            JSONArray forms = root.getJSONArray("summary");

            for (int index = 0; index < forms.length(); index++) {

                JSONObject currentForm = forms.getJSONObject(index);

                String name = currentForm.getString("name");
                String category = currentForm.getString("category");
                String totalEightHours = currentForm.getString("totalEightHours");
                String totalSixHours = currentForm.getString("totalSixHours");
                String totalRosteredHours = currentForm.getString("totalRosteredHours");
                String totalActualHours = currentForm.getString("totalActualHours");
                String extraDutyHours = currentForm.getString("extraDutyHours");
                String reason = currentForm.getString("reason");

                summaryList.add(new OvertimeSummaryObject(name, category, totalEightHours, totalSixHours, totalRosteredHours,
                        totalActualHours, extraDutyHours, reason, 0));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return summaryList;
    }

    private String sendToHq(String url) {

        URL finalInsertUrl = null;

        try {
            finalInsertUrl = new URL(url);
        } catch (MalformedURLException e) {
            Log.e(MainActivity.class.getName(), "Problem Building the URL", e);
        }

        String status = null;

        //Perform HTTP request to the URL and receive a JSON response back
        jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(finalInsertUrl);
            if (jsonResponse == null) {
                return null;
            }

        } catch (IOException e) {
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
            urlConnection.setReadTimeout(30000);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.connect();

            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(jsonToSend.toString());
            out.close();

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

        Log.i(OvertimeSummaryListActivity.class.getName(), "Status of mail   " + jsonResponse);
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
            String result = sendToHq(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            if (result == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "Error connecting to the Internet, Try again", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                v.setGravity(Gravity.CENTER);
                toast.show();
            } else {
                Toast.makeText(OvertimeSummaryListActivity.this, "Forms eMailed to Headquarters", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private String getOtFroms(String url) {

        //Create URI
        Uri baseUri = Uri.parse(url);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("startDate", sd);
        uriBuilder.appendQueryParameter("endDate", ed);
        uriBuilder.appendQueryParameter("isPreviousCycle", isPreviousCycle.toString());

        String insertUrl = uriBuilder.toString();
        URL finalInsertUrl = null;

        try {
            finalInsertUrl = new URL(insertUrl);
        } catch (MalformedURLException e) {
            Log.e(MainActivity.class.getName(), "Problem Building the URL", e);
        }

        //Perform HTTP request to the URL and receive a JSON response back
        jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest2(finalInsertUrl);
            if (jsonResponse == null) {
                return null;
            }
        } catch (IOException e) {
            Log.e(MainActivity.class.getName(), "Problem in Making HTTP request.", e);
        }

        return jsonResponse;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private String makeHttpRequest2(URL url) throws IOException {

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

    private class MainAsyncTask2 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = getOtFroms(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            updateFormProgressLayout.setVisibility(View.GONE);

            if (result == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "Error connecting to the Internet, Try again", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                v.setGravity(Gravity.CENTER);
                toast.show();
            } else {

                formList = parseJson1(result);
                summaryList = parseJson2(result);

                View emptyView = findViewById(R.id.empty_view);
                listView.setEmptyView(emptyView);

                formListAdapter = new OvertimeFormListAdapter(OvertimeSummaryListActivity.this, formList);
                summaryListAdapter = new OvertimeSummaryListAdapter(OvertimeSummaryListActivity.this, summaryList);

                listView.setAdapter(summaryListAdapter);

                formListLayout.setVisibility(View.VISIBLE);
                updateFormProgressLayout.setVisibility(View.GONE);
                detailedReport.setVisibility(View.VISIBLE);
                sendToHq.setVisibility(View.VISIBLE);

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
