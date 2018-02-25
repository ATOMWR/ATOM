package com.example.akav.atom.overtime;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class OvertimeFormListActivity extends AppCompatActivity {

    private Button pullReport;
    private Button verifyList;

    private String startDateTimestamp;
    private String endDateTimeStamp;
    private String jsonResponse;
    private String buttonColor;

    private JSONObject jsonToSend;

    private LinearLayout updateFormProgressLayout;
    private RelativeLayout formListLayout;

    private TextView loadingFormTextView;

    private Integer isPreviousCycle;
    private Integer numberOfVerifiedForms;

    private ArrayList<OvertimeFormObject> formList;

    private final String ERROR_MESSAGE = "Can't Pull Report\nThere are UNVERIFIED forms in this list";
    private final String GET_OT_FORMS_URL = "http://atomwrapp.dx.am/getOtForms.php";
    private final String INTER_VERIFY_OT_FORMS = "http://atomwrapp.dx.am/interVerifyOtForms.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime_form_list);

        Intent intent = getIntent();
        startDateTimestamp = intent.getStringExtra("startDate");
        endDateTimeStamp = intent.getStringExtra("endDate");
        isPreviousCycle = intent.getIntExtra("isPreviousCycle", 0);

        numberOfVerifiedForms = 0;

        MainAsyncTask2 task2 = new MainAsyncTask2();
        task2.execute(GET_OT_FORMS_URL);

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

                // Start the AsyncTask
                MainAsyncTask task = new MainAsyncTask();
                task.execute(INTER_VERIFY_OT_FORMS);
            }
        });

    }

    private JSONObject ArrayListToJson(ArrayList<OvertimeFormObject> formList) {

        JSONObject formsListJsonObject = new JSONObject();
        JSONArray formsArray = new JSONArray();

        for (int index = 0; index < formList.size(); index++) {

            if (formList.get(index).getInterVerification() == 0) {
                continue;
            }

            JSONObject singleForm = new JSONObject();
            try {
                singleForm.put("name", formList.get(index).getName());
                singleForm.put("date", formList.get(index).getDate());
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

    private ArrayList<OvertimeFormObject> parseJson(String formListJson) {

        ArrayList<OvertimeFormObject> formList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(formListJson);
            JSONArray forms = root.getJSONArray("forms");

            for (int index = 0; index < forms.length(); index++) {

                JSONObject currentForm = forms.getJSONObject(index);

                String date = currentForm.getString("date1");
                String platformNumber = currentForm.getString("pfno");
                String name = currentForm.getString("name");
                String shift = currentForm.getString("shift");
                String actualStart = currentForm.getString("actualstart");
                String actualEnd = currentForm.getString("actualend");
                String extraHours = currentForm.getString("extrahours");
                String reason = currentForm.getString("reason");
                Integer interverification = currentForm.getInt("inter_verification");
                Integer finalVerification = currentForm.getInt("final_verification");

                formList.add(new OvertimeFormObject(date, platformNumber, name, shift, actualStart,
                        actualEnd, extraHours, reason, interverification, finalVerification));

                if (interverification == 1) {
                    numberOfVerifiedForms += 1;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return formList;
    }

    private String verifyOtForms(String url) {

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
        } catch (IOException e) {
            Log.e(MainActivity.class.getName(), "Problem in Making HTTP request.", e);
        }

        return null;
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
            String result = verifyOtForms(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(OvertimeFormListActivity.this, "Successfully updated OT forms", Toast.LENGTH_LONG).show();
            if (isPreviousCycle == 0) {
                finish();
            } else {
                //Reload activity
                finish();
                startActivity(getIntent());
            }
        }
    }

    private String getOtFroms(String url) {

        //Create URI
        Uri baseUri = Uri.parse(url);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("startDate", startDateTimestamp);
        uriBuilder.appendQueryParameter("endDate", endDateTimeStamp);
        uriBuilder.appendQueryParameter("isPreviousCycle", isPreviousCycle.toString());

        String insertUrl = uriBuilder.toString();
        URL finalInsertUrl = null;

        try {
            finalInsertUrl = new URL(insertUrl);
        } catch (MalformedURLException e) {
            Log.e(MainActivity.class.getName(), "Problem Building the URL", e);
            ;
        }

        //Perform HTTP request to the URL and receive a JSON response back
        jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest2(finalInsertUrl);
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
            formList = parseJson(result);

            OvertimeFormListAdapter formListAdapter = new OvertimeFormListAdapter(OvertimeFormListActivity.this, formList);

            ListView listView = (ListView) findViewById(R.id.list);
            listView.setAdapter(formListAdapter);

            checkPullReportStatus();

            updateFormProgressLayout.setVisibility(View.GONE);
            formListLayout.setVisibility(View.VISIBLE);
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
}