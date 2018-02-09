package com.example.akav.atom;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import java.util.Objects;

public class OvertimeFormListActivity extends AppCompatActivity {

    private Button goBack;
    private Button verifyList;

    private JSONObject jsonToSend;

    private String jsonResponse;

    private LinearLayout updateFormProgressLayout;
    private RelativeLayout formListLayout;

    private ArrayList<OvertimeFormObject> formList;

    private final String INTER_VERIFY_OT_FORMS = "http://atomwrapp.dx.am/interVerifyOtForms.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime_form_list);

        Intent intent = getIntent();
        String formListJson = intent.getStringExtra("OtFormList");

        formList = parseJson(formListJson);

        OvertimeFormListAdapter formListAdapter = new OvertimeFormListAdapter(this, formList);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(formListAdapter);

        updateFormProgressLayout = (LinearLayout) findViewById(R.id.update_form_list_progress_layout);
        formListLayout = (RelativeLayout) findViewById(R.id.form_list_layout);

        verifyList = (Button) findViewById(R.id.verify_list);
        goBack = (Button) findViewById(R.id.undo_and_go_back);

        verifyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonToSend = ArrayListToJson(formList);

                formListLayout.setVisibility(View.GONE);
                updateFormProgressLayout.setVisibility(View.VISIBLE);

                // Start the AsyncTask
                MainAsyncTask task = new MainAsyncTask();
                task.execute(INTER_VERIFY_OT_FORMS);
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

            for (int index = 0; index < forms.length(); index++){

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
                Integer finalVerificatin = currentForm.getInt("final_verification");

                formList.add(new OvertimeFormObject(date, platformNumber, name, shift, actualStart,
                        actualEnd, extraHours, reason, interverification, finalVerificatin));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return formList;
    }

    private String verifyOtForms(String url){

        URL finalInsertUrl = null;

        try {
            finalInsertUrl = new URL(url);
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
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json");
            //urlConnection.setRequestProperty("Host", "android.schoolportal.gr");

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
            String result = verifyOtForms(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(OvertimeFormListActivity.this, "Successfully updated OT forms", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
