package com.example.akav.atom.travel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.nio.charset.Charset;
import java.util.ArrayList;

public class TravelFormListActiviy extends AppCompatActivity {

    private Button pullReport;
    private Button verifyList;

    private String startDateTimestamp;
    private String endDateTimeStamp;
    private String jsonResponse;
    private String buttonColor;
    private String sd, ed;

    private JSONObject jsonToSend;

    private LinearLayout updateFormProgressLayout;
    private RelativeLayout formListLayout;

    private TextView loadingFormTextView;

    private Integer isPreviousCycle;
    private Integer numberOfVerifiedForms;

    private ArrayList<TravelFormObject> formList;

    private final String ERROR_MESSAGE = "Can't Pull Report\nThere are UNVERIFIED forms in this list";

    private final String GET_TA_FORMS_URL = "http://atomwrapp.dx.am/getTaForms.php";
    private final String INTER_VERIFY_TA_FORMS = "http://atomwrapp.dx.am/interVerifyTaForms.php";
    private final String PULL_REPORT = "http://atomwrapp.dx.am/temp.php";
    private final String MAIL_REPORT = "http://atomwrapp.dx.am/mail.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_form_list_activiy);

        Intent intent = getIntent();
        startDateTimestamp = intent.getStringExtra("startDate");
        endDateTimeStamp = intent.getStringExtra("endDate");
        isPreviousCycle = intent.getIntExtra("isPreviousCycle", 0);
        sd = getIntent().getExtras().getString("strt");
        ed = getIntent().getExtras().getString("enddt");

        numberOfVerifiedForms = 0;


        if (isOnline()) {
            MainAsyncTask2 task2 = new MainAsyncTask2();
            task2.execute(GET_TA_FORMS_URL);
        } else {
            Toast.makeText(this, "NO Internet Connection, Try again", Toast.LENGTH_SHORT).show();
        }

        updateFormProgressLayout = (LinearLayout) findViewById(R.id.update_form_list_progress_layout);
        formListLayout = (RelativeLayout) findViewById(R.id.form_list_layout);
        loadingFormTextView = (TextView) findViewById(R.id.loading_forms_text);

        verifyList = (Button) findViewById(R.id.verify_list);
        pullReport = (Button) findViewById(R.id.pull_report);

        if (isPreviousCycle == 0) {
            pullReport.setVisibility(View.GONE);
        } else {
            pullReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO : Change Colors Later.
                    buttonColor = (String) pullReport.getTag();
                    if (buttonColor.equals("#B1BBF0")) {

                        Toast errorToast = new Toast(getApplicationContext());
                        errorToast.setGravity(Gravity.CENTER, 0, 0);

                        TextView errorText = new TextView(getApplicationContext());
                        errorText.setText(ERROR_MESSAGE);
                        errorText.setGravity(Gravity.CENTER);
                        errorText.setBackgroundColor(getResources().getColor(R.color.red));
                        errorText.setPadding(48, 48, 48, 48);
                        errorText.setTextColor(getResources().getColor(R.color.white));
                        errorText.setTextSize(16);

                        errorToast.setView(errorText);
                        errorToast.setDuration(Toast.LENGTH_LONG);
                        errorToast.show();

                    } else {
                        // TODO : Add code to pull report.
                        // msg();

                        loadingFormTextView.setText("Generating Report.");
                        formListLayout.setVisibility(View.GONE);
                        updateFormProgressLayout.setVisibility(View.VISIBLE);

                        if(isOnline()) {
                            MainAsyncTask3 task2 = new MainAsyncTask3();
                            task2.execute(MAIL_REPORT);
                        } else{
                            Toast.makeText(getApplicationContext(), "NO Internet Connection, Try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

        }

        verifyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonToSend = ArrayListToJson(formList);

                loadingFormTextView.setText("Updating Forms, Please Wait.");
                formListLayout.setVisibility(View.GONE);
                updateFormProgressLayout.setVisibility(View.VISIBLE);


                if (isOnline()) {
                    // Start the AsyncTask
                    MainAsyncTask task = new MainAsyncTask();
                    task.execute(INTER_VERIFY_TA_FORMS);
                } else {
                    Toast.makeText(TravelFormListActiviy.this, "NO Internet Connection, Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private JSONObject ArrayListToJson(ArrayList<TravelFormObject> formList) {

        JSONObject formsListJsonObject = new JSONObject();
        JSONArray formsArray = new JSONArray();

        for (int index = 0; index < formList.size(); index++) {

            if (formList.get(index).getInterVerification() == 0) {
                continue;
            }

            JSONObject singleForm = new JSONObject();
            try {
                singleForm.put("name", formList.get(index).getName());
                singleForm.put("date", formList.get(index).getDateOfTravel());
                singleForm.put("startTime", formList.get(index).getStartTime());
                singleForm.put("endTime", formList.get(index).getEndTime());
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

    private ArrayList<TravelFormObject> parseJson(String formListJson) {

        ArrayList<TravelFormObject> formList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(formListJson);
            JSONArray forms = root.getJSONArray("forms");

            for (int index = 0; index < forms.length(); index++) {

                JSONObject currentForm = forms.getJSONObject(index);

                String dateOfTravel = currentForm.getString("date1");
                String pfNumber = currentForm.getString("pfno");
                String name = currentForm.getString("name");
                String tspNumber = currentForm.getString("TSP_no");
                String startStation = currentForm.getString("start_station");
                String endStation = currentForm.getString("end_station");
                String startTime = currentForm.getString("start_time");
                String endTime = currentForm.getString("end_time");
                String extraHours = currentForm.getString("extrahours");
                String reason = currentForm.getString("reason");
                Integer percentageCategory = currentForm.getInt("percentage_category");
                Integer interVerification = currentForm.getInt("inter_verification");
                Integer finalVerification = currentForm.getInt("final_verification");

                String startDate = dateOfTravel;
                String endDate = dateOfTravel;

                formList.add(new TravelFormObject(dateOfTravel, pfNumber, name, tspNumber, startDate,
                        endDate, startTime, endTime, startStation, endStation, extraHours, reason,
                        percentageCategory, interVerification, finalVerification));

                if (interVerification == 1) {
                    numberOfVerifiedForms += 1;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return formList;
    }

    private String verifyTaForms(String url) {

        URL finalInsertUrl = null;

        try {
            finalInsertUrl = new URL(url);
        } catch (MalformedURLException e) {
            Log.e(MainActivity.class.getName(), "Problem Building the URL", e);
            ;
        }

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
            String result = verifyTaForms(urls[0]);
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
                Toast.makeText(TravelFormListActiviy.this, "Successfully updated TA forms", Toast.LENGTH_LONG).show();
                if (isPreviousCycle == 0) {
                    finish();
                } else {
                    //Reload activity
                    finish();
                    startActivity(getIntent());
                }
            }

        }
    }

    private String getTaForms(String url) {

        //Create URI
        Uri baseUri = Uri.parse(url);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("startDate",startDateTimestamp);
        uriBuilder.appendQueryParameter("endDate", endDateTimeStamp);
        uriBuilder.appendQueryParameter("fromAdmin", "1");
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
            String result = getTaForms(urls[0]);
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
                formList = parseJson(result);

                TravelFormListAdapter formListAdapter = new TravelFormListAdapter(TravelFormListActiviy.this, formList);

                ListView listView = (ListView) findViewById(R.id.list);
                listView.setAdapter(formListAdapter);

                checkPullReportStatus();

                formListLayout.setVisibility(View.VISIBLE);
            }

        }
    }


    // Background Task to pull report.
    private class MainAsyncTask3 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = pullReportFromDatabase(urls[0]);
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
                Toast.makeText(TravelFormListActiviy.this, "Report Generated.", Toast.LENGTH_LONG).show();
                openWebsite(result);
            }

            /*Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(result));
            startActivity(i);

            finish();*/
        }
    }

    void openWebsite(final String result) {

        updateFormProgressLayout.setVisibility(View.GONE);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TravelFormListActiviy.this);

        alertDialogBuilder.setMessage("Report Generated. View Report?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        updateFormProgressLayout.setVisibility(View.GONE);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(result));
                        startActivity(i);
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private String pullReportFromDatabase(String url) {

        //Create URI
        Uri baseUri = Uri.parse(url);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("startDate", startDateTimestamp);
        uriBuilder.appendQueryParameter("endDate", endDateTimeStamp);

        String insertUrl = uriBuilder.toString();
        URL finalInsertUrl = null;

        try {
            finalInsertUrl = new URL(insertUrl);
        } catch (MalformedURLException e) {
            Log.e(MainActivity.class.getName(), "Problem Building the URL", e);
        }

        //Perform HTTP request to the URL
        jsonResponse = null;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) finalInsertUrl.openConnection();
            urlConnection.setReadTimeout(30000);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);

        } catch (SocketTimeoutException s) {
            s.printStackTrace();
            return null;
        } catch (IOException e) {
            Log.e(MainActivity.class.getName(), "Problem Connecting to Server.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }


        Integer status = 0;

        try {
            JSONObject root = new JSONObject(jsonResponse);
            status = root.getInt("Status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create URI to view report
        Uri baseUri2 = Uri.parse(PULL_REPORT);
        Uri.Builder uriBuilder2 = baseUri2.buildUpon();

        uriBuilder2.appendQueryParameter("sdate", sd);
        uriBuilder2.appendQueryParameter("edate", ed);

        String viewReportUrl = uriBuilder2.toString();
        URL finalViewReport = null;

        try {
            finalViewReport = new URL(viewReportUrl);
        } catch (MalformedURLException e) {
            Log.e(MainActivity.class.getName(), "Problem Building the URL", e);
        }

        if (status == 1) {
            return finalViewReport.toString();
        } else {
            return null;
        }
    }


    private void checkPullReportStatus() {
        if (canPullReport()) {
            // TODO : Change Colors later.
            pullReport.setTag("#3F51B5");
            pullReport.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private Boolean canPullReport() {
        if (numberOfVerifiedForms.intValue() == formList.size()) {
            return true;
        } else {
            return false;
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
