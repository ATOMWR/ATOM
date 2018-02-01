package com.example.akav.atom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OvertimeFormListActivity extends AppCompatActivity {

    private ArrayList<OvertimeFormObject> formList;

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
    }

    private ArrayList<OvertimeFormObject> parseJson(String formListJson) {

        ArrayList<OvertimeFormObject> formList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(formListJson);
            JSONArray forms = root.getJSONArray("forms");

            for (int index = 0; index <forms.length(); index++){

                JSONObject currentForm = forms.getJSONObject(index);

                String date = currentForm.getString("date1");
                String platformNumber = currentForm.getString("pfno");
                String name = currentForm.getString("name");
                String shift = currentForm.getString("shift");
                String actualStart = currentForm.getString("actualstart");
                String actualEnd = currentForm.getString("actualend");
                String extraHours = currentForm.getString("extrahours");
                String reason = currentForm.getString("reason");

                formList.add(new OvertimeFormObject(date, platformNumber, name, shift,
                        actualStart, actualEnd, extraHours, reason));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return formList;
    }
}
