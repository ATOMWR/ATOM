package com.example.akav.atom.travel;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akav.atom.MainActivity;
import com.example.akav.atom.R;
import com.example.akav.atom.overtime.OvertimeFormListActivity;
import com.example.akav.atom.overtime.OvertimeFormListAdapter;
import com.example.akav.atom.overtime.OvertimeFormObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

public class TravelFormsActivity extends AppCompatActivity {

    private FloatingActionButton addNew;

    private ProgressBar progressBar;

    private TextView loadingTextView;

    private String userID;
    private String currentDate;

    private ArrayList<TravelFormObject> formList;

    private final String GET_TA_FORMS = "http://atomwrapp.dx.am/getTaForms.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_forms);

        Intent intent = getIntent();
        currentDate = intent.getStringExtra("currdate");
        userID = intent.getStringExtra("userID");


        if (isOnline()) {
            // Start the AsyncTask
            MainAsyncTask task = new MainAsyncTask();
            task.execute(GET_TA_FORMS);
        } else {
            Toast.makeText(this, "NO Internet Connection, Try again", Toast.LENGTH_SHORT).show();
        }

        addNew = (FloatingActionButton) findViewById(R.id.fill_new_ta_form);
        progressBar = (ProgressBar) findViewById(R.id.ta_forms_progress_bar);
        loadingTextView = (TextView) findViewById(R.id.loading_text_view);

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoTaForm = new Intent(TravelFormsActivity.this, TravelForm.class);
                gotoTaForm.putExtra("userID", userID);
                gotoTaForm.putExtra("date", currentDate);
                startActivity(gotoTaForm);
            }
        });
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

                // String startDate = currentForm.getString("start_date");
                // String endDate = currentForm.getString("end_date");

                String startDate = null;
                String endDate = null;

                String startTime = currentForm.getString("start_time");
                String endTime = currentForm.getString("end_time");
                String startStation = currentForm.getString("start_station");
                String endStation = currentForm.getString("end_station");
                String extraHours = currentForm.getString("extrahours");
                String reason = currentForm.getString("reason");

                Integer percentageCategory = currentForm.getInt("percentage_category");
                Integer interVerification = currentForm.getInt("inter_verification");
                Integer finalVerification = currentForm.getInt("final_verification");


                formList.add(new TravelFormObject(dateOfTravel, pfNumber, name, tspNumber, startDate,
                        endDate, startTime, endTime, startStation, endStation, extraHours, reason,
                        percentageCategory, interVerification, finalVerification));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return formList;
    }

    private String getTaForms(String url) {

        //Create URI
        Uri baseUri = Uri.parse(url);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("userID", userID);
        uriBuilder.appendQueryParameter("date", currentDate);
        uriBuilder.appendQueryParameter("fromAdmin", "0");

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
            String result = getTaForms(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            progressBar.setVisibility(View.GONE);

            if (result == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "Error connecting to the Internet, Try again", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                v.setGravity(Gravity.CENTER);
                toast.show();
            } else {
                formList = parseJson(result);

                if (formList.size() == 0) {
                    loadingTextView.setText("No TA Forms for this date.");
                } else {
                    loadingTextView.setVisibility(View.GONE);
                    TravelFormUserListAdapter formListAdapter = new TravelFormUserListAdapter(TravelFormsActivity.this, formList);

                    ListView listView = (ListView) findViewById(R.id.ta_list);
                    listView.setVisibility(View.VISIBLE);
                    listView.setAdapter(formListAdapter);
                }

                for (TravelFormObject travelFormObject : formList) {
                    Log.i(TravelFormsActivity.class.getName(), "Date of Travel " + travelFormObject.getDateOfTravel());
                    Log.i(TravelFormsActivity.class.getName(), "Start Station " + travelFormObject.getStartStation());
                    Log.i(TravelFormsActivity.class.getName(), "End Station " + travelFormObject.getEndStation());
                    Log.i(TravelFormsActivity.class.getName(), "Extra Hours " + travelFormObject.getExtraHours());
                }

                addNew.setVisibility(View.VISIBLE);
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
